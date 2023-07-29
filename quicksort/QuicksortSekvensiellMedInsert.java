class QuickSortSekvensiellMedInsert{

    int[] quickSort(int[] tallArray, int lav, int hoy){
        if(hoy - lav > 10){
            int dreiepunkt = partisjon(tallArray, lav, hoy);
            quickSort(tallArray, lav, dreiepunkt - 1);
            quickSort(tallArray, dreiepunkt + 1, hoy);
        }
        else{
            innstikkSortering(tallArray, lav, hoy);
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
        int[] arr = {1, 5, 7, 2, 0, 6, 7, 99, -975};
        QuickSortSekvensiell qss = new QuickSortSekvensiell();
       int [] sortertArr = qss.quickSort(arr, 0, 8);
        for (int i = 0; i < 8; i++){
            System.out.println(sortertArr[i]);
        }
    }
}