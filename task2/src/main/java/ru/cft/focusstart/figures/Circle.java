package ru.cft.focusstart.figures;

public class Circle implements Figure {

    private double radius;

    Circle(double radius) {
        this.radius = radius;
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
        return "Тип фигуры: " + getTitle() + "\n" +
                "Площадь: " + getSquare() + "\n" +
                "Периметр: " + getPerimeter() + "\n" +
                "Радиус: " + getRadius() + "\n" +
                "Диаметр: " + getDiameter();
    }
}
