package vm;

import base.ElException;
import processor.ProParameter;

public class I64Return extends Return {
  @Override
  public VMCommand create(ProParameter parameter) throws ElException {
    return new I64Return();
  }
  
  @Override
  public void execute() {
    int pos = stackPointer;
    returnFromCall(1);
    i64Stack[stackPointer] = i64Stack[pos];
    if(log) typeStack[stackPointer] = ValueType.I64;
  }
}
