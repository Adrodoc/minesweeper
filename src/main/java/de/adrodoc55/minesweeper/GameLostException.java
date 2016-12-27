package de.adrodoc55.minesweeper;

import static com.google.common.base.Preconditions.checkNotNull;

public class GameLostException extends Exception {
  private static final long serialVersionUID = 1L;
  private final Coordinate2D coordinate;

  public GameLostException(Coordinate2D coordinate) {
    this.coordinate = checkNotNull(coordinate, "coordinate == null!");
  }

  public Coordinate2D getCoordinate() {
    return coordinate;
  }
}
