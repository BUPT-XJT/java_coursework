
package shapeville;

import javax.swing.ImageIcon;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.awt.Image; // For scaling

public class ImageLoader {
    private static Map<String, ImageIcon> imageCache = new HashMap<>();
    private static ImageIcon placeholderIcon = null;

    // Static initializer for placeholder (optional)
    static {
        URL placeholderURL = ImageLoader.class.getResource("/images/placeholder.png");
        if (placeholderURL != null) {
            placeholderIcon = new ImageIcon(placeholderURL);
        } else {
            System.err.println("Warning: Placeholder image 'placeholder.png' not found.");
        }
    }

    public static ImageIcon loadImage(String path) {
        if (path == null || path.trim().isEmpty()) {
            System.err.println("Error: Image path is null or empty.");
            return placeholderIcon;
        }
        if (imageCache.containsKey(path)) {
            return imageCache.get(path);
        }
        // Assumes images are in 'resources/images' which is on the classpath
        // The leading "/" is important for getResource to search from the classpath
        // root.
        URL imgURL = ImageLoader.class.getResource("/images/" + path);
        if (imgURL != null) {
            ImageIcon icon = new ImageIcon(imgURL);
            imageCache.put(path, icon);
            return icon;
        } else {
            System.err.println("Couldn't find file: /images/" + path);
            return placeholderIcon; // Return placeholder if image not found
        }
    }

    public static ImageIcon loadAndScaleImage(String path, int width, int height) {
        ImageIcon originalIcon = loadImage(path);
        if (originalIcon != null && originalIcon != placeholderIcon) {
            Image img = originalIcon.getImage();
            Image scaledImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImg);
        }
        // If original is placeholder or null, scale placeholder if available
        if (placeholderIcon != null) {
            Image img = placeholderIcon.getImage();
            Image scaledImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImg);
        }
        return null; // Or a default blank icon
    }

    public static ImageIcon getPlaceholderIcon() {
        return placeholderIcon;
    }
}