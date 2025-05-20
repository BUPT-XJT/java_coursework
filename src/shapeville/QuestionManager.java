package shapeville;

import java.util.*;
import java.text.DecimalFormat;

/**
 * Manages all question data, logic for selecting questions,
 * calculating answers, and tracking progress within tasks.
 *
 * !!! THIS IS A CRITICAL FILE THAT YOU NEED TO POPULATE WITH DATA !!!
 * The example data below is minimal. You must add all shapes, dimensions,
 * pre-calculated answers, and solution breakdowns as per the project
 * specification.
 */
public class QuestionManager {
    private static final DecimalFormat df = new DecimalFormat("#.##"); // For formatting answers

    // Task 1: Shape Identification
    private List<ShapeData> shapes2D;
    private List<ShapeData> shapes3D;
    private int currentShape2DIndex;
    private Set<String> identified2DShapeIdsThisSession; // Tracks unique shapes identified
    private int currentShape3DIndex;
    private Set<String> identified3DShapeIdsThisSession;

    // Task 2: Angle Identification
    private List<AngleType> angleTypesToIdentifyInSession; // The 4 types targeted for this session
    private Map<AngleType, Boolean> identifiedAngleTypesThisSession; // Tracks identified types

    // Task 3: Area Calculation (Basic Shapes)
    private List<String> basicAreaShapeTypes; // e.g., "Rectangle", "Parallelogram", etc.
    private int currentBasicAreaShapeTypeIndex;
    private Set<String> practicedBasicAreaShapesThisSession;

    // Task 4: Circle Calculation
    // Combinations: RadiusArea, DiameterArea, RadiusCircumference,
    // DiameterCircumference
    private List<String> circleCalcTaskKeys;
    private int currentCircleCalcTaskKeyIndex;
    private Set<String> practicedCircleCalcKeysThisSession;

    // Bonus 1: Compound Shapes
    private List<Map<String, Object>> compoundShapesDataList; // Data for all 9 shapes from Fig 10
    private int currentCompoundShapeIndex;
    private Set<String> practicedCompoundShapeIdsThisSession;

    // Bonus 2: Sector Area
    private List<Map<String, Object>> sectorAreaDataList; // Data for all 8 sectors from Fig 13
    private int currentSectorAreaIndex;
    private Set<String> practicedSectorIdsThisSession;

    private Random random = new Random();

    public QuestionManager() {
        initializeAllData();
        resetAllTaskProgress();
    }

    private void initializeAllData() {
        load2DShapeData();
        load3DShapeData();
        loadAngleIdentificationTaskData();
        loadBasicAreaShapeTypes();
        loadCircleCalcTaskKeys();
        loadCompoundShapeData(); // <<< YOU MUST POPULATE THIS METHOD
        loadSectorAreaData(); // <<< YOU MUST POPULATE THIS METHOD
    }

    public void resetAllTaskProgress() {
        currentShape2DIndex = 0;
        identified2DShapeIdsThisSession = new HashSet<>();
        Collections.shuffle(shapes2D);

        currentShape3DIndex = 0;
        identified3DShapeIdsThisSession = new HashSet<>();
        Collections.shuffle(shapes3D);

        // For Angle ID, select 4 distinct types for the session
        List<AngleType> allTypes = new ArrayList<>(Arrays.asList(AngleType.values()));
        Collections.shuffle(allTypes);
        angleTypesToIdentifyInSession = allTypes.subList(0, Math.min(4, allTypes.size()));
        identifiedAngleTypesThisSession = new HashMap<>();
        for (AngleType type : angleTypesToIdentifyInSession) {
            identifiedAngleTypesThisSession.put(type, false);
        }

        currentBasicAreaShapeTypeIndex = 0;
        practicedBasicAreaShapesThisSession = new HashSet<>();
        Collections.shuffle(basicAreaShapeTypes);

        currentCircleCalcTaskKeyIndex = 0;
        practicedCircleCalcKeysThisSession = new HashSet<>();
        Collections.shuffle(circleCalcTaskKeys);

        currentCompoundShapeIndex = 0;
        practicedCompoundShapeIdsThisSession = new HashSet<>();
        if (compoundShapesDataList != null)
            Collections.shuffle(compoundShapesDataList);

        currentSectorAreaIndex = 0;
        practicedSectorIdsThisSession = new HashSet<>();
        if (sectorAreaDataList != null)
            Collections.shuffle(sectorAreaDataList);
    }

