package louizidis.peter.shapessorter.shapes;

import louizidis.peter.shapessorter.ShapesSorter;

/**
 * Author: Peter Louizidis
 * Date: 3/10/13
 * Time: 8:18 PM
 * Description: Represents an abstract single-sided two-dimensional shape, such as a circle or an oval.
 * Notes: Indeed it has a name besides "single-sided two-dimensional entity". :)
 */

public class Ellipse extends Shape
{
    /* Strings for the base shape and names of sub-shapes we are checking for. */
    public static final String BASE_SHAPE = "Ellipse";
    public static final String CIRCLE = "Circle";
    public static final String OVAL = "Oval";

    private float semiMajorAxis; //the largest radius of an ellipse
    private float semiMinorAxis; //the smallest radius of an ellipse

    /* @param semiMajorAxis The largest radius.
     * @param semiMinorAxis The smallest radius. */
    public Ellipse(int shapeID, float semiMajorAxis, float semiMinorAxis)
    {
        super(shapeID, BASE_SHAPE);
        this.semiMajorAxis = semiMajorAxis;
        this.semiMinorAxis = semiMinorAxis;
        subType = getShapeTypeLabel();
    }

    @Override
    public String getShapeTypeLabel()
    {
        if (semiMajorAxis == semiMinorAxis)
        {
            return CIRCLE;
        }
        else
        {
            return OVAL;
        }
    }

    @Override
    public double getSurfaceArea()
    {
        return ShapesSorter.round(Math.PI * semiMajorAxis * semiMinorAxis, 2);
    }

    @Override
    public double getPermimeter()
    {
        //this is an approximation, ellipse perimeters are apparently not practical to calculate accurately
        return ShapesSorter.round(Math.PI * (3 * (semiMajorAxis + semiMinorAxis)
                - Math.sqrt(3 * semiMajorAxis + semiMinorAxis) * (semiMajorAxis + 3 * semiMinorAxis)), 2);
    }
}
