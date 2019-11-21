package ru.cft.focusstart.figures;

import ru.cft.focusstart.exceptions.WrongParameterException;

public class Circle implements Figure {

    private double radius;

    Circle(double radius) {
        if (radius < 0) {
            throw new WrongParameterException();
        } else {
            this.radius = radius;
        }
    }

    public String getTitle() {
        return "Круг";
    }

    public double getSquare() {
        double square = Math.PI * (Math.pow(radius, 2));
        return Math.round(square * 100.0) / 100.0;
    }

    public double getPerimeter() {
        double perimeter = Math.PI * 2 * radius;
        return Math.round(perimeter * 100.0) / 100.0;
    }

    private double getRadius() {
        return radius;
    }

    private double getDiameter() {
        return radius * 2;
    }

    @Override
    public String toString() {
        return "Тип фигуры: " + getTitle() + System.lineSeparator() +
                "Площадь: " + getSquare() + System.lineSeparator() +
                "Периметр: " + getPerimeter() + System.lineSeparator() +
                "Радиус: " + getRadius() + System.lineSeparator() +
                "Диаметр: " + getDiameter();
    }
}
