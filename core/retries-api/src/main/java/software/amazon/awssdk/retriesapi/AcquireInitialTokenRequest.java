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

package software.amazon.awssdk.retriesapi;

import software.amazon.awssdk.annotations.SdkPublicApi;
import software.amazon.awssdk.annotations.ThreadSafe;

@SdkPublicApi
@ThreadSafe
//TODO: is this Lombok? @Data
public interface AcquireInitialTokenRequest {
    /**
     * An abstract scope for the attempts about to be executed.
     *
     * <p>A scope should be a unique string describing the smallest possible scope of failure for
     * the attempts about to be executed. In practical terms, this is a key for the token bucket
     * used to throttle request attempts. All attempts with the same scope share the same token
     * bucket, ensuring that token-bucket throttling for requests against one resource do not
     * result in throttling for requests against other, unrelated resources.
     */
    String scope();
}