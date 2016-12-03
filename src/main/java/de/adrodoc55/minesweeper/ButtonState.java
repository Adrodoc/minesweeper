package de.adrodoc55.minesweeper;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import javax.swing.ImageIcon;

public enum ButtonState {
  ZERO(0), //
  ONE(1), //
  TWO(2), //
  THREE(3), //
  FOUR(4), //
  FIVE(5), //
  SIX(6), //
  SEVEN(7), //
  EIGHT(8), //
  NORMAL("field.png", "selected_field.png"), //
  FLAG("flag_field.png", "selected_flag_field.png"), //
  QUESTION_MARK("question_mark_field.png", "selected_question_mark_field.png"), //
  MINE("mine_field.png"), //
  EXPLODED_MINE("exploded_mine_field.png"),//
  ;

  private static ImageIcon createIcon(String name) {
    return new ImageIcon(MinesweeperPanel.class.getResource(name));
  }

  private final ImageIcon defaultIcon;
  private final ImageIcon rolloverIcon;
  private int displayedMineCount = -1;

  private ButtonState(int displayedMineCount) {
    this(displayedMineCount + "_field.png");
    this.displayedMineCount = displayedMineCount;
  }

  private ButtonState(String defaultIcon) {
    this.defaultIcon = createIcon("/icons/" + checkNotNull(defaultIcon, "defaultIcon == null!"));
    this.rolloverIcon = null;
  }

  private ButtonState(String defaultIcon, String rolloverIcon) {
    checkNotNull(defaultIcon, "defaultIcon == null!");
    checkNotNull(rolloverIcon, "rolloverIcon == null!");
    this.defaultIcon = createIcon("/icons/" + defaultIcon);
    this.rolloverIcon = createIcon("/icons/" + rolloverIcon);
  }

  public ButtonState getNext() {
    checkState(!isRevealed(), "getNext() may only be called on a state that is not revealed");
    return values()[NORMAL.ordinal() + ((ordinal() + 1) % 3)];
  }

  public boolean isRevealed() {
    return rolloverIcon == null;
  }

  public ImageIcon getDefaultIcon() {
    return defaultIcon;
  }

  public ImageIcon getRolloverIcon() {
    return rolloverIcon;
  }

  public int getDisplayedMineCount() {
    return displayedMineCount;
  }
}
