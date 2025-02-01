package org.firstinspires.ftc.teamcode.mechanics;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;
import java.util.List;

public class V2blue extends OpenCvPipeline {

    // Declare the scalars and mats we will be using
    private Scalar low;
    private Scalar high;

    private Mat mask;
    private Mat frame;
    private Mat output;
    private Point centroid;


    public V2blue() {
        low = new Scalar(88, 150, 0); // Lower bound for blue in HSV
        high = new Scalar(127, 255, 255); // Upper bound for blue in HSV
    }

    public V2blue(Scalar lowerBound, Scalar upperBound) {
        low = lowerBound;
        high = upperBound;
    }

    @Override
    public Mat processFrame(Mat input) {

        frame = new Mat();
        output = new Mat();
        mask = new Mat(output.rows(), output.cols(), CvType.CV_8UC4);

        // Converts the colorspace from RGB to HSV
        Imgproc.cvtColor(input, output, Imgproc.COLOR_RGB2HSV);
        // Creates a mask of all pixels within the specified color bounds yipee
        Core.inRange(output, low, high, mask);
        // Puts the mask over the original image
        Core.bitwise_and(input, input, frame, mask);

        // Blurs the image to smooth it out and remove unwanted pixels
        Imgproc.GaussianBlur(mask, mask, new Size(11, 15), 0.0);

        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(mask, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

        // Draws contours onto the frame from the list created above.
        if (hierarchy.size().height > 0 && hierarchy.size().width > 0) {
            MatOfPoint biggest = new MatOfPoint();
            for (int index = 0; index >= 0; index = (int) hierarchy.get(0, index)[0]) {
                Imgproc.drawContours(frame, contours, index, new Scalar(88, 0, 0), 2);
                if (index == 0)
                    biggest = contours.get(index);
                else if (contours.get(index).size().area() > contours.get(index - 1).size().area())
                    biggest = contours.get(index);
            }

            // Creates a point and sets it to the approximate center of the largest contour
            Moments moments = Imgproc.moments(biggest);
            centroid = new Point();

            centroid.x = moments.get_m10() / moments.get_m00();
            centroid.y = moments.get_m01() / moments.get_m00();

            Rect rect = new Rect((int) centroid.x, (int) centroid.y, 10, 10);
            Imgproc.rectangle(frame, rect, new Scalar(0, 255, 255));

        }

        mask.release();

        return frame;
    }

    // Returns the center of the largest contour
    public Point getCentroid() {
        return centroid;
    }

    public void setLowerBound(Scalar low) {
        this.low = low;
    }

    public void setUpperBound(Scalar high) {
        this.high = high;
    }

    public void setLowerAndUpperBounds(Scalar low, Scalar high) {
        this.low = low;
        this.high = high;
    }



    //probably put the next part in the auto opmode yippeee


}