package processor.parameter;

import processor.parameter.ProParameter;
import ast.ClassEntity;
import ast.Entity;
import ast.exception.ElException;

public class ProClass extends ProParameter {
  private final ClassEntity entity;

  public ProClass(ClassEntity classEntity) {
    this.entity = classEntity;
  }

  @Override
  public Entity getValue() throws ElException {
    return entity;
  }
}
