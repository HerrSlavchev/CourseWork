/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package images;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author root
 */
public class ImageViewFactory {

    public static ImageView getImageView(String imageName) {
        Image image = new Image(ImageViewFactory.class.getResourceAsStream(imageName));
        return new ImageView(image);
    }

    public static ImageView getImageView(String imageName, double h) {
        
        ImageView iv = getImageView(imageName);
        iv.setFitHeight(h);
        iv.setPreserveRatio(true);
        iv.setSmooth(true);
        iv.setCache(true);
        
        return iv;
    }
}
