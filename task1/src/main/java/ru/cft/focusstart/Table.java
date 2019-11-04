package ru.cft.focusstart;

import java.util.Scanner;

public class Table {

    private int number;

    public void makeTable() {
        System.out.print("Input an integer from 1 to 32: ");
        Scanner in = new Scanner(System.in);
        number = in.nextInt();
        validate();
        renderTable();
        in.close();
    }

    private void renderTable() {
        String horizontalMark = markHorizontalBorders();
        for (int i = 1; i <= number; i++) {
            for (int b = 1; b <= number; b++) {
                int result = i * b;
                String space = getSpace(result);
                System.out.print(space);
                if (b != number) {
                    System.out.printf("%d|", result);
                } else {
                    System.out.print(result);
                }
            }
            System.out.println();
            if (i != number) {
                System.out.println(horizontalMark);
            }
        }
    }

    private String markHorizontalBorders() {
        String mark = "";
        int numLength = countLongestNum();
        for (int i = 0; i < number; i++) {
            for (int j = 0; j < numLength; j++) {
                mark += "-";
            }
            if (i != number - 1) {
                mark += "+";
            }
        }
        return mark;
    }

    private String getSpace(int result) {
        String mark = "";
        int resultSpaces = countLongestNum() - Integer.toString(result).length();
        for (int i = 0; i < resultSpaces; i++) {
            mark += " ";
        }
        return mark;
    }

    private int countLongestNum() {
        int longestNum = number * number;
        return Integer.toString(longestNum).length();
    }

    private void validate() {
        if (number < 1 || number > 32) {
            throw new IllegalArgumentException();
        }
    }
}
