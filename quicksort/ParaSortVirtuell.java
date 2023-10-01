import java.util.concurrent.CyclicBarrier;
import java.security.spec.ECFieldF2m;
import java.util.Arrays;
import java.util.Random;

public class ParaSortVirtuell {
    CyclicBarrier[] segBarriers;
    CyclicBarrier cb;
    int[] pivot;
    
    int[] paraSort(int k, int[] arr, int venstre, int hoyre) throws InterruptedException{
        pivot = new int[k];
        segBarriers = new CyclicBarrier[k];
        cb = new CyclicBarrier(k+1, null);
        segBarriers[0] = new CyclicBarrier(k, null);
        for (long i = 0; i < k; i++){
            long traad_lav = i * (hoyre-venstre + 1) / k;
            long traad_hoy = (i+1) * (hoyre-venstre + 1) / k - 1;
            ParaTraad traad = new ParaTraad(arr, traad_lav, traad_hoy, i, (long) venstre, (long) hoyre, 0, k-1, (int) i);
            Thread.ofVirtual().start(traad);
        }
        try{
            cb.await();
        }catch(Exception e){}
        // System.out.println("Ferdig");
        
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
            try{
                cb.await();
            }catch(Exception e){}
        }
    }

    void traad_funksjon(int[] arr, long lav, long hoy, long traad_nr, long arr_lav, long arr_hoy, long lav_traad, long hoy_traad, long traad_id) throws InterruptedException{
        
        // System.out.println("Tråd: " + traad_id + " lav: " + lav + " hoy: " + hoy);
        int barr_indeks = (int) traad_id - (int) traad_nr;
        int m = (int) arr_lav + ((int) arr_hoy- (int) arr_lav + 1)/2;
        int[] valg = {arr[m-1], arr[m], arr[m+1]};
        innstikkSortering(valg, 0, 2);
        // for(int i = 0; i < 3; i++){
        //     System.out.println("Tråd: " + traad_id + " Valg: " + i + " " + valg[i]);
        // }
        int dreietapp = valg[1];
        int valgverdi = arr[m-1];
        if (arr[m] > arr[m -1]){
            if (arr[m+1] > arr[m]){
                valgverdi = arr[m];
            }else{
                if (arr[m+1] > arr[m-1]){
                    valgverdi = arr[m+1];
                }
            }
        }else if(arr[m+1] < arr[m-1]){
            if (arr[m+1] < arr[m]){
                valgverdi = arr[m];
            }
        }
        if ( traad_id > 3){
        // System.out.println("Tråd: " + traad_id + " nr: " + traad_nr + " lav: " + lav + " hoy: " + hoy + " arrlav: " + arr_lav + " arrhoy: " + arr_hoy + " lavtraad: " + lav_traad + " hoytraad: " + hoy_traad + " barrindeks: " + barr_indeks);
        }
        // System.out.println("Tråd: " + traad_id + " ferdig steg 1");
        try{
            segBarriers[barr_indeks].await();
        }catch(Exception e){}
        // Steg 2

        long dreiepunkt = lav;
        // System.out.println("Tråd: " + traad_id + " startet steg 2");
        
        for (long i = lav; i <= hoy; i++){
            
            // if(traad_id == 26){    
            //     System.out.println("Tråd: " + traad_id + " i: " + i);
            // }
            if(arr[(int) i] < dreietapp){
                int midlertidig = arr[(int) i];
                arr[(int) i] = arr[(int) dreiepunkt];
                arr[(int) dreiepunkt] = midlertidig;
                dreiepunkt += 1;
            }
        }
        // System.out.println("Tråd: " + traad_id + " Dreiepunkt: " + dreiepunkt + " verdidrei: " + arr[dreiepunkt] + " -1: " + arr[dreiepunkt-1] + " verdipivot: " + valgverdi + " trådmin: " + arr[lav] + " trådmaks: " + arr[hoy]);
        if (dreiepunkt-lav > hoy){
            pivot[(int) traad_id] = -1;
        }else{
            pivot[(int) traad_id] = (int) (dreiepunkt-lav);
        }
        // System.out.println("Tråd: " + traad_id + " dreiepunkt: " + dreiepunkt + " lav: " + lav + " hoy: " + hoy + " pivot: " + pivot[traad_id]);

        // System.out.println("Tråd: " + traad_id + " ferdig steg 2");
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
        // System.out.println("Tråd: " + traad_id + " pivot_indeks: " + pivot_index);

        long store_til_venstre = 0;
        for (long i = lav_traad; i <= hoy_traad; i++){
            long traad_lav =  arr_lav + i * (arr_hoy-arr_lav + 1) / (hoy_traad + 1);
            if (traad_lav < pivot_index){
                long traad_hoy =  arr_lav + (i+1) * (arr_hoy-arr_lav + 1) / (hoy_traad + 1) - 1;
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
        // System.out.println("Tråd: " + traad_id + " startnr: " + start_nr + " sluttnr: " + slutt_nr);
        int storre_funnet = 0;
        int venstre_start_partisjon = 0;
        int venstre_partisjon_indeks = 0;
        for (int i = 0; i < (hoy_traad-lav_traad+1); i++){
            long start_indeks_long = (long) arr_lav + (i * (arr_hoy-arr_lav + 1))/(hoy_traad + 1);
            long slutt_indeks_long = (long) arr_lav + ((i+1) * (arr_hoy-arr_lav + 1))/(hoy_traad + 1) - 1;
            int start_indeks = (int) start_indeks_long;
            int slutt_indeks = (int) slutt_indeks_long;
            // System.out.println("Tråd: " + traad_id + " startindeks: " + start_indeks + " sluttindeks: " + slutt_indeks);
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
        // System.out.println("Tråd: " + traad_id + " venstrepartisjon: " + venstre_start_partisjon + " venstreindeks: " + venstre_partisjon_indeks + " hoyrepartisjon: " + hoyre_start_partisjon + " hoyreindeks: " + hoyre_partisjon_indeks);
        // System.out.println("Tråd: " + traad_id + " større: " + ((long) arr_lav + (venstre_start_partisjon * (arr_hoy-arr_lav + 1))/(hoy_traad + 1) + venstre_partisjon_indeks) + " mindre: " + ((long) arr_lav + (hoyre_start_partisjon * (arr_hoy-arr_lav + 1))/(hoy_traad + 1) + hoyre_partisjon_indeks));
            
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
            if ( i == start_nr){
            // System.out.println("Tråd: " + traad_id + " storretall: " + storre_tall + " mindretall: " + mindre_tall);
            }
            if (mindre_tall == (long) arr_lav + (hoyre_start_partisjon * (arr_hoy-arr_lav + 1) / (hoy_traad-lav_traad+1)) - 1){
                for (long j = hoyre_start_partisjon - 1; j > 0; j--){
                    // System.out.println("Tråd: " + traad_id + " pivot: " + pivot[barr_indeks +j]);
                    if (pivot[(int) (barr_indeks + j)] > 0){
                        hoyre_start_partisjon = j;
                        hoyre_partisjon_indeks = pivot[(int) (barr_indeks + j)] - 1;
                        mindre_tall = (long) arr_lav + (hoyre_start_partisjon * (arr_hoy-arr_lav + 1) / (hoy_traad-lav_traad+1)) + hoyre_partisjon_indeks;
                        // System.out.println("Tråd: " + traad_id + " partisjon: " + hoyre_start_partisjon);
                        hoyre_partisjon_indeks--;
                        break;
                    }else if(pivot[(int) (barr_indeks + j)] == 0){
                        break;
                    }else{
                        
                    }
                }
            }else{
                hoyre_partisjon_indeks--;
            }
            if(traad_id == 0){

            
            // System.out.println("Tråd: " + traad_id + " arr[storretall]: " + arr[(int) storre_tall] + " arr[mindretall]: " + arr[(int) mindre_tall] + " indeksverdi: " + valgverdi);
            }
            arr[(int) storre_tall] = arr[(int) mindre_tall];
            arr[(int) mindre_tall] = midlertidig;
        }
        // System.out.println("Tråd: " + traad_id + " ferdig steg 4");
        try{
            // Thread.sleep(100000);
            segBarriers[barr_indeks].await();
        }catch(Exception e){}
        if(hoy_traad < 3){
            try{
                segBarriers[barr_indeks].await();
            }catch(Exception e){}
            if(hoy_traad == 1){
                if(traad_nr == 0){
                    // for (int i = arr_lav; i < pivot_index; i++){
                    //     System.out.println("Tråd: " + traad_id + " i: " + i + " før sekvens: " + arr[i]);
                    // }
                    // System.out.println("Tråd: " + traad_id + " før sekvensiell lav: " + arr_lav + " hoy: " + (pivot_index-1));
                    // System.out.println("Tråd: " + traad_id + " Sekvensiell sortering lav: " + arr_lav + " hoy: " + (pivot_index-1));
                    sekvensiellKvikkSort(arr, (int) arr_lav, pivot_index-1);
                    // System.out.println("Tråd: " + traad_id + " etter sekvensiell lav: " + arr_lav + " hoy: " + (pivot_index-1));
                }else{
                    // for (int i = pivot_index; i <= arr_hoy; i++){
                    //     System.out.println("Tråd: " + traad_id + " i: " + i + " før sekvens: " + arr[i]);
                    // }
                    // System.out.println("Tråd: " + traad_id + " før sekvensiell lav: " + pivot_index + " hoy: " + arr_hoy);
                    // System.out.println("Tråd: " + traad_id + " Sekvensiell sortering lav: " + pivot_index + " hoy: " + arr_hoy);
                    sekvensiellKvikkSort(arr, pivot_index, (int) arr_hoy);
                    // System.out.println("Tråd: " + traad_id + " etter sekvensiell lav: " + pivot_index + " hoy: " + arr_hoy);
                }
            }else{
                // System.out.println("Kom hit");
                if (pivot_index < (arr_hoy + arr_lav) / 2){
                    // System.out.println("før pivot er minst");
                    if (traad_nr == 0){
                        try{
                            segBarriers[barr_indeks].await();
                        }catch(Exception e){}
                        // System.out.println("Tråd: " + traad_id + " Sekvensiell sortering lav: " + arr_lav + " hoy: " + (pivot_index-1));
                        sekvensiellKvikkSort(arr, (int) arr_lav, pivot_index-1);
                    }else{
                        if (traad_nr == 1){
                            segBarriers[(int) (traad_id)] = new CyclicBarrier(2, null);
                        }
                        try{
                            segBarriers[barr_indeks].await();
                        }catch(Exception e){}
                        // System.out.println("etter barrier");
                        long ny_lav = (long) pivot_index + (traad_nr-1) * (arr_hoy - pivot_index + 1) / 2;
                        long ny_hoy = (long) pivot_index + traad_nr * (arr_hoy - pivot_index + 1) / 2 - 1;
                        traad_funksjon(arr, (int) ny_lav, (int) ny_hoy, traad_nr-1, pivot_index, arr_hoy, 0, 1, traad_id);
                    }
                }else{
                    // System.out.println("etter pivot er minst");
                    if (traad_nr == 2){
                        try{
                            segBarriers[barr_indeks].await();
                        }catch(Exception e){}
                        // System.out.println("Tråd: " + traad_id + " Sekvensiell sortering lav: " + pivot_index + " hoy: " + arr_hoy);
                        sekvensiellKvikkSort(arr, pivot_index, (int) arr_hoy);
                    }else{
                        if (traad_nr == 0){
                            segBarriers[(int) traad_id+1] = new CyclicBarrier(2, null);
                            try{
                                segBarriers[barr_indeks].await();
                            }catch(Exception e){}
                            // System.out.println("Tråd: " + traad_id + " etter 1. seg");
                            segBarriers[(int) traad_id] = new CyclicBarrier(2, null);
                            try{
                                segBarriers[(int) traad_id+1].await();
                            }catch(Exception e){}
                            // System.out.println("Tråd: " + traad_id + " etter 2. seg");
                        }else{
                            try{
                                segBarriers[barr_indeks].await();
                            }catch(Exception e){}
                            // System.out.println("Tråd: " + traad_id + " etter 1. seg");
                            try{
                                segBarriers[(int) traad_id].await();
                            }catch(Exception e){}
                            // System.out.println("Tråd: " + traad_id + " etter 2. seg");
                        }
                        long ny_lav = (long) arr_lav + traad_nr * (pivot_index - arr_lav) / 2;
                        long ny_hoy = (long) arr_lav + (traad_nr+1) * (pivot_index - arr_lav) / 2 - 1;
                        traad_funksjon(arr, (int) ny_lav, (int) ny_hoy, traad_nr, arr_lav, pivot_index-1, 0, 1, traad_id);
                    }
                }
            }
        }else{
            long ant_traader = hoy_traad - lav_traad + 1;
            if (traad_nr == lav_traad){
                segBarriers[(int)traad_id+1] = new CyclicBarrier((int) ant_traader/2);
                try{
                    segBarriers[barr_indeks].await();
                }catch(Exception e){}
                segBarriers[(int) traad_id] = new CyclicBarrier((int) ant_traader/2);
            }else if (traad_nr == lav_traad + ant_traader/2){
                segBarriers[(int) traad_id+1] = new CyclicBarrier((int) hoy_traad-(int) traad_nr+1);
                try{
                    segBarriers[barr_indeks].await();
                }catch(Exception e){}
                segBarriers[(int) traad_id] = new CyclicBarrier((int) hoy_traad-(int) traad_nr+1);
            }else{
                try{
                    segBarriers[barr_indeks].await();
                }catch(Exception e){}
            }
            if (traad_nr < (lav_traad + (ant_traader/2))){
                try{
                    if (traad_nr == lav_traad){
                        for(int i = (int) arr_lav; i < pivot_index; i++){
                            // System.out.println("Tråd: " + traad_id + " i: " + i + " nr: " + arr[i]);
                            if (arr[i] >= dreietapp){
                                // System.out.println("Tråd: " + traad_id + " Større: " + arr[i] + " dreietapp: " + dreietapp);
                            }
                        }
                    }
                    segBarriers[barr_indeks + 1].await();
                }catch(Exception e){}
                long ny_lav = arr_lav + traad_nr * (pivot_index - arr_lav) / (ant_traader/2);
                long ny_hoy = arr_lav + (traad_nr+1) * (pivot_index - arr_lav) / (ant_traader/2) - 1;
                try{
                    // System.out.println("Tråd: " + traad_id + " bunn lav: " + arr_lav + " verdi: " + arr[arr_lav] + " hoy: " + (pivot_index-1) + " verdi: " + arr[pivot_index-1]);
                    traad_funksjon(arr, (int) ny_lav, (int) ny_hoy, traad_nr, arr_lav, pivot_index-1, 0, ant_traader / 2 - 1, traad_id);
                }catch(Exception e){}
            }else{
                try{
                    if (traad_nr == hoy_traad){
                        for(int i = pivot_index; i <= arr_hoy; i++){
                            // System.out.println("Tråd: " + traad_id + " i: " + i + " nr: " + arr[i]);
                            if (arr[i] < dreietapp){
                                // System.out.println("Tråd: " + traad_id + " Mindre: " + arr[i] + " dreietapp: " + dreietapp);
                            }
                        }
                    }
                    segBarriers[(int) barr_indeks + (int) ant_traader/2 + 1].await();
                }catch(Exception e){}
                long ny_traadnr = traad_nr - (lav_traad + ant_traader/2);
                long ny_lav = (long) pivot_index + ny_traadnr * (arr_hoy - pivot_index + 1) / (ant_traader - (ant_traader / 2));
                long ny_hoy = (long) pivot_index + (ny_traadnr + 1) * (arr_hoy - pivot_index + 1) / (ant_traader - (ant_traader / 2)) - 1;
                try{
                    // System.out.println("Tråd: " + traad_id + " topp lav: " + pivot_index + " verdi: " + arr[pivot_index] + " hoy: " + (arr_hoy) + " verdi: " + arr[arr_hoy]);
                    traad_funksjon(arr, ny_lav, ny_hoy, ny_traadnr, pivot_index, arr_hoy, 0, (ant_traader - (ant_traader / 2) - 1), traad_id);
                }catch(Exception e){}
            }
        }
    }

    int[] sekvensiellKvikkSort(int[] tallArray, int lav, int hoy){
        // System.out.println("Sekvensiell lav: " + lav + " hoy: " + hoy + " " + (hoy-lav));
        // try{
        //     Thread.sleep(1);
        // }catch(Exception e){}
        if(hoy - lav > 31){
            int dreiepunkt = partisjon(tallArray, lav, hoy);
            if (dreiepunkt > hoy){
                // while (dreiepunkt >= hoy){

                // }
                dreiepunkt = partisjon(tallArray, lav, hoy-1);
                sekvensiellKvikkSort(tallArray, lav, hoy-1);
            // }else if(dreiepunkt <= lav){
            //     try{
            //         System.out.println("Feil dreiepunkt: " + dreiepunkt);
            //         Thread.sleep(10000);
            //     }catch(Exception e){}
            }else{
                sekvensiellKvikkSort(tallArray, lav, dreiepunkt - 1);
            
                sekvensiellKvikkSort(tallArray, dreiepunkt, hoy);
            }
            
        }
        else if(hoy > lav){
            // System.out.println("innstikksortering lav: " + lav + " hoy: " + hoy);
            innstikkSortering(tallArray, lav, hoy);
        }
        return tallArray;
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
        int storrelse = 10000000;
        int[] testArr = new LagListe().nyttArray(storrelse, 5814);
        int[] test2Arr = new LagListe().nyttArray(storrelse, 5814);
        Arrays.sort(test2Arr);
        ParaSortVirtuell para = new ParaSortVirtuell();
        // para.sekvensiellKvikkSort(testArr, 0, storrelse-1);
        try{
            para.paraSort(27, testArr, 0, storrelse-1);
        }catch(InterruptedException e){}
        for (int i = 0; i < storrelse; i++){
            // System.out.println(i + " arr2 " + test2Arr[i]);
            // System.out.println(i + " arr1 " + testArr[i]);
            if (i > 0){
                if (testArr[i] < testArr[i-1]){
                    System.out.println("Break i" + i + " arr[i]: " + testArr[i] + " arr[i-1]: " + testArr[i-1]);
                    break;
                }
            }
            if (test2Arr[i] != testArr[i]){
                System.out.println("BREAK: " + i + " test2: " + test2Arr[i] + " test: " + testArr[i]);
                break;
            }
        }
    }
}
