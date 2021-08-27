package commons;

import java.util.ArrayList;
import java.util.Objects;

public class Collaboration {

	Person actorA;
	Person actorB;
	ArrayList<Movie> movies;
	
	public Collaboration(Person actorA, Person actorB) {
		this.actorA = actorA;
		this.actorB = actorB;
		this.movies = new ArrayList<Movie>();
	}

	public Person getActorA() {
		return actorA;
	}

	public Person getActorB() {
		return actorB;
	}

	public Double getScore(){
		
		Double score = 0.0;
		
		for (Movie m : movies)
			score += m.getVotes();
		
		return score / movies.size();
	}

	public ArrayList<Movie> getMovies() {
		return movies;
	}

	@Override
	public String toString() {
		return "Collaboration{" +
				 actorA +
				  actorB +

				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Collaboration that = (Collaboration) o;
		return Objects.equals(actorA, that.actorA) &&
				Objects.equals(actorB, that.actorB);
	}

	@Override
	public int hashCode() {
		return Objects.hash(actorA, actorB, movies);
	}
}
