package inflorit;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import com.ibm.wala.analysis.pointers.HeapGraph;
import com.ibm.wala.classLoader.CallSiteReference;
import com.ibm.wala.classLoader.IClass;
import com.ibm.wala.classLoader.IMethod;
import com.ibm.wala.core.tests.callGraph.CallGraphTest;
import com.ibm.wala.core.tests.callGraph.CallGraphTestUtil;
import com.ibm.wala.core.tests.util.TestConstants;
import com.ibm.wala.core.tests.util.WalaTestCase;
import com.ibm.wala.ipa.callgraph.AnalysisCache;
import com.ibm.wala.ipa.callgraph.AnalysisOptions;
import com.ibm.wala.ipa.callgraph.AnalysisScope;
import com.ibm.wala.ipa.callgraph.CGNode;
import com.ibm.wala.ipa.callgraph.CallGraph;
import com.ibm.wala.ipa.callgraph.CallGraphBuilder;
import com.ibm.wala.ipa.callgraph.ContextSelector;
import com.ibm.wala.ipa.callgraph.DelegatingContext;
import com.ibm.wala.ipa.callgraph.Entrypoint;
import com.ibm.wala.ipa.callgraph.impl.ClassHierarchyClassTargetSelector;
import com.ibm.wala.ipa.callgraph.impl.ClassHierarchyMethodTargetSelector;
import com.ibm.wala.ipa.callgraph.impl.ContextInsensitiveSelector;
import com.ibm.wala.ipa.callgraph.impl.DefaultContextSelector;
import com.ibm.wala.ipa.callgraph.impl.DefaultEntrypoint;
import com.ibm.wala.ipa.callgraph.impl.DelegatingContextSelector;
import com.ibm.wala.ipa.callgraph.impl.Util;
import com.ibm.wala.ipa.callgraph.propagation.FilterReceiversByMethodContextSelector;
import com.ibm.wala.ipa.callgraph.propagation.HeapModel;
import com.ibm.wala.ipa.callgraph.propagation.InstanceFieldKey;
import com.ibm.wala.ipa.callgraph.propagation.InstanceKey;
import com.ibm.wala.ipa.callgraph.propagation.LocalPointerKey;
import com.ibm.wala.ipa.callgraph.propagation.PointerAnalysis;
import com.ibm.wala.ipa.callgraph.propagation.PointerKey;
import com.ibm.wala.ipa.callgraph.propagation.TargetMethodContextSelector;
import com.ibm.wala.ipa.callgraph.propagation.cfa.DefaultSSAInterpreter;
import com.ibm.wala.ipa.callgraph.propagation.cfa.ZeroXCFABuilder;
import com.ibm.wala.ipa.callgraph.propagation.cfa.ZeroXInstanceKeys;
import com.ibm.wala.ipa.cha.ClassHierarchy;
import com.ibm.wala.ipa.cha.ClassHierarchyException;
import com.ibm.wala.ipa.cha.IClassHierarchy;
import com.ibm.wala.types.MethodReference;
import com.ibm.wala.types.Selector;
import com.ibm.wala.types.TypeName;
import com.ibm.wala.types.TypeReference;
import com.ibm.wala.util.CancelException;
import com.ibm.wala.util.graph.GraphPrint;
import com.ibm.wala.util.graph.GraphSlicer;
import com.ibm.wala.util.graph.GraphUtil;
import com.ibm.wala.util.graph.traverse.DFS;

public class InfloritTestCase extends WalaTestCase {

  public static Set<Entrypoint> makeEntryPoint(AnalysisScope scope, IClassHierarchy cha, String entryClass, String entryMethod) {
    // val typeReference: TypeReference =
    // TypeReference.findOrCreate(scope.getLoader(AnalysisScope.Application),
    // TypeName.string2TypeName(entryClass))
    // val methodReference: MethodReference =
    // MethodReference.findOrCreate(typeReference,
    // entryMethod.substring(0, entryMethod.indexOf('(')),
    // entryMethod.substring(entryMethod.indexOf('(')))
    // new DefaultEntrypoint(methodReference, cha)

    TypeReference typeReference = TypeReference.findOrCreate(scope.getLoader(AnalysisScope.APPLICATION),
        TypeName.string2TypeName(entryClass));
    MethodReference methodReference = MethodReference.findOrCreate(typeReference, Selector.make(entryMethod));
    DefaultEntrypoint defaultEntrypoint = new DefaultEntrypoint(methodReference, cha);
    defaultEntrypoint.makeSymbolicParameter(0);
    return Collections.singleton((Entrypoint) defaultEntrypoint);
  }

