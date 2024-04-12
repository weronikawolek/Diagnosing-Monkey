package diagnosingmonkey;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;
import javax.swing.Timer;

public class Program extends JPanel implements ActionListener {
    
    // Deklaracja pól
    TextField answer = new TextField();  
    Button submit1 = new Button("Sprawdź");
    Button submit2 = new Button("Sprawdź");
    Button submit3 = new Button("Sprawdź");
       
    private final String correctAnswer1 = "ospa wietrzna";
    private final String correctAnswer2 = "zawał serca";
    private final String correctAnswer3 = "udar mózgu";
         
    private Image menuMonkey;
    private Image menuGhost;
    private Image ghost;
    private Image envelope;
    private Image heart;
    private Image monkey; 
          
    private Dimension size;
    
    private final Font menuFont = new Font("Monospaced", Font.BOLD, 50);
    private final Font titleFont = new Font("Monospaced", Font.BOLD, 20);
    private final Font textFont = new Font("Monospaced", Font.BOLD, 14);
    private final Font ruleFont = new Font("Monospaced", Font.BOLD, 12);
    
    private final Color textColor = new Color(121, 12, 48);
    private final Color frameColor = new Color(224, 111, 149);
    
    private boolean gameOn = false;
    private boolean gameOver = false;
    private boolean level1 = false;
    private boolean level2 = false;  
    private boolean level3 = false; 
    private boolean quiz1On = false; 
    private boolean quiz2On = false;
    private boolean quiz3On = false;
    private boolean finishedGame = false;
    
    private final int blockSize = 30;    
    private final int blocksNumber = 15;
    private final int screenSize = blocksNumber * blockSize;
    private final int ghostsMaxNumber = 12;
    private final int monkeySpeed = 6;
    
    private int ghostsNumber = 6;
    private int lives;
    private int envelopes;
    
    private int[] dx, dy;
    private int[] xGhost, yGhost, dxGhost, dyGhost;
    private int[] ghostSpeed;
    
    private int xMonkey, yMonkey, dxMonkey, dyMonkey;
    private int dxPos, dyPos; 
    
    private final int validSpeeds[] = {1, 2, 3, 4, 6, 8};
    private final int maxSpeed = 6;

    private int currentSpeed = 3;
    private short[] screenData;
    private Timer time;
    
    // Plansza pierwszego poziomu
    private final short level1Data[] = {
        19, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 22,
        17, 16, 16, 16, ' ', 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,
        17, 16, 24, 24, 24, 24, 24, 24, 16, 16, 16, ' ', 16, 16, 20,
        17, 20, 0, 0, 0, 0, 0, 0, 17, 16, 16, 16, 16, 16, 20,
        17, 16, 18, 18, 18, 18, 18, 18, 16, 16, 16, 16, 16, 16, 20,
        17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,
        17, 16, ' ', 16, 16, 16, 16, 16, 24, 24, 16, 16, 16, 16, 20,
        17, 16, 16, 16, 16, 16, 16, 20, 0, 0, 17, 16, 16, ' ', 20,
        17, 16, 16, 16, 16, 16, 16, 20, 0, 0, 17, 16, 16, 16, 20,
        17, 16, 16, 16, 16, 16, 16, 20, 0, 0, 17, 16, 16, 16, 20,
        17, 16, 16, 16, 16, 16, 16, 20, 0, 0, 17, 16, 16, 16, 20,
        17, 16, 16, 16, 16, 16, 16, 20, 0, 0, 17, 16, 16, 16, 20,
        17, 16, 16, 16, 16, 16, ' ', 16, 18, 18, 16, 16, 16, 16, 20,
        17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,
        25, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 28
    };
    
     // Plansza drugiego poziomu
    private final short level2Data[] = {
    	19, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 22,
        17, 16, 16, 16, 16, 16, 16, ' ', 16, 16, 16, 24, 16, 16, 20,
        17, 16, 16, 16, 16, 16, 16, 16, 24, 24, 28, 0, 17, 16, 20,
        17, 16, ' ', 16, 16, 16, 16, 20, 0, 0, 0, 0, 17, 16, 20,
        17, 16, 16, 16, 16, 16, 16, 16, 18, 18, 22, 0, 17, 16, 20,
        17, 16, 24, 24, 24, 24, 16, 16, 16, 16, 16, 18, 16, 16, 20,
        17, 20, 0, 0, 0, 0, 17, 16, 16, 16, 16, ' ', 16, 16, 20,
        17, 20, 0, 19, 18, 18, 16, 16, 16, 16, 16, 16, 16, 16, 20,
        17, 20, 0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,
        17, 16, 18, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,
        17, 16, 16, 16, ' ', 16, 16, 24, 24, 16, 16, 16, 16, 16, 20,
        17, 16, 16, 16, 16, 16, 20, 0, 0, 17, 16, 16, 16, 16, 20,
        17, 16, 16, 16, 16, 16, 20, 0, 0, 17, 16, 16, 16, 16, 20,
        17, ' ', 16, 16, 16, 16, 20, 0, 0, 17, 16, 16, 16, 16, 20,
        25, 24, 24, 24, 24, 24, 28, 8, 8, 25, 24, 24, 24, 24, 28
    };
    
