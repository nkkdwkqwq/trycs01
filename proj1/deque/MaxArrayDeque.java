package deque;

import java.util.Comparator;
import java.util.Iterator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
    private Comparator<T> pri;
    public MaxArrayDeque(Comparator<T> c) {
        pri = c;
    }

    public T max() {
        T maxitem;
        Iterator<T> item1 = iterator();
        if (item1.hasNext()) {
            maxitem = item1.next();
            while (item1.hasNext()) {
                T mid01 = item1.next();
                if (pri.compare(maxitem, mid01) < 0) {
                    maxitem = mid01;
                }
            }
            return maxitem;
        } else {
            return null;
        }
    }

    public T max(Comparator<T> c) {
        T maxitem;
        Iterator<T> item1 = iterator();
        if (item1.hasNext()) {
            maxitem = item1.next();
            while (item1.hasNext()) {
                T mid01 = item1.next();
                if (c.compare(maxitem, mid01) < 0) {
                    maxitem = mid01;
                }
            }
            return maxitem;
        } else {
            return null;
        }
    }
}
