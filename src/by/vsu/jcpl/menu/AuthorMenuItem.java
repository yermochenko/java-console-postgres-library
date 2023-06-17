package by.vsu.jcpl.menu;

import by.vsu.jcpl.AuthorDatabaseMapper;

abstract public class AuthorMenuItem implements MenuItem {
	private final AuthorDatabaseMapper authorDatabaseMapper;

	public AuthorMenuItem(AuthorDatabaseMapper authorDatabaseMapper) {
		this.authorDatabaseMapper = authorDatabaseMapper;
	}

	protected AuthorDatabaseMapper getAuthorDatabaseMapper() {
		return authorDatabaseMapper;
	}
}