     // Plansza trzeciego poziomu
    private final short level3Data[] = {
    	19, 18, 18, 18, 18, 18, 18, 18, 18, 22, 2, 2, 2, 19, 22,
        17, ' ', 16, 24, 16, 16, 16, 16, 16, 20, 0, 0, 0, 17, 20,
        17, 16, 20, 0, 17, 16, 16, 16, 16, 20, 0, 0, 0, 17, 20,
        17, 24, 28, 0, 25, 24, 16, 16, 16, 16, 18, 18, 18, 16, 20,
        21, 0, 0, 0, 0, 0, 17, 16, 16, 16, ' ', 16, 16, 16, 20,
        17, 18, 22, 0, 19, 18, 16, 16, 16, 16, 16, 16, 16, 16, 20,
        17, 16, 20, 0, 17, 16, 16, 16, 24, 16, 16, 16, 16, 16, 20,
        17, 16, ' ', 18, 16, 16, 16, 20, 0, 17, 16, 16, 16, 16, 20,
        17, 16, 16, 16, 16, 16, 16, 20, 0, 25, 24, 24, 24, ' ', 20,
        17, 16, 16, 16, 16, 16, 16, 20, 0, 0, 0, 0, 0, 17, 20,
        25, 24, 16, 16, 16, 16, 16, 16, 18, 18, 18, 22, 0, 17, 20,
        1, 0, 17, 16, 16, 16, ' ', 16, 16, 16, 16, 20, 0, 17, 20,
        1, 0, 25, 24, 24, 16, 16, 16, 16, 16, 16, 16, 18, 16, 20,
        1, 0, 0, 0, 0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 20,
        9, 8, 8, 8, 8, 9, 24, 24, 24, 24, 24, 24, 24, 24, 28
    };
    
    // Konstruktor
    public Program() {                
        setLayout(null);            
        answer.setBounds(128,370,200,40);
        answer.setForeground(textColor);        
        answer.setFont(titleFont);
        answer.setEditable(true);
        answer.getText();
        answer.setVisible(false); 
        
        submit1.setBounds(128,430,200,40);
        submit1.setForeground(textColor);
        submit1.setFont(titleFont);
        submit1.setVisible(false);
        submit1.addActionListener(this);
        
        submit2.setBounds(128,430,200,40);
        submit2.setForeground(textColor);
        submit2.setFont(titleFont);
        submit2.setVisible(false);
        submit2.addActionListener(this);
        
        submit3.setBounds(128,430,200,40);
        submit3.setForeground(textColor);
        submit3.setFont(titleFont);
        submit3.setVisible(false);
        submit3.addActionListener(this);
                
        add(submit1);
        add(submit2);
        add(submit3);
        add(answer);    
        
        setBackground(Color.white);
        loadImages();
        initVariables();  
        addKeyListener(new TAdapter());
        setFocusable(true);              
    }    
    
    // Wywoływanie obrazów
    private void loadImages() {    
        menuMonkey = new ImageIcon("W:/Java/DiagnosingMonkey/res/monkey.png").getImage();
        menuGhost = new ImageIcon("W:/Java/DiagnosingMonkey/res/ghost.png").getImage();
        monkey = new ImageIcon("W:/Java/DiagnosingMonkey/res/monkey1.png").getImage();
        ghost = new ImageIcon("W:/Java/DiagnosingMonkey/res/ghost1.png").getImage();        
        envelope = new ImageIcon("W:/Java/DiagnosingMonkey/res/envelope1.png").getImage();
        heart = new ImageIcon("W:/Java/DiagnosingMonkey/res/heart2.png").getImage();        
    }
    
