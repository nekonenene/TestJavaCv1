package hatonekoe

import org.bytedeco.javacpp.avcodec.AV_CODEC_ID_AAC
import org.bytedeco.javacpp.avutil.AV_PIX_FMT_BGR8
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
            val filename = "miku.mp4"
            val targetFile = File(filedir + filename)
            println(targetFile.absolutePath + " を読み込みます（読込可 : ${targetFile.canRead()} ）")

            // GIFアニメ作成
            // convertToGifAnimation(targetFile)

            val movie = FFmpegFrameGrabber.createDefault(targetFile)
            movie.start()
            println(movie.frameRate.toString() + "fps")
            println("width : ${movie.imageWidth}px, height : ${movie.imageHeight}px")

            val recorder = FFmpegFrameRecorder("../output.mp4", movie.imageWidth, movie.imageHeight)
            recorder.frameRate = movie.frameRate
            recorder.videoBitrate = movie.videoBitrate
            // recorder.videoCodec = AV_CODEC_ID_H264
            recorder.audioCodec = AV_CODEC_ID_AAC
            recorder.audioChannels = 2 // ステレオ録音、指定がないと audioChannels = 0 で録音しようとする
            recorder.start()

            val maxFrames = 1000
            for(i in 1..maxFrames) {
                val frame = movie.grab() ?: break // ?: 演算子は https://kotlinlang.org/docs/reference/null-safety.html 参照
                recorder.record(frame)
            }
            recorder.stop()
            recorder.release()
            movie.stop()
            movie.release()
        } catch (e: Exception) {
            println(e)
        }
    }

    /** 動画を GIFアニメ に変換
     *
     * @param originalFile: File 元動画ファイル
     * @param loopTimes: Int     GIFアニメのループ回数。0 だと無限ループ
     */
    fun convertToGifAnimation(originalFile: File, loopTimes: Int = 0) {
        try {
            println(originalFile.name + " を gif アニメにします（読込可 : ${originalFile.canRead()} ）")

            val movie = FFmpegFrameGrabber.createDefault(originalFile)
            movie.start()

            val recorder = FFmpegFrameRecorder("../output.gif", movie.imageWidth, movie.imageHeight)
            recorder.frameRate = movie.frameRate // @TODO: 元動画のフレームレートを落として 15fps くらいにできるといい
            recorder.pixelFormat = AV_PIX_FMT_BGR8
            /* 参考
            http://ffmpeg.org/doxygen/trunk/pixfmt_8h.html#a9a8e335cf3be472042bc9f0cf80cd4c5
            http://stackoverflow.com/questions/37861764/creating-gif-from-qimages-with-ffmpeg
             */
            recorder.setOption("loop", loopTimes.toString())
            recorder.start()

            val maxFrames = 1000
            for(i in 1..maxFrames) {
                val frame = movie.grab() ?: break // ?: 演算子は https://kotlinlang.org/docs/reference/null-safety.html 参照

                frame.samples = null // サウンド部分は不要なので削ぎ落とす
                recorder.record(frame)
            }
            recorder.stop()
            recorder.release()
            movie.stop()
            movie.release()
        } catch (e: Exception) {
            println(e)
        }
    }
}
