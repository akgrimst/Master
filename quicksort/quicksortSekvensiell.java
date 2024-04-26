public class QuickSortSekvensiell{

    public int[] quickSort(int[] tallArray, int lav, int hoy){
        if(lav < hoy){
            int dreiepunkt = partisjon(tallArray, lav, hoy);
            quickSort(tallArray, lav, dreiepunkt - 1);
            quickSort(tallArray, dreiepunkt + 1, hoy);
        }
        return tallArray;
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


    public static void main(String[] args) {
        int[] arr = {1, 5, 7, 2, 0, 6, 7, 99, -975};
        QuickSortSekvensiell qss = new QuickSortSekvensiell();
       int [] sortertArr = qss.quickSort(arr, 0, 8);
        for (int i = 0; i < 8; i++){
            System.out.println(sortertArr[i]);
        }
    }
}