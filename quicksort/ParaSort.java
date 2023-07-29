import java.util.concurrent.CyclicBarrier;
import java.util.Arrays;
import java.util.Random;

public class ParaSort {
    CyclicBarrier[] segBarriers;
    CyclicBarrier cb;
    int[] pivot;
    int venstre, hoyre;
    
    int[] paraSort(int k, int[] arr, int venstre, int hoyre) throws InterruptedException{
        int lengde = arr.length;
        this.venstre = venstre;
        this.hoyre = hoyre;
        pivot = new int[k];
        segBarriers = new CyclicBarrier[k];
        if (lengde < 32){
            innstikkSortering(arr, 0, lengde-1);
            return arr;
        }
        if (lengde < 33){
            QuickSortSekvensiell sekvensiell = new QuickSortSekvensiell();
            return sekvensiell.quickSort(arr, 0, lengde-1);
        }
        cb = new CyclicBarrier(k+1, null);
        segBarriers[0] = new CyclicBarrier(k+1, null);
        for (int i = 0; i < k; i++){
            int traad_lav = (int) i * (hoyre-venstre + 1) / k;
            int traad_hoy = (int) (i+1) * (hoyre-venstre + 1) / k - 1;

            ParaTraad traad = new ParaTraad(arr, traad_lav, traad_hoy, i, venstre, hoyre, 0, k-1);
            Thread t = Thread.ofPlatform().start(traad);

            System.out.println("Lav: " + i + " = " + traad_lav);
            System.out.println("Hoy: " + i + " = " + traad_hoy);
        }
        try{
            segBarriers[0].await();
        }catch(Exception e){}
        System.out.println("Ferdig steg 1");

        try{
            segBarriers[0].await();
        }catch(Exception e){}
        System.out.println("Ferdig steg 2");
            

        for (int i = 0; i < k; i++){
            System.out.println("pivots: " + i + " " + pivot[i]);
        }

        try{
            segBarriers[0].await();
        }catch(Exception e){}
        System.out.println("Ferdig steg 3");

        for (int i = 0; i < 50; i++){
            System.out.println(i + " " + arr[i]);
        }
        
        return arr;
    }

    class ParaTraad extends Thread{

        int[] arr;
        int lav, hoy, traad_nr, arr_lav, arr_hoy, lav_traad, hoy_traad;

        ParaTraad(int[] arr, int lav, int hoy, int traad_nr, int arr_lav, int arr_hoy, int lav_traad, int hoy_traad) throws InterruptedException{
            this.arr = arr;
            this.lav = lav;
            this.hoy = hoy;
            this.traad_nr = traad_nr;
            this.arr_lav = arr_lav;
            this.arr_hoy = arr_hoy;
            this.lav_traad = lav_traad;
            this.hoy_traad = hoy_traad;
        }

        public void run(){
            try{
                traad_funksjon(arr, lav, hoy, traad_nr, arr_lav, arr_hoy, lav_traad, hoy_traad);
            }catch(Exception e){}
        }
    }

