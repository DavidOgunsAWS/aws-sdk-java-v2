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

public enum RetryErrorType {
    /**
     * This is a connection level error such as a socket timeout, socket connect
     * error, tls negotiation timeout etc...
     * Typically these should never be applied for non-idempotent request types
     * since in this scenario, it's impossible to know whether the operation had
     * a side effect on the server.
     */
    Transient,

    /**
     * This is an error where the server explicitly told the client to back off,
     * such as a 429 or 503 Http error.
     */
    Throttling,

    /**
     * This is a server error that isn't explicitly throttling but is considered
     * by the client to be something that should be retried.
     */
    ServerError,

    /**
     * Doesn't count against any budgets. This could be something like a 401
     * challenge in Http.
     */
    ClientError,
}
