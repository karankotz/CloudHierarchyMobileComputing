package com.edge.http;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class Headers {

    public static final String HEADER_ALLOW = "Allow";
    public static final String HEADER_SERVER = "Server";
    public static final String HEADER_CONTENT_DISPOSITION = "Content-Disposition";
    public static final String HEADER_LOCATION = "Location";
    public static final String HEADER_CONTENT_LENGTH = "Content-Length";
    public static final String HEADER_CONTENT_TYPE = "Content-Type";
    public static final String HEADER_CONNECTION = "Connection";
    public static final String HEADER_SET_COOKIE = "Set-Cookie";
    public static final String HEADER_CACHE_CONTROL = "Cache-Control";
    public static final String HEADER_ACCEPT_LANGUAGE = "Accept-Language";
    public static final String HEADER_PRAGMA = "Pragma";
    public static final String HEADER_COOKIE = "Cookie";
    public static final String HEADER_TRANSFER_ENCODING = "Transfer-Encoding";
    public static final String HEADER_HOST = "Host";
    public static final String HEADER_RANGE = "Range";
    public static final String HEADER_ACCEPT_RANGES = "Accept-Ranges";
    public static final String HEADER_CONTENT_RANGE = "Content-Range";

    // TreeMap is used to obtain case insensitive map
    private final Map<String, String> headers = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    /**
     * Sets a header.
     *
     * @param name  header name
     * @param value header value
     */
    public void setHeader(String name, String value) {
        headers.put(name, value);
    }

    /**
     * Returns header's value
     *
     * @param name name of the header
     * @return header's value
     */
    public String getHeader(String name) {
        return headers.get(name);
    }

    /**
     * Returns header names' set.
     *
     * @return
     */
    public Set<String> keySet() {
        return headers.keySet();
    }

    /**
     * Tells whether a header of specified name exists.
     *
     * @param name
     * @return
     */
    public boolean containsHeader(String name) {
        return headers.containsKey(name);
    }
}
