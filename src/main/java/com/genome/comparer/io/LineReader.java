package com.genome.comparer.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class LineReader {

    public List<String> readLines(String inputPath) {
        List<String> lines = new ArrayList<String>();
        try {
            FileInputStream inputStream = new FileInputStream(new File(inputPath));
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String strLine;
            while ((strLine = br.readLine()) != null) {
                lines.add(strLine);
            }
            br.close();
        } catch (IOException e) {
            System.out.println("Can\'t read file");
            e.printStackTrace();
        }
        return lines;
    }
}
