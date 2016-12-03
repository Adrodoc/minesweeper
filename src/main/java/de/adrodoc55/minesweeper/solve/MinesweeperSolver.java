package de.adrodoc55.minesweeper.solve;

import java.awt.event.MouseEvent;
import java.util.Collection;

import com.google.common.collect.Collections2;

import de.adrodoc55.minesweeper.ButtonState;
import de.adrodoc55.minesweeper.Coordinate2D;
import de.adrodoc55.minesweeper.Grid;
import de.adrodoc55.minesweeper.MinesweeperPanel.MinesweeperButton;

public class MinesweeperSolver {
  public void solve(Grid<MinesweeperButton> grid, Coordinate2D start) {
    grid.getElement(start).doClick();
    for (MinesweeperButton button : grid) {
      ButtonState state = button.getState();
      if (!state.isRevealed())
        continue;
      Coordinate2D coordinate = button.getCoordinate();
      int mineCount = button.getState().getDisplayedMineCount();
      Collection<MinesweeperButton> neighbours =
          Collections2.filter(grid.getNeighbours(coordinate), b -> !b.getState().isRevealed());
      if (neighbours.size() == mineCount) {
        for (MinesweeperButton neighbour : neighbours) {
          flag(neighbour);
        }
      }
    }
  }

  private void flag(MinesweeperButton neighbour) {
    ButtonState state = neighbour.getState();
    if (state == ButtonState.NORMAL) {
      doLeftClick(neighbour);
    } else if (state == ButtonState.QUESTION_MARK) {
      doLeftClick(neighbour);
      doLeftClick(neighbour);
    }
  }

  private void doLeftClick(MinesweeperButton neighbour) {
    neighbour.dispatchEvent(new MouseEvent(neighbour, 0, 0, 0, 0, 0, 1, false, MouseEvent.BUTTON3));
  }
}