    public void resetTaskProgress(String taskTypeConstant) {
        // Called when a specific task panel starts
        switch (taskTypeConstant) {
            case "SHAPE_ID_2D":
                currentShape2DIndex = 0;
                identified2DShapeIdsThisSession.clear();
                Collections.shuffle(shapes2D);
                break;
            case "SHAPE_ID_3D":
                currentShape3DIndex = 0;
                identified3DShapeIdsThisSession.clear();
                Collections.shuffle(shapes3D);
                break;
            case "ANGLE_ID":
                List<AngleType> allTypes = new ArrayList<>(Arrays.asList(AngleType.values()));
                Collections.shuffle(allTypes);
                angleTypesToIdentifyInSession = allTypes.subList(0, Math.min(4, allTypes.size()));
                identifiedAngleTypesThisSession.clear();
                for (AngleType type : angleTypesToIdentifyInSession) {
                    identifiedAngleTypesThisSession.put(type, false);
                }
                break;
            case "AREA_CALC":
                currentBasicAreaShapeTypeIndex = 0;
                practicedBasicAreaShapesThisSession.clear();
                Collections.shuffle(basicAreaShapeTypes);
                break;
            case "CIRCLE_CALC":
                currentCircleCalcTaskKeyIndex = 0;
                practicedCircleCalcKeysThisSession.clear();
                Collections.shuffle(circleCalcTaskKeys);
                break;
            case "COMPOUND_SHAPE":
                currentCompoundShapeIndex = 0;
                practicedCompoundShapeIdsThisSession.clear();
                if (compoundShapesDataList != null)
                    Collections.shuffle(compoundShapesDataList);
                break;
            case "SECTOR_AREA":
                currentSectorAreaIndex = 0;
                practicedSectorIdsThisSession.clear();
                if (sectorAreaDataList != null)
                    Collections.shuffle(sectorAreaDataList);
                break;
        }
    }

    // --- Data Loading Methods (YOU NEED TO COMPLETE THESE) ---
    private void load2DShapeData() {
        shapes2D = new ArrayList<>();
        // From Figure 1
        shapes2D.add(new ShapeData("circle", "circle.png", false));
        shapes2D.add(new ShapeData("rectangle", "rectangle.png", false));
        shapes2D.add(new ShapeData("triangle", "triangle.png", false));
        shapes2D.add(new ShapeData("oval", "oval.png", false)); // "ellipse" might be a synonym
        shapes2D.add(new ShapeData("square", "square.png", false));
        shapes2D.add(new ShapeData("rhombus", "rhombus.png", false));
        shapes2D.add(new ShapeData("pentagon", "pentagon.png", false));
        shapes2D.add(new ShapeData("hexagon", "hexagon.png", false));
        shapes2D.add(new ShapeData("heptagon", "heptagon.png", false));
        shapes2D.add(new ShapeData("octagon", "octagon.png", false));
        shapes2D.add(new ShapeData("kite", "kite.png", false));
        // Consider adding synonyms if needed, or handle them in check logic
    }

    private void load3DShapeData() {
        shapes3D = new ArrayList<>();
        // From Figure 2
        shapes3D.add(new ShapeData("cube", "cube.png", true));
        shapes3D.add(new ShapeData("cuboid", "cuboid.png", true)); // "rectangular prism"
        shapes3D.add(new ShapeData("cylinder", "cylinder.png", true));
        shapes3D.add(new ShapeData("sphere", "sphere.png", true));
        shapes3D.add(new ShapeData("triangular prism", "triangular_prism.png", true));
        shapes3D.add(new ShapeData("square-based pyramid", "square_pyramid.png", true)); // or "square pyramid"
        shapes3D.add(new ShapeData("cone", "cone.png", true));
        shapes3D.add(new ShapeData("tetrahedron", "tetrahedron.png", true)); // "triangular pyramid"
    }

    private void loadAngleIdentificationTaskData() {
        // angleTypesToIdentifyInSession is populated in resetAllTaskProgress or
        // resetTaskProgress("ANGLE_ID")
    }

    private void loadBasicAreaShapeTypes() {
        basicAreaShapeTypes = Arrays.asList("Rectangle", "Parallelogram", "Triangle", "Trapezium");
    }

    private void loadCircleCalcTaskKeys() {
        circleCalcTaskKeys = Arrays.asList(
                "RadiusArea", "DiameterArea",
                "RadiusCircumference", "DiameterCircumference");
    }

