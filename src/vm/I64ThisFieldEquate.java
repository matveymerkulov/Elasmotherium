package vm;

import base.ElException;
import static vm.VMBase.currentCall;

public class I64ThisFieldEquate extends VMFieldCommand {
  public I64ThisFieldEquate(int fieldIndex) {
    super(fieldIndex);
  }
  
  @Override
  public VMCommand create(int index) throws ElException {
    return new I64ThisFieldEquate(index);
  }
  
  @Override
  public void execute() throws ElException {
    currentCall.thisField(fieldIndex).i64Set(i64Stack[stackPointer]);
    stackPointer--;
    currentCommand++;
  }
}