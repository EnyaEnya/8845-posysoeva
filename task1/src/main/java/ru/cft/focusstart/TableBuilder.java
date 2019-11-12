package ru.cft.focusstart;

import java.io.IOException;
import java.io.Writer;

public class TableBuilder {

    public void buildTable(InputReader inputReader, Writer writer) throws IOException {
        int size = inputReader.readInt("Input an integer from 1 to 32: ");
        validate(size);
        Table table = new Table(size, writer);
        table.renderTable();
        writer.flush();
    }


    private void validate(int size) {
        if (size < 1 || size > 32) {
            throw new IllegalArgumentException();
        }
    }
}
