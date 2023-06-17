package by.vsu.jcpl.orm;

import by.vsu.jcpl.domain.Author;
import by.vsu.jcpl.domain.Book;
import by.vsu.jcpl.domain.Entity;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class BookDatabaseMapper extends EntityDatabaseMapper {
	private final AuthorDatabaseMapper authorDatabaseMapper;

	public BookDatabaseMapper(Connection connection, AuthorDatabaseMapper authorDatabaseMapper) {
		super(connection);
		this.authorDatabaseMapper = authorDatabaseMapper;
	}

	public List<Book> readByAuthor(Integer authorId) throws SQLException {
		String sql;
		if(authorId != null) {
			sql = "SELECT \"book\".\"id\", \"book\".\"title\", \"book\".\"year\" FROM \"book\" LEFT JOIN \"author_vs_book\" ON \"book\".\"id\" = \"author_vs_book\".\"book_id\" WHERE \"author_vs_book\".\"author_id\" = ?";
		} else {
			sql = "SELECT \"book\".\"id\", \"book\".\"title\", \"book\".\"year\" FROM \"book\" LEFT JOIN \"author_vs_book\" ON \"book\".\"id\" = \"author_vs_book\".\"book_id\" WHERE \"author_vs_book\".\"author_id\" IS NULL";
		}
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			statement = getConnection().prepareStatement(sql);
			if(authorId != null) {
				statement.setInt(1, authorId);
			}
			resultSet = statement.executeQuery();
			List<Book> books = new ArrayList<>();
			while(resultSet.next()) {
				books.add(parseResultSet(resultSet));
			}
			Map<Integer, Author> authors = readAuthors();
			for(Book book : books) {
				restoreReferences(book, authors);
			}
			return books;
		} finally {
			try { Objects.requireNonNull(resultSet).close(); } catch(Exception ignored) {}
			try { Objects.requireNonNull(statement).close(); } catch(Exception ignored) {}
		}
	}

	public Book readById(Integer id) throws SQLException {
		String sql = "SELECT \"id\", \"title\", \"year\" FROM \"book\" WHERE \"id\" = ?";
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			statement = getConnection().prepareStatement(sql);
			statement.setInt(1, id);
			resultSet = statement.executeQuery();
			Book book = null;
			if(resultSet.next()) {
				book = parseResultSet(resultSet);
			}
			if(book != null) {
				restoreReferences(book, readAuthors());
			}
			return book;
		} finally {
			try { Objects.requireNonNull(resultSet).close(); } catch(Exception ignored) {}
			try { Objects.requireNonNull(statement).close(); } catch(Exception ignored) {}
		}
	}

	public Integer create(Book book) throws SQLException {
		String sql = "INSERT INTO \"book\" (\"title\", \"year\") VALUES (?, ?)";
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Integer id;
		try {
			statement = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			fillStatement(statement, book);
			statement.executeUpdate();
			resultSet = statement.getGeneratedKeys();
			resultSet.next();
			id = resultSet.getInt(1);
		} finally {
			try { Objects.requireNonNull(resultSet).close(); } catch(Exception ignored) {}
			try { Objects.requireNonNull(statement).close(); } catch(Exception ignored) {}
		}
		updateAuthorIds(id, book.getAuthors().stream().map(Entity::getId).collect(Collectors.toSet()), Operation.INSERT);
		return id;
	}

	public void update(Book book) throws SQLException {
		String sql = "UPDATE \"book\" SET \"title\" = ?, \"year\" = ? WHERE \"id\" = ?";
		PreparedStatement statement = null;
		try {
			statement = getConnection().prepareStatement(sql);
			fillStatement(statement, book);
			statement.setInt(3, book.getId());
			statement.executeUpdate();
		} finally {
			try { Objects.requireNonNull(statement).close(); } catch(Exception ignored) {}
		}
		Set<Integer> oldAuthorIds = readAuthorIdsByBook(book.getId());
		Set<Integer> newAuthorIds = book.getAuthors().stream().map(Entity::getId).collect(Collectors.toSet());
		if(!oldAuthorIds.equals(newAuthorIds)) {
			Set<Integer> nonChangedAuthorIds = new LinkedHashSet<>(oldAuthorIds);
			nonChangedAuthorIds.retainAll(newAuthorIds);
			Set<Integer> removingAuthorIds = new LinkedHashSet<>(oldAuthorIds);
			removingAuthorIds.removeAll(nonChangedAuthorIds);
			Set<Integer> addingAuthorIds = new LinkedHashSet<>(newAuthorIds);
			addingAuthorIds.removeAll(nonChangedAuthorIds);
			updateAuthorIds(book.getId(), removingAuthorIds, Operation.DELETE);
			updateAuthorIds(book.getId(), addingAuthorIds, Operation.INSERT);
		}
	}

	public void delete(Integer id) throws SQLException {
		String sql = "DELETE FROM \"book\" WHERE \"id\" = ?";
		PreparedStatement statement = null;
		try {
			statement = getConnection().prepareStatement(sql);
			statement.setInt(1, id);
			statement.executeUpdate();
		} finally {
			try { Objects.requireNonNull(statement).close(); } catch(Exception ignored) {}
		}
	}

	private void fillStatement(PreparedStatement statement, Book book) throws SQLException {
		statement.setString(1, book.getTitle());
		statement.setInt(2, book.getYear());
	}

	private Book parseResultSet(ResultSet resultSet) throws SQLException {
		Book book = new Book();
		book.setId(resultSet.getInt("id"));
		book.setTitle(resultSet.getString("title"));
		book.setYear(resultSet.getInt("year"));
		return book;
	}

	private void restoreReferences(Book book, Map<Integer, Author> authors) throws SQLException {
		if(authors != null) {
			Set<Integer> authorIds = readAuthorIdsByBook(book.getId());
			for(Integer id : authorIds) {
				book.getAuthors().add(authors.get(id));
			}
		}
	}

	private Set<Integer> readAuthorIdsByBook(Integer bookId) throws SQLException {
		String sql = "SELECT \"author_id\" FROM \"author_vs_book\" WHERE \"book_id\" = ?";
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			statement = getConnection().prepareStatement(sql);
			statement.setInt(1, bookId);
			resultSet = statement.executeQuery();
			Set<Integer> authorIds = new LinkedHashSet<>();
			while(resultSet.next()) {
				authorIds.add(resultSet.getInt("author_id"));
			}
			return authorIds;
		} finally {
			try { Objects.requireNonNull(resultSet).close(); } catch(Exception ignored) {}
			try { Objects.requireNonNull(statement).close(); } catch(Exception ignored) {}
		}
	}

	private enum Operation { INSERT, DELETE }

	private void updateAuthorIds(Integer bookId, Set<Integer> authorIds, Operation operation) throws SQLException {
		if(!authorIds.isEmpty()) {
			String sql;
			switch(operation) {
				case INSERT -> sql = "INSERT INTO \"author_vs_book\" (\"author_id\", \"book_id\") VALUES (?, ?)";
				case DELETE -> sql = "DELETE FROM \"author_vs_book\" WHERE \"author_id\" = ? AND \"book_id\" = ?";
				default -> throw new EnumConstantNotPresentException(Operation.class, operation.name());
			}
			PreparedStatement statement = null;
			try {
				statement = getConnection().prepareStatement(sql);
				for(Integer authorId : authorIds) {
					statement.setInt(1, authorId);
					statement.setInt(2, bookId);
					statement.executeUpdate();
				}
			} finally {
				try { Objects.requireNonNull(statement).close(); } catch(Exception ignored) {}
			}
		}
	}

	private Map<Integer, Author> readAuthors() throws SQLException {
		Map<Integer, Author> authors = new HashMap<>();
		for(Author author : authorDatabaseMapper.readAll()) {
			authors.put(author.getId(), author);
		}
		return authors;
	}
}
