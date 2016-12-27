package de.adrodoc55.minesweeper;

public interface GameEndingHandler {
  void win(GameWonException ex);

  void loose(GameLostException ex);
}