package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Graph {

  private HashMap<Integer, Node> nodeMap;
  private Set<Edge> edgeSet;
  private Set<Edge> markedEdges;
  private Map<Node, List<Edge>> edgeMaping;
  private Set<Node> exposedVerts;

  public Graph() {
    this.nodeMap = new HashMap<>();
    this.edgeSet = new HashSet<>();
    this.markedEdges = new HashSet<>();
    this.edgeMaping = new HashMap<>();
    this.exposedVerts = new HashSet<>();
  }

  public int size() {
    return nodeMap.size();
  }

  public void addEdge(Integer from, Integer to) {
    Node f = addOrRetrieveNode(from);
    Node t = addOrRetrieveNode(to);
    Edge edge = new Edge(t, f);
    f.addEdge(t, edge);
    edgeSet.add(edge);
  }

  protected Node addOrRetrieveNode(Integer value) {
    Node node = nodeMap.get(value);
    if (node == null) {
      node = new Node(value);
      exposedVerts.add(node);
      nodeMap.put(value, node);
    }
    return node;
  }

  public Set<Node> getExposedVertices() {
    return exposedVerts;
  }

  public void markEdges(List<Edge> edges) {
    for (Edge edge : edges) {
      markEdge(edge);
    }
  }

  public void markEdge(Edge edge) {
    markEdge(edge.from, edge.to);
  }

  public void markEdge(Node v, Node w) {
    Edge edge = v.getEdge(w);
    edgeSet.remove(edge);
    markedEdges.add(edge);
    edge.marked = true;
  }

  public Graph contractBlossom(List<Edge> blossom) {
    throw new RuntimeException();
  }

  public SuperNode getContractedNode() {
    throw new RuntimeException();
  }
}