  @Test
  public void testBasic() throws ClassHierarchyException, IllegalArgumentException, CancelException, IOException {
    AnalysisScope scope = CallGraphTestUtil.makeJ2SEAnalysisScope("basic.txt", CallGraphTestUtil.REGRESSION_EXCLUSIONS);
    ClassHierarchy cha = ClassHierarchy.make(scope);
    Iterable<Entrypoint> entrypoints = makeEntryPoint(scope, cha, "Lparticle/Basic", "test0()V");
    AnalysisOptions options = CallGraphTestUtil.makeAnalysisOptions(scope, entrypoints);

    CallGraphTest.doCallGraphs(options, new AnalysisCache(), cha, scope, true);
  }

  @Test
  public void testBasic1() throws ClassHierarchyException, IllegalArgumentException, CancelException, IOException {
    AnalysisScope scope = CallGraphTestUtil.makeJ2SEAnalysisScope("basic.txt", CallGraphTestUtil.REGRESSION_EXCLUSIONS);
    ClassHierarchy cha = ClassHierarchy.make(scope);
    Iterable<Entrypoint> entrypoints = makeEntryPoint(scope, cha, "Lparticle/Basic", "test1()V");
    AnalysisOptions options = CallGraphTestUtil.makeAnalysisOptions(scope, entrypoints);

    CallGraphTest.doCallGraphs(options, new AnalysisCache(), cha, scope, true);
  }

  @Test
  public void testMain() throws ClassHierarchyException, IllegalArgumentException, CancelException, IOException {
    AnalysisScope scope = CallGraphTestUtil.makeJ2SEAnalysisScope("basic.txt", CallGraphTestUtil.REGRESSION_EXCLUSIONS);
    ClassHierarchy cha = ClassHierarchy.make(scope);
    Iterable<Entrypoint> entrypoints = makeEntryPoint(scope, cha, "Lparticle/Basic", "main([Ljava/lang/String;)V");
    AnalysisOptions options = CallGraphTestUtil.makeAnalysisOptions(scope, entrypoints);

    CallGraphTest.doCallGraphs(options, new AnalysisCache(), cha, scope, true);
  }

  @Test
  public void testCircle02() throws ClassHierarchyException, IllegalArgumentException, CancelException, IOException {
    AnalysisScope scope = CallGraphTestUtil.makeJ2SEAnalysisScope("basic.txt", CallGraphTestUtil.REGRESSION_EXCLUSIONS);
    ClassHierarchy cha = ClassHierarchy.make(scope);
    Iterable<Entrypoint> entrypoints = makeEntryPoint(scope, cha, "Lparticle/Circle02", "analyzeMe(Lparticle/Point01;)V");

    AnalysisOptions analysisOptions = CallGraphTestUtil.makeAnalysisOptions(scope, entrypoints);
    AnalysisCache analysisCache = new AnalysisCache();

    System.out.println(cha.getNumberOfClasses());

//     CallGraphBuilder cgBuilder = Util.makeZeroCFABuilder(analysisOptions,
//     analysisCache, cha, scope);
    Util.addDefaultSelectors(analysisOptions, cha);
    CallGraphBuilder cgBuilder = new ZeroXCFABuilder(cha, analysisOptions, analysisCache, new ContextInsensitiveSelector(), new DefaultSSAInterpreter(analysisOptions, analysisCache), ZeroXInstanceKeys.NONE);
    CallGraph callGraph = cgBuilder.makeCallGraph(analysisOptions, null);
    // System.out.println("DONE: " + callGraph.getNumberOfNodes());
    // System.out.println("GGG: " +callGraph);

    boolean foundRemove = false;
    boolean foundAdd = false;
    for (CGNode curNode : callGraph) {
      if (curNode.getMethod().getName().toString().contains("remove")) {
        foundRemove = true;
        LocalPointerKey thethis = new LocalPointerKey(curNode, 1);
        HeapGraph heapGraph = cgBuilder.getPointerAnalysis().getHeapGraph();
        Iterator<Object> succNodes = heapGraph.getSuccNodes(thethis);
        while (succNodes.hasNext()) {
          Object object = (Object) succNodes.next();
          System.out.println(curNode + "  - 1 > " + object);
          Iterator<Object> succNodes2 = heapGraph.getSuccNodes(object);
          while (succNodes2.hasNext()) {
            Object object2 = (Object) succNodes2.next();
            System.out.println("                     > " + object2);
          }
        }
      }
      if (curNode.getMethod().getName().toString().contains("add")) {
        foundAdd = true;
      }
    }
    Assert.assertTrue("should see the call to add", foundAdd);
    Assert.assertTrue("should see the call to remove", foundRemove);
  }

