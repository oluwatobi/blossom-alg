package util;

public class SuperNode extends Node {

  public SuperNode(int value) {
    super(value);
  }

  @Override
  public String toString() {
    return "<~" + value + "~>";
  }
}