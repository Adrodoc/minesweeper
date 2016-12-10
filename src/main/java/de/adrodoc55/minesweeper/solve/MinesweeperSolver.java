package de.adrodoc55.minesweeper.solve;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;

import com.google.common.collect.Collections2;

import de.adrodoc55.minesweeper.ButtonState;
import de.adrodoc55.minesweeper.Coordinate2D;
import de.adrodoc55.minesweeper.Grid;
import de.adrodoc55.minesweeper.MinesweeperPanel.MinesweeperButton;

public class MinesweeperSolver {
  private final Grid<MinesweeperButton> grid;

  public MinesweeperSolver(Grid<MinesweeperButton> grid) {
    this.grid = checkNotNull(grid, "grid == null!");
  }

  public void solve(Coordinate2D start) {
    grid.getElement(start).doClick();
    while (true) {
      boolean actionPerformed = false;
      for (MinesweeperButton button : grid) {
        actionPerformed |= flagObviousMines(button);
        actionPerformed |= revealObviousFields(button);
      }
      if (!actionPerformed) {
        break;
      }
    }
  }

  private boolean flagObviousMines(MinesweeperButton button) {
    ButtonState state = button.getState();
    if (!state.isRevealed())
      return false;
    Coordinate2D coordinate = button.getCoordinate();
    int mineCount = button.getState().getDisplayedMineCount();
    Collection<MinesweeperButton> neighbours =
        Collections2.filter(grid.getNeighbours(coordinate), b -> !b.getState().isRevealed());
    boolean result = false;
    if (neighbours.size() == mineCount) {
      for (MinesweeperButton neighbour : neighbours) {
        if (neighbour.getState() != ButtonState.FLAG) {
          flag(neighbour);
          result = true;
        }
      }
    }
    return result;
  }

  private boolean revealObviousFields(MinesweeperButton button) {
    ButtonState state = button.getState();
    if (!state.isRevealed())
      return false;
    Coordinate2D coordinate = button.getCoordinate();
    int mineCount = button.getState().getDisplayedMineCount();
    long flagCount = grid.getNeighbours(coordinate).stream()
        .filter(b -> b.getState() == ButtonState.FLAG).count();
    long hiddenCount = grid.getNeighbours(coordinate).stream()
        .filter(b -> b.getState() != ButtonState.FLAG && !b.getState().isRevealed()).count();
    if (mineCount == flagCount && hiddenCount > 0) {
      button.onLeftClick();
      return true;
    }
    return false;
  }

  private void flag(MinesweeperButton neighbour) {
    ButtonState state = neighbour.getState();
    if (state == ButtonState.NORMAL) {
      neighbour.onRightClick();
    } else if (state == ButtonState.QUESTION_MARK) {
      neighbour.onRightClick();
      neighbour.onRightClick();
    }
  }
}
