package inflorit;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import org.junit.Assert;

import com.ibm.wala.analysis.pointers.HeapGraph;
import com.ibm.wala.classLoader.IClass;
import com.ibm.wala.classLoader.IMethod;
import com.ibm.wala.ipa.callgraph.CGNode;
import com.ibm.wala.ipa.callgraph.CallGraph;
import com.ibm.wala.ipa.callgraph.propagation.InstanceKey;
import com.ibm.wala.ipa.callgraph.propagation.PointerKey;
import com.ibm.wala.ipa.cha.ClassHierarchy;

public class TestUtils {
  public static IMethod getMethod_HelperHack(ClassHierarchy classHierarchy, String qualifiedClassName, String methodName,
      String... argsClassName) {
    IMethod res = null;
    for (IClass curClass : classHierarchy) {
      if (!curClass.toString().contains(qualifiedClassName)) {
        continue;
      }
      for (IMethod curMethod : curClass.getDeclaredMethods()) {
        if (!curMethod.toString().contains(methodName)) {
          continue;
        }

        if (argsClassName.length != numberOfProperArgs(curMethod)) {
          continue;
        }
        assert argsClassName.length == numberOfProperArgs(curMethod) : "FALL";
        boolean allParamsAreFine = true;
        for (int i = 0; i < argsClassName.length; i++) {
          int delta = curMethod.isStatic() ? 0 : 1;
          // System.out.println("X: " +
          // curMethod.getParameterType(i+delta).getName().getClassName().toString());
          if (!argsClassName[i].equals(curMethod.getParameterType(i + delta).getName().getClassName().toString())) {
            allParamsAreFine = false;
            break;
          }
        }
        if (!allParamsAreFine) {
          continue;
        }
        if (!curMethod.getDeclaringClass().isPublic()) {
          continue;
        }

        System.out.println(curMethod);
        assert res == null : "ERR: I don't handle method signatures right now";
        res = curMethod;
      }
    }

    assert res != null : "ERR: did not find: " + qualifiedClassName + "." + methodName;
    return res;
  }

  public static String getQualifiedName(IClass iClass) {
    try {
      String packageName = iClass.getName().getPackage().toString().replace("/", ".");
      if (!packageName.equals("")) {
        packageName += ".";
      }
      return packageName + iClass.getName().getClassName().toString();
    } catch (Exception e) {
      return "";
    }
  }

  public static int numberOfProperArgs(IMethod method) {
    if (method.isStatic()) {
      return method.getNumberOfParameters();
    }
    assert method.getNumberOfParameters() >= 1 : "ERR: proper methods should have at least THIS as param";
    return method.getNumberOfParameters() - 1;
  }

  public static void collectReachable(Object start, HashSet<PointerKey> reachablePointerKeys,
      HashSet<InstanceKey> reachableInstanceKeys, HeapGraph heapGraph) {

    for (Iterator<Object> it = heapGraph.getSuccNodes(start); it.hasNext();) {
      Object curSucc = it.next();
      if (reachablePointerKeys.contains(curSucc) || reachableInstanceKeys.contains(curSucc)) {
        continue; // don't want circles
      }
      Assert.assertTrue("FALL", !reachablePointerKeys.contains(curSucc) && !reachableInstanceKeys.contains(curSucc));

      if (curSucc instanceof PointerKey) {
        reachablePointerKeys.add((PointerKey) curSucc);
      } else if (curSucc instanceof InstanceKey) {
        reachableInstanceKeys.add((InstanceKey) curSucc);
      } else {
        Assert.assertTrue("ERR: should be one of the two", false);
      }
      collectReachable(curSucc, reachablePointerKeys, reachableInstanceKeys, heapGraph);
    }
  }

  /**
   * @short gets ONE node that has thins method (in whatever context) checks
   *        there is one andd only one just for my hello play
   * */
  public static CGNode getCGNodeONE_HelperHack(IMethod method, CallGraph cg) {
    ArrayList<CGNode> list = getCGNodes(method, cg);
    assert list.size() == 1 : "ERR: should be one and only one: " + list.size();
    return list.get(0);
  }

  /**
   * @short gets the nodes that have this mehtod (and whatever context)
   * */
  public static ArrayList<CGNode> getCGNodes(IMethod method, CallGraph cg) {
    ArrayList<CGNode> res = new ArrayList<CGNode>();
    for (CGNode curNode : cg) {
      if (equalIMethod(curNode.getMethod(), method)) {
        res.add(curNode);
      }
    }
    return res;
  }

  /**
   * @short says if two IMethod-s are equal
   * */
  public static boolean equalIMethod(IMethod one, IMethod two) {
    return one.getSignature().equals(two.getSignature());
  }

} // class TestUtils
