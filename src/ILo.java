import tester.*;
import java.util.Random;
import javalib.funworld.*;

// represents a list of pieces
interface ILoPieces {

  // counts the amount of pieces in a list
  int count();

  // adds the given piece to this list.
  ILoPieces add(IPieces piece);

  // draws every piece in the list onto the given scene.
  WorldScene draw(WorldScene scene);

  // updates the coordinates of each piece in the list
  ILoPieces update(int width, int height);

  // adds a random amount of pieces to the given list
  ILoPieces addRandomAmountOfPieces(Random random, int width, int height);

  // adds a random amount of pieces to the given list until the accumulator is 0
  ILoPieces addRandomAmountOfPiecesHelper(Random random, int width, int height, int acc);

  // removes every piece off the list that is off the screen
  ILoPieces removeIfOffScreen(int width, int height);

  // removes all the ships that have experienced a collision on this list
  ILoPieces collideShip(ILoPieces bullets);

  // removes all the ships that have experienced a collision on the given list
  ILoPieces collideShipshelper(ILoPieces ships);

  // removes all the ships that have experienced a collision with the given bullet
  // on the this list
  ILoPieces collideShipshelperhelper(IPieces bullet);

  // removes all the bullets that have experienced a collision on this list
  ILoPieces collideBullet(ILoPieces ships);

  // removes all the bullets that have experienced a collision on the given list
  ILoPieces collideBulletshelper(ILoPieces bullets);

  // removes all the bullets that have experienced a collision with the given ship
  // on the this list
  ILoPieces collideBulletshelperhelper(IPieces ship);

  // adds the same amount of bullets as the streak of the given bullet.
  ILoPieces addStreakAmountOfBullets(IPieces bullet);

  // adds the same amount of bullets as the streak of the given bullet until that
  // number is 0;
  ILoPieces addStreakAmountOfBulletsHelper(int acc, IPieces bullet);

}

/// represents an empty list of pieces
class MtLoPieces implements ILoPieces {
  MtLoPieces() {
  }

  // adds the given piece to this list.
  public ILoPieces add(IPieces piece) {
    return new ConsLoPieces(piece, new MtLoPieces());
  }

  // draws every piece in the list onto the given scene.
  public WorldScene draw(WorldScene scene) {
    return scene;
  }

  // updates the coordinates of each piece in the list
  public ILoPieces update(int width, int height) {
    return this;
  }

  // adds a random amount of pieces to the given list
  public ILoPieces addRandomAmountOfPieces(Random random, int width, int height) {
    int randomAmount = random.nextInt(3);

    return this.addRandomAmountOfPiecesHelper(random, width, height, randomAmount);
  }

  // adds a random amount of pieces to the given list until the accumulator is 0
  public ILoPieces addRandomAmountOfPiecesHelper(Random random, int width, int height, int acc) {
    if (acc <= 0) {
      return this;
    }
    else {
      MyPosn randomPosn = new MyPosn(width * random.nextInt(2),
          random.nextInt(height - 2 * (height / 7)) + height / 7);
      MyPosn randomVelocity;

      if (randomPosn.x == 500) {
        randomVelocity = new MyPosn(-4, 0);
      }
      else {
        randomVelocity = new MyPosn(4, 0);
      }

      return this.add(new Ships(randomPosn, randomVelocity, height / 30))
          .addRandomAmountOfPiecesHelper(random, width, height, acc - 1);
    }
  }

  // removes every piece off the list that is off the screen
  public ILoPieces removeIfOffScreen(int width, int height) {
    return this;
  }

  // counts the amount of pieces in a list
  public int count() {
    return 0;
  }

  // removes all the ships that have experienced a collision on this list
  public ILoPieces collideShip(ILoPieces bullets) {
    return this;
  }

  // removes all the ships that have experienced a collision on the given list
  public ILoPieces collideBulletshelper(ILoPieces bullets) {
    return bullets;
  }

