package grottomanieri;
import commons.*;
import java.io.*;
import java.util.ArrayList;

public class MovidaCore implements IMovidaConfig, IMovidaDB, IMovidaSearch, IMovidaCollaborations{
    //FIELDS
    protected MapImplementation map;
    protected SortingAlgorithm algo;
    protected ListaNonOrdinata list;
    protected Albero23 tree;
    protected Grafo graph;

    public MovidaCore(){
        list=new ListaNonOrdinata();
        tree=new Albero23();
        graph=new Grafo();
        map=null;
        algo=null;
    }

    //IMOVIDACONFIG IMPLEMENTATION

    //Costo O(N) se si imposta la ListaNonOrdinata con N numero di Movie
    //Costo O(N*logN) se si imposta l'Albero23
    @Override
    public boolean setMap(MapImplementation m) {
        if(map==m) {
            System.out.print("Configurazione già selezionata \n");
            return false;
        }
        else if(m==MapImplementation.ListaNonOrdinata){
            Movie[] movies=tree.toArrayIt(tree.root);
            for(Movie i:movies){
                list.insert(i,i.getTitle());
            }
            map=m;
            System.out.print("Configurazione cambiata correttamente in "+m +"\n");
            return true;
        }
        else if(m==MapImplementation.Alberi23){
            Movie[] movie=list.toArray();
            for(Movie i:movie){
                tree.root=tree.insertion(MovidetoNodo(i));
            }
            map=m;
            System.out.print("Configurazione cambiata correttamente in "+m +"\n");
            return true;
        }
        System.out.print("Configurazione "+m+" non disponibile \n");
        return false;
    }

    //Costo O(1)
    @Override
    public boolean setSort(SortingAlgorithm a) {
        if(a==SortingAlgorithm.InsertionSort || a==SortingAlgorithm.HeapSort){
            if(algo==a) {
                System.out.print("Configurazione già selezionata\n");
                return false;
            }
            else {
                algo = a;
                System.out.print("Configurazione cambiata correttamente in "+a+"\n");
                return true;
            }
        }
        else {
            System.out.print("Configurazione "+a+" non disponibile\n");
            return false;
        }
    }

    //IMOVIDADB IMPLEMENTATION

    //Caso Lista: Costo O(N+|cast|) = O(N) perché il cast è dell'ordine O(1)
    //Caso Albero23: Costo O(N*logN)
    @Override
    public void loadFromFile(File f)throws MovidaFileException{
        try{
            if(!f.exists() || !f.canRead()) throw new MovidaFileException();
            if(f==null)throw new MovidaFileException();
            FileReader file=new FileReader(f);
            BufferedReader reader=new BufferedReader(file);
            salva(reader);
            System.out.print("Dati caricati correttamente\n");
        }catch (Exception e){
            System.out.print(e.getMessage());
        }
    }


