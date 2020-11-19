package kr.ar.sejong.dbp.team4.group;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import kr.ar.sejong.dbp.team4.Direction;
import kr.ar.sejong.dbp.team4.Edge;
import kr.ar.sejong.dbp.team4.Vertex;

public class Team4Vertex implements Vertex {
    
	private Connection connection;
	private Statement stmt;
	private ResultSet rs;
	private Team4Graph graph;
	private PreparedStatement pstmt;
    //예: string 형태의 고유 아이디, '|' 사용 금지
    private int id;
    private String property = null;

    Team4Vertex(final int id,final Team4Graph graph) throws SQLException{
		this.id = id;
		this.graph = graph;
		
		connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306" , "root" , "1234");
		stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE , ResultSet.CONCUR_UPDATABLE);
		stmt.executeUpdate("USE Team4Graph");
		pstmt = connection.prepareStatement("INSERT INTO vertex VALUES (?,?);");
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
    	try{
    		return stmt.executeQuery("SELECT JSON_VALUE(properties, $."+key+") FROM Vertex WHERE id = "+this.id+";");
    	} catch(Exception e){
    		return null;
    	}
    }

    @Override
    public Set<String> getPropertyKeys() {
        return null;
    }
    
	// '{"key":"value"}'
    @Override
    public void setProperty(String key, Object value) {    	
    	//property가 비어있으면 선언
    	if (property == null) {
    		property = new String();
    		
    		//value가 String 이다.
        	if(value instanceof String) {
        		try {
        		stmt.executeUpdate("UPDATE vertex SET properties = '{\"" + key +"\":\""+ value + "\"}' WHERE ID ="+ this.id +";" );
        		}
        		catch(Exception e){
        			return;
        		}
        		}
        	// value가 숫자형.
        	else {
        		try {
            		stmt.executeUpdate("UPDATE vertex SET properties = '{\"" + key +"\":"+ value + "}' WHERE ID ="+ this.id +";" );
            		}
            		catch(Exception e){
            			return;
            		}
        	}
    	}
    	// 해당 vertex에 기존에 저장된 property가 있다
    	else {
    		try {
    			ResultSet set = stmt.executeQuery("SELECT properties from vertex WHERE ID = "+this.id+";");	
    			property = set.getString(1);
    			property = property.substring(1,property.length()-1);	
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
    		//value가 String 이다.
        	if(value instanceof String) {
        		try {
        		stmt.executeUpdate("UPDATE vertex SET properties = '{"+property+"\"" + key +"\":\""+ value + "\"}' WHERE ID ="+ this.id +";" );
        		}
        		catch(Exception e){
        			return;
        		}
        		}
        	// value가 숫자형.
        	else {
        		try {
            		stmt.executeUpdate("UPDATE vertex SET properties = '{"+property+"\"" + key +"\":"+ value + "}' WHERE ID ="+ this.id +";" );
            		}
            		catch(Exception e){
            			return;
            		}
        	}
    	}
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
