package particle;

public class Basic implements SomeInterface {

  Basic x;

  SomeInterface y;

  int[] blabla;

  private void test1() {
    y.foo();
  }

  public void foo() {
    Basic x2 = this.x;
    this.x.foo();
  }

  private void test0() {
    x.foo();
  }

  public static void main(String[] args) {

  }

  boolean bar() {
    if (Math.random() > 10)
      return true;
    else
      return false;
  }
  
  void tar(boolean z) {
    
  }

  void testPrintConditional() {
    boolean x = bar();
    boolean y = bar();
    boolean z = x && y;
    tar(z);
  }
}
