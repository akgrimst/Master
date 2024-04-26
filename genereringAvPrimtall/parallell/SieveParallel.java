import java.util.concurrent.CyclicBarrier;

public class SieveParallel {
    int n, root, numOfPrimes, c;
    byte[] oddNumbers;
    int[] firstPrimes;
    CyclicBarrier cb;

    SieveParallel(int n, int c){
        this.n = n;
        this.c = c;
        root = (int) Math.sqrt(n);
        oddNumbers = new byte[(n / 16) + 1];
        cb = new CyclicBarrier(c+1);
        SieveOfEratosthenes s = new SieveOfEratosthenes(root);
        firstPrimes = s.getPrimes();
    }

    void sieve(){

        for (int i = 0; i < c; i++){

            double cores = c;

            double ps = n;

            int start = (int) Math.ceil((double) i * (ps/cores));

            int stop = (int) Math.ceil((double) ((i+1) * (ps/cores)));

            SieveThread p = new SieveThread(start, stop, firstPrimes, oddNumbers);
            Thread t = new Thread(p);
            t.start();
        }
        try{
            cb.await();
        }catch(Exception e){}

        }

    private boolean isPrime(int num) {
        int bitIndex = (num%16)/2;
        int byteIndex = num/16;

        return (oddNumbers[byteIndex] & (1 << bitIndex)) == 0;
    }

    int[] getPrimes(){
        if (n <= 1) return new int[0];

        sieve();
        numOfPrimes = 0;

        for (int i = 1; i <= n; i += 2){
            if (isPrime(i)){
                numOfPrimes++;
            }
        }
        int[] primes = new int[numOfPrimes];
        
        primes[0] = 2;

        int j = 1;

        for (int i = 3; i <= n; i+= 2){
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
            System.out.println(start + " " + stop);
            this.start = start;
            this.stop = stop;
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
            System.err.println("start: " + start + " slutt: " + stop);

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
            int modulo = start % prime;
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
            int bitIndex = (num%16)/2;
            int byteIndex = num/16;
            oddNumbers[byteIndex] |= (1 << bitIndex);
        }
    }
    public static void main(String[] args) {
        SieveParallel sp = new SieveParallel(100, 32);
        sp.sieve();
        int[] sp_liste = sp.getPrimes();
        for (int i = 0; i < sp_liste.length; i++){
            System.out.println(sp_liste[i]);
        }
    }
}
