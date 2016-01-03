package task3;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DB {
	public static final String HOST = "jdbc:postgresql://localhost:5432/";
	public static final String DB_NAME = "task3";
	public static final String USERNAME = "postgres";
	public static final String PASSWORD = "root";
	private Connection connection;

	private PreparedStatement update_film_actor, select_id_from_filmname, select_id_from_actorname, update_actor,
			update_film;

	public DB() throws SQLException {
		super();
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {

			System.out.println("Where is your PostgreSQL JDBC Driver? " + "Include in your library path!");
			e.printStackTrace();
			return;
		}
		try {

			connection = DriverManager.getConnection(HOST + DB_NAME, USERNAME, PASSWORD);
		} catch (SQLException e) {

			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return;

		}
		if (connection != null) {

		} else {
			System.out.println("Failed to make connection!");
		}
		// create prepared statements
		String sql = "SELECT film_id FROM film WHERE film_name=?";
		select_id_from_filmname = connection.prepareStatement(sql);
		sql = "SELECT actor_id FROM actor WHERE actor_name= ? ";
		select_id_from_actorname = connection.prepareStatement(sql);
		sql = "INSERT INTO actor (actor_name,description) SELECT    ?  ,  ? WHERE    NOT EXISTS (SELECT actor_name,description FROM actor WHERE actor_name= ?  AND description= ? );";
		update_actor = connection.prepareStatement(sql);
		sql = "INSERT INTO film_actor (actor_id,film_id)  SELECT   ?  ,  ?	WHERE    NOT EXISTS (SELECT actor_id,film_id FROM film_actor WHERE actor_id=  ? AND film_id= ? );";
		update_film_actor = connection.prepareStatement(sql);
		sql = "INSERT INTO film (film_name,film_year) SELECT  ?,? WHERE   NOT EXISTS (SELECT film_name,film_year FROM film WHERE film_name= ? AND film_year=  ?  );";
		update_film = connection.prepareStatement(sql);
		// end of prepared statements 
	}

	public void save_actor(String name, String description) throws SQLException {
		
		update_actor.setString(1, name);
		update_actor.setString(2, description);
		update_actor.setString(3, name);
		update_actor.setString(4, description);
		update_actor.executeUpdate();
	}

	public void add_film_actor_conection(String film_name, String actor_name) throws SQLException {

		int film_id = 0, actor_id = 0;
		
		select_id_from_filmname.setString(1, film_name);
		ResultSet rs = select_id_from_filmname.executeQuery();
		while (rs.next()) {
			film_id = rs.getInt(1);
		}
	
		select_id_from_actorname.setString(1, actor_name);
		rs = select_id_from_actorname.executeQuery();
		while (rs.next()) {
			actor_id = rs.getInt(1);
		}
		rs.close();

		update_film_actor.setInt(1, actor_id);
		update_film_actor.setInt(2, film_id);
		update_film_actor.setInt(3, actor_id);
		update_film_actor.setInt(4, film_id);

		update_film_actor.executeUpdate();

	}

	public void save_film(String name, String year) throws SQLException {
		
		update_film.setString(1, name);
		update_film.setString(2, year);
		update_film.setString(3, name);
		update_film.setString(4, year);
		update_film.executeUpdate();
	}
}
