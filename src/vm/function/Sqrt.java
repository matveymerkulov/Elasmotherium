package vm.function;

import exception.ElException;
import vm.VMCommand;

public class Sqrt extends VMCommand {
  @Override
  public void execute() throws ElException {
    f64Stack[stackPointer] = Math.sqrt(f64Stack[stackPointer]);
    currentCommand++;
  }
}
