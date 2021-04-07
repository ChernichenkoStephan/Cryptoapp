import com.encryption.Encryption.PlainKeyGenerator;
import com.encryption.Encryption.RSAKeyGenerator;
import com.encryption.Model.PlainKey;
import com.encryption.Model.RSAKey;
import com.encryption.Utils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class KeyGeneratorTest {

    private RSAKey rsaKey = RSAKeyGenerator.nextKey();
    private PlainKey pKey = PlainKeyGenerator.nextKey();

    @Test
    public void plainKeyGeneratorTest() {
        boolean[] trues  = new boolean[256];
        Arrays.fill(trues, true);

        boolean[] exists = new boolean[256];
        for (int i = 0; i <= 255; i++) {
            exists[i] = pKey.getPairs().containsKey(i);
        }
        Assertions.assertArrayEquals(exists, trues);
    }

    @Test
    public void RSAKeyGeneratorModTest() {
        Assertions.assertTrue(rsaKey.getMod() > 255);
    }

}
