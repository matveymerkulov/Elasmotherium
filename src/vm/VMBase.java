package vm;

import vm.values.ObjectEntity;
import base.Base;
import base.ElException;
import java.util.Arrays;
import javax.swing.JFrame;

public class VMBase extends Base{
  private static final int STACK_SIZE = 16, COMMANDS_SIZE = 256;
  
  static boolean[] booleanStack = new boolean[STACK_SIZE];
  static ValueType[] typeStack = new ValueType[STACK_SIZE];
  static long[] i64Stack = new long[STACK_SIZE];
  static String[] stringStack = new String[STACK_SIZE];
  static ObjectEntity[] objectStack = new ObjectEntity[STACK_SIZE];
  static VMFunctionCall[] callStack = new VMFunctionCall[STACK_SIZE];
  static int stackPointer = -1, callStackPointer = -1;
  static VMFunctionCall currentCall = new VMFunctionCall(0, 0);
  static VMCommand[] commands = new VMCommand[COMMANDS_SIZE];
  static JFrame frame;
  static boolean usesWindow = false, usesConsole = false;
  
  public static int currentCommand = -1;
  
  public enum ValueType {UNDEFINED, BOOLEAN, I64, STRING, OBJECT};
  
  public static void append(VMCommand command) {
    currentCommand++;
    commands[currentCommand] = command;
  }
  
  public static void appendLog(VMCommand command) {
    if(log) 
    append(command);
  }

  public static void prepare() {
    if(log) {
      Arrays.fill(typeStack, ValueType.UNDEFINED);
      if(log) printChapter("Generated bytecode");
      for(int index = 0; index <= currentCommand; index++)
        System.out.println(String.format("%03d", index)
            + ": " + commands[index].toString());
    }
  }
    
  public static void execute(boolean showCommands) {
    if(usesWindow) {
      frame = new JFrame();
      frame.setVisible(true);
      frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
    }
      
    if(log) printChapter("Bytecode execution");
    currentCommand = 0;
    while(true) {
      if(showCommands) System.out.println(commands[currentCommand].toString());

      try {
        commands[currentCommand].execute();
      } catch (ElException ex) {
        error("Bytecode execution error", ex.message);
      }

      if(showCommands) {
        String stack = "";
        for(int index = 0; index <= stackPointer; index++)
          switch(typeStack[index]) {
            case UNDEFINED:
              stack += "- ";
              break;
            case BOOLEAN:
              stack += booleanStack[index] ? "true " : "false ";
              break;
            case I64:
              stack += i64Stack[index] + " ";
              break;
            case STRING:
              stack += "\"" + stringStack[index] + "\" ";
              break;
            case OBJECT:
              stack += objectStack[index].type.toString() + " ";
              break;
          }
        System.out.println("Stack: " + stack);
      }
    }
  }
}
