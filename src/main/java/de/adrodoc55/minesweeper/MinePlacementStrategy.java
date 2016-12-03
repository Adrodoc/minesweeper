package de.adrodoc55.minesweeper;

import static com.google.common.collect.Iterables.filter;

import java.util.Random;

import com.google.common.collect.Iterables;

public interface MinePlacementStrategy {
  void setupMines(Grid<MinesweeperField> grid, Coordinate2D pressedByUser);

  public static class MineProbability implements MinePlacementStrategy {
    private final double probability;

    public MineProbability(double probability) {
      this.probability = probability;
    }

    @Override
    public void setupMines(Grid<MinesweeperField> grid, Coordinate2D pressedByUser) {
      Iterable<MinesweeperField> notPressedFields =
          filter(grid, f -> !pressedByUser.equals(f.getCoordinate()));
      Random random = new Random();
      for (MinesweeperField field : notPressedFields) {
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
      Random random = new Random();
      Iterable<MinesweeperField> notPressedFields =
          filter(grid, f -> !pressedByUser.equals(f.getCoordinate()));
      int mineableFieldCount = grid.size() - 1;
      for (int mineCount = 0; mineCount < totalMineCount; mineCount++) {
        int mineIndex = random.nextInt(mineableFieldCount - mineCount);
        Iterable<MinesweeperField> mineableFields = filter(notPressedFields, f -> !f.isMine());
        MinesweeperField field = Iterables.get(mineableFields, mineIndex);
        field.setMine(true);
      }
    }
  }
}