    // ---------->>> YOU MUST COMPLETE THE FOLLOWING TWO METHODS <<<----------
    private void loadCompoundShapeData() {
        compoundShapesDataList = new ArrayList<>();
        // EXAMPLE (Shape 1 from your previous example, using Fig 10 Shape 1 values)
        // Rect: 14cm base, 5cm height (part of total height of outer shape if it's a
        // house like shape)
        // Triangle on top: 14cm base, (e.g. 5cm height if outer total height is 10cm
        // for the house part)
        // THIS IS JUST A PLACEHOLDER STRUCTURE.
        // YOU NEED TO ANALYZE EACH OF THE 9 SHAPES IN FIGURE 10.
        // Map<String, Object> shape1 = new HashMap<>();
        // shape1.put("id", "_Fig10_1");
        // shape1.put("name", "Compound Shape (Fig 10 - 1)"); // Use a descriptive name
        // shape1.put("image", "compound1.png"); // Ensure this image exists

        // Example for a shape made of a 14x5 rectangle and a triangle of base 14,
        // height 3
        // double rectArea1 = 14 * 5; // 70
        // double triArea1 = 0.5 * 14 * 3; // 21
        // double totalArea1 = rectArea1 + triArea1; // 91
        // shape1.put("area", totalArea1);

        // StringBuilder sol1 = new StringBuilder();
        // sol1.append("Split into a rectangle and a triangle.\n");
        // sol1.append("Rectangle: 14cm × 5cm = ").append(df.format(rectArea1)).append("
        // cm²\n");
        // sol1.append("Triangle: 0.5 × 14cm × 3cm =
        // ").append(df.format(triArea1)).append(" cm²\n");
        // sol1.append("Total Area = ").append(df.format(rectArea1)).append(" +
        // ").append(df.format(triArea1))
        // .append(" = ").append(df.format(totalArea1)).append(" cm²");
        // shape1.put("solution_breakdown", sol1.toString());
        // compoundShapesDataList.add(shape1);

        // ... ADD ALL 8 OTHER COMPOUND SHAPES FROM FIGURE 10 HERE ...

        Map<String, Object> shape2 = new HashMap<>();
        shape2.put("id", "CS_Fig10_2");
        shape2.put("name", "Compound Shape (Fig 10-2)");
        shape2.put("image", "compound2.png");
        double area2_rect_top = 20.0 * (20.0 - 10.0); // 20 * 11 = 220
        double area2_rect_bottom_left = 11.0 * 10.0; // 110
        double totalArea2 = area2_rect_top + area2_rect_bottom_left; // 330
        shape2.put("area", totalArea2);
        StringBuilder sol2 = new StringBuilder();
        sol2.append("Decompose into two rectangles:\n");
        sol2.append("1. Top Rectangle: 20 cm × (21-10) cm = 20 cm × 11 cm = ").append(df.format(area2_rect_top))
                .append(" cm²\n");
        sol2.append("2. Bottom-Left Rectangle: 11 cm × 10 cm = ").append(df.format(area2_rect_bottom_left))
                .append(" cm²\n");
        sol2.append("Total Area = ").append(df.format(area2_rect_top)).append(" + ")
                .append(df.format(area2_rect_bottom_left))
                .append(" = ").append(df.format(totalArea2)).append(" cm²");
        shape2.put("solution_breakdown", sol2.toString());
        compoundShapesDataList.add(shape2);

        Map<String, Object> shape3 = new HashMap<>();
        shape3.put("id", "CS_Fig10_3");
        shape3.put("name", "Compound Shape (Fig 10-3)");
        shape3.put("image", "compound3.png");
        double area3_rect_left = 18.0 * 19.0; // 342
        double area3_rect_right = 16.0 * 16.0; // 256
        double totalArea3 = area3_rect_left + area3_rect_right; // 598
        shape3.put("area", totalArea3);
        StringBuilder sol3 = new StringBuilder();
        sol3.append("Decompose into two rectangles:\n");
        sol3.append("1. Left Rectangle: 18 cm × 19 cm = ").append(df.format(area3_rect_left)).append(" cm²\n");
        sol3.append("2. Right Rectangle: 16 cm × 16 cm = ").append(df.format(area3_rect_right)).append(" cm²\n");
        sol3.append("Total Area = ").append(df.format(area3_rect_left)).append(" + ")
                .append(df.format(area3_rect_right))
                .append(" = ").append(df.format(totalArea3)).append(" cm²");
        shape3.put("solution_breakdown", sol3.toString());
        compoundShapesDataList.add(shape3);

        Map<String, Object> shape4 = new HashMap<>();
        shape4.put("id", "CS_Fig10_4");
        shape4.put("name", "Compound Shape (Fig 10-4)");
        shape4.put("image", "compound4.png");
        double area4_bottom = 24.0 * 6.0; // 144
        double area4_top = 12.0 * 12.0; // 144
        double totalArea4 = area4_bottom + area4_top; // 216
        shape4.put("area", totalArea4);
        StringBuilder sol4 = new StringBuilder();
        sol4.append("Decompose into three rectangles (from bottom to top):\n");
        sol4.append("1. Bottom Rectangle: 24 m × 6 m = ").append(df.format(area4_bottom)).append(" m²\n");
        sol4.append("2. Top Rectangle: 12 m × 12 m = ").append(df.format(area4_top)).append(" m²\n");
        sol4.append("Total Area = ").append(df.format(area4_bottom)).append(" + ").append(df.format(area4_top))
                .append(" = ").append(df.format(totalArea4)).append(" m²");
        shape4.put("solution_breakdown", sol4.toString());
        compoundShapesDataList.add(shape4);

        Map<String, Object> shape5 = new HashMap<>();
        shape5.put("id", "CS_Fig10_5");
        shape5.put("name", "Compound Shape (Fig 10-5)");
        shape5.put("image", "compound5.png");
        double area5_rect = 4.0 * 3.0; // 12
        double area5_tri = 0.5 * 4.0 * (6.0 - 3.0); // 0.5 * 4 * 2 = 4
        double totalArea5 = area5_rect + area5_tri; // 12
        shape5.put("area", totalArea5);
        StringBuilder sol5 = new StringBuilder();
        sol5.append("Decompose into a rectangle and a right-angled triangle:\n");
        sol5.append("1. Bottom Rectangle: 4 m × 2 m = ").append(df.format(area5_rect)).append(" m²\n");
        sol5.append("2. Top Triangle: base 4 m, height (4-2) m = 2 m. Area = 0.5 × 4 m × 2 m = ")
                .append(df.format(area5_tri)).append(" m²\n");
        sol5.append("Total Area = ").append(df.format(area5_rect)).append(" + ").append(df.format(area5_tri))
                .append(" = ").append(df.format(totalArea5))
                .append(" m² (Ignoring the '16m' as it seems unrelated or a typo for this specific shape).");
        shape5.put("solution_breakdown", sol5.toString());
        compoundShapesDataList.add(shape5);

        // Map<String, Object> shape6 = new HashMap<>();
        // shape6.put("id", "CS_Fig10_6");
        // shape6.put("name", "Compound Shape (Fig 10-6 - Trapezium)");
        // shape6.put("image", "compound6.png");
        // double totalArea6 = 0.5 * (9.0 + 20.0) * 11.0; // 159.5
        // shape6.put("area", totalArea6);
        // StringBuilder sol6 = new StringBuilder();
        // sol6.append("This shape is a trapezium.\n");
        // sol6.append("Formula: Area = 0.5 × (a+b) × h\n");
        // sol6.append("Given: a = 9 m, b = 20 m, h = 11 m\n");
        // sol6.append("Area = 0.5 × (9 + 20) × 11 = 0.5 × 29 × 11 =
        // ").append(df.format(totalArea6)).append(" m²");
        // shape6.put("solution_breakdown", sol6.toString());
        // compoundShapesDataList.add(shape6);

        // Map<String, Object> shape7 = new HashMap<>();
        // shape7.put("id", "CS_Fig10_7");
        // shape7.put("name", "Compound Shape (Fig 10-7)");
        // shape7.put("image", "compound7.png");
        // double area7_rect = 14.0 * 5.0; // 70
        // double area7_tri = 0.5 * 14.0 * (12.0 - 5.0); // 0.5 * 14 * 7 = 49
        // double totalArea7 = area7_rect + area7_tri; // 119
        // shape7.put("area", totalArea7);
        // StringBuilder sol7 = new StringBuilder();
        // sol7.append("Decompose into a rectangle and a triangle:\n");
        // sol7.append("1. Bottom Rectangle: 14 cm × 5 cm =
        // ").append(df.format(area7_rect)).append(" cm²\n");
        // sol7.append("2. Top Triangle: base 14 cm, height (12-5) cm = 7 cm. Area = 0.5
        // × 14 cm × 7 cm = ").append(df.format(area7_tri)).append(" cm²\n");
        // sol7.append("Total Area = ").append(df.format(area7_rect)).append(" +
        // ").append(df.format(area7_tri))
        // .append(" = ").append(df.format(totalArea7)).append(" cm²");
        // shape7.put("solution_breakdown", sol7.toString());
        // compoundShapesDataList.add(shape7);

        Map<String, Object> shape8 = new HashMap<>();
        shape8.put("id", "CS_Fig10_8");
        shape8.put("name", "Compound Shape (Fig 10-8)");
        shape8.put("image", "compound8.png");
        double area8_center_col = 36.0 * (36.0 + 36.0); // 36 * 72 = 2592
        double area8_wing = (60.0 - 36.0) * 36.0; // 24 * 36 = 864
        double totalArea8 = area8_center_col + area8_wing; // 2592 + 864 = 3456
        shape8.put("area", totalArea8);
        StringBuilder sol8 = new StringBuilder();
        sol8.append("Decompose into a central tall rectangle and two side rectangles at the bottom:\n");
        sol8.append("1. Central Rectangle: 36 m × (36+36) m = 36 m × 72 m = ").append(df.format(area8_center_col))
                .append(" m²\n");
        sol8.append("2. Two Side Rectangles: width (60-36) = 24 m each, height 36 m.\n");
        sol8.append("   Area of one side rectangle = 12 m × 36 m = ").append(df.format(area8_wing)).append(" m²\n");
        sol8.append("   Area of both side rectangles = 2 × ").append(df.format(area8_wing)).append(" = ")
                .append(df.format(2 * area8_wing)).append(" m²\n");
        sol8.append("Total Area = ").append(df.format(area8_center_col)).append(" + ").append(df.format(2 * area8_wing))
                .append(" = ").append(df.format(totalArea8)).append(" m²");
        shape8.put("solution_breakdown", sol8.toString());
        compoundShapesDataList.add(shape8);

        Map<String, Object> shape9 = new HashMap<>();
        shape9.put("id", "CS_Fig10_9");
        shape9.put("name", "Compound Shape (Fig 10-9)");
        shape9.put("image", "compound9.png");
        double area9_rect_left = 10.0 * 11.0; // 110
        double area9_rect_right = 8.0 * 8.0; // 64
        double totalArea9 = area9_rect_left + area9_rect_right; // 174
        shape9.put("area", totalArea9);
        StringBuilder sol9 = new StringBuilder();
        sol9.append("Decompose into two rectangles:\n");
        sol9.append("1. Left Rectangle: 10 m × 11 m = ").append(df.format(area9_rect_left)).append(" m²\n");
        sol9.append("2. Right Rectangle: 8 m × 8 m = ").append(df.format(area9_rect_right)).append(" m²\n");
        sol9.append("Total Area = ").append(df.format(area9_rect_left)).append(" + ")
                .append(df.format(area9_rect_right))
                .append(" = ").append(df.format(totalArea9)).append(" m²");
        shape9.put("solution_breakdown", sol9.toString());
        compoundShapesDataList.add(shape9);

        // For each shape:
        // 1. Determine its components (rectangles, triangles, etc.)
        // 2. Extract dimensions from Figure 10.
        // 3. Calculate the area of each component and the total area.
        // 4. Create the "solution_breakdown" string.
        // 5. Add the Map to compoundShapesDataList.
        if (compoundShapesDataList.isEmpty()) { // Add a default if list is empty to avoid nulls later
            Map<String, Object> defaultShape = new HashMap<>();
            defaultShape.put("id", "CS_Default");
            defaultShape.put("name", "Default Compound Shape");
            defaultShape.put("image", "placeholder.png");
            defaultShape.put("area", 0.0);
            defaultShape.put("solution_breakdown", "No compound shape data loaded.");
            compoundShapesDataList.add(defaultShape);
        }
    }

