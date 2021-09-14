package vm.texture;

import static base.Base.log;
import base.ElException;
import vm.VMBase;
import static vm.VMBase.currentCommand;
import vm.VMCommand;

public class TextureHeight extends VMCommand {
  @Override
  public void execute() throws ElException {
    i64Stack[stackPointer] = objectStack[stackPointer].getImage().getHeight();
    if(log) typeStack[stackPointer] = VMBase.ValueType.I64;
    currentCommand++;
  }
}