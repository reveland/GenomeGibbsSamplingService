package com.genome.comparer.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class reads the 8 Vertebrates genomes and selects those synteny blocks
 * that are available in all genomes
 */
public class GenomeDestiller {

    public static void main(String[] args) throws IOException {
        int[] counts = new int[2186];
        List<List<String>> lines = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(new File("./data/HMMRDCOC_100_300.perm")));
        List<String> currentReaders = new ArrayList<>();
        String s;
        s = reader.readLine();
        currentReaders.add(s);
        while ((s = reader.readLine()) != null) {
            if (s.length() != 0 && s.charAt(0) != '>') {
                currentReaders.add(s);
                if (s.charAt(0) != '#') {
                    String[] a = s.split(" ");
                    System.out.println("length of a:" + a.length + "\n" + s);
                    for (int i = 0; i < a.length - 1; i++) {
                        counts[Math.abs(Integer.parseInt(a[i]))]++;
                    }
                }
            } else if (s.length() != 0 && s.charAt(0) == '>') {
                lines.add(currentReaders);
                currentReaders = new ArrayList<>();
                currentReaders.add(s);
            }
        }
        lines.add(currentReaders);
        for (int i = 0; i < counts.length; i++) {
            System.out.print(counts[i] + " ");
            if (i % 50 == 49) {
                System.out.println();
            }
        }
        FileWriter fw = new FileWriter(new File("./data/destilled.txt"));
        for (List<String> line : lines) {
            currentReaders = line;
            for (String CurrentReader : currentReaders) {
                s = CurrentReader;
                if (s.charAt(0) == '>' || s.charAt(0) == '#') {
                    fw.write(s + "\n");
                } else {
                    String[] a = s.split(" ");
                    for (int k = 0; k < a.length - 1; k++) {
                        if (counts[Math.abs(Integer.parseInt(a[k]))] == 8) {
                            fw.write(a[k] + " ");
                        }
                    }
                    fw.write("$\n");
                }

            }
        }
        fw.close();
    }
}
