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

    /*
    @Override
    public boolean equals(Object o) {

        if (o == this) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (o.getClass() != this.getClass()) {
            return false;
        }
        MaxArrayDeque<T> o1 = (MaxArrayDeque<T>) o;
        if (o1.size() != this.size()) {
            return false;
        }
        int mid = 0;
        while (mid <= size() - 1) {
            if (o1.get(mid) != this.get(mid)) {
                return false;
            }
            mid++;
        }
        return true;

    }
     */
}
