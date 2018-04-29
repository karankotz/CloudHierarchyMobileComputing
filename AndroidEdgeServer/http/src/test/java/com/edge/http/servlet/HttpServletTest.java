package com.edge.http.servlet;

import org.junit.Test;

import com.edge.http.exception.ServletException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;

public class HttpServletTest {

    @Test
    public void shouldProvideDefaultValues() {
        HttpServlet httpServlet = new SampleServlet();
        assertThat(httpServlet.getServletInfo(), is(""));
        assertThat(httpServlet.getServletContext(), is(nullValue()));
        httpServlet.destroy();
    }

    @Test
    public void shouldProvideContextAfterInitialization() throws ServletException {
        HttpServlet httpServlet = new SampleServlet();
        ServletContext servletContext = mock(ServletContext.class);
        ServletConfig servletConfig = new ServletConfigWrapper(servletContext);
        httpServlet.init(servletConfig);
        assertThat(httpServlet.getServletInfo(), is(""));
        assertThat(httpServlet.getServletContext(), is(equalTo(servletContext)));
        httpServlet.destroy();
    }

    private class SampleServlet extends HttpServlet {
        @Override
        public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException {

        }
    }
}
