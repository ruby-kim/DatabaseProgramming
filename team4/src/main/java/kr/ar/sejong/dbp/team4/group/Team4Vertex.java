package kr.ar.sejong.dbp.team4.group;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.json.JSONArray;

import kr.ar.sejong.dbp.team4.Direction;
import kr.ar.sejong.dbp.team4.Edge;
import kr.ar.sejong.dbp.team4.Vertex;

public class Team4Vertex implements Vertex {
    
	private Connection connection;
	private Statement stmt;
	private ResultSet rs;
	private Team4Graph graph;
    //예: string 형태의 고유 아이디, '|' 사용 금지
    private int id;
    private String property = null;

    //15011137 김지수
    Team4Vertex(final int id,final Team4Graph graph) throws SQLException{
		this.id = id;
		this.graph = graph;
		
		connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306" , "root" , "0000");
		stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE , ResultSet.CONCUR_UPDATABLE);
		stmt.executeUpdate("USE Team4Graph");
	}
    
    //15011137 김지수
    @Override
    public Iterable<Edge> getEdges(Direction direction, String... labels) {
    	if(direction.equals(Direction.OUT)) {
    		return this.getOutEdges(labels);
    	} else if(direction.equals(Direction.IN)) {
    			return this.getInEdges(labels);
    	}
        return null;
    }
    
    //15011137 김지수
    private Iterable<Edge> getInEdges(String... labels){
    	List<Edge> totalEdges = new ArrayList<Edge>();	
    	String label;
    	try {
			ResultSet set = stmt.executeQuery("SELECT * FROM edge WHERE destination = "+this.id+" AND label = \"label\";");
			while(set.next()) {
				Vertex sVertex = new Team4Vertex(set.getInt(1),graph);
				Vertex dVertex = new Team4Vertex(set.getInt(2),graph);
				label = set.getString(3);
				Edge newEdge = new Team4Edge(sVertex,dVertex,label,graph);
				totalEdges.add(newEdge);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return totalEdges;
    }
    
    //15011137 김지수
    private Iterable<Edge> getOutEdges(String... labels){
    	List<Edge> totalEdges = new ArrayList<Edge>();
    	String label;
    	try {
			ResultSet set = stmt.executeQuery("SELECT * FROM edge WHERE source = "+this.id+" AND label = \"label\";");
			while(set.next()) {
				Vertex sVertex = new Team4Vertex(set.getInt(1),graph);
				Vertex dVertex = new Team4Vertex(set.getInt(2),graph);
				label = set.getString(3);
				Edge newEdge = new Team4Edge(sVertex,dVertex,label,graph);
				totalEdges.add(newEdge);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return totalEdges;
    }
    
    //15011137 김지수
    @Override
    public Iterable<Vertex> getVertices(Direction direction, String... labels) {
    	List<Vertex> totalVertices = new ArrayList<Vertex>();
    	
    	int newId;
    	if(direction.equals(Direction.OUT)) {
			try {
				ResultSet set = stmt.executeQuery("SELECT * FROM edge WHERE source = "+this.id+" AND label = \"label\";");
				while(set.next()) {
					newId = set.getInt(2);
					Vertex newVertex = new Team4Vertex(newId, graph);
					totalVertices.add(newVertex);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}else {
    		try {
				ResultSet set = stmt.executeQuery("SELECT * FROM edge WHERE destination = "+this.id+" AND label = \"label\";");
				while(set.next()) {
					newId = set.getInt(1);
					Vertex newVertex = new Team4Vertex(newId, graph);
					totalVertices.add(newVertex);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	
    	 
        return totalVertices;
    }

    //15011137 김지수
    @Override
    public Edge addEdge(String label, Vertex inVertex) {
        return this.graph.addEdge(this, inVertex, label);
    }

    //15011137 김지수
    @Override
    public Object getProperty(String key) {
    	try{
    		ResultSet set = stmt.executeQuery("SELECT JSON_VALUE(properties, '$."+key+"') FROM Vertex WHERE id = "+this.id+";");
    		set.next();
    		property = set.getString(1);
    		return property;
    	} catch(Exception e){
    		return null;
    	}
    }

    //15011137 김지수
    @Override
    public Set<String> getPropertyKeys() {
    	try {
			ResultSet set = stmt.executeQuery("SELECT JSON_KEYS(properties) FROM Vertex WHERE id = "+this.id+";");
			set.next();
			String propKeys = set.getString(1);
			JSONArray arr = new JSONArray(propKeys);
			
			HashSet<String> returnKeys = new HashSet<String>();
			
			for(int i=0;i<arr.length();i++) {
				returnKeys.add(arr.getString(i));
			}
			return returnKeys;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
        return null;
    }
    
	// '{"key":"value"}'
    //15011137 김지수
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
    			set.next();
    			property = set.getString(1);
    			property = property.substring(1,property.length()-1);	
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
    		//value가 String 이다.
        	if(value instanceof String) {
        		try {
        		stmt.executeUpdate("UPDATE vertex SET properties = '{"+property+",\"" + key +"\":\""+ value + "\"}' WHERE ID ="+ this.id +";" );
        		}
        		catch(Exception e){
        			return;
        		}
        		}
        	// value가 숫자형.
        	else {
        		try {
            		stmt.executeUpdate("UPDATE vertex SET properties = '{"+property+",\"" + key +"\":"+ value + "}' WHERE ID ="+ this.id +";" );
            		}
            		catch(Exception e){
            			return;
            		}
        	}
    	}
    }

    //15011137 김지수
    @Override
    public Object getId() {
        return id;
    }
    
    //15011137 김지수
    @Override
	public String toString() {
		return "v[" + id + "]";
	}
}
