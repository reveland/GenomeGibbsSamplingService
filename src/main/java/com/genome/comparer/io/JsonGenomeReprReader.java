package com.genome.comparer.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import com.genome.comparer.domain.repr.BlockRepr;
import com.genome.comparer.domain.repr.ChromosomeRepr;
import com.genome.comparer.domain.repr.GenomeRepr;
import com.google.gson.stream.JsonReader;

public class JsonGenomeReprReader {

    private String inputJsonPath_;

    public JsonGenomeReprReader(String inputJsonPath) {
        inputJsonPath_ = inputJsonPath;
    }

    public GenomeRepr readJsonStream() throws IOException {
        FileInputStream inputStream = new FileInputStream(new File(inputJsonPath_));
        InputStreamReader in = new InputStreamReader(inputStream, "UTF-8");
        try (JsonReader reader = new JsonReader(in)) {
            return parseGenomeRepr(reader);
        }
    }

    private GenomeRepr parseGenomeRepr(JsonReader reader) throws IOException {
        GenomeRepr genome = new GenomeRepr();

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("label")) {
                genome.setName(reader.nextString());
            } else if (name.equals("original")) {
                reader.beginArray();
                while (reader.hasNext()) {
                    genome.addChromosome(parseChromosomeRepr(reader));
                }
                reader.endArray();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();

        return genome;
    }

    private ChromosomeRepr parseChromosomeRepr(JsonReader reader) throws IOException {
        ChromosomeRepr chromosome = new ChromosomeRepr();

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("block")) {
                reader.beginArray();
                while (reader.hasNext()) {
                    chromosome.addBlock(parseBlockRepr(reader));
                }
                reader.endArray();
            } else if (name.equals("size")) {
                chromosome.setSize(reader.nextInt());
            } else if (name.equals("label")) {
                chromosome.setLabel(reader.nextString());
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();

        return chromosome;
    }

    private BlockRepr parseBlockRepr(JsonReader reader) throws IOException {
        BlockRepr block = new BlockRepr();

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("size")) {
                block.setSize(reader.nextInt());
            } else if (name.equals("id")) {
                block.setRefChrLabel(reader.nextString());
            } else if (name.equals("squares")) {
                parseSquares(reader, block);
            } else if (name.equals("transversal")) {
                parseTransversals(reader, block);
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();

        return block;
    }

    private void parseSquares(JsonReader reader, BlockRepr block) throws IOException {
        reader.beginArray();
        while (reader.hasNext()) {
            reader.beginObject();

            int begin = -1;
            int end = -1;
            while (reader.hasNext()) {
                String nameInSquare = reader.nextName();
                if (nameInSquare.equals("begin")) {
                    begin = reader.nextInt();
                } else if (nameInSquare.equals("end")) {
                    end = reader.nextInt();
                } else {
                    reader.skipValue();
                }
            }

            if (begin >= 0 && end >= 0) {
                //                block.addSquare(end - begin);
            }
            reader.endObject();
        }
        reader.endArray();
    }

    private void parseTransversals(JsonReader reader, BlockRepr block) throws IOException {
        reader.beginArray();
        while (reader.hasNext()) {
            //            block.addTransversal(parseTransversalRepr(reader));
        }
        reader.endArray();
    }

    //    public TransversalRepr parseTransversalRepr(JsonReader reader) throws IOException {
    //        double beginX = 0.0;
    //        double beginY = 0.0;
    //        double endX = 0.0;
    //        double endY = 0.0;
    //
    //        reader.beginObject();
    //        while (reader.hasNext()) {
    //            String name = reader.nextName();
    //            if (name.equals("beginX")) {
    //                beginX = reader.nextDouble();
    //            } else if (name.equals("beginY")) {
    //                beginY = reader.nextDouble();
    //            } else if (name.equals("endX")) {
    //                endX = reader.nextDouble();
    //            } else if (name.equals("endY")) {
    //                endY = reader.nextDouble();
    //            } else {
    //                reader.skipValue();
    //            }
    //        }
    //        reader.endObject();
    //
    //        return new TransversalRepr(beginX, beginY, endX, endY);
    //    }
}
