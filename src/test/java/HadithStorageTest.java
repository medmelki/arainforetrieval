
import com.indexing.storage.api.HadithStorage;
import com.indexing.storage.entity.HadithTerm;
import org.junit.Test;

public class HadithStorageTest {

    @Test
    public void persistTest() {
        HadithTerm hadithTerm = new HadithTerm();
        hadithTerm.setCr("maradh");
        hadithTerm.setTerm("taoun");
        HadithStorage.save(hadithTerm);

        HadithStorage.delete(hadithTerm);
    }
}
