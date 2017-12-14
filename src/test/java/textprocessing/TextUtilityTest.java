package textprocessing;

import com.textprocessing.util.TextUtility;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class TextUtilityTest {

    @Test
    public void testTokenize() {
        List<String> stringList = TextUtility.tokenize("الإسهال الحاد والمزمن");
        assertArrayEquals(new String[]{"الإسهال", "الحاد", "والمزمن"}, stringList.toArray());
        // verify Encoding
        assertNotEquals(Arrays.toString(new String[]{"???????", "?????", "???????"}), Arrays.toString(stringList.toArray()));
    }

    @Test
    public void testRemoveStopWords() {
        String str = TextUtility.removeStopWords("بعض الإسهال الحاد و المزمن");
        assertEquals("الإسهال الحاد المزمن", str);
    }
}