    private void loadSectorAreaData() {
        sectorAreaDataList = new ArrayList<>();
        // EXAMPLE (Sector 1 from Figure 13)
        // Radius = 8 cm, Angle = 90 degrees. Use PI = 3.14
        Map<String, Object> sector1 = new HashMap<>();
        sector1.put("id", "SA_Fig13_1");
        sector1.put("name", "Sector (Fig 13 - 1: 8cm, 90°)");
        sector1.put("image", "sector1.png"); // Ensure this image exists
        double r1 = 8.0;
        double angle1 = 90.0;
        double area1 = (angle1 / 360.0) * 3.14 * r1 * r1; // (90/360)*3.14*8*8 = 0.25 * 3.14 * 64 = 50.24
        sector1.put("area", Double.parseDouble(df.format(area1))); // Store rounded

        StringBuilder sol1 = new StringBuilder();
        sol1.append("Formula: Area = (θ/360) × π × r²\n");
        sol1.append("Given: θ = ").append(angle1).append("°, r = ").append(r1).append(" cm, π ≈ 3.14\n");
        sol1.append("Area = (").append(angle1).append("/360) × 3.14 × ").append(r1).append("²\n");
        sol1.append("Area = ").append(df.format(angle1 / 360.0)).append(" × 3.14 × ").append(df.format(r1 * r1))
                .append("\n");
        sol1.append("Area ≈ ").append(df.format(area1)).append(" cm²");
        sector1.put("solution_formula", sol1.toString());
        sectorAreaDataList.add(sector1);

        // ... ADD ALL 7 OTHER SECTORS FROM FIGURE 13 HERE ...
        // For each sector:
        // 1. Get radius and angle from Figure 13.
        // 2. Calculate area using PI = 3.14 and round to two decimal places.
        // 3. Create the "solution_formula" string.
        // 4. Add the Map to sectorAreaDataList.
        Map<String, Object> sector2 = new HashMap<>();
        sector2.put("id", "SA_Fig13_2");
        sector2.put("name", "Sector (Fig 13 - 2: 18ft, 130°)");
        sector2.put("image", "sector2.png"); // Ensure this image exists
        double r2 = 18.0;
        double angle2 = 130.0;
        double area2 = (angle2 / 360.0) * 3.14 * r2 * r2;
        sector2.put("area", Double.parseDouble(df.format(area2))); // Store rounded

        StringBuilder sol2 = new StringBuilder();
        sol2.append("Formula: Area = (θ/360) × π × r²\n");
        sol2.append("Given: θ = ").append(angle2).append("°, r = ").append(r2).append(" cm, π ≈ 3.14\n");
        sol2.append("Area = (").append(angle2).append("/360) × 3.14 × ").append(r2).append("²\n");
        sol2.append("Area = ").append(df.format(angle2 / 360.0)).append(" × 3.14 × ").append(df.format(r2 * r2))
                .append("\n");
        sol2.append("Area ≈ ").append(df.format(area2)).append(" cm²");
        sector2.put("solution_formula", sol2.toString());
        sectorAreaDataList.add(sector2);

            Map<String, Object> sector3 = new HashMap<>();
        sector3.put("id", "SA_Fig13_3");
        sector3.put("name", "Sector (Fig 13 - 3: 19cm, 120°)");
        sector2.put("image", "sector3.png"); // Ensure this image exists
        double r3 = 19.0;
        double angle3 = 120.0;
        double area3 = (angle3 / 360.0) * 3.14 * r3 * r3;
        sector3.put("area", Double.parseDouble(df.format(area3))); // Store rounded

        StringBuilder sol3 = new StringBuilder();
        sol3.append("Formula: Area = (θ/360) × π × r²\n");
        sol3.append("Given: θ = ").append(angle3).append("°, r = ").append(r3).append(" cm, π ≈ 3.14\n");
        sol3.append("Area = (").append(angle3).append("/360) × 3.14 × ").append(r3).append("²\n");
        sol3.append("Area = ").append(df.format(angle3 / 360.0)).append(" × 3.14 × ").append(df.format(r3 * r3))
                .append("\n");
        sol3.append("Area ≈ ").append(df.format(area3)).append(" cm²");
        sector3.put("solution_formula", sol3.toString());
        sectorAreaDataList.add(sector3);


  Map<String, Object> sector4 = new HashMap<>();
        sector4.put("id", "SA_Fig13_4");
        sector4.put("name", "Sector (Fig 13 - 4: 22ft, 110°)");
        sector4.put("image", "sector4.png"); // Ensure this image exists
        double r4 = 22.0;
        double angle4 = 110.0;
        double area4 = (angle4 / 360.0) * 3.14 * r4 * r4;
        sector4.put("area", Double.parseDouble(df.format(area4))); // Store rounded

        StringBuilder sol4 = new StringBuilder();
        sol4.append("Formula: Area = (θ/360) × π × r²\n");
        sol4.append("Given: θ = ").append(angle4).append("°, r = ").append(r4).append(" cm, π ≈ 3.14\n");
        sol4.append("Area = (").append(angle4).append("/360) × 3.14 × ").append(r4).append("²\n");
        sol4.append("Area = ").append(df.format(angle4 / 360.0)).append(" × 3.14 × ").append(df.format(r4 * r4))
                .append("\n");
        sol4.append("Area ≈ ").append(df.format(area4)).append(" cm²");
        sector4.put("solution_formula", sol4.toString());
        sectorAreaDataList.add(sector4);



        Map<String, Object> sector5 = new HashMap<>();
        sector5.put("id", "SA_Fig13_5");
        sector5.put("name", "Sector (Fig 13 - 5: 3.5m, 100°)");
        sector5.put("image", "sector5.png"); // Ensure this image exists
        double r5 = 3.5;
        double angle5 = 100.0;
        double area5 = (angle5 / 360.0) * 3.14 * r5 * r5;
        sector5.put("area", Double.parseDouble(df.format(area5))); // Store rounded

        StringBuilder sol5 = new StringBuilder();
        sol5.append("Formula: Area = (θ/360) × π × r²\n");
        sol5.append("Given: θ = ").append(angle5).append("°, r = ").append(r5).append(" cm, π ≈ 3.14\n");
        sol5.append("Area = (").append(angle5).append("/360) × 3.14 × ").append(r5).append("²\n");
        sol5.append("Area = ").append(df.format(angle5 / 360.0)).append(" × 3.14 × ").append(df.format(r5 * r5))
                .append("\n");
        sol5.append("Area ≈ ").append(df.format(area5)).append(" cm²");
        sector5.put("solution_formula", sol5.toString());
        sectorAreaDataList.add(sector5);

        Map<String, Object> sector6 = new HashMap<>();
        sector6.put("id", "SA_Fig13_6");
        sector6.put("name", "Sector (Fig 13 - 6: 8in, 270°)");
        sector6.put("image", "sector6.png"); // Ensure this image exists
        double r6 = 8.0;
        double angle6 = 270.0;
        double area6 = (angle6 / 360.0) * 3.14 * r6 * r6;
        sector6.put("area", Double.parseDouble(df.format(area6))); // Store rounded

        StringBuilder sol6 = new StringBuilder();
        sol6.append("Formula: Area = (θ/360) × π × r²\n");
        sol6.append("Given: θ = ").append(angle6).append("°, r = ").append(r6).append(" cm, π ≈ 3.14\n");
        sol6.append("Area = (").append(angle6).append("/360) × 3.14 × ").append(r6).append("²\n");
        sol6.append("Area = ").append(df.format(angle6 / 360.0)).append(" × 3.14 × ").append(df.format(r6 * r6))
                .append("\n");
        sol6.append("Area ≈ ").append(df.format(area6)).append(" cm²");
        sector6.put("solution_formula", sol6.toString());
        sectorAreaDataList.add(sector6);

       Map<String, Object> sector7 = new HashMap<>();
        sector7.put("id", "SA_Fig13_7");
        sector7.put("name", "Sector (Fig 13 - 7: 12yd, 280°)");
        sector7.put("image", "sector7.png"); // Ensure this image exists
        double r7 = 12.0;
        double angle7 = 280.0;
        double area7 = (angle7 / 360.0) * 3.14 * r7 * r7;
        sector7.put("area", Double.parseDouble(df.format(area7))); // Store rounded

        StringBuilder sol7 = new StringBuilder();
        sol7.append("Formula: Area = (θ/360) × π × r²\n");
        sol7.append("Given: θ = ").append(angle7).append("°, r = ").append(r7).append(" cm, π ≈ 3.14\n");
        sol7.append("Area = (").append(angle7).append("/360) × 3.14 × ").append(r7).append("²\n");
        sol7.append("Area = ").append(df.format(angle7 / 360.0)).append(" × 3.14 × ").append(df.format(r7 * r7))
                .append("\n");
        sol7.append("Area ≈ ").append(df.format(area7)).append(" cm²");
        sector7.put("solution_formula", sol7.toString());
        sectorAreaDataList.add(sector7);

       Map<String, Object> sector8 = new HashMap<>();
        sector8.put("id", "SA_Fig13_8");
        sector8.put("name", "Sector (Fig 13 - 8: 15mm, 250°)");
        sector8.put("image", "sector8.png"); // Ensure this image exists
        double r8 = 15.0;
        double angle8 = 250.0;
        double area8 = (angle8 / 360.0) * 3.14 * r8 * r8;
        sector8.put("area", Double.parseDouble(df.format(area8))); // Store rounded

        StringBuilder sol8 = new StringBuilder();
        sol8.append("Formula: Area = (θ/360) × π × r²\n");
        sol8.append("Given: θ = ").append(angle8).append("°, r = ").append(r8).append(" cm, π ≈ 3.14\n");
        sol8.append("Area = (").append(angle8).append("/360) × 3.14 × ").append(r8).append("²\n");
        sol8.append("Area = ").append(df.format(angle8 / 360.0)).append(" × 3.14 × ").append(df.format(r8 * r8))
                .append("\n");
        sol8.append("Area ≈ ").append(df.format(area8)).append(" cm²");
        sector8.put("solution_formula", sol8.toString());
        sectorAreaDataList.add(sector8);


        if (sectorAreaDataList.isEmpty()) { // Add a default
            Map<String, Object> defaultSector = new HashMap<>();
            defaultSector.put("id", "SA_Default");
            defaultSector.put("name", "Default Sector");
            defaultSector.put("image", "placeholder.png");
            defaultSector.put("area", 0.0);
            defaultSector.put("solution_formula", "No sector data loaded.");
            sectorAreaDataList.add(defaultSector);
        }
    }

