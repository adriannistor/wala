package inflorit;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

import org.junit.Test;

import com.ibm.wala.core.tests.callGraph.CallGraphTest;
import com.ibm.wala.core.tests.callGraph.CallGraphTestUtil;
import com.ibm.wala.core.tests.util.TestConstants;
import com.ibm.wala.core.tests.util.WalaTestCase;
import com.ibm.wala.ipa.callgraph.AnalysisCache;
import com.ibm.wala.ipa.callgraph.AnalysisOptions;
import com.ibm.wala.ipa.callgraph.AnalysisScope;
import com.ibm.wala.ipa.callgraph.Entrypoint;
import com.ibm.wala.ipa.callgraph.impl.DefaultEntrypoint;
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
}