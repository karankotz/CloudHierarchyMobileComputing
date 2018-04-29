package com.edge.http.protocol.parser.impl;

import org.junit.Test;

import com.edge.http.RequestStatus;
import com.edge.http.protocol.parser.MalformedInputException;
import com.edge.http.protocol.parser.Parser;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class RequestStatusParserTest {

    @Test
    public void shouldParseStatusString() throws MalformedInputException {
        Parser<RequestStatus> requestStatusParser = new RequestStatusParser();
        RequestStatus requestStatus = requestStatusParser.parse("GET /home?param1=ABC&param2=123 HTTP/1.1");

        assertThat(requestStatus.getMethod(), is("GET"));
        assertThat(requestStatus.getQueryString(), is("param1=ABC&param2=123"));
        assertThat(requestStatus.getUri(), is("/home"));
        assertThat(requestStatus.getProtocol(), is("HTTP/1.1"));
    }

    @Test
    public void shouldIgnoreTrailingCharacters() throws MalformedInputException {
        Parser<RequestStatus> requestStatusParser = new RequestStatusParser();
        RequestStatus requestStatus = requestStatusParser.parse("GET /home?param1=ABC&param2=123 HTTP/1.1\r\n");

        assertThat(requestStatus.getMethod(), is("GET"));
        assertThat(requestStatus.getQueryString(), is("param1=ABC&param2=123"));
        assertThat(requestStatus.getUri(), is("/home"));
        assertThat(requestStatus.getProtocol(), is("HTTP/1.1"));
    }

    @Test(expected = MalformedInputException.class)
    public void shouldThrowMalformedInputExceptionOnInvalidStatus() throws MalformedInputException {
        Parser<RequestStatus> requestStatusParser = new RequestStatusParser();
        requestStatusParser.parse("GET HTTP/1.1");
    }
}
