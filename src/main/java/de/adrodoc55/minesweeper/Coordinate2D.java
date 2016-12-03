package de.adrodoc55.minesweeper;

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
