/**************************************************
 * Android Web Server
 * Based on JavaLittleWebServer (2008)
 * <p/>
 * Copyright (c) Piotr Polak 2008-2018
 **************************************************/

package example.filter;

import java.io.IOException;

import com.edge.http.exception.AccessDeniedException;
import com.edge.http.exception.ServletException;
import com.edge.http.servlet.Filter;
import com.edge.http.servlet.FilterChain;
import com.edge.http.servlet.FilterConfig;
import com.edge.http.servlet.HttpServletRequest;
import com.edge.http.servlet.HttpServletResponse;

/**
 * Always throws AccessDeniedException
 *
 * @author Piotr Polak piotr [at] polak [dot] ro
 * @since 201803
 */
public class FakeSecuredFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Do nothing
    }

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        throw new AccessDeniedException();
    }
}
