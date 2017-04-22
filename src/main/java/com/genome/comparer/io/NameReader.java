package com.genome.comparer.io;

import java.io.IOException;
import java.util.List;

public class NameReader {

    private LineReader lineReader;
    private String delimiter_;

    public NameReader(String delimiter) {
        delimiter_ = delimiter;
    }

    public String[] read(String inputPath) throws IOException {
        lineReader = new LineReader();
        List<String> lines = lineReader.readLines(inputPath);
        return lines.get(0).split(delimiter_);
    }
}
