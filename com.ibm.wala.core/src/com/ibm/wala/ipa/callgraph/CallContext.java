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
package com.ibm.wala.ipa.callgraph;

import java.util.Map;

/**
 * A better Context interface that actually behaves like a map between ContextKey's and ContextItem's
 * 
 * This will facilitate better implementations for comparing contexts when the ContextItem's are not known beforehand.
 * 
 */
public interface CallContext extends Context, Map<ContextKey,ContextItem>{
  /**
   * @return the objects corresponding to a given name
   */
  ContextItem get(ContextKey name);
}