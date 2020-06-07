package doodlejump.client.game.collision;

public final class Vector2 {
    public double x;
    public double y;

    public Vector2() {
        this.x = 0.0d;
        this.y = 0.0d;
    }

    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2(Vector2 v) {
        this.x = v.x;
        this.y = v.y;
    }

    public final static Vector2 divideByDouble(Vector2 v, double divideBy) {
        if (divideBy == 0) {
            return null;
        } else {
            double newX = v.x / divideBy;
            double newY = v.y / divideBy;
            return new Vector2(newX, newY);
        }
    }

    public final static Vector2 divideXByDouble(Vector2 v, double divideBy) {
        if (divideBy == 0) {
            return null;
        } else {
            double newX = v.x / divideBy;
            return new Vector2(newX, v.y);
        }
    }

    public final static Vector2 divideYByDouble(Vector2 v, double divideBy) {
        if (divideBy == 0) {
            return null;
        } else {
            double newY = v.y / divideBy;
            return new Vector2(v.x, newY);
        }
    }

    public final static double getMagnitude(Vector2 v) {
        return Math.sqrt(Math.pow(v.x, 2) + Math.pow(v.y, 2));
    }

    public final static Vector2 add(Vector2 v1, Vector2 v2) {
        return new Vector2(v1.x + v2.x, v1.y + v2.y);
    }

    public final static Vector2 subtract(Vector2 v1, Vector2 v2) {
        return new Vector2(v1.x - v2.x, v1.y - v2.y);
    }
    //////

    public final static Vector2 multiplyByInt(Vector2 v, int m) {
        return new Vector2(v.x * m, v.y * m);
    }

    public final static Vector2 multiplyByDouble(Vector2 v, double m) {
        return new Vector2(v.x * m, v.y * m);
    }

    public final static boolean isZero(Vector2 v) {
        return (v.x == 0 && v.y == 0);
    }
    //////

    public final static boolean equals(Vector2 v1, Vector2 v2) {
        return (v1.x == v2.x && v1.y == v2.y);
    }

    public final static double dot(Vector2 v1, Vector2 v2) {
        return v1.x * v2.x + v1.y * v2.y;
    }
    //////

    public final static double distance(Vector2 vec1, Vector2 vec2) {
        double xDist = vec2.x - vec1.x;
        double yDist = vec2.y - vec1.y;
        return Math.sqrt(xDist * xDist + yDist * yDist);
    }

    public final static Vector2 normalize(Vector2 v) {
        return v.normalize();
    }

    ///divide
    public final Vector2 divideByDouble(double divideBy) {
        if (divideBy == 0) {
            return null;
        } else {
            double newX = this.x / divideBy;
            double newY = this.y / divideBy;
            return new Vector2(newX, newY);
        }
    }
    //////

    public final void divideThisByDouble(double divideBy) {
        if (divideBy == 0) {
            this.setToZero();
        } else {
            this.x /= divideBy;
            this.y /= divideBy;
        }
    }

    public final Vector2 divideXByDouble(double divideBy) {
        if (divideBy == 0) {
            return null;
        } else {
            double newX = this.x / divideBy;
            return new Vector2(newX, this.y);
        }
    }

    public final Vector2 divideYByDouble(double divideBy) {
        if (divideBy == 0) {
            return null;
        } else {
            double newY = this.y / divideBy;
            return new Vector2(this.x, newY);
        }
    }

    ///normalize
    public final Vector2 normalize() {
        double m = this.getMagnitude();
        return new Vector2(this.x /= m, this.y /= m);
    }

    public final void normalizeThis() {
        double m = this.getMagnitude();
        this.x /= m;
        this.y /= m;
    }
    //////


    ///magnitude
    public final double getMagnitude() {
        return Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2));
    }

    ///add
    public final Vector2 add(Vector2 v) {
        return new Vector2(this.x + v.x, this.y + v.y);
    }

    public final void addToThis(Vector2 v) {
        this.x += v.x;
        this.y += v.y;
    }

    ///Subtract
    public final Vector2 subtract(Vector2 v) {
        return new Vector2(this.x - v.x, this.y - v.y);
    }

    public final void subtractFromThis(Vector2 v) {
        this.x -= v.x;
        this.y -= v.y;
    }
    //////

    ///multiply
    public final Vector2 multiplyByInt(int m) {
        return new Vector2(this.x * m, this.y * m);
    }

    public final void multiplyThisByInt(int m) {
        this.x *= m;
        this.y *= m;
    }

    public final Vector2 multiplyByDouble(double m) {
        return new Vector2(this.x * m, this.y * m);
    }

    public final void multiplyThisByDouble(double m) {
        this.x *= m;
        this.y *= m;
    }

    public final void setToZero() {
        this.x = 0;
        this.y = 0;
    }

    public final boolean isZero() {
        return (this.x == 0 && this.y == 0);
    }

    public final boolean equals(Vector2 other) {
        return (this.x == other.x && this.y == other.y);
    }
    //////

    ///dot product
    public final double dot(Vector2 v) {
        return this.x * v.x + this.y * v.y;
    }

    ///distance
    public final double distance(Vector2 other) {
        double xDist = other.x - this.x;
        double yDist = other.y - this.y;
        return Math.sqrt(xDist * xDist + yDist * yDist);
    }
    //////
}