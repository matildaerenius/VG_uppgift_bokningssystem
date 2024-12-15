package gui.factories;

import javax.swing.*;
import java.awt.*;

public class ImageFactory {

    /**
     *
     * @param path file path e.g. "src/resources/{image}.jpg"
     * @param width desired width
     * @param height desired height
     * @return a scaled ImageIcon
     */
    public static ImageIcon createScaledImageIcon(String path, int width, int height) {
        //TODO kanske felhantering om pathen inte finns(?)
        ImageIcon originalIcon = new ImageIcon(path);
        Image scaledImage = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);

        return new ImageIcon(scaledImage);
    }
}
