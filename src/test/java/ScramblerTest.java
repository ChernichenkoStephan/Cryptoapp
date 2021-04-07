import com.encryption.Encryption.*;
import com.encryption.Model.BlowfishKey;
import com.encryption.Model.PlainKey;
import com.encryption.Model.RSAKey;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ScramblerTest {

    @Test
    public void blowfishTest() {
        List<Integer> dataToHide = new ArrayList<>(256);
        for (int i = Byte.MIN_VALUE; i < Byte.MAX_VALUE; i++) dataToHide.add(i);

        BlowfishKey key = BlowfishScrambler.nextKey("testKey".getBytes());
        List<Integer> encData  = BlowfishScrambler.encrypt(dataToHide, key);
        List<Integer> decrData = BlowfishScrambler.decrypt(encData, key);
        Assertions.assertEquals(dataToHide, decrData);
    }

    @Test
    public void lsbTest() {
        List<Integer> dataToHide = new ArrayList<>(256);
        for (int i = 0; i < Byte.MAX_VALUE; i++) dataToHide.add(i);

        Integer[] arr = new Integer[dataToHide.size() * 4];
        Arrays.fill(arr, 0);
        List<Integer> rawData  = Arrays.asList(arr);
        List<Integer> encData  = LSBScrambler.insert(dataToHide, rawData);
        List<Integer> decrData = LSBScrambler.extract(encData);
        Assertions.assertEquals(dataToHide, decrData);
    }

    @Test
    public void rsaTest() {
        List<Integer> dataToHide = new ArrayList<>(256);
        for (int i = Byte.MIN_VALUE; i < Byte.MAX_VALUE; i++) dataToHide.add(i);

        RSAKey key = RSAKeyGenerator.nextKey();
        List<Integer> encData  = RSAScrambler.encrypt(dataToHide, key);
        List<Integer> decrData = RSAScrambler.decrypt(encData, key);
        Assertions.assertEquals(dataToHide, decrData);
    }

    @Test
    public void plainTest() {
        List<Integer> dataToHide = new ArrayList<>(256);
        for (int i = Byte.MIN_VALUE; i < Byte.MAX_VALUE; i++) dataToHide.add(i);

        PlainKey key = PlainKeyGenerator.nextKey();
        List<Integer> encData  = PlainScrambler.encrypt(dataToHide, key);
        List<Integer> decrData = PlainScrambler.decrypt(encData, key);
        Assertions.assertEquals(dataToHide, decrData);
    }

}
