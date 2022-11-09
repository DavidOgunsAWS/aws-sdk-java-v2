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

import java.time.Duration;
import software.amazon.awssdk.annotations.SdkProtectedApi;
import software.amazon.awssdk.retriesapi.AcquireInitialTokenResponse;
import software.amazon.awssdk.retriesapi.RetryToken;
import software.amazon.awssdk.utils.builder.CopyableBuilder;
import software.amazon.awssdk.utils.builder.ToCopyableBuilder;

@SdkProtectedApi
public class StandardRetryInitialTokenResponse implements
        AcquireInitialTokenResponse,
        ToCopyableBuilder<StandardRetryInitialTokenResponse.Builder, StandardRetryInitialTokenResponse> {
    private final RetryToken retryToken;
    private final Duration delay;

    private StandardRetryInitialTokenResponse(Builder builder) {
        this.retryToken = builder.retryToken;
        this.delay = builder.delay;
    }

    @Override
    public RetryToken token() {
        return retryToken;
    }

    @Override
    public Duration delay() {
        return delay;
    }

    @Override
    public Builder toBuilder() {
        return new Builder(this);
    }

    public static Builder builder() {
        return new StandardRetryInitialTokenResponse.Builder();
    }

    public static class Builder implements CopyableBuilder<Builder, StandardRetryInitialTokenResponse> {
        private RetryToken retryToken;
        private Duration delay;

        private Builder() {
            retryToken = null; //TODO: fix
            delay = Duration.ofMillis(0);
        }

        private Builder(StandardRetryInitialTokenResponse copy) {
            retryToken = copy.retryToken;
            delay = copy.delay;
        }

        public Builder retryToken(RetryToken token) {
            this.retryToken = token;
            return this;
        }

        public Builder delay(Duration delay) {
            this.delay = delay;
            return this;
        }

        @Override
        public StandardRetryInitialTokenResponse build() {
            return new StandardRetryInitialTokenResponse(this);
        }
    }
}
