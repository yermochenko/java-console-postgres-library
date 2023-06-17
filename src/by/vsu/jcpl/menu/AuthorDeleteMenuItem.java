package by.vsu.jcpl.menu;

import by.vsu.jcpl.orm.AuthorDatabaseMapper;
import by.vsu.jcpl.EntityValidationException;

import java.sql.SQLException;
import java.util.Scanner;

public class AuthorDeleteMenuItem extends AuthorMenuItem {
	public AuthorDeleteMenuItem(String title, AuthorDatabaseMapper authorDatabaseMapper) {
		super(title, authorDatabaseMapper);
	}

	@Override
	public boolean activate() throws SQLException, EntityValidationException {
		Scanner console = new Scanner(System.in);
		System.out.println("\n==<[ DELETING INFORMATION ABOUT AUTHOR ]>==\n");
		System.out.print("Enter author's identifier (ID): ");
		int id;
		try {
			id = Integer.parseInt(console.nextLine());
		} catch(NumberFormatException e) {
			throw new EntityValidationException("Identifier should be integer");
		}
		getAuthorDatabaseMapper().delete(id);
		System.out.println("Information about author was successfully deleted");
		return true;
	}
}
