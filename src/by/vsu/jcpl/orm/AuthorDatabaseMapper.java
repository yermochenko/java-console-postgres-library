package by.vsu.jcpl.orm;

import by.vsu.jcpl.domain.Author;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AuthorDatabaseMapper extends EntityDatabaseMapper {
	public AuthorDatabaseMapper(Connection connection) {
		super(connection);
	}

	public List<Author> readAll() throws SQLException {
		String sql = "SELECT \"id\", \"surname\", \"name\", \"birth_year\", \"death_year\" FROM \"author\"";
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = getConnection().createStatement();
			resultSet = statement.executeQuery(sql);
			List<Author> authors = new ArrayList<>();
			while(resultSet.next()) {
				authors.add(parseResultSet(resultSet));
			}
			return authors;
		} finally {
			try { Objects.requireNonNull(resultSet).close(); } catch(Exception ignored) {}
			try { Objects.requireNonNull(statement).close(); } catch(Exception ignored) {}
		}
	}

	public Author readById(Integer id) throws SQLException {
		String sql = "SELECT \"id\", \"surname\", \"name\", \"birth_year\", \"death_year\" FROM \"author\" WHERE \"id\" = ?";
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			statement = getConnection().prepareStatement(sql);
			statement.setInt(1, id);
			resultSet = statement.executeQuery();
			if(resultSet.next()) {
				return parseResultSet(resultSet);
			} else {
				return null;
			}
		} finally {
			try { Objects.requireNonNull(resultSet).close(); } catch(Exception ignored) {}
			try { Objects.requireNonNull(statement).close(); } catch(Exception ignored) {}
		}
	}

	public Integer create(Author author) throws SQLException {
		String sql = "INSERT INTO \"author\" (\"name\", \"surname\", \"birth_year\", \"death_year\") VALUES (?, ?, ?, ?)";
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			statement = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			fillStatement(statement, author);
			statement.executeUpdate();
			resultSet = statement.getGeneratedKeys(); // receiving of all generated primary keys
			resultSet.next();
			return resultSet.getInt(1);
		} finally {
			try { Objects.requireNonNull(resultSet).close(); } catch(Exception ignored) {}
			try { Objects.requireNonNull(statement).close(); } catch(Exception ignored) {}
		}
	}

	public void update(Author author) throws SQLException {
		String sql = "UPDATE \"author\" SET \"name\" = ?, \"surname\" = ?, \"birth_year\" = ?, \"death_year\" = ? WHERE \"id\" = ?";
		PreparedStatement statement = null;
		try {
			statement = getConnection().prepareStatement(sql);
			fillStatement(statement, author);
			statement.setInt(5, author.getId());
			statement.executeUpdate();
		} finally {
			try { Objects.requireNonNull(statement).close(); } catch(Exception ignored) {}
		}
	}

	public void delete(Integer id) throws SQLException {
		String sql = "DELETE FROM \"author\" WHERE \"id\" = ?";
		PreparedStatement statement = null;
		try {
			statement = getConnection().prepareStatement(sql);
			statement.setInt(1, id);
			statement.executeUpdate();
		} finally {
			try { Objects.requireNonNull(statement).close(); } catch(Exception ignored) {}
		}
	}

	private void fillStatement(PreparedStatement statement, Author author) throws SQLException {
		statement.setString(1, author.getName());
		statement.setString(2, author.getSurname());
		statement.setInt(3, author.getBirthYear());
		if(author.getDeathYear() != null) {
			statement.setInt(4, author.getDeathYear());
		} else {
			statement.setNull(4, Types.INTEGER);
		}
	}

	private Author parseResultSet(ResultSet resultSet) throws SQLException {
		Author author = new Author();
		author.setId(resultSet.getInt("id"));
		author.setSurname(resultSet.getString("surname"));
		author.setName(resultSet.getString("name"));
		author.setBirthYear(resultSet.getInt("birth_year"));
		Integer deathYear = resultSet.getInt("death_year");
		if(!resultSet.wasNull()) {
			author.setDeathYear(deathYear);
		}
		return author;
	}
}
