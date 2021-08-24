package vm;

import base.ElException;

public class StringFieldPush extends VMFieldCommand {
  public StringFieldPush(int fieldIndex) {
    super(fieldIndex);
  }
  
  @Override
  public VMCommand create(int index) throws ElException {
    return new StringFieldPush(index);
  }
  
  @Override
  public void execute() throws ElException {
    stringStack[stackPointer]
        = objectStack[stackPointer].fields[fieldIndex].stringGet();
    if(log) typeStack[stackPointer] = ValueType.STRING;
    currentCommand++;
  }
}
