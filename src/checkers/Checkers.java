package checkers;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class Checkers extends JPanel implements ActionListener, ItemListener, MouseMotionListener, MouseListener {

    Graphics g;

    JTextArea msg=new JTextArea("Start a new game... Yellow is to move first...");
    ImageIcon redN=new ImageIcon(new ImageIcon(getClass().getResource("/images/red_normal.jpg")).getImage());//red_normal.jpg
    ImageIcon yellowN=new ImageIcon(new ImageIcon(getClass().getResource("/images/yellow_normal.jpg")).getImage());//yellow_normal.jpg
    ImageIcon redK=new ImageIcon(new ImageIcon(getClass().getResource("/images/red_king.jpg")).getImage());//red_king.jpg
    ImageIcon yellowK=new ImageIcon(new ImageIcon(getClass().getResource("/images/yellow_king.jpg")).getImage());//yellow_king.jpg
    ImageIcon hlp=new ImageIcon(new ImageIcon(getClass().getResource("/images/help.jpg")).getImage());//help.jpg
    ImageIcon snp=new ImageIcon(new ImageIcon(getClass().getResource("/images/sound.jpg")).getImage());//sound.jpg
    ImageIcon mup=new ImageIcon(new ImageIcon(getClass().getResource("/images/mute.jpg")).getImage());//mute.jpg

    JButton newGameButton=new JButton("New Game");
    JButton undoButton=new JButton("Undo");
    JButton helpButton=new JButton(hlp);
    JButton soundButton=new JButton(snp);

    ButtonGroup players = new ButtonGroup();
    JRadioButton p1 = new JRadioButton("1-Player", true);
    JRadioButton p2 = new JRadioButton("2-Player", false);

    ButtonGroup colors = new ButtonGroup();
    JRadioButton c1 = new JRadioButton("Red", false);
    JRadioButton c2 = new JRadioButton("Yellow", true);

    Help hp=new Help();

    JLabel modeText=new JLabel("Mode");
    JLabel colorText=new JLabel("Colour");
    JLabel difficultyLevelText=new JLabel("Difficulty Level");
    JLabel redCheckerImage=new JLabel();
    JLabel redCheckerText=new JLabel("Opponent's Piece");
    JLabel yellowCheckerText=new JLabel("Your Piece");
    JLabel yellowCheckerImage=new JLabel();
    JLabel redKingCheckerImage=new JLabel();
    JLabel redKingCheckerText=new JLabel("Opponent's King");
    JLabel yellowKingCheckerText=new JLabel("Your King");
    JLabel yellowKingCheckerImage=new JLabel();

    JComboBox<String> level = new JComboBox<String>();

    String selectedColor;
    int selectedMode;
    int difficulty;

    static final int redNormal = 1;
	static final int yellowNormal = 2;
	static final int redKing = 3;
	static final int yellowKing = 4;
	static final int empty = 0;

    int currType;
    boolean movable;

    int[][] board = new int[8][8];

    int [][] preBoard1= new int[8][8];                 //for undo
    int preToMove1;
    int [][] preBoard2= new int[8][8];
    int preToMove2;
    int [][] preBoard3= new int[8][8];
    int preToMove3;

    int startX,startY,endX,endY;
    boolean incomplete=false;
    boolean highlight=false;

    int toMove =redNormal;
	int loser = empty;

    static boolean silent=false;

    int undoCount;

    int won=0;

    Point winPoint;

    Checkers(){
        setupGUI();
    }

    private void setupGUI(){
        setLayout(null);

        newGameButton.setFocusPainted(false);
        undoButton.setFocusPainted(false);
        undoButton.setEnabled(false);
        c1.setFocusPainted(false);
        c2.setFocusPainted(false);
        p1.setFocusPainted(false);
        p2.setFocusPainted(false);
        helpButton.setFocusPainted(false);
        soundButton.setFocusPainted(false);

        difficultyLevelText.setFont(new Font("SansSerif",Font.PLAIN,11));
        colorText.setFont(new Font("SansSerif",Font.PLAIN,11));
        modeText.setFont(new Font("SansSerif",Font.PLAIN,11));
        c1.setFont(new Font("SansSerif",Font.PLAIN,11));
        c2.setFont(new Font("SansSerif",Font.PLAIN,11));
        p1.setFont(new Font("SansSerif",Font.PLAIN,11));
        p2.setFont(new Font("SansSerif",Font.PLAIN,11));
        newGameButton.setFont(new Font("SansSerif",Font.BOLD,11));
        undoButton.setFont(new Font("SansSerif",Font.BOLD,11));
        helpButton.setFont(new Font("SansSerif",Font.PLAIN,11));
        soundButton.setFont(new Font("SansSerif",Font.PLAIN,11));
        msg.setFont(new Font("SansSerif",Font.PLAIN,11)); 

        newGameButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        undoButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        helpButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        soundButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        newGameButton.addActionListener(this);
        undoButton.addActionListener(this);
        helpButton.addActionListener(this);
        soundButton.addActionListener(this);
        newGameButton.setBounds(405,70,95,25);//297
        this.add(newGameButton);
        undoButton.setBounds(405,100,95,25);
        this.add(undoButton);
        helpButton.setBounds(415,10,25,25);
        this.add(helpButton);
        soundButton.setBounds(460,10,25,25);
        this.add(soundButton);

        modeText.setBounds(420,260,80,25);
        this.add(modeText);
        p1.addActionListener(this);
        p2.addActionListener(this);
        p1.setCursor(new Cursor(Cursor.HAND_CURSOR));
        p2.setCursor(new Cursor(Cursor.HAND_CURSOR));
        players.add(p1);
        players.add(p2);
        p1.setBounds(415,280,80,25);
        p2.setBounds(415,300,80,25);
        this.add(p1);
        this.add(p2);

        colorText.setBounds(420,348,80,25);
        this.add(colorText);
        c1.addActionListener(this);
        c2.addActionListener(this);
        c1.setCursor(new Cursor(Cursor.HAND_CURSOR));
        c2.setCursor(new Cursor(Cursor.HAND_CURSOR));
        colors.add(c1);
        colors.add(c2);
        c1.setBounds(415,368,80,25);
        c2.setBounds(415,388,80,25);
        this.add(c1);
        this.add(c2);

        level.setCursor(new Cursor(Cursor.HAND_CURSOR));
        level.addItemListener(this);
        level.addItem("Easy");
        level.addItem("Fairly Easy");
        level.addItem("Moderate");
        level.addItem("Bit Difficult");
        level.addItem("Tough");
        level.setSelectedIndex(2);
        level.setBounds(400,200,110,25);  // original 415,200,80,25
        this.add(level);

        difficultyLevelText.setCursor(new Cursor(Cursor.HAND_CURSOR));
        difficultyLevelText.setBounds(415,170,100,25);
        this.add(difficultyLevelText);

        this.addMouseListener(this);
        this.addMouseMotionListener(this);

        msg.setBounds(0,405,400,20);
        msg.setEnabled(false);
        this.add(msg);

     // yellow checker and text
        yellowCheckerImage.setBounds(10, 450, 50, 50); // original 110,440,50,50
        yellowCheckerImage.setIcon(yellowN);
        this.add(yellowCheckerImage);
        yellowCheckerText.setBounds(10, 430, 100, 20);  //original 160,450,90,20
        this.add(yellowCheckerText);
        
        // red checker and text
        redCheckerImage.setBounds(110, 450, 50, 50);  // original 10,440,50,50
        redCheckerImage.setIcon(redN);
        this.add(redCheckerImage);
        redCheckerText.setBounds(110, 430, 110, 20); // original 60,450,60,20
        this.add(redCheckerText);

     // yellow king checker and text
        yellowKingCheckerImage.setBounds(250, 450, 50, 50);
        yellowKingCheckerImage.setIcon(yellowK);
        this.add(yellowKingCheckerImage);
        yellowKingCheckerText.setBounds(250, 430, 110, 20);
        this.add(yellowKingCheckerText);
        
        // red king checker and text
        redKingCheckerImage.setBounds(365, 450, 50, 50);  // original 250,440,50,50
        redKingCheckerImage.setIcon(redK);
        this.add(redKingCheckerImage);
        redKingCheckerText.setBounds(365, 430, 110, 20); // original 305,450,60,20
        this.add(redKingCheckerText);

        //g=getGraphics();
        //g.drawImage(redN.getImage(),30,450,this);

    }

    public void paintComponent(Graphics g)	{
		super.paintComponent(g);
        g.setColor(new Color(0,0,0));

        for(int i=0;i<4;i++){
            for(int j=0;j<4;j++){
                g.fillRect(100*j,100*i,50,50);
            }
        }
        for(int i=0;i<4;i++){
            for(int j=0;j<4;j++){
                g.fillRect(50+100*j,50+100*i,50,50);
            }
        }
        g.drawLine(0,400,400,400);
        g.drawLine(400, 0, 400, 400);
        drawCheckers();
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equalsIgnoreCase("1-Player")){
            new PlaySound("src//sounds//option.wav").start();
            colorText.setEnabled(true);
            colorText.setVisible(true);
            difficultyLevelText.setEnabled(true);
            difficultyLevelText.setVisible(true);
            c1.setEnabled(true);
            c1.setVisible(true);
            c2.setEnabled(true);
            c2.setVisible(true);
            level.setEnabled(true);
            level.setVisible(true);
        }
        if(e.getActionCommand().equalsIgnoreCase("2-Player")){
        	 yellowCheckerImage.setIcon(yellowN);
        	 redCheckerImage.setIcon(redN);
        	 yellowKingCheckerImage.setIcon(yellowK);
        	 redKingCheckerImage.setIcon(redK);
            new PlaySound("src//sounds//option.wav").start();
            colorText.setEnabled(false);
            colorText.setVisible(false);
            difficultyLevelText.setEnabled(false);
            difficultyLevelText.setVisible(false);
            c1.setEnabled(false);
            c1.setVisible(false);
            c2.setEnabled(false);
            c2.setVisible(false);
            level.setEnabled(false);
            level.setVisible(false);
            c2.setSelected(true);
        }
        if(e.getActionCommand().equalsIgnoreCase("red")){
        	// set checker images accordingly
        	yellowCheckerImage.setIcon(redN);
        	redCheckerImage.setIcon(yellowN);
        	yellowKingCheckerImage.setIcon(redK);
        	redKingCheckerImage.setIcon(yellowK);
            new PlaySound("src//sounds//option.wav").start();
        }
        if(e.getActionCommand().equalsIgnoreCase("yellow")){
        	 yellowCheckerImage.setIcon(yellowN);
        	 redCheckerImage.setIcon(redN);
        	 yellowKingCheckerImage.setIcon(yellowK);
        	 redKingCheckerImage.setIcon(redK);
            new PlaySound("src//sounds//option.wav").start();
        }
        if(e.getActionCommand().equalsIgnoreCase("New Game")){
            new PlaySound("src//sounds//button.wav").start();
            newGame();
        }
        if(e.getActionCommand().equalsIgnoreCase("Undo") && undoCount>3){
            new PlaySound("src//sounds//button.wav").start();
            undo();
        }
        if(e.getSource()==helpButton){
            new PlaySound("src//sounds//button.wav").start();
            hp.setVisible(true);
        }
        if(e.getSource()==soundButton){
            if(silent){
                soundButton.setIcon(snp);
                silent=false;
                new PlaySound("src//sounds//button.wav").start();
            }
            else{
                soundButton.setIcon(mup);
                silent=true;
            }
        }
    }

    public void newGame()	{                            //creates a new game

        //Yellow takes the first move in both modes
        //If someone wants to move secondly, red has to be selected
        //Yellow is always at the bottom of the board

        selectedColor= c1.isSelected() ? "red" : "yellow";
        selectedMode=p1.isSelected()?1:2;
        difficulty=level.getSelectedIndex();

        undoButton.setEnabled(false);

        won=0;

        undoCount=0;


        highlight = false;
		incomplete = false;

        loser=empty;

        for (int i=0; i<8; i++)                                  //applies values to the board
		{
			for (int j=0; j<8; j++)
				board[i][j] = empty;

			for (int j=0; j<3; j++)
			    if ( isPossibleSquare(i,j) )
				    board[i][j] =  redNormal;

			for (int j=5; j<8; j++)
			    if ( isPossibleSquare(i,j) )
				    board[i][j] =  yellowNormal;
		}

        toMove = yellowNormal;

        for(int i=0;i<8;i++){
            System.arraycopy(board[i],0,preBoard1[i],0,8);                       //for undo
            System.arraycopy(preBoard1[i],0,preBoard2[i],0,8);
            System.arraycopy(preBoard2[i],0,preBoard3[i],0,8);
            preToMove3=preToMove2=preToMove1=toMove;
        }

        if (selectedMode == 1 && selectedColor.equalsIgnoreCase("yellow"))
		{
            this.toMove = redNormal;
            play();
		}
		else if (selectedMode==1 && selectedColor.equalsIgnoreCase("red"))
		{
           this.toMove = redNormal;
            play();
		}

        update(getGraphics());
        drawCheckers();
        showStatus();
    }

    public void drawCheckers(){                   //paint checkers on the board
       g=getGraphics();

        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++){
                if(board[i][j]==redNormal)
                    g.drawImage(redN.getImage(),i*50,j*50,this);
                else if(board[i][j]==yellowNormal)
                    g.drawImage(yellowN.getImage(),i*50,j*50,this);
                else if(board[i][j]==redKing)
                    g.drawImage(redK.getImage(),i*50,j*50,this);
                else if(board[i][j]==yellowKing)
                    g.drawImage(yellowK.getImage(),i*50,j*50,this);
            }
        }
       
    }

    public void undo(){            //undo function
        undoCount=1;
        for(int i=0;i<8;i++){
            System.arraycopy(preBoard3[i],0,board[i],0,8);              //copies previous board
        }
        toMove=preToMove3;
        drawCheckers();
        update(g);

        if(selectedMode==1){
            play();
        }
    }

    public void play()	{

        undoCount++;

        if(undoCount>3){
            if(selectedMode==1 && difficulty!=4)
                undoButton.setEnabled(true);
            else if(selectedMode==2)
                undoButton.setEnabled(true);
        }
        
        for(int i=0;i<8;i++){
            System.arraycopy(preBoard2[i],0,preBoard3[i],0,8);
            System.arraycopy(preBoard1[i],0,preBoard2[i],0,8);
            System.arraycopy(board[i],0,preBoard1[i],0,8);
        }
        preToMove3=preToMove2;
        preToMove2=preToMove1;
        preToMove1=toMove;                                                                                  
        int tempScore;
		int[] result = new int[4];
		int[] counter = new int[1];

		counter[0]=0;
        
		if (this.toMove == yellowNormal && selectedMode==1 && selectedColor.equalsIgnoreCase("yellow"))
		{
			this.toMove = redNormal;
			showStatus();
			tempScore = GameEngine.MinMax(board,0,difficulty+2,result,this.toMove,counter);

			if (result[0] == 0 && result[1] == 0)
				loser = redNormal;
			else
			{
                CheckerMove.moveComputer(board, result);

                if (loser == empty){
                    new PlaySound("src//sounds//comPlay.wav").start();
                    play();
                }
                this.toMove = yellowNormal;
			}
		}

		else if (this.toMove == redNormal && selectedMode==1 && selectedColor.equalsIgnoreCase("red"))
		{
			this.toMove = yellowNormal;
			showStatus();
			tempScore = GameEngine.MinMax(board,0,difficulty+2,result,this.toMove,counter);

			if (result[0] == 0 && result[1] == 0)
				loser = yellowNormal;
			else
			{
                CheckerMove.moveComputer(board, result);
                if (loser == empty){
                    new PlaySound("src//sounds//comPlay.wav").start();
                    play();
                }

				this.toMove = redNormal;
			}
		}
		else
		{
            if (this.toMove == redNormal)
				this.toMove = yellowNormal;
			else
				this.toMove = redNormal;
        }
		if (CheckerMove.noMovesLeft(board,this.toMove))  //
		{
			if (this.toMove == redNormal)
				loser = redNormal;
			else
				loser = yellowNormal;
		}

        showStatus();
	}

    private boolean isPossibleSquare(int i, int j) {
		return (i+j)%2 == 1;
    }

    public void itemStateChanged(ItemEvent e) {          
    }

    public void mousePressed(MouseEvent e) {

        int x=e.getX();
        int y=e.getY();
        int [] square=new int[2];

        if(x>=0 && x<=500 && y<=500 && y>=0)
            square= CheckerMove.getIndex(x,y);
        
        if (toMove == Checkers.redNormal &&	(board[square[0]][square[1]] == Checkers.redNormal ||
		board[square[0]][square[1]] == Checkers.redKing)|| toMove == Checkers.yellowNormal &&
		(board[square[0]][square[1]] == Checkers.yellowNormal || board[square[0]][square[1]] == Checkers.yellowKing))
		{

			// we don't want to lose the incomplete move info:
			// only set new start variables if !incomplete
			if (!incomplete)
			{
				highlight = true;
				startX = square[0];
				startY = square[1];
                update(g);
                g=getGraphics();
                g.setColor(new Color(255,100,30));
                g.fillRect(50*square[0],50*square[1],50,50);                 
                drawCheckers();
                new PlaySound("src//sounds//clickChecker.wav").start();
            }
		}
		else if ( highlight  && (float)(square[0]+square[1]) / 2 != (square[0]+square[1]) / 2)
		{
			endX = square[0];
			endY = square[1];
			int status = CheckerMove.ApplyMove(board,startX,startY,endX,endY);
			switch (status)
			{
			case CheckerMove.legalMove:
				incomplete = false;
				highlight = false;
				play();
                update(g);
                drawCheckers();
                break;
			case CheckerMove.incompleteMove:
				incomplete = true;
				highlight = true;
				// the ending square is now starting square for the next capture
				startX = square[0];
				startY = square[1];
                update(g);
                g=getGraphics();
                g.setColor(new Color(255,100,30));
                g.fillRect(50*square[0],50*square[1],50,50);
                drawCheckers();
                break;
			}
        }
	}

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
    }

    public void mouseMoved(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    private void showStatus() {       //prints msgs to the statuss bar
        if (this.toMove == redNormal){
            msg.setText("Red to move");
        }
        else{
            msg.setText("Yellow to move");
        }

        if (loser == redNormal && won==0){
            msg.setText("Yellow Wins!");
            try {
                Thread.sleep(150);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            new GameWin("Yellow",this.getLocationOnScreen());
            won=1;
            undoCount=0;
            newGame();
        }
        else if (loser == yellowNormal && won==0){
            msg.setText("Red Wins!");
            try {
                Thread.sleep(150);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }            
            new GameWin("Red",this.getLocationOnScreen());
            won=1;
            undoCount=0;
            newGame();            
        }
    }
   // The AWT invokes the update() method in response to the repaint() method
   // calls that are made as a checker is dragged. The default implementation
   // of this method, which is inherited from the Container class, clears the
   // applet's drawing area to the background color prior to calling paint().
   // This clearing followed by drawing causes flicker. CheckerDrag overrides
   // update() to prevent the background from being cleared, which eliminates
   // the flicker.
    @Override
    public void update(Graphics g){                                                                                                     
        paint(g);
    }
}