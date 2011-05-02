/******************************************************************************
 * Copyright (c) 2002 - 2006 IBM Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *****************************************************************************/
package com.ibm.wala.cast.ir.ssa;

import java.util.Collection;

import com.ibm.wala.ssa.SSAInstruction;
import com.ibm.wala.types.TypeReference;

/**
 *  This abstract class provides helper functionality for recording
 * lexical uses and/or definitions.  It is used in lexical read and
 * write instructions
 *
 * @author Julian Dolby (dolby@us.ibm.com)
 */
public abstract class AstLexicalAccess extends SSAInstruction {

  /**
   * A single lexical access.
   *
   * @author Julian Dolby (dolby@us.ibm.com)
   */
  public static class Access {
    /**
     * name being accessed
     */
    public final String variableName;
    /**
     * name of entity that defines the variable
     */
    public final String variableDefiner; 
    /**
     * value number used for name where access is being performed (not in the declaring entity)
     */
    public final int valueNumber;

    public Access(String name, String definer, int vn) {
      variableName = name;
      variableDefiner = definer;
      valueNumber = vn;
    }

    public int hashCode() {
      return variableName.hashCode() * valueNumber;
    }

    public boolean equals(Object other) {
      return (other instanceof Access) &&
	variableName.equals( ((Access)other).variableName ) &&
	valueNumber == ((Access)other).valueNumber &&
	( variableDefiner == null?
	  ((Access)other).variableDefiner == null:
	  variableDefiner.equals(((Access)other).variableDefiner) );
    }

    public String toString() {
      return "Access(" + variableName + "@" + variableDefiner + ":" + valueNumber + ")";
    }
  }

  private Access[] accesses;

  AstLexicalAccess(Access[] accesses) {
    setAccesses( accesses );
  }

  public void setAccesses(Access[] accesses) {
    this.accesses = accesses;
  }

  public Access[] getAccesses() {
    return accesses; 
  }

  public Access getAccess(int i) {
    return accesses[i]; 
  }

  public int getAccessCount() {
    return accesses.length; 
  }

  public boolean isFallThrough() {
    return true;
  }

  public Collection<TypeReference> getExceptionTypes() {
    return null;
  }

  public int hashCode() {
    int v = 1;
    for(int i = 0; i < accesses.length; i++) 
      v *= accesses[i].variableName.hashCode();

    return v;
  }

}