    void traad_funksjon(int[] arr, int lav, int hoy, int traad_nr, int arr_lav, int arr_hoy, int lav_traad, int hoy_traad) throws InterruptedException{
        int m = (arr_hoy-arr_lav + 1)/2;
        System.out.println("m:" + m);
            int[] valg = {arr[m-1], arr[m], arr[m+1]};
            innstikkSortering(valg, 0, 2);
            int dreietapp = valg[1];
            System.out.println("Traad: " + lav + " - " + hoy + " Dreietapp: " + dreietapp);
            try{
                segBarriers[lav_traad].await();
            }catch(Exception e){}
            // Steg 2

            int dreiepunkt = lav;
            System.out.println("Traad: " + traad_nr + " dreiepunkt: " + dreiepunkt + " lav: " + lav + " hoy: " + hoy);
            
            for (int i = lav; i <= hoy; i++){
                if(arr[i] < dreietapp){
                    // if(traad_nr == 2){
                    //     System.out.println("finner: " + i + " " + arr[i] + " dreitapp: " + dreietapp);
                    //     tlllr++;
                    // }
                    // System.out.println("Traad: " + lav + " - " + hoy + " Dreietapp: " + dreietapp + " " + arr[i] + " " + i + " Dreiepunkt: " + dreiepunkt);
                    int midlertidig = arr[i];
                    // System.out.println("Traad: " + lav + " - " + hoy + " Dreietapp: " + dreietapp + " " + arr[i] + " " + i + " Dreiepunkt: " + dreiepunkt);
                    arr[i] = arr[dreiepunkt];
                    // System.out.println("Traad: " + lav + " - " + hoy + " Dreietapp: " + dreietapp + " " + arr[i] + " " + i + " Dreiepunkt: " + dreiepunkt);
                    arr[dreiepunkt] = midlertidig;
                    // System.out.println("Traad: " + lav + " - " + hoy + " Dreietapp: " + dreietapp + " " + arr[i] + " " + i + " Dreiepunkt: " + dreiepunkt);
                    dreiepunkt += 1;
                    // System.out.println("Traad: " + lav + " - " + hoy + " Dreietapp: " + dreietapp + " " + arr[i] + " " + i + " Dreiepunkt: " + dreiepunkt);
                }
            }
            // System.out.println("tlllr: " + tlllr);
            System.out.println("Traad: " + traad_nr + " dreiepunkt: " + dreiepunkt + " lav: " + lav + " hoy: " + hoy);
            if (dreiepunkt-lav > hoy){
                pivot[traad_nr] = -1;
            }else{
                pivot[traad_nr] = dreiepunkt-lav;
            }
            

            // System.out.println("Traad: " + lav + " - " + hoy + " Dreietapp: " + dreietapp);
            // int midlertidig = dreietapp;
            // System.out.println("Traad: " + lav + " - " + hoy + " Dreietapp: " + dreietapp + " Dreiepunkt: " + dreiepunkt + " Høy: " + hoy);
            // arr[hoy] = arr[dreiepunkt];
            // System.out.println("Traad: " + lav + " - " + hoy + " Dreietapp: " + dreietapp);

            // arr[dreiepunkt] = midlertidig;
            // // System.out.println("pivot: " + traad_nr + " " + dreiepunkt + " " + lav + " " + (dreiepunkt - lav));
            // pivot[traad_nr] = dreiepunkt-lav;

            try{
                segBarriers[lav_traad].await();
            }catch(Exception e){}
            // Steg 3

            int pivot_index = 0;
            for (int i = 0; i < pivot.length; i++){
                if (pivot[i] > -1){
                    pivot_index += pivot[i];
                }
            }
            // System.out.println("pivot_index " + pivot_index);

            int store_til_venstre = 0;
            // int traad_lav = (int) i * lengde / k;
            // int traad_hoy = (int) (i+1) * lengde / k - 1;

            for (int i = 0; i < pivot.length; i++){
                int traad_lav = (int) i * (arr_hoy-arr_lav + 1) / pivot.length;
                // System.out.println("Traad: " + traad_lav);
                if (traad_lav < pivot_index){
                    int traad_hoy = (int) (i+1) * (arr_hoy-arr_lav + 1) / pivot.length - 1;
                    if (traad_hoy < pivot_index){
                        store_til_venstre += traad_hoy - (traad_lav + pivot[i] - 1);
                        // System.out.println("Traad: " + traad_lav + " - " + traad_hoy + " Pivot: " + pivot[i] + " Større tall: " + (traad_hoy - (traad_lav + pivot[i] - 1)));
                    }else{
                        if (traad_lav + pivot[i] < pivot_index){
                            store_til_venstre += (traad_lav + pivot[i]) - pivot_index;
                            // System.out.println("Traad: " + traad_lav + " - " + traad_hoy + " Pivot: " + pivot[i] + " Større tall: " + ((traad_lav + pivot[i]) - pivot_index));
                            break;
                        }
                        break;
                    }
                }else{
                    break;
                }
            }
            // System.out.println("store_til_venstre: " + store_til_venstre);

            int start_nr = (traad_nr * store_til_venstre) / pivot.length;
            int slutt_nr = (((traad_nr + 1) * store_til_venstre) / pivot.length) - 1;
            int storre_funnet = 0;
            int venstre_start_partisjon = 0;
            int venstre_partisjon_indeks = 0;
            System.out.println("Tråd: " + traad_nr + " start: " + start_nr + " slutt: " + slutt_nr);
            for (int i = 0; i < pivot.length; i++){
                int start_indeks = (i * (arr_hoy-arr_lav + 1))/pivot.length;
                int slutt_indeks = ((i+1) * (arr_hoy-arr_lav + 1))/pivot.length - 1;
                int intervall = slutt_indeks - start_indeks;
                // System.out.println("Tråd: " + traad_nr + " ");
                if (pivot[i] == -1){
                    // System.out.println("Tråd: " + traad_nr + " pivot=-1 " + i);
                }else if (storre_funnet + (intervall - pivot[i]) < start_nr){
                    storre_funnet += intervall - pivot[i] + 1;
                    // System.out.println("Tråd: " + traad_nr + " i: " + i + " storre_funnet: " + storre_funnet + " pivot: " + pivot[i] + " intervall: " + intervall);
                }else{
                    venstre_start_partisjon = i;
                    venstre_partisjon_indeks = pivot[i] + start_nr - storre_funnet;
                    // System.out.println("Tråd: " + traad_nr + " i: " + i + " venstrepartisjonindeks: " + venstre_partisjon_indeks + " startnr: " + start_nr + " storrefunnet: " + storre_funnet + " pivot: " + pivot[i]);
                    // int forste_storre_tall_indeks = (venstre_start_partisjon * arr.length / pivot.length) + venstre_partisjon_indeks;
                    // System.out.println("Tråd: " + traad_nr + " venstre_start: " + (venstre_start_partisjon * arr.length / pivot.length) + " i: " + i);
                    // System.out.println("Tråd: " + traad_nr + " indeks: " + forste_storre_tall_indeks + " arr["+ forste_storre_tall_indeks +"]: " + arr[forste_storre_tall_indeks]);
                    break;
                }
            }
            int mindre_funnet = 0;
            int hoyre_start_partisjon = 0;
            int hoyre_partisjon_indeks = 0;

            for (int i = pivot.length - 1; i >= 0; i--){
                int start_indeks = (i * (arr_hoy-arr_lav + 1)) / pivot.length;
                int slutt_indeks = ((i+1) * (arr_hoy-arr_lav + 1)) / pivot.length - 1;
                int intervall = slutt_indeks - start_indeks;
                // System.out.println("Tråd: " + traad_nr + " start: " + start_nr + " slutt: " + slutt_nr + " startindeks: " + start_indeks + " sluttindeks: " + slutt_indeks);
                if (mindre_funnet + pivot[i] <= start_nr){
                    mindre_funnet += pivot[i];
                    // System.out.println("Tråd: " + traad_nr  + " mindre funnet: " + mindre_funnet);
                }else if (mindre_funnet + pivot[i] > start_nr){
                    hoyre_start_partisjon = i;
                    // System.out.println("Tråd: " + traad_nr + " start: " + start_nr + " mindre_funnet: " + mindre_funnet + " hoyrestartpartisjon: " + hoyre_start_partisjon);
                    hoyre_partisjon_indeks = pivot[i] - (start_nr - mindre_funnet) - 1;
                    // System.out.println("Tråd: " + traad_nr + " start: " + start_nr + " slutt: " + slutt_nr + " partisjon: " + i + " pivot[i]: " + pivot[i] + " intervall: " + intervall + " storre_funnet: " + mindre_funnet + " hoyre_partisjon_indeks: " + hoyre_partisjon_indeks);
                    // int mindre_tall = (hoyre_start_partisjon * arr.length / pivot.length) + hoyre_partisjon_indeks;
                    // System.out.println("Tråd: " + traad_nr + " mindre indeks: " + mindre_tall);
                    // System.out.println("Tråd: " + traad_nr + " mindre_tall: " + arr[mindre_tall]);
                    break;
                }
            }
                
            for (int i = 0; i <= slutt_nr - start_nr; i++){
                int storre_tall = (venstre_start_partisjon * (arr_hoy-arr_lav + 1) / pivot.length) + venstre_partisjon_indeks;
                if (storre_tall == ((venstre_start_partisjon + 1) * (arr_hoy-arr_lav + 1) / pivot.length)){
                    for (int j = venstre_start_partisjon + 1; j < pivot.length; j++){
                        int start_indeks = (j * (arr_hoy-arr_lav + 1))/pivot.length;
                        int slutt_indeks = ((j+1) * (arr_hoy-arr_lav + 1))/pivot.length;
                        int intervall = slutt_indeks - start_indeks;
                        if (intervall >= pivot[j]){
                            venstre_start_partisjon = j;
                            venstre_partisjon_indeks = pivot[j];
                            storre_tall = (venstre_start_partisjon * (arr_hoy-arr_lav + 1) / pivot.length) + venstre_partisjon_indeks;
                            break;
                        }
                    }
                }else{
                    venstre_partisjon_indeks++;
                }
                int midlertidig = arr[storre_tall];
                // System.out.println("Tråd: " + traad_nr + " større_tall: " + storre_tall + " " + arr[storre_tall]);

                int mindre_tall = (hoyre_start_partisjon * (arr_hoy-arr_lav + 1) / pivot.length) + hoyre_partisjon_indeks;
                if (mindre_tall == (hoyre_start_partisjon * (arr_hoy-arr_lav + 1) / pivot.length) - 1){
                    for (int j = hoyre_start_partisjon - 1; j > 0; j--){
                        if (pivot[j] > 0){
                            hoyre_start_partisjon = j;
                            hoyre_partisjon_indeks = pivot[j] - 1;
                            mindre_tall = (hoyre_start_partisjon * (arr_hoy-arr_lav + 1) / pivot.length) + hoyre_partisjon_indeks;
                            break;
                        }if(pivot[i] == 0){
                        }else{
                            
                        }
                    }
                }else{
                    hoyre_partisjon_indeks--;
                }
                arr[storre_tall] = arr[mindre_tall];
                arr[mindre_tall] = midlertidig;
                System.out.println("Tråd: " + traad_nr + " storre: " + storre_tall + " mindre: " + mindre_tall);
            }

            // for (int i = (traad_nr * store_til_venstre) / pivot.length; i < ((traad_nr + 1) * store_til_venstre) / pivot.length; i++){
            //     System.out.println("Traad: " + traad_nr + " " + i);
            // }

            try{
                segBarriers[lav_traad].await();
            }catch(Exception e){}
            if(hoy_traad-lav_traad == 1){
                if(traad_nr == lav_traad){
                    sekvensiellKvikkSort(arr, lav, dreiepunkt-1);
                }else{
                    sekvensiellKvikkSort(arr, dreiepunkt, hoy);
                }
            }else if(hoy_traad-lav_traad == 2){
                if(traad_nr == lav_traad){
                    sekvensiellKvikkSort(arr, lav, dreiepunkt-1);
                }
            }else{

            }
            // Steg 4
    }

