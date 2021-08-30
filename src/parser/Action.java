package parser;

import base.ElException;

public abstract class Action extends ParserBase {
  static int savedTextPos, savedLineNum, savedLineStart;
  static Action currentAction;
  
  private final int parserLine;
  Action nextAction;

  public Action() {
    parserLine = currentLineNum;
  }
  
  public Action create(String params) throws ElException {
    throw new ElException("No creating funciton for " + toString());
  }
  
  public abstract void execute() throws ElException;

  public Sub getErrorActionSub() {
    while(!returnStack.isEmpty()) {
      ActionSub action = returnStack.pop();
      if(action.errorSub != null) return action.errorSub;
    }
    return null;
  }
  
  public void log(String message) {
    System.out.println(subIndent + parserLine + ": " + message);
  }

  public String errorString() {
    return "parser code (" + parserLine + ")\n";
  }

  @Override
  public String toString() {
    return getClassName();
  }
}
