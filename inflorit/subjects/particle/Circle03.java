package particle;

import java.util.ArrayList;
import java.util.List;

public class Circle03 {

  private List<Point01> point01 = new ArrayList<Point01>(); // this does not work
//  private ArrayList<Point01> point01 = new ArrayList<Point01>();// this works

  public void analyzeMe(Point01 p) {
    point01.remove(p);
  }

}