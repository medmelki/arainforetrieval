import com.textprocessing.util.HadithIndexUtil;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HadithIndexUtilTest {

    @Test
    public void testGetIndexInSahih() {
        assertEquals(5445, HadithIndexUtil.getIndexInSahih(92L));
        assertEquals(5354, HadithIndexUtil.getIndexInSahih(0L));
        assertEquals(5363, HadithIndexUtil.getIndexInSahih(10L));
        assertEquals(5403, HadithIndexUtil.getIndexInSahih(50L));
    }
}
