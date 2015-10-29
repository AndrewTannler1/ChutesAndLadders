//Andrew Tannler:Author
//Noah Vermillion
//Ben Davis
package chutes_and_ladders;
import java.io.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import javax.swing.*;
public class Chutes_and_Ladders extends JFrame implements Runnable {
    static final int XBORDER = 20;
    static final int YBORDER = 20;
    static final int YTITLE = 30;
    static final int WINDOW_BORDER = 8;
    static final int WINDOW_WIDTH = 2*(WINDOW_BORDER + XBORDER) + 700;
    static final int WINDOW_HEIGHT = YTITLE + WINDOW_BORDER + 2 * YBORDER + 700;
    boolean animateFirstTime = true;
    int xsize = -1;
    int ysize = -1;
    Image image;
    Graphics2D g;

    final int numRows = 10;
    final int numColumns = 10;
    final int numSpaces=numRows*numColumns;
    Piece board[][];
    final int numPlayers=4;
    int currentRowPlayer1;
    int currentColumnPlayer1;
    int currentRowPlayer2;
    int currentColumnPlayer2;
    int currentRowPlayer3;
    int currentColumnPlayer3;
    int currentRowPlayer4;
    int currentColumnPlayer4;
    
    enum playersTurn
    {
        one,two,three,four
    }
    playersTurn whosTurn;
    int diceNum;
    boolean moveHappening;
    
    static Chutes_and_Ladders frame1;
    public static void main(String[] args) {
        frame1 = new Chutes_and_Ladders();
        frame1.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame1.setVisible(true);
    }
    public Chutes_and_Ladders() {
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.BUTTON1 == e.getButton()) {
                    //left button
                    
                        diceNum=(int)((Math.random()*6)+1);
                        
                        if(whosTurn==playersTurn.one)
                            whosTurn=playersTurn.two;
                        else if(whosTurn==playersTurn.two)
                            whosTurn=playersTurn.three;
                        else if(whosTurn==playersTurn.three)
                            whosTurn=playersTurn.four;
                        else
                            whosTurn=playersTurn.one;
                    
                }
                if (e.BUTTON3 == e.getButton()) {
                    //right button
                }
                repaint();
            }
        });
    addMouseMotionListener(new MouseMotionAdapter() {
      public void mouseDragged(MouseEvent e) {
        repaint();
      }
    });
    addMouseMotionListener(new MouseMotionAdapter() {
      public void mouseMoved(MouseEvent e) {
        repaint();
      }
    });
        addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent e) {
                if (e.VK_RIGHT == e.getKeyCode())
                {
                }
                if (e.VK_LEFT == e.getKeyCode())
                {
                }
                if (e.VK_UP == e.getKeyCode())
                {
                }
                if (e.VK_DOWN == e.getKeyCode())
                {
                }
                 if (e.VK_ESCAPE == e.getKeyCode())
                    reset();
                repaint();
            }
        });
        init();
        start();
    }
    Thread relaxer;
////////////////////////////////////////////////////////////////////////////
    public void init() {
        requestFocus();
    }
////////////////////////////////////////////////////////////////////////////
    public void destroy() {
    }
