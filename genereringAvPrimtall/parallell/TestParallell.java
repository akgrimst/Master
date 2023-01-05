public class TestParallell {
    
    public static void main(String[] args) {
        long timeTaken = 0;
        long startTime;
        long iterationTime;

        for (int i = 0; i < 100; i++){
           SieveParallel sm = new SieveParallel(100000000, 128);
            sm.getPrimes();
        }

        timeTaken = 0;
        for (int i = 0; i < 10; i++){
            startTime = System.nanoTime();
           SieveParallel sm = new SieveParallel(1000, 128);
            sm.getPrimes();
            iterationTime = System.nanoTime() - startTime;
            timeTaken += iterationTime;
        }
        System.out.println("1000: " + (timeTaken / 10));

        timeTaken = 0;
        for (int i = 0; i < 10; i++){
            startTime = System.nanoTime();
           SieveParallel sm = new SieveParallel(10000, 128);
            sm.getPrimes();
            iterationTime = System.nanoTime() - startTime;
            timeTaken += iterationTime;
        }
        System.out.println("10000: " + (timeTaken / 10));

        timeTaken = 0;
        for (int i = 0; i < 10; i++){
            startTime = System.nanoTime();
           SieveParallel sm = new SieveParallel(100000, 128);
            sm.getPrimes();
            iterationTime = System.nanoTime() - startTime;
            timeTaken += iterationTime;
        }
        System.out.println("100000: " + (timeTaken / 10));

        timeTaken = 0;
        for (int i = 0; i < 10; i++){
            startTime = System.nanoTime();
           SieveParallel sm = new SieveParallel(1000000, 128);
            sm.getPrimes();
            iterationTime = System.nanoTime() - startTime;
            timeTaken += iterationTime;
        }
        System.out.println("1000000: " + (timeTaken / 10));

        timeTaken = 0;
        for (int i = 0; i < 10; i++){
            startTime = System.nanoTime();
           SieveParallel sm = new SieveParallel(10000000, 128);
            sm.getPrimes();
            iterationTime = System.nanoTime() - startTime;
            timeTaken += iterationTime;
        }
        System.out.println("10000000: " + (timeTaken / 10));

        timeTaken = 0;
        for (int i = 0; i < 10; i++){
            startTime = System.nanoTime();
           SieveParallel sm = new SieveParallel(100000000, 128);
            sm.getPrimes();
            iterationTime = System.nanoTime() - startTime;
            timeTaken += iterationTime;
        }
        System.out.println("100000000: " + (timeTaken / 10));

        timeTaken = 0;
        for (int i = 0; i < 10; i++){
            startTime = System.nanoTime();
           SieveParallel sm = new SieveParallel(1000000000, 128);
            sm.getPrimes();
            iterationTime = System.nanoTime() - startTime;
            timeTaken += iterationTime;
        }
        System.out.println("1000000000: " + (timeTaken / 10));

    }
}