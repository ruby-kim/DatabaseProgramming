package kr.ac.sejong.da.project;

public interface Edge extends Element {
    /**
     * Return the tail/out or head/in vertex.
     *
     * @param direction whether to return the tail/out or head/in vertex
     * @return the tail/out or head/in vertex
     * @throws IllegalArgumentException is thrown if a direction of both is provided
     */
    public Vertex getVertex(Direction direction) throws IllegalArgumentException;

    /**
     * Return the label associated with the edge.
     *
     * @return the edge label
     */
    public String getLabel();
}
