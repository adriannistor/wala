package com.ibm.wala.cast.js.ssa;

import com.ibm.wala.ssa.SSAInstruction;
import com.ibm.wala.ssa.SSAInstructionFactory;
import com.ibm.wala.ssa.SymbolTable;

public class SetPrototype extends SSAInstruction {
  private final int object;
  private final int prototype;
  
  public SetPrototype(int object, int prototype) {
    this.object = object;
    this.prototype = prototype;
  }

  @Override
  public int getNumberOfUses() {
    return 2;
  }
  
  @Override
  public int getUse(int j) throws UnsupportedOperationException {
    assert j >= 0 && j <= 1;
    return (j==0)? object: prototype;
  }

  @Override
  public SSAInstruction copyForSSA(SSAInstructionFactory insts, int[] defs, int[] uses) {
    return ((JSInstructionFactory)insts).SetPrototype((uses != null ? uses[0] : object), (uses != null ? uses[1] : prototype));
  }

  @Override
  public String toString(SymbolTable symbolTable) {
    return "set_prototype(" + getValueString(symbolTable, object) + ", " + getValueString(symbolTable, prototype) + ")";
  }

  @Override
  public void visit(IVisitor v) {
    ((JSInstructionVisitor)v).visitSetPrototype(this);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + object;
    result = prime * result + prototype;
    return result;
  }

  @Override
  public boolean isFallThrough() {
    return true;
  }

}
