package kr.ar.sejong.dbp.team4.group;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import kr.ar.sejong.dbp.team4.Edge;
import kr.ar.sejong.dbp.team4.Graph;
import kr.ar.sejong.dbp.team4.Vertex;

public class Team4Graph implements Graph{

    protected HashMap<String, Team4Vertex> vertices = new HashMap<String, Team4Vertex>();
    protected HashMap<String, Team4Edge> edges = new HashMap<>();
    
    private Connection connection;
	private Statement stmt;
	private ResultSet rs;
	
	Team4Graph() throws SQLException
	{
		this.vertices = new HashMap<String, Team4Vertex>();
		this.edges = new HashMap<String, Team4Edge>();
		connection = DriverManager.getConnection("jdbc:mariadb://localhost:3307" , "root" , "ahffk232");
		stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE , ResultSet.CONCUR_UPDATABLE);
		stmt.executeUpdate("CREATE OR REPLACE DATABASE Team4Graph");
		stmt.executeUpdate("USE Team4Graph");
		stmt.executeUpdate("CREATE OR REPLACE TABLE vertex (ID INTEGER UNIQUE PRIMARY KEY, properties JSON);");	
		stmt.executeUpdate("CREATE OR REPLACE TABLE edge (source INTEGER, destination INTEGER, label VARCHAR(50), properties JSON);");
	}
    @Override
    public Vertex addVertex(String id)
    {
    	try {
    		PreparedStatement pstmt = connection.prepareStatement("INSERT INTO vertex VALUES(?,?);");
    		pstmt.clearParameters();
    		pstmt.setInt(1, Integer.parseInt(id)); // set id
    		pstmt.setString(2, "{\"none\": \"none\"}"); // set properties
    		pstmt.executeUpdate();
    		Team4Vertex vertex = new Team4Vertex(Integer.parseInt(id),this);//Team4Vertex 생성자
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
		//"name":"yang"
		// key value 맞는 Vertex 반환
		try {//, JSON_VALUE(properties,'$.?')
			//2번 접근 ?? key->value
			PreparedStatement pstmt = connection.prepareStatement("SELECT JSON_VALUE(properties,'$.?') FROM vertex;");
			pstmt.clearParameters();
			pstmt.setString(1, (String) value); // set properties

    		
			Team4Vertex vertex;
    		ResultSet rset =  pstmt.executeQuery();
    		vertices = new HashMap<String, Team4Vertex>();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Edge addEdge(Vertex outVertex, Vertex inVertex, String label) {
		//PreparedStatement pstmt = connection.prepareStatement("INSERT INTO edge VALUES(?,?,?,'{\"KEY1\":\"VALUE1\"}');"); // 마지막은 jsonobject 
		//properties는 어떻게 넣어야 할까요?
		//id값이 없는데 edges를 어케만들어야할까요..?
		//vertex에는 in,out edge 표시 안해도 될까요?
		//죄송합니다 ㅠ-ㅠ
		Team4Edge newEdge = new Team4Edge(outVertex , inVertex , label , this); // 새로 edge 만들기
		String Edge_id = outVertex.getId()+ "|" + label + "|" +inVertex.getId();
		edges.put(Edge_id, newEdge);
		return newEdge;
	}

	@Override
	public Edge getEdge(Vertex outVertex, Vertex inVertex, String label) {
		
		return edges.get(outVertex.getId()+ "|" + label + "|" +inVertex.getId()); // 이러한 EdgeId 를 가지는 Edge 찾아서 반환
		
	
//		return (Edge)edges.get(outVertex.getId().toString());
	}

	

	@Override
	public Iterable<Edge> getEdges(String key, Object value) { // key:value 와 매칭되는 propertiy 를 가진 Edge 들 Iterabnle 객체로 반환하기
		List<Edge> egs = new ArrayList<>();
		for(Team4Edge edge : edges.values()) {
			if(edge.properties.get(key) == value) {
				egs.add(edge);
			}
		}
		Iterator<Edge> e = egs.iterator();
		// TODO Auto-generated method stub
		return (Iterable<Edge>) e;
	}
	@Override
	public Iterable<Edge> getEdges() { // 이 그래프에 연결되어있는 모든 Edges Iterable 객체로 반환 
		
		// TODO Auto-generated method stub
		return null;
	}
}
