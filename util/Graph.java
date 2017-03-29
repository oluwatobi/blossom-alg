package util;

import java.util.HashMap;
import java.util.List;

public class Graph {

  private HashMap<Integer, Node> nodeMap;

  public Graph() {
    this.nodeMap = new HashMap<>();
  }

  public int size() {
    return nodeMap.size();
  }

  public void addEdge(Integer from, Integer to) {
    Node f = addOrRetrieveNode(from);
    Node t = addOrRetrieveNode(to);
    f.addEdge(t);
  }

  protected Node addOrRetrieveNode(Integer value) {
    Node node = nodeMap.get(value);
    if (node == null) {
      node = new Node(value);
      nodeMap.put(value, node);
    }
    return node;
  }

  public List<Node> getExposedVertices() {
    throw new RuntimeException();
  }

  public void markEdges(List<Edge> edges) {
    throw new RuntimeException();
  }

  public void markEdge(Edge edge) {
    throw new RuntimeException();
  }

  public void markEdge(Node v, Node w) {
    throw new RuntimeException();
  }

  public Graph contractBlossom(List<Edge> blossom) {
    throw new RuntimeException();
  }

  public SuperNode getContractedNode() {
    throw new RuntimeException();
  }

}