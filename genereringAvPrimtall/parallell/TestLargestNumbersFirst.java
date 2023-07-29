import java.util.Arrays;

public class TestLargestNumbersFirst {

    public static void main(String[] args) {

        SjekkOmPrimtall sjekk = new SjekkOmPrimtall();

        int n = 1000;

        System.out.println("Sequential:");
        for (int i = n; i >= 1000; i = i / 10){
            long timeTaken[] = new long[9];
            for (int j = 0; j < 9; j++){
                long startTime = System.nanoTime();
                SieveOfEratosthenes s = new SieveOfEratosthenes(i);
                s.getPrimes();
                timeTaken[j] = System.nanoTime() - startTime;
                System.out.println(sjekk.sjekk(s.getPrimes(), i));
            }
            Arrays.sort(timeTaken);
            System.out.printf("%.3f%n", timeTaken[4] / 1000000.0);        
        }

        System.out.println("\nSequential with masking:");
        for (int i = n; i >= 1000; i = i / 10){
            long timeTaken[] = new long[9];
            for (int j = 0; j < 9; j++){
                long startTime = System.nanoTime();
                SieveSevenBits s = new SieveSevenBits(i);
                s.getPrimes();
                timeTaken[j] = System.nanoTime() - startTime;
                System.out.println(sjekk.sjekk(s.getPrimes(), i));
            }
            Arrays.sort(timeTaken);
            System.out.printf("%.3f%n", timeTaken[4] / 1000000.0);        }

        System.out.println("\nParallell platform threads:");
        for (int t = 4; t < 4000; t = t * 2){
            System.out.println("\nThreads: " + t);
            for (int i = n; i >= 1000; i = i / 10){
                long timeTaken[] = new long[9];
                for (int j = 0; j < 9; j++){
                    long startTime = System.nanoTime();
                    SieveParallel s = new SieveParallel(i, t);
                    s.getPrimes();
                    timeTaken[j] = System.nanoTime() - startTime;
                    System.out.println(sjekk.sjekk(s.getPrimes(), i));
                }
                Arrays.sort(timeTaken);
                System.out.printf("%.3f%n", timeTaken[4] / 1000000.0);            }
        }

        System.out.println("\nParallell platform threads with masking:");
        for (int t = 4; t < 4000; t = t * 2){
            System.out.println("\nThreads: " + t);
            for (int i = n; i >= 1000; i = i / 10){
                long timeTaken[] = new long[9];
                for (int j = 0; j < 9; j++){
                    long startTime = System.nanoTime();
                    SieveParallelMasked s = new SieveParallelMasked(i, t);
                    s.getPrimes();
                    timeTaken[j] = System.nanoTime() - startTime;
                    System.out.println(sjekk.sjekk(s.getPrimes(), i));
                }
                Arrays.sort(timeTaken);
                System.out.printf("%.3f%n", timeTaken[4] / 1000000.0);      }
        }

        System.out.println("\nParallell virtual threads:");
        for (int t = 4; t < 4000; t = t * 2){
            System.out.println("\nThreads: " + t);
            for (int i = n; i >= 1000; i = i / 10){
                long timeTaken[] = new long[9];
                for (int j = 0; j < 9; j++){
                    long startTime = System.nanoTime();
                    SieveParallelVirtual s = new SieveParallelVirtual(i, t);
                    s.getPrimes();
                    timeTaken[j] = System.nanoTime() - startTime;
                    System.out.println(sjekk.sjekk(s.getPrimes(), i));
                }
                Arrays.sort(timeTaken);
                System.out.printf("%.3f%n", timeTaken[4] / 1000000.0);      }
        }

        System.out.println("\nParallell virtual threads with masking:");
        for (int t = 4; t < 4000; t = t * 2){
            System.out.println("\nThreads: " + t);
            for (int i = n; i >= 1000; i = i / 10){
                long timeTaken[] = new long[9];
                for (int j = 0; j < 9; j++){
                    long startTime = System.nanoTime();
                    SieveParallelMaskedVirtual s = new SieveParallelMaskedVirtual(i, t);
                    s.getPrimes();
                    timeTaken[j] = System.nanoTime() - startTime;
                    System.out.println(sjekk.sjekk(s.getPrimes(), i));
                }
                Arrays.sort(timeTaken);
                System.out.printf("%.3f%n", timeTaken[4] / 1000000.0);     
            }
        }
        }
}
