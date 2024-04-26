import java.util.concurrent.CyclicBarrier;
import java.security.spec.ECFieldF2m;
import java.util.Arrays;
import java.util.Random;

public class ParaSortPlattform {
    CyclicBarrier[] segBarriers;
    CyclicBarrier cb;
    int[] pivot;
    
    public int[] paraSort(int k, int[] arr, int venstre, int hoyre) throws InterruptedException{
        pivot = new int[k];
        segBarriers = new CyclicBarrier[k];
        cb = new CyclicBarrier(k+1, null);
        segBarriers[0] = new CyclicBarrier(k, null);
        for (long i = 0; i < k; i++){
            long traad_lav = i * (hoyre-venstre + 1) / k;
            long traad_hoy = (i+1) * (hoyre-venstre + 1) / k - 1;
            ParaTraad traad = new ParaTraad(arr, traad_lav, traad_hoy, i, (long) venstre, (long) hoyre, 0, k-1, (int) i);
            Thread.ofPlatform().start(traad);
        }
        try{
            cb.await();
        }catch(Exception e){}
        
        return arr;
    }

    class ParaTraad extends Thread{

        int[] arr;
        long lav, hoy, lav_traad, hoy_traad, traad_id;
        long arr_lav, arr_hoy, traad_nr;

        ParaTraad(int[] arr, long lav, long hoy, long traad_nr, long arr_lav, long arr_hoy, long lav_traad, long hoy_traad, long traad_id) throws InterruptedException{
            this.arr = arr;
            this.lav = lav;
            this.hoy = hoy;
            this.traad_nr = traad_nr;
            this.arr_lav = arr_lav;
            this.arr_hoy = arr_hoy;
            this.lav_traad = lav_traad;
            this.hoy_traad = hoy_traad;
            this.traad_id = traad_id;
        }

        public void run(){
            try{
                traad_funksjon(arr, lav, hoy, traad_nr, arr_lav, arr_hoy, lav_traad, hoy_traad, traad_id);
            }catch(Exception e){}
            segBarriers[(int) traad_id] = null;
            try{
                cb.await();
            }catch(Exception e){}
        }
    }

