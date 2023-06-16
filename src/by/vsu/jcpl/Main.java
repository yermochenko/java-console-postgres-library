package by.vsu.jcpl;

import java.sql.*;
import java.util.Objects;

public class Main {
	public static void main(String[] args) {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			/*
			 * Loading driver into memory of JVM
			 * File "lib/postgresql-42.6.0.jar" should be added into classpath
			 */
			Class.forName("org.postgresql.Driver");
			/*
			 * Connecting to database server
			 * Here
			 * 127.0.0.1 is IP address of database server. You can run server on remote computer and use IP address
			 *           of this remote computer. Or you can run server on your local computer. In this case special
			 *           IP address 127.0.0.1 used that shows you connect locally to the same computer where your
			 *           Java-application run
			 * 5432      is number of port used by database server. Value 5432 is default for PostgreSQL server.
			 * root      is username and password of user who have permissions to connect and work with database
			 */
			connection = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/book_catalog_db", "root", "root");
			/* Creating of statement object. This object allow to execute SQL queries */
			statement = connection.createStatement();
			/* Execution of SELECT query and receiving result set object. This object allow to read query result */
			resultSet = statement.executeQuery("SELECT \"name\", \"surname\", \"birth_year\", \"death_year\" FROM \"author\"");
			/* Loop process each row of result set */
			while(resultSet.next()) {
				/* Reading values of each column by its name. Choosing different methods allow to choose necessary data type */
				String name = resultSet.getString("name");
				String surname = resultSet.getString("surname");
				Integer birthYear = resultSet.getInt("birth_year");
				Integer deathYear = resultSet.getInt("death_year");
				/*
				 * Method wasNull() checks if null value has been received during last operation of column value reading.
				 * If yes in this case method getInt() return value 0 but not null because of returned type is "int"
				 * (primitive value-based type) but not "Integer" (reference-based type).
				 */
				if(resultSet.wasNull()) {
					deathYear = null;
				}
				System.out.print(name + ' ' + surname + ' ');
				if(deathYear != null) {
					System.out.printf("(%d - %d)\n", birthYear, deathYear);
				} else {
					System.out.printf("(%d - ...)\n", birthYear);
				}
			}
		} catch(ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			/*
			 * It's very important while working with database to close all used resources. That's why closing of
			 * each resource (connection, statement and result set) doing in separately try-catch block.
			 * Block catch is empty because it's nothing to do if error while closing resource happen.
			 */
			try { Objects.requireNonNull(resultSet).close(); } catch(Exception ignored) {}
			try { Objects.requireNonNull(statement).close(); } catch(Exception ignored) {}
			try { Objects.requireNonNull(connection).close(); } catch(Exception ignored) {}
		}
	}
}