    // Inicjacja zmiennych
    private void initVariables() {
        screenData = new short[blocksNumber * blocksNumber];         
        size = new Dimension(500,540);
        xGhost = new int[ghostsMaxNumber];
        dxGhost = new int[ghostsMaxNumber];
        yGhost = new int[ghostsMaxNumber];
        dyGhost = new int[ghostsMaxNumber];
        ghostSpeed = new int[ghostsMaxNumber];
        dx = new int[4];
        dy = new int[4];        
        time = new Timer(30, this);
        time.start();        
    }
    
    // Warunki ogólne gry
    private void startGame(Graphics2D g2d) {        
        if (gameOver) { 
            death();          
        }        
        else if (quiz1On) {   
            showQuiz1(g2d);
            answer.setVisible(true);
            submit1.setVisible(true);            
        }        
        else if (quiz2On) {   
            showQuiz2(g2d);
            answer.setVisible(true);
            submit2.setVisible(true);            
        }
        else if (quiz3On) {   
            showQuiz3(g2d);
            answer.setVisible(true);
            submit3.setVisible(true);            
        }
        else {              
            drawBoard(g2d);
            drawEnvelopes(g2d);
            moveMonkey();
            drawMonkey(g2d);              
            moveGhosts(g2d);            
            nextLevel();
            displayEnding(g2d);
        }                          
    }
    
    // Wywołanie pierwszego quizu
    public void showQuiz1(Graphics2D g2d) {
        setLayout(null);
        g2d.setColor(textColor);
        g2d.fillRect(0, 0, size.width, size.height - 200);        
        // ospa wietrzna
        String p = "Quiz: choroby wewnętrzne - poziom 1";       

        g2d.setColor(Color.white);
        g2d.setFont(titleFont);
        g2d.drawString(p, 10, 50);
        
        String t1 = "a) gorączka";
        String t2 = "b) zmęczenie";
        String t3 = "c) brak apetytu";
        String t4 = "d) wysypka";
        String t5 = "e) ból głowy";      
        
        g2d.setColor(Color.white);
        g2d.setFont(textFont);
        g2d.drawString(t1, 10, 100);
        g2d.drawString(t2, 10, 150);
        g2d.drawString(t3, 10, 200);
        g2d.drawString(t4, 10, 250);
        g2d.drawString(t5, 10, 300);   
    }  
    
    // Wywołanie drugiego quizu
    public void showQuiz2(Graphics2D g2d) {
        g2d.setColor(textColor);
        g2d.fillRect(0, 0, size.width, size.height - 200);        
        // zawał serca
        String p = "Quiz: kardiologia - poziom 2";       

        g2d.setColor(Color.white);
        g2d.setFont(titleFont);
        g2d.drawString(p, 10, 50);
        
        String t1 = "a) nasilający się ból w klatce piersiowej";
        String t2 = "b) duszność";
        String t3 = "c) bladość skóry";
        String t4 = "d) atak paniki";
        String t5 = "e) zimne poty";      
        
        g2d.setColor(Color.white);
        g2d.setFont(textFont);
        g2d.drawString(t1, 10, 100);
        g2d.drawString(t2, 10, 150);
        g2d.drawString(t3, 10, 200);
        g2d.drawString(t4, 10, 250);
        g2d.drawString(t5, 10, 300);        
    }
    
    // Wywołanie trzeciego quizu    
    public void showQuiz3(Graphics2D g2d) {
        g2d.setColor(textColor);
        g2d.fillRect(0, 0, size.width, size.height - 200);        
        //udar mózgu
        String p = "Quiz: neurologia - poziom 3";       

        g2d.setColor(Color.white);
        g2d.setFont(titleFont);
        g2d.drawString(p, 10, 50);
        
        String t1 = "a) nagły i silny ból głowy";
        String t2 = "b) niezrozumiała mowa";
        String t3 = "c) asymetria twarzy";
        String t4 = "d) utrata władzy w kończynach";
        String t5 = "e) zaburzenia widzenia";      
        
        g2d.setColor(Color.white);
        g2d.setFont(textFont);
        g2d.drawString(t1, 10, 100);
        g2d.drawString(t2, 10, 150);
        g2d.drawString(t3, 10, 200);
        g2d.drawString(t4, 10, 250);
        g2d.drawString(t5, 10, 300);       
    }  
      
