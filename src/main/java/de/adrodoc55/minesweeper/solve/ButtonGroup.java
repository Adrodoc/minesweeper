package de.adrodoc55.minesweeper.solve;

import java.util.Collection;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;

import de.adrodoc55.minesweeper.MinesweeperPanel.MinesweeperButton;

public class ButtonGroup {
  private int min;
  private int max;

  public ButtonGroup(Collection<MinesweeperButton> buttons) {
    this(0, buttons.size(), buttons);
  }

  private final ImmutableCollection<MinesweeperButton> buttons;

  public ButtonGroup(int min, int max, Collection<MinesweeperButton> buttons) {
    this.min = min;
    this.max = max;
    this.buttons = ImmutableList.copyOf(buttons);
  }

  public int getMin() {
    return min;
  }

  public void setMin(int min) {
    this.min = min;
  }

  public int getMax() {
    return max;
  }

  public void setMax(int max) {
    this.max = max;
  }

  public ImmutableCollection<MinesweeperButton> getButtons() {
    return buttons;
  }
}
