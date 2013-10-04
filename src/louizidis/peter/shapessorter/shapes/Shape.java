package louizidis.peter.shapessorter.shapes;

import java.util.*;

/**
 * Author: Peter Louizidis
 * Date: 3/10/13
 * Time: 6:15 PM
 * Description: Represents an abstract shape. The super-type for all shape objects. Units of measurement
 *              are undefined. It is assumed that the unit of measurement is consistent in the input file.
 */

public abstract class Shape
{
    private static Map<Integer, Shape> shapesMap = new HashMap<>(); //stores all instantiated shapes objects

    private int shapeID;
    protected String baseType;
    protected String subType;   //Integers are much cheaper to store than Strings, but as this is a demo,
                                //I'm keeping things simple and not worrying about the redundancy of having
                                //many identical strings. You can store a type ID here instead and use a
                                //lookup table to get the actual String label small efficiency gains are important.


    protected Shape(int shapeID, String baseType)
    {
        this.shapeID = shapeID;
        this.baseType = baseType;
    }

    /* Takes one line from the input file and instantiates a shape from it.
         * @param inputString One line of data from the input file. */
    public static void instantiateShapeFromInputString(String inputString)
    {
        //first, we're going to split the inputString on space characters, then try converting each string to a float.
        String[] stringPart = inputString.split(" ");
        float[] part = new float[stringPart.length];
        try
        {
            for (int i = 0; i < stringPart.length; i++)
            {
                part[i] = Float.parseFloat(stringPart[i]);
            }
        }
        catch (NumberFormatException e)
        {
            //Do some proper error handling etc...
//            System.exit(4);
        }

        if (part.length < 4) //this is the minimum parts there should be
        {
            //do proper error handling...
            System.out.println("Skipping item...");
            return;
        }

        int shapeID = (int)part[0]; //I'm aware of how bad this is to do haha. Just a demo app so go easy! :P
        int sides = (int)part[1];

        switch (sides)
        {
            case 1:
                if (part.length != 4)
                {
                    //do proper error handling...
                    System.out.println("Case 1 error...");
                    return;
                }
                shapesMap.put(shapeID, new Ellipse(shapeID, part[2], part[3]));
                break;

            case 3:
                if (part.length != 5)
                {
                    //do proper error handling...
                    System.out.println("Case 3 error...");
                    return;
                }
                shapesMap.put(shapeID, new Triangle(shapeID, part[2], part[3], part[4]));
                break;

            case 4:
                if (part.length != 4)
                {
                    //do proper error handling...
                    System.out.println("Case 4 error...");
                    return;
                }
                shapesMap.put(shapeID, new Quadrilateral(shapeID, part[2], part[3]));
                break;

            default:
                //do proper error handling...
                System.out.println("Default case triggered...");
                return;
        }

    }

    /* @Return The surface area for this shape. */
    public abstract double getSurfaceArea();

    /* @Return The perimeter for this shape. */
    public abstract double getPermimeter();

    /* Determines the concrete subtype shape based on data available about the shape. */
    public abstract String getShapeTypeLabel();

    /* @return A string label indicating the name for this type of base shape. eg. "Quadrilateral" */
    public String getBaseType()
    {
        return baseType;
    }

    /* @return A string label indicating the specific name for this type of shape. eg. "Rectangle" */
    public String getSubType()
    {
        return subType;
    }

    /* @return The shape ID for this shape. */
    public int getShapeID()
    {
        return shapeID;
    }

    public static Map<Integer, Shape> getShapesMap()
    {
        return shapesMap;
    }
}