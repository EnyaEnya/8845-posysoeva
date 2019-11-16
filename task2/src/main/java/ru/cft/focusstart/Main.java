package ru.cft.focusstart;

import ru.cft.focusstart.exceptions.*;

import java.io.IOException;

public class Main {

    private static void showExceptionMessage(String message) {
        System.out.println(message);
        System.exit(1);
    }

    public static void main(String... args) {
        FigureService figureService = new FigureService();
        try {
            figureService.createFigureWithParams(args);
        } catch (NoInputFileException e) {
            showExceptionMessage("There is no input file, add one input file");
        } catch (WrongStringIndexException e) {
            showExceptionMessage("Internal exception: wrong string index");
        } catch (RedundantArgumentException e) {
            showExceptionMessage("There are redundant arguments");
        } catch (NoSuchFigureException e) {
            showExceptionMessage("Wrong type of figure");
        } catch (MissingRequiredParameterException e) {
            showExceptionMessage("There is no required parameter in input file");
        } catch (IOException |
                RuntimeException e) {
            showExceptionMessage("Problem with input-output files");
        }
    }
}
