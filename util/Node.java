package util;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Node {

  public final int value;
  public Set<Node> nodeSet;
  public Map<Node, Edge> edgeMap;

  public Node(int value) {
    this.value = value;
    this.nodeSet = new HashSet<>();
    this.edgeMap = new HashMap<>();
  }

  public List<Node> getAdjacentNodeList() {
    return new ArrayList<>(nodeSet);
  }

  public Node getForestRoot() {
    throw new RuntimeException();
  }

  public void addEdge(Node node, Edge edge) {
    nodeSet.add(node);
    edgeMap.put(node, edge);
  }

  public Edge getEdge(Node node) {
    return edgeMap.get(node);
  }

  @Override
  public String toString() {
    return "<" + value + ">";
  }
}