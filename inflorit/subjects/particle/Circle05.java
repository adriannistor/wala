package particle;

public class Circle05 extends Circle04 {

  Point02 p = new Point02(8);
  @Override public void analyzeMe() {
    mutate();
  }
  
  @Override public void mutate() {
    p.setField(8);
  }

}