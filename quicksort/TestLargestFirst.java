import java.util.Arrays;
public class TestLargestFirst {
    public static void main(String[] args) {
        int testStorrelse = 100000000;
        
        // System.out.println("Sekvensiell:");
        // for (int i = testStorrelse; i >= 1000; i = i / 10){
        //     long timeTaken[] = new long[9];
        //     for (int j = 0; j < 9; j++){
        //         int[] testArr = new LagListe().nyttArray(i, j);
        //         long startTime = System.nanoTime();
        //         QuickSortSekvensiell s = new QuickSortSekvensiell();
        //         s.quickSort(testArr, 0, i-1);
        //         timeTaken[j] = System.nanoTime() - startTime;
        //     }
        //     Arrays.sort(timeTaken);
        //     System.out.printf("%.3f%n", timeTaken[4] / 1000000.0);        
        // }

        // System.out.println("Sekvensiell med innstikksortering:");
        // for (int i = testStorrelse; i >= 1000; i = i / 10){
        //     long timeTaken[] = new long[9];
        //     for (int j = 0; j < 9; j++){
        //         int[] testArr = new LagListe().nyttArray(i, 0);
        //         long startTime = System.nanoTime();
        //         QuickSortSekvensiellMedInsert s = new QuickSortSekvensiellMedInsert();
        //         s.quickSort(testArr, 0, i-1);
        //         timeTaken[j] = System.nanoTime() - startTime;
        //     }
        //     Arrays.sort(timeTaken);
        //     System.out.printf("%.3f%n", timeTaken[4] / 1000000.0);        
        // }

        // System.out.println("Parallell plattform:");
        // for (int i = 10000; i >= 1000; i = i / 10){
        //     long timeTaken[] = new long[9];
        //     for (int j = 0; j < 9; j++){
        //         int[] testArr = new LagListe().nyttArray(i, 0);
        //         long startTime = System.nanoTime();
        //         QuicksortParallellPlattform s = new QuicksortParallellPlattform();
        //         try{s.quickSort(testArr, 0, i-1);}
        //         catch(Exception e){}
        //         timeTaken[j] = System.nanoTime() - startTime;
        //     }
        //     Arrays.sort(timeTaken);
        //     System.out.printf("%.3f%n", timeTaken[4] / 1000000.0);        
        // }

        // System.out.println("Parallell virtuell:");
        // for (int i = 10000000; i >= 1000; i = i / 10){
        //     long timeTaken[] = new long[9];
        //     for (int j = 0; j < 9; j++){
        //         int[] testArr = new LagListe().nyttArray(i, 0);
        //         long startTime = System.nanoTime();
        //         QuicksortParallellVirtuell s = new QuicksortParallellVirtuell();
        //         try{s.quickSort(testArr, 0, i-1);}
        //         catch(Exception e){}
        //         timeTaken[j] = System.nanoTime() - startTime;
        //         System.out.println("Time taken: " + i + " " + timeTaken[j]);
        //     }
        //     Arrays.sort(timeTaken);
        //     System.out.printf("%.3f%n", timeTaken[4] / 1000000.0);        
        // }

        System.out.println("Parallell lag plattform:");
        for (int i = testStorrelse; i > 1000; i = i / 10){
            long timeTaken[] = new long[9];
            for (int j = 0; j < 9; j++){
                int[] testArr = new LagListe().nyttArray(i, 0);
                long startTime = System.nanoTime();
                KvikksortLagPlattform s = new KvikksortLagPlattform();
                try{s.quickSort(testArr, 0, i-1, 0, 100000000);}
                catch(Exception e){}
                timeTaken[j] = System.nanoTime() - startTime;
                // System.out.println("Time taken: " + i + " " + timeTaken[j]);
            }
            Arrays.sort(timeTaken);
            System.out.printf("%.3f%n", timeTaken[4] / 1000000.0);        
        }

        System.out.println("Parallell lag virtuell:");
        for (int i = testStorrelse; i > 1000; i = i / 10){
            long timeTaken[] = new long[9];
            for (int j = 0; j < 9; j++){
                int[] testArr = new LagListe().nyttArray(i, 0);
                long startTime = System.nanoTime();
                KvikksortLagVirtuell s = new KvikksortLagVirtuell();
                try{s.quickSort(testArr, 0, i-1, 0, 100000000);}
                catch(Exception e){}
                timeTaken[j] = System.nanoTime() - startTime;
                // System.out.println("Time taken: " + i + " " + timeTaken[j]);
            }
            Arrays.sort(timeTaken);
            System.out.printf("%.3f%n", timeTaken[4] / 1000000.0);        
        }

        System.out.println("Parallell plattform med sekvensiell:");
        for (int i = testStorrelse; i > 1000; i = i / 10){
            long timeTaken[] = new long[9];
            for (int j = 0; j < 9; j++){
                int[] testArr = new LagListe().nyttArray(i, 0);
                long startTime = System.nanoTime();
                KvikksortParallellPlattformHalvpartenTraader s = new KvikksortParallellPlattformHalvpartenTraader();
                try{s.quickSort(testArr, 0, i-1);}
                catch(Exception e){}
                timeTaken[j] = System.nanoTime() - startTime;
            }
            Arrays.sort(timeTaken);
            System.out.printf("%.3f%n", timeTaken[4] / 1000000.0);        
        }

        System.out.println("Parallell virtuell med sekvensiell:");
        for (int i = testStorrelse; i > 1000; i = i / 10){
            long timeTaken[] = new long[9];
            for (int j = 0; j < 9; j++){
                int[] testArr = new LagListe().nyttArray(i, j);
                long startTime = System.nanoTime();
                QuicksortParallellMedSekvensiellVirtuell s = new QuicksortParallellMedSekvensiellVirtuell();
                try{
                    s.quickSort(testArr, 0, i-1);
                }
                catch(Exception e){}
                timeTaken[j] = System.nanoTime() - startTime;
                // System.out.println(timeTaken[j]);
            }
            Arrays.sort(timeTaken);
            System.out.printf("%.3f%n", timeTaken[4] / 1000000.0);        
        }

        // System.out.println("Parallell plattform med sekvensiell:");
        // for (int i = 10000000; i >= 1000; i = i / 10){
        //     long timeTaken[] = new long[9];
        //     for (int j = 0; j < 9; j++){
        //         int[] testArr = new LagListe().nyttArray(i, j);
        //         long startTime = System.nanoTime();
        //         KvikksortParallellPlattformMedSekvensiell s = new KvikksortParallellPlattformMedSekvensiell();
        //         try{s.quickSort(testArr, 0, i-1);}
        //         catch(Exception e){}
        //         timeTaken[j] = System.nanoTime() - startTime;
        //         // System.out.println(timeTaken[j]);
        //     }
        //     Arrays.sort(timeTaken);
        //     System.out.printf("%.3f%n", timeTaken[4] / 1000000.0);        
        // }
    }
}
