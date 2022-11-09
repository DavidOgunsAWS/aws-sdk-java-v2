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

import software.amazon.awssdk.annotations.SdkProtectedApi;

@SdkProtectedApi
interface RetryErrorTypedException<T extends Exception> {

    /**
     * Retrieves generic retry error metadata modeled in the exception.
     *
     * Implementations may have a class static determination of error type,
     * or use logic dependent on
     *
     *
     *
     * @return
     */
    RetryErrorType getRetryErrorType();
}
