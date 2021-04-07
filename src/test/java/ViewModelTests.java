import com.encryption.ViewModel.BlowfishViewModel;
import com.encryption.ViewModel.LSBViewModel;
import com.encryption.ViewModel.PlainViewModel;
import com.encryption.ViewModel.RSAViewModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * This is not best way to test vm's, because it require test files in specific folder,
 * also these tests does not cover all possible variates.
 * But in my circumstances it is  the best way to do it / it fulfills all my needs.
 */
public class ViewModelTests {
    @Test
    public void PlainViewModelTest() {
        final List<Integer> coded   = PlainViewModel.testCode();
        final List<Integer> decoded = PlainViewModel.testDecode();
        Assertions.assertEquals(coded, decoded);
    }

    @Test
    public void RSAViewModelTest() {
        final List<Integer> coded   = RSAViewModel.testCode();
        final List<Integer> decoded = RSAViewModel.testDecode();
        Assertions.assertEquals(coded, decoded);
    }

    @Test
    public void LSBViewModelTest() {
        final List<Integer> in = LSBViewModel.testInsert();
        final List<Integer> ex = LSBViewModel.testExtract();
        Assertions.assertEquals(in, ex);
    }

    @Test
    public void BlowfishViewModelTest() {
        final List<Integer> coded   = BlowfishViewModel.testCode();
        final List<Integer> decoded = BlowfishViewModel.testDecode();
        Assertions.assertEquals(coded, decoded);
    }
}
