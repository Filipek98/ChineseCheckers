package Server;

import PartsOfGame.PlayerColor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class Player extends Thread {
    private String name;
    private BufferedReader in;
    private PrintWriter out;
    private Socket socket;
    private int status;
    private Game game;
    private PlayerColor color;

    public Player(Socket socket){
        this.socket=socket;
        status=0;
        game=null;
        color=null;
    }
    @Override
    public void run(){
        try{
            in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out=new PrintWriter(socket.getOutputStream(), true);

            Submitname();
            Games();

            while(true){
                String command=in.readLine();

                if(status==0){
                    if(command.startsWith("JOIN GAME")){
                        String input=in.readLine();
                        int index=Integer.valueOf(input);
                        status=1;

                        ArrayList<Player> playersInGame=Server.getPlayersFromGame(index);
                        if(!playersInGame.contains(this)){
                            Server.addPlayerToGame(index,this);
                            game=Server.getSpecyficGame(index);

                            setColor();

                            out.println("NEW GAME WINDOW");
                            out.println(Server.getSpecyficGame(index).boardDraw);
                            out.println(Server.getSpecyficGame(index).numberOfPlayers);
                        }
                        for(Player p: Server.getAllPlayers()){
                            p.Games();
                        }
                    }else if(command.startsWith("CREATE")){
                        String sizeOfBoard=in.readLine();
                        int numberOfPlayers=Integer.valueOf(in.read());
                        status=1;
                        try{
                            int size=Integer.parseInt(sizeOfBoard);
                            out.println("GAME");
                            out.println(size);
                            out.println(numberOfPlayers);

                            game=new Game(size,numberOfPlayers);
                            Server.addGame(game);
                            setColor();
                            game.addPlayer(this);

                            for(Player p: Server.getAllPlayers()){
                                p.Games();
                            }
                    }catch (NumberFormatException e){
                            continue;
                        }
                }
            }if(status==1){
                    if(command.startsWith("CLOSE GAME")){
                        Server.removePlayerFromGame(Server.getIndexOfGame(game),this);
                        if(Server.getPlayersFromGame(Server.getIndexOfGame(game)).isEmpty()){
                            Server.removeGame(game);
                        }
                        game=null;
                        status=0;

                        for(Player p: Server.getAllPlayers()){
                            p.Games();
                            p.out.println("REFRESH");
                        }
                    }else if(command.startsWith("START GAME")){
                        Server.getSpecyficGame(Server.getIndexOfGame(game)).isProgress=true;
                        game.setStartingPlayer();
                        game.refreshCurrentPlayerView();
                    }else if(command.startsWith("getMoves")||command.startsWith("canMove")){
                        if(game.getPlayerByColor(game.currentPlayer)==this){
                            game.decodeMessage(command);
                        }
                    }else if(command.startsWith("PASS")){
                        game.changeTurn();
                    }
                }

                }
        }catch(IOException e){
            System.out.print(e);
        }finally{
            Server.removeName(name);
        }
    }

    void returnMessage(String message){
        out.println("TEST");
        out.println(message);
    }
    void changeCurrentPlayer(Player player){
        out.println("CURRENT PLAYER");
        out.println(player.name);
    }
    private void Games(){
        out.println("RESET");
        for(Game g: Server.getAllGames()){
            out.println("NEW GAME");
            for(Player p: game.players){
                out.println(p.name);
            }
            out.println("STOP");
        }
    }
    public PlayerColor getColor(){
        return color;
    }
    private void setColor(){
        for(PlayerColor pc : game.getPlayerColors()){
            if(game.currentColors.contains(pc)) {
                System.out.println(pc + " in use");
            }else{
                game.currentColors.add(pc);
                System.out.println(pc+" not in use");
                this.color=pc;
                break;
            }
        }
    }
    private void Submitname() {
        try {
            while (true) {
                out.println("SUBMITNAME");
                name = in.readLine();
                if (name == null) {
                    return;
                }
                synchronized (Server.names) {
                    if (!Server.getNames().contains(name)) {
                        out.println("NAME ACCEPTD");
                        Server.addName(name);
                        break;
                    }
                }
            }
        } catch (IOException e) {
            System.out.print("EXCEPTION");
        }
    }
}
