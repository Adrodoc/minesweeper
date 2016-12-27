package de.adrodoc55.minesweeper;

import static com.google.common.base.Preconditions.checkNotNull;

public class GameWonException extends Exception {
  private static final long serialVersionUID = 1L;
  private final Coordinate2D coordinate;

  public GameWonException(Coordinate2D coordinate) {
    this.coordinate = checkNotNull(coordinate, "coordinate == null!");
  }

  public Coordinate2D getCoordinate() {
    return coordinate;
  }
}
