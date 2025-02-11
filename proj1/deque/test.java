package deque;
import org.junit.Test;
import static org.junit.Assert.*;
/** Performs some basic linked list tests. */

public class test {
    @Test
    public void addtest(){
        ArrayDeque<Integer> test1 = new ArrayDeque<>();
        test1.addFirst(1);
        test1.addFirst(2);
        test1.addFirst(3);
        assertEquals(3,test1.size());
    }
}