  @Test
  public void testCircle03() throws ClassHierarchyException, IllegalArgumentException, CancelException, IOException {
    AnalysisScope scope = CallGraphTestUtil.makeJ2SEAnalysisScope("basic.txt", CallGraphTestUtil.REGRESSION_EXCLUSIONS);
    ClassHierarchy cha = ClassHierarchy.make(scope);
    Iterable<Entrypoint> entrypoints = makeEntryPoint(scope, cha, "Lparticle/Circle03", "analyzeMe(Lparticle/Point01;)V");

    AnalysisOptions analysisOptions = CallGraphTestUtil.makeAnalysisOptions(scope, entrypoints);
    AnalysisCache analysisCache = new AnalysisCache();

    Util.addDefaultSelectors(analysisOptions, cha);
    analysisOptions.setSelector(new ClassHierarchyMethodTargetSelector(cha) {
      @Override
      public IMethod getCalleeTarget(CGNode caller, CallSiteReference call, IClass receiver) {
        if(call.getDeclaredTarget().getSelector().equals(MethodReference.clinitSelector))
          return null;
        return super.getCalleeTarget(caller, call, receiver);
      }
    });
    analysisOptions.setSelector(new ClassHierarchyClassTargetSelector(cha));
    CallGraphBuilder cgBuilder = new ZeroXCFABuilder(cha, analysisOptions, analysisCache, new ContextInsensitiveSelector(), new DefaultSSAInterpreter(analysisOptions, analysisCache), ZeroXInstanceKeys.ALLOCATIONS);
    CallGraph callGraph = cgBuilder.makeCallGraph(analysisOptions, null);
    // System.out.println("DONE: " + callGraph.getNumberOfNodes());
    // System.out.println("GGG: " +callGraph);

    PointerAnalysis pointerAnalysis = cgBuilder.getPointerAnalysis();
    HeapGraph heapGraph = pointerAnalysis.getHeapGraph();
    HeapModel heapModel = heapGraph.getHeapModel();

//    System.out.println(GraphPrint.genericToString(callGraph));
//    System.out.println(GraphPrint.genericToString(heapGraph));

    IMethod analyzedMethod = TestUtils.getMethod_HelperHack(cha, "particle.Circle03", "analyzeMe", "Point01");
    CGNode curMethodCGNode = TestUtils.getCGNodeONE_HelperHack(analyzedMethod, callGraph);
    Object theThisPointerKey = heapModel.getPointerKeyForLocal(curMethodCGNode, 1);

    System.out.println("YY: " + theThisPointerKey);

    HashSet<PointerKey> reachablePointerKeys = new HashSet<PointerKey>();
    HashSet<InstanceKey> reachableInstanceKeys = new HashSet<InstanceKey>();

    Set<Object> reachableNodes = DFS.getReachableNodes(heapGraph, Collections.singleton(theThisPointerKey));

//    for (Object object : reachableNodes) {
//      System.out.println(object);
//    }

    TestUtils.collectReachable(theThisPointerKey, reachablePointerKeys, reachableInstanceKeys, heapGraph);

    boolean foundFiled = false;
    for (PointerKey pk : reachablePointerKeys) {
      System.out.println(pk);
      if (pk instanceof InstanceFieldKey) {
        InstanceFieldKey ifk = (InstanceFieldKey) pk;
        if (ifk.getField().getName().toString().contains("elementData")) {
          foundFiled = true;
        }
      }
    }
    Assert.assertTrue("field elementData in ArrayList should be reachable", foundFiled);
  }
  

