import java.util.concurrent.CyclicBarrier;
import java.security.spec.ECFieldF2m;
import java.util.Arrays;
import java.util.Random;

public class ParaSortV2 {
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
            Thread.ofPlatform().start(traad);
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
            segBarriers[(int) traad_id] = null;
            try{
                cb.await();
            }catch(Exception e){}
        }
    }

    void traad_funksjon(int[] arr, long lav, long hoy, long traad_nr, long arr_lav, long arr_hoy, long lav_traad, long hoy_traad, long traad_id) throws InterruptedException{
        
        // System.out.println("Tråd: " + traad_id + " lav: " + arr_lav + " hoy: " + arr_hoy);
        int barr_indeks = (int) traad_id - (int) traad_nr;
        int m = (int) arr_lav + ((int) arr_hoy- (int) arr_lav + 1)/2;
        int[] valg = {arr[m-1], arr[m], arr[m+1]};
        innstikkSortering(valg, 0, 2);
        int dreietapp = valg[1];
        
        try{
            segBarriers[barr_indeks].await();
        }catch(Exception e){}
        // Steg 2
        // System.out.println("Tråd: " + traad_id + " barrindeks: " + barr_indeks + " Ferdig steg 1");

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
        // System.out.println("Tråd: " + traad_id + " barrindeks: " + barr_indeks + " Ferdig steg 2");

        int pivot_index = (int) arr_lav;
        for (long i = traad_id - (int) traad_nr; i <= traad_id - traad_nr + hoy_traad; i++){
            if (pivot[(int) i] > -1){
                pivot_index += pivot[(int) i];
            }
        }

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
        // System.out.println("Tråd: " + traad_id + " barrindeks: " + barr_indeks + " pivotindeks: " + pivot_index + " Ferdig steg 3");


        long start_nr_long = (long)(traad_nr * store_til_venstre) / (hoy_traad + 1);
        long slutt_nr_long = (long)(((traad_nr + 1) * store_til_venstre) / (hoy_traad + 1)) - 1;
        int start_nr = (int) start_nr_long;
        int slutt_nr = (int) slutt_nr_long;
        int storre_funnet = 0;
        int venstre_start_partisjon = 0;
        int venstre_partisjon_indeks = 0;
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
            if ( i == start_nr){
            }
            if (mindre_tall == (long) arr_lav + (hoyre_start_partisjon * (arr_hoy-arr_lav + 1) / (hoy_traad-lav_traad+1)) - 1){
                for (long j = hoyre_start_partisjon - 1; j > 0; j--){
                    if (pivot[(int) (barr_indeks + j)] > 0){
                        hoyre_start_partisjon = j;
                        hoyre_partisjon_indeks = pivot[(int) (barr_indeks + j)] - 1;
                        mindre_tall = (long) arr_lav + (hoyre_start_partisjon * (arr_hoy-arr_lav + 1) / (hoy_traad-lav_traad+1)) + hoyre_partisjon_indeks;
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
            arr[(int) storre_tall] = arr[(int) mindre_tall];
            arr[(int) mindre_tall] = midlertidig;
        }
        try{
            segBarriers[barr_indeks].await();
        }catch(Exception e){}
        if(hoy_traad < 3){
            try{
                segBarriers[barr_indeks].await();
            }catch(Exception e){}
            if(hoy_traad == 1){
                System.out.println("Tråd: " + traad_id + " pivotindeks: " + pivot_index);
                if(traad_nr == 0){
                    System.out.println("Tråd: " + traad_id + " sekvensiell lav: " + arr_lav + " hoy: " + (pivot_index-1));
                    sekvensiellKvikkSort(arr, (int) arr_lav, pivot_index-1);
                }else{
                    System.out.println("Tråd: " + traad_id + " sekvensiell lav: " + pivot_index + " hoy: " + arr_hoy);
                    sekvensiellKvikkSort(arr, pivot_index, (int) arr_hoy);
                }
            }else{
                if (pivot_index < (arr_hoy + arr_lav) / 2){
                    System.out.println("Tråd: " + traad_id + " før pivot er minst pivotindeks: " + pivot_index);
                    if (traad_nr == 0){
                        System.out.println("Tråd: " + traad_id + " før try " + segBarriers[barr_indeks].getNumberWaiting() + " " + barr_indeks);
                        try{
                            segBarriers[barr_indeks].await();
                        }catch(Exception e){}
                        System.out.println("Tråd: " + traad_id + " sekvensiell lav: " + arr_lav + " hoy: " + (pivot_index-1));
                        sekvensiellKvikkSort(arr, (int) arr_lav, pivot_index-1);
                    }else{
                        if (traad_nr == 1){
                            segBarriers[(int) (traad_id)] = new CyclicBarrier(2);
                        }
                        try{
                            System.out.println("Tråd: " + traad_id + " i try " + segBarriers[barr_indeks].getNumberWaiting());
                            segBarriers[barr_indeks].await();
                        }catch(Exception e){}
                        long ny_lav = (long) pivot_index + (traad_nr-1) * (arr_hoy - pivot_index + 1) / 2;
                        long ny_hoy = (long) pivot_index + traad_nr * (arr_hoy - pivot_index + 1) / 2 - 1;
                        traad_funksjon(arr, (int) ny_lav, (int) ny_hoy, traad_nr-1, pivot_index, arr_hoy, 0, 1, traad_id);
                    }
                }else{
                    System.out.println("Tråd: " + traad_id + " etter pivot er minst pivotindeks: " + pivot_index);
                    if (traad_nr == 2){
                        try{
                            segBarriers[barr_indeks].await();
                        }catch(Exception e){}
                        System.out.println("Tråd: " + traad_id + " sekvensiell lav: " + pivot_index + " hoy: " + arr_hoy);
                        sekvensiellKvikkSort(arr, pivot_index, (int) arr_hoy);
                    }else{
                        if (traad_nr == 0){
                            segBarriers[(int) traad_id+1] = new CyclicBarrier(2);
                            try{
                                System.out.println("Tråd: " + traad_id + " i try " + segBarriers[barr_indeks].getNumberWaiting());
                                segBarriers[barr_indeks].await();
                            }catch(Exception e){}
                            System.out.println("Tråd: " + traad_id + " etter 1. seg");
                            segBarriers[(int) traad_id] = new CyclicBarrier(2);
                            try{
                                segBarriers[(int) traad_id+1].await();
                            }catch(Exception e){}
                            System.out.println("Tråd: " + traad_id + " etter 2. seg");
                        }else{
                            try{
                                segBarriers[barr_indeks].await();
                            }catch(Exception e){}
                            System.out.println("Tråd: " + traad_id + " etter 1. seg");
                            try{
                                segBarriers[(int) traad_id].await();
                            }catch(Exception e){}
                            System.out.println("Tråd: " + traad_id + " etter 2. seg");
                        }
                        long ny_lav = (long) arr_lav + traad_nr * (pivot_index - arr_lav) / 2;
                        long ny_hoy = (long) arr_lav + (traad_nr+1) * (pivot_index - arr_lav) / 2 - 1;
                        System.out.println("Tråd: " + traad_id + " fra 3 til 2 nylav: " + ny_lav + " nyhoy: " + ny_hoy);
                        traad_funksjon(arr, ny_lav, ny_hoy, traad_nr, arr_lav, pivot_index-1, 0, 1, traad_id);
                    }
                }
            }
        }else{
            long ant_traader = hoy_traad - lav_traad + 1;
            long traader_under_pivot = (pivot_index - arr_lav) / ((arr_hoy - arr_lav) / ant_traader);
            System.out.println("Tråd: " + traad_id + " tråder under pivot: " + traader_under_pivot + " tråder over pivot: " + (ant_traader - traader_under_pivot) + " anttråder: " + ant_traader + " pivot: " + pivot_index + " tråådlav: " + lav + " trådhøy: " + hoy + " arrlav: " + arr_lav + " arrhoy: " + arr_hoy + " trådnr: " + traad_nr);
            if (traader_under_pivot == 0){
                System.out.println("Tråd: " + traad_id + " tråder under er null");
                if (traad_nr == 0){
                    try{
                        segBarriers[barr_indeks].await();
                    }catch(Exception e){}
                    System.out.println("Tråd: " + traad_id + " sekvensiell ingen under lav: " + arr_lav + " hoy: " + (pivot_index-1));
                    sekvensiellKvikkSort(arr, (int) arr_lav, pivot_index-1);
                }else{
                    if(traad_nr == 1){
                        segBarriers[(int) traad_id + 1] = new CyclicBarrier((int) (ant_traader - 1));
                        try{
                            segBarriers[barr_indeks].await();
                        }catch(Exception e){}
                        segBarriers[(int) traad_id] = new CyclicBarrier((int) (ant_traader - 1));
                    }else{
                        try{
                            segBarriers[barr_indeks].await();
                        }catch(Exception e){}
                    }
                    try{
                        segBarriers[(int) barr_indeks + 2].await();
                    }catch(Exception e){}
                    long ny_traadnr = traad_nr - 1;
                    long ny_lav = (long) pivot_index + (long) ny_traadnr * ((long) arr_hoy - (long) pivot_index + 1) / ((long) ant_traader - 1);
                    long ny_hoy = (long) pivot_index + ((long) ny_traadnr + 1) * ((long) arr_hoy - (long) pivot_index + 1) / ((long) ant_traader - 1) - 1;
                    try{
                        System.out.println("Tråd: " + traad_id + " arrlav: " + pivot_index + " arrhoy: " + arr_hoy + " ny lav: " + ny_lav + " ny hoy: " + ny_hoy + " ny_traadnr: " + ny_traadnr + " pivotindeks: " + pivot_index + " ");
                        traad_funksjon(arr, ny_lav, ny_hoy, ny_traadnr, pivot_index, arr_hoy, 0, (ant_traader - 2), traad_id);
                    }catch(Exception e){}
                }
            }else if (traader_under_pivot == ant_traader){
                System.out.println("Tråd: " + traad_id + " ingen over pivot");
                if (traad_nr == hoy_traad){
                    try{
                        segBarriers[barr_indeks].await();
                    }catch(Exception e){}
                    System.out.println("Tråd: " + traad_id + " sekvensiell ingen over");
                    sekvensiellKvikkSort(arr, (int) pivot_index, (int) arr_hoy);
                }else{
                    if (traad_nr == lav_traad){
                        segBarriers[(int) traad_id + 1] = new CyclicBarrier((int) ant_traader - 1);
                        try{
                            segBarriers[barr_indeks].await();
                        }catch(Exception e){}
                        segBarriers[(int) traad_id] = new CyclicBarrier((int) ant_traader - 1);
                    }else{
                        try{
                            segBarriers[barr_indeks].await();
                        }catch(Exception e){}
                    }
                    try{
                        segBarriers[barr_indeks + 1].await();
                    }catch(Exception e){}
                    long ny_lav = (long) arr_lav + (long) traad_nr * ((long) pivot_index - (long) arr_lav) / (ant_traader - 1);
                    long ny_hoy = (long) arr_lav + ((long) traad_nr+1) * ((long) pivot_index - (long) arr_lav) / (ant_traader - 1) - 1;
                    try{
                        System.out.println("Tråd: " + traad_id + " arrlav: " + arr_lav + " arrhoy: " + (pivot_index-1) + " ny lav: " + ny_lav + " ny hoy: " + ny_hoy);
                        traad_funksjon(arr, (int) ny_lav, (int) ny_hoy, traad_nr, arr_lav, pivot_index-1, 0, ant_traader - 2, traad_id);
                    }catch(Exception e){}
                }
            }else{
                if (traad_nr == lav_traad){
                    // System.out.println("Tråd: " + traad_id + " tråder under pivot: " + (traader_under_pivot));
                    segBarriers[(int)traad_id+1] = new CyclicBarrier((int) traader_under_pivot);
                    try{
                        segBarriers[barr_indeks].await();
                    }catch(Exception e){}
                    segBarriers[(int) traad_id] = new CyclicBarrier((int) traader_under_pivot);
                }else if (traad_nr == traader_under_pivot){
                    // System.out.println("Tråd: " + traad_id + " tråder over pivot: " + (ant_traader - traader_under_pivot));
                    segBarriers[(int) traad_id+1] = new CyclicBarrier((int) (ant_traader - traader_under_pivot));
                    try{
                        segBarriers[barr_indeks].await();
                    }catch(Exception e){}
                    segBarriers[(int) traad_id] = new CyclicBarrier((int) (ant_traader - traader_under_pivot));
                }else{
                    try{
                        segBarriers[barr_indeks].await();
                    }catch(Exception e){}
                }
                // System.out.println("Tråd: " + traad_id + " trådnr: " + traad_nr + " tråderunderpivot: " + traader_under_pivot);
                if (traad_nr < traader_under_pivot){
                    try{
                        // System.out.println("Tråd: " + traad_id + " anttråder: " + ant_traader + " i try " + segBarriers[barr_indeks + 1].getNumberWaiting() + " " + segBarriers[barr_indeks + 1].getParties() + " " + (barr_indeks + 1));
                        segBarriers[barr_indeks + 1].await();
                    }catch(Exception e){}
                    // System.out.println("Tråd: " + traad_id + " anttråder: " + ant_traader);
                    if (traader_under_pivot == 1){
                        System.out.println("Tråd: " + traad_id + " sekvensiell 1 under lav: " + arr_lav + " hoy: " + (pivot_index-1));
                        sekvensiellKvikkSort(arr, (int) arr_lav, pivot_index-1);
                    }else{
                        long ny_lav = (long) arr_lav + (long) traad_nr * ((long) pivot_index - (long) arr_lav) / (long) traader_under_pivot;
                        long ny_hoy = (long) arr_lav + ((long) traad_nr+1) * ((long) pivot_index - (long) arr_lav) / (long) traader_under_pivot - 1;
                        try{
                            System.out.println("Tråd: " + traad_id + " arrlav: " + arr_lav + " arrhoy: " + (pivot_index-1) + " ny lav: " + ny_lav + " ny hoy: " + ny_hoy);
                            traad_funksjon(arr, (int) ny_lav, (int) ny_hoy, traad_nr, arr_lav, pivot_index-1, 0, traader_under_pivot - 1, traad_id);
                        }catch(Exception e){}
                    }  
                }else{
                    // System.out.println("Tråd: " + traad_id + " else: " + ant_traader);
                    try{
                        // System.out.println("Tråd: " + traad_id + " anttråder: " + ant_traader + " i try " + segBarriers[(int) barr_indeks + (int) traader_under_pivot + 1].getNumberWaiting() + " " + segBarriers[(int) barr_indeks + (int) traader_under_pivot + 1].getParties() + " " + ((int) barr_indeks + (int) traader_under_pivot + 1));
                        segBarriers[(int) barr_indeks + (int) traader_under_pivot + 1].await();
                    }catch(Exception e){}
                    // System.out.println("Tråd: " + traad_id + " anttråder: " + ant_traader);
                    if (traader_under_pivot == ant_traader - 1){
                        System.out.println("Tråd: " + traad_id + " sekvensiell 1 over lav: " + pivot_index + " hoy: " + arr_hoy);
                        sekvensiellKvikkSort(arr, (int) pivot_index, (int) arr_hoy);
                    }else{
                        long ny_traadnr = traad_nr - traader_under_pivot;
                        long ny_lav = (long) pivot_index + (long) ny_traadnr * ((long) arr_hoy - (long) pivot_index + 1) / ((long) ant_traader - (long) traader_under_pivot);
                        long ny_hoy = (long) pivot_index + ((long) ny_traadnr + 1) * ((long) arr_hoy - (long) pivot_index + 1) / ((long) ant_traader - (long) traader_under_pivot) - 1;
                        try{
                            System.out.println("Tråd: " + traad_id + " arrlav: " + pivot_index + " arrhoy: " + arr_hoy + " ny lav: " + ny_lav + " ny hoy: " + ny_hoy + " ny_traadnr: " + ny_traadnr + " pivotindeks: " + pivot_index + " hoytråd: " + (ant_traader - traader_under_pivot - 1));
                            traad_funksjon(arr, ny_lav, ny_hoy, ny_traadnr, pivot_index, arr_hoy, 0, (ant_traader - traader_under_pivot - 1), traad_id);
                        }catch(Exception e){}
                    }
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
        int storrelse = 1000;
        int[] testArr = new LagListe().nyttArray(storrelse, 5814);
        // int[] test2Arr = new LagListe().nyttArray(storrelse, 5814);
        // Arrays.sort(test2Arr);
        ParaSortV2 para = new ParaSortV2();
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
            // if (test2Arr[i] != testArr[i]){
            //     System.out.println("BREAK: " + i + " test2: " + test2Arr[i] + " test: " + testArr[i]);
            //     break;
            // }
        }
    }
}
