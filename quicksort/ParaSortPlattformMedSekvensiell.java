import java.util.concurrent.CyclicBarrier;
import java.util.Arrays;

public class ParaSortPlattformMedSekvensiell 
{
    CyclicBarrier[] segBarriers;
    CyclicBarrier cb;
    int[] pivot;
    int[] arr;
    int sekvensiell_grense;
    
    public int[] paraSort(int k, int[] arr, int venstre, int hoyre) throws InterruptedException
    {
        sekvensiell_grense = 10000;
        pivot = new int[k];
        this.arr = arr;
        segBarriers = new CyclicBarrier[k];
        cb = new CyclicBarrier(k+1, null);
        segBarriers[0] = new CyclicBarrier(k, null);
        for (int i = 0; i < k; i++){
            int traad_lav = nyIndeksering(venstre, hoyre, i, k);
            int traad_hoy = nyIndeksering(venstre, hoyre, i+1, k) - 1;
            ParaTraad traad = new ParaTraad(traad_lav, traad_hoy, i, venstre, hoyre, k-1, i);
            Thread.ofPlatform().start(traad);
        }
        try{
            cb.await();
        }catch(Exception e){}
        
        return arr;
    }

    class ParaTraad extends Thread{
        int lav, hoy, hoy_traad, traad_id, arr_lav, arr_hoy, traad_nr;

        ParaTraad(int lav, int hoy, int traad_nr, int arr_lav, int arr_hoy, int hoy_traad, int traad_id) throws InterruptedException
        {
            this.lav = lav;
            this.hoy = hoy;
            this.traad_nr = traad_nr;
            this.arr_lav = arr_lav;
            this.arr_hoy = arr_hoy;
            this.hoy_traad = hoy_traad;
            this.traad_id = traad_id;
        }

        public void run()
        {
            try{
                traad_funksjon(lav, hoy, traad_nr, arr_lav, arr_hoy, hoy_traad, traad_id);
            }catch(Exception e){}
            segBarriers[(int) traad_id] = null;
            try{
                cb.await();
            }catch(Exception e){}
        }
    }

    void traad_funksjon(int lav, int hoy, int traad_nr, int arr_lav, int arr_hoy, int hoy_traad, int traad_id) throws InterruptedException
    {
        int barr_indeks = (int) traad_id - (int) traad_nr;
        int m = (int) arr_lav + ((int) arr_hoy- (int) arr_lav + 1)/2;
        int[] valg = {arr[m-1], arr[m], arr[m+1]};
        innstikkSortering(valg, 0, 2);
        int dreietapp = valg[1];
        
        try{
            segBarriers[barr_indeks].await();
        }catch(Exception e){}
        // Steg 2

        finnDreiepunkt(lav, hoy, dreietapp, traad_id);

        try{
            segBarriers[barr_indeks].await();
        }catch(Exception e){}
        // Steg 3

        int pivot_indeks = finnPivotIndeks(arr_lav, traad_id, traad_nr, hoy_traad);

        int store_til_venstre = finnStoreTilVenstre(arr_lav, arr_hoy, hoy_traad, barr_indeks, pivot_indeks);

        try{
            segBarriers[barr_indeks].await();
        }catch(Exception e){}

        long start_nr = (traad_nr * (long) store_til_venstre) / (long) (hoy_traad + 1);
        long slutt_nr = (traad_nr + 1) * (long) store_til_venstre / (long) (hoy_traad + 1) - 1;
        // System.out.println("Tråd: " + traad_id + " startnr: " + start_nr + " sluttnr: " + slutt_nr);
        if (slutt_nr < start_nr){
            try{
                segBarriers[barr_indeks].await();
            }catch(Exception e){}

            delOppTraader(lav, hoy, traad_nr, arr_lav, arr_hoy, hoy_traad, traad_id, barr_indeks, pivot_indeks);
            return;
        }
        int[] swap_indekser = finnSwapStartIndeks(arr_lav, arr_hoy, hoy_traad, barr_indeks, (int) start_nr);

        swapStorreOgMindre(arr_lav, arr_hoy, hoy_traad, (int) start_nr, (int) slutt_nr, barr_indeks, swap_indekser[0], swap_indekser[1], swap_indekser[2], swap_indekser[3]);
        
        try{
            segBarriers[barr_indeks].await();
        }catch(Exception e){}
        
        delOppTraader(lav, hoy, traad_nr, arr_lav, arr_hoy, hoy_traad, traad_id, barr_indeks, pivot_indeks);
    }

