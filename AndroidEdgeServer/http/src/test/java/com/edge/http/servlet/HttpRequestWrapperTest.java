package com.edge.http.servlet;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.edge.http.Headers;
import com.edge.http.RequestStatus;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsMapContaining.hasKey;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.hamcrest.core.IsNot.not;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HttpRequestWrapperTest {

    private HttpRequestWrapper httpRequestWrapper;
    private RequestStatus requestStatus;
    private InputStream inputStream;
    private ServletContextWrapper servletContext;
    private Headers headers;

    @Before
    public void setUp() {
        requestStatus = new RequestStatus();
        requestStatus.setMethod(HttpRequestWrapper.METHOD_GET);
        requestStatus.setQueryString("a=1&b=2");
        requestStatus.setUri("/someuri");
        requestStatus.setProtocol("HTTP/1.1");

        Map<String, String> postParameters = new HashMap<>();
        postParameters.put("postKey", "postValue");

        Map<String, String> getParameters = new HashMap<>();
        getParameters.put("getKey", "getValue");

        Map<String, Cookie> cookies = new HashMap<>();
        headers = new Headers();
        headers.setHeader(Headers.HEADER_ACCEPT_LANGUAGE, "pl-PL,pl;q=0.8,en-US;q=0.6,en;q=0.4,ro;q=0.2,ru;q=0.2");

        inputStream = new InputStream() {
            @Override
            public int read() throws IOException {
                return 0;
            }
        };

        servletContext = mock(ServletContextWrapper.class);

        httpRequestWrapper = new HttpRequestWrapper();
        httpRequestWrapper.setStatus(requestStatus);
        httpRequestWrapper.setPostParameters(postParameters);
        httpRequestWrapper.setGetParameters(getParameters);
        httpRequestWrapper.setScheme("http");
        httpRequestWrapper.setCookies(cookies);
        httpRequestWrapper.setHeaders(headers);
        httpRequestWrapper.setInputStream(inputStream);
        httpRequestWrapper.setLocalPort(123);
        httpRequestWrapper.setLocalAddr("localAddr");
        httpRequestWrapper.setLocalName("localName");
        httpRequestWrapper.setRemotePort(987);
        httpRequestWrapper.setRemoteAddr("remoteAddr");
        httpRequestWrapper.setRemoteHost("remoteHost");
        httpRequestWrapper.setServerPort(8080);
        httpRequestWrapper.setServerName("serverName");
        httpRequestWrapper.setSecure(true);
        httpRequestWrapper.setServletContext(servletContext);
        httpRequestWrapper.setMultipart(true);
        httpRequestWrapper.setUploadedFiles(new HashSet<UploadedFile>());
    }

    @Test
    public void shouldReturnCorrectValuesGetters() {
        requestStatus.setMethod(HttpRequestWrapper.METHOD_GET);
        httpRequestWrapper.setAttribute("name", "value");
        assertThat((List<String>) (Collections.list(httpRequestWrapper.getAttributeNames())), hasSize(1));
        assertThat((List<String>) (Collections.list(httpRequestWrapper.getAttributeNames())), hasItems("name"));
        assertThat((String) httpRequestWrapper.getAttribute("name"), is("value"));
        httpRequestWrapper.removeAttribute("name");
        assertThat((List<String>) (Collections.list(httpRequestWrapper.getAttributeNames())), hasSize(0));

        assertThat(httpRequestWrapper.getRequestURI(), is(requestStatus.getUri()));
        assertThat(httpRequestWrapper.getQueryString(), is(requestStatus.getQueryString()));
        assertThat(httpRequestWrapper.getRequestedSessionId(), is(nullValue()));
        assertThat(httpRequestWrapper.getContentLength(), is(-1));
        assertThat(httpRequestWrapper.getCharacterEncoding(), is("UTF-8"));
        httpRequestWrapper.setCharacterEncoding("ISO 8859-1");
        assertThat(httpRequestWrapper.getCharacterEncoding(), is("ISO 8859-1"));
        assertThat(httpRequestWrapper.getLocalPort(), is(123));
        assertThat(httpRequestWrapper.getLocalAddr(), is("localAddr"));
        assertThat(httpRequestWrapper.getLocalName(), is("localName"));
        assertThat(httpRequestWrapper.getRemotePort(), is(987));
        assertThat(httpRequestWrapper.getRemoteAddr(), is("remoteAddr"));
        assertThat(httpRequestWrapper.getRemoteHost(), is("remoteHost"));
        assertThat(httpRequestWrapper.getServerPort(), is(8080));
        assertThat(httpRequestWrapper.getServerName(), is("serverName"));
        assertThat(httpRequestWrapper.isSecure(), is(true));
        assertThat(httpRequestWrapper.getRequestURL().toString(), is("http://localAddr:8080/someuri"));
        assertThat(httpRequestWrapper.getIntHeader("someInexistentHeader"), is(-1));
        assertThat(httpRequestWrapper.getInputStream(), is(inputStream));
        assertThat(httpRequestWrapper.getProtocol(), is(requestStatus.getProtocol()));
        assertThat(httpRequestWrapper.getSession(), is(nullValue()));
        assertThat(httpRequestWrapper.isMultipart(), is(true));
        assertThat(httpRequestWrapper.getCookies(), is(Matchers.<Cookie>emptyArray()));
        assertThat(httpRequestWrapper.getUploadedFiles().size(), is(0));
        assertThat(httpRequestWrapper.getHeaders(), is(headers));
        assertThat((List<String>) (Collections.list(httpRequestWrapper.getHeaderNames())), hasItems(Headers.HEADER_ACCEPT_LANGUAGE));
    }

    @Test
    public void shouldReturnGetRequestParametersMapOnGetMethod() {
        requestStatus.setMethod(HttpRequestWrapper.METHOD_GET);
        assertThat(httpRequestWrapper.getParameter("getKey"), is("getValue"));
        assertThat(((Map<String, String>) httpRequestWrapper.getParameterMap()), hasKey("getKey"));
        assertThat(((Map<String, String>) httpRequestWrapper.getParameterMap()), not(hasKey("postKey")));
        assertThat((List<String>) (Collections.list(httpRequestWrapper.getParameterNames())), hasSize(1));
        assertThat((List<String>) (Collections.list(httpRequestWrapper.getParameterNames())), hasItems("getKey"));
        assertThat((List<String>) (Collections.list(httpRequestWrapper.getParameterNames())), not(hasItems("postKey")));
    }

    @Test
    public void shouldReturnPostRequestParametersMapOnPostMethod() {
        requestStatus.setMethod(HttpRequestWrapper.METHOD_POST);
        assertThat(httpRequestWrapper.getPostParameter("postKey"), is("postValue"));
        assertThat(((Map<String, String>) httpRequestWrapper.getParameterMap()), hasKey("postKey"));
        assertThat(((Map<String, String>) httpRequestWrapper.getParameterMap()), not(hasKey("getKey")));
        assertThat((List<String>) (Collections.list(httpRequestWrapper.getParameterNames())), hasSize(1));
        assertThat((List<String>) (Collections.list(httpRequestWrapper.getParameterNames())), hasItems("postKey"));
        assertThat((List<String>) (Collections.list(httpRequestWrapper.getParameterNames())), not(hasItems("getKey")));
    }

    @Test
    public void shouldReturnPostRequestParametersMapOnPutMethod() {
        requestStatus.setMethod(HttpRequestWrapper.METHOD_PUT);
        assertThat(httpRequestWrapper.getPostParameter("postKey"), is("postValue"));
        assertThat(((Map<String, String>) httpRequestWrapper.getParameterMap()), hasKey("postKey"));
        assertThat(((Map<String, String>) httpRequestWrapper.getParameterMap()), not(hasKey("getKey")));
        assertThat((List<String>) (Collections.list(httpRequestWrapper.getParameterNames())), hasSize(1));
        assertThat((List<String>) (Collections.list(httpRequestWrapper.getParameterNames())), hasItems("postKey"));
        assertThat((List<String>) (Collections.list(httpRequestWrapper.getParameterNames())), not(hasItems("getKey")));
    }

    @Test
    public void shouldParseNumericHeader() {
        Headers headers = new Headers();
        headers.setHeader("intKey", "3333");
        headers.setHeader("unableToParseKey", "AAAA");
        httpRequestWrapper.setHeaders(headers);

        assertThat(httpRequestWrapper.getIntHeader("missingIntKey"), is(-1));
        assertThat(httpRequestWrapper.getIntHeader("intKey"), is(3333));
        assertThat(httpRequestWrapper.getIntHeader("unableToParseKey"), is(0));
    }

    @Test
    public void shouldReturnContentLength() {
        Headers headers = new Headers();
        headers.setHeader(Headers.HEADER_CONTENT_LENGTH, "1234");
        httpRequestWrapper.setHeaders(headers);

        assertThat(httpRequestWrapper.getContentLength(), is(1234));
    }

    @Test
    public void shouldReturnContentType() {
        Headers headers = new Headers();
        headers.setHeader(Headers.HEADER_CONTENT_TYPE, "SOME_TYPE/TEXT");
        httpRequestWrapper.setHeaders(headers);

        assertThat(httpRequestWrapper.getContentType(), is("SOME_TYPE/TEXT"));
    }


    @Test
    public void shouldReturnCookies() {
        Map<String, Cookie> cookies = new HashMap<>();

        Cookie cookie1 = new Cookie("someName", "someValue");
        Cookie cookie2 = new Cookie("someOtherName", "someOtherValue");
        Cookie sessionCookie = new Cookie(HttpSessionWrapper.COOKIE_NAME, "sessionId");

        cookies.put("someName", cookie1);
        cookies.put("someOtherName", cookie2);
        cookies.put(HttpSessionWrapper.COOKIE_NAME, sessionCookie);
        httpRequestWrapper.setCookies(cookies);

        assertThat(httpRequestWrapper.getCookies().length, is(cookies.size()));
        assertThat(Arrays.asList(httpRequestWrapper.getCookies()), hasItems(cookie1, cookie2, sessionCookie));
        assertThat(httpRequestWrapper.getCookie("someName"), is(cookie1));
        assertThat(httpRequestWrapper.getCookie("someOtherName"), is(cookie2));
        assertThat(httpRequestWrapper.getCookie(HttpSessionWrapper.COOKIE_NAME), is(sessionCookie));
        assertThat(httpRequestWrapper.getRequestedSessionId(), is("sessionId"));
        assertThat(httpRequestWrapper.getCookie("inexistingName"), is(nullValue()));
    }

    @Test
    public void shouldParseDateHeader() {
        Headers headers = new Headers();
        headers.setHeader("If-Modified-Since", "Thu, 15 Jan 2015 16:30:13 GMT");
        headers.setHeader("If-Modified-Since-MALFORMED", "Malformed Value");
        httpRequestWrapper.setHeaders(headers);
        assertThat(httpRequestWrapper.getDateHeader("If-Modified-Since"), is(1421339413000L));
        assertThat(httpRequestWrapper.getDateHeader("If-Modified-Since-MALFORMED"), is(-1L));
        assertThat(httpRequestWrapper.getDateHeader("Inexisting"), is(-1L));
    }

    @Test
    public void shouldReturnSession() {
        Map<String, Cookie> cookies = new HashMap<>();
        Cookie sessionCookie = new Cookie(HttpSessionWrapper.COOKIE_NAME, "sessionId");
        cookies.put(HttpSessionWrapper.COOKIE_NAME, sessionCookie);
        httpRequestWrapper.setCookies(cookies);
        httpRequestWrapper.setServletContext(servletContext);
        when(servletContext.getSession("sessionId")).thenReturn(new HttpSessionWrapper("sessionId"));
        assertThat(httpRequestWrapper.getSession(), is(instanceOf(HttpSessionWrapper.class)));
    }

    @Test
    public void shouldReturnTheSameSessionForConsecutiveCalls() {
        Map<String, Cookie> cookies = new HashMap<>();
        Cookie sessionCookie = new Cookie(HttpSessionWrapper.COOKIE_NAME, "sessionId");
        cookies.put(HttpSessionWrapper.COOKIE_NAME, sessionCookie);
        httpRequestWrapper.setCookies(cookies);
        httpRequestWrapper.setServletContext(servletContext);
        when(servletContext.getSession("sessionId")).thenReturn(new HttpSessionWrapper("sessionId"));
        assertThat(httpRequestWrapper.getSession(), is(instanceOf(HttpSessionWrapper.class)));
        assertThat(httpRequestWrapper.getSession().equals(httpRequestWrapper.getSession()), is(true));
    }

    @Test
    public void shouldParseUrlFromHost() {
        Headers headers = new Headers();
        headers.setHeader(Headers.HEADER_HOST, "example.com:3366");
        httpRequestWrapper.setHeaders(headers);
        assertThat(httpRequestWrapper.getRequestURL().toString(), is("http://example.com:8080/someuri"));
    }
}
