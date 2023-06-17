package by.vsu.jcpl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AuthorDatabaseMapper {
	private final Connection connection;

	public AuthorDatabaseMapper(Connection connection) {
		this.connection = connection;
	}

	public List<Author> readAll() throws SQLException {
		String sql = "SELECT \"id\", \"surname\", \"name\", \"birth_year\", \"death_year\" FROM \"author\"";
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			resultSet = statement.executeQuery(sql);
			List<Author> authors = new ArrayList<>();
			while(resultSet.next()) {
				Author author = new Author();
				author.setId(resultSet.getInt("id"));
				author.setSurname(resultSet.getString("surname"));
				author.setName(resultSet.getString("name"));
				author.setBirthYear(resultSet.getInt("birth_year"));
				Integer deathYear = resultSet.getInt("death_year");
				if(!resultSet.wasNull()) {
					author.setDeathYear(deathYear);
				}
				authors.add(author);
			}
			return authors;
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
			statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, author.getName());
			statement.setString(2, author.getSurname());
			statement.setInt(3, author.getBirthYear());
			if(author.getDeathYear() != null) {
				statement.setInt(4, author.getDeathYear());
			} else {
				statement.setNull(4, Types.INTEGER);
			}
			statement.executeUpdate();
			resultSet = statement.getGeneratedKeys(); // receiving of all generated primary keys
			resultSet.next();
			return resultSet.getInt(1);
		} finally {
			try { Objects.requireNonNull(resultSet).close(); } catch(Exception ignored) {}
			try { Objects.requireNonNull(statement).close(); } catch(Exception ignored) {}
		}
	}
}
