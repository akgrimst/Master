import java.util.Arrays;

public class KvikksortLagVirtuell {
    
    public int[] quickSort(int[] tallArray, int lav, int hoy, int lag, int maksLag) throws InterruptedException{
        
        if(lag < maksLag && hoy - lav > 10000){
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
        int[] arr = new LagListe().nyttArray(10000, 6);
        KvikksortLagVirtuell qss = new KvikksortLagVirtuell();
        int[] java_sortert = arr.clone();
        Arrays.sort(java_sortert);
       try{
        int[] sort_arr = qss.quickSort(arr, 0, arr.length-1, 0, 256);
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