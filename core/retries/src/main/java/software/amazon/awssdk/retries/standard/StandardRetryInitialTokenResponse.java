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
    @Override
    public RetryToken token() {
        return null;
    }

    @Override
    public Duration delay() {
        return null;
    }

    @Override
    public Builder toBuilder() {
        return null;
    }

    public static Builder builder() {
        return new StandardRetryInitialTokenResponse.Builder();
    }

    public static class Builder implements CopyableBuilder<Builder, StandardRetryInitialTokenResponse> {

        @Override
        public StandardRetryInitialTokenResponse build() {
            return null;
        }
    }
}
