package parser;

import ast.function.NativeFunction;
import base.Base;
import base.LineReader;
import base.SymbolReader;
import exception.ElException;
import exception.ElException.MethodException;
import exception.ElException.ParserException;
import exception.EntityException;
import exception.NotFound;

import java.util.HashMap;
import java.util.LinkedList;

public class Rules extends Base {
  public final String fileName;

  public Rules(String fileName) {
    this.fileName = fileName;
  }
  
  // masks
  
  private final HashMap<String, SymbolMask> masks = new HashMap<>();
  
  private SymbolMask getMask(String name) throws NotFound {
    SymbolMask mask = masks.get(name);
    if(mask == null) throw new NotFound("symbol mask \"" + name + "\""
      , fileName);
    return mask;
  }
  
  // subs
  
  private final HashMap<String, Sub> subs = new HashMap<>();

  private Sub getSub(String name) {
    Sub sub = subs.get(name);
    if(sub == null) {
      sub = new Sub(name, null);
      subs.put(name, sub);
    }
    return sub;
  }
  
  // fields
  
  private Sub root;
  private final HashMap<String, Error> errors = new HashMap<>();
  private final LinkedList<String> defSym = new LinkedList<>();
  
  // loading 
  
  public Rules load() {
    masks.clear();
    masks.put("tab", new SymbolMask('\t'));
    masks.put("space", new SymbolMask(' '));
    masks.put("newline", new SymbolMask('\n').set('\r'));
    masks.put("eof", new SymbolMask(129));
    
    subs.clear();
    
    try {
      currentLineReader = new LineReader(fileName);
      String line;
      while((line = currentLineReader.readLine()) != null) {
        if(line.startsWith("ERROR ")) {
          String[] parts = line.substring(6).split(":");
          errors.put(parts[0].trim(), new Error(parts[1].trim()));
        } else if(line.startsWith("DEFAULT ")) {
          defSym.add(line.substring(8));
        } else if(line.startsWith("FUNCTION ")) {
          String[] part = trimmedSplit(line.substring(9), ',');
          NativeFunction.create(part[0], Byte.parseByte(part[1])
              , part.length < 3 ? "" : stringParam(part[2]));
        } else {
          int equalPos = line.indexOf('=');
          int colonPos = line.indexOf(':');
          if(equalPos >= 0 && (equalPos < colonPos || colonPos < 0)) {
            SymbolMask mask = new SymbolMask();
            for(String symbol : line.substring(equalPos + 1).trim().split(" ")) {
              if(symbol.length() == 1) {
                mask.set(symbol.charAt(0));
              } else if(symbol.length() == 3 && symbol.charAt(1) == '-') {
                mask.set(symbol.charAt(0), symbol.charAt(2));
              } else if(symbol.length() >= 2) {
                SymbolMask mask2 = masks.get(symbol);
                if(mask2 == null) throw new NotFound("Mask \"" + symbol + "\""
                    , fileName);
                mask.or(mask2);
              }
            }
            masks.put(line.substring(0, equalPos).trim(), mask);
          } else {
            if(colonPos < 0 && equalPos < 0)
              throw new MethodException("Rules" ,"load", ": or = expected");
            String name = line.substring(0, colonPos).trim();
            Sub sub = getSub(name);
            if(sub.action != null) throw new MethodException("Rules" ,"load"
                , "Sub \"" + name + "\" is already defined");
            sub.action = actionChain(line.substring(colonPos + 1), null, sub);
          }
        }
      }
    } catch (ElException ex) {
      currentLineReader.showDebugMessage(ex.message);
    } catch (NotFound ex) {
      currentLineReader.showDebugMessage(ex.message);
    }
    
    root = subs.get("root");
    return this;
  }
  
  // actions
  
  private static final HashMap<String, Action> actions = new HashMap<>();
  
