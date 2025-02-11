package deque;

import java.util.Comparator;
import java.util.Iterator;

public class MaxArrayDeque<Item> extends ArrayDeque<Item> {
    private Comparator<Item> pri ;
    public MaxArrayDeque(Comparator<Item> c) {
        pri = c;
    }

    public Item max() {
        Item maxitem;
        Iterator<Item> item1 = iterator();
        if(item1.hasNext()) {
            maxitem = item1.next();
            while(item1.hasNext()) {
                Item mid01 = item1.next();
                if(pri.compare(maxitem,mid01)<0) {
                    maxitem = mid01;
                }
            }
            return maxitem;
        } else {
            return null;
        }
    }

    public Item max(Comparator<Item> c) {
        Item maxitem;
        Iterator<Item> item1 = iterator();
        if(item1.hasNext()) {
            maxitem = item1.next();
            while(item1.hasNext()) {
                Item mid01 = item1.next();
                if (c.compare(maxitem,mid01)<0) {
                    maxitem = mid01;
                }
            }
            return maxitem;
        } else {
            return null;
        }
    }

    @Override
    public boolean equals(Object o) {

        if(o == this) {
            return true;
        }
        if(o == null) {
            return false;
        }
        if(o.getClass() != this.getClass()) {
            return false;
        }
        MaxArrayDeque<Item> o1 = (MaxArrayDeque<Item>) o;
        if (o1.size() != this.size()) {
            return false;
        }
        int mid = 0;
        while(mid <= size()-1) {
            if(o1.get(mid) != this.get(mid)) {
                return false;
            }
            mid++;
        }
        return true;

    }
}
