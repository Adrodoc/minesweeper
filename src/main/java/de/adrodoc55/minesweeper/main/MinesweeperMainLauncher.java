package de.adrodoc55.minesweeper.main;

import com.jdotsoft.jarloader.JarClassLoader;

public class MinesweeperMainLauncher {
  public static void main(String[] args) throws Throwable {
    JarClassLoader jcl = new JarClassLoader();
    jcl.invokeMain("de.adrodoc55.minesweeper.main.MinesweeperMain", args);
  }
}
