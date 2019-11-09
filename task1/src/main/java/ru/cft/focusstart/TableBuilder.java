package ru.cft.focusstart;

import java.io.IOException;
import java.io.Writer;

public class TableBuilder {

    public void buildTable(InputReader inputReader, Writer writer) throws IOException {
        Table table = new Table();
        table.makeTable(inputReader.readInt("Input an integer from 1 to 32: "), writer);
        writer.flush();
    }
}
