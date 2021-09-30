package processor;

import processor.parameter.ProParameter;
import base.ElException;
import base.EntityException;

public class GetField extends ProCommand {
  static final GetField instance = new GetField(null);

  private final ProParameter parameter;

  private GetField(ProParameter parameter) {
    this.parameter = parameter;
  }
  
  @Override
  public ProCommand create(String param) throws ElException {
    return new GetField(ProParameter.get(param));
  }

  @Override
  public void execute() throws ElException, EntityException {
    if(log) println("Getting field of " + currentObject + ".");
    currentObject = currentObject.getField();
  }  
}
