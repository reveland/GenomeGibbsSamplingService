package com.genome.comparer.io;

import java.io.IOException;
import java.util.List;

public class NewickReader {

    private LineReader lineReader;

    public String read(String inputPath) throws IOException {
        String result = "";
        lineReader = new LineReader();

        List<String> lines = lineReader.readLines(inputPath);
        for (String line : lines) {
            result += line;
        }
        return result;
    }
}
