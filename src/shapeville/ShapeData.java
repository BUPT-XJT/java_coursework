package shapeville;

/**
 * Represents data for a single shape used in identification tasks.
 */
public class ShapeData {
    private String name; // Correct name of the shape (e.g., "circle", "cube")
    private String imageName; // Filename of the image (e.g., "circle.png")
    private boolean is3D; // True if it's a 3D shape, false for 2D
    private String id; // Optional unique identifier

    public ShapeData(String id, String name, String imageName, boolean is3D) {
        this.id = (id == null || id.trim().isEmpty()) ? name.replaceAll("\\s+", "_").toLowerCase() : id; // Auto-generate
                                                                                                         // ID if null
        this.name = name.toLowerCase().trim(); // Store lowercase for easier comparison
        this.imageName = imageName;
        this.is3D = is3D;
    }

    // Constructor without explicit ID
    public ShapeData(String name, String imageName, boolean is3D) {
        this(null, name, imageName, is3D);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImageName() {
        return imageName;
    }

    public boolean is3D() {
        return is3D;
    }

    @Override
    public String toString() {
        return "ShapeData{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", imageName='" + imageName + '\'' +
                ", is3D=" + is3D +
                '}';
    }
}
