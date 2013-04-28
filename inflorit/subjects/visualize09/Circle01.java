package visualize09;


public class Circle01 {
private Point01 pc01;
    
public void analyzeMe1() {
    for (int i = 0; i < 3; i ++) {
        Point03 p = new Point03();
        p.setField1(88);
    }
}

// Main01-> wrongly reports the loop as mutating
public void analyzeMe2(Point01 b) {
    Point01 local1 = new Point01();
    for (int i = 0; i < 3; i ++) {
        Point01 p = new Point01();
        p.setP02(77);
    }
    local1.setP02(99);

}

//Main01-> correctly reports the loop as NON-mutating
// same as analyzeMe2, but the exterior are from Symbolic, so they are already differentiated
public void analyzeMe3(Point01 b) {
    for (int i = 0; i < 3; i ++) {
        Point01 p = new Point01();
        p.setP02(77);
    }
}

public void analyzeMe4(Point01 b) {
    boolean found = falseBool();
    for (int i = 0; i < 3; i++) {
        if(pure1() && !found) {
            found = true;
            fox();
        }
    }
}

public void analyzeMe5(Point01 b) {
    boolean found = falseBool();
    for (int i = 0; i < 3; i++) {
        found &= pure1();
    }
    useBool(found);
    useBool(found);
}

public void analyzeMe6(Point01 b) {
    boolean found = falseBool();
    for (int i = 0; i < 3; i++) {
        if(pure1() && found) {
            found = false;
            fox();
        }
    }
}

public void analyzeMe7(Point01 b) {
    boolean found = falseBool();
    for (int i = 0; i < 3; i++) {
        if(pure1() && !found) {
            found = true;
        }
    }
    useBool(found);
    useBool(found);
}

public void analyzeMe8(Point01 b) {
    int i = 0;
//    for (int i = 0; i < 55; i+=3) {
//        fox();
//    }
    do {
        fox();
    } while (i < 6);

}

public void analyzeMe9(Point01 b) {
    boolean found = falseBool();
    for (int i = 0; i < 3; i++) {
        if(this.pc01 != null) {
            this.pc01.setP02(99);
            pure1();
        }
    }
}

private boolean trueBool() {
    return true;
}

private boolean falseBool() {
    return false;
}

private boolean pure1() {
    return true;
}


private void fox() {
    this.pc01 = null;
}


private void useBool(boolean b) {
    ;
}






























} // class Circle01
