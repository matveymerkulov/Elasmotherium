package vm;

import base.ElException;

public class I64FieldEquate extends VMFieldCommand {
  public I64FieldEquate(int fieldIndex) {
    super(fieldIndex);
  }
  
  @Override
  public VMCommand create(int index) throws ElException {
    return new I64FieldEquate(index);
  }
  
  @Override
  public void execute() throws ElException {
    objectStack[stackPointer - 1].fields[fieldIndex]
        .i64Set(i64Stack[stackPointer]);
    stackPointer--;
    currentCommand++;
  }
}
