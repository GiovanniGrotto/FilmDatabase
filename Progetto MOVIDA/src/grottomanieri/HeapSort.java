package grottomanieri;

public class HeapSort {
    public HeapSort(){}
    public void HeapSort(Record[] a,int n) {
        for (int i = n / 2 - 1; i >= 0; i--) { //imposta l'array inizialmente
            heapfy(a, n, i);
        }
        for (int i = n - 1; i >= 0; i--) {   //sposta i max nel fondo dell'array
            Record var = a[0];
            a[0] = a[i];
            a[i] = var;
            heapfy(a, i, 0);
        }
    }
    private void heapfy(Record[] a,int n,int i){
        int t=i;
        int l = t * 2+1;
        int r = t * 2 + 2;

        if(l<n && a[t].key.compareTo(a[l].key)<0){
            t=l;
        }
        if(r<n && a[t].key.compareTo(a[r].key)<0){
            t=r;
        }
        if(t!=i) {
            Record var = a[i];
            a[i] = a[t];
            a[t] = var;
            heapfy(a, n, t); //richiama heapfy nei sottoalberi
        }
    }
}
