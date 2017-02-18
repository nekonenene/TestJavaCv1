package hatonekoe

import org.bytedeco.javacpp.opencv_core.*
import org.bytedeco.javacpp.opencv_imgcodecs.imwrite

class CreateImage {
    fun createRedImg() {
        try {
            println("赤色の画像を出力します")

            val redImage: Mat = Mat(100, 200, CV_8UC3, Scalar(0.0, 0.0, 255.0, 0.0))
            imwrite("../redImg.jpg", redImage)
        } catch (e: Exception) {
            println(e)
        }
    }
}

fun main(args: Array<String>) {
    CreateImage.cre
}
