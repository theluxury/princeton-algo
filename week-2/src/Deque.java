import java.util.Iterator;

/**
 * Created by theluxury on 9/17/15.
 */
public class Deque<Item> implements Iterable<Item> {

    private class Node {
        private Node prev;
        private Node next;
        private Item value;
    }

    private Node first;
    private Node last;
    private int size = 0;

    public Deque()        {
    }                    // construct an empty deque

    public boolean isEmpty()             {
        return (size == 0);
    }     // is the deque empty?

    public int size() {
        return size;
    }                  // return the number of items on the deque

    public void addFirst(Item item) {
        if (item == null) {
            throw new java.lang.NullPointerException("Cannot insert a null element");
        }
        Node newNode = new Node();
        newNode.value = item;
        newNode.next = first;
        if (first != null) {
            first.prev = newNode;
        }
        if (first == null) {
            last = newNode; }
        first = newNode;
        size++;
    }          // add the item to the front

    public void addLast(Item item)           {
        if (item == null) {
            throw new java.lang.NullPointerException("Cannot insert a null element");
        }
        Node newNode = new Node();
        newNode.value = item;
        newNode.prev = last;
        if (last != null) { last.next = newNode; }
        if (last == null) { first = newNode; }
        last = newNode;
        size++;
    } // add the item to the end

    public Item removeFirst() {

        if (size == 0) {
            throw new java.util.NoSuchElementException("Cannot remove from an empty deque.");
        }
        size--;
        Item item = first.value;
        first = first.next;

        if (size == 0) {
            first = null;
            last = null;
        } else {
            first.prev = null;
        }
        return item;
    } // remove and return the item from the front

    public Item removeLast() {
        if (size == 0) {
            throw new java.util.NoSuchElementException("Cannot remove from an empty deque.");
        }
        size--;
        Item item = last.value;
        last = last.prev;

        if (size == 0) {
            first = null;
            last = null;
        } else {
            last.next = null;
        }

        return item;
    } // remove and return the item from the end

    public Iterator<Item> iterator()         {
        return new DequeIterator();
    } // return an iterator over items in order from front to end

    private class DequeIterator implements Iterator<Item> {
        private Node current = first;
        public boolean hasNext() {
            return current != null;
        }
        public void remove() {
            throw new java.lang.UnsupportedOperationException("We don't do remove around these parts");
        }

        public Item next() {
            if (current == null) {
                throw new java.util.NoSuchElementException("Can't get next from a null, dummy");
            }
            Item item = current.value;
            current = current.next;
            return item;
        }
    }
    public static void main(String[] args)   {
    } // unit testing

}