  @Test
  public void testCircle04() throws ClassHierarchyException, IllegalArgumentException, CancelException, IOException {
    AnalysisScope scope = CallGraphTestUtil.makeJ2SEAnalysisScope("basic.txt", CallGraphTestUtil.REGRESSION_EXCLUSIONS);
    ClassHierarchy cha = ClassHierarchy.make(scope);
    Iterable<Entrypoint> entrypoints = makeEntryPoint(scope, cha, "Lparticle/Circle04", "analyzeMe()V");

    AnalysisOptions analysisOptions = CallGraphTestUtil.makeAnalysisOptions(scope, entrypoints);
    AnalysisCache analysisCache = new AnalysisCache();

    Util.addDefaultSelectors(analysisOptions, cha);
    analysisOptions.setSelector(new ClassHierarchyMethodTargetSelector(cha) {
      @Override
      public IMethod getCalleeTarget(CGNode caller, CallSiteReference call, IClass receiver) {
        if(call.getDeclaredTarget().getSelector().equals(MethodReference.clinitSelector))
          return null;
        return super.getCalleeTarget(caller, call, receiver);
      }
    });
    analysisOptions.setSelector(new ClassHierarchyClassTargetSelector(cha));
    CallGraphBuilder cgBuilder = new ZeroXCFABuilder(cha, analysisOptions, analysisCache, new FilterReceiversByMethodContextSelector(cha), new DefaultSSAInterpreter(analysisOptions, analysisCache), ZeroXInstanceKeys.ALLOCATIONS);
    CallGraph callGraph = cgBuilder.makeCallGraph(analysisOptions, null);
    // System.out.println("DONE: " + callGraph.getNumberOfNodes());
    // System.out.println("GGG: " +callGraph);

    PointerAnalysis pointerAnalysis = cgBuilder.getPointerAnalysis();
    HeapGraph heapGraph = pointerAnalysis.getHeapGraph();
    HeapModel heapModel = heapGraph.getHeapModel();

//    System.out.println(GraphPrint.genericToString(callGraph));
//    System.out.println(GraphPrint.genericToString(heapGraph));

    IMethod analyzedMethod = TestUtils.getMethod_HelperHack(cha, "particle.Circle04", "analyzeMe");
    CGNode curMethodCGNode = TestUtils.getCGNodeONE_HelperHack(analyzedMethod, callGraph);
    Object theThisPointerKey = heapModel.getPointerKeyForLocal(curMethodCGNode, 1);

    System.out.println("YY: " + theThisPointerKey);

    HashSet<PointerKey> reachablePointerKeys = new HashSet<PointerKey>();
    HashSet<InstanceKey> reachableInstanceKeys = new HashSet<InstanceKey>();

    Set<Object> reachableNodes = DFS.getReachableNodes(heapGraph, Collections.singleton(theThisPointerKey));

    for (Object object : reachableNodes) {
      System.out.println("REACHnode: "+object);
    }

    TestUtils.collectReachable(theThisPointerKey, reachablePointerKeys, reachableInstanceKeys, heapGraph);

    boolean foundFiled = false;
    for (PointerKey pk : reachablePointerKeys) {
      System.out.println("REACHpk: " + pk);
      if (pk instanceof InstanceFieldKey) {
        InstanceFieldKey ifk = (InstanceFieldKey) pk;
        if (ifk.getField().getName().toString().contains("pField")) {
          foundFiled = true;
        }
      }
    }
    Assert.assertTrue("when called from Circle04, I should NOT find any pField!!!", !foundFiled);
  }


}
