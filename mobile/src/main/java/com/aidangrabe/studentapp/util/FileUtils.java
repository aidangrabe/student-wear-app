package com.aidangrabe.studentapp.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * Created by aidan on 08/01/15.
 *
 */
public class FileUtils {

    public static String getFileContents(InputStream inputStream) throws IOException {

        String line;
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        while ((line = reader.readLine()) != null) {
            sb.append(line);
            sb.append("\n");
        }

        reader.close();
        inputStream.close();

        return sb.toString();

    }

    public static void putFileContents(String string, OutputStream outputStream) throws IOException {

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
        writer.write(string);
        writer.close();
        outputStream.close();

    }

}
