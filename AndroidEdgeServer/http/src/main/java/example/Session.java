/**************************************************
 * Android Web Server
 * Based on JavaLittleWebServer (2008)
 * <p/>
 * Copyright (c) Piotr Polak 2008-2016
 **************************************************/

package example;

import com.edge.http.exception.ServletException;
import com.edge.http.servlet.HttpServletRequest;
import com.edge.http.servlet.HttpServletResponse;
import com.edge.http.servlet.HttpServlet;

public class Session extends HttpServlet {

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException {

        // Saving session attribute name in a variable for convenience
        String attributeName = "pageHits";

        // Resetting the counter
        int pageHits = 0;

        // Getting the page hits from session if exists
        if (request.getSession().getAttribute(attributeName) != null) {
            // Please note the session attribute is of String type
            pageHits = Integer.parseInt((String) request.getSession().getAttribute(attributeName));
        }

        // Incrementing hits counter
        ++pageHits;

        // Persisting incremented value in session
        request.getSession().setAttribute(attributeName, Integer.toString(pageHits));

        // Printing out the result
        response.getWriter().println("<p>Session page hits: " + pageHits + "</p>");
        response.getWriter().println("<p>Session is new: " + request.getSession().isNew() + "</p>");
        response.getWriter().println("<p>Session creation time: " + request.getSession().getCreationTime() + "</p>");
        response.getWriter().println("<p>Session last accessed time: " + request.getSession().getLastAccessedTime() + "</p>");
        response.getWriter().println("<p>Session max inactive interval in seconds: " + request.getSession().getMaxInactiveInterval() + "</p>");
    }
}
