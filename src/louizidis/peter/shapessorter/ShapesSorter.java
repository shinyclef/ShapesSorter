package louizidis.peter.shapessorter;

import louizidis.peter.shapessorter.shapes.Shape;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Author: Peter Louizidis
 * Date: 3/10/13
 * Time: 6:14 PM
 * Description: The main class of the ShapesSorter program. Its purpose is to take an input file with information on
 *              various shapes, provide information on those shapes when requested, and and produce a sorted output
 *              file if requested. It is assumed that the unit of measurement is consistent int the input file.
 * Notes:   This is one possible solution for the technical exercise presented to applicants of the REA Group 2014
 *          Graduate Program. As the problem is very open, the challenge I have set myself is to produce a solution
 *          with the smallest possible input file size with least software dependencies as possible. An excel
 *          spread-sheet may be more suitable than a text file, or even a database depending on the requirements and
 *          scale of the theoretical scenario, but for the purposes of this exercise I will use a simple .txt file.
 *          This is 'not' designed to be a user friendly final product. Obviously, there's no way for you to know
 *          that I wrote this myself. I did this mostly because I have the time and I felt like it.
 */

public class ShapesSorter
{
    private static final String INPUT_FILENAME = "Shapes.txt";
    private static final String OUTPUT_FILENAME = "SortedShapes.txt";
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args)
    {
        instantiateShapesFromInputFile();
        runMainMenu();
    }

    /* Handles the instantiation of shape objects from the input file. */
    private static void instantiateShapesFromInputFile()
    {

        Path path = getFilePath(INPUT_FILENAME);
        Scanner scanner = null;
        try
        {
            scanner = new Scanner(path);
        }
        catch (IOException e)
        {
            //Do some proper error handling etc...
            System.exit(3);
        }

        while (scanner.hasNextLine())
        {
            String line = scanner.nextLine();
            if (!line.startsWith("#") && !line.isEmpty())
            {
                Shape.instantiateShapeFromInputString(line);
            }
        }
    }

    /* Runs the main menu and parses user input. */
    private static void runMainMenu()
    {
        String input = "";
        while (!input.equalsIgnoreCase("x"))
        {
            //display the menu
            System.out.println("What do you wanna do?");
            System.out.println("1: Show details of a shape.");
            System.out.println("2: Write sorted output file.");
            System.out.println("x: Exit.");

            //get a parse user selection
            input = scanner.nextLine();
            switch (input.toLowerCase())
            {
                case "1": //show details
                    showShapeDetailsOption();
                    break;

                case "2": //write output
                    writeOutputFileOption();
                    break;

                case "x": //this will exit
                    break;

                default:
                    input = ""; //bad input
                    System.out.println("Sorry, choose an option from the list.");
                    break;
            }
        }
    }

    /* Asks user for a shape ID, and prints out information about that shape. */
    private static void showShapeDetailsOption()
    {
        //ask user for shape ID
        System.out.println("Enter a shape ID.");
        String input = scanner.nextLine();
        int shapeID;

        //try converting the input to an integer
        try
        {
            shapeID = Integer.parseInt(input);
        }
        catch (NumberFormatException e)
        {
            System.out.println("Sorry, that's not a valid shape ID. It must be an integer.");
            return;
        }

        //make sure there is a shape with that ID
        if (!Shape.getShapesMap().containsKey(shapeID))
        {
            System.out.println("There is no shape with that ID.");
            return;
        }

        //get and print that shape's details
        Shape shape = Shape.getShapesMap().get(shapeID);
        String type = shape.getSubType();
        double area = shape.getSurfaceArea();
        double perimeter = shape.getPermimeter();

        System.out.println("\nID: " + shapeID);
        System.out.println("Type: " + type);
        System.out.println("Area: " + area);
        System.out.println("Perimeter: " + perimeter + "\n");
    }

    /* Sorts all shapes based on shape type, and prints a list of those shapes to the output file. */
    private static void writeOutputFileOption()
    {
        Map<String, Map<String, Set<Integer>>> sortedMap = getSortedShapesMap();
        Path path = getFilePath(OUTPUT_FILENAME);

        //inform user of file location
        System.out.println("Writing output file: " + path);

        //create the file if it doesn't exist
        File file = new File(path.toUri());
        try
        {
            file.createNewFile();
        }
        catch (IOException e)
        {
            //do proper error handling here
            e.printStackTrace();
            return;
        }

        Charset ENCODING = StandardCharsets.UTF_8;
        try (BufferedWriter writer = Files.newBufferedWriter(path, ENCODING))
        {
            //loop through the base types
            for (Map.Entry<String, Map<String, Set<Integer>>> baseType : sortedMap.entrySet())
            {
                //write the base name as a heading
                String baseName = baseType.getKey();
                writer.write(baseName);
                writer.newLine();

                //loop through the subtypes
                for (Map.Entry<String, Set<Integer>> subType : baseType.getValue().entrySet())
                {
                    String subName = subType.getKey();
                    writer.write("\t" + subName);
                    writer.newLine();

                    for (Integer ID : subType.getValue())
                    {
                        writer.write("\t\t" + ID.toString());
                        writer.newLine();
                    }
                }
            }
        }
        catch (IOException e)
        {
            //do proper error handling etc...
            return;
        }

    }

    private static Map<String, Map<String, Set<Integer>>> getSortedShapesMap()
    {
        /* We're going to create some temporary collections to hold the IDs of various shapes.
         * For this demonstration, I'm going to assume that an alphabetical order of shapes is ok. */

        //Here is a hierarchy of collections. base-shape map -> sub-shape map -> set of IDs
        Map<String, Map<String, Set<Integer>>> baseShape = new TreeMap<>();

        baseShape.put("Triangle", new LinkedHashMap<String, Set<Integer>>());
        baseShape.put("Quadrilateral", new LinkedHashMap<String, Set<Integer>>());

        //loop through shapes and put them where they belong
        for (Shape s : Shape.getShapesMap().values())
        {
            //first, add the base type if we haven't encountered this base type before
            Map<String, Set<Integer>> baseTypeMap = baseShape.get(s.getBaseType());
            if (baseTypeMap == null)
            {
                baseShape.put(s.getBaseType(), new LinkedHashMap<String, Set<Integer>>());
                baseTypeMap = baseShape.get(s.getBaseType());
            }

            //next if we have not encountered this subtype, add it too
            Set<Integer> subTypeSet = baseTypeMap.get(s.getSubType());
            if (subTypeSet == null)
            {
                baseTypeMap.put(s.getSubType(), new LinkedHashSet<Integer>());
                subTypeSet = baseTypeMap.get(s.getSubType());
            }

            //put this shapeID into the set
            subTypeSet.add(s.getShapeID());
        }

        return baseShape;
    }


    /* A utility method for rounding numbers to a specified amount of decimal places.
     * @param unrounded The unrounded number to be rounded.
     * @param precision The number of decimal places to round the number to.
     * @return The rounded number as a double. */
    public static double round(double unrounded, int precision)
    {
        BigDecimal bd = new BigDecimal(unrounded);
        BigDecimal rounded = bd.setScale(precision, BigDecimal.ROUND_HALF_UP);
        return rounded.doubleValue();
    }

    /* Utility method that returns a path belong to the given filename within the working directory of the .jar file.
    *  @param fileName The file name of the file for which to create a path.
    *  @return The path consisting of the .jar working directory and the filename. */
    private static Path getFilePath(String fileName)
    {
        String jarDir = "";

        try
        {
            File jarFile = new File(ShapesSorter.class.getProtectionDomain()
                    .getCodeSource().getLocation().toURI().getPath());
            jarDir = jarFile.getPath();
        }
        catch (URISyntaxException e)
        {
            //Do some proper error handling etc...
            System.exit(2);
        }

        if (jarDir.equals(""))
        {
            return null;
        }
        return Paths.get(jarDir + File.separator + fileName);
    }
}
