//package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Forest {

  private static final boolean DEBUG = false;

  private Set<Node> nodeSet;
  // Free tree is an undirected graph with no cycles.
  private Map<Integer, Graph> forestMap;
  private Map<Integer, Node> nodeMap;
  private Map<Integer, Integer> distanceMap;

  public Forest() {
    this.nodeSet = new HashSet<>();
    this.forestMap = new HashMap<>();
    this.distanceMap = new HashMap<>();
  }

  public boolean addTreeRoot(Node node) {
    if (nodeSet.contains(node)) {
      return false;
    }
    Graph graph = new Graph();
    Node forestNode = graph.addOrRetrieveNode(node.value);
    nodeSet.add(forestNode);
    forestMap.put(forestNode.value, graph);
    nodeMap.put(forestNode.value, forestNode);
    distanceMap.put(forestNode.value, 0);
    forestNode.setTreeRoot(forestNode);
    return true;
  }

  public Set<Node> getNodes() {
    return new HashSet<>(nodeSet);
  }

  public boolean contains(Node node) {
    return forestMap.containsKey(node.value);
  }

  public boolean addToForest(Set<Edge> edges, Node v, Node w) {
    Graph tree = forestMap.get(v.value);
    if (nodeSet.contains(w)) {
      return false;
    }
    System.out.println("v: " + v);
    System.out.println("w: " + w);
    System.out.println("edges: " + edges);
    System.out.println("nodes: " + nodeSet);
    System.out.println(this);
    Node x = findAdjacentTo(edges, w);
    Node vInt = tree.addOrRetrieveNode(v.value);
    Node wInt = tree.addOrRetrieveNode(w.value);
    Node xInt = tree.addOrRetrieveNode(x.value);
    Node treeRoot = v.getTreeRoot();
    wInt.setTreeRoot(treeRoot);
    xInt.setTreeRoot(treeRoot);
    // Add edge (v, w) to the forest
    tree.addEdge(v.value, w.value);
    // Add edge (w, x) to the forest
    tree.addEdge(w.value, x.value);
    // Update distance
    distanceMap.put(w.value, distanceMap.get(v.value)+1);
    distanceMap.put(x.value, distanceMap.get(v.value)+1);
    return true;
  }

  private Node findAdjacentTo(Set<Edge> edges, Node w) {
    for (Edge edge : edges) {
      if (edge.from.value == w.value) {
        return edge.to;
      }
      if (edge.to.value == w.value) {
        return edge.from;
      }
    }
    return null;
  }

  public List<Edge> shortestPath(Node v, Node w) {
    List<Edge> path = dfs(v, w);
    return path;
  }

  public int distance(Node node) {
    return distanceMap.get(node.value);
  }

  private List<Edge> dfs(Node v, Node w) {
    Graph tree = forestMap.get(v.getTreeRoot().value);
    Node vInt = tree.getNode(v.value);
    Node wInt = tree.getNode(w.value);
    List<Edge> path = new ArrayList<>();
    dfs(path, vInt, wInt);
    return path;
  }

  private boolean dfs(List<Edge> path, Node curr, Node goal) {
    boolean finished = false;
    curr.visited = true;
    for (Node adj : curr.getAdjacentNodeList()) {
      if (!adj.visited) {
        path.add(new Edge(curr, adj));
        if (curr == goal) {
          return true;
        }
        finished = dfs(path, adj, goal);
        if (finished) {
          curr.visited = false;
          return true;
        } else {
          path.remove(path.size() - 1);
        }
      }
    }
    return false;
  }

  @Override
  public String toString() {
    return forestMap.toString();
  }
}
