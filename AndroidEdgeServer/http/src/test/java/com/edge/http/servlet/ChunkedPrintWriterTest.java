package com.edge.http.servlet;

import org.junit.Test;

import java.io.ByteArrayOutputStream;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ChunkedPrintWriterTest {

    @Test
    public void shouldSerializeDataProperly() {
        ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
        ChunkedPrintWriter printWriter = new ChunkedPrintWriter(out);

        printWriter.print("Wiki");
        printWriter.print("pedia");
        printWriter.print(" in\r\n\r\nchunks.");
        printWriter.writeEnd();
        printWriter.flush();
        assertThat(new String(out.toByteArray()), is("4\r\nWiki\r\n5\r\npedia\r\nE\r\n in\r\n\r\nchunks.\r\n0\r\n\r\n"));
    }

    @Test
    public void shouldAppendNewLineProperly() {
        ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
        ChunkedPrintWriter printWriter = new ChunkedPrintWriter(out);

        printWriter.print("Wiki");
        printWriter.println();
        printWriter.flush();
        assertThat(new String(out.toByteArray()), is("4\r\nWiki\r\n2\r\n\r\n\r\n"));
    }

    @Test
    public void shouldEncodeLengthAsHex() {
        ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
        ChunkedPrintWriter printWriter = new ChunkedPrintWriter(out);

        printWriter.print("SomeTextLongerThanSixteenCharacters");
        printWriter.flush();
        assertThat(new String(out.toByteArray()), is("23\r\nSomeTextLongerThanSixteenCharacters\r\n"));

        printWriter.writeEnd();
        printWriter.flush();
        assertThat(new String(out.toByteArray()), is("23\r\nSomeTextLongerThanSixteenCharacters\r\n0\r\n\r\n"));
    }
}
