package vm.collection;

import exception.ElException;
import vm.VMCommand;

public class ObjectGetAtIndex extends VMCommand {
  @Override
  public void execute() throws ElException {
    stackPointer--;
    valueStack[stackPointer] = valueStack[stackPointer]
        .valueGet((int) i64Stack[stackPointer + 1]);
    if(log) typeStack[stackPointer] = ValueType.OBJECT;
    currentCommand++;
  }
}