    // Wywołanie ekranu początkowego 
    public void showMenuScreen (Graphics2D g2d) {
        g2d.setColor(textColor);        
        g2d.fillRect(0, 0, size.width,size.height ); 
        
        String m = "MENU";        
        g2d.setColor(Color.pink);
        g2d.setFont(menuFont);
        g2d.drawString(m, 160,100);
        
        g2d.drawImage(menuGhost, 50, 30,this);
        g2d.drawImage(menuGhost, 330, 30,this);
        
        String z1 = "----------- ZASADY GRY -----------";
        String z2 = "----------------------------------";
        g2d.setColor(Color.white);
        g2d.setFont(titleFont);
        g2d.drawString(z1, 20,180);
        g2d.drawString(z2, 20,276);
        
        String r1 = "Po postawieniu poprawnej diagnozy następuje przejście"; 
        String r2 = "do kolejnego poziomu, a w przypadku błędnego rozpoznania"; 
        String r3 = "poziom należy powtórzyć."; 
        g2d.setFont(ruleFont);        
        g2d.drawString(r1, 30,210);
        g2d.drawString(r2, 20,228);
        g2d.drawString(r3, 130,246);
        
        g2d.drawImage(menuMonkey, 180, 310,this);
        
        String p = "Wciśnij spację aby zagrać";     
        g2d.setColor(Color.pink);
        g2d.setFont(titleFont);
        g2d.drawString(p, 75,420);
        
        String w = "Weronika Wołek 188598 Inżynieria Biomedyczna";     
        g2d.setColor(Color.white);
        g2d.setFont(textFont);
        g2d.drawString(w, 35,480);
    }    
    
    // Wywołanie ekranu końcowego
    public void showFinishScreen (Graphics2D g2d) {
        g2d.setColor(textColor);        
        g2d.fillRect(0, 0, size.width,size.height ); 
        
        String g = "GRATULACJE";        
        g2d.setColor(Color.pink);
        g2d.setFont(menuFont);
        g2d.drawString(g, 80,100);
        
        g2d.drawImage(menuMonkey, 180, 140,this);
        
        String p = "Przeszedłeś/aś całą grę!";     
        g2d.setColor(Color.pink);
        g2d.setFont(titleFont);
        g2d.drawString(p, 80,260);
        
        g2d.drawImage(menuGhost, 45, 320,this);
        g2d.drawImage(menuGhost, 195, 320,this);     
        g2d.drawImage(menuGhost, 340,320,this);     
    }  
    
    // Wypisywanie bieżącej ilości zebranych kopert i żyć małpki    
    public void drawEnvelopes(Graphics2D g) {       
        g.setFont(titleFont);
        g.setColor(new Color(128, 14, 52));
        String s = "Koperty: " + envelopes;
        g.drawString(s, screenSize / 2 + 90, screenSize + 32);

        for (int i = 0; i < lives; i++) {            
            g.drawImage(heart, i * 28 + 15, screenSize + 16, this);
        }      
    } 
   
    // Dekrementacja ilości żyć
    public void death() {          
        lives--;

        if (lives == 0) {
            gameOn = false;
        }
        gameState();
    }  
     
    // Poruszanie się duchów
    public void moveGhosts(Graphics2D g2d) {          
        int pos;
        int count;

        for (int i = 0; i < ghostsNumber; i++) {
            if (xGhost[i] % blockSize == 0 && yGhost[i] % blockSize == 0) {
                pos = xGhost[i] / blockSize + blocksNumber * (int) (yGhost[i] / blockSize);

                count = 0;

                if ((screenData[pos] & 1) == 0 && dxGhost[i] != 1) {
                    dx[count] = -1;
                    dy[count] = 0;
                    count++;
                }
                if ((screenData[pos] & 2) == 0 && dyGhost[i] != 1) {
                    dx[count] = 0;
                    dy[count] = -1;
                    count++;
                }
                if ((screenData[pos] & 4) == 0 && dxGhost[i] != -1) {
                    dx[count] = 1;
                    dy[count] = 0;
                    count++;
                }
                if ((screenData[pos] & 8) == 0 && dyGhost[i] != -1) {
                    dx[count] = 0;
                    dy[count] = 1;
                    count++;
                }

                if (count == 0) {
                    if ((screenData[pos] & 15) == 15) {
                        dxGhost[i] = 0;
                        dyGhost[i] = 0;
                    } else {
                        dxGhost[i] = -dxGhost[i];
                        dyGhost[i] = -dyGhost[i];
                    }

                } else {
                    count = (int) (Math.random() * count);
                    if (count > 3) {
                        count = 3;
                    }
                    dxGhost[i] = dx[count];
                    dyGhost[i] = dy[count];
                }
            }  
            
            xGhost[i] = xGhost[i] + (dxGhost[i] * ghostSpeed[i]);
            yGhost[i] = yGhost[i] + (dyGhost[i] * ghostSpeed[i]);
            drawGhost(g2d, xGhost[i] + 1, yGhost[i] + 1);

            if (xMonkey > (xGhost[i] - 12) && xMonkey < (xGhost[i] + 12)
                    && yMonkey > (yGhost[i] - 12) && yMonkey < (yGhost[i] + 12)
                    && gameOn) {
                gameOver = true;
            }
        }
    }
    
