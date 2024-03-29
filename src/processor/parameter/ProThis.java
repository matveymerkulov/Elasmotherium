package processor.parameter;

import ast.Entity;
import exception.ElException;

public class ProThis extends ProParameter {
  static ProThis instance = new ProThis();
  
  private ProThis() {}
  
  @Override
  public Entity getValue() throws ElException {
    return currentObject;
  }

  @Override
  public String toString() {
    return "this";
  }
}
