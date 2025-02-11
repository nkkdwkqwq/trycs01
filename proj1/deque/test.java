package deque;
import org.junit.Test;
import static org.junit.Assert.*;
/** Performs some basic linked list tests. */

public class test {
    @Test
    public void addtest(){
        MaxArrayDeque<Integer> test1 = new MaxArrayDeque<>(Integer::compareTo);
        test1.addFirst(1);
        test1.addFirst(2);
        test1.addFirst(7);
        test1.addFirst(98);
        test1.addFirst(2);
        int mid = test1.max();
        assertEquals(98,mid);

    }
}
