package grottomanieri;

import commons.Movie;

import java.util.LinkedList;

public class Albero23 {
    protected Nodo root;
    private int size;

    public Albero23(){
        root=null;
    }

    //Costo O(1)
    public int getSize(){
        return size;
    }

    //Costo O(1)
    public void clear(){
        root=null;
        size=0;
    }

    //Costo (logN) con N numero di Movie
    public Nodo search(Nodo root, Comparable k){
        if(root==null){
            return null;
        }
        else{
            if(root.isaLeaf){
                if(root.key.equals(k)){
                    return root;
                }
                else return null;
            }
            else if(k.compareTo(root.l)<=0){
                return search(root.left,k);
            }
            else if(root.right!=null && k.compareTo(root.r)>0){
                return search(root.right,k);
            }
            else return search(root.mid,k);
        }
    }

    //Costo O(logN) nel caso pessimo per effettuare logN split
    public Nodo insertion(Nodo n){
        if(root==null){
            size++;
            return new Nodo(n);
        }
        else if(root.isaLeaf){
            size++;
            if(n.key.compareTo(root.key)>0){
                Nodo tmp=new Nodo(null,null,root.key,null,null,root,n,null,false);
                root.parent=tmp;
                n.parent=tmp;
                return tmp;
            }
            else{
                Nodo tmp=new Nodo(null,null,n.key,null,null,n,root,null,false);
                root.parent=tmp;
                n.parent=tmp;
                return tmp;
            }
        }
        else{
            Nodo tmp=find(root,n);
            if(tmp.r==null){
                if(n.key.compareTo(tmp.l)<0){
                    tmp.right=tmp.mid;
                    tmp.mid=tmp.left;
                    tmp.left=n;
                    tmp.l=tmp.left.key;
                    tmp.r=tmp.mid.key;
                    n.parent=tmp;
                }
                else if(n.key.compareTo(tmp.mid.key)>0){
                    tmp.right=n;
                    tmp.r=tmp.mid.key;
                    n.parent=tmp;
                }
                else{
                    tmp.right=tmp.mid;
                    tmp.mid=n;
                    tmp.r=tmp.mid.key;
                    n.parent=tmp;
                }
            }
            else{
                splits(tmp,n);
            }
            size++;
            return root;
        }
    }

