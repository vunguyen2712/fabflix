package project5.domain;

public class Star_In_Movie {
	private int star_id;
	private int movie_id;

	public Star_In_Movie(String csv) {
		try {
			String[] split = csv.split("[,]");
			this.star_id = Integer.parseInt(split[0]);
			this.movie_id = Integer.parseInt(split[1]);
		} catch (Exception e) {
			System.out.println(csv);
			e.printStackTrace();
			System.exit(-1);
		}
	}

	public int getStar_id() {
		return star_id;
	}

	public void setStar_id(int star_id) {
		this.star_id = star_id;
	}

	public int getMovie_id() {
		return movie_id;
	}

	public void setMovie_id(int movie_id) {
		this.movie_id = movie_id;
	}
}