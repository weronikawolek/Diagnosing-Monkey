package diagnosingmonkey;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class Main extends JFrame {    
    
    public Main() {        
        Program firstLevel = new Program();          
        add(firstLevel);        
    }
   
    public static void main(String[] args) {        
        Main game = new Main();              
	game.setVisible(true);  
	game.setTitle("Diagnosing Monkey"); 
        game.setSize(465,540);  
        game.setResizable(false);              
	game.setDefaultCloseOperation(EXIT_ON_CLOSE);
	game.setLocationRelativeTo(null);         
    }    
}
