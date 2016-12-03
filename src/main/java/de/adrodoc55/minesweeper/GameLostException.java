package de.adrodoc55.minesweeper;

import static com.google.common.base.Preconditions.checkNotNull;

public class GameLostException extends Exception {
  private static final long serialVersionUID = 1L;
  private final Coordinate2D mineCoordinate;

  public GameLostException(Coordinate2D mineCoordinate) {
    this.mineCoordinate = checkNotNull(mineCoordinate, "mineCoordinate == null!");
  }

  public Coordinate2D getMineCoordinate() {
    return mineCoordinate;
  }
}
