package project5.quiz;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import project5.domain.*;

public class DataHolder {

	private HashMap<Integer, Movie> movies;
	private HashMap<Integer, Star> stars;
	private HashMap<Integer, Star_In_Movie> stars_in_movies;

    private DataHolder dataHolder;

	public DataHolder(HashMap<Integer, Movie> movies,
			HashMap<Integer, Star> stars,
			HashMap<Integer, Star_In_Movie> stars_in_movies) {
		this.movies = movies;
		this.stars = stars;
		this.stars_in_movies = stars_in_movies;
	}

	public Movie getRandomMovie() {
		Random generator = new Random();
		Object[] values = movies.values().toArray();
		return (Movie) values[generator.nextInt(values.length)];
	}

	public Star getRandomStar() {
		Random generator = new Random();
		Object[] values = stars.values().toArray();
		return (Star) values[generator.nextInt(values.length)];
	}

	public String getRandomDirectorName() {
		return getRandomMovie().getDirector();
	}

	public HashSet<Star> getStarsFromMovie(Movie movie) {
		HashSet<Star> values = new HashSet<Star>();
		for (Star_In_Movie s : stars_in_movies.values()) {
			if (s.getMovie_id() == movie.getId()) {
				values.add(stars.get(s.getStar_id()));
			}
		}
		return values;
	}

	public HashSet<Movie> getMoviesFromStar(Star star) {
		HashSet<Movie> values = new HashSet<Movie>();
		for (Star_In_Movie s : stars_in_movies.values()) {
			if (s.getStar_id() == star.getId()) {
				values.add(movies.get(s.getMovie_id()));
			}
		}
		return values;
	}

	public HashSet<String> getDirectorsFromStar(Star star) {
		HashSet<String> values = new HashSet<String>();
		HashSet<Movie> movieSet = getMoviesFromStar(star);
		for (Movie m : movieSet) {
			values.add(m.getDirector());
		}
		return values;
	}

}
