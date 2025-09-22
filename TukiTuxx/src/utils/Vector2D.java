package utils;

public class Vector2D {
    public double x;
    public double y;

    // Constructores
    public Vector2D() {
        this.x = 0;
        this.y = 0;
    }

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    // Suma de vectores
    public Vector2D add(Vector2D other) {
        return new Vector2D(this.x + other.x, this.y + other.y);
    }

    // Suma con el vector actual
    public void addSelf(Vector2D other) {
        this.x += other.x;
        this.y += other.y;
    }

    // Resta de vectores
    public Vector2D subtract(Vector2D other) {
        return new Vector2D(this.x - other.x, this.y - other.y);
    }

    // Multiplicación por un escalar
    public Vector2D multiply(double scalar) {
        return new Vector2D(this.x * scalar, this.y * scalar);
    }

    // Magnitud del vector
    public double magnitude() {
        return Math.sqrt(x * x + y * y);
    }

    // Normalizar el vector
    public Vector2D normalize() {
        double mag = magnitude();
        if (mag == 0) return new Vector2D(0, 0);
        return new Vector2D(x / mag, y / mag);
    }

    @Override
    public String toString() {
        return "Vector2D(" + x + ", " + y + ")";
    }
    public double getX() { return x; }
    public double getY() { return y; }

}
