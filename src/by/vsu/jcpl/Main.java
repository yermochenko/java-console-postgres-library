package by.vsu.jcpl;

import java.sql.*;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;

public class Main {
	public static void main(String[] args) {
		Connection connection = null;
		try {
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/book_catalog_db", "root", "root");
			System.out.println("Connecting to database establish successfully");
			Scanner console = new Scanner(System.in);
			boolean work = true;
			while(work) {
				System.out.println("+----------------------------+");
				System.out.println("|            MENU            |");
				System.out.println("+----------------------------+");
				System.out.println("| 1) Reading of authors list |");
				System.out.println("| 2) Adding of new author    |");
				System.out.println("| 3) Exit                    |");
				System.out.println("+----------------------------+");
				System.out.print("\nEnter menu item number: ");
				try {
					int menuItem = Integer.parseInt(console.nextLine());
					switch(menuItem) {
						case 1 -> {
							System.out.println("\n==<[ Authors ]>==\n");
							Statement statement = null;
							ResultSet resultSet = null;
							try {
								statement = connection.createStatement();
								resultSet = statement.executeQuery("SELECT \"id\", \"surname\", \"name\", \"birth_year\", \"death_year\" FROM \"author\"");
								while(resultSet.next()) {
									Integer id = resultSet.getInt("id");
									String surname = resultSet.getString("surname");
									String name = resultSet.getString("name");
									Integer birthYear = resultSet.getInt("birth_year");
									Integer deathYear = resultSet.getInt("death_year");
									if(resultSet.wasNull()) {
										deathYear = null;
									}
									System.out.printf("[%04d] %s %s ", id, name, surname);
									if(deathYear != null) {
										System.out.printf("(%d - %d)\n", birthYear, deathYear);
									} else {
										System.out.printf("(%d - ...)\n", birthYear);
									}
								}
							} catch(SQLException e) {
								System.out.println("Error working with database");
								e.printStackTrace();
							} finally {
								try { Objects.requireNonNull(resultSet).close(); } catch(Exception ignored) {}
								try { Objects.requireNonNull(statement).close(); } catch(Exception ignored) {}
							}
						}
						case 2 -> {
							System.out.println("\n==<[ ADDING OF NEW AUTHOR ]>==\n");
							/* Prepared statement allow to substitute necessary data into SQL query */
							PreparedStatement statement = null;
							ResultSet resultSet = null;
							try {
								statement = connection.prepareStatement("INSERT INTO \"author\" (\"name\", \"surname\", \"birth_year\", \"death_year\") VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
								System.out.print("Enter author's name: ");
								String name = console.nextLine();
								if(name.isBlank()) {
									System.out.println("Name shouldn't be empty");
									break;
								}
								System.out.print("Enter author's surname: ");
								String surname = console.nextLine();
								if(surname.isBlank()) {
									System.out.println("Surname shouldn't be empty");
									break;
								}
								System.out.print("Enter author's birth year: ");
								int birthYear;
								try {
									birthYear = Integer.parseInt(console.nextLine());
								} catch(NumberFormatException e) {
									System.out.println("Birth year shouldn't be empty and should be integer");
									break;
								}
								System.out.print("Enter author's death year if author is dead or press \"Enter\" if author is alive: ");
								Integer deathYear = null;
								String deathYearStr = console.nextLine();
								if(!deathYearStr.isBlank()) {
									try {
										deathYear = Integer.valueOf(deathYearStr);
									} catch(NumberFormatException e) {
										System.out.println("Death year should be integer");
										break;
									}
								}
								if(deathYear != null && birthYear > deathYear) {
									System.out.println("Birth year should be earlier than death year");
									break;
								}
								/* Substitution of necessary data into the prepared statement */
								statement.setString(1, name);
								statement.setString(2, surname);
								statement.setInt(3, birthYear);
								if(deathYear != null) {
									statement.setInt(4, deathYear);
								} else {
									statement.setNull(4, Types.INTEGER);
								}
								statement.executeUpdate();
								resultSet = statement.getGeneratedKeys(); // receiving of all generated primary keys
								resultSet.next();
								System.out.printf("Author successfully added with identifier [%04d]\n", resultSet.getInt(1));
							} catch(SQLException e) {
								System.out.println("Error working with database");
								e.printStackTrace();
							} finally {
								try { Objects.requireNonNull(resultSet).close(); } catch(Exception ignored) {}
								try { Objects.requireNonNull(statement).close(); } catch(Exception ignored) {}
							}
						}
						case 3 -> {
							System.out.print("\nDo you want to exit? (y/n): ");
							if(Set.of("y", "yes").contains(console.nextLine().trim().toLowerCase())) {
								System.out.println("Good bye");
								work = false;
							}
						}
						default -> throw new IllegalArgumentException();
					}
					System.out.println("\n******************************\n");
				} catch(IllegalArgumentException e) {
					System.out.println("Incorrect menu item number");
				}
			}
		} catch(ClassNotFoundException e) {
			System.out.println("Couldn't load the database driver");
			e.printStackTrace();
		} catch(SQLException e) {
			System.out.println("Couldn't connect to the database");
			e.printStackTrace();
		} finally {
			try { Objects.requireNonNull(connection).close(); } catch(Exception ignored) {}
		}
	}
}
