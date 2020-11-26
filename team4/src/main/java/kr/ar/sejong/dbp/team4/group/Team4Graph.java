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

public class Team4Graph implements Graph{
    
   private Connection connection;
   private Statement stmt;
   private ResultSet rs;
   
   Team4Graph() throws SQLException
   { // 박병훈 코드 수정
     connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306" , "root" , "zpfldj");
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
          pstmt.setInt(1, Integer.parseInt(id));
          pstmt.executeUpdate();
          Vertex vertex = new Team4Vertex(Integer.parseInt(id),this);//Team4Vertex 생성자
          return vertex;
       }catch(Exception ex) {
          return null;
       }
    }
   @Override
     public Vertex getVertex(String id) { // 임시 edge 가 다 구현되었을때 다시 짜겠습니다.
       //박병훈 코드
          try {// 해당 버텍스가 있는지 찾고 없다면  null을 있다면 vertex로 반환합니다.
             ResultSet rs1 = stmt.executeQuery("SELECT * FROM vertex where id = " + Integer.parseInt(id) + ";");
             
             if(rs1.next() == false)
            	 return null;
             
             Vertex ver = new Team4Vertex(Integer.parseInt(id), this); // 버텍스 만들기
             return ver;             
          }catch(Exception ex)
          {
             return null;
          }
       }
   
   @Override
   public Iterable<Vertex> getVertices() {
         //박병훈 코드 수정
         try {// 데베를 사용하여 버텍스들을 가져온후 어레이리스트로 반환합니다.
                ResultSet rs = stmt.executeQuery("SELECT id FROM vertex;"); 
                ArrayList<Vertex> arr = new ArrayList<Vertex>();
                while(rs.next())
                {
                	Team4Vertex ver = new Team4Vertex(rs.getInt(1), this);
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
         ResultSet rset =  pstmt.executeQuery();
         
         ArrayList<Vertex> arr = new ArrayList<Vertex>();
         
         Team4Vertex ver = new Team4Vertex(rset.getInt(1), this);
         arr.add(ver);

      } catch (SQLException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      return null;
   }

   @Override
   public Edge addEdge(Vertex outVertex, Vertex inVertex, String label) {
	   try {
    	  PreparedStatement pstmt = connection.prepareStatement("INSERT INTO edge(source,destination,label) VALUES(?,?,?);");
    	  pstmt.clearParameters();
    	  pstmt.setObject(1, outVertex.getId()); // set properties
    	  pstmt.setObject(2, inVertex.getId());
    	  pstmt.setString(3, label);
    	  pstmt.executeUpdate();

    	  Edge edge = new Team4Edge(outVertex, inVertex, label, this);
          return edge;
      } catch (Exception e) {
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
