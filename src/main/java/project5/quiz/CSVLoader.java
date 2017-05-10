package project5.quiz;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.HashMap;

import project5.domain.*;

public class CSVLoader {

	public static HashMap<Integer, Movie> loadMovies(Context context, String csvPath) {
		HashMap<Integer, Movie> map = new HashMap<Integer, Movie>();
		
		BufferedReader br = null;
		String currentLine;
		Movie m;

		try {
			br = new BufferedReader(new InputStreamReader(context.getAssets().open(csvPath)));
			while ((currentLine = br.readLine()) != null) {
				m = new Movie(currentLine);
				map.put(m.getId(), m);
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return map;
	}
	
	public static HashMap<Integer, Star> loadStars(Context context, String csvPath) {
		HashMap<Integer, Star> map = new HashMap<Integer, Star>();
		
		BufferedReader br = null;
		String currentLine;
		Star s;
		
		try {
            br = new BufferedReader(new InputStreamReader(context.getAssets().open(csvPath)));
			while ((currentLine = br.readLine()) != null) {
				s = new Star(currentLine);
				map.put(s.getId(), s);
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return map;
	}
	
	public static HashMap<Integer, Star_In_Movie> loadStarInMovies(Context context, String csvPath) {
		HashMap<Integer, Star_In_Movie> map = new HashMap<Integer, Star_In_Movie>();
		
		BufferedReader br = null;
		String currentLine;
		Star_In_Movie s;
		int i = 0;
		
		try {
            br = new BufferedReader(new InputStreamReader(context.getAssets().open(csvPath)));
			while ((currentLine = br.readLine()) != null) {
				s = new Star_In_Movie(currentLine);
				map.put(i++, s);
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return map;
	}
	
}
