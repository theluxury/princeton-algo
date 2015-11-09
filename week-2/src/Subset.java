import edu.princeton.cs.algs4.StdIn;

/**
 * Created by theluxury on 9/17/15.
 */
public class Subset {

    public static void main(String[] args) {
        int K = Integer.valueOf(args[0]);
        int kCount = 0;
        int count = 0;
        RandomizedQueue<String> rndDeque = new RandomizedQueue<String>();

        for (String string : StdIn.readAllStrings()) {
            if (kCount++ == K)
                break;
            rndDeque.enqueue(string);
        }

        for (String string : rndDeque) {
            if (count++ == K)
                break;
            System.out.println(string);
        }
    }

}
