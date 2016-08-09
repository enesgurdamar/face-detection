package com.enderunlabs.app.utils;

import org.opencv.core.Mat;

/**
 * Created by Lenovo on 8.08.2016.
 */
public interface VideoProcessor {
    Mat process(Mat inputImage);
}
