package genereringAvPrimtall.sekvensiell;

public class TestSekvensiell {
    
    public static void main(String[] args) {
        long timeTaken = 0;
        long startTime;
        long iterationTime;

        for (int i = 0; i < 100; i++){
            SieveOfEratosthenes sm = new SieveOfEratosthenes(100000000);
            sm.getPrimes();
        }

        timeTaken = 0;
        for (int i = 0; i < 10; i++){
            startTime = System.nanoTime();
            SieveOfEratosthenes sm = new SieveOfEratosthenes(1000);
            sm.getPrimes();
            iterationTime = System.nanoTime() - startTime;
            timeTaken += iterationTime;
        }
        System.out.println("1000: " + (timeTaken / 10));

        timeTaken = 0;
        for (int i = 0; i < 10; i++){
            startTime = System.nanoTime();
            SieveOfEratosthenes sm = new SieveOfEratosthenes(10000);
            sm.getPrimes();
            iterationTime = System.nanoTime() - startTime;
            timeTaken += iterationTime;
        }
        System.out.println("10000: " + (timeTaken / 10));

        timeTaken = 0;
        for (int i = 0; i < 10; i++){
            startTime = System.nanoTime();
            SieveOfEratosthenes sm = new SieveOfEratosthenes(100000);
            sm.getPrimes();
            iterationTime = System.nanoTime() - startTime;
            timeTaken += iterationTime;
        }
        System.out.println("100000: " + (timeTaken / 10));

        timeTaken = 0;
        for (int i = 0; i < 10; i++){
            startTime = System.nanoTime();
            SieveOfEratosthenes sm = new SieveOfEratosthenes(1000000);
            sm.getPrimes();
            iterationTime = System.nanoTime() - startTime;
            timeTaken += iterationTime;
        }
        System.out.println("1000000: " + (timeTaken / 10));

        timeTaken = 0;
        for (int i = 0; i < 10; i++){
            startTime = System.nanoTime();
            SieveOfEratosthenes sm = new SieveOfEratosthenes(10000000);
            sm.getPrimes();
            iterationTime = System.nanoTime() - startTime;
            timeTaken += iterationTime;
        }
        System.out.println("10000000: " + (timeTaken / 10));

        timeTaken = 0;
        for (int i = 0; i < 10; i++){
            startTime = System.nanoTime();
            SieveOfEratosthenes sm = new SieveOfEratosthenes(100000000);
            sm.getPrimes();
            iterationTime = System.nanoTime() - startTime;
            timeTaken += iterationTime;
        }
        System.out.println("100000000: " + (timeTaken / 10));

        timeTaken = 0;
        for (int i = 0; i < 10; i++){
            startTime = System.nanoTime();
            SieveOfEratosthenes sm = new SieveOfEratosthenes(1000000000);
            sm.getPrimes();
            iterationTime = System.nanoTime() - startTime;
            timeTaken += iterationTime;
        }
        System.out.println("1000000000: " + (timeTaken / 10));

        

    }
}
