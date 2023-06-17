package by.vsu.jcpl.orm;

import java.sql.Connection;

abstract public class EntityDatabaseMapper {
	private final Connection connection;

	protected EntityDatabaseMapper(Connection connection) {
		this.connection = connection;
	}

	protected Connection getConnection() {
		return connection;
	}
}
