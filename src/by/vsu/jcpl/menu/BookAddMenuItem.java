package by.vsu.jcpl.menu;

import by.vsu.jcpl.EntityValidationException;
import by.vsu.jcpl.domain.Author;
import by.vsu.jcpl.domain.Book;
import by.vsu.jcpl.orm.BookDatabaseMapper;

import java.sql.SQLException;
import java.util.Arrays;

public class BookAddMenuItem extends BookMenuItem {
	public BookAddMenuItem(String title, BookDatabaseMapper bookDatabaseMapper) {
		super(title, bookDatabaseMapper);
	}

	public boolean activate() throws SQLException, EntityValidationException {
		System.out.println("\n==<[ ADDING OF NEW BOOK ]>==\n");
		Book book = new Book();
		System.out.print("Enter author's identifier (or list of authors identifiers separated by comma, or press \"Enter\" if there are no authors of this book): ");
		String authorIds = console.nextLine();
		if(!authorIds.isBlank()) {
			try {
				Arrays.stream(authorIds.split(",")).forEach(id -> {
					Author author = new Author();
					author.setId(Integer.valueOf(id.trim()));
					book.getAuthors().add(author);
				});
			} catch(NumberFormatException e) {
				throw new EntityValidationException("Authors identifiers should be integers");
			}
		}
		System.out.print("Enter book's title: ");
		String title = console.nextLine();
		if(!title.isBlank()) {
			book.setTitle(title);
		} else {
			throw new EntityValidationException("Title shouldn't be empty");
		}
		System.out.print("Enter book's publishing year: ");
		String year = console.nextLine();
		try {
			book.setYear(Integer.valueOf(year));
		} catch(NumberFormatException e) {
			throw new EntityValidationException("Publishing year shouldn't be empty and should be integer");
		}
		Integer id = getBookDatabaseMapper().create(book);
		System.out.printf("Book was successfully added with identifier [%04d]\n", id);
		return true;
	}
}