  static {
    actions.put("RETURN", new ActionReturn());
    actions.put("SKIP", new ActionSkip());
    actions.put("SAVEPOS", new ActionSavePos());
    actions.put("LOADPOS", new ActionLoadPos());
    actions.put("ADD", new ActionAdd(""));
    actions.put("EXPECT", new ActionExpect(' ', ""));
    actions.put("CLEAR", new ActionClear());
    actions.put("CREATE", new ActionCreate(null, null, null));
    actions.put("CBV", new ActionCreateBlockVariable(null));
    actions.put("DUP", new ActionDup(null));
    actions.put("COPY", new ActionMove(null, null, true));
    actions.put("MOVE", new ActionMove(null, null, false));
    actions.put("SET", new ActionSet(null, null));
    actions.put("REMOVE", new ActionRemove(null));
    actions.put(">>", new ActionForward());
  }
  
  private Action actionChain(String commands, Action lastAction, Sub currentSub)
      throws ElException, NotFound {
    Action firstAction = null, currentAction = null;
    boolean exit = false;
    for(String command : listSplit(commands, ' ')) {
      Action action;
      int bracketPos = command.indexOf('(');
      String name = bracketPos < 0 ? command : command.substring(0, bracketPos);
      String params = bracketPos < 0 ? "" : command.substring(bracketPos + 1
          , command.length() - 1);
      
      if(name.equals("{")) {
        ActionSwitch switchAction = new ActionSwitch();
        String line;
        Action back = new ActionGoToAction(switchAction);
        for(String string: defSym)
          parseLine(string, switchAction, back, currentSub);
        while(true) {
          if((line = currentLineReader.readLine()) == null)
            throw new MethodException("Rules", "actionChain"
                , "Unexpected end of file");
          if(line.equals("}")) break;
          parseLine(line, switchAction, back, currentSub);
        }
        action = switchAction;
        exit = true;
      } else {
        action = actions.get(name);
        if(action != null) {
          action = action.create(params);
        } else {
          Error error = errors.get(name);
          if(error == null) {
            //System.out.println(name);
            if(bracketPos < 0) {
              action = new ActionGoToSub(getSub(name));
            } else {
              action = new ActionSub(getSub(name), currentSub
                  , params.isEmpty() ? null : getSub(params));
            }
          } else {
            if(!params.isEmpty()) error = error.derive(stringParam(params));
            action = error;
          }
        }
      }
      //action.parserLine = lineNum;
      if(currentAction == null) {
        firstAction = action;
      } else {
        currentAction.nextAction = action;
      }
      currentAction = action;
      if(exit) break;
    }
    if(lastAction != null) currentAction.nextAction = lastAction;
    return firstAction;
  }
  
  private void parseLine(String line, ActionSwitch switchAction, Action back
      , Sub currentSub) throws ElException, NotFound {
    LinkedList<String> parts = listSplit(line, ':');
    if(parts.size() < 2)
      throw new ParserException(": expected before commands");
    Action actionChain = actionChain(parts.getLast(), back, currentSub);
    for(String token: listSplit(parts.getFirst(), ',')) {
      if(token.startsWith("\"")) {
        switchAction.setStringAction(stringParam(token), actionChain);
      } else if(token.equals("other")) {
        switchAction.setOtherAction(actionChain);
      } else {
        SymbolMask symbolMask = getMask(token);
        switchAction.setMaskAction(symbolMask, actionChain);
        //break;
      }
    }
  }
  
  // parsing code
  
  public void parseCode(StringBuffer text, String fileName) {
    currentSymbolReader = new SymbolReader(text, fileName);
    currentFunction.pushCode();
    
    try {
      NativeFunction.dot = NativeFunction.get("dot");
      NativeFunction.callFunction = NativeFunction.get("callFunction");
      NativeFunction.equate = NativeFunction.get("equate");
      NativeFunction.at = NativeFunction.get("at");

      Action.currentAction = root.action;
      while(Action.currentAction != null)
        Action.currentAction.execute();
    
      EntityStack.code.clear();
    } catch (EntityException ex) {
      currentSymbolReader.showDebugMessage(ex.message);
    } catch (ElException ex) {
      currentSymbolReader.showDebugMessage(ex.message);
    } catch (NotFound ex) {
      currentSymbolReader.showDebugMessage(ex.message);
    }
  }
}
