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
import software.amazon.awssdk.retriesapi.AcquireInitialTokenRequest;
import software.amazon.awssdk.retriesapi.AcquireInitialTokenResponse;
import software.amazon.awssdk.retriesapi.BackoffStrategy;
import software.amazon.awssdk.retriesapi.RecordSuccessRequest;
import software.amazon.awssdk.retriesapi.RecordSuccessResponse;
import software.amazon.awssdk.retriesapi.RefreshRetryTokenRequest;
import software.amazon.awssdk.retriesapi.RefreshRetryTokenResponse;
import software.amazon.awssdk.retriesapi.RetryStrategy;
import software.amazon.awssdk.utils.Logger;

@SdkProtectedApi
class DefaultStandardRetryStrategy implements StandardRetryStrategy {
    private static final Logger LOGGER = Logger.loggerFor(DefaultStandardRetryStrategy.class);
    private final int maxAttempts;
    private final Predicate<? extends Throwable> retryPredicate;
    private final BackoffStrategy backoffStrategy;
    private final boolean circuitBreakerEnabled;

    protected DefaultStandardRetryStrategy(Builder builder) {
        //TODO: non-null validation
        this.maxAttempts = builder.maxAttempts;
        this.retryPredicate = builder.retryPredicate;
        this.backoffStrategy = builder.backoffStrategy;
        this.circuitBreakerEnabled = builder.circuitBreakerEnabled;
    }

    @Override
    public AcquireInitialTokenResponse acquireInitialToken(AcquireInitialTokenRequest request) {
        //TODO: spotbug violation clearing
        LOGGER.info(() -> String.format("Max attempts: %d", maxAttempts));
        LOGGER.info(() -> String.format("RetryPredicate: %s", retryPredicate.toString()));
        LOGGER.info(() -> String.format("BackoffStrategy: %s", backoffStrategy.toString()));
        LOGGER.info(() -> String.format("CircuitBreaker enabled?: %b", circuitBreakerEnabled));
        return null;
    }

    @Override
    public RefreshRetryTokenResponse refreshRetryToken(RefreshRetryTokenRequest request) {
        return null;
    }

    @Override
    public RecordSuccessResponse recordSuccess(RecordSuccessRequest request) {
        return null;
    }

    @Override
    public RetryStrategy.Builder<? extends DefaultStandardRetryStrategy> toBuilder() {
        return null;
    }

    public static class Builder implements RetryStrategy.Builder<DefaultStandardRetryStrategy.Builder> {
        private int maxAttempts = 5; // TODO: find appropriate source for this
        private Predicate<? extends Throwable> retryPredicate;
        private BackoffStrategy backoffStrategy;
        private boolean circuitBreakerEnabled;

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
        public RetryStrategy build() {
            return new DefaultStandardRetryStrategy(this);
        }
    }
}
