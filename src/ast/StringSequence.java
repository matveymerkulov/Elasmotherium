package ast;

import static ast.FunctionCall.resolve;
import static base.Base.currentProcessor;
import base.ElException;
import java.util.LinkedList;
import vm.StringAdd;
import vm.VMCommand;

public class StringSequence extends Value {
  public final static ID id = ID.get("stringSequence");
  
  private final LinkedList<Value> chunks = new LinkedList<>();
  
  // properties

  void add(Value value) {
    chunks.add(value);
  }

  // processor fields
  
  @Override
  public ID getObject() throws ElException {
    return id;
  }
  
  // processing
  
  @Override
  public void resolveAll() throws ElException {
    boolean isNotFirst = false;
    for(Value value: chunks) {
      currentProcessor.call(value, resolve, ClassEntity.String);
      if(isNotFirst) {
        VMCommand command = new StringAdd();
        append(command);
      } else {
        isNotFirst = true;
      }
    }
  }
  
  // moving functions
  
  @Override
  public void move(Entity entity) throws base.ElException {
    entity.moveToStringSequence(this);
  }
  
  // other

  @Override
  public String toString() {
    return listToString(chunks, " + ");
  }
}
