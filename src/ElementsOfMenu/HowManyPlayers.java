package ElementsOfMenu;

import Game.GameWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HowManyPlayers extends JFrame implements ActionListener {
    private JPanel howMany;
    private JButton two, three, four, six;
    Color cButtons = new Color(101, 17, 17);

    public HowManyPlayers() {
        super("How many players?");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(3);
        setBounds(200, 200, 300, 500);

        howMany = new JPanel(new GridLayout(4, 1));
        add(howMany);

        two = new JButton("2");
        three = new JButton("3");
        four = new JButton("4");
        six = new JButton("6");

        two.setBackground(cButtons);
        three.setBackground(cButtons);
        four.setBackground(cButtons);
        six.setBackground(cButtons);

        two.setForeground(Color.BLACK);
        three.setForeground(Color.BLACK);
        four.setForeground(Color.BLACK);
        six.setForeground(Color.BLACK);

        two.addActionListener(this);
        three.addActionListener(this);
        four.addActionListener(this);
        six.addActionListener(this);

        howMany.add(two);
        howMany.add(three);
        howMany.add(four);
        howMany.add(six);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source==two){
            //GameWindow nowa=new GameWindow(4,2);
            //nowa.setDefaultCloseOperation(3);
            //getBoardSize("2");
            setVisible(false);

        }else if(source==three){
            GameWindow nowa=new GameWindow(4,3);
            nowa.setDefaultCloseOperation(3);
        }else if(source==four){
            GameWindow nowa=new GameWindow(4,4);
            nowa.setDefaultCloseOperation(3);
        }else if(source==six){
            GameWindow nowa=new GameWindow(4,6);
            nowa.setDefaultCloseOperation(3);
        }
    }
    public String getBoardSize(String size){
        return size;
    }
}