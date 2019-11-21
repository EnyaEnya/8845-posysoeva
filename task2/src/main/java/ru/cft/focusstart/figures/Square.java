package ru.cft.focusstart.figures;

import ru.cft.focusstart.exceptions.WrongParameterException;

public class Square implements Figure {

    double width;

    Square(double width) {
        if (width < 0) {
            throw new WrongParameterException();
        } else {
            this.width = width;
        }
    }

    public String getTitle() {
        return "Квадрат";
    }

    public double getSquare() {
        double square = Math.pow(width, 2);
        return Math.round(square * 100.0) / 100.0;
    }

    public double getPerimeter() {
        double perimeter = width * 4;
        return Math.round(perimeter * 100.0) / 100.0;
    }

    public double getDiagonal() {
        double diagonal = width * Math.sqrt(2);
        return Math.round(diagonal * 100.0) / 100.0;
    }

    @Override
    public String toString() {
        return "Тип фигуры: " + getTitle() + System.lineSeparator() +
                "Площадь: " + getSquare() + System.lineSeparator() +
                "Периметр: " + getPerimeter() + System.lineSeparator() +
                "Сторона: " + width + System.lineSeparator() +
                "Диагональ: " + getDiagonal();
    }
}
