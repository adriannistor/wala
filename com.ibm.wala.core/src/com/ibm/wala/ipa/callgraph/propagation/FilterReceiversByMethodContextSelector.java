/*******************************************************************************
 * Copyright (c) 2002 - 2006 IBM Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.ibm.wala.ipa.callgraph.propagation;

import com.ibm.wala.classLoader.CallSiteReference;
import com.ibm.wala.classLoader.IMethod;
import com.ibm.wala.ipa.callgraph.CGNode;
import com.ibm.wala.ipa.callgraph.Context;
import com.ibm.wala.ipa.callgraph.ContextItem;
import com.ibm.wala.ipa.callgraph.ContextKey;
import com.ibm.wala.ipa.callgraph.ContextSelector;
import com.ibm.wala.ipa.callgraph.impl.Everywhere;
import com.ibm.wala.ipa.cha.IClassHierarchy;
import com.ibm.wala.util.intset.EmptyIntSet;
import com.ibm.wala.util.intset.IntSet;
import com.ibm.wala.util.intset.IntSetUtil;

/**
 * This context selector selects a context based on whether the receiver type dispatches to a given method.
 */
public class FilterReceiversByMethodContextSelector implements ContextSelector {

  // private final Selector selector;

  public FilterReceiversByMethodContextSelector(IClassHierarchy cha) {
    // this.selector = selector;
  }

  public Context getCalleeTarget(CGNode caller, CallSiteReference site, IMethod callee, InstanceKey[] R) {
    if (R == null || R[0] == null) {
      throw new IllegalArgumentException("R is null");
    }

    if(site.isStatic())
      return Everywhere.EVERYWHERE;
    
    
    final IMethod M = R[0].getConcreteType().getMethod(site.getDeclaredTarget().getSelector());
    
    if(M == null)
      return Everywhere.EVERYWHERE; 

    class MethodDispatchContext implements Context {

      private IMethod getTargetMethod() {
        return M;
      }

      public ContextItem get(ContextKey name) {
        if (name.equals(ContextKey.PARAMETERS[0])) {
          return new FilteredPointerKey.TargetMethodFilter(M);
        } else {
          return null;
        }
      }

      @Override
      public String toString() {
        return "DispatchContext: " + M;
      }

      @Override
      public int hashCode() {
        return M.hashCode();
      }

      @Override
      public boolean equals(Object o) {
        return (o instanceof MethodDispatchContext) && ((MethodDispatchContext) o).getTargetMethod().equals(M);
      }
    }
    ;

    return new MethodDispatchContext();
  }

  private static final IntSet thisParameter = IntSetUtil.make(new int[] { 0 });

  public IntSet getRelevantParameters(CGNode caller, CallSiteReference site) {
    if(site.isStatic())
      return EmptyIntSet.instance;
    else
      return thisParameter;
  }

}
