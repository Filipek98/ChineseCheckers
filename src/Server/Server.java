package Server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Server {
    static ArrayList<String> names;
    private static ArrayList<Game> games;
    private static ArrayList<Player> players;

    private Server() throws IOException{
        ServerSocket listener=new ServerSocket(4040);
        InetAddress ip;
        names=new ArrayList<>();
        players=new ArrayList<>();
        games=new ArrayList<>();

        try{
            ip=InetAddress.getLocalHost();
            System.out.println("Current IP adress: "+ip.getHostAddress());
        }catch(UnknownHostException e){
            e.printStackTrace();
        }
        try{
            while(true){
                Player player=new Player(listener.accept());
                player.start();
                players.add(player);
            }
        }finally {
            listener.close();
        }
    }
    static ArrayList<Player> getAllPlayers(){
        return players;
    }
    static void addGame(Game game){
        games.add(game);
    }
    static Game getSpecyficGame(int index){
        return games.get(index);
    }
    static void removeGame(Game game){
        games.remove(game);
    }
    static void removePlayerFromGame(int index,Player player){
        games.get(index).players.remove(player);
    }
    static int getIndexOfGame(Game game){
        return games.indexOf(games);
    }
    static ArrayList<Game> getAllGames(){
        return games;
    }
    static ArrayList<Player> getPlayersFromGame(int index){
        return games.get(index).players;
    }
    static void addPlayerToGame(int index ,Player player){
        games.get(index).players.add(player);
    }
    static void addName(String name){
        names.add(name);
    }
    static ArrayList<String> getNames(){
        return names;
    }
    static void removeName(String name){
        names.remove(name);
    }
    public static void main(String[] args) throws IOException{
        Server server=new Server();
    }




}
