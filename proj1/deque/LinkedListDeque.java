package deque;

import java.util.Iterator;

public class LinkedListDeque<T> implements Deque<T>, Iterable<T> {
    private class Node {
        private T item;
        private Node next;
        private Node first;

        private Node(Node c, T i, Node n) {
            item = i;
            next = n;
            first = c;
        }

    }
    private Node sentinel;
    private int size = 0;


    public LinkedListDeque() {
        sentinel = new Node(sentinel, null, sentinel);
        sentinel.first = sentinel;
        sentinel.next = sentinel;
        size = 0;
    }

    private T getRecursiveHelpFunc(int index, Node t) {
        if (index >= size || index < 0) {
            return null;
        }
        if (index == 0) {
            return t.item;
        }
        return getRecursiveHelpFunc(index = index - 1, t.next);
    }
    public T getRecursive(int index) {
        Node c = sentinel;
        return getRecursiveHelpFunc(index, c.next);
    }

    @Override
    public void addFirst(T item) {
        Node mid = sentinel.next;
        sentinel.next = new Node(sentinel, item, sentinel.next);
        mid.first = sentinel.next;

        size++;
    }

    @Override
    public void addLast(T item) {
        Node mid = sentinel.first;
        sentinel.first = new Node(sentinel.first, item, sentinel);
        mid.next = sentinel.first;

        size++;
    }



    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        Node mid = sentinel.next;
        while (mid.next.item != null) {
            System.out.print(sentinel.item + " ");
            mid = mid.next;
        }
        System.out.println(mid.item);

    }

    @Override
    public T removeFirst() {
        if (sentinel.next.item == null) {
            return null;
        }
        T miditem = sentinel.next.item;
        Node midnode = sentinel.next.next;
        sentinel.next = midnode;
        midnode.first = sentinel;
        size--;
        return miditem;
    }

    @Override
    public T removeLast() {
        if (sentinel.first.item == null) {
            return null;
        }
        T mid = sentinel.first.item;
        Node midnode = sentinel.first.first;
        sentinel.first = midnode;
        midnode.next = sentinel;
        size--;
        return mid;
    }

    @Override
    public T get(int index) {
        if (index >= size || index < 0) {
            return null;
        }
        Node mid = sentinel.next;
        while (index != 0) {
            index--;
            mid = mid.next;
        }
        return mid.item;
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
        if (!(o instanceof Deque)) {
            return false;
        }
        Deque<T> o1 = (Deque<T>) o;
        if (o1.size() != this.size()) {
            return false;
        }
        int mid = 0;
        while (mid <= size - 1) {
            if (!o1.get(mid).equals(this.get(mid))) {
                return false;
            }
            mid++;
        }
        return true;
    }
}
