package com.edge.http.servlet;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import com.edge.http.AbstractIT;
import com.edge.http.RequestBuilder;

import static junit.framework.TestCase.fail;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.assertThat;

public class HttpResponseWrapperIT extends AbstractIT {

    @Test
    public void shouldPrintHeadersFirstWhenWritingToOutputStream() throws IOException {
        String requestBody = RequestBuilder.defaultBuilder()
                .get("/example/Streaming")
                .withCloseConnection()
                .toString();

        Socket socket = getSocket();
        OutputStream out = socket.getOutputStream();
        out.write(requestBody.getBytes());
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        String line;
        int numberOfLinesRead = 0;
        while ((line = in.readLine()) != null) {
            if (++numberOfLinesRead == 1) {
                assertThat(line, startsWith("HTTP/1.1 200"));
            }
        }

        if (numberOfLinesRead == 0) {
            fail("No server response was read");
        }

        socket.close();
    }
}