    int[] sekvensiellKvikkSort(int[] tallArray, int lav, int hoy){
        if(hoy - lav > 32){
            int dreiepunkt = partisjon(tallArray, lav, hoy);
            sekvensiellKvikkSort(tallArray, lav, dreiepunkt - 1);
            sekvensiellKvikkSort(tallArray, dreiepunkt + 1, hoy);
        }
        else{
            innstikkSortering(tallArray, lav, hoy);
        }
        return tallArray;
    }

    int partisjon(int[] tallArray, int lav, int hoy){
        int dreietapp = tallArray[(lav + hoy) / 2];
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
        int[] testArr = new LagListe().nyttArray(50, 0);
        // testArr[25] = 99900000;
        // testArr[24] = 88888888;
        // testArr[26] = 98512344;
        int[] fifties = {36,6,45,46,1,8,18,14,44,34,41,30,26,38,9,16,32,5,29,47,43,7,39,19,23,15,20,42,25,24,40,12,35,50,22,48,3,31,33,4,27,49,2,11,10,13,21,28,17,37};
        // for (int i = 0; i < 32; i++){
        //     System.out.println(i + " " + testArr[i]);
        // }
        ParaSort para = new ParaSort();
        try{
            para.paraSort(8, testArr, 0, 49);
        }catch(InterruptedException e){}
        // for (int i = 0; i < 50; i++){
        //     System.out.println(testArr[i]);
        // }
        
        // Arrays.sort(testArr);
        // System.out.println("Nr. 27 " + testArr[27]);
        
    }
}
