package de.adrodoc55.minesweeper;

import static com.google.common.base.Preconditions.checkNotNull;
import static de.adrodoc55.minesweeper.MinesweeperFrame.FIELD_SIZE;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Collection;

import javax.annotation.Nullable;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

public class MinesweeperPanel extends JPanel {
  private static final long serialVersionUID = 1L;

  private final int height;
  private final int width;
  private final MinePlacementStrategy strategy;
  private @Nullable MinesweeperGrid grid;
  private final Grid<MinesweeperButton> buttons;
  private final GameOverDialogController ctrl =
      new GameOverDialogController(new GameOverDialog.Context() {
        @Override
        public void spielErneutStarten() {
          resetButtons();
        }

        @Override
        public void neuesSpielStarten() {
          grid = null;
          resetButtons();
        }
      });

  public MinesweeperPanel(int height, int width, MinePlacementStrategy strategy) {
    this.height = height;
    this.width = width;
    this.strategy = checkNotNull(strategy, "strategy == null!");
    GridLayout mgr = new GridLayout();
    mgr.setRows(height);
    mgr.setColumns(width);
    setLayout(mgr);
    buttons = new Grid<>(height, width, new Grid.ElementSupplier<MinesweeperButton>() {
      @Override
      public MinesweeperButton supply(Coordinate2D coordinate) {
        MinesweeperButton button = new MinesweeperButton(coordinate);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.gridx = coordinate.getX();
        gbc.gridy = coordinate.getY();
        add(button, gbc);
        return button;
      }
    });
  }

  public Grid<MinesweeperButton> getButtons() {
    return buttons;
  }

  private MinesweeperGrid getGrid(Coordinate2D pressedByUser) {
    if (grid == null) {
      grid = new MinesweeperGrid(height, width, pressedByUser, strategy);
    }
    return grid;
  }

  private void resetButtons() {
    for (MinesweeperButton button : buttons) {
      button.reset();
    }
  }

  public boolean checkWinCondition(Coordinate2D pressedByUser) {
    boolean won = isWon();
    if (won) {
      win(pressedByUser);
    }
    return won;
  }

  private boolean isWon() {
    for (MinesweeperButton button : buttons) {
      if (!button.isRevealed() && !button.getField().isMine()) {
        return false;
      }
    }
    return true;
  }

  private void win(Coordinate2D pressedByUser) {
    terminateGame(pressedByUser, "GEWONNEN!!!");
  }

  public void loose(Coordinate2D pressedByUser) {
    terminateGame(pressedByUser, "Spiel beendet!");
  }

  private void terminateGame(Coordinate2D pressedByUser, String title) {
    revealMines(pressedByUser);
    GameOverDialog view = ctrl.getView();
    view.setTitle(title);
    view.setVisible(true);
  }

  private void revealMines(Coordinate2D pressedByUser) {
    for (MinesweeperButton button : buttons) {
      MinesweeperGrid grid = getGrid(pressedByUser);
      Coordinate2D coordinate = button.getCoordinate();
      MinesweeperField field = grid.getElement(coordinate);
      if (field.isMine()) {
        if (coordinate.equals(pressedByUser)) {
          button.setState(ButtonState.EXPLODED_MINE);
        } else {
          button.setState(ButtonState.MINE);
        }
      }
    }
  }

  public class MinesweeperButton extends JButton {
    private static final long serialVersionUID = 1L;
    private final Coordinate2D coordinate;
    private ButtonState state = ButtonState.NORMAL;

    public MinesweeperButton(Coordinate2D coordinate) {
      this.coordinate = checkNotNull(coordinate, "coordinate == null!");
      reset();
      addMouseListener(new MouseAdapter() {
        @Override
        public void mouseEntered(MouseEvent e) {
          setBackground(new Color(35, 70, 70));
        }

        @Override
        public void mouseExited(MouseEvent e) {
          setBackground(Color.BLACK);
        }

        @Override
        public void mouseClicked(MouseEvent e) {
          if (e.getButton() == MouseEvent.BUTTON3) {
            onRightClick();
          }
        }
      });
      addActionListener(e -> {
        onLeftClick();
      });
    }

    public void onRightClick() {
      if (!isRevealed()) {
        setState(state.getNext());
      }
    }

    public void onLeftClick() {
      try {
        handleLeftClick();
      } catch (GameWonException ex) {
        win(coordinate);
      } catch (GameLostException ex) {
        loose(ex.getMineCoordinate());
      }
    }

    private void handleLeftClick() throws GameWonException, GameLostException {
      if (isRevealed()) {
        Collection<MinesweeperButton> neighbours = buttons.getNeighbours(coordinate);
        long flagCount = neighbours.stream().filter(b -> b.state == ButtonState.FLAG).count();
        if (flagCount == grid.check(coordinate)) {
          clickNeighbours();
        }
        return;
      }
      if (state == ButtonState.FLAG)
        return;
      int mineCount = getGrid(coordinate).check(coordinate);
      if (mineCount < 0) {
        throw new GameLostException(coordinate);
      }
      setState(ButtonState.values()[mineCount]);
      if (mineCount == 0) {
        clickNeighbours();
      }
      if (isWon()) {
        throw new GameWonException();
      }
    }

    private void clickNeighbours() throws GameWonException, GameLostException {
      Collection<MinesweeperButton> neighbours = buttons.getNeighbours(coordinate);
      for (MinesweeperButton button : neighbours) {
        if (!button.isRevealed()) {
          button.handleLeftClick();
        }
      }
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
      super.setBounds(x, y, width, height);
      setState(state);
    }

    @Override
    public Dimension getPreferredSize() {
      return new Dimension(FIELD_SIZE + 2, FIELD_SIZE + 2);
    }

    public void reset() {
      setState(ButtonState.NORMAL);
      setBackground(Color.BLACK);
    }

    public boolean isRevealed() {
      return state.isRevealed();
    }

    public ButtonState getState() {
      return state;
    }

    private void setState(ButtonState state) {
      this.state = checkNotNull(state, "state == null!");
      setIcon(resizeIcon(state.getDefaultIcon()));
      setRolloverIcon(resizeIcon(state.getRolloverIcon()));
    }

    private ImageIcon resizeIcon(ImageIcon icon) {
      if (icon == null)
        return null;
      int width = getWidth() - 2;
      int height = getHeight() - 2;
      if (width <= 0 || height <= 0)
        return null;

      BufferedImage resizedImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
      Graphics2D g2 = resizedImg.createGraphics();
      g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
          RenderingHints.VALUE_INTERPOLATION_BILINEAR);
      g2.drawImage(icon.getImage(), 0, 0, width, height, null);
      g2.dispose();
      return new ImageIcon(resizedImg);
    }

    public Coordinate2D getCoordinate() {
      return coordinate;
    }

    private MinesweeperField getField() {
      return getGrid(coordinate).getElement(coordinate);
    }

    @Override
    public String toString() {
      return state.toString();
    }

  }
}
