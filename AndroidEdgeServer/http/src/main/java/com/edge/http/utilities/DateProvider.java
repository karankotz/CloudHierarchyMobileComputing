/**************************************************
 * Android Web Server
 * Based on JavaLittleWebServer (2008)
 * <p>
 * Copyright (c) Piotr Polak 2017-2017
 **************************************************/
package com.edge.http.utilities;

import java.util.Date;

/**
 * Date provider
 *
 * @author Piotr Polak piotr [at] polak [dot] ro
 * @since 201710
 */
public class DateProvider {

    /**
     * Returns current date
     *
     * @return
     */
    public Date now() {
        return new Date();
    }
}
