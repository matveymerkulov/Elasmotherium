package ast;

import base.ElException;
import base.ElException.MethodException;

public class ConstantValue extends Value {
  public static final ID id = ID.get("const");
  public static final ID intID = ID.get("int");
  public static final ID stringID = ID.get("string");
  
  private final ID type;
  private final String value;

  // creating
  
  public ConstantValue(ID type, String value) {
    this.type = type;
    this.value = value;
  }
  
  // properties

  @Override
  public ID getID() throws ElException {
    return id;
  }
  
  @Override
  public Entity getType() throws ElException {
    if(type == intID) return ClassEntity.Int;
    if(type == stringID) return ClassEntity.String;
    throw new MethodException(this, "getType", "Unknown const type " + type);
  }
  
  @Override
  public String getStringValue() throws ElException {
    return value;
  }
  
  // moving functions

  @Override
  public void moveToStringSequence(StringSequence sequence) {
    if(!value.isEmpty()) sequence.add(this);
  }
  
  // other

  @Override
  public String toString() {
    return type == stringID ? "\"" + value + "\"" : type + "(" + value + ")";
  }
}
