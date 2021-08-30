package vm;

import base.ElException;
import processor.ProParameter;

public class GoTo extends VMCommand {
  private int command;
  
  @Override
  public GoTo create(ProParameter parameter) throws ElException {
    GoTo goTo = new GoTo();
    parameter.addLabelCommand(goTo);
    return goTo;
  }
  
  @Override
  public void setGoto(int command) {
    this.command = command;
  }
  
  @Override
  public void execute() {
    currentCommand = command;
  }
  
  @Override
  public String toString() {
    return super.toString() + " " + command;
  }
}