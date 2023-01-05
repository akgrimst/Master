import java.util.ArrayList;

class Factorization{

    long n = 0L;
    int[] primes;

    Factorization(long n, int[] primes){
        this.n = n; 
        this.primes = primes;
    }

    Long[] factorize(){
        ArrayList<Long> list = new ArrayList<Long>();

        for (int i = 0; i < primes.length; i++){
            while (n % primes[i] == 0){

                if (n/primes[i] == 1){
                    long lp = 0L;
                    lp = primes[i];
                    list.add(lp);
                    Long[] array = list.toArray(new Long[0]);
                    return array;
                }
                n = n/primes[i];
                long lp = 0L;
                lp = primes[i];
                list.add(lp);
            }
        }

        if (n != 1){
            list.add(n);
        }

        Long[] array = list.toArray(new Long[0]);

        return array;
    }
}