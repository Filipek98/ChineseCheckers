package Game;

//import ElementsOfMenu.HowManyPlayers;
import ElementsOfMenu.MainMenu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Client implements ActionListener {
    public JFrame frame;
    private ArrayList<JPanel> jpanels;
    private GameWindow window;
    private Action action;

    private JButton startGame, howToPlay, exit;
    private JLabel background;
    private JPanel menu;
    Color cButtons=new Color(101,17,17);

   public Client(){
        action=new Action(this);
        frame=new JFrame("Menu");
        frame.setBounds(200,200,513,389);
        frame.setResizable(false);
        frame.setVisible(true);

       menu=new JPanel();//(new GridLayout(3,1));
       frame.add(menu);
       frame.setLayout(new BorderLayout());
       background=new JLabel(new ImageIcon("C:\\Users\\fkula\\IdeaProjects\\ChineseCheckers\\img\\chinesee.jpg"));
       background.setLayout(new FlowLayout());
       frame.add(background);

       background.setLayout(null);
       startGame=new JButton("Start Game");
       howToPlay=new JButton("How to play");
       exit=new JButton("Exit");
       startGame.setBackground(cButtons);
       howToPlay.setBackground(cButtons);
       exit.setBackground(cButtons);
       startGame.setForeground(Color.BLACK);
       howToPlay.setForeground(Color.BLACK);
       exit.setForeground(Color.BLACK);

       startGame.setBounds(20,100,100,40);
       howToPlay.setBounds(350,150,100,40);
       exit.setBounds(195,303,100,40);

       background.add(startGame);
       background.add(howToPlay);
       background.add(exit);

       startGame.addActionListener(this);


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if(source==startGame){
           String number=getSizeOfBoard();
           action.createGame(number);
        }
        if(source==howToPlay){
            JOptionPane.showMessageDialog( frame,"Application created by Michał Jachorek and Filip Kulasza","Information",JOptionPane.PLAIN_MESSAGE);
        }
    }
    public void clearL(){//??
       frame.getContentPane().removeAll();
       jpanels = new ArrayList<>();
    }
    public void refreshLobby(ArrayList<String> players){
       JPanel panel=addAButton();

       for(String s : players){
           JLabel newlebel=new JLabel(s);
           panel.add(newlebel);
       }
       jpanels.add(panel);
       frame.getContentPane().add(panel);

       refresh();

    }
    public void refresh(){
       frame.invalidate();
       frame.validate();
       frame.repaint();
    }
    private JPanel addAButton(){
       final JPanel panel=new JPanel(new FlowLayout(FlowLayout.LEFT));
       JButton newbutton=new JButton("Join Game");
       newbutton.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               action.joinGame(jpanels.indexOf(panel));
           }
       });
       newbutton.setAlignmentX(JButton.RIGHT);
       panel.setAlignmentX(Component.CENTER_ALIGNMENT);
       panel.add(newbutton);
       return panel;
    }

    public void run(){
       action.run();
    }
    public void drawWindow(String size, String numberOfPlayers){
       window=new GameWindow(Integer.valueOf(size),Integer.valueOf(numberOfPlayers), action);
       frame.setVisible(false);//Lobby menu
    }

    private String getSizeOfBoard(){
        return JOptionPane.showInputDialog(frame,"Give board size:", "Board size", JOptionPane.PLAIN_MESSAGE);
    }
    public String setTheServerAdress(){
        return JOptionPane.showInputDialog(frame, "Enter IP Address of the Server:", "Set Server Adress", JOptionPane.QUESTION_MESSAGE);
    }
    public String getName(){
        return JOptionPane.showInputDialog(frame, "Choose a screen name:", "Screen name selection", JOptionPane.PLAIN_MESSAGE);
    }

}
