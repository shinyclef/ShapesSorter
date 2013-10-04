package louizidis.peter.shapessorter.shapes;

import louizidis.peter.shapessorter.ShapesSorter;

/**
 * Author: Peter Louizidis
 * Date: 3/10/13
 * Time: 6:16 PM
 * Description: Represents an abstract triangle. The super-type for all specific types of triangles.
 */

public class Triangle extends Shape
{
    /* Strings for the base shape and names of sub-shapes we are checking for. */
    public static final String BASE_SHAPE = "Triangle";
    public static final String EQUILATERAL = "Equilateral Triangle";
    public static final String ISOSCELES = "Isosceles Triangle";
    public static final String SCALENE = "Scalene Triangle";

    private double sideALength;
    private double sideBLength;
    private double sideCLength;
    private float angleAB;

    /* @param sideALength Length of side A.
    *  @param sideBLength Length of side B.
    *  @param angleAB Angle in degrees for the inner side of the corner formed between sides A and B */
    protected Triangle(int shapeID, float sideALength, float sideBLength, float angleAB)
    {
        super(shapeID, BASE_SHAPE);
        this.sideALength = sideALength;
        this.sideBLength = sideBLength;
        this.angleAB = angleAB;

        //calculate side C to help with other functions, if other functions are rarely needed this does not have
        //to occur during instantiation
        //c2 = a2 + b2 - 2ab * cos(angleAB)
        this.sideCLength = Math.sqrt((sideALength * sideALength) + (sideBLength * sideBLength)
            - (2 * sideALength * sideBLength * Math.cos(Math.toRadians(angleAB))));

        //set the type of triangle
        subType = getShapeTypeLabel();
    }

    /* Here we will check for the type of triangle and set the type index accordingly. */
    public String getShapeTypeLabel()
    {
        //if side A == side B, it's either equilateral or isosceles depending on the angle.
        if (sideALength == sideBLength)
        {
            if (angleAB == 60)
            {
                return EQUILATERAL;
            }
            else
            {
                return ISOSCELES;
            }
        }

        //It could still be isosceles based on side C
        if (sideCLength == sideALength || sideCLength == sideBLength)
        {
            return ISOSCELES;
        }

        //no two sides are equal, it's scalene
        return SCALENE;
    }

    public double getSurfaceArea()
    {
        //area = 1/2 sideA * sideB * sin(angleAB)
        return ShapesSorter.round(0.5 * sideALength * sideBLength * Math.sin(Math.toRadians(angleAB)), 2);
    }

    @Override
    public double getPermimeter()
    {
        return ShapesSorter.round(sideALength + sideBLength + sideCLength, 2);
    }
}
