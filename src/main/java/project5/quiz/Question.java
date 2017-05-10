package project5.quiz;

import java.util.ArrayList;
import java.util.HashSet;

import project5.domain.*;

public class Question {
	
	private DataHolder dataHolder;
	private QuestionType type;
	private String question;
	private HashSet<String> answers;
	private String correctAnswer;

    public Question(DataHolder dataHolder) {
        this.dataHolder = dataHolder;
        this.type = QuestionType.getRandomType();
        generateQuestion();
    }

	public Question(DataHolder dataHolder, QuestionType type) {
		this.dataHolder = dataHolder;
		this.type = type;
		generateQuestion();
	}

	private void generateQuestion() {
		do {
			question = "";
			answers = new HashSet<String>();
			correctAnswer = "";
	
			Movie movie;
			Star star;
			HashSet<Movie> movies;
			HashSet<Star> stars;
			HashSet<String> directors;
			HashSet<String> starNames = new HashSet<String>();
			String name;
			Movie movie1;
			Movie movie2;
			Star star1;
			Star star2;
	
			switch (this.type) {
			case WHO_DIRECTED_MOVIE:
				movie = dataHolder.getRandomMovie();
				correctAnswer = movie.getDirector();
				answers.add(correctAnswer);
				while (answers.size() < 4) {
					answers.add(dataHolder.getRandomMovie().getDirector());
				}
				question = "Who directed the movie '" + movie.getTitle() + "'?";
				break;
	
			case WHEN_MOVIE_RELEASED:
				movie = dataHolder.getRandomMovie();
				correctAnswer = Integer.toString(movie.getYear());
				answers.add(correctAnswer);
				while (answers.size() < 4) {
					answers.add(Integer.toString(dataHolder.getRandomMovie()
							.getYear()));
				}
				question = "When was the movie '" + movie.getTitle()
						+ "' released?";
				break;
	
			case WHICH_STAR_IN_MOVIE:
				movie = dataHolder.getRandomMovie();
				stars = dataHolder.getStarsFromMovie(movie);
				for (Star s : stars) {
					starNames.add(s.getFullName());
				}
				if (stars.size() < 3) {
					question = "Which star was in the movie '" + movie.getTitle()
							+ "'?";
					for (Star s : stars) {
						correctAnswer = s.getFullName();
						answers.add(correctAnswer);
						break;
					}
					while (answers.size() < 4) {
						name = dataHolder.getRandomStar().getFullName();
						if (!starNames.add(name)) {
							answers.add(name);
						}
					}
				} else {
					question = "Which star was NOT in the movie '"
							+ movie.getTitle() + "'?";
					for (Star s : stars) {
						answers.add(s.getFullName());
						if (answers.size() == 3)
							break;
					}
					while (answers.size() < 4) {
						name = dataHolder.getRandomStar().getFullName();
						if (!starNames.add(name)) {
							correctAnswer = name;
							answers.add(correctAnswer);
							break;
						}
					}
				}
				break;
	
			case WHICH_MOVIE_WITH_STAR_X_AND_Y:
				while (true) {
					star1 = star2 = null;
					movie = dataHolder.getRandomMovie();
					stars = dataHolder.getStarsFromMovie(movie);
	
					if (stars.size() < 2) {
						continue;
					}
	
					for (Star s : stars) {
						if (star1 == null)
							star1 = s;
						else if (star2 == null) {
							star2 = s;
							break;
						}
					}
	
					correctAnswer = movie.getTitle();
					answers.add(correctAnswer);
					while (answers.size() < 4) {
						movie1 = dataHolder.getRandomMovie();
						stars = dataHolder.getStarsFromMovie(movie1);
						if (!stars.contains(star1) && !stars.contains(star2)) {
							answers.add(movie1.getTitle());
						}
					}
					question = "In which movies do the stars '"
							+ star1.getFullName() + "' and '" + star2.getFullName()
							+ "' appear together?";
					break;
				}
				break;
	
			case WHO_DIRECTED_STAR:
				star = dataHolder.getRandomStar();
				directors = dataHolder.getDirectorsFromStar(star);
				if (directors.size() < 3) {
					question = "Who has directed the star '" + star.getFullName()
							+ "'?";
					for (String d : directors) {
						correctAnswer = d;
						answers.add(correctAnswer);
						break;
					}
					while (answers.size() < 4) {
						name = dataHolder.getRandomDirectorName();
						if (!directors.add(name)) {
							answers.add(name);
						}
					}
				} else {
					question = "Who has NOT directed the star '"
							+ star.getFullName() + "'?";
					for (String d : directors) {
						answers.add(d);
						if (answers.size() == 3)
							break;
					}
					while (answers.size() < 4) {
						name = dataHolder.getRandomDirectorName();
						if (!directors.add(name)) {
							correctAnswer = name;
							answers.add(correctAnswer);
							break;
						}
					}
				}
				
				
				break;
	
			case WHICH_STAR_IN_MOVIE_X_AND_Y:
				while (true) {
					movie1 = movie2 = null;
					star = dataHolder.getRandomStar();
					movies = dataHolder.getMoviesFromStar(star);
	
					if (movies.size() < 2) {
						continue;
					}
	
					correctAnswer = star.getFullName();
					answers.add(correctAnswer);
					for (Movie m : movies) {
						if (movie1 == null)
							movie1 = m;
						else if (movie2 == null) {
							movie2 = m;
							break;
						}
					}
					question = "Which star appears in both movies '"
							+ movie1.getTitle() + "' and '" + movie2.getTitle()
							+ "'?";
					while (answers.size() < 4) {
						star1 = dataHolder.getRandomStar();
						if (!dataHolder.getStarsFromMovie(movie1).contains(star1)
								&& !dataHolder.getStarsFromMovie(movie2).contains(
										star1)) {
							answers.add(star1.getFullName());
						}
					}
					break;
				}
				break;
	
			case WHICH_STAR_NOT_IN_SAME_MOVIE_AS_STAR:
				while (true) {
					star = null;
					movie = dataHolder.getRandomMovie();
					stars = dataHolder.getStarsFromMovie(movie);
					
					if (stars.size() < 4) {
						continue;
					}
					
					for (Star s : stars) {
						starNames.add(s.getFullName());
						if (star == null) {
							star = s;
						}
						else if (answers.size() < 3) {
							answers.add(s.getFullName());
						}
					}
					question = "Which star did not appear in the same movie with the star '"
							+ star.getFullName() + "'?";
					while (answers.size() < 4) {
						name = dataHolder.getRandomStar().getFullName();
						if (!starNames.add(name)) {
							correctAnswer = name;
							answers.add(correctAnswer);
							break;
						}
					}
					break;
				}
				break;
	
			case WHO_DIRECTED_STAR_IN_YEAR:
				while (true) {
					star = null;
					movie = dataHolder.getRandomMovie();
					stars = dataHolder.getStarsFromMovie(movie);
					
					if (stars.size() == 0) {
						continue;
					}
					
					for (Star s : stars) {
						starNames.add(s.getFullName());
						star = s;
					}
					question = "Who directed the star '" + star.getFullName()
							+ "' in year " + movie.getYear() + "?";
					
					correctAnswer = movie.getDirector();
					answers.add(correctAnswer);
		
					while (answers.size() < 4) {
						name = dataHolder.getRandomDirectorName();
						// TODO: wrong, should check movie... oh well
						if (!dataHolder.getDirectorsFromStar(star).contains(name)) {
							answers.add(name);
						}
					}
					break;
				}
				break;
	
			default:
				break;
			}
		} while (answers.size() != 4);
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append("type:     " + type + "\n");
		s.append("question: " + question + "\n");
		s.append("answers:  " + "\n");
		for (String answer : answers) {
			s.append("          " + answer);
			if (answer.equals(correctAnswer)) {
				s.append("*");
			}
			s.append("\n");
		}
		return s.toString();
	}

    public boolean checkAnswer(String choice) {
        return this.correctAnswer.equals(choice);
    }

	public QuestionType getType() {
		return type;
	}

	public void setType(QuestionType type) {
		this.type = type;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public HashSet<String> getAnswers() {
		return answers;
	}

	public void setAnswers(HashSet<String> answers) {
		this.answers = answers;
	}

	public String getCorrectAnswer() {
		return correctAnswer;
	}

	public void setCorrectAnswer(String correctAnswer) {
		this.correctAnswer = correctAnswer;
	}

	public DataHolder getDataHolder() {
		return dataHolder;
	}

	public void setDataHolder(DataHolder dataHolder) {
		this.dataHolder = dataHolder;
	}

    public ArrayList<String> getAnswersInList() { return new ArrayList<String>(this.answers); }
}
