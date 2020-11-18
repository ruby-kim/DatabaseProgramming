package kr.ar.sejong.dbp.team4.group;

import java.util.Set;

import kr.ar.sejong.dbp.team4.Direction;
import kr.ar.sejong.dbp.team4.Edge;
import kr.ar.sejong.dbp.team4.Vertex;

public class Team4Edge implements Edge{

    private final String label;
    private final Vertex inVertex;
    private final Vertex outVertex;
    private Team4Graph graph;
    
	public Team4Edge(final Vertex outVertex, final Vertex inVertex, final String label, final Team4Graph team4Graph) {
	        this.label = label;
	        this.outVertex = outVertex;
	        this.inVertex = inVertex;

	}

	@Override
	public Object getProperty(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> getPropertyKeys() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setProperty(String key, Object value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vertex getVertex(Direction direction) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLabel() {
		// TODO Auto-generated method stub
		return null;
	}
	public String toString() {
		return "["+ outVertex+"-" + label +"->"+ inVertex+"]";
		
	}
}
