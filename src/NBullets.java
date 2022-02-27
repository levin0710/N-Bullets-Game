import tester.*;
import javalib.worldimages.*;
import javalib.funworld.*;
import java.awt.Color;
import java.util.Random;

class MyGame extends World {
  int width;
  int height;
  int currentTick;
  int score;
  int bulletsLeft;
  ILoPieces shipslist;
  ILoPieces bulletslist;
  Random random;

  MyGame(int bulletsLeft) {
    this(500, 300, 1, 0, new MtLoPieces(), new MtLoPieces(), bulletsLeft, new Random());
  }

  MyGame(int bulletsLeft, Random random) {
    this(500, 300, 1, 0, new MtLoPieces(), new MtLoPieces(), bulletsLeft, random);
  }

  MyGame(int width, int height, ILoPieces shipslist, ILoPieces bulletslist) {
    this(width, height, 1, 0, shipslist, bulletslist, 10, new Random());
  }

  MyGame(int width, int height, int currentTick, int score, ILoPieces shipslist,
      ILoPieces bulletslist, int bulletsLeft, Random random) {

    if (width < 0 || height < 0) {
      throw new IllegalArgumentException("Invalid arguments passed to constructor.");
    }
    this.width = width;
    this.height = height;
    this.currentTick = currentTick;
    this.score = score;
    this.shipslist = shipslist;
    this.bulletslist = bulletslist;
    this.bulletsLeft = bulletsLeft;
    this.random = random;
  }

  // returns a scene
  @Override
  public WorldScene makeScene() {
    WorldScene scene = new WorldScene(this.width, this.height);
    scene = this.listofbulletsdraw(scene);
    scene = this.listofshipsdraw(scene);
    scene = scene.placeImageXY(
        (new TextImage("Bullets: " + Integer.toString(this.bulletsLeft), Color.BLACK)),
        this.width / 10, this.height / 8);

    scene = scene.placeImageXY(
        (new TextImage("Score: " + Integer.toString(this.score), Color.BLACK)), this.width - 40,
        this.height / 8);

    return scene;

  }

  // returns a list of ships on the given scene
  public WorldScene listofshipsdraw(WorldScene scene) {
    return this.shipslist.draw(scene);

  }

  // returns a list of bullets on the given scene
  public WorldScene listofbulletsdraw(WorldScene scene) {
    return this.bulletslist.draw(scene);

  }

  // returns a new game with every tick
  @Override
  public MyGame onTick() {
    if (this.currentTick % 28 == 0) {
      return new MyGame(this.width, this.height, this.currentTick + 1,
          this.score + (this.shipslist.count() - this.shipslist.collideShip(bulletslist).count()),
          this.shipslist.addRandomAmountOfPieces(this.random, this.width, this.height)
              .update(this.width, this.height),
          this.bulletslist.update(this.width, this.height), this.bulletsLeft, this.random);
    }
    else {
      return new MyGame(this.width, this.height, this.currentTick + 1,
          this.score + (this.shipslist.count() - this.shipslist.collideShip(bulletslist).count()),
          this.shipslist.collideShip(bulletslist).update(this.width, this.height),
          this.bulletslist.collideBullet(shipslist).update(this.width, this.height),
          this.bulletsLeft, this.random);
    }

  }

  // returns a new game after the key is pressed
  public MyGame onKeyEvent(String key) {
    if (key.equals(" ") && this.bulletsLeft > 0) {
      return new MyGame(this.width, this.height, this.currentTick, this.score, this.shipslist,
          this.bulletslist
              .add(new Bullets(new MyPosn(this.width / 2, this.height), new MyPosn(0, -8))),
          this.bulletsLeft - 1, this.random);

    }
    return this;
  }

  // returns a given scene with the amount of bullets left
  WorldScene bulletsLeftToShoot(WorldScene scene, int bullets) {
    return scene.placeImageXY((new TextImage("Bullets: " + Integer.toString(bullets), Color.BLACK)),
        this.width / 10, this.height / 8);
  }

