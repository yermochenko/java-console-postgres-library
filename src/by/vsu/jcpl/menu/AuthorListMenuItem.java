package by.vsu.jcpl.menu;

import by.vsu.jcpl.Author;
import by.vsu.jcpl.AuthorDatabaseMapper;

import java.sql.SQLException;
import java.util.List;

public class AuthorListMenuItem {
	public static void activate(AuthorDatabaseMapper authorDatabaseMapper) throws SQLException {
		System.out.println("\n==<[ Authors ]>==\n");
		List<Author> authors = authorDatabaseMapper.readAll();
		for(Author author : authors) {
			System.out.printf("[%04d] %s %s ", author.getId(), author.getName(), author.getSurname());
			if(author.getDeathYear() != null) {
				System.out.printf("(%d - %d)\n", author.getBirthYear(), author.getDeathYear());
			} else {
				System.out.printf("(%d - ...)\n", author.getBirthYear());
			}
		}
	}
}
