package pl.olekky.projekt1.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.stereotype.Repository;

import pl.olekky.projekt1.model.Item;
import pl.olekky.projekt1.model.Player;
import pl.olekky.projekt1.repository.exception.DatabaseException;

@Repository
public class ItemRepository {

	static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";

	static final String USER = "postgres";
	static final String PASS = "admin";

	public int addItem(Item item) throws DatabaseException {
		Connection connection = null;
		PreparedStatement statement = null;

		try {
			connection = connect();
			String sql = "insert into items (name, attack, defense) values (?,?,?)";
			statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			statement.setString(1, item.getName());
			statement.setInt(2, item.getAttack());
			statement.setInt(3, item.getDefense());

			statement.executeUpdate();
			ResultSet generatedKeys = statement.getGeneratedKeys();
			if (generatedKeys.next()) {
				item.setId(generatedKeys.getInt(1));
			}

		} catch (SQLException e) {
			throw new DatabaseException("insert item failed", e);
		} finally {
			close(connection, statement);
		}
		return item.getId();
	}

	private void close(Connection connection, Statement statement) {
		try {
			statement.close();
			connection.close();
		} catch (SQLException e) {
		}
	}

	private Connection connect() throws SQLException {
		return DriverManager.getConnection(DB_URL, USER, PASS);
	}
}
