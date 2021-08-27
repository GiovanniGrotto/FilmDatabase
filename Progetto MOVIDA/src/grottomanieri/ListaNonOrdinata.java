package grottomanieri;
import commons.*;


public class ListaNonOrdinata {
    protected Record head;
    private int numMovs;


    public ListaNonOrdinata(){
            this.numMovs=0;
            this.head=new Record(null,null,null,null);
            this.head.next=head;
            this.head.prev=head;
    }

    //Costo O(N) con N numero di Movie
    public Record search(Comparable k){
        if(head!=null) {
            Record tmp = head.next;
            while (tmp != head) {
                if (k.equals(tmp.key)) return tmp;
                tmp = tmp.next;
            }
        }
        return null;
    }

    //Costo O(N) con N numero di Movie
    public Record searchByString(String title){
        if(head!=null) {
            Record tmp = head.next;
            while (tmp != head) {
                if (tmp.m.getTitle().toLowerCase().equals(title.toLowerCase())) return tmp;
                tmp = tmp.next;
            }
        }
        return null;
    }

    //Costo O(1)
    public boolean insert(Movie movie, Comparable k) {
        Record tmp=head;
        Record ob=new Record(movie,k,tmp,tmp.prev);
        tmp.prev.next=ob;
        tmp.prev=ob;
        numMovs++;
        return true;
    }

    //Costo O(N) per la chiamata Search con N numero di Movie
    public boolean delete(Comparable k) {
        Record tmp=search(k);
        if(tmp==null)return false;
        tmp.prev.next=tmp.next;
        tmp.next.prev=tmp.prev;
        numMovs--;
        return true;
    }

    //Costo O(N) per la chiamata Search con N numero di Movie
    public boolean deleteByString(String title){
        Record tmp=searchByString(title);
        if(tmp==null)return false;
        tmp.prev.next=tmp.next;
        tmp.next.prev=tmp.prev;
        numMovs--;
        return true;
    }

    //Costo O(1)
    public boolean isEmpty() {
        return numMovs==0;
    }

    //Costo O(N)
    public void print(){
        Record tmp=head.next;
        while(tmp!=head){
            System.out.print(tmp.m);
            System.out.print('\n');
            tmp=tmp.next;
        }
    }

    //Costo O(1)
    public int size() {
        return numMovs;
    }

    //Costo O(N) con N numero di Movie
    public Movie[] toArray() {
        if(head!=null) {
            Movie[] movies = new Movie[numMovs];
            Record tmp = head.next;
            int i = 0;
            while (tmp != head) {
                movies[i] = tmp.m;
                tmp = tmp.next;
                i++;
            }
            return movies;
        }
        else return new Movie[0];
    }

    //Costo O(N) con N numero di Movie
    public Record[] toArrayR(String key){
        Movie[] movies=this.toArray();
        Record[] r=new Record[movies.length];
        switch (key.toLowerCase()){
            case ("title"):
                for(int i=0;i<movies.length;i++){
                    r[i]=new Record(movies[i],movies[i].getTitle(),null,null);
                }
                break;
            case("year"):
                for(int i=0;i<movies.length;i++){
                    r[i]=new Record(movies[i],movies[i].getYear(),null,null);
                }
                break;
            case("votes"):
                for(int i=0;i<movies.length;i++){
                    r[i]=new Record(movies[i],movies[i].getVotes(),null,null);
                }
                break;
        }
        return r;
    }

    //Costo O(1)
    public void clear(){
        head=null;
        numMovs=0;
    }
}
