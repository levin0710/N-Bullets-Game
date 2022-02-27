import tester.*;
import javalib.worldimages.Posn;

// represents a position with x and y coordinates
class MyPosn extends Posn {

  MyPosn(int x, int y) {
    super(x, y);
  }

  MyPosn(Posn p) {
    this(p.x, p.y);
  }

  // adds the coordinates of a given position to this position.
  MyPosn add(MyPosn p) {
    return new MyPosn(this.x + p.x, this.y + p.y);
  }
}

class ExamplesMyPosn {
  MyPosn posn1 = new MyPosn(0, 0);
  MyPosn posn2 = new MyPosn(1, 1);

  // tests the Add method
  boolean testAdd(Tester t) {
    return t.checkExpect(this.posn1.add(posn2), this.posn2)
        && t.checkExpect(this.posn2.add(posn1), this.posn2)
        && t.checkExpect(this.posn2.add(posn2), new MyPosn(2, 2));
  }

}