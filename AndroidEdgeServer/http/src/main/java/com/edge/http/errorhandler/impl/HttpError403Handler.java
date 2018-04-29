/**************************************************
 * Android Web Server
 * Based on JavaLittleWebServer (2008)
 * <p/>
 * Copyright (c) Piotr Polak 2008-2017
 **************************************************/

package com.edge.http.errorhandler.impl;

import com.edge.http.errorhandler.AbstractHtmlErrorHandler;
import com.edge.http.servlet.HttpServletResponse;


public class HttpError403Handler extends AbstractHtmlErrorHandler {

    public HttpError403Handler(String errorDocumentPath) {
        super(HttpServletResponse.STATUS_ACCESS_DENIED, "Error 403 - Forbidden", "<p>Access Denied.</p>",
                errorDocumentPath);
    }
}
