package ru.cft.focusstart.figures;

public class Rectangle extends Square implements Figure {

    private double length;

    Rectangle(double side1, double side2) {
        super(Math.min(side1, side2));
        this.length = Math.max(side1, side2);
    }

    @Override
    public String getTitle() {
        return "Прямоугольник";
    }

    @Override
    public double getSquare() {
        double square = width * length;
        return Math.round(square * 100.0) / 100.0;
    }

    @Override
    public double getPerimeter() {
        double perimeter = (width * 2) + (length * 2);
        return Math.round(perimeter * 100.0) / 100.0;
    }

    @Override
    public double getDiagonal() {
        double diagonal = Math.sqrt(Math.pow(length, 2) + Math.pow(width, 2));
        return Math.round(diagonal * 100.0) / 100.0;
    }

    @Override
    public String toString() {
        return "Тип фигуры: " + getTitle() + "\n" +
                "Площадь: " + getSquare() + "\n" +
                "Периметр: " + getPerimeter() + "\n" +
                "Длина: " + length + "\n" +
                "Ширина: " + width + "\n" +
                "Диагональ: " + getDiagonal();
    }
}
