package Game;

import javax.swing.*;
public class GameWindow extends JFrame
{
    public GameBoardPanel panel;
    public GameWindow(int radius,int numberOfPlayers,Action action)
    {
        this.setResizable(false);
        this.setSize(750,750);
        this.setLayout(null);


        panel = new GameBoardPanel(radius,null,action , numberOfPlayers);
        this.add(panel);

        this.setVisible(true);
    }
}