package de.adrodoc55.minesweeper;

import static com.google.common.collect.Collections2.filter;

import java.util.Collection;
import java.util.Random;

import com.google.common.collect.Iterables;

public interface MinePlacementStrategy {
  void setupMines(Grid<MinesweeperField> grid, Coordinate2D pressedByUser);

  public default Collection<MinesweeperField> getNonCursorFields(Grid<MinesweeperField> grid,
      Coordinate2D pressedByUser) {
    Collection<Coordinate2D> neighbours = pressedByUser.getNeighbours();
    Collection<MinesweeperField> fields =
        filter(grid, f -> !pressedByUser.equals(f.getCoordinate()));
    fields = filter(fields, f -> !neighbours.contains(f.getCoordinate()));
    return fields;
  }

  public static class MineProbability implements MinePlacementStrategy {
    private final double probability;

    public MineProbability(double probability) {
      this.probability = probability;
    }

    @Override
    public void setupMines(Grid<MinesweeperField> grid, Coordinate2D pressedByUser) {
      Collection<MinesweeperField> fields = getNonCursorFields(grid, pressedByUser);

      Random random = new Random();
      for (MinesweeperField field : fields) {
        if (random.nextDouble() < probability) {
          field.setMine(true);
        }
      }
    }
  }

  public static class MineCount implements MinePlacementStrategy {
    private final int totalMineCount;

    public MineCount(int totalMineCount) {
      this.totalMineCount = totalMineCount;
    }

    public void setupMines(Grid<MinesweeperField> grid, Coordinate2D pressedByUser) {
      Collection<MinesweeperField> fields = getNonCursorFields(grid, pressedByUser);

      Random random = new Random();
      int mineableFieldCount = fields.size();
      for (int mineCount = 0; mineCount < totalMineCount; mineCount++) {
        int mineIndex = random.nextInt(mineableFieldCount - mineCount);
        Collection<MinesweeperField> mineableFields = filter(fields, f -> !f.isMine());
        MinesweeperField field = Iterables.get(mineableFields, mineIndex);
        field.setMine(true);
      }
    }
  }
}
