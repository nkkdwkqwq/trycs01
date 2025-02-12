package deque;

import java.util.Iterator;

public class ArrayDeque<Item> implements Deque<Item> {

    private Item[] items;
    private int size;
    private int nextFirst = 0;
    private int nextLast;

    public ArrayDeque() {
        items = (Item[]) new Object[8];
        size = 0;
        nextLast = items.length - 1;
    }

    private void resize(int capacity) {
        Item[] a = (Item[]) new Object[capacity];
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
    }

    @Override
    public void addFirst(Item t) {
        if (size == items.length) {
            resize(2 * size);
        }
        items[nextFirst] = t;
        size++;
        nextFirst++;
    }

    @Override
    public void addLast(Item t) {
        if (size == items.length) {
            resize(2 * size);
        }
        items[nextLast] = t;
        size++;
        nextLast--;
    }


    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        int firstnum = nextFirst - 1;
        int wholelastlength = items.length;
        while (firstnum >= 0) {
            System.out.print(items[firstnum] + " ");
            firstnum--;
        }
        while (nextLast - 1 != wholelastlength) {
            System.out.print(items[wholelastlength] + " ");
            wholelastlength--;
        }
    }

    @Override
    public Item removeFirst() {
        if (items.length >= 16 && 4 * size <= items.length) {
            resize(items.length / 2);
        }
        if (nextFirst == 0) {
            return null;
        }

        nextFirst--;
        size--;
        return items[nextFirst];
    }

    @Override
    public Item removeLast() {
        if (items.length >= 16 && 4 * size <= items.length) {
            resize(items.length / 2);
        }
        if (nextLast >= items.length - 1) {
            return null;
        }

        nextLast++;
        size--;
        return items[nextLast];
    }

    @Override
    public Item get(int index) {
        if (index >= nextFirst - 1 + items.length - nextLast || index < 0) {
            return null;
        }

        if (index < nextFirst) {
            return items[nextFirst - index - 1];
        } else {
            return items[nextFirst - index - 1 + items.length];
        }


    }
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {
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


        public Item next() {
            Item returnItem = get(nextItem);
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
        ArrayDeque<Item> o1 = (ArrayDeque<Item>) o;
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
