package com.edge.http.servlet;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Map;

import com.edge.http.Headers;
import com.edge.http.exception.protocol.ProtocolException;
import com.edge.http.exception.protocol.UnsupportedProtocolException;
import com.edge.http.protocol.parser.MalformedInputException;
import com.edge.http.protocol.parser.Parser;
import com.edge.http.protocol.parser.impl.RequestStatusParser;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class HttpServletRequestWrapperFactoryTest {

    private static HttpServletRequestWrapperFactory factory;
    private static Socket socket;
    private static Parser<Map<String, Cookie>> cookieParser;
    private static Parser<Headers> headersParser;
    private static Headers headers;

    @Before
    public void setUp() throws Exception {
        headers = new Headers();
        headersParser = mock(Parser.class);
        when(headersParser.parse(any(String.class))).thenReturn(headers);
        cookieParser = mock(Parser.class);

        factory = new HttpServletRequestWrapperFactory(
                headersParser,
                mock(Parser.class),
                new RequestStatusParser(),
                cookieParser,
                mock(Parser.class),
                ""
        );

        InputStream inputStream = new ByteArrayInputStream("GET / HTTP/1.0\r\nHeader1: someValue\r\n\r\n".getBytes());

        socket = mock(Socket.class);
        when(socket.getInputStream()).thenReturn(inputStream);
        when(socket.getInetAddress()).thenReturn(mock(InetAddress.class));
        when(socket.getLocalAddress()).thenReturn(mock(InetAddress.class));
        when(socket.getRemoteSocketAddress()).thenReturn(new InetSocketAddress(mock(InetAddress.class), 1));
    }

    @Test(expected = ProtocolException.class)
    public void shouldThrowProtocolExceptionOnMalformedHeaders() throws Exception {
        when(headersParser.parse(any(String.class))).thenThrow(new MalformedInputException("ANY"));
        factory.createFromSocket(socket);
    }

    @Test(expected = UnsupportedProtocolException.class)
    public void shouldThrowUnsuportedProtocolExceptionIllegalProtocol() throws Exception {
        when(socket.getInputStream()).thenReturn(new ByteArrayInputStream("GET / MALF/1.0\r\nHeader1: someValue\r\n\r\n".getBytes()));
        factory.createFromSocket(socket);
    }

    @Test
    public void shouldAssignNoCookieOnMalformedCookieString() throws Exception {
        headers.setHeader(Headers.HEADER_COOKIE, "ANYTHING");
        when(cookieParser.parse(any(String.class))).thenThrow(new MalformedInputException("ANY"));
        HttpRequestWrapper request = factory.createFromSocket(socket);
        assertThat(request.getCookies().length, is(0));
        verify(cookieParser, times(1)).parse(any(String.class));
    }

    @Test
    public void shouldAssignNoCookieAndNoHeadersOnNoHeadersString() throws Exception {
        when(socket.getInputStream()).thenReturn(new ByteArrayInputStream("GET / HTTP/1.0\r\n\r\n".getBytes()));
        HttpRequestWrapper request = factory.createFromSocket(socket);
        assertThat(request.getCookies().length, is(0));
        assertThat(request.getHeaders().keySet().size(), is(0));
    }
}
