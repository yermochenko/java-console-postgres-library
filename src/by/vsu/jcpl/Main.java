package by.vsu.jcpl;

import java.sql.*;
import java.util.*;

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
							List<Author> authors = new AuthorDatabaseMapper(connection).readAll();
							for(Author author : authors) {
								System.out.printf("[%04d] %s %s ", author.getId(), author.getName(), author.getSurname());
								if(author.getDeathYear() != null) {
									System.out.printf("(%d - %d)\n", author.getBirthYear(), author.getDeathYear());
								} else {
									System.out.printf("(%d - ...)\n", author.getBirthYear());
								}
							}
						}
						case 2 -> {
							System.out.println("\n==<[ ADDING OF NEW AUTHOR ]>==\n");
							Author author = new Author();
							System.out.print("Enter author's name: ");
							String name = console.nextLine();
							if(!name.isBlank()) {
								author.setName(name);
							} else {
								throw new EntityValidationException("Name shouldn't be empty");
							}
							System.out.print("Enter author's surname: ");
							String surname = console.nextLine();
							if(!surname.isBlank()) {
								author.setSurname(surname);
							} else {
								throw new EntityValidationException("Surname shouldn't be empty");
							}
							System.out.print("Enter author's birth year: ");
							String birthYear = console.nextLine();
							try {
								author.setBirthYear(Integer.parseInt(birthYear));
							} catch(NumberFormatException e) {
								throw new EntityValidationException("Birth year shouldn't be empty and should be integer");
							}
							System.out.print("Enter author's death year if author is dead or press \"Enter\" if author is alive: ");
							String deathYear = console.nextLine();
							if(!deathYear.isBlank()) {
								try {
									author.setDeathYear(Integer.valueOf(deathYear));
								} catch(NumberFormatException e) {
									throw new EntityValidationException("Death year should be integer");
								}
							}
							if(author.getDeathYear() != null && author.getBirthYear() > author.getDeathYear()) {
								throw new EntityValidationException("Birth year should be earlier than death year");
							}
							Integer id = new AuthorDatabaseMapper(connection).create(author);
							System.out.printf("Author successfully added with identifier [%04d]\n", id);
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
				} catch(EntityValidationException e) {
					System.out.println(e.getMessage());
				} catch(SQLException e) {
					System.out.println("Error working with database");
					e.printStackTrace();
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
