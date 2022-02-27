import tester.*;
import java.awt.Color;
import javalib.worldimages.*;
import javalib.funworld.*;

//represents IPieces that can be ships and bullets
interface IPieces {

  // returns a drawing of a Piece on the given scene
  WorldScene draw(WorldScene scene);

  // updates the IPieces position
  IPieces update();

  // checks if a piece is off the screen
  boolean isOffScreen(int width, int height);

  // checks if two pieces collide
  boolean collides(IPieces that);

  // checks if a piece's position collides with the given
  boolean collidesHelper(int x, int y, int radius);

  // gets the streak of a piece
  int getStreak();

  // returns a piece after collision
  IPieces afterCollision(int acc);

}

//represents an abstract class for a Piece
abstract class APieces implements IPieces {
  MyPosn position;
  MyPosn velocity;
  Color color;
  int radius;

  APieces(MyPosn position, MyPosn velocity, int radius, Color color) {
    this.position = position;
    this.velocity = velocity;
    this.color = color;
    this.radius = radius;
  }

  // returns a drawing of a Piece on the given scene
  public WorldScene draw(WorldScene scene) {
    return scene.placeImageXY(new CircleImage(this.radius, OutlineMode.SOLID, this.color),
        this.position.x, this.position.y);

  }

  // checks if a piece is off the screen
  public boolean isOffScreen(int width, int height) {
    return this.position.x < -this.radius || this.position.x > width + this.radius
        || this.position.y < -this.radius || this.position.y > height + this.radius;
  }

  // checks if two pieces collide
  public boolean collides(IPieces that) {
    return that.collidesHelper(this.position.x, this.position.y, this.radius);
  }

  // checks if a piece's position collides with the given
  public boolean collidesHelper(int x, int y, int radius) {
    return Math.abs(this.position.x - x) <= this.radius + radius
        && Math.abs(this.position.y - y) <= this.radius + radius;
  }

  // returns a piece after collision
  public IPieces afterCollision(int acc) {
    return this;
  }

}

//represents a bullet
class Bullets extends APieces {
  int streak;

  Bullets(MyPosn position, MyPosn velocity, int radius, int streak) {
    super(position, velocity, radius, Color.pink);
    this.streak = streak;
  }

  Bullets(MyPosn position, MyPosn velocity) {
    this(position, velocity, 2, 1);
  }

  // updates the IPieces position
  public IPieces update() {
    return new Bullets(this.position.add(velocity), this.velocity, this.radius, this.streak);
  }

  // gets the streak of a piece
  public int getStreak() {
    return this.streak;
  }

  // checks if a piece's position collides with the given
  @Override
  public IPieces afterCollision(int acc) {
    MyPosn newVelocity = new MyPosn(
        (int) (8 * Math.cos(Math.toRadians(acc * (360 / (this.streak + 1))))),
        (int) (8 * Math.sin(Math.toRadians(acc * (360 / (this.streak + 1))))));

    if (this.radius < 10) {
      return new Bullets(this.position, newVelocity, this.radius + 2, this.streak + 1);
    }
    else {
      return new Bullets(this.position, newVelocity, this.radius, this.streak + 1);
    }

  }

}

//represents a ship
class Ships extends APieces {

  Ships(MyPosn position, MyPosn velocity, int radius) {
    super(position, velocity, radius, Color.cyan);
  }

  // updates the IPieces position
  public IPieces update() {
    return new Ships(this.position.add(velocity), this.velocity, this.radius);

  }

  // gets the streak of a piece
  public int getStreak() {
    return -1;
  }
}

class ExamplesPieces {

  MyPosn posn1 = new MyPosn(0, 0);
  MyPosn posn2 = new MyPosn(1, 1);
  MyPosn posn3 = new MyPosn(202, 230);
  MyPosn posn4 = new MyPosn((int) (8 * Math.cos(Math.toRadians(2 * (360 / (2))))),
      (int) (8 * Math.sin(Math.toRadians(2 * (360 / (2))))));

  WorldScene empty = new WorldScene(100, 100);
  WorldScene shipOnScene = new WorldScene(100, 100);

  IPieces ships1 = new Ships(this.posn1, this.posn2, 2);
  IPieces ships2 = new Ships(this.posn3, this.posn2, 2);
  IPieces bullets1 = new Bullets(this.posn1, this.posn2);
  APieces bullets2 = new Bullets(this.posn1, this.posn2, 2, 45);

  // testing the draw method
  boolean testDraw(Tester t) {
    return t.checkExpect(this.ships1.draw(this.empty),
        this.shipOnScene.placeImageXY(new CircleImage(2, OutlineMode.SOLID, Color.cyan), 0, 0));
  }

  // testing update method on a bullet
  boolean testUpdate(Tester t) {
    return t.checkExpect(this.bullets1.update(), new Bullets(this.posn2, this.posn2));

  }

  // testing update method on a ship
  boolean testUpdate2(Tester t) {
    return t.checkExpect(this.ships1.update(), new Ships(this.posn2, this.posn2, 2));

  }

  // testing getStreak method
  boolean testGetStreak(Tester t) {
    return t.checkExpect(this.bullets1.getStreak(), 1)
        && t.checkExpect(this.bullets2.getStreak(), 45)
        && t.checkExpect(this.ships1.getStreak(), -1);

  }

  // testing isOffScreen method
  boolean testIsOffScreen(Tester t) {
    return t.checkExpect(this.bullets1.isOffScreen(200, 200), false)
        && t.checkExpect(this.ships2.isOffScreen(200, 200), true);
  }

  // testing collide method
  boolean testCollide(Tester t) {
    return t.checkExpect(this.ships1.collides(this.bullets1), true)
        && t.checkExpect(this.bullets1.collides(this.ships1), true)
        && t.checkExpect(this.ships2.collides(this.bullets1), false);
  }

  // testing collideHelper method
  boolean testCollideHelper(Tester t) {
    return t.checkExpect(this.ships1.collidesHelper(2, 3, 2), true)
        && t.checkExpect(this.ships1.collidesHelper(200, 3000, 2), false)
        && t.checkExpect(this.bullets1.collidesHelper(2, 3, 2), true)
        && t.checkExpect(this.bullets1.collidesHelper(50, 13, 22), false);
  }

  // testing afterCollision method
  boolean testAfterCollision(Tester t) {
    return t.checkExpect(this.bullets1.afterCollision(2), new Bullets(this.posn1, this.posn4, 4, 2))
        && t.checkExpect(this.ships1.afterCollision(2), this.ships1);
  }

}
