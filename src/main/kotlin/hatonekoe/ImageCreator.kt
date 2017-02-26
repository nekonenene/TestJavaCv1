package hatonekoe

import org.bytedeco.javacpp.opencv_core.*
import org.bytedeco.javacpp.opencv_imgcodecs.*
import org.bytedeco.javacv.FFmpegFrameGrabber
import org.bytedeco.javacv.FFmpegFrameRecorder
import java.io.File

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
    fun convertToJpg(originalFilePath: String, jpgQuality: Int = 95) {
        try {
            val originalImage: Mat = imread(originalFilePath)
            val outputPath: String = fileHelper.changeExtension(originalFilePath, "jpg")

            imwrite(outputPath, originalImage, intArrayOf(CV_IMWRITE_JPEG_QUALITY, jpgQuality))
        } catch (e: Exception) {
            println(e)
        }
    }

    /** ffmpeg を用いて動画ファイルを読み込みコピー */
    fun copyMovie() {
        try {
            val filedir = "../"
            val filename = "anime.gif"
            val targetFile = File(filedir + filename)
            println(targetFile.absolutePath + " を読み込みます（読込可 : ${targetFile.canRead()} ）")

            val movie = FFmpegFrameGrabber.createDefault(targetFile)
            movie.start()
            println(movie.frameRate.toString() + "fps")
            println("width : ${movie.imageWidth}px, height : ${movie.imageHeight}px")

            val recorder = FFmpegFrameRecorder("../a.mp4", movie.imageWidth, movie.imageHeight)
            recorder.frameRate = movie.frameRate
            recorder.videoBitrate = movie.videoBitrate
            recorder.start()

            do {
                val frame = movie.grab() ?: break // ?: 演算子は https://kotlinlang.org/docs/reference/null-safety.html 参照
                recorder.record(frame)
            } while (true)
            recorder.stop()
            recorder.release()
            movie.stop()
            movie.release()
        } catch (e: Exception) {
            println(e)
        }
    }
}
