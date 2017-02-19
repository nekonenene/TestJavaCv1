package hatonekoe

import org.bytedeco.javacpp.opencv_core.*
import org.bytedeco.javacpp.opencv_imgcodecs.imread
import org.bytedeco.javacpp.opencv_imgcodecs.imwrite

class ImageCreator {
    fun createRedImg() {
        try {
            println("赤色の画像を出力します")

            val redImage = Mat(100, 200, CV_8UC3, Scalar(0.0, 0.0, 255.0, 0.0))
            imwrite("../redImg.jpg", redImage)
        } catch (e: Exception) {
            println(e)
        }
    }

    fun copyImg() {
        try {
            val filepath = "../"
            val filename = "test.png"
            println(filename + " のコピーをおこないます")

            val originalImage: Mat = imread(filepath + filename)
            imwrite(filepath + "copied_" + filename, originalImage)
        } catch (e: Exception) {
            println(e)
        }
    }
}

