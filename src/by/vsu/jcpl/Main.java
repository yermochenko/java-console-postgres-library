package by.vsu.jcpl;

import by.vsu.jcpl.menu.*;
import by.vsu.jcpl.orm.AuthorDatabaseMapper;

import java.sql.*;
import java.util.*;

public class Main {
	public static void main(String[] args) {
		Connection connection = null;
		try {
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/book_catalog_db", "root", "root");
			System.out.println("Connecting to database establish successfully");
			AuthorDatabaseMapper authorDatabaseMapper = new AuthorDatabaseMapper(connection);
			List<MenuItem> menuItems = new ArrayList<>();
			menuItems.add(new AuthorListMenuItem("Reading of authors list", authorDatabaseMapper));
			menuItems.add(new AuthorAddMenuItem("Adding of new author", authorDatabaseMapper));
			menuItems.add(new AuthorEditMenuItem("Updating information about author", authorDatabaseMapper));
			menuItems.add(new AuthorDeleteMenuItem("Deleting information about author", authorDatabaseMapper));
			menuItems.add(new ExitMenuItem("Exit"));
			int width = menuItems.stream().map(item -> item.title().length()).max(Integer::compareTo).get();
			String delimiter = "+" + String.join("", Collections.nCopies(width + 5, "-")) + "+";
			Scanner console = new Scanner(System.in);
			boolean work = true;
			while(work) {
				System.out.println(delimiter);
				System.out.printf("| %-" + (width + 4) + "s|\n", "MENU");
				System.out.println(delimiter);
				int n = 1;
				for(MenuItem menuItem : menuItems) {
					System.out.printf("| %d) %-" + width + "s |\n", n++, menuItem.title());
				}
				System.out.println(delimiter);
				System.out.print("\nEnter menu item number: ");
				try {
					work = menuItems.get(Integer.parseInt(console.nextLine()) - 1).activate();
					System.out.println("\n******************************\n");
				} catch(IndexOutOfBoundsException | NumberFormatException e) {
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
