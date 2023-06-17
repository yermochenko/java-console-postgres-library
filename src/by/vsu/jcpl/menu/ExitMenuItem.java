package by.vsu.jcpl.menu;

import java.util.Set;

public class ExitMenuItem implements MenuItem {
	@Override
	public boolean activate() {
		System.out.print("\nDo you want to exit? (y/n): ");
		if(Set.of("y", "yes").contains(console.nextLine().trim().toLowerCase())) {
			System.out.println("Good bye");
			return false;
		}
		return true;
	}
}
