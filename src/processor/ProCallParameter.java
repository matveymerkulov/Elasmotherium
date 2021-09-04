package processor;

import ast.Entity;
import ast.FunctionCall;
import base.ElException;

public class ProCallParameter extends ProParameter {
  private final int index;

  public ProCallParameter(String index) throws ElException {
    super();
    try {
      this.index = Integer.parseInt(index);
    } catch(NumberFormatException ex) {
      throw new ElException("Wrong parameter number format.");
    }
  }

  @Override
  public Entity getValue() throws ElException {
    return ((FunctionCall) ProBase.current).getParameter(index);
  }

  @Override
  public void setValue(Entity value) {
    ((FunctionCall) ProBase.current).setParameter(index, value);
  }

  @Override
  public String toString() {
    return "v" + index;
  }
}