  // removes all the ships that have experienced a collision with the given bullet
  // on the this list
  public ILoPieces collideShipshelperhelper(IPieces bullet) {
    return this;
  }

  // removes all the bullets that have experienced a collision on this list
  public ILoPieces collideBullet(ILoPieces ships) {
    return this;
  }

  // removes all the bullets that have experienced a collision on the given list
  public ILoPieces collideShipshelper(ILoPieces ships) {
    return ships;
  }

  // removes all the bullets that have experienced a collision with the given ship
  // on the this list
  public ILoPieces collideBulletshelperhelper(IPieces ship) {
    return this;
  }

  // adds the same amount of bullets as the streak of the given bullet.
  public ILoPieces addStreakAmountOfBullets(IPieces bullet) {
    return this.addStreakAmountOfBulletsHelper(bullet.getStreak(), bullet);
  }

  // adds the same amount of bullets as the streak of the given bullet until that
  // number is 0
  public ILoPieces addStreakAmountOfBulletsHelper(int acc, IPieces bullet) {
    if (acc < 0) {
      return this;
    }
    else {
      return this.add(bullet.afterCollision(acc)).addStreakAmountOfBulletsHelper(acc - 1, bullet);
    }
  }

}

// represents a non-empty list of pieces
class ConsLoPieces implements ILoPieces {
  IPieces first;
  ILoPieces rest;

  ConsLoPieces(IPieces first, ILoPieces rest) {
    this.first = first;
    this.rest = rest;
  }

  // adds the given piece to this list.
  public ILoPieces add(IPieces piece) {
    return new ConsLoPieces(piece, this);
  }

  // draws every piece in the list onto the given scene.
  public WorldScene draw(WorldScene scene) {
    return this.rest.draw(this.first.draw(scene));
  }

  // updates the coordinates of each piece in the list
  public ILoPieces update(int width, int height) {
    return new ConsLoPieces(this.first.update(), this.rest.update(width, height))
        .removeIfOffScreen(width, height);
  }

  // adds a random amount of pieces to the given list
  public ILoPieces addRandomAmountOfPieces(Random random, int width, int height) {
    int randomAmount = random.nextInt(3);

    return this.addRandomAmountOfPiecesHelper(random, width, height, randomAmount);
  }

  // adds a random amount of pieces to the given list until the accumulator is 0
  public ILoPieces addRandomAmountOfPiecesHelper(Random random, int width, int height, int acc) {
    if (acc <= 0) {
      return this;
    }
    else {
      MyPosn randomPosn = new MyPosn(width * random.nextInt(2),
          random.nextInt(height - 2 * (height / 7)) + height / 7);
      MyPosn randomVelocity;

      if (randomPosn.x == 500) {
        randomVelocity = new MyPosn(-4, 0);
      }
      else {
        randomVelocity = new MyPosn(4, 0);
      }

      return this.add(new Ships(randomPosn, randomVelocity, height / 30))
          .addRandomAmountOfPiecesHelper(random, width, height, acc - 1);
    }
  }

  // removes every piece off the list that is off the screen
  public ILoPieces removeIfOffScreen(int width, int height) {
    if (this.first.isOffScreen(width, height)) {
      return this.rest.removeIfOffScreen(width, height);
    }
    else {
      return new ConsLoPieces(this.first, this.rest.removeIfOffScreen(width, height));
    }
  }

  // counts the amount of pieces in a list
  public int count() {
    return 1 + this.rest.count();
  }

  // removes all the ships that have experienced a collision on this list
  public ILoPieces collideShip(ILoPieces bullets) {
    return bullets.collideShipshelper(this);
  }

  // removes all the ships that have experienced a collision on the given list
  public ILoPieces collideShipshelper(ILoPieces ships) {
    return this.rest.collideShipshelper(ships.collideShipshelperhelper(this.first));
  }

