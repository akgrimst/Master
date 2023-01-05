public class TestParallelMaskedVirtual {
    
    public static void main(String[] args) {

        int threads = 128;
        long timeTaken = 0;
        long startTime;
        long iterationTime;

        for (int i = 0; i < 100; i++){
         SieveParallelMaskedVirtual sm = new SieveParallelMaskedVirtual(100000000, threads);
            sm.getPrimes();
        }

        timeTaken = 0;
        for (int i = 0; i < 10; i++){
            startTime = System.nanoTime();
         SieveParallelMaskedVirtual sm = new SieveParallelMaskedVirtual(1000, threads);
            sm.getPrimes();
            iterationTime = System.nanoTime() - startTime;
            timeTaken += iterationTime;
        }
        System.out.println((timeTaken / 10));

        timeTaken = 0;
        for (int i = 0; i < 10; i++){
            startTime = System.nanoTime();
         SieveParallelMaskedVirtual sm = new SieveParallelMaskedVirtual(10000, threads);
            sm.getPrimes();
            iterationTime = System.nanoTime() - startTime;
            timeTaken += iterationTime;
        }
        System.out.println((timeTaken / 10));

        timeTaken = 0;
        for (int i = 0; i < 10; i++){
            startTime = System.nanoTime();
         SieveParallelMaskedVirtual sm = new SieveParallelMaskedVirtual(100000, threads);
            sm.getPrimes();
            iterationTime = System.nanoTime() - startTime;
            timeTaken += iterationTime;
        }
        System.out.println((timeTaken / 10));

        timeTaken = 0;
        for (int i = 0; i < 10; i++){
            startTime = System.nanoTime();
         SieveParallelMaskedVirtual sm = new SieveParallelMaskedVirtual(1000000, threads);
            sm.getPrimes();
            iterationTime = System.nanoTime() - startTime;
            timeTaken += iterationTime;
        }
        System.out.println((timeTaken / 10));

        timeTaken = 0;
        for (int i = 0; i < 10; i++){
            startTime = System.nanoTime();
         SieveParallelMaskedVirtual sm = new SieveParallelMaskedVirtual(10000000, threads);
            sm.getPrimes();
            iterationTime = System.nanoTime() - startTime;
            timeTaken += iterationTime;
        }
        System.out.println((timeTaken / 10));

        timeTaken = 0;
        for (int i = 0; i < 10; i++){
            startTime = System.nanoTime();
         SieveParallelMaskedVirtual sm = new SieveParallelMaskedVirtual(100000000, threads);
            sm.getPrimes();
            iterationTime = System.nanoTime() - startTime;
            timeTaken += iterationTime;
        }
        System.out.println((timeTaken / 10));

        timeTaken = 0;
        for (int i = 0; i < 10; i++){
            startTime = System.nanoTime();
         SieveParallelMaskedVirtual sm = new SieveParallelMaskedVirtual(1000000000, threads);
            sm.getPrimes();
            iterationTime = System.nanoTime() - startTime;
            timeTaken += iterationTime;
        }
        System.out.println((timeTaken / 10));

    }
}