    // --- Task 1: Shape Identification ---
    public ShapeData getCurrentShape(boolean is3D) {
        List<ShapeData> list = is3D ? shapes3D : shapes2D;
        int index = is3D ? currentShape3DIndex : currentShape2DIndex;
        if (list.isEmpty() || index < 0 || index >= list.size())
            return null;
        return list.get(index);
    }

    public void recordShapeIdentified(String shapeId, boolean is3D) {
        if (is3D) {
            identified3DShapeIdsThisSession.add(shapeId);
            currentShape3DIndex++;
        } else {
            identified2DShapeIdsThisSession.add(shapeId);
            currentShape2DIndex++;
        }
    }

    public boolean allShapesIdentifiedForTask(boolean is3D, int countRequired) {
        Set<String> set = is3D ? identified3DShapeIdsThisSession : identified2DShapeIdsThisSession;
        // Check if 'countRequired' unique shapes have been identified OR if we've run
        // out of shapes to show
        List<ShapeData> list = is3D ? shapes3D : shapes2D;
        int index = is3D ? currentShape3DIndex : currentShape2DIndex;
        return set.size() >= countRequired || index >= list.size();
    }

    // --- Task 2: Angle Identification ---
    /** Gets the next angle type the user needs to identify correctly. */
    public AngleType getTargetAngleTypeToIdentify() {
        for (AngleType type : angleTypesToIdentifyInSession) {
            if (!identifiedAngleTypesThisSession.getOrDefault(type, false)) {
                return type;
            }
        }
        return null; // All targeted types identified
    }

