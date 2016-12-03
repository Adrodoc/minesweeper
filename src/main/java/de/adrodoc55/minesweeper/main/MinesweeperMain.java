package de.adrodoc55.minesweeper.main;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import de.adrodoc55.minesweeper.MinesweeperFrame;

public class MinesweeperMain {
  public static void main(String[] args) throws Exception {
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    SwingUtilities.invokeLater(() -> {
      MinesweeperFrame frame = new MinesweeperFrame();
      frame.setVisible(true);
    });
  }
}
