public class SjekkOmPrimtall {
    public boolean sjekk(int[] input, int n){
        SieveOfEratosthenes sekvensiell = new SieveOfEratosthenes(n);
        int[] primtall = sekvensiell.getPrimes();

        if (primtall.length == input.length){
            for (int i = 0; i < primtall.length; i++){
                if (primtall[i] != input[i]){
                    System.out.println("Feil på plass " + i + ". Forventet: " + primtall[i] + " Mottatt: " + input[i]);
                    return false;
                }
            }
            return true;
        }
        else{
            System.out.println("Feil lengde. Forventet: " + primtall.length + " Mottatt: " + input.length);
            for (int i = 0; i < primtall.length; i++){
                if (primtall[i] != input[i]){
                    System.out.println("Feil på plass " + i + ". Forventet: " + primtall[i] + " Mottatt: " + input[i]);
                    return false;
                }
            }
            return false;
        }
    }
}
