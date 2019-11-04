package ru.cft.focusstart;

import java.util.InputMismatchException;

public class Main {

    public static void main(String[] args) {

        Table table = new Table();

        try {
            table.makeTable();
        } catch (InputMismatchException e) {
            System.out.println("Invalid input: Not an integer.");
        } catch (IllegalArgumentException e) {
            System.out.println("Out of range. Input an integer from 1 to 32.");
        }
    }

}
