package task3;

import java.util.ArrayList;
import java.util.List;

public class Movie {
	public Movie(int year, String description, String name) {
		super();
		this.year = year;
		this.description = description;
		this.name = name;
	}
	int year;
	String description,name ; 
	List<String> actors = new ArrayList<String>();
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<String> getActors() {
		return actors;
	}
	public void setActors(List<String> actors) {
		this.actors = actors;
	}

}
