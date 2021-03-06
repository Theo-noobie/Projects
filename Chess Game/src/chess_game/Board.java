package chess_game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import chess_game.Player.PlayerColor;
import chess_game.Chess_Pieces;
import chess_game.Chess_Pieces.Type_of_Piece;

public class Board extends JPanel implements ActionListener, MouseListener  {

	private static final long serialVersionUID = 1L;
	
	private Chess_Pieces [][] Board;
	private Tile [][] tiles;
	private int[] CurrentFocusedTile;
	private boolean GameStarted;
	private Player Black_Player, White_Player;
	private String[] BoardLetters;
	private int[] BoardNum;

	//In this constructor, we will implement the mouse listener and set up the pieces
	public Board() {
		setBounds(150, 60, 700, 700);
		setVisible(true);
		addMouseListener(this);
		
		new SpriteRenderer("Chess_Pieces_Spritesheet.png",2,6, getWidth(), getHeight());
		
		Board = new Chess_Pieces[8][8];
		Black_Player=new Player(PlayerColor.BLACK, this);
		White_Player=new Player(PlayerColor.WHITE, this);
		BoardLetters = new String[]{"a","b", "c", "d", "e", "f", "g","h"};
		BoardNum = new int[] {1,2,3,4,5,6,7,8};
		
		CurrentFocusedTile = new int[] {-1,-1};
		
		GameStarted=true;
		tiles = new Tile[8][8];
		for(int i=0; i< tiles.length; i++) {
			for(int j=0; j<tiles[0].length; j++) {
				tiles[i][j]=new Tile((getWidth()/tiles.length)*i, (getHeight()/tiles[0].length)*j, getWidth()/tiles.length, getHeight()/tiles[0].length);
			}
		}
		Timer timer = new Timer(25, this);
		timer.start();
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		for(int i=0; i< tiles.length; i++) {
			for(int j=0; j<tiles[0].length; j++) {
				if((i+j) % 2 ==0) {
					if(tiles[i][j].getFocusState()) {
						g.setColor(new Color(255,255,125));
					} else if(tiles[i][j].getHighLightState()) {
						g.setColor(new Color(255,255,0));
					} else {
						g.setColor(new Color(255,255,204));
					}
				} else {
					if(tiles[i][j].getFocusState()) {
						g.setColor(new Color(25,51,0));
					} else if(tiles[i][j].getHighLightState()) {
						g.setColor(new Color(0,51,0));
					} else {
						g.setColor(new Color(25,100,0));
					}
				}
				g.fillRect((int)tiles[i][j].getX(), (int) tiles[i][j].getY(), (int) tiles[i][j].getWidth() , (int) tiles[i][j].getHeight());
				if(Board[i][j]!= null) {
					g.drawImage(Board[i][j].getSprite(Board[i][j].getColor()), (int) tiles[i][j].getWidth()*Board[i][j].getPosition()[1], (int) tiles[i][j].getHeight()*Board[i][j].getPosition()[0], null);
				} 
			}
		}
		for(int i=0; i<8;i++) {
			g.setFont(new Font("Aerial", 18, 18));
			g.setColor(new Color(218, 103, 83));
			g.drawString(BoardLetters[i], (int)tiles[i][7].getX()+(int)tiles[i][7].getWidth()-11, (int)tiles[i][7].getY()+(int)tiles[i][7].getHeight()-2);
		}
		for(int i=0; i<8;i++) {
			g.setFont(new Font("Aerial", 18, 18));
			g.setColor(new Color(218, 103, 83));
			g.drawString(String.valueOf(BoardNum[i]), (int)tiles[0][i].getX(), (int)tiles[0][i].getY()+16);
		}
	} 

	public int[] getSquare(int Y, int X) {
		int [] Pos = new int[2];
		Pos[0]=(int) Math.floor(X/(getHeight()/tiles.length));
		Pos[1]=(int) Math.floor(Y/(getWidth()/tiles[0].length));
		return Pos;
	}
	
