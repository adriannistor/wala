package visualize09;

public class Circle02 {

private byte[] f1;
private Point01[] f2;
private Point01 f3;

// does NOT work --- does NOT see side effect
public void analyzeMe1() {
    for (int i = 0; i < 3; i ++) {
        f1[i] = 0;
    }
}

//does NOT work --- does NOT see side effect
public void analyzeMe2() {
    for (int i = 0; i < 3; i ++) {
        f2[i].setP02(88);
    }
}

//WORKS-sees side effect
public void analyzeMe3() {
    for (int i = 0; i < 3; i ++) {
        f2[i] = new Point01();
    }
}

//WORKS-sees side effect
public void analyzeMe4() {
    for (int i = 0; i < 3; i ++) {
        f2[i] = null;
    }
}

//WORKS-sees side effect
public void analyzeMe5() {
    for (int i = 0; i < 3; i ++) {
        f3.setP02(99);
    }
}


















































} // class Circle02
