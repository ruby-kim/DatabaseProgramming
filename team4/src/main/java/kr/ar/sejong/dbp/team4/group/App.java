
package kr.ar.sejong.dbp.team4.group;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import kr.ar.sejong.dbp.team4.Vertex;

public class App {
	public static void main(String[] args) throws SQLException {
		Team4Graph g = new Team4Graph();

		Vertex v00 = g.addVertex("0");
		System.out.println(v00);
		g.addVertex("1");
		g.addVertex("2");
		g.addVertex("3");
		g.addVertex("4");
		
		System.out.println(g.getVertices());
		System.out.println(g.getVertex("1"));
//		System.out.println(g.getVertices("none","2"));
//		System.out.println(v00.getId() instanceof String);
		String[] fruitsArray = {"apple", "banana", "kiwi", "mango", "blackberry"};
		ArrayList<String>  fruits = new ArrayList<>(Arrays.asList(fruitsArray));

		fruits.forEach(item -> System.out.println("item : " + item));
		System.out.println(v00.getClass().getFields().length);
//		while(v00.getId().)
		Vertex v1 = g.addVertex("5");
//		Vertex v2 = g.addVertex("6");
//		Vertex v3 = g.addVertex("7");
		
		v1.setProperty("x", 300);
		v1.setProperty("y", 200);
		v1.setProperty("name", "James");
		v1.setProperty("Date", "2020-11-02");
		
		System.out.println(v1.getPropertyKeys());
//		System.out.println(g.addEdge(v00, v1, "test1"));
//		System.out.println(g.addEdge(v2, v3, "test2"));
//		System.out.println(g.getEdges());
//		
//		System.out.println("get"+g.getEdge(v00, v1, "test1"));
	}

}
