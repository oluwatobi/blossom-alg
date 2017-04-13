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

  public Node(Integer value) {
    this.value = value;
    this.visited = false;
  }

  public static Node getSmallerNode(Node node1, Node node2){
    if (node1.value>node2.value)
      return node1;
    else
      return node2;
  }

  public static Node getBiggerNode(Node node1, Node node2){
    if (node1.value<=node2.value)
      return node1;
    else
      return node2;
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
