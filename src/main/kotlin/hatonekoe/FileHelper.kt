package hatonekoe

class FileHelper {
    /**
     * 与えられた path の拡張子を変更
     */
    fun changeExtention(originalFilePath: String, extention: String): String {
        val dotPosition = originalFilePath.lastIndexOf(".")
        val outputPath: String

        if (dotPosition >= 0) {
            outputPath = originalFilePath.substring(0, dotPosition + 1) + extention
        } else {
            outputPath = originalFilePath + "." + extention
        }

        return outputPath
    }
}
