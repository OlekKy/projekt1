package pl.olekky.projekt1.repository.exception;

import java.sql.SQLException;

public class DatabaseException extends Exception {

	public DatabaseException(String message, SQLException e) {
		super(message,e);
	}
	public DatabaseException(String message) {
		super(message);
	}
	public DatabaseException(SQLException e) {
		super(e);
	}
}