    public void recordAngleTypeIdentified(AngleType type) {
        if (identifiedAngleTypesThisSession.containsKey(type)) {
            identifiedAngleTypesThisSession.put(type, true);
        }
    }

    public boolean allAngleTypesIdentifiedForTask() {
        for (AngleType type : angleTypesToIdentifyInSession) {
            if (!identifiedAngleTypesThisSession.getOrDefault(type, false)) {
                return false;
            }
        }
        return true; // All 4 (or fewer if less than 4 types exist) distinct types identified
    }

    public int generateRandomAngleValue(AngleType typeToAvoid, int min, int max, int multiple) {
        // Generate a random angle (multiple of 10) that IS NOT of typeToAvoid (if
        // specified)
        // This is complex if the panel asks user to input an angle and then identify.
        // If panel *shows* an angle and asks for type, then generation is simpler.
        // For simplicity, let's assume the panel will show a random angle and ask for
        // its type.
        int angle;
        do {
            int range = (max - min) / multiple;
            angle = (random.nextInt(range + 1) * multiple) + min;
        } while (typeToAvoid != null && AngleType.fromAngleValue(angle) == typeToAvoid && min != max); // Avoid
                                                                                                       // generating
                                                                                                       // same type if
                                                                                                       // task requires
                                                                                                       // variety
        return angle;
    }