////////////////////////////////////////////////////////////////////////////
    public void paint(Graphics gOld) {
        if (image == null || xsize != getSize().width || ysize != getSize().height) {
            xsize = getSize().width;
            ysize = getSize().height;
            image = createImage(xsize, ysize);
            g = (Graphics2D) image.getGraphics();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
        }
//fill background
        g.setColor(Color.cyan);
        g.fillRect(0, 0, xsize, ysize);
        int x[] = {getX(0), getX(getWidth2()), getX(getWidth2()), getX(0), getX(0)};
        int y[] = {getY(0), getY(0), getY(getHeight2()), getY(getHeight2()), getY(0)};
//fill border
        g.setColor(Color.white);
        g.fillPolygon(x, y, 4);
// draw border
        g.setColor(Color.red);
        g.drawPolyline(x, y, 5);

        if (animateFirstTime) {
            gOld.drawImage(image, 0, 0, null);
            return;
        }
        g.setColor(Color.red);
//horizontal lines
        for (int zi=1;zi<numRows;zi++)
        {
            g.drawLine(getX(0) ,getY(0)+zi*getHeight2()/numRows ,
            getX(getWidth2()) ,getY(0)+zi*getHeight2()/numRows );
        }
//vertical lines
        for (int zi=1;zi<numColumns;zi++)
        {
            g.drawLine(getX(0)+zi*getWidth2()/numColumns ,getY(0) ,
            getX(0)+zi*getWidth2()/numColumns,getY(getHeight2())  );
        }
        int zcolumn;
        for (int zrow=0,count=numSpaces;zrow<numRows||count>=1;zrow++)
        {
            if(zrow % 2 == 0)
            {
                for (zcolumn=0;zcolumn<numColumns;zcolumn++,count--)
                {
                        g.setFont(new Font("Monospaced",Font.BOLD,20) );
                        g.setColor(Color.BLACK);
                        g.drawString("" + count,getX(0)+zcolumn*getWidth2()/numColumns,getY(0)+zrow*getHeight2()/numRows+15);
                }
            }
            else if(zrow % 2 == 1)
            {
                for (zcolumn=numColumns-1;zcolumn>=0;zcolumn--,count--)
                {
                        g.setFont(new Font("Monospaced",Font.BOLD,20) );
                        g.setColor(Color.BLACK);
                        g.drawString("" + count,getX(0)+zcolumn*getWidth2()/numColumns,getY(0)+zrow*getHeight2()/numRows+15);
                }
            }
        }
        g.setColor(Color.BLACK);
        g.drawString("Player "+whosTurn+"'s turn.",260,45);
        g.drawString("Move "+diceNum+" space(s).",500,45);
        
        
        gOld.drawImage(image, 0, 0, null);
    }
////////////////////////////////////////////////////////////////////////////
// needed for     implement runnable
    public void run() {
        while (true) {
            animate();
            repaint();
            double seconds = 0.03;    //time that 1 frame takes.
            int miliseconds = (int) (1000.0 * seconds);
            try {
                Thread.sleep(miliseconds);
            } catch (InterruptedException e) {
            }
        }
    }
/////////////////////////////////////////////////////////////////////////
    public void reset() {
        board = new Piece[numRows][numColumns];
        currentRowPlayer1=0;
        currentColumnPlayer1=0;
        currentRowPlayer2=0;
        currentColumnPlayer2=0;
        currentRowPlayer3=0;
        currentColumnPlayer3=0;
        currentRowPlayer4=0;
        currentColumnPlayer4=0;
        whosTurn=playersTurn.one;
        diceNum=(int)((Math.random()*6)+1);
        moveHappening = false;
    }
/////////////////////////////////////////////////////////////////////////
    public void animate() {

        if (animateFirstTime) {
            animateFirstTime = false;
            if (xsize != getSize().width || ysize != getSize().height) {
                xsize = getSize().width;
                ysize = getSize().height;
            }

            reset();
        }
        if (moveHappening == true)
        {
            if(whosTurn == playersTurn.one)
            {
                 
            }
            else if(whosTurn == playersTurn.two)
            {
                
            }
            else if(whosTurn == playersTurn.three)
            {
                
            }
            else if(whosTurn == playersTurn.four)
            {
                
            }
            checkSpecialBlocks();
            moveHappening = false;
        }
        
        
    }
