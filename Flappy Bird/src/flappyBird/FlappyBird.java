package flappyBird;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.Timer;

public class FlappyBird implements ActionListener, MouseListener, KeyListener
{
	public static FlappyBird flappyBird;

	public final int WIDTH = 1200, HEIGHT = 800;

	public Renderer renderer;

	public Rectangle bird;

	public int ticks, yMotion, score; // motion for the bird

	public boolean gameOver, started;

	public ArrayList<Rectangle> columns;

	public Random rand;


	public FlappyBird() 
	{
		JFrame jfram = new JFrame();
		Timer timer = new Timer(20, this);

		renderer =  new Renderer();
		this.rand = new Random();

		jfram.add(renderer);
		jfram.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jfram.setSize(WIDTH,HEIGHT);
		jfram.addMouseListener(this);
		jfram.addKeyListener(this);
		jfram.setResizable(false);
		jfram.setVisible(true);
		jfram.setTitle("Flappy Bird");

		this.bird = new Rectangle(this.WIDTH / 2 - 10, this.HEIGHT / 2 - 10, 20, 20);
		this.columns = new ArrayList<Rectangle>();

		addColumn(true);
		addColumn(true);
		addColumn(true);
		addColumn(true);

		timer.start();
	}

	public void jump(){
		if(this.gameOver){
			this.bird = new Rectangle(this.WIDTH / 2 - 10, this.HEIGHT / 2 - 10, 20, 20);
			this.columns.clear();
			this.yMotion = 0;
			this.score = 0;


			addColumn(true);
			addColumn(true);
			addColumn(true);
			addColumn(true);

			this.gameOver = false;
		}

		if(!this.started)
		{
			this.started = true;
		}else if(!this.gameOver){

			if (this.yMotion > 0 )
			{
				this.yMotion = 0;
			}

			this.yMotion = -20;
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		int speed = 10 ;
		this.ticks ++;

		if(this.started){
			for(int i = 0 ; i < this.columns.size(); i++){
				Rectangle column = this.columns.get(i);
				column.x -= speed;
			}

			if(this.ticks % 2 == 0 && this.yMotion < 15){
				this.yMotion+=2;
			}


			for(int i = 0 ; i < this.columns.size(); i++){
				Rectangle column = this.columns.get(i);

				if(column.x + column.width < 0)
				{
					this.columns.remove(column);

					if(column.y == 0)
					{
						this.addColumn(false);
					}
				}
			}

			this.bird.y += this.yMotion;

			for(Rectangle column : this.columns){

				if(column.y == 0 && this.bird.x + this.bird.width / 2 > column.x + column.width / 2 - 10 
						&& this.bird.x + this.bird.width / 2 < column.x + column.width / 2 + 10)
				{
					this.score++;
				}

				if(column.intersects(this.bird)){
					gameOver = true;

					if(this.bird.x < column.x){
						this.bird.x = column.x - this.bird.width;
					}else {
						if(column.y != 0 ){
							this.bird.y = column.y - this.bird.height;
							
						}else if(this.bird.y < column.height){
							this.bird.y = column.height;
						}
					}
					
					this.bird.x = column.x - this.bird.width;
				}
			}

			if(bird.y > this.HEIGHT - 120 || this.bird.y < 0){
				gameOver = true;
			}
			if(this.bird.y + this.yMotion >= this.HEIGHT - 120){
				this.bird.y = this.HEIGHT - 120 - this.bird.height;
			}

		}
		this.renderer.repaint();

	}

	public void addColumn(boolean start){
		int space = 300;
		int width = 100;
		int height = 50 + this.rand.nextInt(300); // min height = 50 and max height  = 300

		if(start)
		{
			this.columns.add(new Rectangle (this.WIDTH + width + this.columns.size() * 300 , this.HEIGHT - height -  120, width, height)); // create muitlple columns
			this.columns.add(new Rectangle (this.WIDTH + width + (this.columns.size() - 1) * 300, 0, width, this.HEIGHT - height - space));
		}else
		{

			this.columns.add(new Rectangle (this.columns.get(this.columns.size() - 1).x + 600,this.HEIGHT - height -  120, width, height)); // create muitlple columns
			this.columns.add(new Rectangle (this.columns.get(this.columns.size() - 1).x, 0, width, this.HEIGHT - height - space));
		}
	}


	public void paintColumn(Graphics g, Rectangle column){

		g.setColor(Color.green.darker());
		g.fillRect(column.x, column.y, column.width, column.height);
	}


	public void repaint(Graphics g) 
	{
		// the background of the program
		g.setColor(Color.cyan);
		g.fillRect(0, 0, this.WIDTH, this.HEIGHT);

		// the color of the ground
		g.setColor(Color.orange);
		g.fillRect(0, this.HEIGHT - 120, this.WIDTH, 120);

		// the color of the grace
		g.setColor(Color.green);
		g.fillRect(0, this.HEIGHT - 120, this.WIDTH, 20);

		// the color of the bird
		g.setColor(Color.pink);
		g.fillRect(this.bird.x, this.bird.y, bird.width, bird.height);

		for(Rectangle column : this.columns){
			this.paintColumn(g, column);
		}

		g.setColor(Color.red);
		g.setFont(new Font("Arial", 1, 100));

		//		if(!this.gameOver){
		//			g.drawString("Game Over!", 100, this.HEIGHT / 2 - 50);
		//		}

		if(!this.started){
			g.drawString("Click to start!", 75, this.HEIGHT / 2 - 50);
		}

		if(this.gameOver){
			g.drawString("Game Over!", 100, this.HEIGHT / 2 - 50);
			g.drawString("Click to start!", 75, this.HEIGHT / 2 - 200);
		}

		if(!this.gameOver && this.started){
			g.drawString(String.valueOf(this.score), this.WIDTH / 2 - 25, 100);
		}
	}


	public static void main(String[]args)
	{
		flappyBird = new FlappyBird();

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		this.jump();

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_SPACE){
			this.jump();
		}

	}

	@Override
	public void keyTyped(KeyEvent e) {


	}

}