  // removes all the ships that have experienced a collision with the given bullet
  // on the this list
  public ILoPieces collideShipshelperhelper(IPieces bullet) {
    if (this.first.collides(bullet)) {
      return this.rest.collideShipshelperhelper(bullet);
    }
    else {
      return new ConsLoPieces(this.first, this.rest.collideShipshelperhelper(bullet));
    }
  }

  // removes all the bullets that have experienced a collision on this list
  public ILoPieces collideBullet(ILoPieces ships) {
    return ships.collideBulletshelper(this);
  }

  // removes all the bullets that have experienced a collision on the given list
  public ILoPieces collideBulletshelper(ILoPieces bullets) {
    return this.rest.collideBulletshelper(bullets.collideBulletshelperhelper(this.first));
  }

  // removes all the bullets that have experienced a collision with the given ship
  // on the this list
  public ILoPieces collideBulletshelperhelper(IPieces ship) {
    if (this.first.collides(ship)) {
      return this.rest.collideBulletshelperhelper(ship).addStreakAmountOfBullets(this.first);
    }
    else {
      return new ConsLoPieces(this.first, this.rest.collideBulletshelperhelper(ship));
    }
  }

  // adds the same amount of bullets as the streak of the given bullet.
  public ILoPieces addStreakAmountOfBullets(IPieces bullet) {
    return this.addStreakAmountOfBulletsHelper(bullet.getStreak(), bullet);
  }

  // adds the same amount of bullets as the streak of the given bullet until that
  // number is 0
  public ILoPieces addStreakAmountOfBulletsHelper(int acc, IPieces bullet) {
    if (acc < 0) {
      return this;
    }
    else {
      return this.add(bullet.afterCollision(acc)).addStreakAmountOfBulletsHelper(acc - 1, bullet);
    }
  }

}

class ExamplesILoPices {

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

  // tests the count method
  boolean testCount(Tester t) {
    return t.checkExpect(this.empty.count(), 0) && t.checkExpect(this.list1.count(), 1)
        && t.checkExpect(this.list2.count(), 2);
  }

  // tests the Add method
  boolean testAdd(Tester t) {
    return t.checkExpect(this.empty.add(this.bullet1), this.list1)
        && t.checkExpect(this.list1.add(this.bullet2), this.list2)
        && t.checkExpect(this.list2.add(this.ship1), new ConsLoPieces(this.ship1,
            new ConsLoPieces(this.bullet2, new ConsLoPieces(this.bullet1, this.empty))));
  }

  // tests the update method
  boolean testUpdate(Tester t) {
    return t.checkExpect(this.empty.update(500, 300), this.empty)
        && t.checkExpect(this.list1.update(500, 300), new ConsLoPieces(this.bullet2, this.empty))
        && t.checkExpect(this.list2.update(500, 300), new ConsLoPieces(
            new Bullets(new MyPosn(2, 2), this.posn2), new ConsLoPieces(this.bullet2, this.empty)));
  }

  // tests the removeIfOffScreen method
  boolean testRemoveIfOfScreen(Tester t) {
    return t.checkExpect(this.empty.removeIfOffScreen(500, 300), this.empty)
        && t.checkExpect(this.list1.removeIfOffScreen(500, 300), this.list1)
        && t.checkExpect(this.list3.update(500, 300), this.list2);
  }

  // tests the draw method
  boolean testDraw(Tester t) {
    return t.checkExpect(this.empty.draw(this.emptyscene), this.emptyscene)
        && t.checkExpect(this.list1.draw(this.emptyscene), this.bullet1.draw(this.emptyscene))
        && t.checkExpect(this.list2.draw(this.emptyscene),
            this.bullet2.draw(this.bullet1.draw(this.emptyscene)));

  }

