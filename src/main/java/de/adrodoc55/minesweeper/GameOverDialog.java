package de.adrodoc55.minesweeper;

import static com.google.common.base.Preconditions.checkNotNull;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

public class GameOverDialog extends JDialog {
  private static final long serialVersionUID = 1L;
  private JPanel panel;
  private JButton btnNeuesSpiel;
  private JButton btnSpielErneutStarten;

  public interface Context {
    void neuesSpielStarten();

    void spielErneutStarten();
  }

  private final Context context;

  public GameOverDialog(Window parent, Context context) {
    super(parent);
    this.context = checkNotNull(context, "context == null!");
    getContentPane().add(getPanel(), BorderLayout.CENTER);
    getRootPane().setDefaultButton(getBtnNeuesSpiel());
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    setModal(true);
    pack();
    setLocationRelativeTo(null);
  }

  private JPanel getPanel() {
    if (panel == null) {
      panel = new JPanel();
      panel.setLayout(new GridBagLayout());
      GridBagConstraints gbc_btnNeuesSpiel = new GridBagConstraints();
      gbc_btnNeuesSpiel.fill = GridBagConstraints.BOTH;
      gbc_btnNeuesSpiel.insets = new Insets(5, 5, 5, 5);
      gbc_btnNeuesSpiel.gridx = 0;
      gbc_btnNeuesSpiel.gridy = 0;
      panel.add(getBtnNeuesSpiel(), gbc_btnNeuesSpiel);
      GridBagConstraints gbc_btnSpielErneutStarten = new GridBagConstraints();
      gbc_btnSpielErneutStarten.fill = GridBagConstraints.BOTH;
      gbc_btnSpielErneutStarten.insets = new Insets(5, 5, 5, 5);
      gbc_btnSpielErneutStarten.gridx = 1;
      gbc_btnSpielErneutStarten.gridy = 0;
      panel.add(getBtnSpielErneutStarten(), gbc_btnSpielErneutStarten);
    }
    return panel;
  }

  private JButton getBtnNeuesSpiel() {
    if (btnNeuesSpiel == null) {
      btnNeuesSpiel = new JButton("Neues Spiel");
      btnNeuesSpiel.addActionListener(e -> {
        context.neuesSpielStarten();
        dispose();
      });
    }
    return btnNeuesSpiel;
  }

  private JButton getBtnSpielErneutStarten() {
    if (btnSpielErneutStarten == null) {
      btnSpielErneutStarten = new JButton("Spiel erneut Starten");
      btnSpielErneutStarten.addActionListener(e -> {
        context.spielErneutStarten();
        dispose();
      });
    }
    return btnSpielErneutStarten;
  }
}