    //Costo O(logN) nel caso pessimo per risalire alla radice, con N numero di Movie
    private void splits(Nodo r, Nodo n){
        Nodo newRoot, newNodeLeft, newNodeRight;
        if(n.isaLeaf){
            if (n.key.compareTo(r.l) < 0) {
                newNodeLeft = new Nodo(null, null, n.key, null, null, n, r.left, null, false);
                newNodeRight = new Nodo(null, null, r.mid.key, null, null, r.mid, r.right, null, false);
                newNodeLeft.left.parent=newNodeLeft;
                newNodeLeft.mid.parent=newNodeLeft;
                newNodeRight.left.parent=newNodeRight;
                newNodeRight.mid.parent=newNodeRight;
            } else if (n.key.compareTo(r.l) > 0 && n.key.compareTo(r.r) < 0) {
                newNodeLeft = new Nodo(null, null, r.l, null, null, r.left, n, null, false);
                newNodeRight = new Nodo(null, null, r.mid.key, null, null, r.mid, r.right, null, false);
                newNodeLeft.left.parent=newNodeLeft;
                newNodeLeft.mid.parent=newNodeLeft;
                newNodeRight.left.parent=newNodeRight;
                newNodeRight.mid.parent=newNodeRight;
            } else if (n.key.compareTo(r.r) > 0 && n.key.compareTo(r.right.key) < 0) {
                newNodeLeft = new Nodo(null, null, r.l, null, null, r.left, r.mid, null, false);
                newNodeRight = new Nodo(null, null, n.key, null, null, n, r.right, null, false);
                newNodeLeft.left.parent=newNodeLeft;
                newNodeLeft.mid.parent=newNodeLeft;
                newNodeRight.left.parent=newNodeRight;
                newNodeRight.mid.parent=newNodeRight;
            } else {
                newNodeLeft = new Nodo(null, null, r.l, null, null, r.left, r.mid, null, false);
                newNodeRight = new Nodo(null, null, r.right.key, null, null, r.right, n, null, false);
                newNodeLeft.left.parent=newNodeLeft;
                newNodeLeft.mid.parent=newNodeLeft;
                newNodeRight.left.parent=newNodeRight;
                newNodeRight.mid.parent=newNodeRight;
            }
        }
        else{
            if(n.l.compareTo(r.l)<0){
                newNodeLeft = new Nodo(null, null, findMax(n.left), null, null, n.left, n.mid, null, false);
                newNodeRight = new Nodo(null, null, findMax(r.mid), null, null, r.mid, r.right, null, false);
                newNodeLeft.left.parent=newNodeLeft;
                newNodeLeft.mid.parent=newNodeLeft;
                newNodeRight.left.parent=newNodeRight;
                newNodeRight.mid.parent=newNodeRight;
            }
            else if(n.l.compareTo(r.l)>0 && n.l.compareTo(r.r)<0) {
                newNodeLeft = new Nodo(null, null, findMax(r.left), null, null, r.left, n.left, null, false);
                newNodeRight = new Nodo(null, null, findMax(n.mid), null, null, n.mid, r.right, null, false);
                newNodeLeft.left.parent=newNodeLeft;
                newNodeLeft.mid.parent=newNodeLeft;
                newNodeRight.left.parent=newNodeRight;
                newNodeRight.mid.parent=newNodeRight;
            }
            else{
                newNodeLeft = new Nodo(null, null, findMax(r.left), null, null, r.left, r.mid, null, false);
                newNodeRight = new Nodo(null, null, findMax(n.left), null, null, n.left, n.mid, null, false);
                newNodeLeft.left.parent=newNodeLeft;
                newNodeLeft.mid.parent=newNodeLeft;
                newNodeRight.left.parent=newNodeRight;
                newNodeRight.mid.parent=newNodeRight;
            }
        }
        if(r.parent==null){
            newRoot = new Nodo(null, null, findMax(newNodeLeft), null, null, newNodeLeft, newNodeRight, null, false);
            newNodeLeft.parent=newRoot;
            newNodeRight.parent=newRoot;
            this.root=newRoot;
        }
        else if(r.parent.r==null && r.l.compareTo(r.parent.l)<0){
            r.parent.right=r.parent.mid;
            r.parent.mid=newNodeRight;
            r.parent.left=newNodeLeft;
            r.parent.l=findMax(newNodeLeft);
            r.parent.r=findMax(r.parent.mid);
            newNodeRight.parent=r.parent;
            newNodeLeft.parent=r.parent;
        }
        else if(r.parent.r==null && r.l.compareTo(r.parent.l)>0){
            r.parent.right=newNodeRight;
            r.parent.mid=newNodeLeft;
            r.parent.r=findMax(r.parent.mid);
            newNodeRight.parent=r.parent;
            newNodeLeft.parent=r.parent;
        }
        else{
            newRoot = new Nodo(null, null, findMax(newNodeLeft), null, null, newNodeLeft, newNodeRight, null, false);
            newNodeLeft.parent=newRoot;
            newNodeRight.parent=newRoot;
            splits(r.parent, newRoot);
        }
    }

    //Costo O(N) con N numero nodi interni, O(N*logN) nel caso pessimo per eseguire una union
    public Nodo delete(Nodo n){
        if(root==null) return null;                                     //albero vuoto
        else if(root.isaLeaf && root.key==n.key) root=null;           //un solo elmento
        else if(root.parent==null && root.r==null && root.left.isaLeaf){//solo due elementi
            if(root.left.key==n.key) root=root.mid;
            else if(root.mid.key==n.key) root=root.left;
        }else{                                                          //tre o più
            Nodo tmp = search(root,n.key).parent;
            if (tmp.r != null) {                                        //ha 3 nodi
                if (n.key.compareTo(tmp.left.key) == 0) {
                    tmp.left = tmp.mid;
                    tmp.mid = tmp.right;
                    tmp.right=null;
                } else if (n.key.compareTo(tmp.right.key) == 0) {
                    tmp.right=null;
                }else {
                    tmp.mid=tmp.right;
                    tmp.right=null;
                }
                tmp.r=null;
            } else {                                                  //ha 2 nodi, creo un nodo con un solo figlio a sinistra
                if (tmp.left.key == n.key) tmp.left = tmp.mid;
                tmp.mid = null;
                root=union(tmp);
            }
        }
        FixTree(root);
        fixParent(root);
        size--;
       return root;
    }

