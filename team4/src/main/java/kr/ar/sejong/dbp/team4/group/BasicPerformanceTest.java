package kr.ar.sejong.dbp.team4.group;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Iterator;


import kr.ar.sejong.dbp.team4.Edge;
import kr.ar.sejong.dbp.team4.Vertex;
import kr.ar.sejong.dbp.team4.Graph;
import kr.ar.sejong.dbp.team4.Direction;

public class BasicPerformanceTest {
	public static void createGraph(Graph g) throws IOException {
	      BufferedReader br = new BufferedReader(new FileReader("D:\\dbp_data\\data.txt"));
	      int i = 0;
	      while (true) {
	         String line = br.readLine();
	         System.out.println(i++);
	         if (line == null)
	            break;
	         if (line.startsWith("#"))
	            continue;
	         String[] arr = line.split("\t");

	         Vertex vl = g.getVertex(arr[0]);
	         if (vl == null) {
	            vl = g.addVertex(arr[0]);
	         }
	         Vertex vr = g.getVertex(arr[1]);
	         if (vr == null) {
	            vr = g.addVertex(arr[1]);
	         }

	         g.addEdge(vl, vr, "label");
	      }
	      br.close();
	   }

	   public static void getVertices(Graph g) {
	      Iterator<Vertex> iter = g.getVertices().iterator();
	      while (iter.hasNext()) {
	         Vertex v = iter.next();
	      }
	   }

	   public static void getEdges(Graph g) {
	      Iterator<Edge> iter = g.getEdges().iterator();
	      while (iter.hasNext()) {
	         Edge v = iter.next();
	      }
	   }

	   public static void getOutEdgesFromAllV(Graph g) throws SQLException {
	      Iterator<Vertex> iter = g.getVertices().iterator();
	      while (iter.hasNext()) {
	         Vertex v = iter.next();
	         v.getEdges(Direction.OUT, "label");
	      }
	   }

	   public static void getInEdgesFromAllV(Graph g) throws SQLException {
	      Iterator<Vertex> iter = g.getVertices().iterator();
	      while (iter.hasNext()) {
	         Vertex v = iter.next();
	         v.getEdges(Direction.IN, "label");
	      }
	   }

	   public static void getOutVerticesFromAllV(Graph g) throws SQLException {
	      Iterator<Vertex> iter = g.getVertices().iterator();
	      while (iter.hasNext()) {
	         Vertex v = iter.next();
	         v.getVertices(Direction.OUT, "label");
	      }
	   }

	   public static void getInVerticesFromAllV(Graph g) throws SQLException {
	      Iterator<Vertex> iter = g.getVertices().iterator();
	      while (iter.hasNext()) {
	         Vertex v = iter.next();
	         v.getVertices(Direction.IN, "label");
	      }
	   }

	   public static void getOutVertexFromAllE(Graph g) throws IllegalArgumentException, SQLException {
	      Iterator<Edge> iter = g.getEdges().iterator();
	      while (iter.hasNext()) {
	         Edge e = iter.next();
	         e.getVertex(Direction.OUT);
	      }
	   }

	   public static void getInVertexFromAllE(Graph g) throws IllegalArgumentException, SQLException {
	      Iterator<Edge> iter = g.getEdges().iterator();
	      while (iter.hasNext()) {
	         Edge e = iter.next();
	         e.getVertex(Direction.IN);
	      }
	   }

	   public static void main(String[] args) throws IOException, SQLException {
	      Graph g = new Team4Graph();

	      long p = System.currentTimeMillis();
	      // createGraph(g);
	      System.out.println("Graph Creation (ms.): " + (System.currentTimeMillis() - p));

	      p = System.currentTimeMillis();
	      getVertices(g);
	      System.out.println("g.getVertices (ms.): " + (System.currentTimeMillis() - p));

	      p = System.currentTimeMillis();
	      getEdges(g);
	      System.out.println("g.getEdges (ms.): " + (System.currentTimeMillis() - p));

	      p = System.currentTimeMillis();
	      getOutEdgesFromAllV(g);
	      System.out.println("v.getEdges(OUT) (ms.): " + (System.currentTimeMillis() - p));

	      p = System.currentTimeMillis();
	      getInEdgesFromAllV(g);
	      System.out.println("v.getEdges(IN) (ms.): " + (System.currentTimeMillis() - p));

	      p = System.currentTimeMillis();
	      getOutVerticesFromAllV(g);
	      System.out.println("v.getVertices(OUT) (ms.): " + (System.currentTimeMillis() - p));

	      p = System.currentTimeMillis();
	      getInVerticesFromAllV(g);
	      System.out.println("v.getVertices(IN) (ms.): " + (System.currentTimeMillis() - p));

	      p = System.currentTimeMillis();
	      getOutVertexFromAllE(g);
	      System.out.println("e.getVertex(OUT) (ms.): " + (System.currentTimeMillis() - p));

	      p = System.currentTimeMillis();
	      getInVertexFromAllE(g);
	      System.out.println("e.getVertex(IN) (ms.): " + (System.currentTimeMillis() - p));

	   }
}