    void traad_funksjon(int[] arr, long lav, long hoy, long traad_nr, long arr_lav, long arr_hoy, long lav_traad, long hoy_traad, long traad_id) throws InterruptedException{
        
        int barr_indeks = (int) traad_id - (int) traad_nr;
        int m = (int) arr_lav + ((int) arr_hoy- (int) arr_lav + 1)/2;
        int[] valg = {arr[m-1], arr[m], arr[m+1]};
        innstikkSortering(valg, 0, 2);
        int dreietapp = valg[1];
        
        try{
            segBarriers[barr_indeks].await();
        }catch(Exception e){}
        // Steg 2

        long dreiepunkt = lav;        
        for (long i = lav; i <= hoy; i++){
            
            if(arr[(int) i] < dreietapp){
                int midlertidig = arr[(int) i];
                arr[(int) i] = arr[(int) dreiepunkt];
                arr[(int) dreiepunkt] = midlertidig;
                dreiepunkt += 1;
            }
        }

        if (dreiepunkt-lav > hoy){
            pivot[(int) traad_id] = -1;
        }else{
            pivot[(int) traad_id] = (int) (dreiepunkt-lav);
        }

        try{
            segBarriers[barr_indeks].await();
        }catch(Exception e){}
        // Steg 3

        int pivot_index = (int) arr_lav;
        for (long i = traad_id - (int) traad_nr; i <= traad_id - traad_nr + hoy_traad; i++){
            if (pivot[(int) i] > -1){
                pivot_index += pivot[(int) i];
            }
        }

        long store_til_venstre = 0;
        for (long i = lav_traad; i <= hoy_traad; i++){
            long traad_lav = arr_lav + i * (arr_hoy-arr_lav + 1) / (hoy_traad + 1);
            
            if (traad_lav < pivot_index){
                long traad_hoy = arr_lav + (i+1) * (long) (arr_hoy-arr_lav + 1) / (long) (hoy_traad + 1) - 1;
                if (traad_hoy < pivot_index){
                    store_til_venstre += traad_hoy - (traad_lav + pivot[(int) (barr_indeks + i)] - 1);
                }else{
                    if (traad_lav + pivot[(int) (barr_indeks + i)] < pivot_index){
                        store_til_venstre += pivot_index - (traad_lav + pivot[(int) (barr_indeks + i)]);
                        break;
                    }
                    break;
                }
            }else{
                break;
            }
        }

        try{
            segBarriers[barr_indeks].await();
        }catch(Exception e){}

        long start_nr_long = (long)(traad_nr * store_til_venstre) / (hoy_traad + 1);
        long slutt_nr_long = (long)(((traad_nr + 1) * store_til_venstre) / (hoy_traad + 1)) - 1;
        int start_nr = (int) start_nr_long;
        int slutt_nr = (int) slutt_nr_long;
        int storre_funnet = 0;
        int venstre_start_partisjon = 0;
        int venstre_partisjon_indeks = 0;
        if (slutt_nr < start_nr){
            try{
            segBarriers[barr_indeks].await();
            segBarriers[barr_indeks].await();
            }catch(Exception e){}
            delOppTraader(arr, lav, hoy, traad_nr, arr_lav, arr_hoy, lav_traad, hoy_traad, traad_id, barr_indeks, pivot_index);
            return;
        }
        for (int i = 0; i < (hoy_traad-lav_traad+1); i++){
            long start_indeks_long = (long) arr_lav + (i * (arr_hoy-arr_lav + 1))/(hoy_traad + 1);
            long slutt_indeks_long = (long) arr_lav + ((i+1) * (arr_hoy-arr_lav + 1))/(hoy_traad + 1) - 1;
            int start_indeks = (int) start_indeks_long;
            int slutt_indeks = (int) slutt_indeks_long;
            int intervall = slutt_indeks - start_indeks;
            if (pivot[barr_indeks + i] == -1){
            }else if (storre_funnet + (intervall - pivot[barr_indeks + i]) < start_nr){
                storre_funnet += intervall - pivot[barr_indeks + i] + 1;
            }else{
                venstre_start_partisjon = i;
                venstre_partisjon_indeks = pivot[barr_indeks + i] + start_nr - storre_funnet;
                break;
            }
        }
        long mindre_funnet = 0;
        long hoyre_start_partisjon = 0;
        long hoyre_partisjon_indeks = 0;

        for (long i = (hoy_traad + 1) - 1; i >= 0; i--){
            if (mindre_funnet + pivot[(int) (barr_indeks + i)] <= start_nr){
                mindre_funnet += pivot[(int) (barr_indeks + i)];
            }else if (mindre_funnet + pivot[(int) (barr_indeks + i)] > start_nr){
                hoyre_start_partisjon = i;
                hoyre_partisjon_indeks = pivot[(int) (barr_indeks + i)] - (start_nr - mindre_funnet) - 1;
                break;
            }
        }

        for (int i = start_nr; i <= slutt_nr; i++){
            long storre_tall = (long) arr_lav + (venstre_start_partisjon * (arr_hoy-arr_lav + 1) / (hoy_traad-lav_traad+1)) + venstre_partisjon_indeks;
            
            if (storre_tall == (long) arr_lav + ((venstre_start_partisjon + 1) * (arr_hoy-arr_lav + 1) / (hoy_traad-lav_traad+1))){
                for (int j = venstre_start_partisjon + 1; j < (hoy_traad-lav_traad+1); j++){
                    long start_indeks_long = (long) (j * (arr_hoy-arr_lav + 1))/(hoy_traad-lav_traad+1);
                    long slutt_indeks_long = (long) ((j+1) * (arr_hoy-arr_lav + 1))/(hoy_traad-lav_traad+1) - 1;
                    int start_indeks = (int) start_indeks_long;
                    int slutt_indeks = (int) slutt_indeks_long;
                    int intervall = slutt_indeks - start_indeks;
                    if (intervall >= pivot[barr_indeks + j]){
                        venstre_start_partisjon = j;
                        venstre_partisjon_indeks = pivot[barr_indeks + j];
                        storre_tall = (long) arr_lav + (venstre_start_partisjon * (arr_hoy-arr_lav + 1) / (hoy_traad-lav_traad+1)) + venstre_partisjon_indeks;
                        venstre_partisjon_indeks++;
                        break;
                    }
                }
            }else{
                venstre_partisjon_indeks++;
            }
            int midlertidig = arr[(int) storre_tall];
            long arr_lav_long = (long) arr_lav;
            long arr_hoy_long = (long) arr_hoy;
            long mindre_tall = (long)  arr_lav_long + (hoyre_start_partisjon * (arr_hoy_long-arr_lav_long + 1) / (hoy_traad-lav_traad+1)) + hoyre_partisjon_indeks;
            
            if (mindre_tall == (long) arr_lav + (hoyre_start_partisjon * (arr_hoy-arr_lav + 1) / (hoy_traad-lav_traad+1)) - 1){
                for (long j = hoyre_start_partisjon - 1; j > 0; j--){
                    if (pivot[(int) (barr_indeks + j)] == 0){
                        
                    }
                    else{
                        hoyre_start_partisjon = j;
                        hoyre_partisjon_indeks = pivot[(int) (barr_indeks + j)] - 1;
                        mindre_tall = (long) arr_lav + (hoyre_start_partisjon * (arr_hoy-arr_lav + 1) / (hoy_traad-lav_traad+1)) + hoyre_partisjon_indeks;
                        hoyre_partisjon_indeks--;
                        break;
                    }
                }
            }else{
                hoyre_partisjon_indeks--;
            }

            arr[(int) storre_tall] = arr[(int) mindre_tall];
            arr[(int) mindre_tall] = midlertidig;
        }
        try{
            segBarriers[barr_indeks].await();
        }catch(Exception e){}
        
        try{
            segBarriers[barr_indeks].await();
        }catch(Exception e){}
        
        delOppTraader(arr, lav, hoy, traad_nr, arr_lav, arr_hoy, lav_traad, hoy_traad, traad_id, barr_indeks, pivot_index);
    }

