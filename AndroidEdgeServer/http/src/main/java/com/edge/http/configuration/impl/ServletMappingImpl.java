/**************************************************
 * Android Web Server
 * Based on JavaLittleWebServer (2008)
 * <p/>
 * Copyright (c) Piotr Polak 2018-2018
 **************************************************/

package com.edge.http.configuration.impl;

import java.util.regex.Pattern;

import com.edge.http.configuration.ServletMapping;
import com.edge.http.servlet.HttpServlet;

public class ServletMappingImpl implements ServletMapping {

    private final Pattern urlPattern;

    private final Class<? extends HttpServlet> servletClass;

    public ServletMappingImpl(Pattern urlPattern, Class<? extends HttpServlet> servletClass) {
        this.urlPattern = urlPattern;
        this.servletClass = servletClass;
    }

    @Override
    public Pattern getUrlPattern() {
        return urlPattern;
    }

    @Override
    public Class<? extends HttpServlet> getServletClass() {
        return servletClass;
    }
}
