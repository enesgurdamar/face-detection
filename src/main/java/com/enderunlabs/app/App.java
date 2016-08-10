package com.enderunlabs.app;

import com.enderunlabs.app.utils.ImageProcessor;
import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.opencv.objdetect.Objdetect.CASCADE_SCALE_IMAGE;

public class App extends Thread {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    private JFrame frame;
    private JLabel imageLabel;
    private CascadeClassifier faceDetector;
    private JButton btnRecognize;
    private JPanel buttonPanel;
private CascadeClassifier eyeDetector;
    public static void main(String[] args) throws InterruptedException, IOException {

//        File f = new File("data/engin.png");
//        File f2 = new File("data.png");
//        Compare imgComp = new Compare();
//        System.out.println(imgComp.compareTwoImages(f, f2));
        // Capture video
        App app = new App();
        app.initGUI();
        app.loadCascade();
        app.runMainLoop();

    }

    private void loadCascade() {
        String cascadePath = "src/main/resources/cascades/lbpcascade_frontalface.xml";
        faceDetector = new CascadeClassifier(cascadePath);
        String cascadeEye = "src/main/resources/cascades/haarcascade_eye_tree_eyeglasses.xml";
        eyeDetector =new CascadeClassifier(cascadeEye);
    }

    private void initGUI() {

        frame = new JFrame("Camera Input Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        imageLabel = new JLabel();
        frame.add(imageLabel);
        frame.setVisible(true);
        /*
        buttonPanel = new JPanel();
        btnRecognize = new JButton("Recognize");
        buttonPanel.add(btnRecognize);
        frame.add(buttonPanel,BorderLayout.SOUTH);
        */
    }


    // Capture a video from a camera
    private void runMainLoop() throws InterruptedException, FileNotFoundException {

        ImageProcessor imageViewer = new ImageProcessor();
        Mat webcamMatImage = new Mat();
        Image tempImage;

        VideoCapture capture = new VideoCapture(0); // Capture from web cam
        //VideoCapture capture = new VideoCapture("rtsp://192.168.2.64/Streaming/Channels/101"); // Capture from IP Camera
        //VideoCapture capture = new VideoCapture("src/main/resources/videos/ten.avi"); // Capture avi file

        // Setting camera resolution
        capture.set(Videoio.CAP_PROP_FRAME_WIDTH, 800);
        capture.set(Videoio.CAP_PROP_FRAME_HEIGHT, 600);

        if (capture.isOpened()) { // Check whether the camera is instantiated
            while (true) { // Retrieve each captured frame in a loop
                capture.read(webcamMatImage);

                if (!webcamMatImage.empty()) {
                    detectAndDrawFace(webcamMatImage);
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

    private void detectAndDrawFace(Mat image) {
        MatOfRect faceDetections = new MatOfRect();
        MatOfRect eyeDetections = new MatOfRect();
        faceDetector.detectMultiScale(image, faceDetections, 1.1, 7, 0, new Size(250, 40), new Size());
        eyeDetector.detectMultiScale(image, eyeDetections, 1.3, 2, 0|CASCADE_SCALE_IMAGE, new Size(0, 0), new Size(200, 200) );
        // Draw a bounding box around each face.



        boolean b = false;
        Rect rectCrop = new Rect(10, 10, 10, 10);
        for (Rect rect : faceDetections.toArray()) {
            Imgproc.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0));
            b = true;
            rectCrop = new Rect(rect.x, rect.y, rect.width, rect.height);
        }
        for(Rect eyesArr : eyeDetections.toArray()){


            Imgproc.rectangle(image, new Point(eyesArr.x, eyesArr.y), new Point(eyesArr.x + eyesArr.width, eyesArr.y + eyesArr.height),  new Scalar(255, 0, 0), 4, 8, 0);
        }
        Mat temp = new Mat(image, rectCrop);
        Imgproc.resize(temp, temp, new Size(250, 250));

//        Imgproc.threshold(temp,temp,127,255,Imgcodecs
//        .CV_LOAD_IMAGE_GRAYSCALE);
        Imgproc.cvtColor(temp, temp, Imgproc.COLOR_RGB2GRAY);
        if (b)
            Imgcodecs.imwrite("data.png", temp);


    }

}
