import java.util.ArrayList;
import java.util.concurrent.CyclicBarrier;

public class FactorizationParallel {
    long n = 0L; 
    int c;
    int[] primes;
    ArrayList<Integer>[] factors; 
    CyclicBarrier cb;

    FactorizationParallel(long n, int c, int[] primes){
        this.n = n; 
        this.c = c;
        this.primes = primes;
        factors = new ArrayList[c];
        for (int i = 0; i < c; i++){
            factors[i] = new ArrayList<>();
        }
        cb = new CyclicBarrier(c+1);
    }

    Long[] factorize(){
        
        for (int i = 0; i < c; i++){

            FactorizeThread p = new FactorizeThread(n, i);
            Thread t = new Thread(p);
            t.start();

        }

        try{
            cb.await();
        }catch(Exception e){}

        ArrayList<Long> allFactors = new ArrayList<>();

        for (int i = 0; i < c; i++){
            for (int j = 0; j < factors[i].size(); j++){
                long ln = factors[i].get(j);
                while(n%ln == 0){
                    allFactors.add(ln);
                    n = n/ln;
                }
            }
        }

        long ln = 0L;
        if (n != 1){
            ln = n;
            allFactors.add(ln);
        }
        
        Long[] array = allFactors.toArray(new Long[0]);
        return array;
    }

    class FactorizeThread implements Runnable{

        int thread;
        long num = 0L;

        FactorizeThread(long num, int thread){
            this.num = num;
            this.thread = thread;
        }

        public void run(){
            int l = primes.length;
            for (int i = thread; i < l; i += c){
                int p = primes[i];
                while (num % p == 0){
                    num = num / p;
                    factors[thread].add(p);
                }
            }
            try{
                cb.await();
            }catch(Exception e){
                return;
            }
        }
    }
}