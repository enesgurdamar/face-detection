package com.enderunlabs.app;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.swing.*;

import com.enderunlabs.app.utils.ImageProcessor;
import com.enderunlabs.app.utils.ImageViewer;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.Videoio;
import org.opencv.videoio.VideoCapture;

public class App extends Thread {

    static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }

    private JFrame frame;
    private JLabel imageLabel;

    public static void main( String[] args ) throws InterruptedException, IOException {

        // Open an image file
        String filePath = "src/main/resources/images/building.jpg";
        Mat newImage = Imgcodecs.imread(filePath);
        if(newImage.dataAddr()==0){
            System.out.println("Couldn't open file " + filePath);
        }
        else {
            // Filter
            GUI gui = new GUI("OpenCV GUI", newImage);
            gui.init();
        }


        // Capture video
        App app = new App();
        app.initGUI();
        app.runMainLoop();


    }

    // Set the blue channel to zero (BGR)
    public void filter(Mat image){

        int totalBytes = (int)(image.total() * image.elemSize()); // Number of bytes in the image
        byte[] buffer = new byte[totalBytes];
        image.get(0,0,buffer); // Copy the matrix contents in byte array buffer
        for (int i=0; i<totalBytes; i++){
            if (i%3==0) // Refers to the blue channel
                buffer[i] = 0;
        }
        image.put(0,0,buffer); // Copy the byte array to native storage
    }

    private void initGUI(){

        frame = new JFrame("Camera Input Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400,400);
        imageLabel = new JLabel();
        frame.add(imageLabel);
        frame.setVisible(true);
    }



    // Capture a video from a camera
    private void runMainLoop() {

        ImageProcessor imageViewer = new ImageProcessor();
        Mat webcamMatImage = new Mat();
        Image tempImage;

        //VideoCapture capture = new VideoCapture(0); // Capture from web cam
        VideoCapture capture = new VideoCapture("rtsp://192.168.2.64/Streaming/Channels/101"); // Capture from IP Camera
        //VideoCapture capture = new VideoCapture("src/main/resources/videos/192.168.2.64_20160804140448.avi"); // Capture avi file

        // Setting camera resolution
        capture.set(Videoio.CAP_PROP_FRAME_WIDTH, 800);
        capture.set(Videoio.CAP_PROP_FRAME_HEIGHT, 600);

        if (capture.isOpened()) { // Check whether the camera is instantiated
            while (true) { // Retrieve each captured frame in a loop
                capture.read(webcamMatImage);
                if (!webcamMatImage.empty()) {
                    tempImage = imageViewer.toBufferedImage(webcamMatImage);
                    ImageIcon imageIcon = new ImageIcon(tempImage, "Captured Video");
                    imageLabel.setIcon(imageIcon);
                    frame.pack(); // This will resize the window to fit the image
                } else {
                    System.out.println(" -- Frame not captured or the video has finished! -- Break!");
                    break;
                }
            }
        } else {
            System.out.println("Couldn't open capture.");
        }
    }

}