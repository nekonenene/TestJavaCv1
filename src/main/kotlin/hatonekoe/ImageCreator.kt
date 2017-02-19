package hatonekoe

import org.bytedeco.javacpp.opencv_core.*
import org.bytedeco.javacpp.opencv_imgcodecs.*

class ImageCreator {
    private val fileHelper = FileHelper()

    /** 赤一色の画像を作成 */
    fun createRedImg(height: Int = 100, width: Int = 200) {
        try {
            println("赤色の画像を出力します")

            val redImage = Mat(height, width, CV_8UC3, Scalar(0.0, 0.0, 255.0, 0.0))
            imwrite("../redImg.jpg", redImage)
        } catch (e: Exception) {
            println(e)
        }
    }

    fun copyImg() {
        try {
            val filedir = "../"
            val filename = "test.png"
            println(filename + " のコピーをおこないます")

            val originalImage: Mat = imread(filedir + filename)
            imwrite(filedir + "copied_" + filename, originalImage)

            convertToJpg(filedir + filename, 40)
        } catch (e: Exception) {
            println(e)
        }
    }

    /** 指定圧縮率の jpg 画像を作る
     *
     * @param originalFilePath : 対象画像の path（拡張子まで含む）
     * @param jpgQuality : JPG品質（デフォは95）
     */
    fun convertToJpg (originalFilePath: String, jpgQuality: Int = 95) {
        try {
            val originalImage: Mat = imread(originalFilePath)
            val dotPosition = originalFilePath.lastIndexOf(".")
            val outputPath: String = fileHelper.changeExtension(originalFilePath, "jpg")

            imwrite(outputPath, originalImage, intArrayOf(CV_IMWRITE_JPEG_QUALITY, jpgQuality))
        } catch (e: Exception) {
            println(e)
        }
    }
}
