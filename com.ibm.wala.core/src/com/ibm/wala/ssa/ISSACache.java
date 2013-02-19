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

import com.ibm.wala.classLoader.IMethod;
import com.ibm.wala.ipa.callgraph.Context;

public interface ISSACache {

  /**
   * @param m a "normal" (bytecode-based) method
   * @param options options governing ssa construction
   * @return an IR for m, built according to the specified options. null if m is abstract or native.
   * @throws IllegalArgumentException if m is null
   */
  public abstract IR findOrCreateIR(IMethod m, Context c, SSAOptions options);

  /**
   * @param m a method
   * @param options options governing ssa construction
   * @return DefUse information for m, built according to the specified options. null if unavailable
   * @throws IllegalArgumentException if m is null
   */
  public abstract DefUse findOrCreateDU(IMethod m, Context c, SSAOptions options);

  /**
   * @return {@link DefUse} information for m, built according to the specified options. null if unavailable
   * @throws IllegalArgumentException if ir is null
   */
  public abstract DefUse findOrCreateDU(IR ir, Context C);

  /**
   * The existence of this is unfortunate.
   */
  public abstract void wipe();

  /**
   * Invalidate the cached IR for a <method,context> pair
   */
  public abstract void invalidateIR(IMethod method, Context c);

  /**
   * Invalidate the cached {@link DefUse} for a <method,context> pair
   */
  public abstract void invalidateDU(IMethod method, Context c);

  /**
   * Invalidate all cached information for a <method,context> pair
   */
  public abstract void invalidate(IMethod method, Context c);

}