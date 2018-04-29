package com.edge.http.protocol.parser.impl;

import org.junit.Test;

import java.util.Map;

import com.edge.http.protocol.parser.MalformedInputException;
import com.edge.http.protocol.parser.Parser;
import com.edge.http.servlet.Cookie;
import com.edge.http.utilities.Utilities;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsMapContaining.hasKey;
import static org.junit.Assert.assertThat;

public class CookieParserTest {

    private static Parser<Map<String, Cookie>> cookieParser = new CookieParser();

    @Test
    public void shouldParseCookieHavingSpaceInValue() throws MalformedInputException {
        String value = "value containing spaces";
        Map<String, Cookie> cookies = cookieParser.parse("name=" + value);
        assertThat(cookies, hasKey("name"));
        assertThat(cookies.get("name").getValue(), is(value));
    }

    @Test
    public void shouldParseCookieHavingUrlEncodedValue() throws MalformedInputException {
        String value = "&<>some value";
        Map<String, Cookie> cookies = cookieParser.parse("name=" + Utilities.urlEncode(value));
        assertThat(cookies, hasKey("name"));
        assertThat(cookies.get("name").getValue(), is(value));
    }

    @Test
    public void shouldTrimCookieNameValue() throws MalformedInputException {
        Map<String, Cookie> cookies = cookieParser.parse(" name =");
        assertThat(cookies, hasKey("name"));
    }

    @Test
    public void shouldParseEmptyValue() throws MalformedInputException {
        Map<String, Cookie> cookies = cookieParser.parse("");
        assertThat(cookies.size(), is(0));
    }

    @Test
    public void shouldReturnZeroSizeForInvalidValue() throws MalformedInputException {
        Map<String, Cookie> cookies = cookieParser.parse("name");
        assertThat(cookies.size(), is(0));
    }

    @Test
    public void shouldReturnZeroSizeForInvalidKey() throws MalformedInputException {
        Map<String, Cookie> cookies = cookieParser.parse(" = value");
        assertThat(cookies.size(), is(0));
    }

    @Test
    public void shouldParseMalformedEmptyValue() throws MalformedInputException {
        Map<String, Cookie> cookies = cookieParser.parse(" ; ");
        assertThat(cookies.size(), is(0));
    }
}
