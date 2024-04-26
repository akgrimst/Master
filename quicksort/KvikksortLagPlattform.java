import java.util.Arrays;

public class KvikksortLagPlattform {
    
    public int[] quickSort(int[] tallArray, int lav, int hoy, int lag, int maksLag) throws InterruptedException{
        
        if(lag < maksLag && hoy - lav > 1000){
            int dreiepunkt = partisjon(tallArray, lav, hoy);
            
            QuickSortTraad hoyTraad = new QuickSortTraad(tallArray, dreiepunkt+1, hoy, lag + 1, maksLag);
            Thread htrad = Thread.ofPlatform().start(hoyTraad);
            quickSort(tallArray, lav, dreiepunkt-1, lag + 1, maksLag);

            htrad.join();
            return tallArray;
        }
        else{
            QuickSortSekvensiell sekvensiell = new QuickSortSekvensiell();
            return sekvensiell.quickSort(tallArray, lav, hoy);
        }
    }

    int partisjon(int[] tallArray, int lav, int hoy){
        int dreietapp = tallArray[hoy];
        int dreiepunkt = lav;
        
        for (int i = lav; i < hoy; i++){
            if(tallArray[i] <= dreietapp){
                int midlertidig = tallArray[i];
                tallArray[i] = tallArray[dreiepunkt];
                tallArray[dreiepunkt] = midlertidig;
                dreiepunkt += 1;
            }
        }
        int midlertidig = dreietapp;
        tallArray[hoy] = tallArray[dreiepunkt];
        tallArray[dreiepunkt] = midlertidig;
        return dreiepunkt;
    }

    class QuickSortTraad extends Thread{

        int[] tallArray;
        int lav, hoy, lag, maksLag;

        QuickSortTraad(int[] tallArray, int lav, int hoy, int lag, int maksLag) throws InterruptedException{
            this.tallArray = tallArray;
            this.lav = lav;
            this.hoy = hoy;
            this.lag = lag;
            this.maksLag = maksLag;
        }
        public void run(){
            try{
                quickSort(tallArray, lav, hoy, lag, maksLag);
            }
            catch(InterruptedException e){}
        }
    }


    public static void main(String[] args) {

        int storrelse = 1000000;
        int[] testArr = new LagListe().nyttArray(storrelse, 5814);
        int[] test2Arr = new LagListe().nyttArray(storrelse, 5814);
        Arrays.sort(test2Arr);
        KvikksortLagPlattform para = new KvikksortLagPlattform();
        // para.sekvensiellKvikkSort(testArr, 0, storrelse-1);
        try{
            para.quickSort(testArr, 0, storrelse-1, 0, 3);
        }catch(InterruptedException e){}
        for (int i = 0; i < storrelse; i++){
            // System.out.println(i + " arr2 " + test2Arr[i]);
            System.out.println(i + " arr1 " + testArr[i]);
            if (i > 0){
                if (testArr[i] < testArr[i-1]){
                    System.out.println("Break i " + i + " arr[i]: " + testArr[i] + " arr[i-1]: " + testArr[i-1]);
                }
            }
            if (test2Arr[i] != testArr[i]){
                System.out.println("BREAK: " + i + " test2: " + test2Arr[i] + " test: " + testArr[i]);
                break;
            }
        }
    }
}