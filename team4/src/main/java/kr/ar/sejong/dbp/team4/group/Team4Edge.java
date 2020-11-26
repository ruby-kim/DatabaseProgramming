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
	Team4Edge() throws SQLException{ // 생성자 , properties 넣을 때 필요
		//16011140 안재현
		properties = new HashMap<>();
		connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306" , "root" , "0000"); // 본인에 맞춰서
		stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE , ResultSet.CONCUR_UPDATABLE);
		stmt.executeUpdate("USE Team4Graph");
	}

	Team4Edge(Vertex outVertex , Vertex inVertex , String label , Team4Graph graph) throws SQLException{
		//16011140 안재현
		this.outVertex = outVertex;
		this.inVertex = inVertex;
		this.id = outVertex.getId() + "|" + label + "|" + inVertex.getId();
		this.label = label;
		this.graph = graph;
		properties = new HashMap<>();
		connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306" , "root" , "0000"); // 본인에 맞춰서
		stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE , ResultSet.CONCUR_UPDATABLE);
		stmt.executeUpdate("USE Team4Graph");


	}



	@Override
	public Object getProperty(String key) { // 이 Edge 에 properties 중 key 에 매칭되는 value 가져오기
		// 안재현 수정
		// TODO Auto-generated method stub
		try{
			ResultSet set = stmt.executeQuery("SELECT JSON_VALUE(properties, '$."+key+"') FROM Edge WHERE source = "+this.outVertex.getId()+"and"+"destination = " + this.inVertex.getId() +" and "+"label = " + this.label +";")
			set.next();
			return set.getString(1);
		}
	}



	@Override
	public Set<String> getPropertyKeys() { // 현재 edge 에 대한  properties 의 key 값들 리턴하는 함수 , outvertex,  invertex 의 id 와 label 이 맞는 edge 를 찾아 키값들 가져오기
		//안재현 구문 수정
		// TODO Auto-generated method stub
		try{
		ResultSet set = stmt.executeQuery("SELECT JSON_KEYS(properties) FROM Edge WHERE source = "+this.outVertex.getId()+"and"+"destination = " + this.inVertex.getId() +" and "+"label = " + this.label +";");
		set.next();
		String propKeys = set.getString(1); // key 값
		JSONArray arr= new JSONArray(propKeys);

		HashSet<String> returnKeys = new HashSet<String>();
		for(int i = 0 ; i < arr.length() ; i++){
			returnKeys.add(arr.getString(i));
		}
		return returnKeys;
	}catch(SQLException e){
		e.printStackTrace;
	}
	return null;

	}



	@Override
	public void setProperty(String key, Object value) { // 프로퍼티 설정하기 , 또한 엣지테이블 select 후 그것에 맞는 edge 찾아서 넣기
		// TODO Auto-generated method stub
		// 박병훈 구문 수정
		// 안재현 구문 수정( source 와 destination 을 잇는 엣지는 두개가 될 수 있음 , label 이 다를 수 있기 때문 )
		// key : value
		properties.put(key, value);
		try {
			stmt.executeUpdate("UPDATE Edge SET properties = '{\"" + key +"\":\""+ value + "\"}' "
					+ "WHERE source = "+ this.outVertex.getId() +" and " + "destination = " + this.inVertex.getId()+" and "+"label = " + this.label + ";" );

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}



	@Override
	public Object getId() {
		//16011149 안재현
		// TODO Auto-generated method stub
		return this.id;
	}



	@Override
	public Vertex getVertex(Direction direction) throws IllegalArgumentException {
		//16011140 안재현
		// TODO Auto-generated method stub
		if(direction.equals(direction.OUT)) // out 이면 outvertex return 그게 아니라면 invertex return  >>>>>> 문법 맞는지 모르겠음
			return outVertex;
		else
			return inVertex;

	}



	@Override
	public String getLabel() {
		//16011140 안재현
		// TODO Auto-generated method stub
		return this.label;
	}
    @Override	//김지수
	public String toString() {
		return "e[" +outVertex.getId()+"-"+label+"->"+inVertex.getId()+"]";
	}
}
