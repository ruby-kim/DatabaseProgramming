package kr.ar.sejong.dbp.team4.group;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.json.JSONArray;

import kr.ar.sejong.dbp.team4.Direction;
import kr.ar.sejong.dbp.team4.Edge;
import kr.ar.sejong.dbp.team4.Vertex;

public class Team4Vertex implements Vertex {
    
	private Statement stmt;
	private Team4Graph graph;
    //예: string 형태의 고유 아이디, '|' 사용 금지
    private int id;
    private String property = null;
    private Connection conn;
    
    Team4Vertex(final int id,final Team4Graph graph) throws SQLException{
        // 15011137 김지수
    	// 17011654 김경남
    	// 16011189 양승주
		this.id = id;
		this.graph = graph;
		stmt = graph.stmt;
		conn = graph.connection; //속도 향상을 위해 stmt -> pstmt
	}
    
    @Override
    public Iterable<Edge> getEdges(Direction direction, String... labels) {
        // 15011137 김지수
    	// 16011189 양승주
    	List<Edge> totalEdges = new ArrayList<Edge>();	
    	String label=labels[0];
    	String sql=null; 
    	
    	if(direction.equals(Direction.OUT)) {
    		sql = "SELECT * FROM edge WHERE source = ? AND label = ?;";
    	} else if(direction.equals(Direction.IN)) {
    		sql = "SELECT * FROM edge WHERE destination = ? AND label = ?;";
    	}
        
		try {
			PreparedStatement pstmt;
			pstmt = conn.prepareStatement(sql);
			pstmt.clearParameters();
			pstmt.setInt(1, this.id);
			pstmt.setString(2, label);
			ResultSet set = pstmt.executeQuery();
			ResultSetMetaData rsmd = set.getMetaData();
			set.setFetchSize(rsmd.getColumnCount());
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
    
    
    @Override
    public Iterable<Vertex> getVertices(Direction direction, String... labels) {
        // 15011137 김지수
    	// 16011189 양승주
    	List<Vertex> totalVertices = new ArrayList<Vertex>();
    	String label=labels[0];
    	String sql=null; 
    	int newId;
    	int op; 
    	
    	if(direction.equals(Direction.OUT)) {
    		sql = "SELECT source, destination FROM edge WHERE source = ? AND label = ?;";
        	op = 2;
    	}	
    	else {
    		sql = "SELECT source, destination FROM edge WHERE destination = ? AND label = ?;";
    		op = 1;
    	} 
    	try {
    		PreparedStatement pstmt;
			pstmt = conn.prepareStatement(sql);
			pstmt.clearParameters();
			pstmt.setInt(1, this.id);
			pstmt.setString(2, label);
			ResultSet set = pstmt.executeQuery();
			ResultSetMetaData rsmd = set.getMetaData();
			set.setFetchSize(rsmd.getColumnCount());
			while(set.next()) {
				newId = set.getInt(op);
				Vertex newVertex = new Team4Vertex(newId, graph);
				totalVertices.add(newVertex);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
    	
    	return totalVertices;
    }

    @Override
    public Edge addEdge(String label, Vertex inVertex) {
        // 15011137 김지수
        return this.graph.addEdge(this, inVertex, label);
    }

    @Override
    public Object getProperty(String key) {
        // 15011137 김지수
    	try{
    		PreparedStatement pstmt = conn.prepareStatement(
					"SELECT JSON_VALUE(properties, '$.?') FROM Vertex WHERE id = ?;");
			pstmt.clearParameters();
			pstmt.setString(1, key);
			pstmt.setString(2, key);
			ResultSet rset = pstmt.executeQuery();
    		rset.next();
    		property = rset.getString(1);
    		return property;
    	} catch(Exception e){
    		return null;
    	}
    }

    @Override
    public Set<String> getPropertyKeys() {
        // 15011137 김지수
    	try {
    		PreparedStatement pstmt = conn.prepareStatement(
					"SELECT JSON_KEYS(properties) FROM Vertex WHERE id = "+this.id+";");
			ResultSet set = pstmt.executeQuery();
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
    @Override
    public void setProperty(String key, Object value) {    	
        // 15011137 김지수
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

    @Override
    public Object getId() {
        // 15011137 김지수
        return id;
    }
    
    @Override
	public String toString() {
        // 15011137 김지수
    	// 16011176 박병훈
		return "v[" + id + "]";
	}
}
