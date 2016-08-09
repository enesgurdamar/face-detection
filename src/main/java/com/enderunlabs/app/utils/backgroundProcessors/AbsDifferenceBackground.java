package com.enderunlabs.app.utils.backgroundProcessors;

import com.enderunlabs.app.utils.VideoProcessor;
import org.opencv.core.Core;
import org.opencv.core.Mat;

/**
 * Created by Lenovo on 8.08.2016.
 */
public class AbsDifferenceBackground implements VideoProcessor{

    private Mat backgroundImage;

    public AbsDifferenceBackground(Mat backgroundImage){
        this.backgroundImage = backgroundImage;
    }

    public Mat process(Mat inputImage) {
        Mat foregroundImage = new Mat();
        Core.absdiff(backgroundImage, inputImage, foregroundImage);
        return foregroundImage;
    }
}
