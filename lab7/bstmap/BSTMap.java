package bstmap;
import edu.princeton.cs.algs4.Queue;
import java.util.*;

public class BSTMap<K extends Comparable<K>, V extends Comparable<V>> implements Map61B<K, V>  {
    private Node root;

    private class Node {
        private K key;
        private V value;
        private Node left, right;
        private int size;

        public Node (K key, V value, int size){
            this.key = key;
            this.value = value;
            this.size = size;
        }

    }

    public BSTMap(){
    }

    @Override
   /** Removes all of the mappings from this map. */
    public void clear() {
        root = null;
    }

    @Override
    /* Returns true if this map contains a mapping for the specified key. */
    public boolean containsKey(K key){
        if(key == null) throw new IllegalArgumentException("calls get() with a null key");
        return containsKey(root, key) != null;
    }

    private K containsKey(Node x, K key) {
        if(key == null) throw new IllegalArgumentException("calls get() with a null key");
        if (x == null) return null;
        int cmp = key.compareTo(x.key);
        if (cmp > 0)        return containsKey(x.right, key);
        else if (cmp < 0)   return containsKey(x.left, key);
        else                return x.key;
    }

    @Override
    /* Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    public V get(K key){
        return get(root, key);
    }

    private V get(Node x, K key) {
        if(key == null) throw new IllegalArgumentException("calls get() with a null key");
        if (x == null) return null;
        int cmp = key.compareTo(x.key);
        if (cmp > 0)        return get(x.right, key);
        else if (cmp < 0)   return get(x.left, key);
        else                return x.value;
        }

    @Override
    /* Returns the number of key-value mappings in this map. */
    public int size(){
        return size(root);
    }

    private int size(Node x){
        if (x == null) {
            return 0;
        }
        return x.size;
    }

    @Override
    /* Associates the specified value with the specified key in this map. */
    public void put(K key, V val){
       if (key == null) throw new IllegalArgumentException("calls put() with a null key");
       root = put(root, key, val);
    }

    private Node put(Node x, K key, V val) {
        if (x == null) {
            return new Node(key, val, 1);
        }
        int cmp = key.compareTo(x.key);
        if (cmp < 0) {
            x.left = put(x.left, key, val);
        } else if (cmp > 0) {
            x.right = put(x.right, key, val);
        } else {
            x.value = val;
        }
        x.size = 1 + size(x.left) + size(x.right);
        return x;
    }

    @Override
    /* Returns a Set view of the keys contained in this map. Not required for Lab 7.
     * If you don't implement this, throw an UnsupportedOperationException. */
    public Set<K> keySet() {
        Set<K> queue = new LinkedHashSet<>();
        keyset(root, queue, min(), max());
        return queue;
    }

    private void keyset(Node x, Set<K> queue ,K lo, K hi) {
        if (x == null) return;
        int cmplo = lo.compareTo(x.key);
        int cmphi = hi.compareTo(x.key);
        if (cmplo < 0) keyset(x.left, queue, lo, hi);
        if (cmplo <= 0 && cmphi >= 0) queue.add(x.key);
        if (cmphi > 0 ) keyset(x.right, queue, lo, hi);
    }

    private boolean isEmpty() {
        return size() == 0;
    }

    public K min() {
        if (isEmpty()) throw new NoSuchElementException("call min() with empty symbol table");
        return min(root).key;
    }

    private Node min(Node x) {
        if(x.left == null) {
            return x;
        }
        return min(x.left);
    }

    public K max() {
        if (isEmpty()) throw new NoSuchElementException("call max() with empty symbol table");
        return max(root).key;
    }

    private Node max(Node x) {
        if (x.right == null) {
            return x;
        }
        return max(x.right);
    }

    @Override
    /* Removes the mapping for the specified key from this map if present.
     * Not required for Lab 7. If you don't implement this, throw an
     * UnsupportedOperationException. */
    public V remove(K key) {
        root = remove(root, key);
        return removeValue;
    }

    private V removeValue;
    private Node minNode(Node x) {
        if(x.left == null) {
            return x;
        }
        return minNode(x.left);
    }

    private Node removeMin(Node x) {
        if (x == null) throw new NoSuchElementException("symbol table underflow");
        if (x.left == null) return x.right;
        x.left =removeMin(x.left);
        x.size = size(x.left) + size(x.right);
        return x;
    }

    private Node remove(Node x, K key) {
        if (x == null) return null;
        int cmp = key.compareTo(x.key);
        if      (cmp < 0) x.left  = remove(x.left,  key);
        else if (cmp > 0) x.right = remove(x.right, key);
        else {
            removeValue = x.value;
            if (x.right == null) return x.left;
            if (x.left  == null) return x.right;
            Node t = x;
            x = minNode(t.right);
            x.right = removeMin(t.right);
            x.left = t.left;
        }
        x.size = size(x.left) + size(x.right) + 1;
        return x;
    }

    @Override
    /* Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Not required for Lab 7. If you don't implement this,
     * throw an UnsupportedOperationException.*/
    public V remove(K key, V value) {
        root = remove(root, key, value);
        if(removeValue != value) {
            return null;
        }
        return removeValue;
    }

    private Node remove(Node x, K key, V val) {
        if (x == null) return null;
        int cmp = key.compareTo(x.key);
        if      (cmp < 0) x.left  = remove(x.left,  key, val);
        else if (cmp > 0) x.right = remove(x.right, key, val);
        else {
            removeValue = x.value;
            if(removeValue != val) {
                return x;
            }
            if (x.right == null) return x.left;
            if (x.left  == null) return x.right;
            Node t = x;
            x = minNode(t.right);
            x.right = removeMin(t.right);
            x.left = t.left;
        }
        x.size = size(x.left) + size(x.right) + 1;
        return x;
    }

    @Override
    public Iterator<K> iterator(){
        return keySet().iterator();
    }

}

