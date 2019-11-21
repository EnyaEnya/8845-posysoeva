package ru.cft.focusstart;

import ru.cft.focusstart.exceptions.*;

import java.io.IOException;

public class Main {

    private static void showExceptionMessage(String message) {
        System.out.println(message);
    }

    public static void main(String[] args) {
        FigureService figureService = new FigureService();
        try {
            figureService.displayDescriptionOfFigure(args);
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
        } catch (IllegalArgumentException e) {
            showExceptionMessage("Invalid data in input file");
        } catch (WrongParameterException e) {
            showExceptionMessage("Wrong value in figure parameters. Value must be greater than zero.");
        } catch (IOException |
                RuntimeException e) {
            showExceptionMessage("Problem with input-output files");
        }
    }
}
