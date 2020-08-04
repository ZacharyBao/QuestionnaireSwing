package Utils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImagePanel extends JPanel {

    private BufferedImage image;

    public ImagePanel() {

    }

    public ImagePanel(String imgPath) {
        try {
            image = ImageIO.read(new File(imgPath));
        } catch (IOException ex) {
            // handle exception...
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, 300, 150, null); // see javadoc for more info on the parameters
    }

}