    void delOppTraader(int[] arr, long lav, long hoy, long traad_nr, long arr_lav, long arr_hoy, long lav_traad, long hoy_traad, long traad_id, int barr_indeks, int pivot_indeks)
    {
        
        if (hoy_traad <= 2)
        {
            if (hoy_traad == 1)
            {
                if (traad_nr == 0)
                {
                    try
                    {
                        segBarriers[barr_indeks].await();
                    }catch (Exception e){}
                    sekvensiellKvikkSort(arr, (int) arr_lav, pivot_indeks - 1);
                }
                else
                {
                    try
                    {
                        segBarriers[barr_indeks].await();
                    }catch (Exception e){}
                    sekvensiellKvikkSort(arr, pivot_indeks, (int) arr_hoy);
                }
            }
            else
            {
                if (pivot_indeks < (arr_hoy - arr_lav) / 2)
                {
                    if (traad_nr == 0)
                    {
                        try
                        {
                            segBarriers[barr_indeks].await();
                        }catch (Exception e){}
                        sekvensiellKvikkSort(arr, (int) arr_lav, pivot_indeks - 1);
                    }
                    else if (traad_nr == 1)
                    {
                        segBarriers[(int) traad_id] = new CyclicBarrier(2);
                        try
                        {
                            segBarriers[barr_indeks].await();
                            long ny_hoy = nyIndeksering(pivot_indeks, arr_hoy, 1, 2) - 1;
                            traad_funksjon(arr, pivot_indeks, ny_hoy, 0, pivot_indeks, arr_hoy, 0, 1, traad_id);
                        }catch (Exception e){}
                    }
                    else
                    {
                        try
                        {
                            segBarriers[barr_indeks].await();
                            long ny_lav = nyIndeksering(pivot_indeks, arr_hoy, 1, 2);
                            traad_funksjon(arr, ny_lav, arr_hoy, 1, pivot_indeks, arr_hoy, 0, 1, traad_id);
                        }catch (Exception e){}
                    }
                }
                else
                {
                    if (traad_nr == 2)
                    {
                        try
                        {
                            segBarriers[barr_indeks].await();
                        }catch (Exception e){}
                        sekvensiellKvikkSort(arr, pivot_indeks, (int) arr_hoy);
                    }
                    else if (traad_nr == 0)
                    {
                        segBarriers[barr_indeks + 1] = new CyclicBarrier(2);
                        try
                        {
                            segBarriers[barr_indeks].await();
                            segBarriers[barr_indeks] = new CyclicBarrier(2);
                            segBarriers[barr_indeks + 1].await();
                            long ny_hoy = nyIndeksering(arr_lav, pivot_indeks - 1, traad_nr + 1, 2) - 1;
                            traad_funksjon(arr, arr_lav, ny_hoy, 0, arr_lav, pivot_indeks - 1, 0, 1, traad_id);
                        }catch (Exception e){}
                    }
                    else
                    {
                        try
                        {
                            segBarriers[barr_indeks].await();
                            segBarriers[barr_indeks + 1].await();
                            long ny_lav = nyIndeksering(arr_lav, pivot_indeks-1, traad_nr, 2);
                            traad_funksjon(arr, ny_lav, pivot_indeks - 1, 1, arr_lav, pivot_indeks - 1, 0, 1, traad_id);
                        }catch (Exception e){}
                    }
                }
            }
        }
        else
        {
            long traader_under_pivot = (pivot_indeks - arr_lav) / ((arr_hoy - arr_lav) / (hoy_traad + 1));

            if (traader_under_pivot == 0)
            {
                if (traad_nr == 0)
                {
                    try
                    {
                        segBarriers[barr_indeks].await();
                    } catch (Exception e){}
                    sekvensiellKvikkSort(arr, (int) arr_lav, pivot_indeks - 1);
                }
                else
                {
                    if (traad_nr == 1)
                    {
                        segBarriers[(int) traad_id] = new CyclicBarrier((int) hoy_traad);
                    }
                    try
                    {
                        segBarriers[barr_indeks].await();
                        long ny_lav = (long) pivot_indeks + (traad_nr - 1) * (arr_hoy - (long) pivot_indeks + 1) / hoy_traad;
                        long ny_hoy = (long) pivot_indeks + (traad_nr) * ((long) arr_hoy - (long) pivot_indeks + 1) / hoy_traad - 1;
                        traad_funksjon(arr, ny_lav, ny_hoy, traad_nr - 1, pivot_indeks, arr_hoy, 0, hoy_traad - 1, traad_id);
                    } catch (Exception e){}
                    
                }
            }
            else if (traader_under_pivot == hoy_traad)
            {
                if (traad_nr == hoy_traad)
                {
                    try
                    {
                        segBarriers[barr_indeks].await();
                    } catch (Exception e){}
                    sekvensiellKvikkSort(arr, pivot_indeks, (int) arr_hoy);
                }
                else
                {
                    if (traad_nr == 0)
                    {
                        try
                        {
                            segBarriers[barr_indeks + 1] = new CyclicBarrier((int) hoy_traad);
                            segBarriers[barr_indeks].await();
                            segBarriers[barr_indeks] = new CyclicBarrier((int) hoy_traad);
                            segBarriers[barr_indeks + 1].await();
                        } catch (Exception e){}
                    }
                    else
                    {
                        try
                        {
                            segBarriers[barr_indeks].await();
                            segBarriers[barr_indeks + 1].await();
                        } catch (Exception e){}
                    }
                    long ny_lav = nyIndeksering(arr_lav, pivot_indeks-1, traad_nr, hoy_traad);
                    long ny_hoy = nyIndeksering(arr_lav, pivot_indeks - 1, traad_nr + 1, hoy_traad) - 1;
                    try
                    {
                        
                        traad_funksjon(arr, ny_lav, ny_hoy, traad_nr, arr_lav, pivot_indeks - 1, 0, hoy_traad - 1, traad_id);
                    } catch (Exception e){}
                }
            }
            else
            {
                if (traad_nr == lav_traad)
                {
                    if (traader_under_pivot == 1)
                    {
                        try
                        {
                            segBarriers[barr_indeks].await();
                            sekvensiellKvikkSort(arr, (int) arr_lav, pivot_indeks - 1);
                            return;
                        } catch (Exception e){}
                    }
                    else
                    {
                        segBarriers[barr_indeks + 1] = new CyclicBarrier((int) traader_under_pivot);
                        try
                        {
                            segBarriers[barr_indeks].await();
                            segBarriers[barr_indeks] = new CyclicBarrier((int) traader_under_pivot);
                        } catch (Exception e){}
                    }

                }
                else if (traad_nr == traader_under_pivot)
                {
                    if (traader_under_pivot == hoy_traad)
                    {
                        try
                        {
                            segBarriers[barr_indeks].await();
                            sekvensiellKvikkSort(arr, pivot_indeks, (int) arr_hoy);
                            return;
                        } catch (Exception e){}
                    }
                    else
                    {
                        segBarriers[(int) traad_id + 1] = new CyclicBarrier((int) hoy_traad - (int) traader_under_pivot + 1);
                        try
                        {
                            segBarriers[barr_indeks].await();
                            segBarriers[barr_indeks + (int) traader_under_pivot] = new CyclicBarrier((int) hoy_traad - (int) traader_under_pivot + 1);
                        } catch (Exception e){}
                    }
                }
                else
                {
                    try
                    {
                        segBarriers[barr_indeks].await();
                    } catch (Exception e){}
                }
                if (traad_nr < traader_under_pivot)
                {
                    try
                    {
                        segBarriers[barr_indeks + 1].await();
                        long ny_lav = arr_lav + traad_nr * ((long) pivot_indeks - arr_lav) / (traader_under_pivot);
                        long ny_hoy = arr_lav + (traad_nr + 1) * ((long) pivot_indeks - arr_lav) / (traader_under_pivot) - 1;
                        traad_funksjon(arr, ny_lav, ny_hoy, traad_nr, arr_lav, pivot_indeks - 1, 0, traader_under_pivot - 1, traad_id);
                    } catch (Exception e){}
                }
                else
                {
                    try
                    {
                        segBarriers[barr_indeks + (int) traader_under_pivot + 1].await();
                        long ny_traadnr = traad_nr - traader_under_pivot;
                        long ny_lav = (long) pivot_indeks + (ny_traadnr) * (arr_hoy - (long) pivot_indeks + 1) / (hoy_traad - traader_under_pivot + 1);
                        long ny_hoy = (long) pivot_indeks + (ny_traadnr + 1) * ((long) arr_hoy - (long) pivot_indeks + 1) / (hoy_traad - traader_under_pivot + 1) - 1;
                        traad_funksjon(arr, ny_lav, ny_hoy, ny_traadnr, pivot_indeks, arr_hoy, 0, hoy_traad - traader_under_pivot, traad_id);
                    } catch (Exception e){}
                }
            }
        }
    }

