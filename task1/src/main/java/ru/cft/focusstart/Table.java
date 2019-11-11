package ru.cft.focusstart;

import java.io.IOException;
import java.io.Writer;

public class Table {

    private int size;
    private Writer writer;

    public Table(int size, Writer writer) {
        this.size = size;
        this.writer = writer;
    }

    public void renderTable() throws IOException {
        String horizontalMark = markHorizontalBorders();
        for (int i = 1; i <= size; i++) {
            for (int j = 1; j <= size; j++) {
                int result = i * j;
                String space = getSpaces(result);
                writer.write(space);
                if (j != size) {
                    writer.write(String.format("%d|", result));
                } else {
                    writer.write(String.valueOf(result));
                }
            }
            writer.write("\n");
            if (i != size) {
                writer.write(horizontalMark + "\n");
            }
        }
    }

    private String markHorizontalBorders() {
        String mark = "";
        int numLength = countLongestNum();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < numLength; j++) {
                mark += "-";
            }
            if (i != size - 1) {
                mark += "+";
            }
        }
        return mark;
    }

    private String getSpaces(int result) {
        String spaces = "";
        int resultSpaces = countLongestNum() - Integer.toString(result).length();
        for (int i = 0; i < resultSpaces; i++) {
            spaces += " ";
        }
        return spaces;
    }

    private int countLongestNum() {
        int longestNum = size * size;
        return Integer.toString(longestNum).length();
    }

//    private void validate() {
//        if (size < 1 || size > 32) {
//            throw new IllegalArgumentException();
//        }
//    }
}
