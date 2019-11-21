package ru.cft.focusstart.figures;

import ru.cft.focusstart.exceptions.MissingRequiredParameterException;

import java.util.List;

public enum Figures {

    CIRCLE {
        @Override
        public Circle createFigure(List<Double> figureParams) {
            validateParams(figureParams, 1);
            return new Circle(figureParams.get(0));
        }
    },

    RECTANGLE {
        @Override
        public Rectangle createFigure(List<Double> figureParams) {
            validateParams(figureParams, 2);
            return new Rectangle(figureParams.get(0), figureParams.get(1));
        }
    },

    SQUARE {
        @Override
        public Square createFigure(List<Double> figureParams) {
            validateParams(figureParams, 1);
            return new Square(figureParams.get(0));
        }
    };

    public abstract Figure createFigure(List<Double> figureParams);

    public void validateParams(List<Double> figureParams, int size) {
        if (figureParams.size() < size) {
            throw new MissingRequiredParameterException();
        }
    }
}