    int[] sekvensiellKvikkSort(int[] tallArray, int lav, int hoy){
        if(hoy - lav > 31){
            int dreiepunkt = partisjon(tallArray, lav, hoy);
            if (dreiepunkt > hoy){
                dreiepunkt = partisjon(tallArray, lav, hoy-1);
                sekvensiellKvikkSort(tallArray, lav, hoy-1);
            }else{
                sekvensiellKvikkSort(tallArray, lav, dreiepunkt - 1);
                sekvensiellKvikkSort(tallArray, dreiepunkt, hoy);
            }
            
        }
        else if(hoy > lav){
            innstikkSortering(tallArray, lav, hoy);
        }
        return tallArray;
    }

    long nyIndeksering(long lav, long hoy, long traad_nr, long ant_traader)
    /* Finner ny inndeling,  lav eller hoy */
    {
        return (long) (lav + traad_nr * (long) (hoy - lav + 1) / ant_traader);
    }

    int partisjon(int[] tallArray, int lav, int hoy){
        int m = (lav + hoy) / 2;
        int valg[] = {tallArray[m-1], tallArray[m], tallArray[m+1]};
        innstikkSortering(valg, 0, 2);
        int dreietapp = valg[1];
        int dreiepunkt = lav;
        
        for (int i = lav; i <= hoy; i++){
            if(tallArray[i] <= dreietapp){
                int midlertidig = tallArray[i];
                tallArray[i] = tallArray[dreiepunkt];
                tallArray[dreiepunkt] = midlertidig;
                dreiepunkt += 1;
            }
        }
        if (dreiepunkt > hoy){
            for (int i = m-1; i <= m+1; i++){
                if (tallArray[i] == dreietapp){
                    int midlertidig = tallArray[i];
                    tallArray[i] = tallArray[hoy];
                    tallArray[hoy] = midlertidig;
                    break;
                }
            }
        }
        return dreiepunkt;
    }

    private void innstikkSortering(int[] tallArray, int lav, int hoy){
        int i = lav+1;
        while(i <= hoy){
            int j = i;
            while (j > lav){
                if (tallArray[j] < tallArray[j-1]){
                    int midlertidig = tallArray[j];
                    tallArray[j] = tallArray[j-1];
                    tallArray[j-1] = midlertidig;
                    j--;
                }else{
                    break;
                }
            }
            i++;
        }
    }


    public static void main(String[] args) {
        int storrelse = 1000000;
        int[] testArr = new LagListe().nyttArray(storrelse, 5814);
        int[] test2Arr = new LagListe().nyttArray(storrelse, 5814);
        Arrays.sort(test2Arr);
        ParaSortPlattform para = new ParaSortPlattform();
        // para.sekvensiellKvikkSort(testArr, 0, storrelse-1);
        try{
            para.paraSort(64, testArr, 0, storrelse-1);
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
