package ru.cft.focusstart;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MultiplicationTableTests {

    private InputReader reader = mock(InputReader.class);

    @Test(expected = IllegalArgumentException.class)
    public void rangeTest1() throws IOException {
        when(reader.readInt(anyString())).thenReturn(33);
        TableBuilder tableBuilder = new TableBuilder();
        tableBuilder.buildTable(reader, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rangeTest2() throws IOException {
        when(reader.readInt(anyString())).thenReturn(0);
        TableBuilder tableBuilder = new TableBuilder();
        tableBuilder.buildTable(reader, null);
    }

    @Test
    public void tableTest() throws IOException {
        StringWriter writer = new StringWriter();
        Table table = new Table(3, writer);
        table.renderTable();
        Assert.assertEquals("1|2|3\n" +
                "-+-+-\n" +
                "2|4|6\n" +
                "-+-+-\n" +
                "3|6|9\n", writer.toString());
    }
}
