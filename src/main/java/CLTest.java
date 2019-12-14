import org.apache.commons.cli.*;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_videoio;

import static org.bytedeco.javacpp.opencv_highgui.*;
import static org.bytedeco.javacpp.opencv_highgui.destroyAllWindows;
import static org.bytedeco.javacpp.opencv_videoio.CAP_PROP_FPS;

public class CLTest {
    public static void main(String[] args) throws ParseException {
        String output = null;

        Options options = new Options();

        Option option = new Option("h", "help", false, "display help text");
        options.addOption(option);

        option = new Option("i", "index", true, "device index");
        options.addOption(option);

        option = new Option("o", "output", true, "the filepath for output");
//        option.setRequired(true);//必须设置
        options.addOption(option);

        option = new Option("f", "format", true, "the format of output video");
        option.setRequired(false);
        options.addOption(option);


        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, args);

        if (commandLine.hasOption('h')) {
            new HelpFormatter().printHelp("video capture", options, true);
            return;
        }

        if (commandLine.hasOption('o')) {
            //获取参数
            output = commandLine.getOptionValue('o');
            System.out.println(output);
        }

        opencv_videoio.VideoCapture capture = new opencv_videoio.VideoCapture(0);
        opencv_videoio.VideoWriter writer = new opencv_videoio.VideoWriter(output, opencv_videoio.VideoWriter.fourcc((byte) ('X'), (byte) ('V'), (byte) ('I'), (byte) ('D')), 20, new opencv_core.Size(640, 480));

        int key = 0;

        while ((key & 0xFF) != 27) {
            opencv_core.Mat frame = new opencv_core.Mat();
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
