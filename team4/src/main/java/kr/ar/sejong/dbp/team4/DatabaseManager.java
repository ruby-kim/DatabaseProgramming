package kr.ar.sejong.dbp.team4;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager { //17011654 김경남
	Connection m_connection = null;
	Statement m_stmt = null;
	PreparedStatement m_pStmt = null;
		
	public Connection getConnection() { return m_connection;}
	public Statement getStatement() { return m_stmt; }
	public PreparedStatement getPreStatement() { return m_pStmt; }
		
	// connect DB server (initialize)
	public void initialize(String port, String pswd) throws SQLException {
		m_connection = DriverManager.getConnection("jdbc:mariadb://localhost:" + port, "root", pswd);
		m_stmt = m_connection.createStatement();
		m_pStmt = m_connection.prepareStatement("");		
		
		//m_pStmt.executeUpdate("CREATE OR REPLACE DATABASE Team4Graph;");
		m_pStmt.executeUpdate("USE Team4Graph;");
		//m_pStmt.executeUpdate("CREATE OR REPLACE TABLE vertex (ID INTEGER UNIQUE PRIMARY KEY, properties JSON);");
		//m_pStmt.executeUpdate("CREATE OR REPLACE TABLE edge (source INTEGER, destination INTEGER, label VARCHAR(50), properties JSON);");	
	}
		
	// close DB server
	public void release() throws SQLException {
		if(m_stmt != null) m_stmt.close();
		if(m_connection != null) m_connection.close();
	}
		
	// singleton
	private static DatabaseManager m_instance;
	private DatabaseManager() {};
	public static DatabaseManager getInstance() {
		if(m_instance == null) 
			m_instance = new DatabaseManager();
		return m_instance;
	}
}
