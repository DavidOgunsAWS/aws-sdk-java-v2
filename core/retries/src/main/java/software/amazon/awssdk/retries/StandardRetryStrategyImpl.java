/*
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package software.amazon.awssdk.retries;

import java.util.function.Predicate;
import software.amazon.awssdk.annotations.SdkProtectedApi;
import software.amazon.awssdk.retries.standard.StandardRetryInitialTokenResponse;
import software.amazon.awssdk.retries.standard.StandardRetryToken;
import software.amazon.awssdk.retriesapi.AcquireInitialTokenRequest;
import software.amazon.awssdk.retriesapi.AcquireInitialTokenResponse;
import software.amazon.awssdk.retriesapi.BackoffStrategy;
import software.amazon.awssdk.retriesapi.RecordSuccessRequest;
import software.amazon.awssdk.retriesapi.RecordSuccessResponse;
import software.amazon.awssdk.retriesapi.RefreshRetryTokenRequest;
import software.amazon.awssdk.retriesapi.RefreshRetryTokenResponse;
import software.amazon.awssdk.retriesapi.RetryStrategy;
import software.amazon.awssdk.retriesapi.TokenAcquisitionFailedException;
import software.amazon.awssdk.utils.Logger;

/**
 * TODO: javadoc needed
 */
@SdkProtectedApi
public class StandardRetryStrategyImpl implements StandardRetryStrategy {
    private static final Logger LOGGER = Logger.loggerFor(StandardRetryStrategyImpl.class);
    private final int maxAttempts;
    private final Predicate<Throwable> retryPredicate;
    private final BackoffStrategy backoffStrategy;
    private final boolean circuitBreakerEnabled;

    protected StandardRetryStrategyImpl(Builder builder) {
        //TODO: non-null parameter validation
        this.maxAttempts = builder.maxAttempts;
        this.retryPredicate = builder.retryPredicate;
        this.backoffStrategy = builder.backoffStrategy;
        this.circuitBreakerEnabled = builder.circuitBreakerEnabled;
    }

    @Override
    public AcquireInitialTokenResponse acquireInitialToken(AcquireInitialTokenRequest request) {
        StandardRetryInitialTokenResponse.Builder builder = StandardRetryInitialTokenResponse.builder()
                .retryToken(StandardRetryToken.builder()
                        .retriesAttempted(0).build());

        LOGGER.info(() -> String.format("Max attempts: %d", maxAttempts));
        LOGGER.info(() -> String.format("RetryPredicate: %s", retryPredicate.toString()));
        LOGGER.info(() -> String.format("BackoffStrategy: %s", backoffStrategy.toString()));
        LOGGER.info(() -> String.format("CircuitBreaker enabled?: %b", circuitBreakerEnabled));

        return builder.build();
    }

    @Override
    public RefreshRetryTokenResponse refreshRetryToken(RefreshRetryTokenRequest request) {
        if (!(request.token() instanceof StandardRetryToken)) {
            //TODO: or is this an IllegalArgumentException?
            throw new IllegalArgumentException("Incompatiable RetryToken supplied");
        }
        StandardRetryToken token = (StandardRetryToken) request.token();

        //verify if retryable exception occurred at all
        boolean retryableFailure = retryPredicate.test(request.failure());
        if (!retryableFailure) {
            throw new TokenAcquisitionFailedException("Non-retryable exception occurred");
        }

        //verify if we exhausted attempts first
        boolean exhaustedRetries = token.retriesAttempted() < maxAttempts;
        if (exhaustedRetries) {
            throw new TokenAcquisitionFailedException("Retries exhausted");
        }

        //verify if the scope "bucket" has enough resources to perform a retry now
        if (circuitBreakerEnabled && !canRetryForScope(token)) {
            throw new TokenAcquisitionFailedException("Retry unavailable or scope: " + token.scope());
        }

        StandardRetryToken.Builder newTokenBuilder = StandardRetryToken.builder()
            .retriesAttempted(token.retriesAttempted() + 1);



        return null;
    }

    private boolean canRetryForScope(StandardRetryToken token) {
        return true; //TODO: implement bucket resource
    }

    private void recordSuccessInScope(StandardRetryToken token) {

    }

    @Override
    public RecordSuccessResponse recordSuccess(RecordSuccessRequest request) {

        return null;
    }

    @Override
    // Builder<? extends RetryStrategy> toBuilder();
    public RetryStrategy.Builder<StandardRetryStrategy.Builder> toBuilder() {
        return new Builder(this);
    }

    public static Builder builder() {
        return new Builder();
    }

