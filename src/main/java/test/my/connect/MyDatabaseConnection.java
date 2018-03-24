package test.my.connect;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component("myLiveDatabaseConnection")
public class MyDatabaseConnection implements MyConnection {

	private static Connection connection = null;

	@Autowired
	@Qualifier("myDataSource")
	private DataSource datasource;

	public Connection getStaticConnection() {

		try {
			if (connection == null || connection.isClosed()) {

				connection = datasource.getConnection();
			}
			return connection;

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return connection;

	}

	@Override
	public Connection getConnection() {
		return getStaticConnection();
	}

}
