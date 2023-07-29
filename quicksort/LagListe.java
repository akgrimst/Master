import java.util.Random;

public class LagListe {
    
    public static int[] nyttArray(int storrelse, int seed){
        Random random = new Random(seed);
        int[] tilfeldigListe = new int[storrelse];
        for (int i = 0; i < storrelse; i++){
            tilfeldigListe[i] = random.nextInt();
        }
        return tilfeldigListe;
    }

    public static void main(String[] args) {
        int [] array = nyttArray(1000, 0);
        for (int i = 0; i < 1000; i++){
            System.out.println(array[i]);
        }
    }
}
