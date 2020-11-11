// 마지막 수정일: 11월 7일
package kr.ar.sejong.dbp.team4.group;

import java.util.Iterator;

import kr.ar.sejong.dbp.team4.Edge;
import kr.ar.sejong.dbp.team4.Graph;
import kr.ar.sejong.dbp.team4.Vertex;

public class Team4Graph implements Graph{
	
	/* 
	 * 여기서 그래프 만드는건가?그런가?맞겠지?그렇지?
	 * CREATE TABLE Vertex(id integer);
	 * CREATE TABLE Edge(startID integer, endID integer); // 여기서 vertex 테이블 참조하면 되지 않을까..
	 */
	
	public class Vertex {
		private String id;
		private Vertex next; // 연결리스트의 다음 vertex로 링크
		
		private Vertex(String id) {
			this.id = id;
		}
	}
	
	public class Edge {
		private Vertex startID;
		private Vertex endID;
		private String label;
		private Edge next; // 연결리스트의 다음 edge로 링크
		
		private Edge(Vertex start, Vertex end) {
			this.startID = start;
			this.endID = end;
		}
	}
	
	
	// 그래프 클래스 메소드 구현
	private Vertex vertices;
	private Edge edges;
	
	
	// 밑에 오버라이딩 함수들 빨간줄 뜨는데 이유를 모르겠다 완전간단한 이유같은데 모르겠다 이거 지금 빨간줄뜨는게 정상인가??
	@Override
	public Vertex addVertex(String id) {
		// INSERT INTO Vertex VALUES (id);
		
		Vertex v = new Vertex(id);
		v.next = vertices;
		vertices = v;
		
		return v;
	}

	@Override
	public Vertex getVertex(String id) {
		// SELECT * FROM Vertex WHERE Vertex.id = id;
		
		Vertex v = vertices;
		while (v != null && !v.id.contentEquals(id))
			v = v.next;
		
		return v;
	}

	@Override
	public Iterable<Vertex> getVertices() {
		// SELECT * FROM Vertex;
		
		// 뭘로 반환하는거지 교수님 정녕 자바만이 답입니까 일단패스
		return null;
	}

	@Override
	public Iterable<Vertex> getVertices(String key, Object value) {
		// 이것도 패스
		return null;
	}

	@Override
	public Edge addEdge(Vertex outVertex, Vertex inVertex, String label) {
		// INSERT INTO Edge VALUES (outVertex.id, inVertex.id, label);
		
		Vertex start = getVertex(outVertex);
		if (start == null) //자바는 null 소문자. c언어도 사실 가능한건가 나중에 확인해봐야지
			start = addVertex(outVertex);
		
		Vertex end = getVertex(inVertex);
		if (end == null)
			end = addVertex(inVertex);
		
		Edge e = new Edge(start, end, label);
		e.next = edges; // 연결리스트 맨 앞에 삽입
		edges = e;
		
		return e;
	}

	@Override
	public Edge getEdge(Vertex outVertex, Vertex inVertex, String label) {
		// SELECT * FROM Edge WHERE (outVectex.id == startID && inVertex.id == endID && (edge.)label == label);
		
		Edge e = edges;
		while (e != null) {
			e = e.next;
			if (outVertex.id == e.startID && invertex.id == e.endID && (edge.)label == e.label) {
				// 자바에서는 스트링값을 어케처리하는지 검색해보기
				break;
			}
		}
		return e;
	}

	@Override
	public Iterable<Edge> getEdges() {
		// SELECT * FROM Edges;
		// 이것도 뭘 반환해야하지 교수님 해협
		return null;
	}

	@Override
	public Iterable<Edge> getEdges(String key, Object value) {
		// TODO Auto-generated method stub
		return null;
	}

}
