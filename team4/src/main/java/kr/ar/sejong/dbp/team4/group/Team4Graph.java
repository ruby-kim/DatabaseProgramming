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
import java.util.Map;


import kr.ar.sejong.dbp.team4.Edge;
import kr.ar.sejong.dbp.team4.Graph;
import kr.ar.sejong.dbp.team4.Vertex;

public class Team4Graph implements Graph{
    
    private Connection connection;
	private Statement stmt;
	private ResultSet rs;
	
	Team4Graph() throws SQLException
	{ // 박병훈 코드 수정
	  connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306" , "root" , "0000");
	  stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE , ResultSet.CONCUR_UPDATABLE);
	  stmt.executeUpdate("CREATE OR REPLACE DATABASE Team4Graph");
	  stmt.executeUpdate("USE Team4Graph");
	  stmt.executeUpdate("CREATE OR REPLACE TABLE vertex (ID INTEGER UNIQUE PRIMARY KEY, properties JSON);");   
	  stmt.executeUpdate("CREATE OR REPLACE TABLE edge (source INTEGER, destination INTEGER, label VARCHAR(50), properties JSON);");
	}
    @Override
    public Vertex addVertex(String id)
    { //박병훈 코드 수정
       try {
          PreparedStatement pstmt = connection.prepareStatement("INSERT INTO vertex (id) VALUES(?);");
          pstmt.clearParameters();
          pstmt.setInt(1, Integer.parseInt(id)); // set id
          pstmt.executeUpdate();
          Team4Vertex vertex = new Team4Vertex(Integer.parseInt(id),this);//Team4Vertex 생성자
          return (Vertex)vertex;
       }catch(Exception ex) {
          return null;
       }
    }
	@Override
	  public Vertex getVertex(String id) {
	    //박병훈 코드
	       try {// 해당 버텍스가 있는지 찾고 없다면  null을 있다면 vertex로 반환합니다.
	          ResultSet rs = stmt.executeQuery("SELECT DISTINCT id FROM vertex WHERE id = " + Integer.parseInt(id) + ";");          
	          if(rs != null)      
	             return new Team4Vertex(Integer.parseInt(id),this);
	          else
	             return null;
	       }catch(Exception ex)
	       {
	          return null;
	       }
	    }

	@Override
	public Iterable<Vertex> getVertices() {
	      //박병훈 코드 수정
	      try {// 해당 버텍스가 있는지 찾고 없다면  null을 있다면 vertex로 반환합니다.
	             ResultSet rs = stmt.executeQuery("SELECT * FROM vertex;"); 
	             if(rs == null)      
	                return null;
	             ArrayList<Vertex> arr = new ArrayList<Vertex>();
	             while(rs.next())
	             {
	            	 Team4Vertex ver = new Team4Vertex(rs.getInt(1), this);
	            	 //ver.setProperty(key, value); 프로퍼티 부분만 다시 짜보겠습니다.
	            	 arr.add(ver);
	             }  
	             return arr;
	          }catch(Exception ex)
	          {
	             return null;
	          }
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

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Edge addEdge(Vertex outVertex, Vertex inVertex, String label) {
		try {
			PreparedStatement pstmt = connection.prepareStatement("INSERT INTO edge VALUES(?,?,?,'{\"KEY1\":\"VALUE1\"}');");
			//properties는 어떻게 넣어야 할까요?
			//id값이 없는데 edges를 어케만들어야할까요..?
			//vertex에는 in,out edge 표시 안해도 될까요?
			//죄송합니다 ㅠ-ㅠ
			pstmt.clearParameters();
			pstmt.setObject(1, outVertex.getId()); // set properties
			pstmt.setObject(2, inVertex.getId());
			pstmt.setString(3, label);
			pstmt.executeUpdate();
			
			Team4Edge edge = new Team4Edge(outVertex, inVertex, label, this);
	        return edge;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Edge getEdge(Vertex outVertex, Vertex inVertex, String label) {
		return null;
	}

	@Override
	public Iterable<Edge> getEdges() {
		// return all the edges
		return null;
	}

	@Override
	public Iterable<Edge> getEdges(String key, Object value) {
		// TODO Auto-generated method stub
		return null;
	}
}
