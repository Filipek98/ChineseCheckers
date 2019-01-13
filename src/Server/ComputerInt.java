package Server;

import PartsOfGame.PlayerColor;
import PartsOfGame.Point;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static java.lang.Math.abs;
import static java.lang.StrictMath.max;

public class ComputerInt {
    private Game game;
    private PlayerColor colorOfPlayer;
    private Point enemyCorner;
    private int sizeOfBoard;
    private int theLongestMove;
    private Point start, end;
    private Point[][] memory;

    ComputerInt(Game game, PlayerColor colorOfPlayer){
        this.game=game;
        this.colorOfPlayer=colorOfPlayer;
        theLongestMove=0;
        start=null;
        end=null;
        sizeOfBoard=game.sizeOfBoard;
        PlayerColor colorOfEnemy=game.getColorOfEnemy(colorOfPlayer);
        enemyCorner=enemyTopPoint(colorOfEnemy);
        memory=new Point[4][2];
    }

    PlayerColor getColorOfPlayer(){
        return colorOfPlayer;
    }
    private void findLongestMove(Point given){
        ArrayList<Point> possibleMoves=game.returnPossibleMoves(given.getX(),given.getY());
        for(Point p: possibleMoves){
            if((distance(p,enemyCorner)-distance(given,enemyCorner))>=theLongestMove){
                theLongestMove=(distance(p,enemyCorner)-distance(given,enemyCorner));

                if(theLongestMove==0 && game.gameboard[given.getX()][given.getY()].getColor()!=null){
                    continue;
                }
                start=given;
                end=p;
            }
        }
    }
    private int distance(Point a,Point b){
        return max(abs(a.getX()-b.getX()),(a.getY()-b.getY()));
    }
    private Point enemyTopPoint(PlayerColor playerColor){
        int radius=(sizeOfBoard-1)/4;
        switch (playerColor){
            case BLACK:return new Point(radius,4*radius);
            case GREEN:return new Point(radius,radius);
            case BLUE:return new Point(0,3*radius);
            case RED:return new Point(3*radius, 0);
            case YELLOW:return new Point(3*radius,3*radius);
            default:return new Point(4*radius,radius);
        }
    }
    void makeMove(){
        theLongestMove=0;
        start=null;
        end=null;

        for(int i=0;i<sizeOfBoard;i++){
            for(int j=0;j<sizeOfBoard;j++){
                if(game.gameboard[i][j]!=null){
                    if(game.gameboard[i][j].getPawn()!=null){
                        if(game.gameboard[i][j].getPawn().getColor()==colorOfPlayer){
                            findLongestMove(new Point(i,j));
                        }
                    }
                }
            }
        }
        if(start==null||end==null){
            game.changeTurn();
        }else{
            try{
                TimeUnit.MILLISECONDS.sleep(400);
            }catch(InterruptedException e){
                System.out.print("SLEEP FAILURE");
            }
            game.move(start.getX(),start.getY(),end.getX(),end.getY());
        }
    }
}
