package com.ibm.wala.util;

/**
 * Use this interface to decouple core utilities from the Eclipse layer
 */
public interface IProgressMonitor {

  void beginTask(String task, int totalWork);

  boolean isCanceled();

  void done();

  void worked(int units);
}
