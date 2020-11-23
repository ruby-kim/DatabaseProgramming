package kr.ar.sejong.dbp.team4.group;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Set;

import kr.ar.sejong.dbp.team4.Direction;
import kr.ar.sejong.dbp.team4.Edge;
import kr.ar.sejong.dbp.team4.Vertex;

public class Team4Edge implements Edge{

	private String label;
	private Vertex inVertex;
	private Vertex outVertex;
	private Team4Graph graph;
	public String id;
	private Connection connection;
	private Statement stmt;
	private ResultSet rs;
	HashMap<String  , Object> properties;
	 // 만약 해쉬맵 사용하지 못한다면 , properties 는 JSONParser 를 이용하여 파싱해서 찾아야
	Team4Edge() throws SQLException{ // 생성자 , properties 넣을 때 필요  // 안재현수정
		properties = new HashMap<>();
		connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306" , "root" , "1234"); // 본인에 맞춰서 
		stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE , ResultSet.CONCUR_UPDATABLE);
		stmt.executeUpdate("CREATE OR REPLACE DATABASE Team4Graph");
		stmt.executeUpdate("USE Team4Graph");
	}
	
	Team4Edge(Vertex outVertex , Vertex inVertex , String label , Team4Graph graph){ // 안재현수정
		this.outVertex = outVertex;
		this.inVertex = inVertex;
		this.id = outVertex.getId() + "|" + label + "|" + inVertex.getId();
		this.label = label;
		this.graph = graph;
		try {
			connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306" , "root" , "1234"); // 본인에 맞춰서 
			stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE , ResultSet.CONCUR_UPDATABLE);
			PreparedStatement pstmt = connection.prepareStatement("INSERT INTO edge VALUES(?,?,?);");
			pstmt.clearParameters();
			pstmt.setObject(1, outVertex.getId()); // set properties
			pstmt.setObject(2, inVertex.getId());
			pstmt.setString(3, label);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



	@Override
	public Object getProperty(String key) { // 안재현수정
		// TODO Auto-generated method stub
		return properties.get(key);
	}



	@Override
	public Set<String> getPropertyKeys() { // 모든 key 값들 리턴하는 함수  // 안재현수정
		// TODO Auto-generated method stub
		Set<String> keys = properties.keySet();
	
		return keys;
	}



	@Override
	public void setProperty(String key, Object value) { // 프로퍼티 설정하기 , 또한 엣지테이블 select 후 그것에 맞는 edge 찾아서 넣기 //안재현수정
		// TODO Auto-generated method stub
		
		properties.put(key, value);
		try {
			stmt.executeUpdate("UPDATE Edge SET properties = '{\"" + key +"\":\""+ value + "\"}' "
					+ "WHERE outVertex ="+ this.outVertex.getId() +"," + "inVertex = " + this.inVertex.getId() + ",label = " + this.label + ";" );
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}



	@Override
	public Object getId() { // 안재현수정
		// TODO Auto-generated method stub
		return this.id;
	}



	@Override
	public Vertex getVertex(Direction direction) throws IllegalArgumentException { //안재현수정
		// TODO Auto-generated method stub
		if(direction.equals(direction.OUT)) // out 이면 outvertex return 그게 아니라면 invertex return  >>>>>> 문법 맞는지 모르겠음 
			return outVertex;
		else
			return inVertex;
		
	}



	@Override
	public String getLabel() { // 
		// TODO Auto-generated method stub
		return this.label;
	}
	}
//	public Team4Edge(final Vertex outVertex, final Vertex inVertex, String label, Team4Graph team4Graph) throws SQLException { // 엣지 생성 
//        
//}
//	@Override
//	public Object getProperty(String key) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public Set<String> getPropertyKeys() { // 이 엣지의 키값들 배열 or JsonObject 로 변
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public void setProperty(String key, Object value) { //key : value 형식으로 된 db 접속 후 key : value 값 넣기 
//		this.properties.put(key, value);
//		
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public Object getId() {
//		// TODO Auto-generated method stub
//		return this.id;
//	}
//
//	@Override
//	public Vertex getVertex(Direction direction) throws IllegalArgumentException {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public String getLabel() {
//		// TODO Auto-generated method stub
//		return this.label;
//	}
//
//}
//// method 6
