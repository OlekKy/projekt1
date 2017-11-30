package pl.olekky.projekt1.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import pl.olekky.projekt1.model.Field;
import pl.olekky.projekt1.model.Player;
import pl.olekky.projekt1.repository.exception.DatabaseException;

@Repository
public class FieldRepository {

	static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";

	static final String USER = "postgres";
	static final String PASS = "admin";
	
	public List<Field> getFields() throws DatabaseException {
		
		Connection connection = null;
		Statement statement = null;
		List<Field> fieldList = new ArrayList<Field>();
		try {
			connection = connect();
			statement = connection.createStatement();
			String sql = "select * from fields";
			ResultSet resultSet = statement.executeQuery(sql);
			
			while (resultSet.next()) {
				Field field = resultToField(resultSet);
				fieldList.add(field);
			}
			statement.close();


		} catch (SQLException e) {
			e.printStackTrace();
			throw new DatabaseException("getting field info failed", e);
		} finally {
			close(connection, statement);
		}
		return fieldList;
		
	}
	
	private Field resultToField(ResultSet resultSet) throws SQLException {
		
		int x = resultSet.getInt("x");
		int y = resultSet.getInt("y");
		int fieldTypeId = resultSet.getInt("field_type_id");
		Field field = new Field();
		field.setX(x);
		field.setY(y);
		field.setFieldTypeId(fieldTypeId);
		return field;
	}

	private Connection connect() throws SQLException {
		return DriverManager.getConnection(DB_URL, USER, PASS);
	}
	
	private void close(Connection connection, Statement statement) {
		try {
			statement.close();
			connection.close();
		} catch (SQLException e) {
		}
	}
}
