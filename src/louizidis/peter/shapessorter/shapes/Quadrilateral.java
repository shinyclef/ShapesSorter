package louizidis.peter.shapessorter.shapes;

import louizidis.peter.shapessorter.ShapesSorter;

/**
 * Author: Peter Louizidis
 * Date: 3/10/13
 * Time: 6:21 PM
 * Description: Represents a quadrilateral.
 */

public class Quadrilateral extends Shape
{
    /* Strings for the base shape and names of sub-shapes we are checking for. */
    public static final String BASE_SHAPE = "Quadrilateral";
    public static final String RECTANGLE = "Rectangle";
    public static final String SQUARE = "Square";

    private float sideALength;
    private float sideBLength;

    /* @param sideALength Length of side A.
    *  @param sideBLength Length of side B where side B is perpendicular to side A. */
    public Quadrilateral(int shapeID, float sideALength, float sideBLength)
    {
        super(shapeID, BASE_SHAPE);
        this.sideALength = sideALength;
        this.sideBLength = sideBLength;
        subType = getShapeTypeLabel();
    }

    @Override
    public String getShapeTypeLabel()
    {
        if (sideALength == sideBLength)
        {
            return SQUARE;
        }
        else
        {
            return RECTANGLE;
        }
    }

    @Override
    public double getSurfaceArea()
    {
        return ShapesSorter.round(sideALength * sideBLength, 2);
    }

    @Override
    public double getPermimeter()
    {
        return ShapesSorter.round(sideALength * 2 + sideBLength * 2, 2);
    }
}
