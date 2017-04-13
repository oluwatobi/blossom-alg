package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Graph {

  private Map<Integer, Node> nodeMap;
  private Map<Node, Map<Node, Edge>> adjacencyList;
  private Set<Edge> edgeSet;
  protected Node contractedNode;

  public Graph() {
    this.nodeMap = new HashMap<>();
    this.edgeSet = new HashSet<>();
    this.adjacencyList = new HashMap<>();
  }

  public int size() {
    return nodeMap.size();
  }

  protected Node addOrRetrieveNode(Integer value) {
    Node node = nodeMap.get(value);
    if (node == null) {
      node = new Node(value);
      this.nodeMap.put(value, node);
      this.adjacencyList.put(node, new HashMap<>());
    }
    return node;
  }

  public void addEdge(Integer from, Integer to) {
    Node f = addOrRetrieveNode(from);
    Node t = addOrRetrieveNode(to);
    Edge edge = new Edge(f, t);
    this.adjacencyList.get(f).put(t, edge);
    this.adjacencyList.get(t).put(f, edge);
    this.edgeSet.add(edge);
  }

  public void addEdge(Edge edge){
    this.adjacencyList.get(edge.from).put(edge.to, edge);
    this.adjacencyList.get(edge.to).put(edge.from, edge);
    this.edgeSet.add(edge);
  }

  public void removeEdge(Node from, Node to){
    this.edgeSet.remove(this.adjacencyList.get(from).get(to));
    this.adjacencyList.get(from).remove(to);
    this.adjacencyList.get(to).remove(from);
  }

  public Edge getEdge(Node node1, Node node2) {
    return this.adjacencyList.get(node1).get(node2);
  }

  public Node getNode(Integer value) {
    return nodeMap.get(value);
  }

  public Set<Node> getAdjacentNodes(Node node){
    return new HashSet<Node>(this.adjacencyList.get(node).keySet());
  }

  public List<Edge> getAdjacentEdges(Node node){
    return new ArrayList<Edge>(this.adjacencyList.get(node).values());
  }

  public Set<Node> getAllNodes(){
    return new HashSet<Node>(this.nodeMap.values());
  }

  public Set<Edge> getAllEdges(){
    return new HashSet<Edge>(this.edgeSet);
  }

  public boolean containsNode(Integer value) {
    return nodeMap.containsKey(value);
  }

  public void addContractedNode(Node node) {
    this.contractedNode = node;
  }

  public Node getContractedNode() {
    return this.contractedNode;
  }

  @Override
  public String toString() {
    return nodeMap.toString();
  }
}
