package grottomanieri;

import commons.*;
import java.io.*;

public class MovidaMain {
    public static void main(String[] args){
        MovidaCore core=new MovidaCore();
        System.out.print("\n");
        System.out.print("-------MOVIDA CONFIG TEST------- \n \n");
        core.setMap(MapImplementation.ABR);
        core.setMap(MapImplementation.Alberi23);
        core.setMap(MapImplementation.ListaNonOrdinata);
        core.setSort(SortingAlgorithm.InsertionSort);
        core.setSort(SortingAlgorithm.BubbleSort);
        core.setSort(SortingAlgorithm.HeapSort);
        System.out.print("\n");

        System.out.print("-------MOVIDA DB TEST------- \n \n");
        File load=new File("..\\Progetto MOVIDA\\src\\grottomanieri\\esempio-formato-dati.txt");
        File save=new File("..\\Progetto MOVIDA\\src\\grottomanieri\\dati-modificati.txt");
        core.loadFromFile(load);
        core.clear();
        core.loadFromFile(load);

        System.out.print("\n");


        System.out.print("Movies presenti: "+core.countMovies()+"\n");
        System.out.print("Persone presenti: "+core.countPeople()+"\n");
        System.out.print("\n");

        if(core.deleteMovieByTitle("Pulp fIction")) System.out.print("Movie eliminato correttamente");
        else System.out.print("Movie non presente");
        System.out.print("\n");
        String movie="pulp fiction";
        Movie tmp=core.getMovieByTitle(movie);
        if(tmp!=null) System.out.print(tmp+"\n");
        else System.out.print(movie+" non è presente"+"\n");
        tmp=core.getMovieByTitle("taxi driver");
        System.out.print("Trovato: "+tmp+"\n");
        System.out.print("\n");

        core.saveToFile(save);
        String person="uma thurman";
        Person actor=core.getPersonByName(person);
        if(actor!=null) System.out.print(actor+"\n");
        else System.out.print(person+" non è presente"+"\n");
        actor=core.getPersonByName("Alan rickman");
        System.out.print("Trovato: "+actor+"\n");
        System.out.print("\n");

        System.out.println("Lista film: ");
        Movie[] m=core.getAllMovies();
        for(Movie i:m){
            System.out.println(i);
        }
        System.out.println("\n");

        System.out.println("Lista persone: ");
        Person[] p=core.getAllPeople();
        for(Person i:p){
            System.out.println(i);
        }
        System.out.print("\n");

        System.out.print("-------MOVIDA-SEARCH TEST------- \n \n");

        Movie[] r=core.searchMostRecentMovies(4);
        System.out.print("Film più recenti:\n");
        for(Movie i:r){
            System.out.println(i);
        }
        System.out.print("\n");

        r=core.searchMostVotedMovies(4);
        System.out.print("Film più votati:\n");
        for(Movie i:r){
            System.out.println(i);
        }
        System.out.print("\n");

        String substring="on";
        System.out.print("Film che contengono la sottostringa '"+substring+"':\n");
        r=core.searchMoviesByTitle(substring);
        for(Movie i:r){
            System.out.println(i);
        }
        System.out.print("\n");

        String director="Martin Scorsese";
        System.out.print("Film diretti da "+director+":\n");
        r=core.searchMoviesDirectedBy(director);
        for(Movie i:r){
            System.out.println(i);
        }
        System.out.print("\n");

        int year=1997;
        System.out.print("Film prodotti nel "+year+":\n");
        r=core.searchMoviesInYear(year);
        for(Movie i:r){
            System.out.println(i);
        }
        System.out.print("\n");

        person="jodie foster";
        r=core.searchMoviesStarredBy(person);
        System.out.println("Cerco i film dove compare " + person+ ":");
        for(Movie i:r){
            System.out.println(i);
        }
        System.out.print("\n");

        System.out.println("Cerco gli attori più attivi:");
        p=core.searchMostActiveActors(3);
        for(Person i:p){
            System.out.println(i);
        }
        System.out.print("\n");

        System.out.print("-------MOVIDA-COLLABORATION TEST------- \n \n");

        person="jodie foster";
        System.out.println("I collaboraori diretti di "+person+" sono:");
        actor=core.getPersonByName(person);
        p=core.getDirectCollaboratorsOf(actor);
        for(Person i:p){
            System.out.println(i);
        }
        System.out.print("\n");

        person="robert de niro";
        System.out.println("Il team di " +person+" è composto da:");
        actor=core.getPersonByName(person);
        p=core.getTeamOf(actor);
        for(Person i:p){
            System.out.println(i);
        }
        System.out.print("\n");

        person="jessica lange";
        System.out.println("Le collaborazioni più prolifiche per il team di "+person+" sono state:");
        actor=core.getPersonByName(person);
        Collaboration[] c=core.maximizeCollaborationsInTheTeamOf(actor);
        for(Collaboration i:c){
            System.out.println(i);
        }
        System.out.print("\n");
    }
}
