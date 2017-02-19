package hatonekoe

import org.junit.Test
import kotlin.test.assertEquals

class FileHelperTest {
    val fileHelper = FileHelper()

    @Test
    fun changeExtentionTest() {
        assertEquals("test.jpg", fileHelper.changeExtention("test.png", "jpg"))
        assertEquals("../../test.test.png", fileHelper.changeExtention("../../test.test.jpg", "png"))
    }
}
