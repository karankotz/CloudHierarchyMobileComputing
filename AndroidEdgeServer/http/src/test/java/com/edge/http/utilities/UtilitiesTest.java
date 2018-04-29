package com.edge.http.utilities;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static com.edge.http.ExtraMarchers.utilityClass;

public class UtilitiesTest {

    @Test
    public void shouldNotBeInstantiableFinalClass() {
        assertThat(Utilities.class, is(utilityClass()));
    }

    @Test
    public void shouldReturnValidExtension() {
        assertThat(Utilities.getExtension("file.ext"), is("ext"));
        assertThat(Utilities.getExtension("/path/file.ext"), is("ext"));
        assertThat(Utilities.getExtension("file"), is(""));
        assertThat(Utilities.getExtension(null), is(nullValue()));
    }

    @Test
    public void shouldFormatFileSize() {
        assertThat(Utilities.fileSizeUnits(1), is("1 B"));
        assertThat(Utilities.fileSizeUnits(1024), is("1.00 KB"));
        assertThat(Utilities.fileSizeUnits(1025), is("1.00 KB"));
        assertThat(Utilities.fileSizeUnits(1048576), is("1.00 MB"));
        assertThat(Utilities.fileSizeUnits(1048577), is("1.00 MB"));
        assertThat(Utilities.fileSizeUnits(1073741824), is("1.00 GB"));
        assertThat(Utilities.fileSizeUnits(1073741825), is("1.00 GB"));
    }
}
