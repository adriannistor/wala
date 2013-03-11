/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.ibm.wala.ssa;

import com.ibm.wala.types.ClassLoaderReference;
import com.ibm.wala.types.TypeReference;

public abstract class SSANewSymbolicInstruction extends SSAInstruction {
  private final int result;
  private TypeReference type;
  private int[] params;

  /**
   * Create a new-symbolic instruction to allocate a scalar.
   */
  protected SSANewSymbolicInstruction(int result, TypeReference type) throws IllegalArgumentException {
    super();
    if (type == null) {
      throw new IllegalArgumentException("type cannot be null");
    }
    this.type = type;
    this.result = result;
  }

  /**
   * Create a new-symbolic instruction to allocate an array.
   * 
   * @throws IllegalArgumentException
   *           if site is null
   * @throws IllegalArgumentException
   *           if params is null
   */
  protected SSANewSymbolicInstruction(int result, TypeReference type, int[] params) {
    super();
    if (params == null) {
      throw new IllegalArgumentException("params is null");
    }
    if (type == null) {
      throw new IllegalArgumentException("site is null");
    }
    assert type.isArrayType() || type.getClassLoader().getLanguage() != ClassLoaderReference.Java;
    this.result = result;
    this.type = type;
    this.params = new int[params.length];
    System.arraycopy(params, 0, this.params, 0, params.length);
  }

  @Override
  public SSAInstruction copyForSSA(SSAInstructionFactory insts, int[] defs, int[] uses) {
    return insts.NewSymbolicInstruction(defs == null ? result : defs[0], type);
  }

  @Override
  public String toString(SymbolTable symbolTable) {
    return getValueString(symbolTable, result) + " = new symbolic " + type;
    // add some program counter if we add this to Java syntax + "@";
  }

  @Override
  public void visit(IVisitor v) {
    if (v == null) {
      throw new IllegalArgumentException("v is null");
    }
    v.visitNewSymbolic(this);
  }

  @Override
  public int hashCode() {
    return result * 7027 + type.hashCode();
  }

  @Override
  public boolean isFallThrough() {
    return true;
  }

  public TypeReference getTypeReference() {
    return type;
  }

  /**
   * @see com.ibm.wala.ssa.SSAInstruction#getDef()
   */
  @Override
  public boolean hasDef() {
    return true;
  }

  @Override
  public int getDef() {
    return result;
  }

  @Override
  public int getDef(int i) {
    assert i == 0;
    return result;
  }

  @Override
  public int getNumberOfDefs() {
    return 1;
  }

  /**
   * @return TypeReference
   */
  public TypeReference getConcreteType() {
    return type;
  }

  @Override
  public int getNumberOfUses() {
    return 0;
  }

  /*
   * @see com.ibm.wala.ssa.Instruction#isPEI()
   */
  @Override
  public boolean isPEI() {
    return false;
  }
}
