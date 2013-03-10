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
package com.ibm.wala.ipa.callgraph.propagation;

import java.util.Iterator;

import com.ibm.wala.classLoader.IClass;
import com.ibm.wala.classLoader.NewSiteReference;
import com.ibm.wala.ipa.callgraph.CGNode;
import com.ibm.wala.ipa.callgraph.CallGraph;
import com.ibm.wala.util.collections.EmptyIterator;
import com.ibm.wala.util.collections.Pair;
import com.ibm.wala.util.debug.Assertions;

/**
 * @author cos
 *
 */
public class SymbolicTypeKey implements InstanceKey {

  private IClass type;

  public SymbolicTypeKey(IClass type) {
    if (type == null) {
      throw new IllegalArgumentException("type is null");
    }
    this.type = type;
  }
  

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof SymbolicTypeKey) {
      SymbolicTypeKey other = (SymbolicTypeKey) obj;
      return type.equals(other.type);
    } else {
      return false;
    }
  }
  
  @Override
  public int hashCode() {
    return 3677 * type.hashCode();
  }

  @Override
  public String toString() {
    return "S[" + type + "]";
  }

  public IClass getType() {
    return type;
  }
  
  /* 
   * Implements getConcreteType() in order to conform to the interface, but this is not necessarily a concrete type.
   */
  public IClass getConcreteType() {
    return type;
  }

  /* 
   * We do not track creation sites for new-symbolic instructions for now 
   */
  public Iterator<Pair<CGNode, NewSiteReference>> getCreationSites(CallGraph CG) {
     return EmptyIterator.instance();
  }
}