    // Rysowanie duchów na planszy
    public void drawGhost(Graphics2D g2d, int x, int y) {
          g2d.drawImage(ghost, x, y, this);        
    } 
     
    // Poruszanie się małpki
    public void moveMonkey() {
        int pos;
        int ch;
          
           if (xMonkey % blockSize == 0 && yMonkey % blockSize == 0) {
            pos = xMonkey / blockSize + blocksNumber * (int) (yMonkey / blockSize);
            ch = screenData[pos];
            
            if ((ch & ' ') != 0) {
                screenData[pos] = (short) (ch & 15);
                envelopes++;
            }
            
            if (dxPos != 0 || dyPos != 0) {
                if (!((dxPos == -1 && dyPos == 0 && (ch & 1) != 0)
                        || (dxPos == 1 && dyPos == 0 && (ch & 4) != 0)
                        || (dxPos == 0 && dyPos == -1 && (ch & 2) != 0)
                        || (dxPos == 0 && dyPos == 1 && (ch & 8) != 0))) {
                    dxMonkey = dxPos;
                    dyMonkey = dyPos;
                }
            }
            
            if ((dxMonkey == -1 && dyMonkey == 0 && (ch & 1) != 0)
                    || (dxMonkey == 1 && dyMonkey == 0 && (ch & 4) != 0)
                    || (dxMonkey == 0 && dyMonkey == -1 && (ch & 2) != 0)
                    || (dxMonkey == 0 && dyMonkey == 1 && (ch & 8) != 0)) {
                dxMonkey = 0;
                dyMonkey = 0;
            }          
        }
        xMonkey = xMonkey + monkeySpeed * dxMonkey;
        yMonkey = yMonkey + monkeySpeed * dyMonkey;
    }   
    
    // Rysowanie małpki na planszy
    public void drawMonkey (Graphics2D g2d) {        
        g2d.drawImage(monkey, xMonkey, yMonkey, this);
    }
     
    // Rysowanie planszy
    public void drawBoard(Graphics2D g2d) {
        short i = 0;
        
        for (int y = 0; y < screenSize; y += blockSize) {
            for (int x = 0; x < screenSize; x += blockSize) {
            
            g2d.setColor(frameColor);
            g2d.setStroke(new BasicStroke(6));            
                       
            if ((screenData[i] & 1) != 0) { 
                g2d.drawLine(x, y, x, y + blockSize - 1);
            }
            if ((screenData[i] & 2) != 0) { 
                g2d.drawLine(x, y, x + blockSize - 1, y);
            }
            if ((screenData[i] & 4) != 0) { 
                g2d.drawLine(x + blockSize - 1, y, x + blockSize - 1, y + blockSize - 1);
            }
            if ((screenData[i] & 8) != 0) { 
                g2d.drawLine(x, y + blockSize - 1, x + blockSize - 1, y + blockSize - 1);
            }
            if ((screenData[i] & ' ') != 0) { 
                g2d.drawImage(envelope, x, y, null);  
            }    
            i++;
            
            }
        }
    } 
   
    // Warunek przejścia do następnego poziomu
    private void nextLevel() {  
       if (level2 && envelopes == 5) {
           quiz1On = true;
           init2Level();
           level2 = false;
       }
       else if (level3 && envelopes == 5) {        
           quiz2On = true;
           init3Level();
           level3 = false;
        }  
       else if (level1 && envelopes == 5) {        
           quiz3On = true;           
        }  
    }
    
    // Inicjacja pierwszego poziomu
    private void init1Level() {     
        lives = 3;
        envelopes = 0;
        
        for (int i = 0; i < blocksNumber * blocksNumber; i++) {
            screenData[i] = level1Data[i];
        }          
        ghostsNumber = 6; 
        currentSpeed = 3;        
        gameState(); 
        level2 = true;
    }
    
