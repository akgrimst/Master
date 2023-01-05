import genereringAvPrimtall.sekvensiell.SieveOfEratosthenes;

public class Oblig3Main {
    public static void main(String[] args) {
        int n;
        int c = 0;

        try {
            n = Integer.parseInt(args[0]);
            if (args.length == 1){
                c = Runtime.getRuntime().availableProcessors();
            }else{
                c = Integer.parseInt(args[1]);
            }
            if (n <= 0) throw new Exception();
            if (c <= 0) throw new Exception();
        } catch(Exception e) {
            System.out.println("Correct use of program is: " +
            "java Oblig3Main <n> <c> where <n> and <c> are positive integers.");
            return;
        }

        long ln = 0L;
        ln = n;
        System.out.println("Cores: " + c);

        SieveOfEratosthenes sieve = new SieveOfEratosthenes(n);
        long startTime = System.nanoTime();
        int[] sequentPrimes = sieve.getPrimes();
        long estimatedTime = System.nanoTime() - startTime;
        System.out.println("Sieve Sequential: " + estimatedTime);

        SieveParallel sp = new SieveParallel(n, c);
        startTime = System.nanoTime();
        int[] primes = sp.getPrimes();
        estimatedTime = System.nanoTime() - startTime;
        System.out.println("Sieve Parallel: " + estimatedTime);

        boolean isTrue = true;
        for (int i = 0; i < sequentPrimes.length; i++){
            if (sequentPrimes[i] != primes[i]){
                isTrue = false;
            }
        }
        System.out.println("SieveCheck: " + isTrue);

        Factorization factorization = new Factorization(ln*ln, primes);
        startTime = System.nanoTime();
        Long[] factorized = factorization.factorize();
        estimatedTime = System.nanoTime() - startTime;
        System.out.println("Factorization Sequential: " + estimatedTime);

        FactorizationParallel factorizationParallel = new FactorizationParallel(ln*ln, c, primes);
        startTime = System.nanoTime();
        Long[] parallelFactorized = factorizationParallel.factorize();
        estimatedTime = System.nanoTime() - startTime;
        System.out.println("Factorization Parallel  : " + estimatedTime);

        isTrue = true;
        for (int i = 0; i < factorized.length; i++){
            if (factorized[i] != parallelFactorized[i]){
                isTrue = false;
            }
        }
        System.out.println("FactorizedCheck: " + isTrue);

        

        Oblig3Precode precode = new Oblig3Precode(n);

        long lesserN = 0L;
        
        startTime = System.nanoTime();
        for (int i = 0; i < 100; i++){
            lesserN = ln*ln - (i+1);
            if (lesserN < 1){
                break;
            }
            Factorization f = new Factorization(lesserN, primes);
            Long[] factors = f.factorize();
            for (int j = 0; j < factors.length; j++){
                precode.addFactor(lesserN, factors[j]);
            }
        }
        estimatedTime = System.nanoTime() - startTime;


        precode = new Oblig3Precode(n);
        System.out.println("Factorization Sequential: " + estimatedTime);
        startTime = System.nanoTime();
        for (int i = 0; i < 100; i++){
            lesserN = ln*ln - (i+1);
            if (lesserN < 1){
                break;
            }
            FactorizationParallel fp = new FactorizationParallel(lesserN, c, primes);
            Long[] factors = fp.factorize();
            for (int j = 0; j < factors.length; j++){
                precode.addFactor(lesserN, factors[j]);
            }
        }

        estimatedTime = System.nanoTime() - startTime;
        System.out.println("Factorization Parallel  : " + estimatedTime);

        precode.writeFactors();

        
    }
}