package com.genome.comparer.io;

import java.io.IOException;
import java.util.List;

public class MatrixReader {

	private LineReader lineReader;
	private String delimiter;

	public MatrixReader(String delimiter) {
		this.delimiter = delimiter;
	}

	public double[][] read(String inputPath) throws IOException {
		double[][] result;

		lineReader = new LineReader();

		List<String> lines = lineReader.readLines(inputPath);

		int rowNr = lines.size();
		double[] firstRow = processLine(lines.get(0));
		int colNr = firstRow.length;
		result = new double[rowNr][colNr];

		result[0] = firstRow;
		for (int i = 1; i < lines.size(); ++i) {
			result[i] = processLine(lines.get(i));
		}
		return result;
	}

	public double[] processLine(String lineStr) {
		String[] split = lineStr.split(delimiter);
		double[] result = new double[split.length];
		for (int i = 0; i < split.length; ++i) {
			result[i] = Double.parseDouble(split[i]);
		}
		return result;
	}
}
