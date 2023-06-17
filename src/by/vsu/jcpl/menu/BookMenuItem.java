package by.vsu.jcpl.menu;

import by.vsu.jcpl.orm.BookDatabaseMapper;

abstract public class BookMenuItem extends NamedMenuItem {
	private final BookDatabaseMapper bookDatabaseMapper;

	protected BookMenuItem(String title, BookDatabaseMapper bookDatabaseMapper) {
		super(title);
		this.bookDatabaseMapper = bookDatabaseMapper;
	}

	protected final BookDatabaseMapper getBookDatabaseMapper() {
		return bookDatabaseMapper;
	}
}
