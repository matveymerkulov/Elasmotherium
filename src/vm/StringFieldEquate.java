package vm;

import base.ElException;

public class StringFieldEquate extends VMFieldCommand {
  public StringFieldEquate(int fieldIndex) {
    super(fieldIndex);
  }

  @Override
  public VMCommand create(int index) throws ElException {
    return new StringFieldEquate(index);
  }
  
  @Override
  public void execute() throws ElException {
    objectStack[stackPointer]
        .fields[fieldIndex].stringSet(stringStack[stackPointer]);
    currentCommand++;
  }
}
