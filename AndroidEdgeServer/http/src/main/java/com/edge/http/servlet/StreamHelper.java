package com.edge.http.servlet;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.List;

import com.edge.http.RangePartHeader;
import com.edge.http.Statistics;
import com.edge.http.exception.UnexpectedSituationException;
import com.edge.http.protocol.serializer.impl.RangePartHeaderSerializer;

public class StreamHelper {

    private static final int BUFFER_SIZE = 512;
    private static final String NEW_LINE = "\r\n";
    private static final Charset CHARSET = Charset.forName("UTF-8");

    private final RangeHelper rangeHelper;
    private final RangePartHeaderSerializer rangePartHeaderSerializer;

    public StreamHelper(final RangeHelper rangeHelper,
                        final RangePartHeaderSerializer rangePartHeaderSerializer) {
        this.rangeHelper = rangeHelper;
        this.rangePartHeaderSerializer = rangePartHeaderSerializer;
    }

    /**
     * Serves all input stream to the output stream.
     *
     * @param inputStream
     * @param outputStream
     * @throws IOException
     */
    public void serveMultiRangeStream(InputStream inputStream, OutputStream outputStream)
            throws IOException {
        int numberOfBufferReadBytes;
        byte[] buffer = new byte[BUFFER_SIZE];

        while ((numberOfBufferReadBytes = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, numberOfBufferReadBytes);
            outputStream.flush();

            Statistics.addBytesSent(numberOfBufferReadBytes);
        }
    }

    /**
     * Serves a range of the input stream to the output stream.
     *
     * @param inputStream
     * @param outputStream
     * @param range
     * @throws IOException
     */
    private void doServeRangeStream(InputStream inputStream, OutputStream outputStream, Range range)
            throws IOException {
        int numberOfBufferReadBytes;
        byte[] buffer = new byte[BUFFER_SIZE];
        long numberOfBytesServedForRange = 0;

        inputStream.reset();
        if (inputStream.skip(range.getFrom()) != range.getFrom()) {
            throw new UnexpectedSituationException("Failed to skip bytes from input stream.");
        }

        while ((numberOfBufferReadBytes = inputStream.read(buffer)) != -1) {
            int numberOfBytesToServe = numberOfBufferReadBytes;
            long numberOfBytesRemaining = rangeHelper.getRangeLength(range) - numberOfBytesServedForRange;
            if (numberOfBytesRemaining < numberOfBytesToServe) {
                // There is no way this value is greater than Integer.MAX_VALUE
                numberOfBytesToServe = (int) numberOfBytesRemaining;
            }

            outputStream.write(buffer, 0, numberOfBytesToServe);
            outputStream.flush();
            Statistics.addBytesSent(numberOfBytesToServe);

            numberOfBytesServedForRange += numberOfBytesToServe;

            if (numberOfBytesServedForRange >= range.getTo()) {
                break;
            }
        }
    }

    /**
     * Serves multiple ranges of the input stream to the output stream
     *
     * @param inputStream
     * @param outputStream
     * @param rangeList
     * @param boundary
     * @param contentType
     * @param totalLength
     * @throws IOException
     */
    public void serveMultiRangeStream(InputStream inputStream, OutputStream outputStream,
                                      List<Range> rangeList, String boundary, String contentType,
                                      long totalLength) throws IOException {
        inputStream.mark(0);

        serveMultiRangeStream(new ByteArrayInputStream(NEW_LINE.getBytes(CHARSET)), outputStream);
        for (Range range : rangeList) {
            doServeRangePartHeader(outputStream, boundary, contentType, totalLength, range);
            doServeRangeStream(inputStream, outputStream, range);
            serveMultiRangeStream(new ByteArrayInputStream(NEW_LINE.getBytes(CHARSET)), outputStream);
        }
        serveMultiRangeStream(new ByteArrayInputStream(rangePartHeaderSerializer.serializeLastBoundaryDeliminator(boundary).getBytes(CHARSET)), outputStream);
    }

    private void doServeRangePartHeader(OutputStream outputStream, String boundary,
                                        String contentType, long totalLength, Range range)
            throws IOException {
        RangePartHeader rangePartHeader = new RangePartHeader(range, boundary, contentType, totalLength);
        byte[] rangePartHeaderBytes= rangePartHeaderSerializer.serialize(rangePartHeader).getBytes(CHARSET);

        serveMultiRangeStream(new ByteArrayInputStream(rangePartHeaderBytes), outputStream);
    }

    /**
     * Serves single range to the output stream.
     *
     * @param inputStream
     * @param outputStream
     * @param range
     * @throws IOException
     */
    public void serveMultiRangeStream(InputStream inputStream, OutputStream outputStream,
                                      Range range) throws IOException {
        inputStream.mark(0);
        doServeRangeStream(inputStream, outputStream, range);
    }
}
