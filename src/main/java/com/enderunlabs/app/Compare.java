package com.enderunlabs.app;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by engin on 9.8.2016.
 */
public class Compare {
    public boolean compareTwoImages(File fileOne, File  fileTwo) {
        Boolean isTrue = true;
        try{
            Image imgOne = ImageIO.read(fileOne);
            Image imgTwo = ImageIO.read(fileTwo);
            BufferedImage bufImgOne = ImageIO.read(fileOne);
            BufferedImage bufImgTwo = ImageIO.read(fileTwo);
            int imgOneHt = bufImgOne.getHeight();
            int imgTwoHt = bufImgTwo.getHeight();
            int imgOneWt = bufImgOne.getWidth();
            int imgTwoWt = bufImgTwo.getWidth();
            if(imgOneHt!=imgTwoHt ||(imgOneWt!=imgTwoWt)){
                System.out.println(" size are not equal ");
                isTrue = false;
            }
            for(int x =0; x< imgOneHt; x++ ){
                for(int y =0; y <imgOneWt ; y++){
                    if(bufImgOne.getRGB(x, y) != bufImgTwo.getRGB(x, y) ){
                        //System.out.println(" size are not equal ");
                        isTrue = false;
                        break;
                    }
                }
            }
        }catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return isTrue;
    }
}
