package com.edge.http.protocol.parser.impl;

import org.junit.Test;

import com.edge.http.MultipartHeadersPart;
import com.edge.http.protocol.parser.MalformedInputException;
import com.edge.http.protocol.parser.Parser;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class MultipartHeadersPartParserTest {

    private static Parser<MultipartHeadersPart> multipartHeadersPartParser
            = new MultipartHeadersPartParser(new HeadersParser());

    @Test
    public void shouldParseValidAttachmentHeader() throws MalformedInputException {
        MultipartHeadersPart headers = multipartHeadersPartParser.parse("Content-Disposition: attachment; name=\"FIELDNAME\"; filename=\"FILE.PDF\"\nContent-type: application/pdf");

        assertThat(headers.getFileName(), is("FILE.PDF"));
        assertThat(headers.getName(), is("FIELDNAME"));
        assertThat(headers.getContentType(), is("application/pdf"));
    }

    @Test
    public void shouldParseValidAttachmentHeaderCaseInsensitive() throws MalformedInputException {
        MultipartHeadersPart headers = multipartHeadersPartParser.parse("CONTENT-DISPOSITION: attachment; NAME=\"FIELDNAME\"; FILENAME=\"FILE.PDF\"\nContent-TYPE: application/pdf");

        assertThat(headers.getFileName(), is("FILE.PDF"));
        assertThat(headers.getName(), is("FIELDNAME"));
        assertThat(headers.getContentType(), is("application/pdf"));
    }

    @Test
    public void shouldParseFormDataText() throws MalformedInputException {
        MultipartHeadersPart headers = multipartHeadersPartParser.parse("Content-Disposition: form-data; name=\"text\"");

        assertThat(headers.getFileName(), is(nullValue()));
        assertThat(headers.getName(), is("text"));
        assertThat(headers.getContentType(), is(nullValue()));
    }

    @Test
    public void shouldParseFormDataTextWhenThereIsNoName() throws MalformedInputException {
        MultipartHeadersPart headers = multipartHeadersPartParser.parse("Content-Disposition: form-data;");

        assertThat(headers.getFileName(), is(nullValue()));
        assertThat(headers.getName(), is(nullValue()));
        assertThat(headers.getContentType(), is(nullValue()));
    }

    @Test(expected = MalformedInputException.class)
    public void shouldThrowMalformedInputException() throws MalformedInputException {
        multipartHeadersPartParser.parse("Content-Disposition: form-data; name=\"text\" filename=\"text");
    }

    @Test(expected = MalformedInputException.class)
    public void shouldThrowMalformedInputExceptionForMissingClosing() throws MalformedInputException {
        multipartHeadersPartParser.parse("Content-Disposition: form-data; name=\"text");
    }
}