    // --- Task 3: Area Calculation (Basic Shapes) ---
    public String getCurrentBasicAreaShapeType() {
        if (basicAreaShapeTypes.isEmpty() || currentBasicAreaShapeTypeIndex < 0
                || currentBasicAreaShapeTypeIndex >= basicAreaShapeTypes.size())
            return null;
        return basicAreaShapeTypes.get(currentBasicAreaShapeTypeIndex);
    }

    public void recordBasicAreaShapePracticed(String shapeType) {
        practicedBasicAreaShapesThisSession.add(shapeType);
        currentBasicAreaShapeTypeIndex++; // Move to next in shuffled list
    }

    public boolean allBasicAreaShapesPracticedForTask(int countRequired) {
        // Check if 'countRequired' unique shapes have been practiced OR if we've run
        // out of shapes
        return practicedBasicAreaShapesThisSession.size() >= Math.min(countRequired, basicAreaShapeTypes.size()) ||
                currentBasicAreaShapeTypeIndex >= basicAreaShapeTypes.size();
    }

    public Map<String, Integer> getRandomDimensionsForShape(String shapeType) {
        Map<String, Integer> dims = new HashMap<>();
        int val1 = random.nextInt(19) + 1; // 1 to 19 (spec says 1 to 20, check bounds)
        int val2 = random.nextInt(19) + 1;
        int val3 = random.nextInt(19) + 1;
        // Ensure val1, val2, val3 are appropriate for the shape (e.g., for trapezium,
        // bases can't be negative)
        switch (shapeType.toLowerCase()) {
            case "rectangle":
                dims.put("length", val1);
                dims.put("width", val2);
                break;
            case "parallelogram":
                dims.put("base", val1);
                dims.put("height", val2);
                break;
            case "triangle":
                dims.put("base", val1);
                dims.put("height", val2);
                break;
            case "trapezium":
                dims.put("top_base_a", val1);
                dims.put("bottom_base_b", val2);
                dims.put("height", val3);
                break;
        }
        return dims;
    }

