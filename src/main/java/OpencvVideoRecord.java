import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_highgui.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;
import static org.bytedeco.javacpp.opencv_imgcodecs.*;
import static org.bytedeco.javacpp.opencv_videoio.*;
import static org.bytedeco.javacpp.opencv_video.*;

public class OpencvVideoRecord {
    public static void main(String[] args) {
        VideoCapture capture = new VideoCapture(0);
        capture.set(CAP_PROP_FPS, 25);
        VideoWriter writer = new VideoWriter("F:/test.mp4", VideoWriter.fourcc((byte) ('X'), (byte) ('V'), (byte) ('I'), (byte) ('D')), CAP_PROP_FPS, new Size(640, 480));

        System.out.println("帧率：" + CAP_PROP_FPS);

        int key = 0;

        while ((key & 0xFF) != 27) {
            Mat frame = new Mat();
            capture.read(frame);
            writer.write(frame);
            namedWindow("camera", WINDOW_NORMAL);
            imshow("camera", frame);
            frame.release();
            key = cvWaitKey(1);
        }

        capture.release();
        writer.release();
        destroyAllWindows();
    }
}
