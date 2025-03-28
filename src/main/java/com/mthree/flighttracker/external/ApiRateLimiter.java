package com.mthree.flighttracker.external;

import java.time.Instant;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

/**
 * A class to help handling 3rd party API rate limits.
 */
public class ApiRateLimiter {
    public static final long RECOMMENDED_MS_BETWEEN_CALLS = 10000L;

    private static final int MAX_HOLDERS = 1;
    private static final int MAX_CALLS_PER_MINUTE = 10;

    private final Semaphore apiCallSemaphore;
    private final Queue<Long> callTimestamps;
    // something to hold number of calls made in last minute?

    public ApiRateLimiter() {
        this.apiCallSemaphore = new Semaphore(1);
        this.callTimestamps = new LinkedList<>();
    }

    /**
     * You MUST call releaseCall() after preparing a call and receiving true. Otherwise, you do not need to.
     * @throws InterruptedException if the acquire wait gets interrupted.
     */
    public synchronized void prepareCall() throws InterruptedException {
        waitForRateLimit();
        apiCallSemaphore.acquire();
        callTimestamps.add(System.currentTimeMillis());
    }

    public synchronized void releaseCall() {
        apiCallSemaphore.release();
    }

    /**
     * Informs if preparing a call will block or not.
     * @return true if your thread will NOT get blocked, false if your thread will get blocked.
     */
    public boolean canCall() {
        return apiCallSemaphore.availablePermits() > 0;
    }

    private void waitForRateLimit() throws InterruptedException {
        long oneMinuteAgo = cleanStaleStamps();

        if(callTimestamps.size() >= MAX_CALLS_PER_MINUTE) {
            long oldestCall = callTimestamps.peek();
            long waitTime = (oldestCall + 60000) - System.currentTimeMillis();
            if (waitTime > 0) {
                Thread.sleep(waitTime);
            }

            oneMinuteAgo = Instant.now().minusSeconds(60).toEpochMilli();
            while (!callTimestamps.isEmpty() && callTimestamps.peek() < oneMinuteAgo) {
                callTimestamps.poll();
            }
        }
    }

    private long cleanStaleStamps() {
        long oneMinuteAgo = Instant.now().minusSeconds(60).toEpochMilli();
        while (!callTimestamps.isEmpty() && callTimestamps.peek() < oneMinuteAgo) {
            callTimestamps.poll();
        }
        return oneMinuteAgo;
    }
}
