package de.adrodoc55.minesweeper;

import static com.google.common.base.Preconditions.checkNotNull;

public class MinesweeperField {
  private final Coordinate2D coordinate;
  private boolean mine;

  public MinesweeperField(Coordinate2D coordinate) {
    this.coordinate = checkNotNull(coordinate, "coordinate == null!");
  }

  public Coordinate2D getCoordinate() {
    return coordinate;
  }

  public boolean isMine() {
    return mine;
  }

  public void setMine(boolean mine) {
    this.mine = mine;
  }
}
