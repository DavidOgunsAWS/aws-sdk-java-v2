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

import software.amazon.awssdk.annotations.SdkPublicApi;
import software.amazon.awssdk.annotations.ThreadSafe;
import software.amazon.awssdk.retriesapi.BackoffStrategy;
import software.amazon.awssdk.retriesapi.RetryStrategy;

/**
 * The standard retry strategy is the recommended {@link RetryStrategy} for normal use-cases.
 *
 * Unlike {@link AdaptiveRetryStrategy}, the standard strategy is generally useful across all retry use-cases.
 *
 * The standard retry strategy by default:
 * <ol>
 *     <li>Retries on the conditions configured in the {@link software.amazon.awssdk.retriesapi.RetryStrategy.Builder}.
 *     <li>Retries 2 times (3 total attempts). Adjust with
 *     {@link software.amazon.awssdk.retriesapi.RetryStrategy.Builder#maxRetries}
 *     <li>Uses the {@link BackoffStrategy#exponentialDelay} backoff strategy, with a base delay of
 *     1 second and max delay of 20 seconds. Adjust with {@link Builder#backoffStrategy}
 *     <li>Circuit breaking (disabling retries) in the event of high downstream failures across the scope of
 *     the strategy. The circuit breaking will never prevent a successful first attempt. Adjust with
 *     {@link Builder#circuitBreakerEnabled}.
 * </ol>
 *
 * @see AdaptiveRetryStrategy
 */
@SdkPublicApi
@ThreadSafe
public interface StandardRetryStrategy extends RetryStrategy {
    /**
     * Create a new {@link StandardRetryStrategy.Builder}.
     *
     * <p>Example Usage
     * <pre>
     * StandardRetryStrategy retryStrategy =
     *     StandardRetryStrategy.builder()
     *                          .retryOnExceptionInstanceOf(IllegalArgumentException.class)
     *                          .retryOnExceptionInstanceOf(IllegalStateException.class)
     *                          .build();
     * </pre>
     */
    static Builder builder() {
        return RetryStrategies.standardStrategyBuilder();
    }

    interface Builder extends RetryStrategy.Builder<Builder> {
        /**
         * Configure the backoff strategy used by this executor.
         *
         * <p>By default, this uses jittered exponential backoff.
         */
        Builder backoffStrategy(BackoffStrategy backoffStrategy);

        /**
         * Whether circuit breaking is enabled for this executor.
         *
         * <p>The exact circuit breaking used depends upon the configured {@link RetryMode}, but all
         * retry mode circuit breakers will prevent attempts (even below the {@link #maxAttempts(int)})
         * if a large number of failures are observed by this executor.
         *
         * <p>Note: The circuit breaker scope is local to the created {@link software.amazon.awssdk.retriesapi.RetryStrategy},
         * and will therefore not be effective unless the {@link software.amazon.awssdk.retriesapi.RetryStrategy} is used for
         * more than one call. It's recommended that a {@link software.amazon.awssdk.retriesapi.RetryStrategy} be reused for
         * all calls to a single unreliable resource. It's also recommended that separate
         * {@link software.amazon.awssdk.api.retries.RetryStrategy}s be used for calls to unrelated resources.
         *
         * <p>By default, this is {@code true}.
         */
        Builder circuitBreakerEnabled(Boolean circuitBreakerEnabled);

        @Override
        StandardRetryStrategy build();
    }
}