    //Costo O(N*logN)
    public Nodo union(Nodo n){                                           //fonde il nodo con un solo figlio con uno dei fratelli
        Nodo f1=null;
        if(n.parent==null && n.mid==null){                               //caso base arrivati alla radice
            n.left.parent=null;
            return n.left;
        }else if(n.parent==null && n.mid!=null){
                return delete(n);
            }else{
                if (n.l.compareTo(n.parent.mid.l) < 0) {                        //il nodo è a sinistra
                    f1 = n.parent.mid;
                    if (f1.r == null) {                                             //f ha 2 figli
                        f1.right = f1.mid;
                        f1.mid = f1.left;
                        f1.left = n.left;
                        n.parent.left = f1;
                        n.parent.mid = n.parent.right;
                        n.parent.right = null;
                    } else {                                                      //f ha 3 figli
                        n.mid = f1.left;
                        f1.left = f1.mid;
                        f1.mid = f1.right;
                        f1.right = null;
                        n.parent.mid = f1;
                    }

                } else if (n.l.compareTo(n.parent.mid.l) > 0) {                           //il nodo è a destra
                    f1 = n.parent.mid;
                    if (f1.r == null) {                                             //f ha 2 figli
                        f1.right = n.left;
                        if (n.parent.r != null) {
                            n.parent.mid = f1;
                            n.parent.right = null;
                        } else {
                            n.parent.left = f1;
                            n.parent.mid = null;
                        }
                    } else {                                                      //f ha 3 figli
                        n.mid = n.left;
                        n.left = f1.right;
                        f1.right = null;
                        if (n.parent.r != null) {
                            n.parent.mid = f1;
                        } else {
                            n.parent.left = f1;
                        }
                    }

                } else {                                                        //il nodo è centrale
                    f1 = n.parent.left;
                    if (f1.r != null) {
                        n.mid = n.left;
                        n.left = f1.right;
                        f1.right = null;
                        n.parent.left = f1;
                    } else {
                        f1.right = n.left;
                        n.parent.mid = n.parent.right;
                        n.parent.right = null;
                        n.parent.left = f1;
                    }
                }
                FixTree(root);
                if (n.parent.mid == null) return union(n.parent);
                else return root;
            }
        }

        //Costo O(logN)
    public Nodo find(Nodo r,Nodo n){
        if(r==null)return null;
        else {
            if(r.left.isaLeaf) return r;
            else{
                if(n.key.compareTo(r.l)<=0) return find(r.left,n);
                else if(r.r!=null && n.key.compareTo(r.r)>0) return find(r.right,n);
                else return find(r.mid,n);
            }
        }
    }

    //Costo O(N) con N numero nodi interni
    public void FixTree(Nodo t){                    //fix gli indici l,r dei nodi
        if(t!=null && !t.isaLeaf) {
            if (t.right!= null) t.r = findMax(t.mid);
            else t.r=null;
            t.l = findMax(t.left);
            FixTree(t.left);
            FixTree(t.mid);
            FixTree(t.right);
        }
    }

    //Costo O(N) con N numero nodi interni
    public void fixParent(Nodo r){
        if(r!=null && !r.isaLeaf){
            r.left.parent=r;
            r.mid.parent=r;
            if(r.right!=null) r.right.parent=r;
            fixParent(r.left);
            fixParent(r.mid);
            fixParent(r.right);
        }
    }

    //Costo O(logN)
    public Comparable findMax(Nodo root){
        if(root==null)return null;
        else{
            if(root.isaLeaf)return root.key;
            else {
                if(root.right!=null)return findMax(root.right);
                else return findMax(root.mid);
            }
        }
    }

    //Costo O(N)
    public void stampa(Nodo root){
        if(root!=null){
            if(root.isaLeaf){
                System.out.print(root.key);
                System.out.print('\n');
            }
            else{
                stampa(root.left);
                stampa(root.mid);
                stampa(root.right);
            }
        }
    }

    //Costo O(N) per la chiamata a delete
    public boolean deleteTitle(String title){
        Nodo n=search(root,title.toLowerCase());
        if(n!=null){
            delete(n);
            return true;
        }
        return false;
    }

    //Costo O(N)
    public Movie[] toArrayIt(Nodo root){
        ListaNonOrdinata movies=new ListaNonOrdinata();
        LinkedList<Nodo> queue=new LinkedList<>();
        queue.addLast(root);
        while(!queue.isEmpty()){
            Nodo u=queue.poll();
            if(u!=null) {
                if (u.isaLeaf) {
                    movies.insert(u.movie, u.movie.getYear());
                }
                if (u.left != null) queue.addLast(u.left);
                if (u.mid != null) queue.addLast(u.mid);
                if (u.right != null) queue.addLast(u.right);
            }
        }
        return movies.toArray();
    }


    //Costo O(N)
    public Record[] toArrayR(String key){
        Movie[] movies=toArrayIt(root);
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

}