    void delOppTraader(int lav, int hoy, int traad_nr, int arr_lav, int arr_hoy, int hoy_traad, int traad_id, int barr_indeks, int pivot_indeks)
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
                    sekvensiellKvikkSort(arr_lav, pivot_indeks - 1);
                }
                else
                {
                    try
                    {
                        segBarriers[barr_indeks].await();
                    }catch (Exception e){}
                    sekvensiellKvikkSort(pivot_indeks, arr_hoy);
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
                        sekvensiellKvikkSort(arr_lav, pivot_indeks - 1);
                    }
                    else
                    {
                        if (arr_hoy - pivot_indeks < sekvensiell_grense)
                        {
                            try
                            {
                                segBarriers[barr_indeks].await();
                            }catch (Exception e){}
                            if (traad_nr == 1)
                            {
                                sekvensiellKvikkSort(pivot_indeks, arr_hoy);
                            }
                            return;
                        }
                        if (traad_nr == 1)
                        {
                            segBarriers[(int) traad_id] = new CyclicBarrier(2);
                            try
                            {
                                segBarriers[barr_indeks].await();
                                int ny_hoy = nyIndeksering(pivot_indeks, arr_hoy, 1, 2) - 1;
                                traad_funksjon(pivot_indeks, ny_hoy, 0, pivot_indeks, arr_hoy, 1, traad_id);
                            }catch (Exception e){}
                        }
                        else
                        {
                            try
                            {
                                segBarriers[barr_indeks].await();
                                int ny_lav = nyIndeksering(pivot_indeks, arr_hoy, 1, 2);
                                traad_funksjon(ny_lav, arr_hoy, 1, pivot_indeks, arr_hoy, 1, traad_id);
                            }catch (Exception e){}
                        }
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
                        sekvensiellKvikkSort(pivot_indeks, (int) arr_hoy);
                    }
                    else 
                    {
                        if (pivot_indeks - 1 - arr_lav < sekvensiell_grense)
                        {
                            try
                            {
                                segBarriers[barr_indeks].await();
                            }catch (Exception e){}
                            if (traad_nr == 0)
                            {
                                sekvensiellKvikkSort(arr_lav, pivot_indeks - 1);
                            }
                            return;
                        }

                        if (traad_nr == 0)
                        {
                            segBarriers[barr_indeks + 1] = new CyclicBarrier(2);
                            try
                            {
                                segBarriers[barr_indeks].await();
                                segBarriers[barr_indeks] = new CyclicBarrier(2);
                                segBarriers[barr_indeks + 1].await();
                                int ny_hoy = nyIndeksering(arr_lav, pivot_indeks - 1, traad_nr + 1, 2) - 1;
                                traad_funksjon(arr_lav, ny_hoy, 0, arr_lav, pivot_indeks - 1, 1, traad_id);
                            }catch (Exception e){}
                        }
                        else
                        {
                            try
                            {
                                segBarriers[barr_indeks].await();
                                segBarriers[barr_indeks + 1].await();
                                int ny_lav = nyIndeksering(arr_lav, pivot_indeks-1, traad_nr, 2);
                                traad_funksjon(ny_lav, pivot_indeks - 1, 1, arr_lav, pivot_indeks - 1, 1, traad_id);
                            }catch (Exception e){}
                        }
                    }
                }
            }
        }
        else
        {
            int traader_under_pivot = (pivot_indeks - arr_lav) / ((arr_hoy - arr_lav) / (hoy_traad + 1));

            if (traader_under_pivot == 0)
            {
                if (traad_nr == 0)
                {
                    try
                    {
                        segBarriers[barr_indeks].await();
                    } catch (Exception e){}
                    sekvensiellKvikkSort(arr_lav, pivot_indeks - 1);
                }
                else
                {
                    if (arr_hoy - pivot_indeks < sekvensiell_grense)
                    {
                        try
                        {
                            segBarriers[barr_indeks].await();
                        } catch (Exception e){}
                        if (traad_nr == 1)
                        {
                            sekvensiellKvikkSort(pivot_indeks, arr_hoy);
                        }
                        return;
                    }

                    if (traad_nr == 1)
                    {
                        segBarriers[(int) traad_id] = new CyclicBarrier((int) hoy_traad);
                    }
                    try
                    {
                        segBarriers[barr_indeks].await();
                        int ny_lav = nyIndeksering(pivot_indeks, arr_hoy, traad_nr - 1, hoy_traad);
                        int ny_hoy = nyIndeksering(pivot_indeks, arr_hoy, traad_nr, hoy_traad) - 1;
                        traad_funksjon(ny_lav, ny_hoy, traad_nr - 1, pivot_indeks, arr_hoy, hoy_traad - 1, traad_id);
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
                    sekvensiellKvikkSort(pivot_indeks, arr_hoy);
                }
                else
                {
                    if (pivot_indeks - 1 - arr_lav < sekvensiell_grense)
                    {
                        try
                        {
                            segBarriers[barr_indeks].await();
                        } catch (Exception e){}
                        if (traad_nr == 0)
                        {
                            sekvensiellKvikkSort(arr_lav, pivot_indeks - 1);
                        }
                        return;
                    }
                    if (traad_nr == 0)
                    {
                        try
                        {
                            segBarriers[barr_indeks + 1] = new CyclicBarrier(hoy_traad);
                            segBarriers[barr_indeks].await();
                            segBarriers[barr_indeks] = new CyclicBarrier(hoy_traad);
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
                    int ny_lav = nyIndeksering(arr_lav, pivot_indeks-1, traad_nr, hoy_traad);
                    int ny_hoy = nyIndeksering(arr_lav, pivot_indeks - 1, traad_nr + 1, hoy_traad) - 1;
                    try
                    {
                        
                        traad_funksjon(ny_lav, ny_hoy, traad_nr, arr_lav, pivot_indeks - 1, hoy_traad - 1, traad_id);
                    } catch (Exception e){}
                }
            }
            else
            {
                if (traad_nr == 0)
                {
                    if (pivot_indeks - 1 - arr_lav < sekvensiell_grense)
                    {
                        try
                        {
                            segBarriers[barr_indeks].await();
                        } catch (Exception e){}
                        sekvensiellKvikkSort(arr_lav, pivot_indeks - 1);
                        return;
                    }
                    if (traader_under_pivot == 1)
                    {
                        try
                        {
                            segBarriers[barr_indeks].await();
                            sekvensiellKvikkSort(arr_lav, pivot_indeks - 1);
                            return;
                        } catch (Exception e){}
                    }
                    else
                    {
                        segBarriers[barr_indeks + 1] = new CyclicBarrier(traader_under_pivot);
                        try
                        {
                            segBarriers[barr_indeks].await();
                            segBarriers[barr_indeks] = new CyclicBarrier(traader_under_pivot);
                        } catch (Exception e){}
                    }

                }
                else if (traad_nr == traader_under_pivot)
                {
                    if (arr_hoy - pivot_indeks < sekvensiell_grense)
                    {
                        try
                        {
                            segBarriers[barr_indeks].await();
                        } catch (Exception e){}
                        sekvensiellKvikkSort(pivot_indeks, arr_hoy);
                        return;
                    }
                    if (traader_under_pivot == hoy_traad)
                    {
                        try
                        {
                            segBarriers[barr_indeks].await();
                            sekvensiellKvikkSort(pivot_indeks, arr_hoy);
                            return;
                        } catch (Exception e){}
                    }
                    else
                    {
                        segBarriers[traad_id + 1] = new CyclicBarrier(hoy_traad - traader_under_pivot + 1);
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
                    if (pivot_indeks - 1 - arr_lav < sekvensiell_grense)
                    {
                        return;
                    }
                    try
                    {
                        segBarriers[barr_indeks + 1].await();
                        int ny_lav = nyIndeksering(arr_lav, pivot_indeks - 1, traad_nr, traader_under_pivot);
                        int ny_hoy = nyIndeksering(arr_lav, pivot_indeks - 1, traad_nr + 1, traader_under_pivot) - 1;
                        traad_funksjon(ny_lav, ny_hoy, traad_nr, arr_lav, pivot_indeks - 1, traader_under_pivot - 1, traad_id);
                    } catch (Exception e){}
                }
                else
                {
                    if (arr_hoy - pivot_indeks < sekvensiell_grense)
                    {
                        return;
                    }
                    try
                    {
                        segBarriers[barr_indeks + (int) traader_under_pivot + 1].await();
                        int ny_traadnr = traad_nr - traader_under_pivot;
                        int ny_lav = nyIndeksering(pivot_indeks, arr_hoy, ny_traadnr, hoy_traad - traader_under_pivot + 1);
                        int ny_hoy = nyIndeksering(pivot_indeks, arr_hoy, ny_traadnr+1, hoy_traad - traader_under_pivot + 1) - 1;
                        traad_funksjon(ny_lav, ny_hoy, ny_traadnr, pivot_indeks, arr_hoy, hoy_traad - traader_under_pivot, traad_id);
                    } catch (Exception e){}
                }
            }
        }
    }

    int nyIndeksering(long lav, long hoy, long traad_nr, long ant_traader)
    /* Finner ny inndeling,  lav eller hoy */
    {
        return (int) (lav + traad_nr * (long) (hoy - lav + 1) / ant_traader);
    }

    void finnDreiepunkt(int lav, int hoy, int dreietapp, int traad_id)
    {
        int dreiepunkt = lav;        
        for (int i = lav; i <= hoy; i++){
            
            if(arr[i] < dreietapp){
                int midlertidig = arr[i];
                arr[i] = arr[dreiepunkt];
                arr[dreiepunkt] = midlertidig;
                dreiepunkt += 1;
            }
        }

        if (dreiepunkt - lav > hoy){
            pivot[traad_id] = -1;
        }else{
            pivot[traad_id] = dreiepunkt - lav;
        }
    }

    int finnPivotIndeks(int arr_lav, int traad_id, int traad_nr, int hoy_traad)
    {
        int pivot_index = arr_lav;
        for (int i = traad_id - traad_nr; i <= traad_id - traad_nr + hoy_traad; i++){
            if (pivot[i] > -1){
                pivot_index += pivot[i];
            }
        }
        return pivot_index;
    }

    int finnStoreTilVenstre(int arr_lav, int arr_hoy, int hoy_traad, int barr_indeks, int pivot_indeks)
    {
        int store_til_venstre = 0;
        for (int i = 0; i <= hoy_traad; i++){
            int traad_lav = nyIndeksering(arr_lav, arr_hoy, i, hoy_traad + 1);
            if (traad_lav < pivot_indeks){
                int traad_hoy = nyIndeksering(arr_lav, arr_hoy, i + 1, hoy_traad + 1) - 1;
                if (traad_hoy < pivot_indeks){
                    store_til_venstre += traad_hoy - (traad_lav + pivot[barr_indeks + i] - 1);
                }else{
                    if (traad_lav + pivot[barr_indeks + i] < pivot_indeks){
                        store_til_venstre += pivot_indeks - (traad_lav + pivot[barr_indeks + i]);
                        break;
                    }
                    break;
                }
            }else{
                break;
            }
        }
        return store_til_venstre;
    }
    
    int [] finnSwapStartIndeks(int arr_lav, int arr_hoy, int hoy_traad, int barr_indeks, int start_nr)
    {
        int funnet = 0;
        int venstre_start_partisjon = 0;
        int venstre_partisjon_indeks = 0;
        for (int i = 0; i < (hoy_traad+1); i++){
            int start_indeks = nyIndeksering(arr_lav, arr_hoy, i, hoy_traad + 1);
            int slutt_indeks = nyIndeksering(arr_lav, arr_hoy, i + 1, hoy_traad + 1) - 1;
            int intervall = slutt_indeks - start_indeks;
            if (pivot[barr_indeks + i] == -1){
            }else if (funnet + (intervall - pivot[barr_indeks + i]) < start_nr){
                funnet += intervall - pivot[barr_indeks + i] + 1;
            }else{
                venstre_start_partisjon = i;
                venstre_partisjon_indeks = pivot[barr_indeks + i] + start_nr - funnet;
                break;
            }
        }

        funnet = 0;
        int hoyre_start_partisjon = 0;
        int hoyre_partisjon_indeks = 0;

        for (int i = (hoy_traad + 1) - 1; i >= 0; i--){
            if (funnet + pivot[barr_indeks + i] <= start_nr){
                funnet += pivot[barr_indeks + i];
            }else if (funnet + pivot[barr_indeks + i] > start_nr){
                hoyre_start_partisjon = i;
                hoyre_partisjon_indeks = pivot[barr_indeks + i] - (start_nr - funnet) - 1;
                break;
            }
        }
        int [] start_indekser = new int [4];
        start_indekser[0] = venstre_start_partisjon;
        start_indekser[1] = venstre_partisjon_indeks;
        start_indekser[2] = hoyre_start_partisjon;
        start_indekser[3] = hoyre_partisjon_indeks;
        return start_indekser;
    }

    void swapStorreOgMindre(int arr_lav, int arr_hoy, int hoy_traad, int start_nr, int slutt_nr, int barr_indeks, int venstre_start_partisjon, int venstre_partisjon_indeks, int hoyre_start_partisjon, int hoyre_partisjon_indeks)
    {
        for (int i = start_nr; i <= slutt_nr; i++){
            int storre_tall = nyIndeksering(arr_lav, arr_hoy, venstre_start_partisjon, hoy_traad + 1) + venstre_partisjon_indeks;
            
            if (storre_tall == nyIndeksering(arr_lav, arr_hoy, venstre_start_partisjon + 1, hoy_traad + 1)){      
                for (int j = venstre_start_partisjon + 1; j < (hoy_traad+1); j++){
                    long start_indeks = (long) (j * (arr_hoy-arr_lav + 1))/(hoy_traad+1);
                    long slutt_indeks = (long) ((j+1) * (arr_hoy-arr_lav + 1))/(hoy_traad+1) - 1;
                    int intervall = (int) slutt_indeks - (int) start_indeks;
                    if (intervall >= pivot[barr_indeks + j]){
                        venstre_start_partisjon = j;
                        venstre_partisjon_indeks = pivot[barr_indeks + j];
                        storre_tall = nyIndeksering(arr_lav, arr_hoy, venstre_start_partisjon, hoy_traad + 1) + venstre_partisjon_indeks;
                        venstre_partisjon_indeks++;
                        break;
                    }
                }
            }else{
                venstre_partisjon_indeks++;
            }
            int midlertidig = arr[storre_tall];
            int mindre_tall = nyIndeksering(arr_lav, arr_hoy, hoyre_start_partisjon, hoy_traad + 1) + hoyre_partisjon_indeks;
            
            if (mindre_tall == arr_lav + (hoyre_start_partisjon * (arr_hoy-arr_lav + 1) / (hoy_traad+1)) - 1){
                for (int j = hoyre_start_partisjon - 1; j > 0; j--){
                    if (pivot[barr_indeks + j] == 0){
                        // hopp over
                    }
                    else{
                        hoyre_start_partisjon = j;
                        hoyre_partisjon_indeks = pivot[barr_indeks + j] - 1;
                        mindre_tall = nyIndeksering(arr_lav, arr_hoy, hoyre_start_partisjon, hoy_traad + 1) + hoyre_partisjon_indeks;
                        hoyre_partisjon_indeks--;
                        break;
                    }
                }
            }else{
                hoyre_partisjon_indeks--;
            }

            arr[storre_tall] = arr[mindre_tall];
            arr[mindre_tall] = midlertidig;
        }
    }

    int[] sekvensiellKvikkSort(int lav, int hoy)
    {
        if(hoy - lav > 31){
            int dreiepunkt = partisjon(lav, hoy);
            if (dreiepunkt > hoy){
                dreiepunkt = partisjon(lav, hoy-1);
                sekvensiellKvikkSort(lav, hoy-1);
            }else{
                sekvensiellKvikkSort(lav, dreiepunkt - 1);
                sekvensiellKvikkSort(dreiepunkt, hoy);
            }
            
        }
        else if(hoy > lav){
            innstikkSortering(arr, lav, hoy);
        }
        return arr;
    }

    int partisjon(int lav, int hoy)
    {
        int m = (lav + hoy) / 2;
        int valg[] = {arr[m-1], arr[m], arr[m+1]};
        innstikkSortering(valg, 0, 2);
        int dreietapp = valg[1];
        int dreiepunkt = lav;
        
        for (int i = lav; i <= hoy; i++){
            if(arr[i] <= dreietapp){
                int midlertidig = arr[i];
                arr[i] = arr[dreiepunkt];
                arr[dreiepunkt] = midlertidig;
                dreiepunkt += 1;
            }
        }
        if (dreiepunkt > hoy){
            for (int i = m-1; i <= m+1; i++){
                if (arr[i] == dreietapp){
                    int midlertidig = arr[i];
                    arr[i] = arr[hoy];
                    arr[hoy] = midlertidig;
                    break;
                }
            }
        }
        return dreiepunkt;
    }

    private void innstikkSortering(int[] tallArray, int lav, int hoy)
    {
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

    public static void main(String[] args) 
    {
        int storrelse = 1000000;
        int[] testArr = new LagListe().nyttArray(storrelse, 5814);
        int[] test2Arr = new LagListe().nyttArray(storrelse, 5814);
        Arrays.sort(test2Arr);
        ParaSortPlattformMedSekvensiell para = new ParaSortPlattformMedSekvensiell();
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
