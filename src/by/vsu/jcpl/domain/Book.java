package by.vsu.jcpl.domain;

import java.util.LinkedHashSet;
import java.util.Set;

public class Book extends Entity {
	private String title;
	private Integer year;
	private final Set<Author> authors = new LinkedHashSet<>();

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Set<Author> getAuthors() {
		return authors;
	}
}
