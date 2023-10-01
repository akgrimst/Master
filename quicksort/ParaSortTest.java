import java.util.Arrays;

public class ParaSortTest {

    public static void main(String[] args) {
        // System.out.println("ParaSort plattform:");
        // for (int t = 2; t < 1000; t *= 2){
        //     System.out.println("Antall traader: " + t);
        //     for (int i = 100000000; i > 1000; i = i / 10){
        //         long timeTaken[] = new long[9];
        //         for (int j = 0; j < 9; j++){
        //             int[] testArr = new LagListe().nyttArray(i, j);
        //             long startTime = System.nanoTime();
        //             ParaSort s = new ParaSort();
        //             try{s.paraSort(t, testArr, 0, i-1);}
        //             catch(Exception e){}
        //             timeTaken[j] = System.nanoTime() - startTime;
        //             // System.out.println("Time taken: " + i + " " + timeTaken[j]);
        //             // if (j == 0){
        //             //     for (int p = 1; p < i; p++){
        //             //         if (testArr[p] < testArr[p-1]){
        //             //             System.out.println("Feil: " + p + " forrige: " + testArr[p-1] + " neste: " + testArr[p]);
        //             //         }
        //             //     }
        //             // }
        //             testArr = null;
        //         }
        //         Arrays.sort(timeTaken);
        //         System.out.printf("%.3f%n", timeTaken[4] / 1000000.0);        
        //     }
        // }
        // System.out.println("ParaSort virtuell:");
        // for (int t = 2; t < 1000; t *= 2){
        //     System.out.println("Antall traader: " + t);
        //     for (int i = 100000000; i > 1000; i = i / 10){
        //         long timeTaken[] = new long[9];
        //         for (int j = 0; j < 9; j++){
        //             int[] testArr = new LagListe().nyttArray(i, j);
                    
        //             long startTime = System.nanoTime();
        //             ParaSortVirtuell s = new ParaSortVirtuell();
        //             try{s.paraSort(t, testArr, 0, i-1);}
        //             catch(Exception e){}
        //             timeTaken[j] = System.nanoTime() - startTime;
        //             // System.out.println("Time taken: " + i + " " + timeTaken[j]);
        //             // if (j == 0){
        //             //     for (int p = 1; p < i; p++){
        //             //         if (testArr[p] < testArr[p-1]){
        //             //             System.out.println("Feil: " + p + " forrige: " + testArr[p-1] + " neste: " + testArr[p]);
        //             //         }
        //             //     }
        //             // }
        //             testArr = null;
                    
        //         }
        //         Arrays.sort(timeTaken);
        //         System.out.printf("%.3f%n", timeTaken[4] / 1000000.0);        
        //     }
        // }
        // System.out.println("Parallell Plattform med sekvensiell:");
        // for (int t = 2; t < 1000; t *= 2){
        //     System.out.println("Antall traader: " + t);
        //     for (int i = 100000000; i > 1000; i = i / 10){
        //         long timeTaken[] = new long[9];
        //         for (int j = 0; j < 9; j++){
        //             int[] testArr = new LagListe().nyttArray(i, j);
                    
        //             long startTime = System.nanoTime();
        //             ParaSortVirtuell s = new ParaSortVirtuell();
        //             try{s.paraSort(t, testArr, 0, i-1);}
        //             catch(Exception e){}
        //             timeTaken[j] = System.nanoTime() - startTime;
        //             // System.out.println("Time taken: " + i + " " + timeTaken[j]);
        //             // if (j == 0){
        //             //     for (int p = 1; p < i; p++){
        //             //         if (testArr[p] < testArr[p-1]){
        //             //             System.out.println("Feil: " + p + " forrige: " + testArr[p-1] + " neste: " + testArr[p]);
        //             //         }
        //             //     }
        //             // }
        //             testArr = null;
                    
        //         }
        //         Arrays.sort(timeTaken);
        //         System.out.printf("%.3f%n", timeTaken[4] / 1000000.0);        
        //     }
        // }
    } 
}
