package by.vsu.jcpl.menu;

import java.util.Scanner;
import java.util.Set;

public class ExitMenuItem {
	public static boolean activate() {
		Scanner console = new Scanner(System.in);
		System.out.print("\nDo you want to exit? (y/n): ");
		if(Set.of("y", "yes").contains(console.nextLine().trim().toLowerCase())) {
			System.out.println("Good bye");
			return false;
		}
		return true;
	}
}
