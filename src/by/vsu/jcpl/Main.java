package by.vsu.jcpl;

import by.vsu.jcpl.menu.*;

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
				System.out.println("+--------------------------------------+");
				System.out.println("|                 MENU                 |");
				System.out.println("+--------------------------------------+");
				System.out.println("| 1) Reading of authors list           |");
				System.out.println("| 2) Adding of new author              |");
				System.out.println("| 3) Updating information about author |");
				System.out.println("| 4) Deleting information about author |");
				System.out.println("| 5) Exit                              |");
				System.out.println("+--------------------------------------+");
				System.out.print("\nEnter menu item number: ");
				try {
					int menuItem = Integer.parseInt(console.nextLine());
					switch(menuItem) {
						case 1 -> new AuthorListMenuItem(new AuthorDatabaseMapper(connection)).activate();
						case 2 -> new AuthorAddMenuItem(new AuthorDatabaseMapper(connection)).activate();
						case 3 -> new AuthorEditMenuItem(new AuthorDatabaseMapper(connection)).activate();
						case 4 -> new AuthorDeleteMenuItem(new AuthorDatabaseMapper(connection)).activate();
						case 5 -> work = new ExitMenuItem().activate();
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
