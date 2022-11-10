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
import java.util.Optional;
import software.amazon.awssdk.annotations.SdkProtectedApi;
import software.amazon.awssdk.retriesapi.RefreshRetryTokenRequest;
import software.amazon.awssdk.retriesapi.RetryToken;

@SdkProtectedApi
public class StandardRefreshRetryTokenRequest implements RefreshRetryTokenRequest {
    private final Throwable failure;
    private Duration suggestedDelay;
    private RetryToken priorToken;

    public StandardRefreshRetryTokenRequest(Throwable failure, Duration suggestedDelay, RetryToken priorToken) {
        this.failure = failure;
        this.suggestedDelay = suggestedDelay;
        this.priorToken = priorToken;
    }


    @Override
    public Throwable failure() {
        return failure;
    }

    @Override
    public Optional<Duration> suggestedDelay() {
        return Optional.of(suggestedDelay);
    }

    @Override
    public RetryToken token() {
        return priorToken;
    }
}
