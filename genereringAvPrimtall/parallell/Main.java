public class Main {
    public static void main(String[] args) {
        for (int i = 0; i < 100; i++){
            SieveParallelVirtual spv = new SieveParallelVirtual(21474836, 64);
            spv.sieve();
        }

        long timeTaken = 0;
        for (int i = 0; i < 10; i++){
            long startTime = System.nanoTime();
            SieveParallelVirtual spv = new SieveParallelVirtual(21474836, 64);
            spv.sieve();
            long endTime = (System.nanoTime() - startTime);
            System.out.println(endTime);
            timeTaken = timeTaken + endTime;
        }
        System.out.println(timeTaken);
        System.out.println("Gjennomsnitt virtuelle-tråder 8 tråder: " + timeTaken/10);
        System.out.println("\n");

        for (int i = 0; i < 100; i++){
            SieveParallelVirtual spv = new SieveParallelVirtual(21474836, 4);
            spv.sieve();
        }

        timeTaken = 0;
        for (int i = 0; i < 10; i++){
            long startTime = System.nanoTime();
            SieveParallelVirtual spv = new SieveParallelVirtual(21474836, 4);
            spv.sieve();
            long endTime = (System.nanoTime() - startTime);
            System.out.println(endTime);
            timeTaken = timeTaken + endTime;
        }
        System.out.println(timeTaken);
        System.out.println("Gjennomsnitt virtuelle-tråder 16 tråder: " + timeTaken/10);
        System.out.println("\n");

        for (int i = 0; i < 100; i++){
            SieveParallel sp = new SieveParallel(21474836, 4);
            sp.sieve();
        }
        timeTaken = 0;
        for (int i = 0; i < 10; i++){
            long startTime = System.nanoTime();
            SieveParallel sp = new SieveParallel(21474836, 4);
            sp.sieve();
            long endTime = (System.nanoTime() - startTime);
            System.out.println(endTime);
            timeTaken = timeTaken + endTime;
        }
        System.out.println(timeTaken);
        System.out.println("Gjennomsnitt system-tråder: " + timeTaken/10);
        System.out.println("\n");
    }

    
}
