package q1;
import org.junit.Assert;
import org.junit.Test;

public class SimpleTest {

    private static final int OPERATIONS = 120000;

    @Test
    public void testAnderson() {
        int result = q1.a.PIncrement.parallelIncrement(0, 4);
        System.out.println(result);
        Assert.assertEquals(result, OPERATIONS);
    }

    @Test
    public void testCLH() {
        int result = q1.b.PIncrement.parallelIncrement(0, 4);
        System.out.println(result);
        Assert.assertEquals(result, OPERATIONS);
    }

    @Test
    public void testMCS() {
        int result = q1.c.PIncrement.parallelIncrement(0, 8);
        System.out.println(result);
        Assert.assertEquals(result, OPERATIONS);
    }

}
