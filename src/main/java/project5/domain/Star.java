package project5.domain;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Star {
	private int id;
	private String first_name;
	private String last_name;
	private Date dob;
	private String photo_url;

	public Star(String csv) {
		try {
			String[] split = csv.split("[,]");
			this.id = Integer.parseInt(split[0]);
			this.first_name = split[1];
			this.last_name = split[2];
			this.dob = parseDate(split[3]);
			this.photo_url = split[4];
			fixStrings();
		} catch (Exception e) {
			System.out.println(csv);
			e.printStackTrace();
			System.exit(-1);
		}
	}

	private void fixStrings() {
		first_name = fixString(first_name);
		last_name = fixString(last_name);
		photo_url = fixString(photo_url);
	}

	private static String fixString(String s) {
		return s.replace("\"", "");
	}

	private Date parseDate(String s) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String fixedString = fixString(s);
		try {
			return formatter.parse(fixedString);
		} catch (Exception e) {
			System.out.println(s);
			e.printStackTrace();
			System.exit(-1);
		}
		return new Date();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public String getFullName() {
		return this.first_name + " " + this.last_name;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getPhoto_url() {
		return photo_url;
	}

	public void setPhoto_url(String photo_url) {
		this.photo_url = photo_url;
	}
}