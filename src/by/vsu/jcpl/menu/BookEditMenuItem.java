package by.vsu.jcpl.menu;

import by.vsu.jcpl.EntityValidationException;
import by.vsu.jcpl.domain.Author;
import by.vsu.jcpl.domain.Book;
import by.vsu.jcpl.orm.BookDatabaseMapper;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Set;

public class BookEditMenuItem extends BookMenuItem {
	public BookEditMenuItem(String title, BookDatabaseMapper bookDatabaseMapper) {
		super(title, bookDatabaseMapper);
	}

	public boolean activate() throws SQLException, EntityValidationException {
		System.out.println("\n==<[ UPDATING INFORMATION ABOUT AUTHOR ]>==\n");
		System.out.print("Enter book's identifier (ID): ");
		int id;
		try {
			id = Integer.parseInt(console.nextLine());
		} catch(NumberFormatException e) {
			throw new EntityValidationException("Identifier should be integer");
		}
		Book book = getBookDatabaseMapper().readById(id);
		if(book != null) {
			boolean isChanged = false;
			Set<Author> authors = book.getAuthors();
			if(authors.isEmpty()) {
				System.out.println("Book have no authors");
			} else {
				if(authors.size() == 1) {
					Author author = authors.iterator().next();
					System.out.printf("Book's author: %s %s (%d)\n", author.getName(), author.getSurname(), author.getId());
				} else {
					System.out.println("Book's authors:");
					for(Author author : authors) {
						System.out.printf("\t%s %s (%d)\n", author.getName(), author.getSurname(), author.getId());
					}
				}
			}
			System.out.print("Enter new author's identifier (or list of new authors identifiers separated by comma, or print \"-\" if there is need to blank authors list, or press \"Enter\" to leave existing author/authors): ");
			String authorIds = console.nextLine();
			if(!authorIds.isBlank()) {
				authors.clear();
				if(!"-".equals(authorIds)) {
					try {
						Arrays.stream(authorIds.split(",")).forEach(authorId -> {
							Author author = new Author();
							author.setId(Integer.valueOf(authorId.trim()));
							authors.add(author);
						});
					} catch(NumberFormatException e) {
						throw new EntityValidationException("Authors identifiers should be integers");
					}
				}
				isChanged = true;
			}
			System.out.printf("Book's title: %s\n", book.getTitle());
			System.out.print("Enter new title (or press \"Enter\" lo leave existing title): ");
			String title = console.nextLine();
			if(!title.isBlank()) {
				book.setTitle(title);
				isChanged = true;
			}
			System.out.printf("Book's publishing year: %d\n", book.getYear());
			System.out.print("Enter new publishing year (or press \"Enter\" lo leave existing publishing year): ");
			String year = console.nextLine();
			if(!year.isBlank()) {
				try {
					book.setYear(Integer.valueOf(year));
				} catch(NumberFormatException e) {
					throw new EntityValidationException("Publishing year should be integer");
				}
				isChanged = true;
			}
			if(isChanged) {
				getBookDatabaseMapper().update(book);
				System.out.println("Information about book was successfully updated");
			} else {
				System.out.println("Nothing changed");
			}
		} else {
			System.out.println("There is no book with such identifier");
		}
		return true;
	}
}
