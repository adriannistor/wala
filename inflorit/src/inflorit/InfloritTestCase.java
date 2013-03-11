package inflorit;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import com.ibm.wala.analysis.pointers.HeapGraph;
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
import com.ibm.wala.ipa.callgraph.Entrypoint;
import com.ibm.wala.ipa.callgraph.impl.DefaultContextSelector;
import com.ibm.wala.ipa.callgraph.impl.DefaultEntrypoint;
import com.ibm.wala.ipa.callgraph.impl.Util;
import com.ibm.wala.ipa.callgraph.propagation.HeapModel;
import com.ibm.wala.ipa.callgraph.propagation.InstanceFieldKey;
import com.ibm.wala.ipa.callgraph.propagation.InstanceKey;
import com.ibm.wala.ipa.callgraph.propagation.PointerAnalysis;
import com.ibm.wala.ipa.callgraph.propagation.PointerKey;
import com.ibm.wala.ipa.cha.ClassHierarchy;
import com.ibm.wala.ipa.cha.ClassHierarchyException;
import com.ibm.wala.ipa.cha.IClassHierarchy;
import com.ibm.wala.types.MethodReference;
import com.ibm.wala.types.Selector;
import com.ibm.wala.types.TypeName;
import com.ibm.wala.types.TypeReference;
import com.ibm.wala.util.CancelException;

public class InfloritTestCase extends WalaTestCase {

  public static Set<Entrypoint> makeEntryPoint(AnalysisScope scope, IClassHierarchy cha, String entryClass, String entryMethod) {
//    val typeReference: TypeReference = TypeReference.findOrCreate(scope.getLoader(AnalysisScope.Application),
//        TypeName.string2TypeName(entryClass))
//      val methodReference: MethodReference = MethodReference.findOrCreate(typeReference,
//        entryMethod.substring(0, entryMethod.indexOf('(')), entryMethod.substring(entryMethod.indexOf('(')))
//      new DefaultEntrypoint(methodReference, cha)
    
    TypeReference typeReference = TypeReference.findOrCreate(scope.getLoader(AnalysisScope.APPLICATION), TypeName.string2TypeName(entryClass));
    MethodReference methodReference = MethodReference.findOrCreate(typeReference, Selector.make(entryMethod));
    DefaultEntrypoint defaultEntrypoint = new DefaultEntrypoint(methodReference, cha);
    defaultEntrypoint.makeSymbolicParameter(0);
    return Collections.singleton((Entrypoint)defaultEntrypoint);
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
    ContextSelector contextSelector = new DefaultContextSelector(analysisOptions, cha);
    
    CallGraphBuilder cgBuilder = Util.makeVanillaZeroOneCFABuilder(
                                    analysisOptions, analysisCache, cha,scope,
                                    contextSelector,
                                    null);
    CallGraph callGraph = cgBuilder.makeCallGraph(analysisOptions, null);
//    System.out.println("DONE: " + callGraph.getNumberOfNodes());
//    System.out.println("GGG: " +callGraph);

    boolean foundRemove = false;
    boolean foundAdd = false;
    for (CGNode curNode : callGraph) {
      if (curNode.getMethod().getName().toString().contains("remove")) {
        foundRemove = true;
      }
      if (curNode.getMethod().getName().toString().contains("add")) {
        foundAdd = true;
      }
    }
    Assert.assertTrue("should see the call to add",foundAdd);
    Assert.assertTrue("should see the call to remove",foundRemove);
  }
  
  @Test
  public void testCircle03() throws ClassHierarchyException, IllegalArgumentException, CancelException, IOException {
    AnalysisScope scope = CallGraphTestUtil.makeJ2SEAnalysisScope("basic.txt", CallGraphTestUtil.REGRESSION_EXCLUSIONS);
    ClassHierarchy cha = ClassHierarchy.make(scope);
    Iterable<Entrypoint> entrypoints = makeEntryPoint(scope, cha, "Lparticle/Circle03", "analyzeMe(Lparticle/Point01;)V");    
    
    AnalysisOptions analysisOptions = CallGraphTestUtil.makeAnalysisOptions(scope, entrypoints);
    AnalysisCache analysisCache = new AnalysisCache();
    ContextSelector contextSelector = new DefaultContextSelector(analysisOptions, cha);
    
    CallGraphBuilder cgBuilder = Util.makeVanillaZeroOneCFABuilder(
                                    analysisOptions, analysisCache, cha,scope,
                                    contextSelector,
                                    null);
    CallGraph callGraph = cgBuilder.makeCallGraph(analysisOptions, null);
//    System.out.println("DONE: " + callGraph.getNumberOfNodes());
//    System.out.println("GGG: " +callGraph);

    
    PointerAnalysis pointerAnalysis = cgBuilder.getPointerAnalysis();
    HeapGraph heapGraph = pointerAnalysis.getHeapGraph();
    HeapModel heapModel = heapGraph.getHeapModel();
    
    IMethod analyzedMethod = TestUtils.getMethod_HelperHack(cha, "particle.Circle03", "analyzeMe", "Point01");
    CGNode curMethodCGNode = TestUtils.getCGNodeONE_HelperHack(analyzedMethod,callGraph);
    Object theThisPointerKey = heapModel.getPointerKeyForLocal(curMethodCGNode, 1);
    
    System.out.println("YY: " +theThisPointerKey);
    
    HashSet<PointerKey> reachablePointerKeys = new HashSet<PointerKey>();
    HashSet<InstanceKey> reachableInstanceKeys = new HashSet<InstanceKey>();
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
    Assert.assertTrue("field elementData in ArrayList should be reachable",foundFiled);
  }
  
  
  
  






























}