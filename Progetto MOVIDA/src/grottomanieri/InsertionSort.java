package grottomanieri;

public class InsertionSort {
    public InsertionSort(){}
    public void insertionSort(Record A[]) {
        for (int k = 1; k <= A.length - 1; k++) {
            int j;
            Record x = A[k];
            for (j = 0; j < k; j++)
                if (A[j].key.compareTo(x.key) > 0) break;
            if (j < k) {
                for (int t = k; t > j; t--) A[t] = A[t - 1];
                A[j] = x;
            }
        }
    }
}
