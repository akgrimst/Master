/**
 * A possible sequential algorithm for Sieve Of Eratosthenes.
 *
 *
 * @author Shiela Kristoffersen.
 *
 * Recreated from idea by:
 * @author Magnus Espeland
 *
 * His code can be found here:
 * https://github.uio.no/magnuesp/IN3030-v19/blob/master/magnuesp/Sieve/Sieve.java
 *
 * And recreated from implementation by:
 * @author Kim Hilton
 *
 * His code can be found here:
 * https://github.uio.no/kimsh/IN3030_V20/blob/master/Sample_Code/Oblig3/SequentialSieve.java
 *
 *
 * Idea:
 * In the spirit of writing cache friendly code, we want to decrease the size
 * of our data set.
 *
 * Therefore, instead of representing the numbers by integers, we are
 * representing each number as a bit in an array of bytes. Non-primes will have
 * a bit value of 1, and primes will have the bit value 0.
 *
 * We also observe that all even numbers, except 2, are never primes (since
 * they can be divided by 2), and so we only include the odd numbers in our
 * data set.
 *
 * We have now in a single byte, managed to squeeze in a set of 16 numbers;
 * each byte represents an odd number and in between are the even numbers.
 *
 * You can think of the first byte in the array (i.e. byte at index 0) like this:
 *
 * 16_____14_____12_____10_____8_____6_____4_____2_____    <-- Not represented
 *  |  15  |  13  |  11  |  9  |  7  |  5  |  3  |  1  |   <-- The first byte
 *
 *
 * Implementation:
 * We map each number to a specific bit in the byte array, and mark them
 * according to the rules of the sieve. Then we run through the byte array
 * to collect all the unmarked numbers.
 *
 *
 */


class SieveMasked {

    /**
     * Declaring all the global variables
     *
     */
    int n, root, numOfPrimes;
    byte[] oddNumbers;
  
  
    /**
     * Constructor that initializes the global variables
     * @param n Prime numbers up until (and including if prime) 'n' is found
     */
    SieveMasked(int n) {
      this.n = n;
      root = (int) Math.sqrt(n);
      oddNumbers = new byte[(n / 16) + 1];
        
      oddNumbers[0] |= (1 << 0);
      oddNumbers[0] |= (1 << 4);
      oddNumbers[0] |= (1 << 7);

      for (int i = 1; i < oddNumbers.length; i++){
        // int oddCtr = i * 8;
        // System.out.println("odd: " + oddCtr);
        int three_modulo = (i * 8 + 2) % 3;
        int five_modulo = (i * 8 + 3) % 5;
        int seven_modulo = (i * 8 + 4) % 7;

        // System.out.println("\nThree-modulo: " + three_modulo + " Five-modulo: " + five_modulo + " Seven-modulo: " + seven_modulo);
        // System.out.println("Start: " + (i * 16 + 1));

        int ctr = 0;
        while (ctr - three_modulo < 8){
            // System.out.println("Found3: " + (ctr - three_modulo + i * 8));
            oddNumbers[i] |= (1 << (ctr - three_modulo));
            ctr += 3;
        }
        ctr = 0;
        while (ctr - five_modulo < 8){
            // System.out.println("Found5: " + (ctr - five_modulo + i * 16 + 1));
            oddNumbers[i] |= (1 << (ctr - five_modulo));
            ctr += 5;
        }
        ctr = 0;
        while (ctr - seven_modulo < 8){
            // System.out.println("Found7: " + (ctr - seven_modulo + i * 16));
            oddNumbers[i] |= (1 << ctr - seven_modulo);
            ctr += 7;
        }
      }
    //   for (int i = 1; i <= n; i += 2)
    //     if (isPrime(i)){
    //         System.out.println("Prime: " + i);
    //     }
    }
  
  
    /**
     * Performs the sieve and collects the primes produced by the sieve.
     * @return An array containing all the primes up to and including 'n'.
     */
    int[] getPrimes() {
      if (n <= 1) return new int[0];
  
      sieve();
  
      return collectPrimes();
    }
  
  
    /**
     * Iterates through the array to count the number of primes found,
     * creates an array of that size and populates the new array with the primes.
     * @return An array containing all the primes up to and including 'n'.
     */
    private int[] collectPrimes() {
  
      int start = (root % 2 == 0) ? root + 1 : root + 2;
  
      for (int i = start; i <= n; i += 2)
        if (isPrime(i))
          numOfPrimes++;
  
      int[] primes = new int[numOfPrimes];
  
      primes[0] = 2;
      primes[1] = 3;
      primes[2] = 5;
      primes[3] = 7;
  
      int j = 1;
  
      for (int i = 3; i <= n; i += 2)
        if (isPrime(i))
          primes[j++] = i;
  
      return primes;
    }
  
  
    /**
     * Performs the Sieve Of Eratosthenes
     */
    private void sieve() {
      mark(1);
      numOfPrimes = 4;
      int prime = nextPrime(7);
  
      while (prime != -1) {
        traverse(prime);
        prime = nextPrime(prime);
        numOfPrimes++;
      }
    }
  
  
    /**
     * Marks all odd number multiples of 'prime', starting from prime * prime.
     * @param prime The prime used to mark the composite numbers.
     */
    private void traverse(int prime) {
      for (int i = prime*prime; i <= n; i += prime * 2)
        mark(i);
    }
  
  
    /**
     * Finds the next prime in the sequence. If there are no more left, it
     * simply returns -1.
     * @param  prev The last prime that has been used to mark all non-primes.
     * @return      The next prime or -1 if there are no more primes.
     */
    private int nextPrime(int prev) {
      for (int i = prev + 2; i <= root; i += 2)
        if (isPrime(i))
          return i;
  
      return -1;
    }
  
  
    /**
     * Checks if a number is a prime number. If 'num' is prime, it returns true.
     * If 'num' is composite, it returns false.
     * @param  num The number to check.
     * @return     A boolean; true if prime, false if not.
     */
    private boolean isPrime(int num) {
      int bitIndex = (num % 16) / 2;
      int byteIndex = num / 16;
      return (oddNumbers[byteIndex] & (1 << bitIndex)) == 0;
    }
  
  
    /**
     * Marks the number 'num' as a composite number (non-prime)
     * @param num The number to be marked non-prime.
     */
    private void mark(int num) {
      int bitIndex = (num % 16) / 2;
      int byteIndex = num / 16;
      oddNumbers[byteIndex] |= (1 << bitIndex);
    }
  
  
    /**
     * Prints the primes found.
     * @param primes The array containing all the primes.
     */
    static void printPrimes(int[] primes) {
      for (int prime : primes)
        System.out.println(prime);
    }
  
  
    /**
     * Expects a positive integer as an argument.
     * @param args Contains the number up to which we want to find prime numbers.
     */
    public static void main(String[] args) {
  
      int n;
  
      try {
        n = Integer.parseInt(args[0]);
        if (n <= 0) throw new Exception();
      } catch(Exception e) {
        System.out.println("Correct use of program is: " +
        "java SieveOfEratosthenes <n> where <n> is a positive integer.");
        return;
      }
  
      SieveMasked soe = new SieveMasked(n);
  
      /**
       * Getting all the primes equal to and below 'n'
       */
      int[] primes = soe.getPrimes();
  
      /**
       * Printing the primes collected
       */
       printPrimes(primes);
    }
  }