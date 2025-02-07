package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {
  // YOUR TESTS HERE
    @Test
    public void testThreeAddThreeRemove(){
        AListNoResizing<Integer> a = new AListNoResizing<>();
        BuggyAList<Integer> b = new BuggyAList<>();
        a.addLast(4);
        a.addLast(5);
        a.addLast(6);
        b.addLast(4);
        b.addLast(5);
        b.addLast(6);

        assertEquals(a.removeLast(),b.removeLast());
        assertEquals(a.removeLast(),b.removeLast());
        assertEquals(a.removeLast(),b.removeLast());

    }

    @Test
    public void randomizedTest(){
        AListNoResizing<Integer> L = new AListNoResizing<>();
        BuggyAList<Integer> B = new BuggyAList<>();
        int N = 5000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 4);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                B.addLast(randVal);
            } else if (operationNumber == 1) {
                // size
                int size01 = L.size();
                int size02 = B.size();
                assertEquals(size01 ,size02);
            } else if (L.size() != 0){
                if (operationNumber == 2){
                    int LastNum01 = L.getLast();
                    int LastNum02 = B.getLast();
                    assertEquals(LastNum01,LastNum02);
                }else if(operationNumber == 3){
                    int LastNum01 = L.removeLast();
                    int LastNum02 = B.removeLast();
                    assertEquals(LastNum01,LastNum02);
                }
            }
        }
    }
}
