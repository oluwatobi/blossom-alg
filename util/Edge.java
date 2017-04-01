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
  public boolean equals(Object obj) {
    if (obj instanceof Edge) {
      Edge objEdge = (Edge) obj;
      return this.from.equals(objEdge.from) && this.to.equals(objEdge.to);
    }
    return false;
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
