package kr.ar.sejong.dbp.team4.group;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import kr.ar.sejong.dbp.team4.Direction;
import kr.ar.sejong.dbp.team4.Edge;
import kr.ar.sejong.dbp.team4.Graph;
import kr.ar.sejong.dbp.team4.Vertex;

public class Team4Vertex implements Vertex {
    
	private Connection connection;
	private Statement stmt;
	private ResultSet rs;
	private Team4Graph graph;
	
    //예: string 형태의 고유 아이디, '|' 사용 금지
    private int id;

    Team4Vertex(final int id,final Team4Graph graph) throws SQLException{
		this.id = id;
		this.graph = graph;
		
		connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306" , "root" , "0000");
		stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE , ResultSet.CONCUR_UPDATABLE);

	}
    
    @Override
    public Iterable<Edge> getEdges(Direction direction, String... labels) {
        return null;
    }

    @Override
    public Iterable<Vertex> getVertices(Direction direction, String... labels) {
        return null;
    }

    @Override
    public Edge addEdge(String label, Vertex inVertex) {
        return this.graph.addEdge(this, inVertex, label);
    }

    @Override
    public Object getProperty(String key) {
        return null;
    }

    @Override
    public Set<String> getPropertyKeys() {
        return null;
    }

    @Override
    public void setProperty(String key, Object value) {
    	
    }

    @Override
    public Object getId() {
        return id;
    }

	@Override
	public String toString() {
		return "[" + id + "]";
	}
}