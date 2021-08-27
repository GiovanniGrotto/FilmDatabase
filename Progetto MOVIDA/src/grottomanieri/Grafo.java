package grottomanieri;

import commons.*;

import java.util.*;

public class Grafo {
    public HashMap<Person, Set<Collaboration>> grafo;

    public Grafo(){
        grafo=new HashMap<Person, Set<Collaboration>>();
    }

    public void addActor(Person p){
        if(!grafo.containsKey(p)) {
            grafo.put(p,new HashSet<>());
        }
    }

    //Costo O(|people|)
    public void addCollaboration(Person p1, Person p2, Movie m){
        addActor(p1);
        addActor(p2);
        Collaboration collaboration=null;
        Set<Collaboration> collaborations=grafo.get(p1);
        for(Collaboration c: collaborations){
            if((c.getActorA().equals(p1) && c.getActorB().equals(p2)) || (c.getActorB().equals(p1) && c.getActorA().equals(p2))){
                collaboration=c;
            }
        }
        if(collaboration==null){
            collaboration=new Collaboration(p1,p2);
            collaboration.getMovies().add(m);
            grafo.get(p1).add(collaboration);
            grafo.get(p2).add(collaboration);
        } else{
            if(!collaboration.getMovies().contains(m)) collaboration.getMovies().add(m);
        }
    }
    //Costo O(|Cast|**2) * O(|people|)      (il cast è dell'ordine O(1) )
    public void addMovie(Movie m){
        for(Person p:m.getCast()){
            for(Person q:m.getCast()){
                if(!p.equals(q)) addCollaboration(p,q,m);
            }
        }
    }

    //Costo
    public void removeMovie(Movie m){
        if(m!=null) {
            ArrayList<Collaboration> toRemove = new ArrayList<>();
            for (Person i : m.getCast()) {
                for (Person j : m.getCast()) {
                    Set<Collaboration> collabs = grafo.get(i);
                    for (Collaboration c : collabs) {
                        if (c.equals(new Collaboration(i, j)) || c.equals(new Collaboration(j, i))) {
                            toRemove.add(c);
                        }
                    }
                    for (Collaboration c : toRemove) {
                        grafo.get(i).remove(c);
                    }
                }
            }
            for (Person i : m.getCast()) {
                if (grafo.get(i).isEmpty()) {
                    grafo.remove(i);
                }
            }
        }
    }

    public Collaboration[] getDirectCollaborationOf(Person p){
        if(grafo.containsKey(p)) {
            Set<Collaboration> collaborationSet = grafo.get(p);
            if(!collaborationSet.isEmpty()) return collaborationSet.toArray(new Collaboration[0]);
        }
        return new Collaboration[0];
    }

    public Person[] getDirectCollaboratorsOf(Person p){
        Collaboration[] collaborations=getDirectCollaborationOf(p);
        Person[] people=new Person[collaborations.length];
        for(int i=0;i<collaborations.length;i++){
            if(collaborations[i].getActorA().equals(p)){
                people[i]=collaborations[i].getActorB();
            } else{
                people[i]=collaborations[i].getActorA();
            }
        }
        return people;
    }

    //Costo O(|team|)
    public Person[] getTeamOf(Person p){
        if(grafo.containsKey(p)){
            ArrayList<Person> team=new ArrayList<>();
            Queue<Person> people=new LinkedList<>();
            people.add(p);  //costante
            while(!people.isEmpty()){
                Person u=people.poll();     //costante
                if(!team.contains(u)) team.add(u);  //costante
                Person[] directCollaborators=getDirectCollaboratorsOf(u);
                for(Person i:directCollaborators){  //O(|collaboratori diretti di i|)
                    if(!team.contains(i)){
                        team.add(i);
                        people.add(i);
                    }
                }
            }
            return team.toArray(new Person[0]);
        } else {
            return new Person[0];
        }
    }

    //Costo O(m*logN) con N=O(|team|) e m=O(|collaborations|)
    public Collaboration[] maximizeCollaborationsInTheTeamOf(Person p){
        if(grafo.containsKey(p)) {
            HashMap<Person, Double> d = new HashMap<>();
            ArrayList<Collaboration> collaborations=new ArrayList<>();
            for (Person i : getTeamOf(p)) {
                d.put(i, Double.MIN_VALUE);
            }
            d.replace(p, 0.0);
            PriorityQueue<Coppia> queue = new PriorityQueue<>();
            queue.add(new Coppia(p, d.get(p)));
            while (!queue.isEmpty()) {
                Coppia u = queue.poll();
                Person person = u.getP();
                for (Collaboration i : getDirectCollaborationOf(person)) {
                    Person other;
                    if (i.getActorA().equals(person)) other = i.getActorB();
                    else other = i.getActorA();
                    if (d.get(other).equals(Double.MIN_VALUE)) {
                        queue.add(new Coppia(other, -i.getScore()));
                        d.replace(other, -i.getScore());
                        collaborations.add(i);
                    } else if (-i.getScore() < d.get(other)) {
                        queue.remove(new Coppia(other, -i.getScore()));
                        queue.add(new Coppia(other, -i.getScore()));
                        d.replace(other, -i.getScore());
                        if(!collaborations.contains(i)) collaborations.add(i);
                    }
                }
            }
            return collaborations.toArray(new Collaboration[0]);
        }
        else return new Collaboration[0];
    }

    private class Coppia implements Comparable<Coppia>{
        private Person p;
        private double priority;

        public Coppia(Person p, double priority){
            this.p=p;
            this.priority=priority;
        }
        public Person getP(){
            return p;
        }
        public double getPriority(){
            return priority;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Coppia coppia = (Coppia) o;
            return Objects.equals(p, coppia.p);
        }

        @Override
        public int hashCode() {
            return Objects.hash(p);
        }

        @Override
        public int compareTo(Coppia o) {
            if(this.p.getName().toLowerCase().compareTo(o.p.getName().toLowerCase())<0) return -1;
            else if(this.p.getName().toLowerCase().compareTo(o.p.getName().toLowerCase())>0) return 1;
            else return 0;
        }
    }
}
