package deque;

import java.util.Iterator;

public class ArrayDeque<T> implements Deque<T>, Iterable<T>  {

    private T[] items;
    private int size;
    private int nextFirst = 0;
    private int nextLast;

    public ArrayDeque() {
        items = (T[]) new Object[8];
        size = 0;
        nextLast = items.length - 1;
    }

    private void resize(int capacity) {
        T[] a = (T[]) new Object[capacity];
        if(nextFirst <= nextLast) {
            int firstnum = nextFirst - 1;
            int wholelastlength = items.length;
            while (firstnum > 0) {
                a[firstnum] = items[firstnum];
                firstnum--;
            }
            while (nextLast + 1 > wholelastlength) {
                a[wholelastlength] = items[wholelastlength];
                wholelastlength--;
            }
            items = a;
            nextLast = items.length - size + nextFirst - 1;
        } else {
            int firstnum = nextFirst - 1;
            int lastnum = nextLast + 1;
            while (firstnum >= lastnum) {
                a[firstnum] = items[firstnum];
                firstnum--;
                nextLast = 0;
                nextFirst = size;
            }
        }
    }

    @Override
    public void addFirst(T t) {
        if (size == items.length) {
            resize(2 * size);
        }
        items[nextFirst] = t;
        size++;
        if (nextFirst + 1 > items.length - 1) {
            nextFirst = 0;
        } else {
            nextFirst++;
        }

    }

    @Override
    public void addLast(T t) {
        if (size == items.length) {
            resize(2 * size);
        }
        items[nextLast] = t;
        size++;
        if(nextLast - 1 < 0) {
            nextLast = items.length - 1;
        } else {
            nextLast--;
        }

    }


    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        if (nextFirst <= nextLast) {
            int firstnum = nextFirst - 1;
            int wholelastlength = items.length;
            int midzise = size;
            while (firstnum >= 0) {
                System.out.print(items[firstnum] + " ");
                firstnum--;
            }
            while (nextLast - 1 != wholelastlength) {
                System.out.print(items[wholelastlength] + " ");
                wholelastlength--;
            }
        } else {
            int firstnum = nextFirst - 1;
            int lastnum = nextLast + 1;
            while (firstnum >= lastnum) {
                System.out.print(items[firstnum] + " ");
                firstnum--;
            }
        }
    }

    @Override
    public T removeFirst() {
        if (items.length >= 16 && 4 * size <= items.length) {
            resize(items.length / 2);
        }
        if (size == 0) {
            return null;
        }
        if (nextFirst == 0) {
            nextFirst = items.length - 1;
        } else {
            nextFirst--;
        }
        size--;
        return items[nextFirst];
    }

    @Override
    public T removeLast() {
        if (items.length >= 16 && 4 * size <= items.length) {
            resize(items.length / 2);
        }
        if (size == 0) {
            return null;
        }

        if (nextLast == items.length-1) {
            nextLast = 0;
        } else {
            nextLast++;
        }
        size--;
        return items[nextLast];
    }

    @Override
    public T get(int index) {
        if (index > size - 1 || index < 0) {
            return null;
        }
        if(nextFirst <= nextLast) {
            if (index < nextFirst) {
                return items[nextFirst - index - 1];
            } else {
                return items[nextFirst - index - 1 + items.length];
            }
        } else {
            return items[nextFirst - 1 - index];
        }


    }
    public Iterator<T> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<T> {
        private int nextItem;

        private DequeIterator() {
            nextItem = 0;
        }
        public boolean hasNext() {
            if (nextItem < size) {
                return true;
            }
            return false;
        }


        public T next() {
            T returnItem = get(nextItem);
            nextItem++;
            return returnItem;
        }
    }

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
        ArrayDeque<T> o1 = (ArrayDeque<T>) o;
        if (o1.size() != this.size()) {
            return false;
        }
        int mid = 0;
        while (mid <=  size - 1) {
            if (o1.get(mid) != this.get(mid)) {
                return false;
            }
            mid++;
        }
        return true;

    }



}
