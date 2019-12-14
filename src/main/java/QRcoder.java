
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.opencv_core;


import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Arrays;

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_highgui.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;
import static org.bytedeco.javacpp.opencv_imgcodecs.*;
import static org.bytedeco.javacpp.opencv_videoio.*;
import static org.bytedeco.javacpp.opencv_video.*;
import static org.bytedeco.javacpp.opencv_objdetect.*;

public class QRcoder {
    public static void main(String[] args) throws IOException {
        VideoCapture capture = new VideoCapture(0);

        if (!capture.isOpened())
            return;

        QRCodeDetector qrCodeDetector = new QRCodeDetector();

        int key = 0;

        namedWindow("frame", WINDOW_NORMAL);
        namedWindow("straight_qrcode", WINDOW_NORMAL);
        Mat gray = new Mat();
        Mat points = new Mat();
        Mat straight_qrcode = new Mat();
        byte[] data = null;
        while (key != 27) {

            Mat frame = new Mat();
            capture.read(frame);
            cvtColor(frame, gray, COLOR_BGR2GRAY);

            BytePointer pointer = qrCodeDetector.detectAndDecode(gray, points, straight_qrcode);
            if (pointer != null) {
                System.out.println("检测到二维码！");
                if (straight_qrcode.rows() != 0 && straight_qrcode.cols() != 0) {
                    data = new byte[points.rows() * points.cols() * points.channels() * 4];
                    BytePointer ptr = points.data();
                    ptr.get(data);
                    int[] data_f = bytes2floats(data);

                    System.out.println(Arrays.toString(data));
                    System.out.println(Arrays.toString(data_f));

                    Point p1 = new Point(data_f[1], data_f[0]);
                    Point p2 = new Point(data_f[3], data_f[2]);
                    Point p3 = new Point(data_f[5], data_f[4]);
                    Point p4 = new Point(data_f[7], data_f[6]);

                    line(frame, p1, p2, Scalar.GREEN, 8, LINE_AA, 0);
                    line(frame, p2, p3, Scalar.GREEN, 8, LINE_AA, 0);
                    line(frame, p3, p4, Scalar.GREEN, 8, LINE_AA, 0);
                    line(frame, p4, p1, Scalar.GREEN, 8, LINE_AA, 0);
                    imshow("straight_qrcode", straight_qrcode);
                }
            }

            imshow("frame", frame);
            frame.release();
            key = cvWaitKey(1);
        }

        gray.release();
        points.release();
        capture.release();
        destroyAllWindows();

    }

    public static int[] bytes2floats(byte[] data_b) throws IOException {
        int[] data_f = new int[data_b.length / 4];
        byte[] data = new byte[data_b.length];

        for (int i = 0; i < data.length; i++)
            data[i] = data_b[data.length - 1 - i];

        System.out.println(Arrays.toString(data));
        ByteArrayInputStream bia = new ByteArrayInputStream(data);
        DataInputStream bsi = new DataInputStream(bia);

        for (int i = 0; i < data_f.length; i++) {
            data_f[i] = (int) bsi.readFloat();
        }

        return data_f;
    }
}
