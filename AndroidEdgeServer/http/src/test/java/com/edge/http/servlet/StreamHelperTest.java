package com.edge.http.servlet;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.matchers.ArrayEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.edge.http.RangePartHeader;
import com.edge.http.protocol.serializer.impl.RangePartHeaderSerializer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

public class StreamHelperTest {

    public static final String BOUNDARY = "someboundary";
    public static final String CONTENT_TYPE = "application/pdf";
    public static final int TOTAL_LENGTH = 0;
    private static final Charset CHARSET = Charset.forName("UTF-8");
    public static final String NEW_LINE = "\r\n";

    private ByteArrayInputStream inputStream;
    private ByteArrayOutputStream outputStream;
    private byte[] inputBytes;
    private final RangeHelper rangeHelper = new RangeHelper();
    private final RangePartHeaderSerializer rangePartHeaderSerializer = new RangePartHeaderSerializer();
    private final StreamHelper streamHelper = new StreamHelper(rangeHelper, rangePartHeaderSerializer);
    private final SliceHelper sliceHelper = new SliceHelper();

    @Before
    public void setUp() {
        inputBytes = new byte[1024 * 5];
        new Random().nextBytes(inputBytes);
        inputStream = new ByteArrayInputStream(inputBytes);
        outputStream = new ByteArrayOutputStream();
    }

    @Test
    public void shouldServeTheSameBytes() throws IOException {
        streamHelper.serveMultiRangeStream(inputStream, outputStream);
        byte[] out = outputStream.toByteArray();
        assertThat(out.length, is(greaterThan(0)));
        assertThat(out, new ArrayEquals(inputBytes));
    }

    @Test
    public void shouldServeTheSameBytesForSingeRangeSmallerThanTheBuffer() throws IOException {
        Range range = new Range(3, 200);

        byte[] inputBytesSliced = sliceHelper.getSliceForRanges(inputBytes, Arrays.asList(range));

        streamHelper.serveMultiRangeStream(inputStream, outputStream, range);

        byte[] out = outputStream.toByteArray();
        assertThat(out.length, is(equalTo((int) rangeHelper.getTotalLength(Arrays.asList(range)))));

        assertThat(out, new ArrayEquals(inputBytesSliced));
    }

    @Test
    public void shouldServeTheSameBytesForSingeRangeGreaterThanTheBuffer() throws IOException {
        Range range = new Range(3, 1024);

        byte[] inputBytesSliced = sliceHelper.getSliceForRanges(inputBytes, Arrays.asList(range));

        streamHelper.serveMultiRangeStream(inputStream, outputStream, range);

        byte[] out = outputStream.toByteArray();
        assertThat(out.length, is(equalTo((int) rangeHelper.getTotalLength(Arrays.asList(range)))));

        assertThat(out, new ArrayEquals(inputBytesSliced));
    }

    @Test
    public void shouldServeTheSameBytesForMultipleRangeSmallerThanTheBuffer() throws IOException {
        List<Range> ranges = new ArrayList<>();
        ranges.add(new Range(0, 2));
        ranges.add(new Range(2, 2));

        byte[] inputBytesSliced = sliceHelper.getSliceForRanges(inputBytes, ranges);

        streamHelper.serveMultiRangeStream(inputStream, outputStream, ranges, BOUNDARY, CONTENT_TYPE, 0);

        byte[] out = outputStream.toByteArray();
        assertThat(out.length, is(equalTo(inputBytesSliced.length)));

        assertThat(out, new ArrayEquals(inputBytesSliced));
    }

    @Test
    public void shouldServeTheSameBytesForMultipleRangeSmallerThanTheBufferOverlapping() throws IOException {
        List<Range> ranges = new ArrayList<>();
        ranges.add(new Range(0, 49));
        ranges.add(new Range(40, 59));

        byte[] inputBytesSliced = sliceHelper.getSliceForRanges(inputBytes, ranges);

        streamHelper.serveMultiRangeStream(inputStream, outputStream, ranges, BOUNDARY, CONTENT_TYPE, TOTAL_LENGTH);

        byte[] out = outputStream.toByteArray();
        assertThat(out.length, is(equalTo(inputBytesSliced.length)));

        assertThat(out, new ArrayEquals(inputBytesSliced));
    }

    @Test
    public void shouldServeTheSameBytesForMultipleRangeGreaterThanTheBuffer() throws IOException {
        List<Range> ranges = new ArrayList<>();
        ranges.add(new Range(0, 550));
        ranges.add(new Range(1024, 1623));

        byte[] inputBytesSliced = sliceHelper.getSliceForRanges(inputBytes, ranges);


        streamHelper.serveMultiRangeStream(inputStream, outputStream, ranges, BOUNDARY, CONTENT_TYPE, TOTAL_LENGTH);

        byte[] out = outputStream.toByteArray();

        assertThat(out.length, is(equalTo(inputBytesSliced.length)));
        assertThat(out, new ArrayEquals(inputBytesSliced));
    }

    @Test
    public void selfTest() {
        byte[] sample = {0, 1, 2, 3, 4};
        List<Range> ranges = new ArrayList<>();
        ranges.add(new Range(2, 4));
        assertThat(sliceHelper.getSliceForRanges(sample, ranges), new ArrayEquals(new byte[]{
                2, 3, 4
        }));
    }


    private class SliceHelper {
        private Random random = new Random();

        public byte[] getSliceForRanges(byte[] input, List<Range> ranges) {
            long totalLength = rangeHelper.getTotalLength(ranges);
            long headersPartLength = rangePartHeaderSerializer.getPartHeadersLength(ranges, BOUNDARY, CONTENT_TYPE, TOTAL_LENGTH);

            byte[] output = new byte[(int) (totalLength + headersPartLength)];
            random.nextBytes(output);

            int destPos = 0;

            for (Range range : ranges) {
                if (ranges.size() > 1) {
                    destPos += appendRangePartHeader(output, destPos, range);
                }
                destPos += appendRangeSlice(input, output, destPos, range);
            }

            if (ranges.size() > 1) {
                appendLastBoundaryDeliminator(output, destPos);
            }

            return output;
        }

        private int appendRangeSlice(byte[] input, byte[] output, int destPos, Range range) {
            byte[] slice = Arrays.copyOfRange(input, (int) range.getFrom(), (int) range.getTo() + 1);
            System.arraycopy(slice, 0, output, destPos, slice.length);
            return slice.length;
        }

        private int appendRangePartHeader(byte[] output, int destPos, Range range) {
            RangePartHeader rangePartHeader = new RangePartHeader(range, BOUNDARY, CONTENT_TYPE, TOTAL_LENGTH);
            byte[] slice = (NEW_LINE + rangePartHeaderSerializer.serialize(rangePartHeader)).getBytes(CHARSET);
            System.arraycopy(slice, 0, output, destPos, slice.length);
            return slice.length;
        }

        private int appendLastBoundaryDeliminator(byte[] output, int destPos) {
            byte[] slice = (NEW_LINE + rangePartHeaderSerializer.serializeLastBoundaryDeliminator(BOUNDARY)).getBytes(CHARSET);
            System.arraycopy(slice, 0, output, destPos, slice.length);
            return slice.length;
        }
    }
}