	public void mouseClicked(MouseEvent e) {
	}
	public void mouseEntered(MouseEvent e) {
	}
	public void mouseExited(MouseEvent e) {
	}
	public void mousePressed(MouseEvent e) {
		System.out.println(getSquare(e.getY(),e.getX())[0]+ " "+getSquare(e.getY(),e.getX())[1]);
		tiles[getSquare(e.getY(),e.getX())[0]][getSquare(e.getY(),e.getX())[1]].setFocusState(true);
		try {
			if(Board[CurrentFocusedTile[0]][CurrentFocusedTile[1]]!= null && getPlayer(Board[CurrentFocusedTile[0]][CurrentFocusedTile[1]].getColor()).getTurn() && Board[CurrentFocusedTile[0]][CurrentFocusedTile[1]].isLegalMoves(this, getSquare(e.getY(),e.getX())[0], getSquare(e.getY(),e.getX())[1])) {
				if(Board[getSquare(e.getY(),e.getX())[0]][getSquare(e.getY(),e.getX())[1]] == null) {
					Board[getSquare(e.getY(),e.getX())[0]][getSquare(e.getY(),e.getX())[1]]=Board[CurrentFocusedTile[0]][CurrentFocusedTile[1]];
					Board[getSquare(e.getY(),e.getX())[0]][getSquare(e.getY(),e.getX())[1]].setPosition(getSquare(e.getY(),e.getX())[1], getSquare(e.getY(),e.getX())[0]);
					Board[getSquare(e.getY(),e.getX())[0]][getSquare(e.getY(),e.getX())[1]].setMovedStatus(true);
				} else if(!Board[getSquare(e.getY(),e.getX())[0]][getSquare(e.getY(),e.getX())[1]].isPermanent() && Board[getSquare(e.getY(),e.getX())[0]][getSquare(e.getY(),e.getX())[1]].getColor()!=Board[CurrentFocusedTile[0]][CurrentFocusedTile[1]].getColor()){
					Board[getSquare(e.getY(),e.getX())[0]][getSquare(e.getY(),e.getX())[1]]=Board[CurrentFocusedTile[0]][CurrentFocusedTile[1]];
					Board[getSquare(e.getY(),e.getX())[0]][getSquare(e.getY(),e.getX())[1]].setPosition(getSquare(e.getY(),e.getX())[1], getSquare(e.getY(),e.getX())[0]);
					Board[getSquare(e.getY(),e.getX())[0]][getSquare(e.getY(),e.getX())[1]].setMovedStatus(true);
				}
			
				Board[CurrentFocusedTile[0]][CurrentFocusedTile[1]]=null;
				//checks if a check as been made
				//subject to change
				if(!White_Player.getTurn()) {
					if(White_Player.isChecked(this)) {
					}
					Black_Player.setTurn(false);
					White_Player.setTurn(true);
				} else {
					if(Black_Player.isChecked(this)) {
					}
					Black_Player.setTurn(true);
					White_Player.setTurn(false);
				}
			} 
			tiles[CurrentFocusedTile[0]][CurrentFocusedTile[1]].setFocusState(false);
		} catch (ArrayIndexOutOfBoundsException ex) {
			return;
		} finally {
			CurrentFocusedTile[0]=getSquare(e.getY(),e.getX())[0];
			CurrentFocusedTile[1]=getSquare(e.getY(),e.getX())[1];	
		}
	}
	
	public void mouseReleased(MouseEvent e) {
		
	}
	public Chess_Pieces [][] getBoard(){
		return Board;
	}
	public int[] getCurrentFocusedTile() {
		return CurrentFocusedTile;
	}
	public boolean getGameCondition() {
		return GameStarted;
	}
	public Tile[][] getTileBoard(){
		return tiles;
	}
	public Player getPlayer(PlayerColor color) {
		switch(color) {
		case WHITE:
			return White_Player;
		case BLACK:
			return Black_Player;
		}
		return null;
	}
	public void actionPerformed(ActionEvent arg0) {
		repaint();
	}
}