  // returns the end scene
  WorldScene makeEndScene() {
    WorldScene endScene = new WorldScene(this.width, this.height);
    return endScene.placeImageXY((new TextImage("GameOver", Color.BLACK)), this.width / 2,
        this.height / 2);
  }

  // returns the end scene when the game is over
  public WorldEnd worldEnds() {
    boolean theGameIsOver = (this.bulletsLeft == 0 && this.bulletslist.count() == 0);
    if (theGameIsOver) {
      return new WorldEnd(true, this.makeEndScene());
    }
    else {
      return new WorldEnd(false, this.makeScene());
    }
  }
}

class ExamplesMyWorldProgram1 {
  MyGame world1 = new MyGame(0);
  MyGame world2 = new MyGame(500, 300, 2, 0, new MtLoPieces(), new MtLoPieces(), 0, new Random());
  WorldScene end = new WorldScene(500, 300);
  WorldScene end2 = end.placeImageXY((new TextImage("GameOver", Color.BLACK)), 250, 150);
  WorldEnd end3 = new WorldEnd(true, this.world1.makeEndScene());
  WorldScene scene1 = end.placeImageXY((new TextImage("Bullets: 0", Color.BLACK)), 500 / 10,
      300 / 8);

  WorldScene scene2 = scene1.placeImageXY((new TextImage("Score: 0", Color.BLACK)), 500 - 40,
      300 / 8);

  MyPosn posn1 = new MyPosn(0, 0);
  MyPosn posn2 = new MyPosn(1, 1);
  MyPosn posn3 = new MyPosn(100, 100);
  MyPosn offscreen = new MyPosn(1000, 1);

  WorldScene emptyscene = new WorldScene(100, 100);

  IPieces bullet1 = new Bullets(this.posn1, this.posn2);
  IPieces bullet2 = new Bullets(this.posn2, this.posn2);
  IPieces offbullet = new Bullets(this.offscreen, this.posn2);

  IPieces ship1 = new Ships(this.posn1, this.posn2, 10);
  IPieces ships2 = new Ships(this.posn3, this.posn2, 10);

  ILoPieces empty = new MtLoPieces();
  ILoPieces list1 = new ConsLoPieces(bullet1, empty);
  ILoPieces list2 = new ConsLoPieces(bullet2, list1);
  ILoPieces list3 = new ConsLoPieces(offbullet, list2);

  ILoPieces listof1ship = new ConsLoPieces(ships2, empty);
  ILoPieces listof2ships = new ConsLoPieces(ship1, listof1ship);

  // runs the game
  boolean testBigBang(Tester t) {
    MyGame world = new MyGame(10);
    return world.bigBang(world.width, world.height, 1.0 / 28.0);
  }

  // testing BulletsLeftToShoot method
  boolean testBulletsLeftToShoot(Tester t) {
    return t.checkExpect(this.world1.bulletsLeftToShoot(this.end, 0), this.scene1);
  }

  // testing makeScene method
  boolean testMakeScene(Tester t) {
    return t.checkExpect(this.world1.makeScene(), this.scene2);
  }

  // testing listofshipsdraw method
  boolean testListofshipsdraw(Tester t) {
    return t.checkExpect(this.world1.listofshipsdraw(this.end), this.empty.draw(this.end));
  }

  // testing listofbulletsdraw method
  boolean testListofbulletsdraw(Tester t) {
    return t.checkExpect(this.world1.listofbulletsdraw(this.end), this.empty.draw(this.end));
  }

  // testing makeEndScene method
  boolean testEndScene(Tester t) {
    return t.checkExpect(this.world1.makeEndScene(), this.end2);
  }

  // testing worldEnds method
  boolean testMakeEndScene(Tester t) {
    return t.checkExpect(this.world1.worldEnds(), this.end3);
  }

  // testing onTick method
  boolean testOnTick(Tester t) {
    return t.checkExpect(this.world1.onTick(), this.world2);
  }

  // testing onKeyEvent method
  boolean testOnKey(Tester t) {
    return t.checkExpect(this.world1.onKeyEvent("k"), this.world1)
        && t.checkExpect(this.world1.onKeyEvent(" "), this.world1);
  }

}

