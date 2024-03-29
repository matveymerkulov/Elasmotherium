package ast.function;

import ast.ClassEntity;
import ast.Entity;
import ast.ID;
import ast.IDEntity;
import exception.ElException;
import exception.EntityException;
import vm.VMFieldCommand;
import vm.object.ObjectVarPush;

public class Method extends StaticFunction {
  public Method(IDEntity name) {
    super(name);
  }

  public static CustomFunction createMethod(IDEntity id) {
    return allocateFunction(new Method(id));
  }
  
  // properties

  @Override
  public boolean isMethod() {
    return true;
  }
  
  @Override
  public boolean isValue(ID name, boolean isThis) {
    return this.name == name && fromParametersQuantity == 0;
  }
  
  // compiling

  @Override
  public void compileCall(FunctionCall call) throws EntityException {
    if(name != null) printChapter(name.string);
    call.resolveParameters(parameters);
    append();
  }

  @Override
  public void resolveTo(Entity type) throws EntityException {
    append(new ObjectVarPush(VMFieldCommand.OBJECT, 0, this));
    resolveChild(type);
  }

  @Override
  public void resolveChild(Entity type) throws EntityException {
    if(log) println(subIndent + "Resolving method " + this);
    append();
    try {
      convert(returnType.getNativeClass(), type.getNativeClass());
    } catch (ElException ex) {
      throw new EntityException(this, ex.message);
    }
  }
  
  // moving functions

  @Override
  public void moveToClass(ClassEntity classEntity) {
    classEntity.addMethod(this);
  }
}
