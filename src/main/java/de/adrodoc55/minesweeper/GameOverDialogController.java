package de.adrodoc55.minesweeper;

import static com.google.common.base.Preconditions.checkNotNull;

import java.awt.KeyboardFocusManager;
import java.awt.Window;

public class GameOverDialogController {
  private GameOverDialog view;
  private final GameOverDialog.Context context;

  public GameOverDialogController(GameOverDialog.Context context) {
    this.context = checkNotNull(context, "context == null!");
  }

  public GameOverDialog getView() {
    if (view == null) {
      Window activeWindow = KeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow();
      view = new GameOverDialog(activeWindow, context);
    }
    return view;
  }
}
