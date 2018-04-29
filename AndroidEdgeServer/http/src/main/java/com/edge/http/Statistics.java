package com.edge.http;

import java.util.concurrent.atomic.AtomicLong;
public final class Statistics {
    private static final AtomicLong bytesSend = new AtomicLong();
    private static final AtomicLong bytesReceived = new AtomicLong();
    private static final AtomicLong requestsHandled = new AtomicLong();
    private static final AtomicLong errors404 = new AtomicLong();
    private static final AtomicLong errors500 = new AtomicLong();

    private Statistics() {
    }
    public static void reset() {
        bytesSend.lazySet(0);
        bytesReceived.lazySet(0);
        requestsHandled.lazySet(0);
        errors404.lazySet(0);
        errors500.lazySet(0);
    }

    /**
     * Increments bytes received counter.
     *
     * @param bytes
     */
    public static void addBytesReceived(long bytes) {
        bytesReceived.addAndGet(bytes);
    }

    /**
     * Increments bytes sent counter.
     *
     * @param bytes
     */
    public static void addBytesSent(long bytes) {
        bytesSend.addAndGet(bytes);
    }

    /**
     * Increments requests handled counter.
     */
    public static void incrementRequestHandled() {
        requestsHandled.incrementAndGet();
    }

    /**
     * Increments 404 errors counter.
     */
    public static void incrementError404() {
        errors404.incrementAndGet();
    }

    /**
     * Increments 500 errors counter.
     */
    public static void incrementError500() {
        errors500.incrementAndGet();
    }

    /**
     * Returns number of bytes sent.
     *
     * @return
     */
    public static long getBytesSent() {
        return bytesSend.get();
    }

    /**
     * Returns number of bytes received.
     *
     * @return
     */
    public static long getBytesReceived() {
        return bytesReceived.get();
    }

    /**
     * Returns number of requestsHandled handled.
     *
     * @return
     */
    public static long getRequestsHandled() {
        return requestsHandled.get();
    }

    /**
     * Returns number of 404 errors encountered.
     *
     * @return
     */
    public static long getError404s() {
        return errors404.get();
    }


    /**
     * Returns number of 500 errors encountered.
     *
     * @return
     */
    public static long getError500s() {
        return errors500.get();
    }
}
