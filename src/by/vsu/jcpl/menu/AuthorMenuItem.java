package by.vsu.jcpl.menu;

import by.vsu.jcpl.AuthorDatabaseMapper;

abstract public class AuthorMenuItem extends NamedMenuItem {
	private final AuthorDatabaseMapper authorDatabaseMapper;

	protected AuthorMenuItem(String title, AuthorDatabaseMapper authorDatabaseMapper) {
		super(title);
		this.authorDatabaseMapper = authorDatabaseMapper;
	}

	protected AuthorDatabaseMapper getAuthorDatabaseMapper() {
		return authorDatabaseMapper;
	}
}
