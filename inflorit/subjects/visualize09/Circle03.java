package visualize09;

import java.util.ArrayList;

public class Circle03 {
Point01 f = null;


public void analyzeMe1() {
    f = new Point01();
    ArrayList<Point01> list = new ArrayList<Point01>();
    for (Point01 p : list) {
        if (p.equals(f)) {
            f = p;
        }
    }
}

}