  // tests the addStreakAmountOfBullets method
  boolean testAddStreakAmountOfBullets(Tester t) {
    return t.checkExpect(this.empty.addStreakAmountOfBullets(this.bullet1),
        new ConsLoPieces(this.bullet1.afterCollision(0),
            new ConsLoPieces(this.bullet1.afterCollision(1), this.empty)))
        && t.checkExpect(this.list1.addStreakAmountOfBullets(this.bullet1),
            new ConsLoPieces(this.bullet1.afterCollision(0),
                new ConsLoPieces(this.bullet1.afterCollision(1), this.list1)))
        && t.checkExpect(this.list2.addStreakAmountOfBullets(this.bullet1),
            new ConsLoPieces(this.bullet1.afterCollision(0),
                new ConsLoPieces(this.bullet1.afterCollision(1), this.list2)));

  }

  // tests the addStreakAmountOfBulletsHelper method
  boolean testAddStreakAmountOfBulletsHelper(Tester t) {
    return t.checkExpect(this.empty.addStreakAmountOfBulletsHelper(-1, this.bullet1), this.empty)
        && t.checkExpect(this.list1.addStreakAmountOfBulletsHelper(0, this.bullet1),
            new ConsLoPieces(this.bullet1.afterCollision(0), this.list1))
        && t.checkExpect(this.list2.addStreakAmountOfBulletsHelper(1, this.bullet1),
            new ConsLoPieces(this.bullet1.afterCollision(0),
                new ConsLoPieces(this.bullet1.afterCollision(1), this.list2)));

  }

  // tests the collideBullet method
  boolean testCollideBullet(Tester t) {
    return t.checkExpect(this.list1.collideBullet(this.listof1ship), this.list1)
        && t.checkExpect(this.list1.collideBullet(this.listof2ships),
            new ConsLoPieces(this.bullet1.afterCollision(0),
                new ConsLoPieces(this.bullet1.afterCollision(1), this.empty)))
        && t.checkExpect(this.empty.collideBullet(this.listof1ship), this.empty);
  }

  // tests the collideBulletshelper method
  boolean testCollideBulletHelper(Tester t) {
    return t.checkExpect(this.listof1ship.collideBulletshelper(this.list1), this.list1)
        && t.checkExpect(this.listof2ships.collideBulletshelper(this.list1),
            new ConsLoPieces(this.bullet1.afterCollision(0),
                new ConsLoPieces(this.bullet1.afterCollision(1), this.empty)))
        && t.checkExpect(this.listof1ship.collideBulletshelper(this.empty), this.empty);
  }

  // tests the collideBulletshelperhelper method
  boolean testCollideBulletHelperHelper(Tester t) {
    return t.checkExpect(this.list1.collideBulletshelperhelper(this.ships2), this.list1)
        && t.checkExpect(this.list1.collideBulletshelperhelper(this.ship1),
            new ConsLoPieces(this.bullet1.afterCollision(0),
                new ConsLoPieces(this.bullet1.afterCollision(1), this.empty)))
        && t.checkExpect(this.empty.collideBulletshelperhelper(this.ship1), this.empty);
  }

  // tests the collideShip method
  boolean testCollideShips(Tester t) {
    return t.checkExpect(this.listof1ship.collideShip(this.list1), this.listof1ship)
        && t.checkExpect(this.listof2ships.collideShip(this.list1), this.listof1ship)
        && t.checkExpect(this.empty.collideShip(this.list1), this.empty);
  }

  // tests the collideShipshelper method
  boolean testCollideShipsHelper(Tester t) {
    return t.checkExpect(this.list1.collideShipshelper(this.listof1ship), this.listof1ship)
        && t.checkExpect(this.list1.collideShipshelper(this.listof2ships), this.listof1ship)
        && t.checkExpect(this.list1.collideShipshelper(this.empty), this.empty);
  }

  // tests the collideShipshelperhelper method
  boolean testCollideShipsHelperHelper(Tester t) {
    return t.checkExpect(this.listof1ship.collideShipshelperhelper(this.bullet1), this.listof1ship)
        && t.checkExpect(this.listof2ships.collideShipshelperhelper(this.bullet1), this.listof1ship)
        && t.checkExpect(this.empty.collideShipshelperhelper(this.bullet2), this.empty);
  }

}
