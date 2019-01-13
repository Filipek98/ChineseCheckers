package Game;

import PartsOfGame.Point;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class Action {

    private GameBoardPanel panel;
    private Client client;
    private String name;
    private BufferedReader in;//to Socket
    private PrintWriter out;
    FieldButton buttonClicked;
    ArrayList<Point> backlight;

    public Action(Client client){
        buttonClicked=null;
        backlight=null;
        panel=null;
        name="";
        this.client=client;

        try{
            Socket socket=new Socket(client.setTheServerAdress(), 4040);
            in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out=new PrintWriter(socket.getOutputStream(),true);
        }catch(IOException e){
            e.printStackTrace();
        }
        setNameOfWindow();
    }



    public void createGame(String number){
        int a;
        while(true){
            try{
                String numberOfPlayers=getNumberOfPlayers();
                a=Integer.valueOf(numberOfPlayers);
            }catch(NumberFormatException e){
                continue;//Tutaj ewentualnie wyswietlic blad
            }
            break;
        }
        out.println("Create");
        out.println(number);
        out.println(a);
    }
    private String getNumberOfPlayers(){
        return JOptionPane.showInputDialog(client.frame, "Give number of players:", "Number of players", JOptionPane.PLAIN_MESSAGE);
    }

    void Move(int x1,int y1,int x2,int y2 ){
        buttonClicked=null;
        panel.movePwn(x1,y1,x2,y2);
    }
    void fieldClicked(FieldButton a){
        if(buttonClicked==null){
            if(a.getPawn()!=null){
                buttonClicked=a;
            }
        }else {
            if(buttonClicked.equals(a)){
                buttonClicked=null;
                fieldClicked(a);
            }else if(a.getPawn()!=null){
                buttonClicked=a;
                //
                //
            }else {
                int x1=buttonClicked.getx();
                int y1=buttonClicked.gety();
                int x2=a.getx();
                int y2=a.gety();
                //
            }
        }
    }
    public void run(){
        while(true){
            try{
                String line=in.readLine();
                if(line.equals("RESET")){
                    client.clearL();//create the method to clear lobby in client class
                }else if(line.equals("GAME WINDOW")){
                    String line2=in.readLine();
                    String numberOfPlayers=in.readLine();
                    client.drawWindow(line2,numberOfPlayers);//create the method in client class
                }else if(line.equals("NEW GAME WINDOW")){
                    ArrayList<String> players=getInformationAboutGame();
                    client.refreshLobby(players);
                }else if(line.equals("REFRESH")){
                    client.frame.setVisible(true);
                    client.refresh();
                }else if(line.equals("TEST")){//??
                    String secondLine=in.readLine();
                    decodeMessage(secondLine);
                }else if(line.equals("CURRENT PLAYER")){
                    String secondLine=in.readLine();
                    panel.setCurrentPlayerDisplay(secondLine);
                }
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }
    void decodeMessage(String message){
        String code[]=message.split(",");

        if(code[0].equals("move")){
            int x1,x2,y1,y2;
            x1=Integer.parseInt(code[1]);
            y1=Integer.parseInt(code[2]);
            x2=Integer.parseInt(code[3]);
            y2=Integer.parseInt(code[4]);
            doMove(x1,y1,x2,y2);
        }else if(code[0].equals("wrongMove")){
            buttonClicked=null;
            lowlight();
        }else if(code[0].equals("returnMoves")){
            int i=1;
            int x,y;
            ArrayList<Point> array=new ArrayList<>();

            while(!code[i].equals("end")){
                x=Integer.parseInt(code[i]);
                y=Integer.parseInt(code[i+1]);
                i+=2;
                array.add(new Point(x,y));
            }
            backlight(array);
        }else if(code[0].equals("pog")){
            String msg=code[1];
            JOptionPane.showMessageDialog(null,msg+" won!","Congratulation",JOptionPane.INFORMATION_MESSAGE);
        }
    }
    void lowlight(){
        if(backlight==null){
            return;
        }if(backlight.size()!=0){
            panel.lowlight(backlight);
            backlight.clear();
        }
    }
    void backlight(ArrayList<Point> points){
        backlight=points;
        panel.backlight(points);
    }
    void doMove(int x1,int y1,int x2,int y2){
        lowlight();
        buttonClicked=null;
        panel.movePwn(x1,y1,x2,y2);
    }
    public String getName(){
        return name;
    }
    public void joinGame(int index){
        out.println("JOIN GAME");
        out.println(index);
    }

    public void setNameOfWindow() {
       while(true){
           try{
               String name=in.readLine();
               if(name.equals("SUBMITNAME")){
                   name=client.getName();
                   out.println(name);
               }else if(name.equals("NAME ACCEPTED")){
                   break;
               }
           }catch(IOException e){
               e.printStackTrace();
           }
       }
    }
    private ArrayList<String> getInformationAboutGame(){
        ArrayList<String> players=new ArrayList<>();
        try{
            String line=in.readLine();
            while (!line.equals("STOP")){
                players.add(line);
                line=in.readLine();
            }
            return players;
        }catch(IOException e){
            e.printStackTrace();
        }
        return players;
    }
}
