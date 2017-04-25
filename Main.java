import util.Edge;
//import util.Forest;
import util.Graph;
import util.Node;
//import util.SuperNode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;

public class Main {

  public static void main(String[] args) {
    Graph graph = readGraphFile(args[0]);
    Set<Edge> matching = blossomAlgorithm(graph);
    System.out.println();
    System.out.println("---------Result-------");
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

  public static Set<Edge> blossomAlgorithm(Graph graph) {
    Set<Edge> matchingSet = new HashSet<>();
    findMaxMatching(graph, matchingSet);
    return matchingSet;
  }

  private static Set<Edge> findMaxMatching(Graph graph, Set<Edge> matching) {
    List<Node> augPath;
    boolean finished = false;
    while (!finished) {
      augPath = findAugPath(graph, matching);
      finished = augPath.isEmpty();
      matching = addAltEdges(augPath, graph, matching);
    }

    return matching;
  }

  private static List<Node> findAugPath(Graph g, Set<Edge> matching) {
    List<Node> augPath;
    Set<Node> exposedVertices;
    Set<Edge> unmarkedEdges;
    Set<Node> nodesToCheck;

    // unmark all vertices and edges in the graph, and then mark all
    // edges in the current matching
    unmarkedEdges = getUnmarkedEdges(g, matching);
    nodesToCheck = getExposedNodes(g,matching);

    // build a forest and add all exposed vertices as roots
    exposedVertices = getExposedNodes(g, matching);
    Map<Node, Node> rootMap = new HashMap<>();
    Map<Node, Node> parentMap = new HashMap<>();
    Map<Node, Integer> heightMap = new HashMap<>();
    System.out.println("++++SEPARATION++++");
    System.out.println("Matching: " + matching);
    for (Node node : exposedVertices) {
      System.out.println("Added to forest: " + node);
      rootMap.put(node, node);
      parentMap.put(node, null);
      heightMap.put(node, 0);
    }

    Node v, w;
    while (nodesToCheck.size()>=1) {
      v = nodesToCheck.iterator().next();
      System.out.println("Working on vertex "+v);
      for (Edge edge : g.getAdjacentEdges(v)) {
        if (unmarkedEdges.contains(edge)) {
          w = edge.getOtherEnd(v);
          System.out.println("    Looking at vertices "+v+" and "+w);
          if (!rootMap.containsKey(w)) {
            Node x = findOtherNodeInMatching(matching, w);
            addToForest(rootMap, parentMap, heightMap, v, w, x);
            nodesToCheck.add(x);
          } else {
            if (heightMap.get(w) % 2 == 0) {
              if (rootMap.get(v) != rootMap.get(w)) {
                augPath = returnAugPath(g, rootMap, parentMap, heightMap, v, w);
              } 
              else {
                System.out.println();
                System.out.println("Starting blossom recursion");
                System.out.println("--------------------------");
                augPath = blossomRecursion(g, matching, rootMap, parentMap, heightMap, v, w);
                System.out.println("Done with blossom recursion");
                System.out.println("---------------------------");
              }
              System.out.println("augPath: " + augPath + "\n");
              return augPath;
            } else {
              // do nothing
            }
          }
        }
        unmarkedEdges.remove(edge);
      }
      nodesToCheck.remove(v);
    }
    // Returning the empty path.
    System.out.println("Could not find any augmenting path");
    return new ArrayList<>();
  }

  private static void addToForest(Map<Node, Node> rootMap, Map<Node, Node> parentMap, 
      Map<Node, Integer> heightMap, Node v, Node w, Node x){
    System.out.println("    Adding edges to forest");
    System.out.println("    v: " + v+" w: " + w + " x: "+x);
    Node root = rootMap.get(v);
    rootMap.put(w,root);
    rootMap.put(x,root);
    parentMap.put(w,v);
    parentMap.put(x,w);
    heightMap.put(w, heightMap.get(v)+1);
    heightMap.put(x, heightMap.get(v)+2);
    return;
  }

  private static Node findOtherNodeInMatching(Set<Edge> edges, Node node1){
    for (Edge edge : edges) {
      if (edge.from == node1) {
        return edge.to;
      }
      if (edge.to == node1) {
        return edge.from;
      }
    }
    return null;
  }

  private static List<Node> returnAugPath(Graph graph, Map<Node, Node> rootMap, 
      Map<Node, Node> parentMap, Map<Node, Integer> heightMap, Node v, Node w) {
    List<Node> augPath = new ArrayList<>();
    Node curr = v;
    while (curr!=null){
      augPath.add(0, curr);
      curr = parentMap.get(curr);
    }
    curr = w;
    while (curr!=null){
      augPath.add(curr);
      curr = parentMap.get(curr);
    }
    return augPath;
  }

  private static List<Node> blossomRecursion(Graph graph, Set<Edge> matching, Map<Node, Node> rootMap,
      Map<Node, Node> parentMap, Map<Node, Integer> heightMap, Node v, Node w) {
    // Construct blossom
    Node root = rootMap.get(v);
    List<Node> blossom = new ArrayList<>();
    System.out.print("Blossom is: ");
    Node curr = v;
    while (curr!=null){
      blossom.add(0, curr);
      curr = parentMap.get(curr);
    }
    curr = w;
    while (curr!=root){
      blossom.add(curr);
      curr = parentMap.get(curr);
    }
    for (Node node: blossom)
      System.out.print(node+" ");
    System.out.println();

    Graph contractedGraph = contractBlossom(graph, blossom);
    Node contractedNode = contractedGraph.getContractedNode();
    Set<Edge> contractedMatching = contractMatching(contractedGraph,
        matching, blossom, contractedNode);
    List<Node> augPath = findAugPath(contractedGraph,
        contractedMatching);
    if (containsEdgeWithNode(augPath, contractedNode)) {
      augPath = liftPathWithBlossom(augPath, blossom, graph);
      System.out.println("Lifted augmenting path is: "+augPath);
    }
    return augPath;
  }

  public static Set<Edge> getUnmarkedEdges(Graph g, Set<Edge> matching){
    Set<Edge> unmarkedEdges = g.getAllEdges();
    for (Edge edge: matching)
      unmarkedEdges.remove(edge);
    return unmarkedEdges;
  }

  public static Set<Node> getExposedNodes(Graph g, Set<Edge> matching){
    Set<Node> exposedNodes = g.getAllNodes();
    for (Edge edge: matching){
      exposedNodes.remove(edge.from);
      exposedNodes.remove(edge.to);
    }
    return exposedNodes;
  }

  public static Graph contractBlossom(Graph g, List<Node> blossom) {
    Graph contracted = new Graph();
    Node contractedNode = blossom.get(0);
    System.out.println("Beginning contraction to "+contractedNode);

    // Add edges to contracted graph
    for (Edge edge : g.getAllEdges()){
      //System.out.println("Added edge: "+edge);
      contracted.addEdge(edge);
    }
    // Delete edges internal to the blossom
    Edge removed;
    for (int i=0;i<blossom.size()-1;i++){
      removed = contracted.removeEdge(blossom.get(i), blossom.get(i+1));
      //System.out.println("Removed edge: "+removed);
    }
    removed = contracted.removeEdge(blossom.get(blossom.size()-1), blossom.get(0));
    //System.out.println("Removed edge: "+removed);
    // Change edges from blossom to other nodes to the contracted node
    for (int i=1;i<blossom.size();i++){
      for (Node node: g.getAdjacentNodes(blossom.get(i))){
        int prev = i-1;
        int next = (i+1)%blossom.size();
        if (node!=blossom.get(prev)&&node!=blossom.get(next)){
          removed = contracted.removeEdge(blossom.get(i), node);
          //System.out.println("Removed edge: "+removed);
          contracted.addEdge(node.value, contractedNode.value);
          //System.out.println("Added edge: "+contracted.getEdge(node, contractedNode));
        }
      }
    }
    contracted.addContractedNode(contractedNode);

    return contracted;
  }

  private static Set<Edge> contractMatching(Graph contracted,
      Set<Edge> matching, List<Node> blossom, Node contractedNode) {
    Set<Edge> contractedMatching = new HashSet<>();
    Set<Node> blossomNodes = new HashSet<>();
    for (Node node: blossom){
      blossomNodes.add(node);
    }
    for (Edge edge : matching) {
      if (blossomNodes.contains(edge.from)
          && !blossomNodes.contains(edge.to)) {
        contractedMatching.add(
            contracted.getEdge(contractedNode, edge.to));
      } else if (blossomNodes.contains(edge.to)
          && !blossomNodes.contains(edge.from)) {
        contractedMatching.add(
            contracted.getEdge(edge.from, contractedNode));
      } else if (!blossomNodes.contains(edge.to) 
          && !blossomNodes.contains(edge.from)){
        contractedMatching.add(contracted.getEdge(edge.from, edge.to));
      }
    }
    return contractedMatching;
  }

  private static boolean containsEdgeWithNode(List<Node> path, Node node) {
    for (Node i : path) {
      if (i==node)
        return true;
    }
    return false;
  }

  private static List<Node> liftPathWithBlossom(List<Node> augPath,
      List<Node> blossom, Graph g) {
    List<Node> lifted = new ArrayList<>();
    Node contractedNode = blossom.get(0);
    for (int i = 0; i < augPath.size(); ++i) {
      // lift the blossom
      if (augPath.get(i)==contractedNode){
        // leftmost of the augmenting path or in the middle of the augmenting path, right unmatched
        if (i%2==0){
          int outgoingIndex = findOutgoingIndex(augPath.get(i+1), blossom, g);
          if (outgoingIndex%2==0){
            for (int j=0;j<=outgoingIndex;j++){
              lifted.add(blossom.get(j));
            }
          }
          else{
            lifted.add(blossom.get(0));
            for (int j=blossom.size()-1;j>=outgoingIndex;j--){
              lifted.add(blossom.get(j));
            }
          }
        }
        // rightmost of the augmenting path or in the middle of the augmenting path, left unmatched
        if (i%2==1){
          int outgoingIndex = findOutgoingIndex(augPath.get(i+1), blossom, g);
          if (outgoingIndex%2==0){
            for (int j=outgoingIndex;j>=0;j--){
              lifted.add(blossom.get(j));
            }
          }
          else{
            for (int j=outgoingIndex;j<blossom.size();j++){
              lifted.add(blossom.get(j));
            }
            lifted.add(blossom.get(0));
          }
        }
      }
      // just add the nodes in the augmenting path
      else{
        lifted.add(augPath.get(i));
      }
    }
    return lifted;
  }

  private static int findOutgoingIndex(Node node, List<Node> blossom, Graph g){
    for (int i=0;i<blossom.size();i++){
      if (g.getEdge(node, blossom.get(i))!=null)
        return i;
    }
    System.out.println("Blossom lifting error");
    return -1;
  }

  private static Set<Edge> addAltEdges(List<Node> augPath, Graph g, Set<Edge> matching) {
    for(int i = 0; i < augPath.size()-1; i += 1) {
      // Add to matching
      if (i%2==0)
        matching.add(g.getEdge(augPath.get(i), augPath.get(i+1)));
      // Substract from matching
      if (i%2==1)
        matching.remove(g.getEdge(augPath.get(i), augPath.get(i+1)));
    }
    return matching;
  }
}
