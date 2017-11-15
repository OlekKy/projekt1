package pl.olekky.projekt1.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.naming.spi.DirStateFactory.Result;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Repository;

import pl.olekky.projekt1.model.Item;
import pl.olekky.projekt1.model.Player;
import pl.olekky.projekt1.repository.exception.DatabaseException;

@Repository
public class PlayerRepository {

	static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";

	static final String USER = "postgres";
	static final String PASS = "admin";

	public void intializedPlayerTable() throws DatabaseException {
		Connection connection = null;
		Statement statement = null;

		try {
			connection = connect();
			statement = connection.createStatement();
			String sql = "CREATE TABLE PLAYERS" + 
						"   (ID INT PRIMARY KEY," +
						"   NICKNAME VARCHAR(255) ,"
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
			connection = connect(false);
			statement = connection.createStatement();
			String sql = "select * from Players order by id";
			ResultSet resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				Player player = resultToPlayer(resultSet);
				playerList.add(player);
			}
			statement.close();

			for (Player player : playerList)
				player.setItems(getItemsByPlayerId(player.getId(), connection));
			connection.commit();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new DatabaseException("getting player info failed", e);
		} finally {
			close(connection, statement);
		}
		return playerList;
	}

	private Player resultToPlayer(ResultSet resultSet) throws SQLException {
		String nickname = resultSet.getString("nickname");
		int id = resultSet.getInt("id");
		int gold = resultSet.getInt("gold");
		int xFromDatabase = resultSet.getInt("x");
		int yFromDatabase = resultSet.getInt("y");
		// Player player = new Player(id, nickname, gold);
		Player player = new Player();
		player.setId(id);
		player.setGold(gold);
		player.setNickname(nickname);
		player.setX(xFromDatabase);
		player.setY(yFromDatabase);
		return player;
	}

	public Player getPlayer(int id) throws DatabaseException {
		Connection connection = null;
		PreparedStatement statement = null;
		Player player = null;
		// List<Item> items = new ArrayList<Item>();
		try {
			connection = connect(false);
			String sql = "select * from Players where id = ?";
			statement = connection.prepareStatement(sql);
			statement.setInt(1, id);

			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				player = resultToPlayer(resultSet);

			} else {
				throw new DatabaseException("player " + id + " dosen exits");
			}
			statement.close();

			player.setItems(getItemsByPlayerId(id, connection));
			connection.commit();
		} catch (SQLException e) {
			throw new DatabaseException("getting player info failed", e);
		} finally {
			close(connection, statement);
		}
		return player;
	}

	private List<Item> getItemsByPlayerId(int id, Connection connection) throws SQLException {
		PreparedStatement statement;
		String sql;
		List<Item> items = new ArrayList<Item>();

		sql = "select * from players_items join items on items.id = players_items.item_id where players_items.player_id = ?";
		statement = connection.prepareStatement(sql);
		statement.setInt(1, id);
		ResultSet resultSet2 = statement.executeQuery();
		while (resultSet2.next()) {
			Item item = resultSetToItem(resultSet2);
			items.add(item);
		}
		return items;
	}

	private Item resultSetToItem(ResultSet resultSet) throws SQLException {
		String name = resultSet.getString("name");
		int item_id = resultSet.getInt("item_id");
		int attack = resultSet.getInt("attack");
		int defense = resultSet.getInt("defense");
		Item item = new Item(item_id, name, attack, defense);
		return item;
	}

	public int addPlayer(Player player) throws DatabaseException {
		Connection connection = null;
		PreparedStatement statement = null;

		try {
			connection = connect();
			String sql = "insert into Players (nickname,gold) values(?,?)";
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

	public Player updateNickname(int id, String nickname) throws DatabaseException {
		Connection connection = null;
		PreparedStatement statement = null;
		Player player = null;
		try {
			connection = connect(false);
			String sql = "update Players set nickname= ? where id=?";
			statement = connection.prepareStatement(sql);

			statement.setString(1, nickname);
			statement.setInt(2, id);
			statement.executeUpdate();
			statement.close();

			sql = "select * from Players where id = ?";
			statement = connection.prepareStatement(sql);
			statement.setInt(1, id);
			statement.execute();
			ResultSet resultSet = statement.getResultSet();
			if (resultSet.next()) {
				player = resultToPlayer(resultSet);

			} else {
				throw new DatabaseException("player " + id + " dosen exits");
			}

			connection.commit();

		} catch (SQLException e) {
			throw new DatabaseException("update player's nickname failed", e);
		} finally {
			try {
				connection.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			close(connection, statement);
		}

		return player;

	}

	private Connection connect(boolean transactional) throws SQLException {
		Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
		connection.setAutoCommit(transactional);
		return connection;
	}

	public void deleteNickname(int id) throws DatabaseException {
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = connect();
			String sql = "delete from Players where id =?";
			statement = connection.prepareStatement(sql);
			statement.setInt(1, id);
			statement.executeUpdate();

		} catch (SQLException e) {
			throw new DatabaseException("delete player failed", e);
		} finally {
			close(connection, statement);
		}
	}

	public Player changeY(int id, int y) throws DatabaseException {
		Connection connection = null;
		PreparedStatement statement = null;
		Player player = null;
		try {
			connection = connect(false);
			String sql = "select y from Players where id=?";
			statement = connection.prepareStatement(sql);

			statement.setInt(1, id);
			statement.executeQuery();
			ResultSet resultSet = statement.getResultSet();
			if (resultSet.next()) {
				int xFromDatabase = resultSet.getInt("y");
				if ((Math.abs(xFromDatabase - y)) != 1)
					throw new DatabaseException("Pole jest poza twoim zasiegiem lub na nim stoisz");
			}
			statement.close();
			sql = "update Players set y= ? where id=?";
			statement = connection.prepareStatement(sql);
			statement.setInt(1, y);
			statement.setInt(2, id);
			statement.executeUpdate();

			statement.close();
			sql = "select * from Players where id = ?";
			statement = connection.prepareStatement(sql);
			statement.setInt(1, id);
			statement.execute();
			resultSet = statement.getResultSet();
			if (resultSet.next()) {
				player = resultToPlayer(resultSet);

			} else {
				throw new DatabaseException("player " + id + " dosen exits");
			}

			connection.commit();

		} catch (SQLException e) {
			throw new DatabaseException("update player's position y failed", e);
		} finally {
			try {
				connection.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			close(connection, statement);
		}
		return player;
	}
	public Player changeX(int id, int x) throws DatabaseException {
		Connection connection = null;
		PreparedStatement statement = null;
		Player player = null;
		try {
			connection = connect(false);
			String sql = "select x from Players where id=?";
			statement = connection.prepareStatement(sql);

			statement.setInt(1, id);
			statement.executeQuery();
			ResultSet resultSet = statement.getResultSet();
			if (resultSet.next()) {
				int xFromDatabase = resultSet.getInt("x");
				if ((Math.abs(xFromDatabase - x)) != 1)
					throw new DatabaseException("Pole jest poza twoim zasiegiem lub na nim stoisz");
			}
			statement.close();
			sql = "update Players set x= ? where id=?";
			statement = connection.prepareStatement(sql);
			statement.setInt(1, x);
			statement.setInt(2, id);
			statement.executeUpdate();

			statement.close();
			sql = "select * from Players where id = ?";
			statement = connection.prepareStatement(sql);
			statement.setInt(1, id);
			statement.execute();
			resultSet = statement.getResultSet();
			if (resultSet.next()) {
				player = resultToPlayer(resultSet);

			} else {
				throw new DatabaseException("player " + id + " dosen exits");
			}

			connection.commit();

		} catch (SQLException e) {
			throw new DatabaseException("update player's position x failed", e);
		} finally {
			try {
				connection.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			close(connection, statement);
		}
		return player;
	}
}
