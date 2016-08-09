package com.enderunlabs.app.utils.backgroundProcessors;

import com.enderunlabs.app.utils.VideoProcessor;
import org.opencv.core.Mat;
import org.opencv.video.BackgroundSubtractorMOG2;

/**
 * Created by Lenovo on 8.08.2016.
 */
public class MixtureOfGaussianBackground implements VideoProcessor {

    private BackgroundSubtractorMOG2 mog = org.opencv.video.Video.createBackgroundSubtractorMOG2();
    private Mat foreground = new Mat();
    private double learningRate = 0.01;

    public Mat process(Mat inputImage) {

        mog.apply(inputImage, foreground, learningRate);

        return foreground;
    }
}
