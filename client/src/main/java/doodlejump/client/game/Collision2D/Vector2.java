package doodlejump.client.game.Collision2D;

public final class Vector2
{
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

    ///divide
    public final Vector2 DivideByDouble(double divideBy) {
        if (divideBy == 0) {
            return null;
        } else {
            double newX = this.x / divideBy;
            double newY = this.y / divideBy;
            return new Vector2(newX,newY);
        }
    }

    public final static Vector2 DivideByDouble(Vector2 v, double divideBy) {
        if (divideBy == 0) {
            return null;
        } else {
            double newX = v.x / divideBy;
            double newY = v.y / divideBy;
            return new Vector2(newX,newY);
        }
    }

    public final void DivideThisByDouble(double divideBy) {
        if (divideBy == 0) {
            this.SetToZero();
        } else {
            this.x /= divideBy;
            this.y /= divideBy;
        }
    }

    public final Vector2 DivideXByDouble(double divideBy) {
        if (divideBy == 0) {
            return null;
        } else {
            double newX = this.x / divideBy;
            return new Vector2(newX,this.y);
        }
    }

    public final static Vector2 DivideXByDouble(Vector2 v, double divideBy) {
        if (divideBy == 0) {
            return null;
        } else {
            double newX = v.x / divideBy;
            return new Vector2(newX,v.y);
        }
    }

    public final Vector2 DivideYByDouble(double divideBy) {
        if (divideBy == 0) {
            return null;
        } else {
            double newY = this.y / divideBy;
            return new Vector2(this.x,newY);
        }
    }

    public final static Vector2 DivideYByDouble(Vector2 v, double divideBy) {
        if (divideBy == 0) {
            return null;
        } else {
            double newY = v.y / divideBy;
            return new Vector2(v.x,newY);
        }
    }
    //////

    ///normalize
    public final Vector2 Normalize()
    {
        double m = this.GetMagnitude();
        return new Vector2(this.x /= m,this.y /= m);
    }

    public final static Vector2 Normalize(Vector2 v)
    {
        return v.Normalize();
    }

    public final void NormalizeThis()
    {
        double m = this.GetMagnitude();
        this.x /= m;
        this.y /= m;
    }
    //////


    ///magnitude
    public final double GetMagnitude()
    {
        return Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2));
    }

    public final static double GetMagnitude(Vector2 v)
    {
        return Math.sqrt(Math.pow(v.x, 2) + Math.pow(v.y, 2));
    }
    //////

    ///add
    public final Vector2 Add(Vector2 v)
    {
        return new Vector2(this.x + v.x, this.y + v.y);
    }

    public final static Vector2 Add(Vector2 v1, Vector2 v2)
    {
        return new Vector2(v1.x + v2.x, v1.y + v2.y);
    }

    public final void AddToThis(Vector2 v)
    {
        this.x += v.x;
        this.y += v.y;
    }
    //////

    ///Subtract
    public final Vector2 subtract(Vector2 v)
    {
        return new Vector2(this.x - v.x, this.y - v.y);
    }

    public final static Vector2 subtract(Vector2 v1, Vector2 v2)
    {
        return new Vector2(v1.x - v2.x, v1.y - v2.y);
    }

    public final void subtractFromThis(Vector2 v)
    {
        this.x -= v.x;
        this.y -= v.y;
    }
    //////

    ///multiply
    public final Vector2 MultiplyByInt(int m)
    {
        return new Vector2(this.x*m,this.y*m);
    }

    public final static Vector2 MultiplyByInt(Vector2 v, int m)
    {
        return new Vector2(v.x*m,v.y*m);
    }

    public final void MultiplyThisByInt(int m)
    {
        this.x *= m;
        this.y *= m;
    }

    public final Vector2 MultiplyByDouble(double m)
    {
        return new Vector2(this.x*m,this.y*m);
    }

    public final static Vector2 MultiplyByDouble(Vector2 v, double m)
    {
        return new Vector2(v.x*m,v.y*m);
    }

    public final void MultiplyThisByDouble(double m)
    {
        this.x *= m;
        this.y *= m;
    }
    //////

    public final void SetToZero()
    {
        this.x = 0;
        this.y = 0;
    }

    public final boolean IsZero()
    {
        return (this.x == 0 && this.y == 0);
    }

    public final static boolean IsZero(Vector2 v)
    {
        return (v.x == 0 && v.y == 0);
    }

    public final boolean Equals(Vector2 other) {
        return (this.x == other.x && this.y == other.y);
    }

    public final static boolean Equals(Vector2 v1, Vector2 v2) {
        return (v1.x == v2.x && v1.y == v2.y);
    }

    ///dot product
    public final double Dot(Vector2 v)
    {
        return this.x*v.x + this.y * v.y;
    }

    public final static double Dot(Vector2 v1, Vector2 v2)
    {
        return v1.x*v2.x + v1.y * v2.y;
    }
    //////

    ///distance
    public final double Distance(Vector2 other) {
        double xDist = other.x - this.x;
        double yDist = other.y - this.y;
        return Math.sqrt(xDist*xDist + yDist*yDist);
    }

    public final static double Distance(Vector2 vec1, Vector2 vec2) {
        double xDist = vec2.x - vec1.x;
        double yDist = vec2.y - vec1.y;
        return Math.sqrt(xDist*xDist + yDist*yDist);
    }
    //////
}