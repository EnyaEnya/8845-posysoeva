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
    public void rangeTestMock() throws IOException {
        when(reader.readInt(anyString())).thenReturn(33);
        TableBuilder tableBuilder = new TableBuilder();
        tableBuilder.buildTable(reader, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rangeTest1() throws IOException {
        Table table = new Table();
        table.makeTable(33, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rangeTest2() throws IOException {
        Table table = new Table();
        table.makeTable(0, null);
    }

    @Test
    public void tableTest() throws IOException {
        Table table = new Table();
        StringWriter writer = new StringWriter();
        table.makeTable(3, writer);
        Assert.assertEquals("1|2|3\n" +
                "-+-+-\n" +
                "2|4|6\n" +
                "-+-+-\n" +
                "3|6|9\n", writer.toString());
    }
}
