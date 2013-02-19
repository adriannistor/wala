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
package com.ibm.wala.ssa;

import com.ibm.wala.classLoader.IMethod;
import com.ibm.wala.ipa.callgraph.Context;
import com.ibm.wala.ipa.callgraph.impl.Everywhere;

/**
 * An ISSACache implementation that does no caching.
 */

public class NoCachingSSACache implements ISSACache {
  /**
   * The factory that actually creates new IR objects
   */
  private final IRFactory<IMethod> factory;

  /**
   * @param factory
   *          a factory for creating IRs
   */
  public NoCachingSSACache(IRFactory<IMethod> factory) {
    this.factory = factory;
  }

  /*
   * @see
   * com.ibm.wala.ssa.ISSACache#findOrCreateIR(com.ibm.wala.classLoader.IMethod,
   * com.ibm.wala.ipa.callgraph.Context, com.ibm.wala.ssa.SSAOptions)
   */
  public IR findOrCreateIR(final IMethod m, Context c, final SSAOptions options) {

    if (m == null) {
      throw new IllegalArgumentException("m is null");
    }
    if (m.isAbstract() || m.isNative()) {
      return null;
    }

    if (factory.contextIsIrrelevant(m)) {
      c = Everywhere.EVERYWHERE;
    }

    return factory.makeIR(m, c, options);
  }

  /*
   * @see
   * com.ibm.wala.ssa.ISSACache#findOrCreateDU(com.ibm.wala.classLoader.IMethod,
   * com.ibm.wala.ipa.callgraph.Context, com.ibm.wala.ssa.SSAOptions)
   */
  public synchronized DefUse findOrCreateDU(IMethod m, Context c, SSAOptions options) {
    if (m == null) {
      throw new IllegalArgumentException("m is null");
    }
    if (m.isAbstract() || m.isNative()) {
      return null;
    }
    if (factory.contextIsIrrelevant(m)) {
      c = Everywhere.EVERYWHERE;
    }

    IR ir = findOrCreateIR(m, c, options);
    return new DefUse(ir);
  }

  /*
   * @see com.ibm.wala.ssa.ISSACache#findOrCreateDU(com.ibm.wala.ssa.IR,
   * com.ibm.wala.ipa.callgraph.Context)
   */
  public synchronized DefUse findOrCreateDU(IR ir, Context C) {
    if (ir == null) {
      throw new IllegalArgumentException("ir is null");
    }
    return new DefUse(ir);
  }

  /*
   * @see com.ibm.wala.ssa.ISSACache#wipe()
   */
  public void wipe() {
    // does nothing
  }

  /*
   * @see
   * com.ibm.wala.ssa.ISSACache#invalidateIR(com.ibm.wala.classLoader.IMethod,
   * com.ibm.wala.ipa.callgraph.Context)
   */
  public void invalidateIR(IMethod method, Context c) {
    // does nothing
  }

  /*
   * @see
   * com.ibm.wala.ssa.ISSACache#invalidateDU(com.ibm.wala.classLoader.IMethod,
   * com.ibm.wala.ipa.callgraph.Context)
   */
  public void invalidateDU(IMethod method, Context c) {
    // does nothing
  }

  /*
   * @see
   * com.ibm.wala.ssa.ISSACache#invalidate(com.ibm.wala.classLoader.IMethod,
   * com.ibm.wala.ipa.callgraph.Context)
   */
  public void invalidate(IMethod method, Context c) {
    invalidateIR(method, c);
    invalidateDU(method, c);
  }
}
