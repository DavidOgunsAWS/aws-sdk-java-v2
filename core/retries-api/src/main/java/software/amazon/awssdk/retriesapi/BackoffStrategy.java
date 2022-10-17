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

import java.time.Duration;
import software.amazon.awssdk.annotations.SdkPublicApi;
import software.amazon.awssdk.annotations.ThreadSafe;

/**
 * Determines how long to wait before each execution attempt.
 */
@SdkPublicApi
@ThreadSafe
public interface BackoffStrategy {
    /**
     * Do not back off: retry immediately.
     */
    static BackoffStrategy retryImmediately() {
        //TODO: implement
        return null;
    }

    /**
     * Wait for a random period of time between 0ms and the provided delay.
     */
    static BackoffStrategy fixedDelay(Duration delay) {
        //TODO: implement
        return null;
    }

    /**
     * Wait for a period of time equal to the provided delay.
     */
    static BackoffStrategy fixedDelayWithoutJitter(Duration delay) {
        //TODO: implement
        return null;
    }

    /**
     * Wait for a random period of time between 0ms and an exponentially increasing amount of time
     * between each subsequent attempt of the same call.
     *
     * <p>Specifically, the first attempt waits 0ms, and each subsequent attempt waits between
     * 0ms and {@code min(maxDelay, baseDelay * (1 << (attempt - 2)))}.
     */
    static BackoffStrategy exponentialDelay(Duration baseDelay, Duration maxDelay) {
        //TODO: implement
        return null;
    }

    /**
     * Wait for an exponentially increasing amount of time between each subsequent attempt of the
     * same call.
     *
     * <p>Specifically, the first attempt waits 0ms, and each subsequent attempt waits for
     * {@code min(maxDelay, baseDelay * (1 << (attempt - 2)))}.
     */
    static BackoffStrategy exponentialDelayWithoutJitter(Duration baseDelay, Duration maxDelay) {
        //TODO: implement
        return null;
    }

    /**
     * Compute the amount of time to wait before the provided attempt number is executed.
     */
    Duration computeDelay(int attempt);
}