    // interface Builder extends RetryStrategy.Builder<StandardRetryStrategy.Builder> {
    public static class Builder implements StandardRetryStrategy.Builder {
        private int maxAttempts;
        private Predicate<Throwable> retryPredicate;
        private BackoffStrategy backoffStrategy;
        private boolean circuitBreakerEnabled;

        private Builder() {
        }

        private Builder(StandardRetryStrategyImpl from) {
            this.maxAttempts = from.maxAttempts;
            this.retryPredicate = from.retryPredicate;
            this.backoffStrategy = from.backoffStrategy;
            this.circuitBreakerEnabled = from.circuitBreakerEnabled;
        }

        /**
         * Configure the backoff strategy used by this executor.
         *
         * <p>By default, this uses jittered exponential backoff.
         * @return
         */
        public Builder backoffStrategy(BackoffStrategy backoffStrategy) {
            this.backoffStrategy = backoffStrategy;
            return this;
        }

        /**
         * Whether circuit breaking is enabled for this executor.
         *
         * <p>The exact circuit breaking used depends upon the configured {@link RetryMode}, but all
         * retry mode circuit breakers will prevent attempts (even below the {@link #maxAttempts(int)}) if a large number of
         * failures are observed by this executor.
         *
         * <p>Note: The circuit breaker scope is local to the created {@link software.amazon.awssdk.retriesapi.RetryStrategy},
         * and will therefore not be effective unless the {@link software.amazon.awssdk.retriesapi.RetryStrategy} is used for
         * more than one call. It's recommended that a {@link software.amazon.awssdk.retriesapi.RetryStrategy} be reused for all
         * calls to a single unreliable resource. It's also recommended that separate {@link
         * software.amazon.awssdk.api.retries.RetryStrategy}s be used for calls to unrelated resources.
         *
         * <p>By default, this is {@code true}.
         */
        public Builder circuitBreakerEnabled(Boolean circuitBreakerEnabled) {
            this.circuitBreakerEnabled = circuitBreakerEnabled;
            return this;
        }

        @Override
        public Builder retryOnException(Predicate<Throwable> shouldRetry) {
            retryPredicate = (t) -> {
                // TODO: wrap this to catch exception if predicate fails
                return shouldRetry.test(t);
            };
            return this;
        }

        @Override
        public Builder retryOnException(Class<? extends Throwable> throwableClass) {
            retryPredicate = (Throwable t) -> {
                return throwableClass.getClass().equals(t.getClass());
            };
            return this;
        }

        @Override
        public Builder retryOnExceptionInstanceOf(Class<? extends Throwable> throwableClass) {
            retryPredicate = (Throwable t) -> {
                return throwableClass.isInstance(t);
            };
            return this;
        }

        @Override
        public Builder retryOnExceptionOrCause(Class<? extends Throwable> throwableClass) {
            retryPredicate = (Throwable t) -> {
                if (throwableClass.getClass().equals(t.getClass())) {
                    return true;
                }
                Throwable cause = t.getCause();
                while (cause != null) {
                    // TODO: instance of or assignable from?
                    if (throwableClass.getClass().equals(cause.getClass())) {
                        return true;
                    }
                    cause = cause.getCause();
                }
                return false;
            };
            return this;
        }

        @Override
        public Builder retryOnExceptionOrCauseInstanceOf(Class<? extends Throwable> throwableClass) {
            retryPredicate = (Throwable t) -> {
                if (throwableClass.isInstance(t)) {
                    return true;
                }
                Throwable cause = t.getCause();
                while (cause != null) {
                    // TODO: instance of or assignable from?
                    if (throwableClass.isInstance(cause)) {
                        return true;
                    }
                    cause = cause.getCause();
                }
                return false;
            };
            return this;
        }

        @Override
        public Builder retryOnRootCause(Class<? extends Throwable> throwableClass) {
            retryPredicate = (Throwable t) -> {
                Throwable rootCause = t;
                while (rootCause.getCause() != null) {
                    rootCause = rootCause.getCause();
                }
                return rootCause.getClass().equals(throwableClass);
            };
            return this;
        }

        @Override
        public Builder retryOnRootCauseInstanceOf(Class<? extends Throwable> throwable) {
            retryPredicate = (Throwable t) -> {
                Throwable rootCause = t;
                while (rootCause.getCause() != null) {
                    rootCause = rootCause.getCause();
                }
                return false;
            };
            return this;
        }

        @Override
        public Builder maxAttempts(int maxAttempts) {
            this.maxAttempts = maxAttempts;
            return this;
        }

        @Override
        public StandardRetryStrategyImpl build() {
            return new StandardRetryStrategyImpl(this);
        }
    }
}
