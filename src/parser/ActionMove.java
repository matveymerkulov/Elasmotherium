package parser;

import ast.Entity;
import ast.ID;
import ast.function.NativeFunction;
import exception.ElException;
import exception.ElException.ActionException;
import exception.EntityException;
import exception.NotFound;

public class ActionMove extends Action {
  private final EntityStack<Entity> from, to;
  private final boolean copy;

  public ActionMove(EntityStack<Entity> from, EntityStack<Entity> to
      , boolean copy) {
    this.from = from;
    this.to = to;
    this.copy = copy;
  }
  
  @Override
  public Action create(String params) throws ElException {
    String[] param = params.split(",");
    EntityStack<Entity> stack0 = EntityStack.all.get(ID.get(param[0]));
    if(stack0 == null) {
      try {
        return new ActionMoveNewFunction(
            NativeFunction.get(param[0]), EntityStack.get(param[1]));
      } catch (NotFound ex) {
        throw new ActionException(this, "MOVE", ex.message);
      }
    } else {
      if(param.length == 1) {
        return new ActionMove(stack0, stack0, copy);
      } else {
        if(param.length != 2) throw new ActionException(this, 
            "MOVE", "requires 2 parameters");
        return new ActionMove(stack0, EntityStack.get(param[1]), copy);
      }
    }
  }
  
  @Override
  public void execute() throws ElException, EntityException {
    currentAction = this;
    Entity entity = copy ? from.peek() : from.pop();
    if(log) log("MOVING " + entity + " to " + to + "("
        + to.peek().toString() + ")");
    to.peek().move(entity);
    currentAction = nextAction;
  }

  @Override
  public String toString() {
    return from.name + " to " + to.name;
  }
}
