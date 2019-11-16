package ru.cft.focusstart.figures;

import java.util.List;

public enum Figures {

    CIRCLE {
        @Override
        public Circle createFigure(List<Double> figureParams) {
            return new Circle(figureParams.get(0));
        }
    },

    RECTANGLE {
        @Override
        public Rectangle createFigure(List<Double> figureParams) {
            return new Rectangle(figureParams.get(0), figureParams.get(1));
        }
    },

    SQUARE {
        @Override
        public Square createFigure(List<Double> figureParams) {
            return new Square(figureParams.get(0));
        }
    };

    public abstract Figure createFigure(List<Double> figureParams);
}
