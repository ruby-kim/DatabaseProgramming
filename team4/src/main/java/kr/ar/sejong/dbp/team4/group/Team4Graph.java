package kr.ar.sejong.dbp.team4.group;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import kr.ar.sejong.dbp.team4.Edge;
import kr.ar.sejong.dbp.team4.Graph;
import kr.ar.sejong.dbp.team4.Vertex;

public class Team4Graph implements Graph{

    protected Map<String, Team4Vertex> vertices = new HashMap<String, Team4Vertex>();
    protected Map<String, Team4Edge> edges = new HashMap<String, Team4Edge>();
    
    private Connection connection;
	private Statement stmt;
	private ResultSet rs;
	
	Team4Graph() throws SQLException
	{
		this.vertices = new HashMap<String, Team4Vertex>();
		this.edges = new HashMap<String, Team4Edge>();
		connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306" , "root" , "0000");
		stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE , ResultSet.CONCUR_UPDATABLE);
		stmt.executeUpdate("CREATE OR REPLACE DATABASE Team4Graph");
		stmt.executeUpdate("USE Team4Graph");
		stmt.executeUpdate("CREATE OR REPLACE TABLE vertex (ID INTEGER UNIQUE PRIMARY KEY, properties JSON);");	
		stmt.executeUpdate("CREATE OR REPLACE TABLE edge (source INTEGER, destination INTEGER, label VARCHAR(50), properties JSON);");
	}
    @Override
    public Vertex addVertex(String id) // 인터페이스를 좀 변경가능한가?
    {
    	try {
    		PreparedStatement pstmt = connection.prepareStatement("INSERT INTO vertex VALUES(?,?);");
    		pstmt.clearParameters();
    		pstmt.setInt(1, Integer.parseInt(id));
    		pstmt.setString(2, "{\"none\": \"none\"}");
    		pstmt.executeUpdate();
    		Team4Vertex vertex = new Team4Vertex(Integer.parseInt(id),this);
    		vertices.put(id, vertex);
    		return (Vertex)vertex;
    	}catch(Exception ex) {
    		return null;
    	}
    }

	@Override
	public Vertex getVertex(String id) {
		return (Vertex)vertices.get(id);
	}

	@Override
	public Iterable<Vertex> getVertices() {
		return new ArrayList<Vertex>(vertices.values());
	}

	@Override
	public Iterable<Vertex> getVertices(String key, Object value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Edge addEdge(Vertex outVertex, Vertex inVertex, String label) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Edge getEdge(Vertex outVertex, Vertex inVertex, String label) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<Edge> getEdges() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<Edge> getEdges(String key, Object value) {
		// TODO Auto-generated method stub
		return null;
	}
}
