package grottomanieri;

import commons.Movie;

public class Nodo {
    protected Movie movie;
    protected Comparable key;
    protected Comparable l, r;
    protected Nodo parent, left, mid, right;
    protected boolean isaLeaf;

    public Nodo(Nodo n) {
        this.movie = n.movie;
        this.key = n.key;
        this.l = n.l;
        this.r = n.r;
        this.parent = n.parent;
        this.left = n.left;
        this.mid = n.mid;
        this.right = n.right;
        this.isaLeaf = n.isaLeaf;
    }

    public Nodo(Movie movie, Comparable key, Comparable l, Comparable r, Nodo parent, Nodo left, Nodo mid, Nodo right, boolean leaf) {
        this.movie = movie;
        this.key = key;
        this.l = l;
        this.r = r;
        this.parent = parent;
        this.left = left;
        this.mid = mid;
        this.right = right;
        this.isaLeaf = leaf;
    }

    public void setNodo(Nodo n) {
        this.movie = n.movie;
        this.key = n.key;
        this.l = n.l;
        this.r = n.r;
        this.parent = n.parent;
        this.left = n.left;
        this.mid = n.mid;
        this.right = n.right;
        this.isaLeaf = n.isaLeaf;
    }
}