////////////////////////////////////////////////////////////////////////////    
    public void checkSpecialBlocks()
    {
        if(whosTurn == playersTurn.one)
        {
        //Ladders
        if(board[currentRowPlayer1][currentColumnPlayer1] == board[numRows-1][0])
          {
              board[currentRowPlayer1][currentColumnPlayer1] = board[6][2];
          }
        //
        else if(board[currentRowPlayer1][currentColumnPlayer1] == board[numRows-1][3])
          {
              board[currentRowPlayer1][currentColumnPlayer1] = board[8][6];
          }
        //
        else if(board[currentRowPlayer1][currentColumnPlayer1] == board[numRows-1][8])
          {
              board[currentRowPlayer1][currentColumnPlayer1] = board[6][numColumns-1];
          }
        //
        else if(board[currentRowPlayer1][currentColumnPlayer1] == board[7][0])
          {
              board[currentRowPlayer1][currentColumnPlayer1] = board[5][1];
          }
        //
        else if(board[currentRowPlayer1][currentColumnPlayer1] == board[7][7])
          {
              board[currentRowPlayer1][currentColumnPlayer1] = board[1][3];
          }
        //
        else if(board[currentRowPlayer1][currentColumnPlayer1] == board[6][4])
          {
              board[currentRowPlayer1][currentColumnPlayer1] = board[5][3];
          }
        //
        else if(board[currentRowPlayer1][currentColumnPlayer1] == board[4][numColumns-1])
          {
              board[currentRowPlayer1][currentColumnPlayer1] = board[3][6];
          }
        //
        else if(board[currentRowPlayer1][currentColumnPlayer1] == board[2][0])
          {
              board[currentRowPlayer1][currentColumnPlayer1] = board[0][0];
          }
        
        else if(board[currentRowPlayer1][currentColumnPlayer1] == board[2][numColumns - 1])
          {
              board[currentRowPlayer1][currentColumnPlayer1] = board[0][numColumns - 1];
          }
        
        //chutes\\
        
        else if(board[currentRowPlayer1][currentColumnPlayer1] == board[0][2])
          {
              board[currentRowPlayer1][currentColumnPlayer1] = board[2][2];
          }
        
        else if(board[currentRowPlayer1][currentColumnPlayer1] == board[0][5])
          {
              board[currentRowPlayer1][currentColumnPlayer1] = board[2][5];
          }
        
        else if(board[currentRowPlayer1][currentColumnPlayer1] == board[0][7])
          {
              board[currentRowPlayer1][currentColumnPlayer1] = board[6][7];
          }
        
        else if(board[currentRowPlayer1][currentColumnPlayer1] == board[1][6])
          {
              board[currentRowPlayer1][currentColumnPlayer1] = board[7][3];
          }
        
        else if(board[currentRowPlayer1][currentColumnPlayer1] == board[3][3])
          {
              board[currentRowPlayer1][currentColumnPlayer1] = board[4][0];
          }
        
        else if(board[currentRowPlayer1][currentColumnPlayer1] == board[4][4])
          {
              board[currentRowPlayer1][currentColumnPlayer1] = board[4][7];
          }
        
        else if(board[currentRowPlayer1][currentColumnPlayer1] == board[5][8])
          {
              board[currentRowPlayer1][currentColumnPlayer1] = board[8][9];
          }
        
        else if(board[currentRowPlayer1][currentColumnPlayer1] == board[5][6])
          {
              board[currentRowPlayer1][currentColumnPlayer1] = board[7][5];
          }
        
        else if(board[currentRowPlayer1][currentColumnPlayer1] == board[8][4])
          {
              board[currentRowPlayer1][currentColumnPlayer1] = board[9][5];
          }
        
        else if(board[currentRowPlayer1][currentColumnPlayer1] == board[3][1])
          {
              board[currentRowPlayer1][currentColumnPlayer1] = board[8][1];
          }
        }
//Player Two//////////////////////////////////////////////////////////////////////        
        if(whosTurn == playersTurn.two)
        {
        //Ladders
        if(board[currentRowPlayer2][currentColumnPlayer2] == board[numRows-1][0])
          {
              board[currentRowPlayer2][currentColumnPlayer2] = board[6][2];
          }
        //
        else if(board[currentRowPlayer2][currentColumnPlayer2] == board[numRows-1][3])
          {
              board[currentRowPlayer2][currentColumnPlayer2] = board[8][6];
          }
        //
        else if(board[currentRowPlayer2][currentColumnPlayer2] == board[numRows-1][8])
          {
              board[currentRowPlayer2][currentColumnPlayer2] = board[6][numColumns-1];
          }
        //
        else if(board[currentRowPlayer2][currentColumnPlayer2] == board[7][0])
          {
              board[currentRowPlayer2][currentColumnPlayer2] = board[5][1];
          }
        //
        else if(board[currentRowPlayer2][currentColumnPlayer2] == board[7][7])
          {
              board[currentRowPlayer2][currentColumnPlayer2] = board[1][3];
          }
        //
        else if(board[currentRowPlayer2][currentColumnPlayer2] == board[6][4])
          {
              board[currentRowPlayer2][currentColumnPlayer2] = board[5][3];
          }
        //
        else if(board[currentRowPlayer2][currentColumnPlayer2] == board[4][numColumns-1])
          {
              board[currentRowPlayer2][currentColumnPlayer2] = board[3][6];
          }
        //
        else if(board[currentRowPlayer2][currentColumnPlayer2] == board[2][0])
          {
              board[currentRowPlayer2][currentColumnPlayer2] = board[0][0];
          }
        
        else if(board[currentRowPlayer2][currentColumnPlayer2] == board[2][numColumns - 1])
          {
              board[currentRowPlayer2][currentColumnPlayer2] = board[0][numColumns - 1];
          }
        
        //chutes\\
        
        else if(board[currentRowPlayer2][currentColumnPlayer2] == board[0][2])
          {
              board[currentRowPlayer2][currentColumnPlayer2] = board[2][2];
          }
        
        else if(board[currentRowPlayer2][currentColumnPlayer2] == board[0][5])
          {
              board[currentRowPlayer2][currentColumnPlayer2] = board[2][5];
          }
        
        else if(board[currentRowPlayer2][currentColumnPlayer2] == board[0][7])
          {
              board[currentRowPlayer2][currentColumnPlayer2] = board[6][7];
          }
        
        else if(board[currentRowPlayer2][currentColumnPlayer2] == board[1][6])
          {
              board[currentRowPlayer2][currentColumnPlayer2] = board[7][3];
          }
        
        else if(board[currentRowPlayer2][currentColumnPlayer2] == board[3][3])
          {
              board[currentRowPlayer2][currentColumnPlayer2] = board[4][0];
          }
        
        else if(board[currentRowPlayer2][currentColumnPlayer2] == board[4][4])
          {
              board[currentRowPlayer2][currentColumnPlayer2] = board[4][7];
          }
        
        else if(board[currentRowPlayer2][currentColumnPlayer2] == board[5][8])
          {
              board[currentRowPlayer2][currentColumnPlayer2] = board[8][9];
          }
        
        else if(board[currentRowPlayer2][currentColumnPlayer2] == board[5][6])
          {
              board[currentRowPlayer2][currentColumnPlayer2] = board[7][5];
          }
        
        else if(board[currentRowPlayer2][currentColumnPlayer2] == board[8][4])
          {
              board[currentRowPlayer2][currentColumnPlayer2] = board[9][5];
          }
        
        else if(board[currentRowPlayer2][currentColumnPlayer2] == board[3][1])
          {
              board[currentRowPlayer2][currentColumnPlayer2] = board[8][1];
          }
        }
//Player three/////////////////////////////////////////////////////////////////////
        if(whosTurn == playersTurn.three)
        {
        //Ladders
        if(board[currentRowPlayer3][currentColumnPlayer3] == board[numRows-1][0])
          {
              board[currentRowPlayer3][currentColumnPlayer3] = board[6][2];
          }
        //
        else if(board[currentRowPlayer3][currentColumnPlayer3] == board[numRows-1][3])
          {
              board[currentRowPlayer3][currentColumnPlayer3] = board[8][6];
          }
        //
        else if(board[currentRowPlayer3][currentColumnPlayer3] == board[numRows-1][8])
          {
              board[currentRowPlayer3][currentColumnPlayer3] = board[6][numColumns-1];
          }
        //
        else if(board[currentRowPlayer3][currentColumnPlayer3] == board[7][0])
          {
              board[currentRowPlayer3][currentColumnPlayer3] = board[5][1];
          }
        //
        else if(board[currentRowPlayer3][currentColumnPlayer3] == board[7][7])
          {
              board[currentRowPlayer3][currentColumnPlayer3] = board[1][3];
          }
        //
        else if(board[currentRowPlayer3][currentColumnPlayer3] == board[6][4])
          {
              board[currentRowPlayer3][currentColumnPlayer3] = board[5][3];
          }
        //
        else if(board[currentRowPlayer3][currentColumnPlayer3] == board[4][numColumns-1])
          {
              board[currentRowPlayer3][currentColumnPlayer3] = board[3][6];
          }
        //
        else if(board[currentRowPlayer3][currentColumnPlayer3] == board[2][0])
          {
              board[currentRowPlayer3][currentColumnPlayer3] = board[0][0];
          }
        
        else if(board[currentRowPlayer3][currentColumnPlayer3] == board[2][numColumns - 1])
          {
              board[currentRowPlayer3][currentColumnPlayer3] = board[0][numColumns - 1];
          }
        
        //chutes\\
        
        else if(board[currentRowPlayer3][currentColumnPlayer3] == board[0][2])
          {
              board[currentRowPlayer3][currentColumnPlayer3] = board[2][2];
          }
        
        else if(board[currentRowPlayer3][currentColumnPlayer3] == board[0][5])
          {
              board[currentRowPlayer3][currentColumnPlayer3] = board[2][5];
          }
        
        else if(board[currentRowPlayer3][currentColumnPlayer3] == board[0][7])
          {
              board[currentRowPlayer3][currentColumnPlayer3] = board[6][7];
          }
        
        else if(board[currentRowPlayer3][currentColumnPlayer3] == board[1][6])
          {
              board[currentRowPlayer3][currentColumnPlayer3] = board[7][3];
          }
        
        else if(board[currentRowPlayer3][currentColumnPlayer3] == board[3][3])
          {
              board[currentRowPlayer3][currentColumnPlayer3] = board[4][0];
          }
        
        else if(board[currentRowPlayer3][currentColumnPlayer3] == board[4][4])
          {
              board[currentRowPlayer3][currentColumnPlayer3] = board[4][7];
          }
        
        else if(board[currentRowPlayer3][currentColumnPlayer3] == board[5][8])
          {
              board[currentRowPlayer3][currentColumnPlayer3] = board[8][9];
          }
        
        else if(board[currentRowPlayer3][currentColumnPlayer3] == board[5][6])
          {
              board[currentRowPlayer3][currentColumnPlayer3] = board[7][5];
          }
        
        else if(board[currentRowPlayer3][currentColumnPlayer3] == board[8][4])
          {
              board[currentRowPlayer3][currentColumnPlayer3] = board[9][5];
          }
        
        else if(board[currentRowPlayer3][currentColumnPlayer3] == board[3][1])
          {
              board[currentRowPlayer3][currentColumnPlayer3] = board[8][1];
          }
        }
//Player four///////////////////////////////////////////////////////////////////////        
        if(whosTurn == playersTurn.four)
        {
        //Ladders
        if(board[currentRowPlayer4][currentColumnPlayer4] == board[numRows-1][0])
          {
              board[currentRowPlayer4][currentColumnPlayer4] = board[6][2];
          }
        //
        else if(board[currentRowPlayer4][currentColumnPlayer4] == board[numRows-1][3])
          {
              board[currentRowPlayer4][currentColumnPlayer4] = board[8][6];
          }
        //
        else if(board[currentRowPlayer4][currentColumnPlayer4] == board[numRows-1][8])
          {
              board[currentRowPlayer4][currentColumnPlayer4] = board[6][numColumns-1];
          }
        //
        else if(board[currentRowPlayer4][currentColumnPlayer4] == board[7][0])
          {
              board[currentRowPlayer4][currentColumnPlayer4] = board[5][1];
          }
        //
        else if(board[currentRowPlayer4][currentColumnPlayer4] == board[7][7])
          {
              board[currentRowPlayer4][currentColumnPlayer4] = board[1][3];
          }
        //
        else if(board[currentRowPlayer4][currentColumnPlayer4] == board[6][4])
          {
              board[currentRowPlayer4][currentColumnPlayer4] = board[5][3];
          }
        //
        else if(board[currentRowPlayer4][currentColumnPlayer4] == board[4][numColumns-1])
          {
              board[currentRowPlayer4][currentColumnPlayer4] = board[3][6];
          }
        //
        else if(board[currentRowPlayer4][currentColumnPlayer4] == board[2][0])
          {
              board[currentRowPlayer4][currentColumnPlayer4] = board[0][0];
          }
        
        else if(board[currentRowPlayer4][currentColumnPlayer4] == board[2][numColumns - 1])
          {
              board[currentRowPlayer4][currentColumnPlayer4] = board[0][numColumns - 1];
          }
        
        //chutes\\
        
        else if(board[currentRowPlayer4][currentColumnPlayer4] == board[0][2])
          {
              board[currentRowPlayer4][currentColumnPlayer4] = board[2][2];
          }
        
        else if(board[currentRowPlayer4][currentColumnPlayer4] == board[0][5])
          {
              board[currentRowPlayer4][currentColumnPlayer4] = board[2][5];
          }
        
        else if(board[currentRowPlayer4][currentColumnPlayer4] == board[0][7])
          {
              board[currentRowPlayer4][currentColumnPlayer4] = board[6][7];
          }
        
        else if(board[currentRowPlayer4][currentColumnPlayer4] == board[1][6])
          {
              board[currentRowPlayer4][currentColumnPlayer4] = board[7][3];
          }
        
        else if(board[currentRowPlayer4][currentColumnPlayer4] == board[3][3])
          {
              board[currentRowPlayer4][currentColumnPlayer4] = board[4][0];
          }
        
        else if(board[currentRowPlayer4][currentColumnPlayer4] == board[4][4])
          {
              board[currentRowPlayer4][currentColumnPlayer4] = board[4][7];
          }
        
        else if(board[currentRowPlayer4][currentColumnPlayer4] == board[5][8])
          {
              board[currentRowPlayer4][currentColumnPlayer4] = board[8][9];
          }
        
        else if(board[currentRowPlayer4][currentColumnPlayer4] == board[5][6])
          {
              board[currentRowPlayer4][currentColumnPlayer4] = board[7][5];
          }
        
        else if(board[currentRowPlayer4][currentColumnPlayer4] == board[8][4])
          {
              board[currentRowPlayer4][currentColumnPlayer4] = board[9][5];
          }
        
        else if(board[currentRowPlayer4][currentColumnPlayer4] == board[3][1])
          {
              board[currentRowPlayer4][currentColumnPlayer4] = board[8][1];
          }
        }
        
        
        
    }
    
////////////////////////////////////////////////////////////////////////////
    public void start() {
        if (relaxer == null) {
            relaxer = new Thread(this);
            relaxer.start();
        }
    }
////////////////////////////////////////////////////////////////////////////
    public void stop() {
        if (relaxer.isAlive()) {
            relaxer.stop();
        }
        relaxer = null;
    }
/////////////////////////////////////////////////////////////////////////
    public int getX(int x) {
        return (x + XBORDER + WINDOW_BORDER);
    }
    public int getY(int y) {
        return (y + YBORDER + YTITLE );
    }
    public int getYNormal(int y) {
        return (-y + YBORDER + YTITLE + getHeight2());
    }
    public int getWidth2() {
        return (xsize - 2 * (XBORDER + WINDOW_BORDER));
    }
    public int getHeight2() {
        return (ysize - 2 * YBORDER - WINDOW_BORDER - YTITLE);
    }
}