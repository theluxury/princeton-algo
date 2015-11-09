import java.util.Iterator;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private int size = 0;
    private Item[] array = (Item[]) new Object[1];

    public RandomizedQueue() {
    }                // construct an empty randomized queue

    public boolean isEmpty() {
        return (size == 0);
    }                 // is the queue empty?
    public int size() {
        return size;
    }                        // return the number of items on the queue
    public void enqueue(Item item) {
        if (item == null) {
            throw new java.lang.NullPointerException("Cannot add null element");
        }

        if (size == array.length) {
            resize(2* array.length);
        }
        array[size++] = item;
    }          // add the item

    public Item dequeue() {
        if (size == 0) {
            throw new java.util.NoSuchElementException("Randomized Queue is empty");
        }

        int rnd = returnRandomInt();
        Item item = array[rnd];
        array[rnd] = array[--size];
        array[size] = null;

        if (size > 0 && size == array.length/4) {
            resize(array.length / 2);
        }

        return item;
    }                    // remove and return a random item

    private void resize(int max) {
        Item[] temp = (Item[]) new Object[max];
        for (int i = 0; i < size; i++) {
            temp[i] = array[i];
        }
        array = temp;
    }
    public Item sample() {
        if (size == 0) {
            throw new java.util.NoSuchElementException("Randomized Queue is empty");
        }
        return array[returnRandomInt()];
    }                     // return (but do not remove) a random item

    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }         // return an independent iterator over items in random order

    private class RandomizedQueueIterator implements Iterator<Item> {
        private int iteratorSize = size;
        private int[] randomizedArray = new int[iteratorSize];

        public RandomizedQueueIterator() {
            for (int i = 0; i < iteratorSize; i++) {
                randomizedArray[i] = i;
            }
            StdRandom.shuffle(randomizedArray);
        }

        public boolean hasNext() {
            return iteratorSize > 0;
        }

        public Item next() {
            if (iteratorSize <= 0) {
                throw new java.util.NoSuchElementException("Can't get next from a null, dummy");
            }
            return array[randomizedArray[--iteratorSize]];
        }

        public void remove() {
            throw new java.lang.UnsupportedOperationException("We don't do remove around these parts");
        }
    }

    private int returnRandomInt() {
        return StdRandom.uniform(size);
    }

    public static void main(String[] args) {

        RandomizedQueue rQue = new RandomizedQueue();
        for (int i = 0; i < 20; i++) {
            rQue.enqueue(i);
        }

        for (Object i : rQue) {
            System.out.println(i);
        }

    }   // unit testing
}