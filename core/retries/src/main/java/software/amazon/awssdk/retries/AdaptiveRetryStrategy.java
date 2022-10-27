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
import software.amazon.awssdk.retriesapi.RetryStrategy;

/**
 * The adaptive retry strategy is a {@link RetryStrategy} when executing against a very resource-constrained
 * set of resources.
 *
 * Unlike {@link StandardRetryStrategy}, care should be taken when using this strategy. Specifically, it should be used:
 * <ol>
 * <li>When the availability of downstream resources are mostly affected by callers that are also using
 * the {@link AdaptiveRetryStrategy}.
 * <li>The scope (either the whole strategy or the {@link AcquireInitialTokenRequest#scope}) of the strategy is constrained
 * to target "resource", so that availability issues in one resource cannot delay other, unrelated resource's availibility.
 *
 * The adaptive retry strategy by default:
 * <ol>
 *     <li>Retries on the conditions configured in the {@link Builder}.
 *     <li>Retries 2 times (3 total attempts). Adjust with {@link Builder#maxRetries}
 *     <li>Uses a dynamic backoff delay based on load currently perceived against the downstream resource
 *     <li>Circuit breaking (disabling retries) in the event of high downstream failures within an individual scope.
 *     Circuit breaking may prevent a first attempt in outage scenarios to protect the downstream service.
 * </ol>
 *
 * @see StandardRetryStrategy
 */

@SdkPublicApi
@ThreadSafe
interface AdaptiveRetryStrategy extends RetryStrategy {
    /**
     * Create a new {@link AdaptiveRetryStrategy.Builder}.
     *
     * <p>Example Usage
     * <pre>
     * AdaptiveRetryStrategy retryStrategy =
     *     AdaptiveRetryStrategy.builder()
     *                          .retryOnExceptionInstanceOf(IllegalArgumentException.class)
     *                          .retryOnExceptionInstanceOf(IllegalStateException.class)
     *                          .build();
     * </pre>
     */
    static AdaptiveRetryStrategy.Builder builder() {
        return null;
    }

    @Override
    RetryStrategy.Builder<? extends AdaptiveRetryStrategy> toBuilder();

    interface Builder extends RetryStrategy.Builder<Builder> {
        @Override
        AdaptiveRetryStrategy build();
    }
}