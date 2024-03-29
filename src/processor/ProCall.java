package processor;

import processor.parameter.ProParameter;
import ast.Entity;
import ast.ID;
import exception.ElException;
import exception.EntityException;

public class ProCall extends ProCommand {
  private final ProParameter callObject, parameter;
  private final ID method;
  
  public ProCall(String object, String method, String parameter, int proLine) throws ElException {
    super(proLine);
    this.callObject = ProParameter.get(object);
    this.method = ID.get(method);
    this.parameter = parameter.isEmpty() ? null : ProParameter.get(parameter);
    this.line = currentLineReader.getLineNum();
  }
  
  @Override
  public void execute() throws ElException, EntityException {
    Entity newCurrent = callObject.getValue().resolve();
    Entity callParam = parameter == null ? null : parameter.getValue();
    if(log) {
      log(callObject + "(" + newCurrent.toString() + ")."
          + method + "(" + callParam + ")");
      subIndent.append("| ");
    }
    currentProcessor.compileCall(newCurrent, method, callParam);
    if(log) {
      subIndent.delete(0, 2);
    }
  }
}
