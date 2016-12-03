package de.adrodoc55.minesweeper;

import java.awt.BorderLayout;

import javax.swing.JFrame;

public class MinesweeperFrame extends JFrame {
  private static final long serialVersionUID = 1L;
  public static final int FIELD_SIZE = 32;

  private MinesweeperPanel minesweeperPanel;

  public MinesweeperFrame() {
    super("Minesweeper");
    getContentPane().add(getMinesweeperPanel(), BorderLayout.CENTER);
    pack();
    setLocationRelativeTo(null);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
  }

  private MinesweeperPanel getMinesweeperPanel() {
    if (minesweeperPanel == null) {
      MinePlacementStrategy strategy = new MinePlacementStrategy.MineCount(80);
      minesweeperPanel = new MinesweeperPanel(20, 20, strategy);
    }
    return minesweeperPanel;
  }
}
