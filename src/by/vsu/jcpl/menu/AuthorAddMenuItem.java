package by.vsu.jcpl.menu;

import by.vsu.jcpl.Author;
import by.vsu.jcpl.AuthorDatabaseMapper;
import by.vsu.jcpl.EntityValidationException;

import java.sql.SQLException;
import java.util.Scanner;

public class AuthorAddMenuItem extends AuthorMenuItem {
	public AuthorAddMenuItem(String title, AuthorDatabaseMapper authorDatabaseMapper) {
		super(title, authorDatabaseMapper);
	}

	@Override
	public boolean activate() throws SQLException, EntityValidationException {
		Scanner console = new Scanner(System.in);
		System.out.println("\n==<[ ADDING OF NEW AUTHOR ]>==\n");
		Author author = new Author();
		System.out.print("Enter author's name: ");
		String name = console.nextLine();
		if(!name.isBlank()) {
			author.setName(name);
		} else {
			throw new EntityValidationException("Name shouldn't be empty");
		}
		System.out.print("Enter author's surname: ");
		String surname = console.nextLine();
		if(!surname.isBlank()) {
			author.setSurname(surname);
		} else {
			throw new EntityValidationException("Surname shouldn't be empty");
		}
		System.out.print("Enter author's birth year: ");
		String birthYear = console.nextLine();
		try {
			author.setBirthYear(Integer.parseInt(birthYear));
		} catch(NumberFormatException e) {
			throw new EntityValidationException("Birth year shouldn't be empty and should be integer");
		}
		System.out.print("Enter author's death year if author is dead or press \"Enter\" if author is alive: ");
		String deathYear = console.nextLine();
		if(!deathYear.isBlank()) {
			try {
				author.setDeathYear(Integer.valueOf(deathYear));
			} catch(NumberFormatException e) {
				throw new EntityValidationException("Death year should be integer");
			}
		}
		if(author.getDeathYear() != null && author.getBirthYear() > author.getDeathYear()) {
			throw new EntityValidationException("Birth year should be earlier than death year");
		}
		Integer id = getAuthorDatabaseMapper().create(author);
		System.out.printf("Author successfully added with identifier [%04d]\n", id);
		return true;
	}
}
