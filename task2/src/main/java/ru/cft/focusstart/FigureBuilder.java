package ru.cft.focusstart;

import ru.cft.focusstart.figures.Figure;
import ru.cft.focusstart.figures.Figures;

import java.util.List;

class FigureBuilder {

    private Figures typeOfFigure;
    private List<Double> figureParams;

    FigureBuilder(Figures typeOfFigure, List<Double> figureParams) {
        this.typeOfFigure = typeOfFigure;
        this.figureParams = figureParams;
    }

    Figure getFigureWithParams() {
        return typeOfFigure.createFigure(figureParams);
    }

}

