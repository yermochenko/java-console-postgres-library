package by.vsu.jcpl.menu;

abstract public class NamedMenuItem implements MenuItem {
	private final String title;

	protected NamedMenuItem(String title) {
		this.title = title;
	}

	@Override
	public final String title() {
		return title;
	}
}
