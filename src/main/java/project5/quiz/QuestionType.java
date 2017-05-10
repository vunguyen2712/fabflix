package project5.quiz;

import java.util.ArrayList;
import java.util.Arrays;

public enum QuestionType {

	/** Who directed the movie X? **/
	WHO_DIRECTED_MOVIE(),

	/** When was the movie X released? **/
	WHEN_MOVIE_RELEASED(),

	/** Which star (was/was not) in the movie X? **/
	WHICH_STAR_IN_MOVIE(),

	/** In which movie the stars X and Y appear together? **/
	WHICH_MOVIE_WITH_STAR_X_AND_Y(),

	/** Who directed/did not direct the star X? **/
	WHO_DIRECTED_STAR(),

	/** Which star appears in both movies X and Y? **/
	WHICH_STAR_IN_MOVIE_X_AND_Y(),

	/** Which star did not appear in the same movie with the star X? **/
	WHICH_STAR_NOT_IN_SAME_MOVIE_AS_STAR(),

	/** Who directed the star X in year Y? **/
	WHO_DIRECTED_STAR_IN_YEAR();

	private QuestionType() {
	}
	
	public static ArrayList<QuestionType> typeList = new ArrayList<QuestionType>(
			Arrays.asList(WHO_DIRECTED_MOVIE, WHEN_MOVIE_RELEASED, WHICH_STAR_IN_MOVIE,
					WHICH_MOVIE_WITH_STAR_X_AND_Y, WHO_DIRECTED_STAR, WHICH_STAR_IN_MOVIE_X_AND_Y,
					WHICH_STAR_NOT_IN_SAME_MOVIE_AS_STAR, WHO_DIRECTED_STAR_IN_YEAR));
	
	public static QuestionType getRandomType() {
		return typeList.get((int) (Math.random() * typeList.size()));
	}

};

/** switch template **/

/*
switch (type) {
case WHO_DIRECTED_MOVIE:
	break;

case WHEN_MOVIE_RELEASED:
	break;

case WHICH_STAR_IN_MOVIE:
	break;

case WHICH_MOVIE_WITH_STAR_X_AND_Y:
	break;

case WHO_DIRECTED_STAR:
	break;

case WHICH_STAR_IN_MOVIE_X_AND_Y:
	break;

case WHICH_STAR_NOT_IN_SAME_MOVIE_AS_STAR:
	break;

case WHO_DIRECTED_STAR_IN_YEAR:
	break;
	
default:
	break;
}
*/
