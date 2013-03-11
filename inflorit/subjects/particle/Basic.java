package particle;

public class Basic implements SomeInterface {
  
  Basic x;
  
  SomeInterface y;
  
  private void test0() {
      x.foo();
  }
  
  private void test1() {
    y.foo();
  }

  public void foo() {
    x.foo();
  }
  
  public static void main(String[] args) {
    
  }
}
