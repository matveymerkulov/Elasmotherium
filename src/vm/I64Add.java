package vm;

public class I64Add extends VMCommand {
  @Override
  public void execute() {
    stackPointer--;
    i64Stack[stackPointer] += i64Stack[stackPointer + 1];
    currentCommand++;
  }
}
