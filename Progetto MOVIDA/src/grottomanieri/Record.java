package grottomanieri;

import commons.Movie;

public class Record {
    public Movie m;
    public Comparable key;
    public Record prev;
    public Record next;
    public Record(Movie m, Comparable key,Record n, Record p) {
        this.m = m;
        this.key = key;
        this.prev = p;
        this.next=n;
    }
}
