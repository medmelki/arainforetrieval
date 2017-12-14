import com.textprocessing.util.HadithIndexUtil;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HadithIndexUtilTest {

    @Test
    public void testGetIndexInSahih() {
        assertEquals(5445, HadithIndexUtil.getIndexInSahih(92));
        assertEquals(5354, HadithIndexUtil.getIndexInSahih(0));
        assertEquals(5363, HadithIndexUtil.getIndexInSahih(10));
        assertEquals(5403, HadithIndexUtil.getIndexInSahih(50));
    }
}
