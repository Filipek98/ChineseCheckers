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
    private String nameOfWindow;
    private BufferedReader in;//to Socket
    private PrintWriter out;
    FieldButton buttonClicked;
    ArrayList<Point> backlight;

    public Action(Client client){
        buttonClicked=null;
        backlight=null;
        panel=null;
        nameOfWindow="";
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
}
