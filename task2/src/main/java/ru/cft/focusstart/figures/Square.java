package ru.cft.focusstart.figures;

public class Square implements Figure {

    protected double width;

    public Square(double width) {
        this.width = width;
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
        return "Тип фигуры: " + getTitle() + "\n" +
                "Площадь: " + getSquare() + "\n" +
                "Периметр: " + getPerimeter() + "\n" +
                "Сторона: " + width + "\n" +
                "Диагональ: " + getDiagonal();
    }
}
