package deque;

public class ArrayDeque<Item> {

    private Item[] items;
    private int size;
    private int nextFirst = 0;
    private int nextLast = items.length - 1;

    public ArrayDeque(){
        items = (Item[]) new Object[8];
        size = 0;
    }

    private void resize(int capacity){
        Item[] a = (Item[]) new Object[capacity];
        int firstnum = nextFirst -1;
        int wholelastlength = items.length;
        while(firstnum > 0){
            a[firstnum] = items[firstnum];
            firstnum--;
        }
        while (nextLast+1 > wholelastlength){
            a[wholelastlength] = items[wholelastlength];
            wholelastlength--;
        }
        items = a;
    }

    public void addFirst(Item t){
        if(size == items.length){
            resize(2*size);
        }
        items[nextFirst] = t;
        size++;
        nextFirst++;
    }

    public void addLast(Item t){
        if(size == items.length){
            resize(2*size);
        }
        items[nextLast] = t;
        size++;
        nextLast--;
    }

    public boolean isEmpty(){
        return size == 0;
    }

    public int size(){
        return size;
    }

    public void printDeque(){
        int firstnum = nextFirst -1;
        int wholelastlength = items.length;
        while(firstnum>=0){
            System.out.print(items[firstnum]+" ");
            firstnum--;
        }
        while(nextLast - 1 != wholelastlength){
            System.out.print(items[wholelastlength]+" ");
            wholelastlength--;
        }
    }

    public Item removeFirst(){
        if(items.length>=16 && 4*size<=items.length ){
            resize(items.length/2);
        }
        if(nextFirst == 0){
            return null;
        }

        nextFirst--;
        size--;
        return items[nextFirst];
    }

    public Item removeLast(){
        if(items.length>=16 && 4*size<=items.length ){
            resize(items.length/2);
        }
        if(nextLast == items.length){
            return null;
        }

        nextLast++;
        size--;
        return items[nextLast];
    }

    public Item get (int index){
        if(index >= nextFirst -1 + items.length - nextLast|| index < 0){
            return null;
        }

        if(index < nextFirst){
            return items[nextFirst - index -1];
        }else{
            return items[nextFirst- index -1 + items.length];
        }


    }

    public boolean equals(Object o){
        if(o instanceof ArrayDeque){
            if(((ArrayDeque<?>) o).size() == this.size()){
                int mid = 0;
                while(mid <=  size-1){
                    if(((ArrayDeque<?>) o).get(mid)!=this.get(mid)){
                        return false;
                    }
                    mid++;
                }
                return true;
            }
        }
        return false;
    }

}
