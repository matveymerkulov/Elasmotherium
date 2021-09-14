package processor;

import processor.parameter.ProParameter;
import ast.Entity;
import ast.ID;
import base.ElException;

public class ProCall extends ProCommand {
  private final ProParameter callObject, parameter;
  private final ID method;
  
  public ProCall(String object, String method, String parameter)
      throws ElException {
    super();
    this.callObject = ProParameter.get(object);
    this.method = ID.get(method);
    this.parameter = parameter.isEmpty() ? null : ProParameter.get(parameter);
  }
  
  @Override
  public void execute() throws ElException {
    Entity newCurrent = callObject.getValue().resolve();
    Entity callParam = parameter == null ? null : parameter.getValue();
    if(log) {
      log(callObject + "(" + newCurrent.toString() + ")."
          + method + "(" + callParam + ")");
      subIndent.append("| ");
    }
    currentProcessor.process(newCurrent, newCurrent.getID(), method, callParam);
    if(log) subIndent = subIndent.delete(0, 2);
  }
}
