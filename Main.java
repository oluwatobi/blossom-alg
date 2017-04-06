import util.Edge;
import util.Forest;
import util.Graph;
import util.Node;
import util.SuperNode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Main {

  public static void main(String[] args) {
    Graph graph = readGraphFile(args[0]);
    List<Edge> matching = blossomAlgorithm(graph);
    System.out.println("Graph Matching size: " + matching.size());
    System.out.println("Graph Matching:\n\t" + matching);
  }

  private static Graph readGraphFile(String fileName) {
    File file;
    String[] line;
    Scanner scan = null;
    Graph graph = new Graph();
    try {
      file = new File(fileName);
      scan = new Scanner(file);
      while (scan.hasNextLine()) {
        line = scan.nextLine().split(" ");
        graph.addEdge(Integer.parseInt(line[0]), Integer.parseInt(line[1]));
      }
    } catch(IOException e) {
      e.printStackTrace();
      // There is NO point in trying to continue
      // processing an empty graph.
      System.exit(1);
    } finally {
      if (scan != null) {
        scan.close();
      }
    }
    return graph;
  }

  public static List<Edge> blossomAlgorithm(Graph graph) {
    List<Edge> matchingSet = new ArrayList<>();
    findMaxMatching(graph, matchingSet);
    return matchingSet;
  }

  private static List<Edge> findMaxMatching(Graph graph, List<Edge> matching) {
    List<Edge> augPath;
    boolean finished = false;
    while (!finished) {
      augPath = findAugPath(graph, matching);
      finished = augPath.isEmpty();
      addAltEdges(augPath, matching);
    }

    return matching;
  }

  private static List<Edge> findAugPath(Graph graph, List<Edge> matching) {
    List<Edge> augPath;
    Set<Node> nodesToCheck;

    Forest forest = new Forest();
    graph.markEdges(matching);
    nodesToCheck = graph.getExposedVertices();
    System.out.println("++++SEPARATION++++");
    System.out.println("Matching: " + matching);
    for (Node node : nodesToCheck) {
      System.out.println("Added to forest: " + node);
      forest.addTreeRoot(node);
    }
    Set<Node> forestNodes = forest.getNodes();
    Node v, w;
    for (Node vForest : forestNodes) {
      v = graph.getNode(vForest.value);
      for (Edge edge : v.getEdgeList()) {
        if (!edge.marked) {
          w = edge.to;
          if (!forest.contains(w)) {
            forest.addToForest(matching, v, w);
          } else {
            if (forest.distance(w, w.getForestRoot()) % 2 == 0) {
              if (v.getForestRoot() != w.getForestRoot()) {
                augPath = returnAugPath(graph, forest, v, w);
              } else {
                augPath = blossomRecursion(graph, matching, forest, v, w);
              }
              System.out.println("Exposed vertices: "
                  + graph.getExposedVertices());
              System.out.println("Curr Vertex: " + v);
              System.out.println("Nodes to Check: " + nodesToCheck);
              System.out.println("augPath: " + augPath + "\n");
              return augPath;
            } else {
              // Do nothing.
            }
          }
          graph.markEdge(v, w);
        }
      }
    }
    // Returning the empty path.
    return new ArrayList<>();
  }

  private static List<Edge> returnAugPath(Graph graph, Forest forest, Node v,
      Node w) {
    Node rootV = v.getForestRoot();
    Node rootW = w.getForestRoot();

    List<Edge> path1 = forest.shortestPath(rootV, v);
    List<Edge> path2 = forest.shortestPath(w, rootW);
    Node vGraph = graph.getNode(v.value);
    Node wGraph = graph.getNode(w.value);
    Edge edge = vGraph.getEdge(wGraph);
    path1.add(edge);
    path1.addAll(path2);
    return path1;
  }

  private static List<Edge> blossomRecursion(Graph graph, List<Edge> matching,
      Forest forest, Node v, Node w) {
    List<Edge> blossom = forest.shortestPath(v, w);
    blossom.add(new Edge(blossom.get(blossom.size() - 1).from, v));
    Graph contractedGraph = graph.contractBlossom(blossom);
    SuperNode contractedNode = contractedGraph.getContractedNode();
    List<Edge> contractedMatching = contractMatching(contractedGraph,
        matching, blossom, contractedNode);
    List<Edge> augPath = findAugPath(contractedGraph,
        contractedMatching);
    if (containsEdgeWithNode(augPath, w)) {
      augPath = liftPathWithBlossom(augPath, blossom);
    }
    return augPath;
  }

  private static List<Edge> contractMatching(Graph contracted,
      List<Edge> matching, List<Edge> blossom, SuperNode contractedNode) {
    List<Edge> contractedMatching = new ArrayList<>();
    Set<Node> blossomNodes = new HashSet<>();
    for (Edge edge : blossom) {
      blossomNodes.add(edge.from);
      blossomNodes.add(edge.to);
    }
    for (Edge edge : matching) {
      if (blossomNodes.contains(edge.from)
          && !blossomNodes.contains(edge.to)) {
        contractedMatching.add(
            contracted.getEdge(contractedNode.value, edge.to.value));
      } else if (blossomNodes.contains(edge.to)
          && !blossomNodes.contains(edge.from)) {
        contractedMatching.add(
            contracted.getEdge(edge.from.value, contractedNode.value));
      }
    }
    return contractedMatching;
  }

  private static boolean containsEdgeWithNode(List<Edge> path, Node w) {
    for (Edge edge : path) {
      if (edge.from.value == w.value || edge.to.value == w.value) {
        return true;
      }
    }
    return false;
  }

  private static List<Edge> liftPathWithBlossom(List<Edge> augPath,
      List<Edge> blossom) {
    List<Edge> lifted = new ArrayList<>();
    int contractedNodeValue = blossom.get(blossom.size() - 1).to.value;
    for (int i = 0; i < augPath.size(); ++i) {
      lifted.add(augPath.get(i));
      if (augPath.get(i).to.value == contractedNodeValue) {
        for (Edge edge : blossom) {
          lifted.add(edge);
        }
      }
    }
    return lifted;
  }

  private static void addAltEdges(List<Edge> augPath, List<Edge> matching) {
    Set<Node> nodeCheck = new HashSet<>();
    for (Edge edge : matching) {
      nodeCheck.add(edge.from);
      nodeCheck.add(edge.to);
    }
    for(int i = 0; i < augPath.size() && matching.size() < 9; i += 2) {

      if (!nodeCheck.contains(augPath.get(i).from)
          || !nodeCheck.contains(augPath.get(i).to)) {
        matching.add(augPath.get(i));
        nodeCheck.add(augPath.get(i).from);
        nodeCheck.add(augPath.get(i).to);
      }
    }
  }
}
