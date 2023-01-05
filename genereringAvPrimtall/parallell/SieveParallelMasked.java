import java.util.concurrent.CyclicBarrier;

import java.util.Arrays;

public class SieveParallelMasked {
    int n, root, numOfPrimes, c;
    byte[] oddNumbers;
    int[] firstPrimes;
    CyclicBarrier cb;

    SieveParallelMasked(int n, int c){
        this.n = n;
        this.c = c;
        root = (int) Math.sqrt(n);
        oddNumbers = new byte[(n / 14) + 1];

        oddNumbers[0] |= (1 << 0);
        oddNumbers[0] |= (1 << 4);
        oddNumbers[0] |= (1 << 7);

        int hits = 0;

        for (int i = 1; i < oddNumbers.length; i++){
            int three_modulo = (i * 7 + 2) % 3;
            int five_modulo = (i * 7 + 3) % 5;
    
            int ctr = 0;
            while (ctr - three_modulo < 7){
                oddNumbers[i] |= (1 << (ctr - three_modulo));
                ctr += 3;
            }
            ctr = 0;
            while (ctr - five_modulo < 7){
                oddNumbers[i] |= (1 << (ctr - five_modulo));
                ctr += 5;
            }
            oddNumbers[i] |= (1 << 3);
    
            int value = oddNumbers[i];
            if (value == oddNumbers[1]){
                hits += 1;
                if (hits > 1 & hits % 2 == 1){
                    boolean foundPattern = false;
                    for (int j = 1; j < (i - 1)/2 + 1; j++){
                        if (oddNumbers[j] != oddNumbers[j+(i - 1)/2]){
                            break;
                        }
                        foundPattern = true;
                    }
                    if (foundPattern){
                        byte[] pattern = Arrays.copyOfRange(oddNumbers, 1, (i - 1)/2 + 1);
                        
                        int patternOffset = 1;
                        while (patternOffset+pattern.length < oddNumbers.length){
                          int copySize = Math.min(pattern.length, oddNumbers.length - patternOffset - 1);
                          System.arraycopy(pattern, 0, oddNumbers, patternOffset, copySize);
                          patternOffset += pattern.length;
                        }
                        System.arraycopy(pattern, 0, oddNumbers, patternOffset, oddNumbers.length-patternOffset);
                        break;
                    }
                    
                }
                
            }
            
          }

        cb = new CyclicBarrier(c+1);
        SieveOfEratosthenes s = new SieveOfEratosthenes(root);
        firstPrimes = s.getPrimes();
    }

    void sieve(){

        for (int i = 0; i < c; i++){

            double cores = c;

            double ps = n;

            int start = (int) Math.ceil((double) i * (ps/16/cores));

            int stop = (int) Math.ceil((double) ((i+1) * (ps/16/cores)));

            SieveThread p = new SieveThread(start, stop, firstPrimes, oddNumbers);
            Thread t = new Thread(p);
            t.start();
        }
        try{
            cb.await();
        }catch(Exception e){}

        }

    private boolean isPrime(int num) {
        int bitIndex = (num%14)/2;
        int byteIndex = num/14;

        return (oddNumbers[byteIndex] & (1 << bitIndex)) == 0;
    }

    int[] getPrimes(){
        if (n <= 1) return new int[0];

        sieve();
        numOfPrimes = 1;
        for (int i = 1; i <= n; i += 2){
            if (isPrime(i)){
                numOfPrimes++;
            }
        }
        int[] primes = new int[numOfPrimes+1];
        
        primes[0] = 2;
        primes[1] = 3;
        primes[2] = 5;
        primes[3] = 7;

        int j = 4;

        for (int i = 11; i <= n; i+= 2){
            if (isPrime(i)){
                primes[j++] = i;
            }
        }
        return primes;
    }

    class SieveThread implements Runnable{

        int start, stop;
        int[] firstPrimes;
        byte[] oddNumbers;

        SieveThread(int start, int stop, int[] firstPrimes, byte[] oddNumbers){
            this.start = start * 14;
            this.stop = stop * 14;
            this.firstPrimes = firstPrimes;
            this.oddNumbers = oddNumbers;

            if (this.stop > n){
                this.stop = n+1;
            }
            if (this.start < 3){
                this.start = 3;
            }
        }

        public void run(){

            for (int i = 1; i < firstPrimes.length; i++){

                if (firstPrimes[i] > this.stop){
                    break;
                }
                traverse(firstPrimes[i]);
            }
            try{
                cb.await();
            }catch(Exception e){
                return;
            }
        }

        private void traverse(int prime){
            int modulo = start%prime;
            int traverseStart;

            if (modulo == 0){
                if (start%2 == 0){
                    traverseStart = start + prime;
                }else{
                    traverseStart = start;
                }
            }else{
                int modulo2 = start + prime - modulo;
                if (modulo2 % 2 == 0){
                    traverseStart = modulo2 + prime;
                }else{
                    traverseStart = modulo2;
                }
            }

            if (traverseStart == prime){
                traverseStart += prime * 2;
            }

            for (int i = traverseStart; i < stop; i += 2 * prime){
                mark(i);
            }
            
        }

        private void mark(int num){
            int bitIndex = (num%14)/2;
            int byteIndex = num/14;
            oddNumbers[byteIndex] |= (1 << bitIndex);
        }
    }
    public static void main(String[] args) {
        SieveParallelMasked sp = new SieveParallelMasked(10000, 32);
        long startTime = System.nanoTime();
        sp.sieve();
        long timeTaken = System.nanoTime() - startTime;
        System.out.println(timeTaken);
        // 2147483646
        int[] sp_liste = sp.getPrimes();
        // for (int i = 0; i < sp_liste.length; i++){
            // System.out.println(sp_liste[i]);
        // }
    }
}
