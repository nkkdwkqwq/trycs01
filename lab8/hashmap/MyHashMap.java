package hashmap;

import java.util.*;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author YOUR NAME HERE
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private Collection<Node>[] buckets;
    private int initialSize01 = 16;
    private double loadFactor01 = 0.75;
    private int size = 0;
    // You should probably define some more!

    /** Constructors */
    public MyHashMap() {
        buckets = createTable(initialSize01);
        for(int i = 0; i < initialSize01; i++) {
            buckets[i] = createBucket();
        }
    }

    public MyHashMap(int initialSize) {
        initialSize01 = initialSize;
        buckets = createTable(initialSize01);
        for(int i = 0; i < initialSize01; i++) {
            buckets[i] = createBucket();
        }
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        initialSize01 = initialSize;
        loadFactor01 = maxLoad;
        buckets = createTable(initialSize01);
        for(int i = 0; i < initialSize01; i++) {
            buckets[i] = createBucket();
        }
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key, value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new LinkedList<>();
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     *
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
        return new Collection[tableSize];
    }

    // TODO: Implement the methods of the Map61B Interface below
    // Your code won't compile until you do so!


    @Override
    public boolean containsKey(K key) {
        int insertNum = Math.floorMod(key.hashCode(), initialSize01);
        if (buckets[insertNum] != null) {
            for(Node x : buckets[insertNum]){
                if (x.key.equals(key)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public V get(K key) {
        int insertNum = Math.floorMod(key.hashCode(), initialSize01);
        if (buckets[insertNum] != null) {
            for( Node x : buckets[insertNum] ){
                if (x.key.equals(key)) {
                    return x.value;
                }
            }
        }
        return null;
    }

    @Override
    public V remove(K key) {
        int insertNum = Math.floorMod(key.hashCode(), initialSize01);
        if (buckets[insertNum] != null) {
            int i = 0;
            for (Node x : buckets[insertNum]) {
                if (x.key.equals(key)) {
                    V value = x.value;
                    buckets[insertNum].remove(x);
                    return value;
                }
                i++;
            }
        }
        return null;
    }

    @Override
    public V remove(K key, V value) {
        int insertNum = Math.floorMod(key.hashCode(), initialSize01);
        if (buckets[insertNum] != null) {
            int i = 0;
            for (Node x : buckets[insertNum]) {
                if (x.key.equals(key) && x.value.equals(value)) {
                    buckets[insertNum].remove(x);
                    return value;
                }
                i++;
            }
        }
        return null;
    }

    @Override
    public void clear() {
        buckets = createTable(initialSize01);
        for(int i = 0; i < initialSize01; i++) {
            buckets[i] = createBucket();
        }
        size = 0;
    }

    @Override
    public Set<K> keySet() {
        Set<K> set = new HashSet<>();
        for(int i = 0; i < initialSize01; i++) {
            if(buckets[i] != null) {
                for (Node x : buckets[i]) {
                    int insertNum = Math.floorMod(x.key.hashCode(), size);
                    set.add(x.key);
                }
            }
        }
        return set;
    }

    public void resize(int size) {
        Collection<Node>[] buckets00 = createTable(size);
        for(int i = 0; i < size; i++) {
            buckets00[i] = createBucket();
        }
        for(int i = 0; i < initialSize01; i++) {
            if(buckets[i] != null) {
                for (Node x : buckets[i]) {
                    int insertNum = Math.floorMod(x.key.hashCode(), size);
                    buckets00[insertNum].add(x);
                }
            }
        }
        buckets = buckets00;
        initialSize01 = size;
    }

    @Override
    public void put(K key, V value) {

        if (size / initialSize01 > loadFactor01) {
            resize(Math.round(2 * initialSize01));
        }


        Node node = createNode(key, value);
        int insertNum = Math.floorMod(key.hashCode(), initialSize01);
        if (buckets[insertNum] != null) {
            for (Node x : buckets[insertNum]) {
                if(x.key.equals(key)) {
                    x.value = value;
                    return;
                }
            }
        }
        buckets[insertNum].add(node);
        size++;
    }

    @Override
    public Iterator<K> iterator() {
        Set<K> set = keySet();
        return set.iterator();
    }
}
