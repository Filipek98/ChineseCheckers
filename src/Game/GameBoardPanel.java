package Game;

import PartsOfGame.Field;
import PartsOfGame.GameboardCreator;
import PartsOfGame.Pawn;

import javax.swing.*;
import java.awt.*;


public class GameBoardPanel extends JPanel
{
    private FieldButton[][] board;
    private ColorsOfField colors;
    GameBoardPanel(int radius, Color player, int numberOfPlayers)
    {
        int size = 4 * radius + 1;
        this.board = new FieldButton[size][size];
        colors=new ColorsOfField();
        this.setLayout(null);
        this.setSize(750, 750);

        Field[][] board = new GameboardCreator(radius,numberOfPlayers).getBoard();

        int portion = 600 / (2 * size);
        int baseX = 350;
        int baseY = 350;
        int tempX, tempY;
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (board[x][y] != null) {
                    tempX = baseX + (portion * 2 * (board[x][y].getX()));
                    tempX = tempX + (((board[x][y].getY()) * portion));
                    tempY = baseY + (portion * 2 * (board[x][y].getY()));

                    FieldButton b = new FieldButton(x,y);
                    b.setBounds(tempX, tempY, 20, 20);
                    b.setPawn(board[x][y].getPawn());
                  // b.setBackground((board[x][y].getColor() == null) ? colors.empty : colors.getPawnColor(board[x][y].getColor()));
                   // b.setDefaultColor((board[x][y].getColor() == null) ? colors.empty : colors.getFieldColor(board[x][y].getColor()));
                  //  b.colorPawn(colors);


                    if(board[x][y].getColor()==null){
                        b.setBackground(colors.empty);
                    }else{
                        b.setBackground(colors.getPawnColor(board[x][y].getColor()));
                    }
                    if(board[x][y].getColor()==null){
                        b.setDefaultColor(colors.empty);
                    }else{
                        b.setDefaultColor(colors.getFieldColor(board[x][y].getColor()));
                    }
                    b.colorPawn(colors);

                    this.board[x][y] = b;
                    this.add(b);
                }
            }
        }
        this.repaint();
    }
    public void movePwn(int x1, int y1, int x2, int y2)
    {
        Pawn pawn = new Pawn(board[x1][y1].getPawn().getColor());

        board[x1][y1].setPawn(null);
        board[x2][y2].setPawn(pawn);

        board[x1][y1].colorPawn(colors);
        board[x2][y2].colorPawn(colors);
    }

}