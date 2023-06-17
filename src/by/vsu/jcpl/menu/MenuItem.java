package by.vsu.jcpl.menu;

import by.vsu.jcpl.EntityValidationException;

import java.sql.SQLException;
import java.util.Scanner;

public interface MenuItem {
	Scanner console = new Scanner(System.in);

	boolean activate() throws SQLException, EntityValidationException;
}
