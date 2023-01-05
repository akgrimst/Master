public class SieveMain {
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int[] maskedPrimes = null;
        int[] sievePrimes = null;
        int[] sevenPrimes = null;
        for (int i = 0; i < 100; i++){
            SieveMasked sm = new SieveMasked(n);
            sm.getPrimes();
        }

        long timeTaken = 0;
        for (int i = 0; i < 10; i++){
            long startTime = System.nanoTime();
            SieveMasked sm = new SieveMasked(n);
            sm.getPrimes();
            long endTime = (System.nanoTime() - startTime);
            System.out.println(endTime);
            timeTaken = timeTaken + endTime;
        }
        SieveMasked sm = new SieveMasked(n);
        maskedPrimes = sm.getPrimes();

        System.out.println(timeTaken);
        System.out.println("Gjennomsnitt masked: " + timeTaken/10);
        System.out.println("\n");

        for (int i = 0; i < 100; i++){
            SieveOfEratosthenes s = new SieveOfEratosthenes(n);
            s.getPrimes();
        }

        timeTaken = 0;
        for (int i = 0; i < 10; i++){
            long startTime = System.nanoTime();
            SieveOfEratosthenes s = new SieveOfEratosthenes(n);
            s.getPrimes();
            long endTime = (System.nanoTime() - startTime);
            System.out.println(endTime);
            timeTaken = timeTaken + endTime;
        }

        SieveOfEratosthenes s = new SieveOfEratosthenes(n);
        sievePrimes = s.getPrimes();

        System.out.println(timeTaken);
        System.out.println("Gjennomsnitt sieve of erothosthenes: " + timeTaken/10);
        System.out.println("\n");
        
        for (int i = 0; i < 100; i++){
            sm = new SieveMasked(n);
            sm.getPrimes();
        }

        for (int i = 0; i < maskedPrimes.length; i++){
            if (maskedPrimes[i] != sievePrimes[i]){
                System.out.println("False");
            }
        }

        timeTaken = 0;
        for (int i = 0; i < 10; i++){
            long startTime = System.nanoTime();
            sm = new SieveMasked(n);
            sm.getPrimes();
            long endTime = (System.nanoTime() - startTime);
            System.out.println(endTime);
            timeTaken = timeTaken + endTime;
        }
        System.out.println(timeTaken);
        System.out.println("Gjennomsnitt masked: " + timeTaken/10);
        System.out.println("\n");

        SieveSevenBits ssb = new SieveSevenBits(n);
        
        for (int i = 0; i < 100; i++){
            ssb = new SieveSevenBits(n);
            sevenPrimes = ssb.getPrimes();
        }

        for (int i = 0; i < maskedPrimes.length; i++){
            if (sevenPrimes[i] != sievePrimes[i]){
                System.out.println("False");
            }
        }
        System.out.println("TRUE");

        timeTaken = 0;
        for (int i = 0; i < 10; i++){
            long startTime = System.nanoTime();
            ssb = new SieveSevenBits(n);
            ssb.getPrimes();
            long endTime = (System.nanoTime() - startTime);
            System.out.println(endTime);
            timeTaken = timeTaken + endTime;
        }
        System.out.println(timeTaken);
        System.out.println("Gjennomsnitt masked seven bits: " + timeTaken/10);
        System.out.println("\n");
    }

    
}
