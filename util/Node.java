package util;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Node {

  public final Integer value;
  public boolean visited;
  private Set<Node> nodeSet;
  private Map<Node, Edge> edgeMap;
  private List<Edge> edgeList;
  private Node forestRoot;

  public Node(Integer value) {
    this.value = value;
    this.nodeSet = new HashSet<>();
    this.edgeMap = new HashMap<>();
    this.edgeList = new ArrayList<>();
    this.visited = false;
  }

  public List<Node> getAdjacentNodeList() {
    return new ArrayList<>(nodeSet);
  }

  public void setForestRoot(Node node) {
    this.forestRoot = node;
  }

  public Node getForestRoot() {
    return forestRoot;
  }

  public void addEdge(Node node, Edge edge) {
    nodeSet.add(node);
    edgeMap.put(node, edge);
    edgeList.add(edge);
  }

  public Edge getEdge(Node node) {
    return edgeMap.get(node);
  }

  public List<Edge> getEdgeList() {
    return edgeList;
  }
 
  @Override
  public int hashCode() {
    return value.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Node) {
      Node objNode = (Node) obj;
      return this.value.equals(objNode.value);
    }
    return false;
  }

  @Override
  public String toString() {
    return "<" + value + ">";
  }
}
