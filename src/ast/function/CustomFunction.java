package ast.function;

import ast.*;
import exception.ElException;
import exception.EntityException;
import exception.EntityException.Cannot;
import exception.NotFound;
import parser.EntityStack;
import vm.VMCommand;
import vm.call.ReturnVoid;

import java.util.LinkedList;

public abstract class CustomFunction extends Function {
  protected final LinkedList<Variable> parameters = new LinkedList<>();
  protected Code code = new Code();
  protected int startingCommand;
  protected int allocation = 0, variablesQuantity = 0;
  protected int fromParametersQuantity = 0, toParametersQuantity = 0;
  protected VMCommand command = null;
  
  // constructors
  
  public CustomFunction() {
    super(null, 0, 0);
  }
  
  public CustomFunction(IDEntity name) {
    super(name);
  }
  
  // properties

  public int getVariablesQuantity() {
    return variablesQuantity;
  }

  public int getStartingCommand() {
    return startingCommand;
  }

  public void setReturnType(Entity returnType) throws EntityException {
    throw new Cannot("set return type of", this);
  }

  @Override
  public boolean isFunction(ID name, int parametersQuantity) {
    return this.name == name
        && fromParametersQuantity <= parametersQuantity
        && parametersQuantity <= toParametersQuantity;
  }
  
  // allocation

  public void incrementAllocation() {
    allocation++;
  }

  public int getAllocation() {
    return allocation;
  }

  public void setAllocation() {
    allocation = Math.max(allocation, currentAllocation);
  }

  public void printAllocation(String fileName) {
    code.print(new StringBuilder(), fileName + ":" + allocation + " ");
  }
  
  // child objects

  public int addParameter(Variable variable) {
    int index = allocation;
    allocation++;
    parameters.add(variable);
    toParametersQuantity = parameters.size();
    if(variable.getValue() == null)
      fromParametersQuantity = toParametersQuantity;
    return index;
  } 
  
  public StaticFunction getFunction(ID id, int parametersQuantity)
      throws NotFound {
    return code.getFunction(id, parametersQuantity);
  }
  
  public void setCommand(VMCommand command) {
    this.command = command;
  }
  
  // properties
  
  @Override
  public ID getID() throws EntityException {
    return id;
  }

  public boolean isConstructor() {
    return false;
  }

  public boolean isMethod() {
    return false;
  }

  public Entity getParameter(int index) {
    return parameters.get(index);
  }

  public int getParametersQuantity() {
    return parameters.size();
  }

  public void setCode(Code code) {
    this.code = code;
    this.variablesQuantity = currentAllocation;
  }

  public void pushCode() {
    EntityStack.code.push(code);
  }
  
  // preprocessing

  public void processConstructor(ClassEntity classEntity) throws NotFound {
    for(Variable param: parameters)
      param.processField(classEntity, code);
  }

  public void processConstructors() throws NotFound {
    code.processConstructors();
  }
  
  public abstract void resolveTypes() throws EntityException;
  
  // compiling
  
  @Override
  public void compile() throws EntityException {
    if(command != null) return;
    if(name != null) printChapter(name.string);
    startingCommand = vm.VMBase.currentCommand + 1;
    CustomFunction oldFunction = currentFunction;
    currentFunction = this;
    allocateScope();
    for(Variable parameter: parameters) addToScope(parameter);
    code.processWithoutScope(getEndingCommand());
    deallocateScope();
    currentFunction = oldFunction;
  }
  
  public VMCommand getEndingCommand() throws EntityException {
    return new ReturnVoid(0, this);
  }
  
  public void processCode(VMCommand endingCommand) throws EntityException {
    code.processWithoutScope(endingCommand);
  }
  
  // moving functions

  @Override
  public void move(Entity entity) throws ElException {
    entity.moveToFunction(this);
  }

  @Override
  public void moveToBlock() {
    deallocateFunction();
  }
  
  // other

  @Override
  public String toString() {
    return name.string;
  }
  
  public void print(StringBuilder indent, String prefix, String name) {
    StringBuilder str = new StringBuilder();
    str.append(name).append("(");
    boolean isNotFirst = false;
    for(Variable parameter : parameters) {
      if(isNotFirst) str.append(", ");
      str.append(parameter.toParamString());
      isNotFirst = true;
    }
    str.append("):").append(allocation);
    if(command == null) {
      code.print(indent, prefix + str);
    } else {
      println(indent + prefix + str + ";");
    }
  }
  
  @Override
  public void print(StringBuilder indent, String prefix) {
    print(indent, prefix, name.string);
  }
}
