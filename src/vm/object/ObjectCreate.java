package vm.object;

import ast.ClassEntity;
import base.ElException;
import vm.VMCommand;

public class ObjectCreate extends VMCommand {
  private final ClassEntity classEntity;

  public ObjectCreate(ClassEntity classEntity) {
    this.classEntity = classEntity;
  }

  @Override
  public void execute() throws ElException {
    stackPointer++;
    objectStack[stackPointer] = classEntity.newObject();
    if(log) typeStack[stackPointer] = ValueType.OBJECT;
    currentCommand++;
  }
  
  @Override
  public String toString() {
    return super.toString() + " " + classEntity.toString();
  }
}