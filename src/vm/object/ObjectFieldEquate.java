package vm.object;

import vm.VMCommand;

public class ObjectFieldEquate extends VMCommand {
  private final int stackIndex, fieldIndex;

  public ObjectFieldEquate(int stackIndex, int fieldIndex) {
    this.stackIndex = stackIndex;
    this.fieldIndex = fieldIndex;
  }
  
  @Override
  public void execute() {
    //objectStack[stackIndex + currentCall.paramPosition]
    //    .objectSet(objectStack[stackPointer]);
    stackPointer--;
    currentCommand++;
  }
  
  @Override
  public String toString() {
    return super.toString() + " " + stackIndex + " " + fieldIndex;
  }
}