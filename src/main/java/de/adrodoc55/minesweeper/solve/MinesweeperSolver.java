package de.adrodoc55.minesweeper.solve;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Collections2.filter;
import static java.lang.Math.max;
import static java.lang.Math.min;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
      actionPerformed |= advancedSolving();
      if (!actionPerformed) {
        break;
      }
    }
  }

  private boolean advancedSolving() {
    boolean actionPerformed = false;
    for (MinesweeperButton button : grid) {
      if (button.getState().getDisplayedMineCount() == -1)
        continue;
      Collection<MinesweeperButton> unknownNeighbours = getUnknownNeighbours(button);
      if (unknownNeighbours.size() <= 1)
        continue;
      Set<MinesweeperButton> knownNeighbours = streamNumberNeighbours(unknownNeighbours)//
          .filter(b -> b != button)//
          .filter(b -> intersection(unknownNeighbours, getUnknownNeighbours(b)).size() >= 2)//
          .collect(Collectors.toSet());
      for (MinesweeperButton knownNeighbour : knownNeighbours) {
        Collection<MinesweeperButton> a = new ArrayList<>(unknownNeighbours);
        Collection<MinesweeperButton> b = getUnknownNeighbours(knownNeighbour);
        Collection<MinesweeperButton> intersection = intersection(a, b);
        a.removeAll(intersection);
        b.removeAll(intersection);
        long missingA = getMissingMineCount(button);
        long missingB = getMissingMineCount(knownNeighbour);
        long maxInt = getMaxMineCountForFields(intersection);
        long minA = max(0, missingA - maxInt);
        long minB = max(0, missingB - maxInt);
        if (minA == a.size()) {
          for (MinesweeperButton aButton : a) {
            flag(aButton);
            actionPerformed = true;
          }
        }
        if (minB == b.size()) {
          for (MinesweeperButton bButton : b) {
            flag(bButton);
            actionPerformed = true;
          }
        }
        long minInt = max(0, max(missingA - a.size(), missingB - b.size()));
        long maxA = min(a.size(), missingA - minInt);
        long maxB = min(b.size(), missingB - minInt);
        if (maxA <= 0) {
          for (MinesweeperButton aButton : a) {
            aButton.onLeftClick();
            actionPerformed = true;
          }
        }
        if (maxB <= 0) {
          for (MinesweeperButton bButton : b) {
            bButton.onLeftClick();
            actionPerformed = true;
          }
        }
      }
    }
    return actionPerformed;
  }

  private Stream<MinesweeperButton> streamNumberNeighbours(Collection<MinesweeperButton> buttons) {
    return buttons.stream()//
        .flatMap(b -> grid.getNeighbours(b.getCoordinate()).stream())//
        .distinct()//
        .filter(b -> b.getState().getDisplayedMineCount() != -1)//
        ;
  }

  /**
   * Liefert die Anzahl der Mienen, die höchstens auf die angegebenen Felder verteilt sein können.
   *
   * @param coordinate
   * @param fields
   * @return
   */
  private long getMaxMineCountForFields(Collection<MinesweeperButton> buttons) {
    return streamNumberNeighbours(buttons)//
        .filter(b -> grid.getNeighbours(b.getCoordinate()).containsAll(buttons))//
        .mapToLong(this::getMissingMineCount)//
        .min().orElse(buttons.size())//
        ;
  }

  private long getMissingMineCount(MinesweeperButton button) {
    return button.getState().getDisplayedMineCount() - getFlagCount(button);
  }

  private Collection<MinesweeperButton> intersection(Collection<MinesweeperButton> a,
      Collection<MinesweeperButton> b) {
    Collection<MinesweeperButton> result = new ArrayList<>(a);
    result.retainAll(b);
    return result;
  }

  private Collection<MinesweeperButton> getUnknownNeighbours(MinesweeperButton button) {
    return filter(grid.getNeighbours(button.getCoordinate()), b -> b.getState().isUnknown());
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
    int mineCount = button.getState().getDisplayedMineCount();
    long flagCount = getFlagCount(button);
    long hiddenCount = getHiddenNeighbourCount(button);
    if (mineCount == flagCount && flagCount != hiddenCount) {
      button.onLeftClick();
      return true;
    }
    return false;
  }

  private long getFlagCount(MinesweeperButton button) {
    return getNeighbours(button).stream().filter(b -> b.getState() == ButtonState.FLAG).count();
  }

  private long getHiddenNeighbourCount(MinesweeperButton button) {
    return getNeighbours(button).stream().filter(b -> !b.getState().isRevealed()).count();
  }

  private Collection<MinesweeperButton> getNeighbours(MinesweeperButton button) {
    return grid.getNeighbours(button.getCoordinate());
  }

  private void flag(MinesweeperButton button) {
    ButtonState state = button.getState();
    if (state == ButtonState.NORMAL) {
      button.onRightClick();
    } else if (state == ButtonState.QUESTION_MARK) {
      button.onRightClick();
      button.onRightClick();
    }
  }
}
