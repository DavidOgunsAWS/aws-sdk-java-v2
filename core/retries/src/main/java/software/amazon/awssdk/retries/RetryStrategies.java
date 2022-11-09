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
import software.amazon.awssdk.retriesapi.RetryStrategy;

/**
 * Built-in implementations of the {@link RetryStrategy} interface.
 */
@SdkPublicApi
final class RetryStrategies {
    private RetryStrategies() {
    }

    /**
     * Create a new builder for a {@link StandardRetryStrategy}.
     *
     * <p>Example Usage
     * <pre>
     * StandardRetryStrategy retryStrategy =
     *     RetryStrategies.standardStrategyBuilder()
     *                    .retryOnExceptionInstanceOf(IllegalArgumentException.class)
     *                    .retryOnExceptionInstanceOf(IllegalStateException.class)
     *                    .build();
     * </pre>
     */
    static StandardRetryStrategy.Builder standardStrategyBuilder() {
        return StandardRetryStrategyImpl.builder();
    }

    /**
     * Create a new builder for a {@link AdaptiveRetryStrategy}.
     *
     * <p>Example Usage
     * <pre>
     * StandardRetryStrategy retryStrategy =
     *     RetryStrategies.adaptiveStrategyBuilder()
     *                    .retryOnExceptionInstanceOf(IllegalArgumentException.class)
     *                    .retryOnExceptionInstanceOf(IllegalStateException.class)
     *                    .build();
     * </pre>
     */
    static AdaptiveRetryStrategy.Builder adaptiveStrategyBuilder() {
        // TODO: implement
        return null;
    }
}