    public double calculateArea(String shapeType, Map<String, Integer> dims) {
        try {
            switch (shapeType.toLowerCase()) {
                case "rectangle":
                    return dims.get("length") * dims.get("width");
                case "parallelogram":
                    return dims.get("base") * dims.get("height");
                case "triangle":
                    return 0.5 * dims.get("base") * dims.get("height");
                case "trapezium":
                    return 0.5 * (dims.get("top_base_a") + dims.get("bottom_base_b")) * dims.get("height");
            }
        } catch (NullPointerException e) {
            System.err.println("Error calculating area: Missing dimension for " + shapeType + " " + e.getMessage());
        }
        return 0;
    }

    // --- Task 4: Circle Calculation ---
    public String getCurrentCircleCalcTaskKey() {
        if (circleCalcTaskKeys.isEmpty() || currentCircleCalcTaskKeyIndex < 0
                || currentCircleCalcTaskKeyIndex >= circleCalcTaskKeys.size())
            return null;
        return circleCalcTaskKeys.get(currentCircleCalcTaskKeyIndex);
    }

    public void recordCircleCalcTaskPracticed(String taskKey) {
        practicedCircleCalcKeysThisSession.add(taskKey);
        currentCircleCalcTaskKeyIndex++;
    }

    public boolean allCircleCalcTasksPracticedForTask() {
        return practicedCircleCalcKeysThisSession.size() >= circleCalcTaskKeys.size() ||
                currentCircleCalcTaskKeyIndex >= circleCalcTaskKeys.size();
    }

    public int getRandomCircleDimension() {
        return random.nextInt(20) + 1;
    } // 1 to 20

    public double calculateCircleProperty(String taskKey, int dimensionValue) {
        // taskKey: "RadiusArea", "DiameterArea", "RadiusCircumference",
        // "DiameterCircumference"
        // dimensionValue: the value of radius or diameter given
        double radius;
        if (taskKey.startsWith("Radius")) {
            radius = dimensionValue;
        } else if (taskKey.startsWith("Diameter")) {
            radius = dimensionValue / 2.0;
        } else {
            return 0; // Invalid task key
        }

        if (taskKey.endsWith("Area")) {
            return Math.PI * radius * radius;
        } else if (taskKey.endsWith("Circumference")) {
            return 2 * Math.PI * radius;
        }
        return 0; // Invalid task key
    }

    // --- Bonus 1: Compound Shapes ---
    public Map<String, Object> getCurrentCompoundShapeData() {
        if (compoundShapesDataList.isEmpty() || currentCompoundShapeIndex < 0
                || currentCompoundShapeIndex >= compoundShapesDataList.size())
            return null;
        return compoundShapesDataList.get(currentCompoundShapeIndex);
    }

    public void recordCompoundShapePracticed(String shapeId) {
        practicedCompoundShapeIdsThisSession.add(shapeId);
        currentCompoundShapeIndex++;
    }

    public boolean allCompoundShapesPracticedForTask(int countRequired) {
        return practicedCompoundShapeIdsThisSession.size() >= Math.min(countRequired, compoundShapesDataList.size()) ||
                currentCompoundShapeIndex >= compoundShapesDataList.size();
    }

    public String[] getCompoundShapeIdentifiers() { // For JComboBox
        if (compoundShapesDataList == null || compoundShapesDataList.isEmpty())
            return new String[0];
        return compoundShapesDataList.stream()
                .map(data -> (String) data.getOrDefault("name", "Unknown Shape"))
                .toArray(String[]::new);
    }

    public Map<String, Object> getCompoundShapeDataByName(String name) { // If JComboBox uses name
        if (compoundShapesDataList == null)
            return null;
        for (Map<String, Object> data : compoundShapesDataList) {
            if (name.equals(data.getOrDefault("name", ""))) {
                return data;
            }
        }
        return null;
    }

    // --- Bonus 2: Sector Area ---
    public Map<String, Object> getCurrentSectorAreaData() {
        if (sectorAreaDataList.isEmpty() || currentSectorAreaIndex < 0
                || currentSectorAreaIndex >= sectorAreaDataList.size())
            return null;
        return sectorAreaDataList.get(currentSectorAreaIndex);
    }

    public void recordSectorAreaPracticed(String sectorId) {
        practicedSectorIdsThisSession.add(sectorId);
        currentSectorAreaIndex++;
    }

    public boolean allSectorsPracticedForTask(int countRequired) {
        return practicedSectorIdsThisSession.size() >= Math.min(countRequired, sectorAreaDataList.size()) ||
                currentSectorAreaIndex >= sectorAreaDataList.size();
    }

    public String[] getSectorAreaIdentifiers() { // For JComboBox
        if (sectorAreaDataList == null || sectorAreaDataList.isEmpty())
            return new String[0];
        return sectorAreaDataList.stream()
                .map(data -> (String) data.getOrDefault("name", "Unknown Sector"))
                .toArray(String[]::new);
    }

    public Map<String, Object> getSectorAreaDataByName(String name) { // If JComboBox uses name
        if (sectorAreaDataList == null)
            return null;
        for (Map<String, Object> data : sectorAreaDataList) {
            if (name.equals(data.getOrDefault("name", ""))) {
                return data;
            }
        }
        return null;
    }
}