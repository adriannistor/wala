package particle;

import java.util.ArrayList;
import java.util.List;

public class Circle02 {

  private List<Point01> point01 = new ArrayList<Point01>();
  private ArrayList<Point01> point02 = new ArrayList<Point01>();

  public void analyzeMe(Point01 p) {
    point01.remove(p); // this will not be seen
    point02.add(p); // this will be seen
  }

}