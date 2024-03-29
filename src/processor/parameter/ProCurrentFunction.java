package processor.parameter;

import ast.Entity;
import exception.ElException;

public class ProCurrentFunction extends ProParameter {
  static ProCurrentFunction instance = new ProCurrentFunction();
  
  private ProCurrentFunction() {}
  
  @Override
  public Entity getValue() throws ElException {
    return currentFunction;
  }

  @Override
  public String toString() {
    return "currentFunction";
  }
}
