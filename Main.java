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
    List<Edge> augPath = findAugPath(graph, matching);
    if (augPath.isEmpty()) {
      return matching;
    } else {
      addAltEdges(augPath, matching);
      return findMaxMatching(graph, matching);
    }
  }

  private static List<Edge> findAugPath(Graph graph, List<Edge> matching) {
    List<Edge> augPath;
    Forest forest = new Forest();
    Set<Node> nodesToCheck = graph.getExposedVertices();
    for (Node node : nodesToCheck) {
      forest.addTreeRoot(node);
      // TODO(oluwatobi): Set root of the Forest Node be itself
      // whenever an add occurs.
    }
    graph.markEdges(matching);
    List<Node> forestNodes = forest.getNodeList();
    for (Node v : forestNodes) {
      for (Node w : v.getAdjacentNodeList()) {
        if (!forest.contains(w)) {
          // TODO(oluwatobi): implementation of the addToForest function.
          forest.addToForest(matching, v, w);
        } else {
          // TODO(oluwatobi): implementation of the distance function.
          if (forest.distance(w, w.getForestRoot()) % 2 == 0) {
            if (v.getForestRoot() != w.getForestRoot()) {
              augPath = returnAugPath(forest, v, w);
            } else {
              augPath = blossomRecursion(graph, matching, forest, v, w);
            }
            return augPath;
          } else {
            // Do nothing.
          }
        }
        graph.markEdge(v, w);
      }
    }
    // Returning the empty path.
    return new ArrayList<>();
  }

  private static List<Edge> returnAugPath(Forest forest, Node v, Node w) {
    Node rootV = v.getForestRoot();
    Node rootW = w.getForestRoot();

    List<Edge> path = forest.shortestPath(rootV, v);
    path.addAll(forest.shortestPath(w, rootW));
    return path;
  }

  private static List<Edge> blossomRecursion(Graph graph, List<Edge> matching,
      Forest forest, Node v, Node w) {
    List<Edge> blossom = forest.shortestPath(v, w);
    blossom.add(new Edge(blossom.get(blossom.size() - 1).from, v));
    Graph contractedGraph = graph.contractBlossom(blossom);
    SuperNode contractedNode = contractedGraph.getContractedNode();
    List<Edge> contractedMatching = contractMatching(blossom, contractedNode);
    List<Edge> augPath = findAugPath(contractedGraph,
        contractedMatching);
    if (augPath.contains(w)) {
      augPath = liftPathWithBlossom(augPath, blossom);
    }
    return augPath;
  }

  private static List<Edge> contractMatching(List<Edge> blossom,
      SuperNode contractedNode) {
    throw new RuntimeException();
  }

  private static List<Edge> liftPathWithBlossom(List<Edge> augPath,
      List<Edge> blossom) {
    throw new RuntimeException();
  }

  private static void addAltEdges(List<Edge> augPath, List<Edge> matching) {
    // TODO(oluwatobi): write that code breh.
    throw new RuntimeException();
  }
}
