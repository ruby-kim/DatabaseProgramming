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
import java.util.Iterator;
import java.util.Map;

import org.json.JSONObject;

import kr.ar.sejong.dbp.team4.Edge;
import kr.ar.sejong.dbp.team4.Graph;
import kr.ar.sejong.dbp.team4.Vertex;

public class Team4Graph implements Graph {

	private Connection connection;
	private Statement stmt;
	private ResultSet rs;

	Team4Graph() throws SQLException { 
		//16011176 박병훈
		connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306", "root", "0000");
		stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		stmt.executeUpdate("CREATE OR REPLACE DATABASE Team4Graph");
		stmt.executeUpdate("USE Team4Graph");
		stmt.executeUpdate("CREATE OR REPLACE TABLE vertex (ID INTEGER UNIQUE PRIMARY KEY, properties JSON);");
		stmt.executeUpdate(
				"CREATE OR REPLACE TABLE edge (source INTEGER, destination INTEGER, label VARCHAR(50), properties JSON);");
	}

	@Override
	public Vertex addVertex(String id) {
		//16011176 박병훈
		try {
			PreparedStatement pstmt = connection.prepareStatement("INSERT INTO vertex (id) VALUES(?);");
			pstmt.clearParameters();
			pstmt.setInt(1, Integer.parseInt(id));
			pstmt.executeUpdate();
			Team4Vertex vertex = new Team4Vertex(Integer.parseInt(id), this);// Team4Vertex 생성자
			return (Vertex) vertex;
		} catch (Exception ex) {
			return null;
		}
	}

	@Override
	public Vertex getVertex(String id) {
		// 16011176 박병훈
		try {// 해당 버텍스가 있는지 찾고 없다면 null을 있다면 vertex로 반환합니다.
			ResultSet rs1 = stmt.executeQuery("SELECT * FROM vertex where id = " + Integer.parseInt(id) + ";");

			if (rs1.next() == false)
				return null;

			Team4Vertex ver = new Team4Vertex(Integer.parseInt(id), this); // 버텍스 만들기
			return ver;
		} catch (Exception ex) {
			return null;
		}
	}

	@Override
	public Iterable<Vertex> getVertices() {
		// 16011176 박병훈
		try {// 데베를 사용하여 버텍스들을 가져온후 어레이리스트로 반환합니다.
			ResultSet rs = stmt.executeQuery("SELECT id FROM vertex;");
			ArrayList<Vertex> arr = new ArrayList<Vertex>();
			while (rs.next()) {
				Team4Vertex ver = new Team4Vertex(rs.getInt(1), this);
				arr.add(ver);
			}
			return arr;
		} catch (Exception ex) {
			return null;
		}
	}

	@Override
	public Iterable<Vertex> getVertices(String key, Object value) {
		//16011189 양승주
		//16011176 박병훈 
		try {
			ResultSet rset = stmt.executeQuery(
					"SELECT id FROM vertex where " + "JSON_VALUE(properties,'$." + key + "') = " + value + "");
			ArrayList<Vertex> arr = new ArrayList<Vertex>();
			while (rset.next()) {
				Team4Vertex ver = new Team4Vertex(rset.getInt(1), this);
				arr.add(ver);
			}
			return arr;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;

	}

	@Override
	public Edge addEdge(Vertex outVertex, Vertex inVertex, String label) {
		//16011189 양승주
		//15011137 김지수
		try {
			PreparedStatement pstmt = connection
					.prepareStatement("INSERT INTO edge(source,destination,label) VALUES(?,?,?);");
			pstmt.clearParameters();
			pstmt.setObject(1, outVertex.getId()); // set properties
			pstmt.setObject(2, inVertex.getId());
			pstmt.setString(3, label);
			pstmt.executeUpdate();

			Edge edge = new Team4Edge(outVertex, inVertex, label, this);
			return edge;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Edge getEdge(Vertex outVertex, Vertex inVertex, String label) {
		//15011137 김지수
		//17011654 김경남
		//16011189 양승주
		try{
		ResultSet rs1 = stmt.executeQuery("SELECT * FROM edge WHERE source = " + outVertex.getId() +" AND destination = " + inVertex.getId()+ " AND label = "+label+ ";");
		if (rs1.next() == false)
			return null; // 일치하는 것이 없으면 null 반환
		Edge edge = new Team4Edge(outVertex , inVertex , label , this);
		return edge;
		}catch (Exception ex) {
			return null;
		}
		
	}

	@Override
	public Iterable<Edge> getEdges() {
		// 16011176 박병훈
		try {// DB를 사용하여 edge들을 가져온후 어레이리스트로 반환합니다.
			ResultSet rs = stmt.executeQuery("SELECT * FROM edge;");
			ArrayList<Edge> arr = new ArrayList<Edge>();
			while (rs.next()) {
				Team4Vertex outvertex = new Team4Vertex(rs.getInt(1), this);
				Team4Vertex invertex = new Team4Vertex(rs.getInt(2), this);
				Team4Edge edge = new Team4Edge(outvertex, invertex, rs.getString(3), null);
				JSONObject jval = new JSONObject(rs.getString(4)); // json object 이용
				Iterator<String> keys = jval.keys();
				while (keys.hasNext()) {
					String key = (String) keys.next();
					edge.setProperty(key, jval.get(key));
				}
				arr.add(edge);
			}
			return arr;
		} catch (Exception ex) {
			return null;
		}
	}

	@Override
	public Iterable<Edge> getEdges(String key, Object value) {
		//16011176 박병훈
		try {
			ResultSet rs = stmt.executeQuery(
					"SELECT source, destination, label FROM edge where " 
							+ "JSON_VALUE(properties,'$." + key + "') = " + value + "");
			ArrayList<Edge> arr = new ArrayList<Edge>();
			while (rs.next()) {
				Team4Vertex outvertex = new Team4Vertex(rs.getInt(1), this);
				Team4Vertex invertex = new Team4Vertex(rs.getInt(2), this);
				Team4Edge edge = new Team4Edge(outvertex, invertex, rs.getString(3), null);
				edge.setProperty(key, value);
				arr.add(edge);
			}
			return arr;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
