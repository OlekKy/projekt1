package pl.olekky.projekt1.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import pl.olekky.projekt1.model.Player;
import pl.olekky.projekt1.repository.exception.DatabaseException;

@Repository
public class PlayerRepository {

	static final String JDBC_DRIVER = "org.h2.Driver";
	static final String DB_URL = "jdbc:h2:~/test";

	static final String USER = "sa";
	static final String PASS = "";

	public void intializedPlayerTable() throws DatabaseException {
		Connection connection = null;
		Statement statement = null;

		// Class.forName(JDBC_DRIVER);
		try {
			connection = connect();
			statement = connection.createStatement();
			String sql = "CREATE TABLE PLAYER" + "   (ID INT PRIMARY KEY," + "   NICKNAME VARCHAR(255) ,"
					+ "   GOLD INT)";
			statement.executeUpdate(sql);

		} catch (SQLException e) {

			throw new DatabaseException("creation table failed", e);
		} finally {
			close(connection, statement);
		}

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

	public List<Player> getPlayers() throws DatabaseException {
		Connection connection = null;
		Statement statement = null;
		List<Player> playerList = new ArrayList<Player>();
		try {
			connection = connect();
			statement = connection.createStatement();
			String sql = "select * from Player";
			ResultSet resultSet = statement.executeQuery(sql);

			while (resultSet.next()) {
				String nickname = resultSet.getString("nickname");
				int id = resultSet.getInt("id");
				int gold = resultSet.getInt("gold");
				Player player = new Player(id, nickname, gold);
				playerList.add(player);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DatabaseException("getting player info failed", e);
		} finally {
			close(connection, statement);
		}
		return playerList;
	}

	public Player getPlayer(int id) throws DatabaseException {
		Connection connection = null;
		Statement statement = null;
		Player player = null;
		try {
			connection = connect();
			statement = connection.createStatement();
			String sql = "select * from Player where id =" + id;
			ResultSet resultSet = statement.executeQuery(sql);
			if (resultSet.next()) {
				String nickname = resultSet.getString("nickname");
				int idFromDatabase = resultSet.getInt("id");
				int gold = resultSet.getInt("gold");
				player = new Player(idFromDatabase, nickname, gold);
			} else {
				throw new DatabaseException("player " + id + " dosen exits");
			}
		} catch (SQLException e) {
			throw new DatabaseException("getting player info failed", e);
		} finally {
			close(connection, statement);
		}
		return player;
	}

	public int addPlayer(Player player) throws DatabaseException {
		Connection connection = null;
		PreparedStatement statement = null;
		
			try {
			connection = connect();
			String sql = "insert into Player (nickname,gold) values(?,?)";
			statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			
			statement.setString(1, player.getNickname());
			statement.setInt(2, player.getGold());
			statement.executeUpdate();
			ResultSet generatedKeys = statement.getGeneratedKeys();
			if (generatedKeys.next()) {
				player.setId(generatedKeys.getInt(1));
			}
			
		} catch (SQLException e) {
			throw new DatabaseException("insert player failed", e);
		} finally {
			close(connection, statement);
		}
		return player.getId();
	}

}
