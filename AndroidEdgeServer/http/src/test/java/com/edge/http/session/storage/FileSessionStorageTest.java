package com.edge.http.session.storage;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import com.edge.http.FileUtils;
import com.edge.http.servlet.HttpSessionWrapper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.IsNot.not;

public class FileSessionStorageTest {

    private static final String VALID_SESSION_ID = "sessionidsjdfhgskldjfsghldkfjsgg";
    private static final String ILLEGAL_SESSION_ID = "////////////////////////////////";
    private static FileSessionStorage fileSessionStorage;
    private static String workingDirectory;

    @BeforeClass
    public static void setUp() throws IOException {
        workingDirectory = FileUtils.createTempDirectory();
        fileSessionStorage = new FileSessionStorage(workingDirectory);
    }

    @AfterClass
    public static void cleanUp() {
        new File(workingDirectory).delete();
    }

    @Test
    public void shouldPersistRestoreAndRemoveSession() throws IOException {
        HttpSessionWrapper sessionWrapper = new HttpSessionWrapper(VALID_SESSION_ID);
        sessionWrapper.setAttribute("attributeName", "SomeValue");
        fileSessionStorage.persistSession(sessionWrapper);

        sessionWrapper = fileSessionStorage.getSession(VALID_SESSION_ID);
        assertThat(sessionWrapper, is(not(nullValue())));
        assertThat((String) sessionWrapper.getAttribute("attributeName"), is("SomeValue"));

        fileSessionStorage.removeSession(sessionWrapper);

        sessionWrapper = fileSessionStorage.getSession(VALID_SESSION_ID);
        assertThat(sessionWrapper, is(nullValue()));
    }

    @Test
    public void shouldPersistSessionAndOverWriteFile() throws IOException {
        HttpSessionWrapper sessionWrapper = new HttpSessionWrapper(VALID_SESSION_ID);
        sessionWrapper.setAttribute("attributeName", "SomeValue");
        fileSessionStorage.persistSession(sessionWrapper);

        sessionWrapper = fileSessionStorage.getSession(VALID_SESSION_ID);
        assertThat(sessionWrapper, is(not(nullValue())));
        assertThat((String) sessionWrapper.getAttribute("attributeName"), is("SomeValue"));

        HttpSessionWrapper session2Wrapper = new HttpSessionWrapper(VALID_SESSION_ID);
        session2Wrapper.setAttribute("otherName", "OtherValue");
        fileSessionStorage.persistSession(session2Wrapper);

        sessionWrapper = fileSessionStorage.getSession(VALID_SESSION_ID);
        assertThat(sessionWrapper, is(not(nullValue())));
        assertThat(sessionWrapper.getAttribute("attributeName"), is(nullValue()));
        assertThat((String) sessionWrapper.getAttribute("otherName"), is("OtherValue"));

        fileSessionStorage.removeSession(sessionWrapper);

        sessionWrapper = fileSessionStorage.getSession(VALID_SESSION_ID);
        assertThat(sessionWrapper, is(nullValue()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldValidateSessionNameLength() throws IOException {
        HttpSessionWrapper sessionWrapper = new HttpSessionWrapper("abcX8");
        fileSessionStorage.persistSession(sessionWrapper);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldValidateSessionNameNull() throws IOException {
        HttpSessionWrapper sessionWrapper = new HttpSessionWrapper(null);
        fileSessionStorage.persistSession(sessionWrapper);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldValidateSessionNameIllegalCharacters() throws IOException {
        HttpSessionWrapper sessionWrapper = new HttpSessionWrapper(ILLEGAL_SESSION_ID);
        fileSessionStorage.persistSession(sessionWrapper);
    }

    @Test
    public void shouldReturnNullOnInvalidSessionName() throws IOException {
        assertThat(fileSessionStorage.getSession(null), is(nullValue()));
        assertThat(fileSessionStorage.getSession("abcX8"), is(nullValue()));
        assertThat(fileSessionStorage.getSession("/asdfghjklzxasdfghjklzxasdfghjklzxz"), is(nullValue()));
    }

    @Test
    public void shouldFailSilentlyOnInvalidFileContents() throws IOException {
        String sid = "asdfghjklzxasdfghjklzxasdfghjklz";
        File sessionFile = new File(workingDirectory + sid + "_session");
        if (sessionFile.exists() && !sessionFile.delete()) {
            throw new IOException("Unable to delete file " + sessionFile.getAbsolutePath());
        }
        if (!sessionFile.createNewFile()) {
            throw new IOException("Unable to create new file " + sessionFile.getAbsolutePath());
        }
        assertThat(fileSessionStorage.getSession(sid), is(nullValue()));
    }

    @Test(expected = IOException.class)
    public void shouldThrowExceptionWhenUnableToCreateSessionDirectory() throws IOException {
        String nonWritableDirectory = FileUtils.createTempDirectory();
        new File(nonWritableDirectory).setWritable(false);

        FileSessionStorage fileSessionStorage = new FileSessionStorage(nonWritableDirectory);
        HttpSessionWrapper sessionWrapper = new HttpSessionWrapper(VALID_SESSION_ID);
        sessionWrapper.setAttribute("attributeName", "SomeValue");
        fileSessionStorage.persistSession(sessionWrapper);
    }
}
