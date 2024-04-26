import java.util.Arrays;

public class ParaSortTest {

    public static void main(String[] args) {
        int testStorrelse = 100000000;
        int traadGrense = 1000;

        // System.out.println("Sekvensiell:");
        // for (int i = testStorrelse; i > 1000; i = i / 10){
        //     long timeTaken[] = new long[9];
        //     for (int j = 0; j < 9; j++){
        //         int[] testArr = new LagListe().nyttArray(i, j);
        //         long startTime = System.nanoTime();
        //         QuickSortSekvensiell s = new QuickSortSekvensiell();
        //         try{s.quickSort(testArr, 0, i-1);}
        //         catch(Exception e){}
        //         timeTaken[j] = System.nanoTime() - startTime;
        //     }
        //     Arrays.sort(timeTaken);
        //     System.out.printf("%.3f%n", timeTaken[4] / 1000000.0);        
        // }

        // System.out.println("Sekvensiell med innstikksortering:");
        // for (int i = testStorrelse; i > 1000; i = i / 10){
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

        // // System.out.println("Parallell plattform:");
        // // for (int i = testStorrelse; i >= 1000; i = i / 10){
        // //     long timeTaken[] = new long[9];
        // //     for (int j = 0; j < 9; j++){
        // //         int[] testArr = new LagListe().nyttArray(i, 0);
        // //         long startTime = System.nanoTime();
        // //         QuicksortParallellPlattform s = new QuicksortParallellPlattform();
        // //         try{s.quickSort(testArr, 0, i-1);}
        // //         catch(Exception e){}
        // //         timeTaken[j] = System.nanoTime() - startTime;
        // //     }
        // //     Arrays.sort(timeTaken);
        // //     System.out.printf("%.3f%n", timeTaken[4] / 1000000.0);        
        // // }

        // // System.out.println("Parallell virtuell:");
        // // for (int i = testStorrelse; i >= 1000; i = i / 10){
        // //     long timeTaken[] = new long[9];
        // //     for (int j = 0; j < 9; j++){
        // //         int[] testArr = new LagListe().nyttArray(i, 0);
        // //         long startTime = System.nanoTime();
        // //         QuicksortParallellVirtuell s = new QuicksortParallellVirtuell();
        // //         try{s.quickSort(testArr, 0, i-1);}
        // //         catch(Exception e){}
        // //         timeTaken[j] = System.nanoTime() - startTime;
        // //     }
        // //     Arrays.sort(timeTaken);
        // //     System.out.printf("%.3f%n", timeTaken[4] / 1000000.0);        
        // // }

        // System.out.println("Paralell med lag plattform:");
        // for (int t = 2; t < 1000; t *= 2){
        //     System.out.println("Antall lag: " + t);
        //     for (int i = testStorrelse; i > 1000; i = i / 10){
        //         long timeTaken[] = new long[9];
        //         for (int j = 0; j < 9; j++){
        //             int[] testArr = new LagListe().nyttArray(i, j);
        //             long startTime = System.nanoTime();
        //             KvikksortLagPlattform s = new KvikksortLagPlattform();
        //             try{s.quickSort(testArr, 0, i-1, 0, t);}
        //             catch(Exception e){}
        //             timeTaken[j] = System.nanoTime() - startTime;
        //             testArr = null;
        //         }
        //         Arrays.sort(timeTaken);
        //         System.out.printf("%.3f%n", timeTaken[4] / 1000000.0);        
        //     }
        // }

        System.out.println("Parallell med lag virtuell:");
        for (int t = 2; t < 1000; t *= 2){
            System.out.println("Antall lag: " + t);
            for (int i = 10000000; i > 1000; i = i / 10){
                long timeTaken[] = new long[9];
                for (int j = 0; j < 9; j++){
                    int[] testArr = new LagListe().nyttArray(i, j);
                    long startTime = System.nanoTime();
                    KvikksortLagVirtuell s = new KvikksortLagVirtuell();
                    try{s.quickSort(testArr, 0, i-1, 0, t);}
                    catch(Exception e){}
                    timeTaken[j] = System.nanoTime() - startTime;
                    testArr = null;
                }
                Arrays.sort(timeTaken);
                System.out.printf("%.3f%n", timeTaken[4] / 1000000.0);        
            }
        }
        
        // System.out.println("ParaSort plattform:");
        // for (int t = 2; t < 1000; t *= 2){
        //     System.out.println("Antall traader: " + t);
        //     for (int i = testStorrelse; i > 1000; i = i / 10){
        //         long timeTaken[] = new long[9];
        //         for (int j = 0; j < 9; j++){
        //             int[] testArr = new LagListe().nyttArray(i, j);
        //             long startTime = System.nanoTime();
        //             ParaSortPlattform s = new ParaSortPlattform();
        //             try{s.paraSort(t, testArr, 0, i-1);}
        //             catch(Exception e){}
        //             timeTaken[j] = System.nanoTime() - startTime;
        //             testArr = null;
        //         }
        //         Arrays.sort(timeTaken);
        //         System.out.printf("%.3f%n", timeTaken[4] / 1000000.0);        
        //     }
        // }

        // System.out.println("ParaSort virtuell:");
        // for (int t = 2; t < 1000; t *= 2){
        //     System.out.println("Antall traader: " + t);
        //     for (int i = testStorrelse; i > 1000; i = i / 10){
        //         long timeTaken[] = new long[9];
        //         for (int j = 0; j < 9; j++){
        //             int[] testArr = new LagListe().nyttArray(i, j);
                    
        //             long startTime = System.nanoTime();
        //             ParaSortV2Virtuell s = new ParaSortV2Virtuell();
        //             try{s.paraSort(t, testArr, 0, i-1);}
        //             catch(Exception e){}
        //             timeTaken[j] = System.nanoTime() - startTime;
        //             testArr = null;
                    
        //         }
        //         Arrays.sort(timeTaken);
        //         System.out.printf("%.3f%n", timeTaken[4] / 1000000.0);        
        //     }
        // }

        System.out.println("ParaSort plattform:");
        for (int t = 2; t < traadGrense; t *= 2){
            System.out.println("Antall traader: " + t);
            for (int i = testStorrelse; i > 1000; i = i / 10){
                long timeTaken[] = new long[9];
                for (int j = 0; j < 9; j++){
                    int[] testArr = new LagListe().nyttArray(i, j);
                    long startTime = System.nanoTime();
                    ParaSortPlattformV3 s = new ParaSortPlattformV3();
                    try{s.paraSort(t, testArr, 0, i-1);}
                    catch(Exception e){}
                    timeTaken[j] = System.nanoTime() - startTime;
                    testArr = null;
                }
                Arrays.sort(timeTaken);
                System.out.printf("%.3f%n", timeTaken[4] / 1000000.0);        
            }
        }

        System.out.println("ParaSort virtuell:");
        for (int t = 2; t < traadGrense; t *= 2){
            System.out.println("Antall traader: " + t);
            for (int i = testStorrelse; i > 1000; i = i / 10){
                long timeTaken[] = new long[9];
                for (int j = 0; j < 9; j++){
                    int[] testArr = new LagListe().nyttArray(i, j);
                    
                    long startTime = System.nanoTime();
                    ParaSortVirtuellV3 s = new ParaSortVirtuellV3();
                    try{s.paraSort(t, testArr, 0, i-1);}
                    catch(Exception e){}
                    timeTaken[j] = System.nanoTime() - startTime;
                    testArr = null;
                    
                }
                Arrays.sort(timeTaken);
                System.out.printf("%.3f%n", timeTaken[4] / 1000000.0);        
            }
        }

    //     System.out.println("ParaSort plattform med sekvensiell:");
    //     for (int t = 2; t < traadGrense; t *= 2){
    //         System.out.println("Antall traader: " + t);
    //         for (int i = testStorrelse; i > 1000; i = i / 10){
    //             long timeTaken[] = new long[9];
    //             for (int j = 0; j < 9; j++){
    //                 int[] testArr = new LagListe().nyttArray(i, j);
                    
    //                 long startTime = System.nanoTime();
    //                 ParaSortPlattformMedSekvensiell s = new ParaSortPlattformMedSekvensiell();
    //                 try{s.paraSort(t, testArr, 0, i-1);}
    //                 catch(Exception e){}
    //                 timeTaken[j] = System.nanoTime() - startTime;
    //                 testArr = null;
                    
    //             }
    //             Arrays.sort(timeTaken);
    //             System.out.printf("%.3f%n", timeTaken[4] / 1000000.0);        
    //         }
    //     }

    //     System.out.println("ParaSort virtuell med sekvensiell:");
    //     for (int t = 2; t < traadGrense; t *= 2){
    //         System.out.println("Antall traader: " + t);
    //         for (int i = testStorrelse; i > 1000; i = i / 10){
    //             long timeTaken[] = new long[9];
    //             for (int j = 0; j < 9; j++){
    //                 int[] testArr = new LagListe().nyttArray(i, j);
                    
    //                 long startTime = System.nanoTime();
    //                 ParaSortVirtuellMedSekvensiell s = new ParaSortVirtuellMedSekvensiell();
    //                 try{s.paraSort(t, testArr, 0, i-1);}
    //                 catch(Exception e){}
    //                 timeTaken[j] = System.nanoTime() - startTime;
    //                 testArr = null;
                    
    //             }
    //             Arrays.sort(timeTaken);
    //             System.out.printf("%.3f%n", timeTaken[4] / 1000000.0);        
    //         }
    //     }

    } 
}
