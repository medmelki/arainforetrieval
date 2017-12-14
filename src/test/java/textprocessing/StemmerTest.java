package textprocessing;

import com.textprocessing.Stemmer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StemmerTest {

    @Test
    public void testStemm() {

        Stemmer stemmer = new Stemmer();

        String root = stemmer.findRoot("الإسهال");
        assertEquals("سهل", root);

        root = stemmer.findRoot("الحاد");
        assertEquals("لحد", root);

        root = stemmer.findRoot("المزمن");
        assertEquals("زمن", root);
    }
}
