package by.vsu.jcpl.menu;

import by.vsu.jcpl.EntityValidationException;
import by.vsu.jcpl.orm.BookDatabaseMapper;

import java.sql.SQLException;

public class BookDeleteMenuItem extends BookMenuItem {
	public BookDeleteMenuItem(String title, BookDatabaseMapper bookDatabaseMapper) {
		super(title, bookDatabaseMapper);
	}

	public boolean activate() throws SQLException, EntityValidationException {
		System.out.println("\n==<[ DELETING INFORMATION ABOUT BOOK ]>==\n");
		System.out.print("Enter book's identifier (ID): ");
		int id;
		try {
			id = Integer.parseInt(console.nextLine());
		} catch(NumberFormatException e) {
			throw new EntityValidationException("Identifier should be integer");
		}
		getBookDatabaseMapper().delete(id);
		System.out.println("Information about book was successfully deleted");
		return true;
	}
}
