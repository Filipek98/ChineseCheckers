package Server;

import PartsOfGame.*;

import java.lang.management.PlatformLoggingMXBean;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

public class Game {
    ArrayList<Player> players;
    private ArrayList<ComputerInt> computerPlayers;
    private HashMap<PlayerColor,Integer> score;
    public Field[][] gameboard;
    int boardDraw;
    boolean isProgress;
    PlayerColor currentPlayer;
    int numberOfPlayers;
    private ArrayList<PlayerColor> playerColors;
    ArrayList<PlayerColor> currentColors;

    private Point constants[];
    int sizeOfBoard;

    public Game(int sizeOfBoard,int numberOfPlayers){
        GameboardCreator creator=new GameboardCreator(sizeOfBoard,numberOfPlayers);
        constants=creator.getConstants();
        gameboard=creator.getBoard();
        this.sizeOfBoard=4*sizeOfBoard+1;
        this.numberOfPlayers=numberOfPlayers;
        boardDraw=sizeOfBoard;

        players=new ArrayList<>();
        isProgress=false;
        currentPlayer=null;

        playerColors=setColorsOfPlayers(numberOfPlayers);
        currentColors=new ArrayList<>();

        score=new HashMap<>();
        int x=((1+sizeOfBoard)*sizeOfBoard)/2;
        for(PlayerColor p: playerColors){
            score.put(p,x);
        }
    }
    public  boolean canMove(int x1,int y1,int x2,int y2){
        if(currentPlayer==null){
            return false;
        }
        return(returnPossibleMoves(x1,y1).contains(new Point(x2,y2)));
    }
    PlayerColor getColorOfEnemy(PlayerColor player){
        switch(player){
            case RED: return PlayerColor.BLACK;
            case WHITE: return PlayerColor.BLUE;
            case YELLOW: return PlayerColor.GREEN;
            case BLACK: return PlayerColor.RED;
            case BLUE: return  PlayerColor.WHITE;
            default: return PlayerColor.YELLOW;
        }
    }
    void addPlayer(Player player){
        players.add(player);
    }
    private void sendMessage(String message){
        if(currentPlayer==null){
            return;
        }getPlayerByColor(currentPlayer).returnMessage(message);
    }
    void changeTurn(){
        int i=playerColors.indexOf(currentPlayer);
        i++;
        i=i%numberOfPlayers;
        currentPlayer=playerColors.get(i);
        if(computerPlayers!=null){
            for(ComputerInt computer: computerPlayers){
                if(computer.getColorOfPlayer()==currentPlayer){
                    computer.makeMove();
                    break;
                }
            }
        }
        for(Player p: players){
            if(p.getColor()==currentPlayer){
                refreshCurrentPlayerView();
                break;
            }
        }
    }
    Player getPlayerByColor(PlayerColor playerColor){
        Player player=null;
        for(Player p: players){
            if(p.getColor()==playerColor){
                player=p;
            }
        }
        return player;
    }
    void refreshCurrentPlayerView(){
        for(Player p: players){
            p.changeCurrentPlayer(getPlayerByColor(currentPlayer));
        }
    }
    void decodeMessage(String message){
        String code[]=message.split(",");
        if(code[0].equals("getMoves")){
            int x,y;
            x=Integer.parseInt(code[1]);
            y=Integer.parseInt(code[2]);

            if(!(gameboard[x][y].getPawn().getColor() == currentPlayer)){
                return;
            }
            String array="returnMoves";
            ArrayList<Point> p=returnPossibleMoves(x,y);
            if(p==null){
                return;
            }
            for(Point point : p){
                array=array+","+point.getX()+","+point.getY();
            }
            array+=",end";
            sendMessage(array);//show files where player can move
        }else if(code[0].equals("canMove")){
            int x1,y1,x2,y2;
            x1 = Integer.parseInt(code[1]);
            y1 = Integer.parseInt(code[2]);
            x2 = Integer.parseInt(code[3]);
            y2 = Integer.parseInt(code[4]);
            boolean canm=canMove(x1,y1,x2,y2);
            if(canm){
                move(x1,y1,x2,y2);
            }else{
                sendMessage("wrongMove");
            }
        }else if(code[0].equals("changeTurn")){
            changeTurn();
        }

    }
    void setStartingPlayer(){
        if(players.size()<numberOfPlayers){
            fillWithComputerInt();
        }
        Random ran=new Random();
        currentPlayer=players.get(ran.nextInt(players.size())).getColor();
    }
    private void fillWithComputerInt(){
        computerPlayers=new ArrayList<>();
        for(PlayerColor p: playerColors){
            boolean add=true;
            for(Player player:players){
                if(player.getColor()==p){
                    add=false;
                }if(add){
                    computerPlayers.add(new ComputerInt(this,p));
                }
                add=true;
            }
        }
    }
    void move(int x1,int y1,int x2,int y2){
        if(gameboard[x1][y1].getColor()==null&&gameboard[x2][y2]!=null){
            if(gameboard[x2][y2].getColor()==getColorOfEnemy(gameboard[x1][y1].getPawn().getColor())){
                PlayerColor key=gameboard[x1][y1].getPawn().getColor();
                int val=score.get(key);
                val--;
                score.replace(key,val);
                if(val==0){
                    for(Player p: players){
                        p.returnMessage("pog,"+key.name());
                    }
                }
            }
        }
        gameboard[x2][y2].setPawn(new Pawn(gameboard[x1][y1].getPawn().getColor()));
        gameboard[x1][y1].setPawn(null);
        for(Player p: players){
            p.returnMessage("move,"+x1+","+y1+","+x2+","+y2);
        }
        changeTurn();
    }

