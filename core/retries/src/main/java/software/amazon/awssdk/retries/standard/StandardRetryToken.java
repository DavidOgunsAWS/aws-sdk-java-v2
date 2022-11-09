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

package software.amazon.awssdk.retries.standard;

import software.amazon.awssdk.annotations.Immutable;
import software.amazon.awssdk.annotations.SdkProtectedApi;
import software.amazon.awssdk.core.SdkRequest;
import software.amazon.awssdk.core.interceptor.ExecutionAttributes;
import software.amazon.awssdk.retries.StandardRetryStrategyImpl;
import software.amazon.awssdk.utils.builder.CopyableBuilder;
import software.amazon.awssdk.utils.builder.ToCopyableBuilder;

/**
 * Contains useful information about a failed request that can be used to make retry and backoff decisions. See {@link
 * StandardRetryStrategyImpl}.
 */
@Immutable
@SdkProtectedApi
public final class StandardRetryToken implements
         ToCopyableBuilder<StandardRetryToken.Builder, StandardRetryToken> {

    private final SdkRequest originalRequest;
    private final ExecutionAttributes executionAttributes;
    private final int retriesAttempted;

    private StandardRetryToken(Builder builder) {
        this.originalRequest = builder.originalRequest;
        this.executionAttributes = builder.executionAttributes;
        this.retriesAttempted = builder.retriesAttempted;
    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * @return The original request passed to the client method for an operation.
     */
    public SdkRequest originalRequest() {
        return this.originalRequest;
    }

    /**
     * @return Mutable execution context.
     */
    public ExecutionAttributes executionAttributes() {
        return this.executionAttributes;
    }

    /**
     * @return Number of retries attempted thus far.
     */
    public int retriesAttempted() {
        return this.retriesAttempted;
    }

    /**
     * @return The total number of requests made thus far.
     */
    public int totalRequests() {
        return this.retriesAttempted + 1;
    }

    @Override
    public Builder toBuilder() {
        return new Builder(this);
    }

    static final class Builder implements CopyableBuilder<Builder, StandardRetryToken> {
        private SdkRequest originalRequest;
        private ExecutionAttributes executionAttributes;
        private int retriesAttempted;

        private Builder() {
        }

        private Builder(StandardRetryToken copy) {
            this.originalRequest = copy.originalRequest;
            this.executionAttributes = copy.executionAttributes;
            this.retriesAttempted = copy.retriesAttempted;
        }

        public Builder originalRequest(SdkRequest originalRequest) {
            this.originalRequest = originalRequest;
            return this;
        }

        public Builder executionAttributes(ExecutionAttributes executionAttributes) {
            this.executionAttributes = executionAttributes;
            return this;
        }

        public Builder retriesAttempted(int retriesAttempted) {
            this.retriesAttempted = retriesAttempted;
            return this;
        }

        @Override
        public StandardRetryToken build() {
            return new StandardRetryToken(this);
        }
    }
}
