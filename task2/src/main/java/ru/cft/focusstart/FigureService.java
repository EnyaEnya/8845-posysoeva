package ru.cft.focusstart;

import org.apache.commons.io.FileUtils;
import ru.cft.focusstart.exceptions.MissingRequiredParameterException;
import ru.cft.focusstart.exceptions.NoInputFileException;
import ru.cft.focusstart.exceptions.RedundantArgumentException;
import ru.cft.focusstart.figures.Figure;
import ru.cft.focusstart.figures.Figures;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class FigureService {

    void displayDescriptionOfFigure(String[] args) throws IOException {
        validateFiles(args);
        File inputFile = getInputFile(args);
        validateInputFile(inputFile);
        File outputFile = getOutputFile(args);

        List<String> fileContent = getStringList(inputFile);
        validate(fileContent);
        Figures typeOfFigure = Figures.valueOf(fileContent.get(0));
        List<Double> figureParams = fileContent.stream().skip(1).map(Double::valueOf).collect(Collectors.toList());

        Figure figure = typeOfFigure.createFigure(figureParams);
        createOutputFigure(figure, outputFile);
    }

    private void validate(List<String> fileContent) {
        if (fileContent.size() < 2) {
            throw new MissingRequiredParameterException();
        }
    }

    private void validateInputFile(File input) {
        if (!input.exists()) {
            throw new NoInputFileException();
        }
    }

    private File getInputFile(String[] files) {
        return new File(files[0]);
    }

    private File getOutputFile(String[] files) {
        if (files.length < 2) {
            return null;
        }
            return new File(files[1]);
    }

    private void validateFiles(String[] files) {
        if (files.length > 2) {
            throw new RedundantArgumentException();
        } else if (files.length == 0) {
            throw new NoInputFileException();
        }
    }

    private List<String> getStringList(File file) throws IOException {
        try (Stream<String> stream = Files.lines(file.toPath())) {
            return stream.collect(Collectors.toList());
        }
    }

    private void createOutputFigure(Figure figure, File outputFile) throws IOException {
        if (outputFile != null && outputFile.exists()) {
            FileUtils.writeStringToFile(outputFile, String.valueOf(figure), Charset.defaultCharset());
        } else {
            System.out.println(figure);
        }
    }
}
