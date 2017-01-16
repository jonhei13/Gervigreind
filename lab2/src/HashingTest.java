import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public class HashingTest {
    private State[] states;

    /**
     * generates some states and stores them in the states array for later
     */
    public void generateStates(int numberOfStates) {
        int sizeOfEnvironment = (int) Math.sqrt(numberOfStates) * 4;
        Random random = new Random(424242469); // to get reproducable results we use a fixed seed
        final Orientation[] orientations = Orientation.values();
        states = new State[numberOfStates];
        for (int i = 0; i < numberOfStates; i++) {
            if (i > 0 && i % 1000 == 0) System.out.println("" + i + " states generated");
            boolean unique;
            do {
                Position p = new Position(random.nextInt(sizeOfEnvironment), random.nextInt(sizeOfEnvironment));
                Orientation o = orientations[random.nextInt(orientations.length)];
                states[i] = new State(p, o, random.nextBoolean());
                unique = true;
                for (int j = 0; j < i && unique; j++) {
                    unique = !statesAreEqual(states[i], states[j]);
                }
            } while (!unique);
        }
    }

    /**
     * check if two states are equal
     * <p>
     * we do this the complicated way here, because we don't want to rely on
     * the (maybe faulty) implementation of State.equals()
     */
    private boolean statesAreEqual(State s1, State s2) {
        return (s1 == null && s2 == null)
                || s1 != null && s2 != null &&
                s1.turned_on == s2.turned_on &&
                s1.orientation == s2.orientation &&
                (s1.position == null && s2.position == null ||
                        s1.position != null && s2.position != null &&
                                s1.position.x == s2.position.x &&
                                s1.position.y == s2.position.y);
    }

    /**
     * check if we are able to retrieve the values we put in to the hash map
     */
    public boolean hashIsCorrect() {

        HashMap<State, Integer> hashMap = new HashMap<State, Integer>();

        // 1. see if the same state as a different object can be used to retrieve a value
        State state1 = new State(new Position(1, 2), Orientation.SOUTH, true);
        State state2 = new State(new Position(1, 2), Orientation.SOUTH, true);
        hashMap.put(state1, 42);
        Integer value = hashMap.get(state2);
        if (value == null || value.intValue() != 42) {
            System.err.println("ERROR: Cannot find the answer to life, the universe and everything!\nEqual state objects are not detected!");
            return false;
        }
        hashMap.clear();

        // 2. put all state-index pairs in the hash map
        for (int i = 0; i < states.length; i++) {
            hashMap.put(states[i], i);
        }
        // for each state check if we retrieve the right value from the hash map
        for (int i = 0; i < states.length; i++) {
            value = hashMap.get(states[i]);
            if (value == null) {
                System.err.println("ERROR: could not retrieve value for " + states[i]);
                return false;
            } else if (value.intValue() != i) {
                if (states[value.intValue()].equals(states[i])) {
                    System.err.println("state " + states[value.intValue()] + " claims it is equal to " + states[i]);
                } else {
                    System.err.println("wrong value (" + value + ") retrieved for " + states[i]);
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * checks how many states have the same hash value and how many will be put in the buckets with other states
     * <p>
     * prints the result on stdout
     */
    public void checkForCollisions() {
        HashSet<Integer> hashSet = new HashSet<Integer>();

        // 1. check how many of the hash values are the same
        for (State s : states) {
            hashSet.add(s.hashCode());
        }
        int collisions = states.length - hashSet.size();
        // the size of the set tells us how many unique values we have
        if (collisions == 0) {
            System.out.println("Excellent, all " + states.length + " states have unique hash codes!");
        } else {
            System.out.println("" + (100.0 * collisions / states.length) + "% of the hash codes of states are duplicates (" + collisions + " / " + states.length + ")!");
        }

        // size of the bucket table is first power of two, that is larger than #states*4/3, because
        // that is how resize of java.util.HashMap works
        int tableSize = 1 << (int) Math.ceil(Math.log(states.length * 4 / 3.0) / Math.log(2));

        hashSet = new HashSet<Integer>();
        // 2. put the bucket indices of all states in a hash set
        for (State s : states) {
            hashSet.add(bucketIndex(s.hashCode(), tableSize));
        }
        collisions = states.length - hashSet.size();
        // the size of the set tells us how many unique values we have
        if (collisions == 0) {
            System.out.println("Excellent, all " + states.length + " states have unique bucket indices!");
        } else {
            System.out.println("" + (100.0 * collisions / states.length) + "% of the bucket indices of states are duplicates (" + collisions + " / " + states.length + ")!");
        }
    }

    /**
     * compute the index of the bucket for a key with the given hash code in a hash table of the given size
     */
    private static int bucketIndex(int hash, int tableSize) {
        // copied from http://grepcode.com/file/repository.grepcode.com/java/root/jdk/openjdk/6-b14/java/util/HashMap.java#HashMap.hash%28int%29
        hash ^= (hash >>> 20) ^ (hash >>> 12);
        hash ^= (hash >>> 7) ^ (hash >>> 4);
        return hash & (tableSize - 1);

    }

}