    // Inicjacja drugiego poziomu
    private void init2Level() {        
        lives = 3;
        envelopes = 0;
        
        for (int i = 0; i < blocksNumber * blocksNumber; i++) {
            screenData[i] = level2Data[i];
        } 
        ghostsNumber = 6; 
        currentSpeed = 3;        
        gameState(); 
        level3 = true;
    }
    
    // Inicjacja trzeciego poziomu
    private void init3Level() {
        lives = 3;
        envelopes = 0;
        
        for (int i = 0; i < blocksNumber * blocksNumber; i++) {
            screenData[i] = level3Data[i];
        }       
        ghostsNumber = 6; 
        currentSpeed = 3;       
        gameState();
        
        level1 = true;
    }
    
    // Stan gry
    private void gameState() {
        int dx = 1; 
         
        for (int i = 0; i < ghostsNumber; i++) {
              xGhost[i] = 6 * blockSize;
              yGhost[i] = 6 * blockSize;
              dxGhost[i] = dx;
              dyGhost[i] = 0; 
              dx = -dx;
              
              int random = (int) (Math.random() * (currentSpeed + 1));  
              
              if (random > currentSpeed) {
                random = currentSpeed;
            }              
              ghostSpeed[i] = validSpeeds[random];            
        }
          
        xMonkey = 5 * blockSize;
        yMonkey = 9 * blockSize;
        dxMonkey = 0;
        dyMonkey = 0;  
          
        dxPos = 0;
        dyPos = 0;
        gameOver = false;
    } 
    
    // Wyświetlanie ekranu końcowego
    private void displayEnding(Graphics2D g2d) {
        if (finishedGame) {
            showFinishScreen(g2d);
        }
    }
    
    // Wyświetlenie komponentów graficznych
    public void paintComponent(Graphics g) {
        super.paintComponent(g);          
        Graphics2D g2d = (Graphics2D)g;     
         
        if (gameOn) {            
            startGame(g2d);              
        }
        else {
            showMenuScreen(g2d);
        }          
        Toolkit.getDefaultToolkit().sync();
        g2d.dispose();
    }
    
    // Obsługa zdarzeń sprawdzających odpowiedź w quizie
    @Override
    public void actionPerformed(ActionEvent e) {
               
        if (e.getSource() == submit1) {           

            String userAnswer = answer.getText().toLowerCase(); 

            if (userAnswer.equals(correctAnswer1)) {
                submit1.setVisible(false);                
                answer.setVisible(false);                
                quiz1On = false;
                answer.setText("");
            } 
            else {                  
                submit1.setVisible(false);                
                answer.setVisible(false);   
                quiz1On = false;
                init1Level();
                answer.setText("");
            }         
        }      
        else if (e.getSource() == submit2) {           

            String userAnswer = answer.getText().toLowerCase(); 

            if (userAnswer.equals(correctAnswer2)) {                
                submit2.setVisible(false);                
                answer.setVisible(false);                
                quiz2On = false;
                answer.setText("");
            } 
            else {                 
                submit2.setVisible(false);                
                answer.setVisible(false);   
                quiz2On = false;
                init2Level();
                answer.setText("");
            }         
        }      
        else if (e.getSource() == submit3) {           

            String userAnswer = answer.getText().toLowerCase(); 

            if (userAnswer.equals(correctAnswer3)) { 
                finishedGame = true;
                submit3.setVisible(false);                
                answer.setVisible(false);   
                quiz3On = false;
                answer.setText("");               
            } 
            else {                 
                submit3.setVisible(false);                
                answer.setVisible(false);   
                quiz3On = false;
                init3Level();
                answer.setText("");
            }         
        }      
        repaint();
    }
   
    // Kierowanie ruchem małpki klawiszami strzałek
    class TAdapter extends KeyAdapter {
        public void keyPressed (KeyEvent e) {
            int key = e.getKeyCode();
             
            if (gameOn) {
                if (key == KeyEvent.VK_LEFT) {
                    dxPos = -1;
                    dyPos = 0;
                } else if (key == KeyEvent.VK_RIGHT) {
                    dxPos = 1;
                    dyPos = 0;
                } else if (key == KeyEvent.VK_UP) {
                    dxPos = 0;
                    dyPos = -1;
                } else if (key == KeyEvent.VK_DOWN) {
                    dxPos = 0;
                    dyPos = 1;
                } else if (key == KeyEvent.VK_ESCAPE && time.isRunning()) {
                    gameOn = false;
                } 
            } 
            else {
                if (key == KeyEvent.VK_SPACE) {
                    gameOn = true;                    
                    init1Level(); 
                    level1 = false;
                }
            }
        }       
    }
}