    public ArrayList<Point> returnPossibleMoves(int x,int y){
        if(currentPlayer==null){
            return null;
        }
        ArrayList<Point> possibleMoves=new ArrayList<>();
        int tmpX,tmpY;
        for(Point way: constants){
            tmpX=x+way.getX();
            tmpY=y+way.getY();
            if(tmpX<0||tmpY<0 || tmpX>=sizeOfBoard || tmpY>=sizeOfBoard || gameboard[tmpX][tmpY]==null){
                continue;
            }
            if(gameboard[tmpX][tmpY].getPawn()==null){
                if(!possibleMoves.contains(new Point(tmpX,tmpY))){
                    possibleMoves.add(new Point(tmpX,tmpY));
                }
            }else{
                tmpX+=way.getX();
                tmpY+=way.getY();
                if(tmpX<0||tmpY<0 || tmpX>=sizeOfBoard || tmpY>=sizeOfBoard || gameboard[tmpX][tmpY]==null){
                    continue;
                }
                if(gameboard[tmpX][tmpY].getPawn()==null){
                    if(!possibleMoves.contains(new Point(tmpX,tmpY))){
                        possibleMoves.add(new Point(tmpX,tmpY));
                        serchJumps(tmpX,tmpY,way,possibleMoves);
                    }
                }
            }
        }

        if(gameboard[x][y].getColor()!=null){
            if(gameboard[x][y].getColor()==getColorOfEnemy(gameboard[x][y].getPawn().getColor())){
                PlayerColor color=getColorOfEnemy(gameboard[x][y].getPawn().getColor());

                Iterator<Point> iterator=possibleMoves.iterator();
                while (iterator.hasNext()){
                    Point p=iterator.next();
                    if(gameboard[p.getX()][p.getY()].getColor()!=color){
                        iterator.remove();
                    }
                }
            }
        }
        return possibleMoves;
    }
    private void serchJumps(int x,int y,Point comeFrom,ArrayList<Point> possibleMove){
        int tmpX,tmpY;
        for(Point way : constants){
            if((way.getX()==-(comeFrom.getX())) && (way.getY()==-(comeFrom.getY()))){
                continue;
            }
            tmpX=x+way.getX();
            tmpY=y+way.getY();

            if(tmpX<0 || tmpY<0 || tmpX>=sizeOfBoard || tmpY>=sizeOfBoard || gameboard[tmpX][tmpY]==null){
                continue;
            }
            if(gameboard[tmpX][tmpY].getPawn()==null){
                tmpX+=way.getX();
                tmpY+=way.getY();
                if(tmpX<0 || tmpY<0 || tmpX>=sizeOfBoard || tmpY>=sizeOfBoard || gameboard[tmpX][tmpY]==null){
                    continue;
                }
                if(gameboard[tmpX][tmpY].getPawn()==null) {
                    if (!possibleMove.contains(new Point(tmpX, tmpY))) {
                        possibleMove.add(new Point(tmpX, tmpY));
                        serchJumps(tmpX, tmpY, way, possibleMove);
                    }
                }
            }
        }
    }
    ArrayList<PlayerColor> getPlayerColors(){
        return playerColors;
    }
    private ArrayList<PlayerColor> setColorsOfPlayers(int number){
        ArrayList<PlayerColor> playerColor=new ArrayList<>();
        switch(number){
            case 2:
                playerColor.add(PlayerColor.RED);
                playerColor.add(PlayerColor.BLACK);
                break;
            case 3:
                playerColor.add(PlayerColor.BLACK);
                playerColor.add(PlayerColor.BLUE);
                playerColor.add(PlayerColor.YELLOW);
                break;
            case 4:
                playerColor.add(PlayerColor.RED);
                playerColor.add(PlayerColor.BLACK);
                playerColor.add(PlayerColor.WHITE);
                playerColor.add(PlayerColor.BLUE);
                break;
            case 6:
                playerColor.add(PlayerColor.RED);
                playerColor.add(PlayerColor.BLACK);
                playerColor.add(PlayerColor.WHITE);
                playerColor.add(PlayerColor.BLUE);
                playerColor.add(PlayerColor.GREEN);
                playerColor.add(PlayerColor.YELLOW);
                break;
        }
        return playerColor;
    }
}
