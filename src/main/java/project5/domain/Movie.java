package project5.domain;

public class Movie {
	private int id;
	private String title;
	private int year;
	private String director;
	private String banner_url;
	private String trailer_url;

	public Movie(String csv) {
		try {
			int i = 0;
			String[] split = csv.split("[,]");
			this.id = Integer.parseInt(split[i++]);
			do {
				this.title = split[i++]; // handle if comma in title
			} while (!this.title.endsWith("\""));
			this.year = Integer.parseInt(split[i++]);
			this.director = split[i++];
			this.banner_url = split[i++];
			this.trailer_url = split[i++];
			fixStrings();
		} catch (Exception e) {
			System.out.println(csv);
			e.printStackTrace();
			System.exit(-1);
		}
	}

	private void fixStrings() {
		title = fixString(title);
		director = fixString(director);
		banner_url = fixString(banner_url);
		trailer_url = fixString(trailer_url);
	}

	private static String fixString(String s) {
		return s.replace("\"", "");
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public String getBanner_url() {
		return banner_url;
	}

	public void setBanner_url(String banner_url) {
		this.banner_url = banner_url;
	}

	public String getTrailer_url() {
		return trailer_url;
	}

	public void setTrailer_url(String trailer_url) {
		this.trailer_url = trailer_url;
	}
}