package ru.cft.focusstart;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.InputMismatchException;

public class Main {

    public static void main(String[] args) {

        InputReader reader = new InputReader();
        Table table = new Table();

        try (Writer writer = new PrintWriter(System.out)) {
            table.makeTable(reader.readInt("Input an integer from 1 to 32: "), writer);
            writer.flush();
        } catch (InputMismatchException e) {
            System.out.println("Invalid input: Not an integer.");
        } catch (IllegalArgumentException e) {
            System.out.println("Out of range. Input an integer from 1 to 32.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
