package ru.cft.focusstart;

import org.apache.commons.io.FileUtils;
import ru.cft.focusstart.exceptions.NoInputFileException;
import ru.cft.focusstart.exceptions.RedundantArgumentException;
import ru.cft.focusstart.exceptions.WrongStringIndexException;
import ru.cft.focusstart.figures.Figure;
import ru.cft.focusstart.figures.Figures;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

class FigureService {

    private File inputFile;
    private File outputFile;
    private boolean isConsoleOutput;
    private Figures typeOfFigure;
    private List<Double> figureParams = new ArrayList<>();

    void createFigureWithParams(String[] args) throws IOException {
        prepareFiles(args);
        getFigureParams(inputFile);
        FigureBuilder figureBuilder = new FigureBuilder(typeOfFigure, figureParams);
        Figure figure = figureBuilder.getFigureWithParams();
        outputFigure(figure);
    }

    private File getInputFile(String[] files) {
        return new File(files[0]);
    }

    private File getOutputFile(String[] files) {
        return new File(files[1]);
    }

    private void prepareFiles(String[] files) {
        if (files.length == 2) {
            inputFile = getInputFile(files);
            outputFile = getOutputFile(files);
        } else if (files.length == 1) {
            inputFile = getInputFile(files);
            isConsoleOutput = true;
        } else if (files.length > 2) {
            throw new RedundantArgumentException();
        } else {
            throw new NoInputFileException();
        }
    }

    private long countLines(File file) throws IOException {
        try (Stream<String> lines = Files.lines(file.toPath())) {
            return lines.count();
        }
    }

    private String getSpecificString(File file, int stringNumber) throws IOException {
        String specificLine;
        try (Stream<String> lines = Files.lines(file.toPath())) {
            specificLine = lines.skip(stringNumber).findFirst().orElseThrow(WrongStringIndexException::new);
        }
        return specificLine;
    }

    private void getFigureParams(File file) throws IOException {
        long rowCount = countLines(file);
        typeOfFigure = Figures.valueOf(getSpecificString(file, 0));
        for (int i = 1; i < rowCount; i++) {
            figureParams.add(Double.parseDouble(getSpecificString(file, i)));
        }
    }

    private void outputFigure(Figure figure) throws IOException {
        if (!isConsoleOutput) {
            FileUtils.writeStringToFile(outputFile, String.valueOf(figure), Charset.defaultCharset());
        } else {
            System.out.println(figure);
        }
    }
}
