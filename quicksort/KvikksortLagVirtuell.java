import java.util.Arrays;

public class KvikksortLagVirtuell {
    
    int[] quickSort(int[] tallArray, int lav, int hoy, int lag, int maksLag) throws InterruptedException{
        
        if(lag < maksLag){
            int dreiepunkt = partisjon(tallArray, lav, hoy);
            
            QuickSortTraad hoyTraad = new QuickSortTraad(tallArray, dreiepunkt+1, hoy, lag + 1, maksLag);
            Thread htrad = Thread.ofVirtual().start(hoyTraad);

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
        }
        public void run(){
            try{
                quickSort(tallArray, lav, hoy, lag, maksLag);
            }
            catch(InterruptedException e){}
        }
    }


    public static void main(String[] args) {
        int[] arr = {1, 5, 7, 2, 0, 6, 7, 99, -975, 6, 234, -55, 1451, 12131, 5122, 5, 7, 1, 112, 1213, 161, 161,7, 8,4, 8,49,9 ,9,65,74,47 ,47, 4767, 4767 ,77};
        KvikksortLagVirtuell qss = new KvikksortLagVirtuell();
        int[] java_sortert = arr.clone();
        Arrays.sort(java_sortert);
       try{
        int[] sort_arr = qss.quickSort(arr, 0, arr.length-1, 0, 10);
       System.out.println("Ferdig");
        for (int i = 0; i < arr.length; i++){
            if (sort_arr[i] != java_sortert[i]){
                System.out.println("Error: " + sort_arr[i] + " != " + java_sortert[i]);
                break;
            }
            System.out.println(sort_arr[i] + " == " + java_sortert[i]);
        }
    }
        catch(InterruptedException e){}

    }
}