    //Costo O(N+|cast|) = O(N) perché il cast è dell'ordine O(1)
    @Override
    public void saveToFile(File f) {
        try {
            FileWriter myWriter1 = new FileWriter(f);
            myWriter1.close();
            FileWriter myWriter = new FileWriter(f);
            if(map==MapImplementation.ListaNonOrdinata){
                Record q=list.head.next; //se si usa la lista
                while(q!=list.head) {
                    myWriter.write("Title: ");
                    myWriter.write(q.m.getTitle());
                    myWriter.write("\n");
                    myWriter.write("Year: ");
                    myWriter.write(q.m.getYear().toString());
                    myWriter.write("\n");
                    myWriter.write("Director: ");
                    myWriter.write(q.m.getDirector().toString());
                    myWriter.write("\n");
                    myWriter.write("Cast: ");
                    Person[] ar;
                    ar = q.m.getCast();
                    for (int i = 0; i < ar.length; i++) {
                        myWriter.write(ar[i].getName());
                        if (i < ar.length - 1) myWriter.write(", ");
                    }
                    myWriter.write("\n");
                    myWriter.write("Votes: ");
                    myWriter.write(q.m.getVotes().toString());
                    myWriter.write("\n");
                    if (q.next != list.head) myWriter.write("\n");
                    q = q.next;
                }
            }else{
                Movie[] m=tree.toArrayIt(tree.root);
                for(int i=0;i<m.length;i++){
                    myWriter.write("Title: ");
                    myWriter.write(m[i].getTitle());
                    myWriter.write("\n");
                    myWriter.write("Year: ");
                    myWriter.write(m[i].getYear().toString());
                    myWriter.write("\n");
                    myWriter.write("Director: ");
                    myWriter.write(m[i].getDirector().toString());
                    myWriter.write("\n");
                    myWriter.write("Cast: ");
                    Person[] ar;
                    ar = m[i].getCast();
                    for (int j = 0; j < ar.length; j++) {
                        myWriter.write(ar[j].getName());
                        if (j < ar.length - 1) myWriter.write(", ");
                    }
                    myWriter.write("\n");
                    myWriter.write("Votes: ");
                    myWriter.write(m[i].getVotes().toString());
                    myWriter.write("\n");
                    myWriter.write("\n");
                }
            }

            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (Exception e) {
            //System.out.println("An error occurred.");
            System.out.print(e.getMessage());
        }
    }

    //Costo O(1)
    @Override
    public void clear() {
        list=new ListaNonOrdinata();
        tree=new Albero23();
        graph.grafo.clear();
    }

    //Costo O(1)
    @Override
    public int countMovies() {
        if(map==MapImplementation.Alberi23) return tree.getSize();
        else return list.size();
    }

    //Costo O(N*|people|)
    @Override
    public int countPeople() {
        return getAllPeople().length;
    }

    //Caso Albero23: Costo O(N)
    //Caso Lista: Costo O(N)
    @Override
    public boolean deleteMovieByTitle(String title) {
        if(map==MapImplementation.Alberi23) {
            Nodo tmp=tree.search(tree.root,title.toLowerCase());
            if(tmp!=null) graph.removeMovie(tmp.movie);
            return tree.deleteTitle(title);
        }
        else {
            Record tmp=list.search(title);
            if(tmp!=null) graph.removeMovie(tmp.m);
            return list.deleteByString(title);
        }
    }

    //Caso Albero23: Costo O(logN)
    //Caso Lista: Costo O(N)
    @Override
    public Movie getMovieByTitle(String title) {
        if(map==MapImplementation.Alberi23) {
            Nodo tmp=tree.search(tree.root,title.toLowerCase());
            if(tmp!=null) return tmp.movie;
        }
        else {
            Record tmp=list.searchByString(title);
            if(tmp!=null) return tmp.m;
        }
        return null;
    }

    //Costo O(N*|people|)
    @Override
    public Person getPersonByName(String name) {
        Person[] people=getAllPeople();
        for(int i=0;i<people.length; i++){
            if(people[i].getName().toLowerCase().equals(name.toLowerCase()))
                return people[i];
        }
        return null;
    }

    //Costo O(N)
    @Override
    public Movie[] getAllMovies() {
        if(map==MapImplementation.Alberi23) return tree.toArrayIt(tree.root);
        else return list.toArray();
    }

    //Costo O(N*|people|)
    @Override
    public Person[] getAllPeople() {
        ArrayList<Person> a=new ArrayList<>();
        Movie[] m;
        if(map==MapImplementation.ListaNonOrdinata){
            m=list.toArray();
        } else{
            m=tree.toArrayIt(tree.root);
        }
        boolean add;
        for(int i=0; i<m.length; i++){
            add=true;
            for(int j=0;j<a.size();j++){
                if(a.get(j).getName().equals(m[i].getDirector().getName())){
                    add=false;
                }
            }
            if(add){
                a.add(m[i].getDirector());
            }
            for(int k=0;k<m[i].getCast().length;k++){
                add=true;
                for(int j=0;j<a.size();j++){
                    if(m[i].getCast()[k].getName().equals(a.get(j).getName())){
                        add=false;
                    }
                }
                if(add)a.add(m[i].getCast()[k]);
            }
        }
        Person[] people=new Person[a.size()];
        for(int i=0;i<people.length;i++){
            people[i]=a.get(i);
        }
        return people;
    }

    //IMOVIDASEARCH IMPLEMENTATION

    //Caso Insertion Sort: Costo O(N**2)
    //Caso Heap Sort: Costo O(N*logN)
    @Override
    public Movie[] searchMostRecentMovies(Integer N) {
        Record[] q;
        if(map==MapImplementation.ListaNonOrdinata){
            q = list.toArrayR("year");
        }else{
            q=tree.toArrayR("year");
        }
        if(algo==SortingAlgorithm.InsertionSort){
            InsertionSort sort=new InsertionSort();
            sort.insertionSort(q);
        }else{
            HeapSort sort=new HeapSort();
            sort.HeapSort(q,q.length);
        }
        if(N>q.length)N=q.length;
        Movie[] m = new Movie[N];
        for (int i = 0; i < N; i++) {
            m[i] = q[q.length - i - 1].m;
        }
        return m;
    }

    //Caso Insertion Sort: Costo O(N**2)
    //Caso Heap Sort: Costo O(N*logN)
    @Override
    public Movie[] searchMostVotedMovies(Integer N) {
        Record[] q;
        if(map==MapImplementation.ListaNonOrdinata){
            q = list.toArrayR("votes");
        }else{
            q =tree.toArrayR("votes");
        }
        if(algo==SortingAlgorithm.InsertionSort){
            InsertionSort sort=new InsertionSort();
            sort.insertionSort(q);
        }else{
            HeapSort sort=new HeapSort();
            sort.HeapSort(q,q.length);
        }
        if(N>q.length)N=q.length;
        Movie[] m = new Movie[N];
        for (int i = 0; i < N; i++) {
            m[i] = q[q.length - i - 1].m;
        }
        return m;
    }

    //Caso Lista: Costo O(N)
    //Caso Albero23: Costo O(N)
    @Override
    public Movie[] searchMoviesByTitle(String title) {
        ListaNonOrdinata l = new ListaNonOrdinata();
        if(map==MapImplementation.ListaNonOrdinata) {
            if(list.head!=null) {
                Record tmp = list.head.next;
                while (tmp != list.head) {
                    if (tmp.m.getTitle().toLowerCase().contains(title.toLowerCase())) {
                        l.insert(tmp.m, tmp.m.getTitle());
                    }
                    tmp = tmp.next;
                }
            }
        }else {
            Movie[] nodi=tree.toArrayIt(tree.root);
            for(int i=0;i<nodi.length;i++){
                if(nodi[i].getTitle().toLowerCase().contains(title.toLowerCase())){
                    l.insert(nodi[i],nodi[i].getTitle());
                }
            }
        }
        return l.toArray();
    }

    //Caso Lista: Costo O(N)
    //Caso Albero23: Costo O(N)
    @Override
    public Movie[] searchMoviesDirectedBy(String name) {
        ListaNonOrdinata l = new ListaNonOrdinata();
        if(map==MapImplementation.ListaNonOrdinata) {
            if(list.head!=null) {
                Record tmp = list.head.next;
                while (tmp != list.head) {
                    if (tmp.m.getDirector().getName().toLowerCase().compareTo(name.toLowerCase()) == 0)
                        l.insert(tmp.m, tmp.m.getTitle());
                    tmp = tmp.next;
                }
            }
            return l.toArray();
        }else{
            Movie[] nodi=tree.toArrayIt(tree.root);
            for(int i=0;i<nodi.length;i++){
                if(nodi[i].getDirector().getName().toLowerCase().compareTo(name.toLowerCase())==0){
                    l.insert(nodi[i],nodi[i].getTitle());
                }
            }
        }
        return l.toArray();
    }

    //Caso Lista: Costo O(N)
    //Caso Albero23: Costo O(N)
    @Override
    public Movie[] searchMoviesInYear(Integer year) {
        ListaNonOrdinata l = new ListaNonOrdinata();
        if(map==MapImplementation.ListaNonOrdinata) {
            if(list.head!=null) {
                Record tmp = list.head.next;
                while (tmp != list.head) {
                    if (tmp.m.getYear().compareTo(year) == 0) l.insert(tmp.m, tmp.m.getTitle());
                    tmp = tmp.next;
                }
            }
        }else{
            Movie[] nodi=tree.toArrayIt(tree.root);
            for(int i=0;i<nodi.length;i++){
                if(nodi[i].getYear().compareTo(year)==0){
                    l.insert(nodi[i],nodi[i].getTitle());
                }
            }
        }
        return l.toArray();
    }

    //Caso Lista: Costo O(N) ( |Cast| è dell'ordine O(1) )
    //Caso Albero23: Costo O(N)
    @Override
    public Movie[] searchMoviesStarredBy(String name) {
        ListaNonOrdinata l=new ListaNonOrdinata();
        if(map==MapImplementation.ListaNonOrdinata) {
            Movie[] m = list.toArray();
            for (int i = 0; i < m.length; i++) {
                for (int j = 0; j < m[i].getCast().length; j++) {
                    if (m[i].getCast()[j].getName().toLowerCase().compareTo(name.toLowerCase()) == 0)
                        l.insert(m[i], m[i].getTitle());
                }
            }
        }else{
            Movie[] nodi=tree.toArrayIt(tree.root);
            for (int i = 0; i < nodi.length; i++) {
                for (int j = 0; j < nodi[i].getCast().length; j++) {
                    if (nodi[i].getCast()[j].getName().toLowerCase().compareTo(name.toLowerCase()) == 0)
                        l.insert(nodi[i],nodi[i].getTitle());
                }
            }
        }
        return l.toArray();
    }

    //Caso Lista: Costo O(N*|people|)
    //Caso Albero23: Costo O(N*|people|)
    @Override
    public Person[] searchMostActiveActors(Integer N) {
        Person[] people=getAllPeople();
        sortPeople(people);
        if(N>getAllPeople().length-countDirectors())N=getAllPeople().length-countDirectors();
        Person[] mostActive=new Person[N];
        for(int i=0; i<N; i++){
            mostActive[i]=people[i];
        }
        return mostActive;
    }

    public int getNumOfDirectors(){
        return countDirectors();
    }

    //IMOVIDA COLLABORATIONS IMPLEMENTATION

    //Costo O(m) con m=collaboratori diretti di actor
    @Override
    public Person[] getDirectCollaboratorsOf(Person actor) {
        return graph.getDirectCollaboratorsOf(actor);
    }

    //Costo O(n) con n=dimensione del team
    @Override
    public Person[] getTeamOf(Person actor) {
        return graph.getTeamOf(actor);
    }

    //Costo O(m*logn)
    @Override
    public Collaboration[] maximizeCollaborationsInTheTeamOf(Person actor) {
        return graph.maximizeCollaborationsInTheTeamOf(actor);
    }

    //FUNZIONI AUSILIARIE

    private int countDirectors(){
        Person[] people=getAllPeople();
        int count=0;
        for(Person i:people){
            if(searchMoviesStarredBy(i.getName()).length==0){
                count++;
            }
        }
        return count;
    }

    private int stringToInt(String c){
        String a=c.trim();
        int x=0;
        for(int i=0;i<a.length();i++){
            x=x+((a.charAt(i)-48)*(int)Math.pow(10,(a.length()-i-1)));
        }
        return x;
    }

    private Person[] stringToPerson(String[] j){
        Person[] p=new Person[j.length];
        for(int i=0; i<j.length; i++){
            p[i]=new Person(j[i]);
        }
        return p;
    }

    private Nodo MovidetoNodo(Movie m){
        return new Nodo(m,m.getTitle().toLowerCase(),null,null,null,null,null,null,true);
    }

    private void salva(BufferedReader r) throws MovidaFileException{
        try {
            Movie ex;
            String title,line;
            Person director;
            Person[] cast;
            int year,votes;
            do{
                r.skip(7);
                line=r.readLine();
                title=line.trim();
                r.skip(6);
                line=r.readLine();
                year=stringToInt(line.trim());
                r.skip(10);
                line=r.readLine();
                director=new Person(line.trim());
                r.skip(6);
                line=r.readLine();
                String tmp=line.trim();
                String[] temp=tmp.split(", ");
                cast=stringToPerson(temp);
                r.skip(7);
                line=r.readLine();
                votes=stringToInt(line.trim());
                Movie m=new Movie(title,year,votes,cast,director);
                ex=m;
                ex=m;
                if(map==MapImplementation.ListaNonOrdinata) {
                    list.insert(m, m.getTitle());
                    graph.addMovie(m);
                }else{
                    tree.root=tree.insertion(MovidetoNodo(ex));
                    graph.addMovie(m);
                }
                line=r.readLine();
            }while(line!=null);
        } catch (Exception e) {
            e.getMessage();
        }
    }

    private void sortPeople(Person A[]) {
        for (int k = 1; k <= A.length - 1; k++) {
            int j;
            Person x = A[k];
            for (j = 0; j < k; j++)
                if ((searchMoviesStarredBy(A[j].getName()).length) < searchMoviesStarredBy(x.getName()).length) break;
            if (j < k) {
                for (int t = k; t > j; t--) A[t] = A[t - 1];
                A[j] = x;
            }
        }
    }
}
