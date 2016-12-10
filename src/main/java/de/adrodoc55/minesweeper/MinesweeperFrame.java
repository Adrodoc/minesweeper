package de.adrodoc55.minesweeper;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;

import de.adrodoc55.minesweeper.solve.MinesweeperSolver;

public class MinesweeperFrame extends JFrame {
  private static final long serialVersionUID = 1L;
  public static final int FIELD_SIZE = 32;

  private MinesweeperPanel minesweeperPanel;
  private JButton btnSolve;

  public MinesweeperFrame() {
    super("Minesweeper");
    getContentPane().add(getMinesweeperPanel(), BorderLayout.CENTER);
    getContentPane().add(getBtnSolve(), BorderLayout.SOUTH);
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

  private JButton getBtnSolve() {
    if (btnSolve == null) {
      btnSolve = new JButton("Solve");
      btnSolve.addActionListener(e -> {
        MinesweeperSolver solver = new MinesweeperSolver(getMinesweeperPanel().getButtons());
        solver.solve(new Coordinate2D(5, 5));
      });
    }
    return btnSolve;
  }
}
