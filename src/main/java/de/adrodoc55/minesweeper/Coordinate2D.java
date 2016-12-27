package de.adrodoc55.minesweeper;

import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.concurrent.Immutable;

@Immutable
public class Coordinate2D {
  private final int x;
  private final int y;

  public Coordinate2D(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public boolean isPositive() {
    return x >= 0 && y >= 0;
  }

  public boolean isNegative() {
    return x < 0 && y < 0;
  }

  public Collection<Coordinate2D> getNeighbours() {
    Collection<Coordinate2D> result = new ArrayList<>(8);
    for (int y = this.y - 1; y <= this.y + 1; y++) {
      for (int x = this.x - 1; x <= this.x + 1; x++) {
        if (y != this.y || x != this.x) {
          result.add(new Coordinate2D(x, y));
        }
      }
    }
    return result;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + x;
    result = prime * result + y;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Coordinate2D other = (Coordinate2D) obj;
    if (x != other.x)
      return false;
    if (y != other.y)
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "Coordinate2D [x=" + x + ", y=" + y + "]";
  }
}
