package by.vsu.jcpl.menu;

import by.vsu.jcpl.EntityValidationException;
import by.vsu.jcpl.domain.Book;
import by.vsu.jcpl.orm.BookDatabaseMapper;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class BookListMenuItem extends BookMenuItem {
	public BookListMenuItem(String title, BookDatabaseMapper bookDatabaseMapper) {
		super(title, bookDatabaseMapper);
	}

	public boolean activate() throws SQLException, EntityValidationException {
		System.out.println("\n==<[ BOOKS ]>==\n");
		System.out.print("Enter author's identifier (or press \"Enter\" is there is need to read list of books without authors): ");
		Integer authorId = null;
		String authorIdStr = console.nextLine();
		if(!authorIdStr.isBlank()) {
			try {
				authorId = Integer.parseInt(authorIdStr);
			} catch(NumberFormatException e) {
				throw new EntityValidationException("Identifier should be integer");
			}
		}
		List<Book> books = getBookDatabaseMapper().readByAuthor(authorId);
		if(!books.isEmpty()) {
			for(Book book : books) {
				System.out.printf("[%04d] ", book.getId());
				if(!book.getAuthors().isEmpty()) {
					System.out.printf("%s. ", book.getAuthors().stream().map(author -> author.getName() + ' ' + author.getSurname()).collect(Collectors.joining(", ")));
				}
				System.out.printf("%s (published in %d)\n", book.getTitle(), book.getYear());
			}
		} else {
			System.out.println("Nothing was found");
		}
		return true;
	}
}
