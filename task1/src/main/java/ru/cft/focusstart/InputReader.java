package ru.cft.focusstart;

import java.util.Scanner;

public class InputReader {

    public int readInt(String message) {
        System.out.print(message);
        try (Scanner in = new Scanner(System.in)) {
            return in.nextInt();
        }
    }
}
