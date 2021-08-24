package processor;

import ast.Entity;
import base.ElException;

public class ProThis extends ProParameter {
  public static ProThis instance = new ProThis();
  
  private ProThis() {}
  
  @Override
  public Entity getValue() throws ElException {
    return current;
  }

  @Override
  public String toString() {
    return "this";
  }
}
