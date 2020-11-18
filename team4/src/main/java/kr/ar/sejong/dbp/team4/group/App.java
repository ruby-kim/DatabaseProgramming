package kr.ar.sejong.dbp.team4.group;

import java.sql.SQLException;

import kr.ar.sejong.dbp.team4.Vertex;

public class App {

	public static void main(String[] args) throws SQLException {
		Team4Graph g = new Team4Graph();
		// vertex 0 추가
		Vertex v00 = g.addVertex("0");
		System.out.println(v00);
		g.addVertex("1");
		g.addVertex("2");
		g.addVertex("3");
		g.addVertex("4");
		
		System.out.println(g.getVertices());
		System.out.println(g.getVertex("1"));
	}

}
