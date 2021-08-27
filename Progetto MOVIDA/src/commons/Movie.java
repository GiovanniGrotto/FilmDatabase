/* 
 * Copyright (C) 2020 - Angelo Di Iorio
 * 
 * Progetto Movida.
 * Corso di Algoritmi e Strutture Dati
 * Laurea in Informatica, UniBO, a.a. 2019/2020
 * 
*/
package commons;

import grottomanieri.Nodo;

import java.util.Arrays;
import java.util.Objects;

/**
 * Classe usata per rappresentare un film
 * nell'applicazione Movida.
 * 
 * Un film � identificato in modo univoco dal titolo 
 * case-insensitive, senza spazi iniziali e finali, senza spazi doppi. 
 * 
 * La classe pu� essere modicata o estesa ma deve implementare tutti i metodi getter
 * per recupare le informazioni caratterizzanti di un film.
 * 
 */
public class Movie {
	
	private String title;
	private Integer year;
	private Integer votes;
	private Person[] cast;
	private Person director;
	
	public Movie(String title, Integer year, Integer votes,
			Person[] cast, Person director) {
		this.title = title;
		this.year = year;
		this.votes = votes;
		this.cast = cast;
		this.director = director;
	}

	public String getTitle() {
		return this.title;
	}

	public Integer getYear() {
		return this.year;
	}

	public Integer getVotes() {
		return this.votes;
	}

	public Person[] getCast() {
		return this.cast;
	}

	public Person getDirector() {
		return this.director;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	public void setCast(Person[] cast) {
		this.cast = cast;
	}

	public void setDirector(Person director) {
		this.director = director;
	}

	public void setVotes(Integer votes) {
		this.votes = votes;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	@Override
	public String toString() {
		return "Movie{" +
				"'" + title + '\'' +
				", " + year +
				", " + votes +
				", " + Arrays.toString(cast) +
				", " + director +
				'}';
	}

	public void stampa(){
		System.out.println(title);
		System.out.println(year);
		System.out.println(votes);
		System.out.println(director.getName());
		for(int i=0;i<cast.length;i++){
			System.out.println(cast[i].getName());
		}
	}
	public Nodo MovidetoNodo(Movie m){
		return new Nodo(m,m.getTitle(),null,null,null,null,null,null,true);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Movie movie = (Movie) o;
		return Objects.equals(title, movie.title) &&
				Objects.equals(year, movie.year) &&
				Objects.equals(votes, movie.votes) &&
				Arrays.equals(cast, movie.cast) &&
				Objects.equals(director, movie.director);
	}

	@Override
	public int hashCode() {
		int result = Objects.hash(title, year, votes, director);
		result = 31 * result + Arrays.hashCode(cast);
		return result;
	}
}
