package ast;

import exception.ElException;
import exception.EntityException;
import java.util.LinkedList;

public class StringSequence extends Value {
  public final static ID id = ID.get("stringSequence");
  
  private final LinkedList<Value> chunks = new LinkedList<>();

  public StringSequence() {
    super(0, 0);
  }
  
  // child objects

  void add(Value value) {
    chunks.add(value);
  }

  // properties
  
  @Override
  public ID getID() throws EntityException {
    return id;
  }
  
  // compiling
  
  @Override
  public void resolveTo(Entity parameter) throws EntityException {
    boolean isNotFirst = false;
    for(Value value: chunks) {
      value.resolveTo(ClassEntity.String);
      if(isNotFirst) {
        append(new vm.string.StringAdd());
      } else {
        isNotFirst = true;
      }
    }
  }
  
  // moving functions
  
  @Override
  public void move(Entity entity) throws ElException {
    entity.moveToStringSequence(this);
  }
  
  // other

  @Override
  public String toString() {
    return listToString(chunks, " + ");
  }
}