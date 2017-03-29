package util;

public class Edge {

  public final Node from;
  public final Node to;
  public boolean marked;

  public Edge(Node from, Node to) {
    this.from = from;
    this.to = to;
    this.marked = false;
  }

  @Override
  public String toString() {
    StringBuilder strBldr = new StringBuilder();
    strBldr.append("(");
    strBldr.append(from);
    strBldr.append(", ");
    strBldr.append(to);
    strBldr.append(")");
    return strBldr.toString();
  }
}
