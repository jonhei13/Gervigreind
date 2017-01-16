public class Main {

    public static void main(String[] args) {
        HashingTest hashingTest = new HashingTest();
        hashingTest.generateStates(1000);
        if (hashingTest.hashIsCorrect()) {
            hashingTest.checkForCollisions();
        }
    }
}
