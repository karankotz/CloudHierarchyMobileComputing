package com.edge.http;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

import com.edge.http.impl.ServerConfigImplTest;
import com.edge.http.utilities.IOUtilities;

public final class FileUtils {

    private FileUtils() {
    }

    /**
     * Creates a temporary directory and returns its path.
     * The directory will be deleted on JVM close.
     *
     * @return
     * @throws IOException
     */
    public static String createTempDirectory() throws IOException {
        File file = Files.createTempDirectory(ServerConfigImplTest.class.getName()).toFile();
        file.deleteOnExit();
        return file.getAbsolutePath() + "/";
    }

    /**
     * Writes contents to a temporary file and returns the file.
     *
     * @param contents
     * @return
     * @throws IOException
     */
    public static File writeToTempFile(String contents) throws IOException {
        File file = File.createTempFile("temp", ".util");
        file.deleteOnExit();
        writeToFile(file, contents);
        return file;
    }

    /**
     * Writes string contents to the given file.
     *
     * @param file
     * @param contents
     * @throws IOException
     */
    public static void writeToFile(File file, String contents) throws IOException {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(file));
            writer.write(contents);
        } finally {
            if (writer != null) {
                IOUtilities.closeSilently(writer);
            }
        }
    }
}
