package de.adrodoc55.minesweeper;

import static com.google.common.base.Preconditions.checkNotNull;

public class MinesweeperGrid extends Grid<MinesweeperField> {
  /**
   * Erzeugt ein neues {@link MinesweeperGrid} ohne Mine an der angegebenen {@link Coordinate2D}.
   *
   * @param height
   * @param width
   * @param pressedByUser die {@link Coordinate2D} an der keine Mine sein darf
   * @param strategy the {@link MinePlacementStrategy}
   */
  public MinesweeperGrid(int height, int width, Coordinate2D pressedByUser,
      MinePlacementStrategy strategy) {
    super(height, width, MinesweeperField::new);
    strategy.setupMines(this, pressedByUser);
  }

  public int check(Coordinate2D coordinate) {
    MinesweeperField field = getElement(coordinate);
    checkNotNull(field, "field == null!");
    if (field.isMine()) {
      return -1;
    }
    long mineCount = getNeighbours(coordinate).stream().filter(MinesweeperField::isMine).count();
    return (int) mineCount;
  }
}
