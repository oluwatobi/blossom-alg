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
  protected SuperNode contractedNode;

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
    Edge edge = new Edge(f, t);
    f.addEdge(t, edge);
    edgeSet.add(edge);
  }

  public Edge getEdge(Integer from, Integer to) {
    Node f = nodeMap.get(from);
    Node t = nodeMap.get(to);
    return f.getEdge(t);
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

  public Node getNode(Integer value) {
    return nodeMap.get(value);
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
    Graph contracted = new Graph();
    Set<Node> blossomNodes = new HashSet<>();
    Set<Edge> newEdgeSet = new HashSet<>();
    for (Edge edge : blossom) {
      blossomNodes.add(edge.from);
      blossomNodes.add(edge.to);
    }
    SuperNode node = new SuperNode(blossom.get(blossom.size() - 1).from.value);
    for (Edge edge : edgeSet) {
      if (blossomNodes.contains(edge.from) && blossomNodes.contains(edge.to)) {
        // edge internal to the blossom.
      } else {
        if (blossomNodes.contains(edge.from)) {
          newEdgeSet.add(new Edge(node, edge.to));
        } else if (blossomNodes.contains(edge.to)) {
          newEdgeSet.add(new Edge(edge.from, node));
        }
      }
    }

    contracted.contractedNode = node;
    for (Edge edge : newEdgeSet) {
      contracted.addEdge(edge.from.value, edge.to.value);
      if (edge.marked) {
        contracted.markEdge(edge.from, edge.to);
      }
    }
    return contracted;
  }

  public SuperNode getContractedNode() {
    return contractedNode;
  }

  @Override
  public String toString() {
    return nodeMap.toString();
  }
}
