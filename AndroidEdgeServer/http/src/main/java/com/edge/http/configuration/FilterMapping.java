/**************************************************
 * Android Web Server
 * Based on JavaLittleWebServer (2008)
 * <p/>
 * Copyright (c) Piotr Polak 2018-2018
 **************************************************/

package com.edge.http.configuration;

import java.util.regex.Pattern;

import com.edge.http.servlet.Filter;

public interface FilterMapping {


    Pattern getUrlPattern();


    Pattern getUrlExcludePattern();


    Class<? extends Filter> getFilterClass();
}
