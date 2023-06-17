package by.vsu.jcpl.menu;

import by.vsu.jcpl.Author;
import by.vsu.jcpl.AuthorDatabaseMapper;
import by.vsu.jcpl.EntityValidationException;

import java.sql.SQLException;
import java.util.Scanner;

public class AuthorEditMenuItem {
	public static void activate(AuthorDatabaseMapper authorDatabaseMapper) throws SQLException, EntityValidationException {
		Scanner console = new Scanner(System.in);
		System.out.println("\n==<[ UPDATING INFORMATION ABOUT AUTHOR ]>==\n");
		System.out.print("Enter author's identifier (ID): ");
		int id;
		try {
			id = Integer.parseInt(console.nextLine());
		} catch(NumberFormatException e) {
			throw new EntityValidationException("Identifier should be integer");
		}
		Author author = authorDatabaseMapper.readById(id);
		boolean isChanged = false;
		System.out.printf("Author's name: %s\n", author.getName());
		System.out.print("Enter new name (or press \"Enter\" to leave existing name): ");
		String name = console.nextLine();
		if(!name.isBlank()) {
			author.setName(name);
			isChanged = true;
		}
		System.out.printf("Author's surname: %s\n", author.getSurname());
		System.out.print("Enter new surname (or press \"Enter\" to leave existing surname): ");
		String surname = console.nextLine();
		if(!surname.isBlank()) {
			author.setSurname(surname);
			isChanged = true;
		}
		System.out.printf("Author's birth year: %d\n", author.getBirthYear());
		System.out.print("Enter new birth year (or press \"Enter\" lo leave existing birth year): ");
		String birthYear = console.nextLine();
		if(!birthYear.isBlank()) {
			try {
				author.setBirthYear(Integer.valueOf(birthYear));
			} catch(NumberFormatException e) {
				throw new EntityValidationException("Birth year should be integer");
			}
			isChanged = true;
		}
		System.out.print("Author's death year: ");
		if(author.getDeathYear() != null) {
			System.out.printf("%d\n", author.getDeathYear());
		} else {
			System.out.println("---");
		}
		System.out.print("Enter new death year (or press \"Enter\" to leave existing death year, or print \"-\" if there is need to blank death year): ");
		String deathYear = console.nextLine();
		if(!deathYear.isBlank()) {
			if("-".equals(deathYear)) {
				author.setDeathYear(null);
			} else {
				try {
					author.setDeathYear(Integer.valueOf(deathYear));
				} catch(NumberFormatException e) {
					throw new EntityValidationException("Death year should be integer");
				}
			}
			isChanged = true;
		}
		if(author.getDeathYear() != null && author.getBirthYear() > author.getDeathYear()) {
			throw new EntityValidationException("Birth year should be earlier than death year");
		}
		if(isChanged) {
			authorDatabaseMapper.update(author);
			System.out.println("Information about author was successfully updated");
		} else {
			System.out.println("Nothing changed");
		}
	}
}
