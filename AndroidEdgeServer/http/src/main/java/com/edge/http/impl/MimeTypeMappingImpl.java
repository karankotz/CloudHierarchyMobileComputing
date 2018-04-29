package com.edge.http.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import com.edge.http.MimeTypeMapping;
import com.edge.http.utilities.IOUtilities;

public class MimeTypeMappingImpl implements MimeTypeMapping {

    private static final Charset CHARSET = Charset.forName("UTF-8");

    private String defaultMimeType;
    private Map<String, String> mapping;

    public MimeTypeMappingImpl() {
        defaultMimeType = "text/html";
        mapping = new HashMap<>();
    }

    public static MimeTypeMapping createFromStream(InputStream in, String defaultMimeType)
            throws IOException {
        MimeTypeMappingImpl mimeTypeMapping = (MimeTypeMappingImpl) createFromStream(in);
        mimeTypeMapping.defaultMimeType = defaultMimeType;
        return mimeTypeMapping;
    }

    public static MimeTypeMapping createFromStream(InputStream in) throws IOException {
        MimeTypeMappingImpl mimeTypeMapping = new MimeTypeMappingImpl();

        InputStreamReader inputStreamReader = new InputStreamReader(in, CHARSET);

        BufferedReader input = new BufferedReader(inputStreamReader);
        String line;
        while ((line = input.readLine()) != null) {
            String mime[] = line.split(" ");
            for (int i = 1; i < mime.length; i++) {
                mimeTypeMapping.mapping.put(mime[i].toLowerCase(), mime[0]);
            }
        }
        IOUtilities.closeSilently(inputStreamReader);
        return mimeTypeMapping;
    }

    @Override
    public String getMimeTypeByExtension(String extension) {
        if (extension != null) {
            String extensionNormalized = extension.toLowerCase();
            if (mapping.containsKey(extensionNormalized)) {
                return mapping.get(extensionNormalized.toLowerCase());
            }
        }

        return defaultMimeType;
    }
}
