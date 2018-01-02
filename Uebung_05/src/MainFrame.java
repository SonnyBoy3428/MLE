import java.awt.Color;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.ImageObserver;
import java.util.Random;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	final static Random random = new Random();
	public static final int	imageWidth		= 360;
	public static final int	imageHeight		= 360;
	public InputOutput		inputOutput		= new InputOutput(this);
	public boolean			stop			= false;
	ImagePanel				canvas			= new ImagePanel();
	ImageObserver			imo				= null;
	Image					renderTarget	= null;
	public int mousex,mousey,mousek;
	public int key;

	public double[][][] Q = new double[12][12][4];

	public MainFrame(String[] args) {
		super("PingPong");

		getContentPane().setSize(imageWidth, imageHeight);
		setSize(imageWidth + 50, imageHeight + 50);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);

		canvas.img = createImage(imageWidth, imageHeight);

		add(canvas);

		run();
	}

	public void run() {

		int xBall=5, yBall=6, xSchlaeger=3, xV=1, yV=1;
		double learnRate = 0.01;
		double discount = 0.5;

		while (!stop) {
			//inputOutput.fillRect(0,0,imageWidth, imageHeight, Color.black);
			//inputOutput.fillRect(xBall*30, yBall*30, 30, 30, Color.green);
			//inputOutput.fillRect(xSchlaeger*90, 11*30+20, 90, 10, Color.orange);
			
			/*double action=(2.0*Math.random()-1.0);
			if (action<-0.3){
				xSchlaeger--;
			}
			if (action>0.3){
				xSchlaeger++;
			}*/

			int reward = 0;
			boolean rewardReceived = false;

			while(!rewardReceived) {
				inputOutput.fillRect(0,0,imageWidth, imageHeight, Color.black);
				inputOutput.fillRect(xBall*30, yBall*30, 30, 30, Color.green);
				inputOutput.fillRect(xSchlaeger*90, 11*30+20, 90, 10, Color.orange);

				double tau = 0.01;
				int action = chooseAction(xBall, yBall, xSchlaeger, tau);

				double oldScore = Q[xBall][yBall][xSchlaeger/3];
				int oldSate = xSchlaeger/3;
				int oldXBall = xBall;
				int oldYBall = yBall;

				xSchlaeger += action;

				double newScore = Q[xBall][yBall][xSchlaeger/3];
				int newState = xSchlaeger/3;

				xBall+=xV;
				yBall+=yV;
				if (xBall>9 || xBall<1){
					xV=-xV;
				}
				if (yBall>10 || yBall<1){
					yV=-yV;
				}

				/*if (xSchlaeger<0){
					xSchlaeger=0;
				}
				if (xSchlaeger>9){
					xSchlaeger=9;
				}*/

				if (yBall == 11) {
					if (xSchlaeger == xBall || xSchlaeger == xBall - 1 || xSchlaeger == xBall - 2) {
						reward++;
						rewardReceived = true;
						//System.out.println("positive reward");
					} else {
						reward--;
						rewardReceived = true;
						//System.out.println("negative reward");
					}
				}

				Q[oldXBall][oldYBall][oldSate] = oldScore + learnRate * (reward + (discount * getMax(Q[xBall][yBall]))-oldScore);

				try {
					Thread.sleep(1);                 //1000 milliseconds is one second.
				} catch(InterruptedException ex) {
					Thread.currentThread().interrupt();
				}

				validate();
				repaint();
			}
		}

		setVisible(false);
		dispose();
	}

	public double getMax(double[] a){
		double max = 0;

		for(int i = 0; i < a.length; i++){
			if(a[i] > max){
				max = a[i];
			}
		}

		return max;
	}

	public int chooseAction(int xBall, int yBall, int xSchlaeger, double tau){
		int result = 0;
		int[] movements = {-3, 0, 3};


		if(xSchlaeger == 0){
			double rightReward = Q[xBall][yBall][(xSchlaeger+3)/3];
			double stayReward = Q[xBall][yBall][xSchlaeger/3];

			double rightWeight = softMax(xBall, yBall, rightReward, tau);
			double stayWeight = softMax(xBall, yBall, stayReward, tau);

			if(rightWeight > stayWeight){
				result = movements[2];
			}else if(rightWeight < stayWeight){
				result = movements[1];
			}else{
				int randomNumber = 1 + random.nextInt(2);
				result = movements[randomNumber];
			}
		} else if(xSchlaeger == 9){
			double leftReward = Q[xBall][yBall][(xSchlaeger-3)/3];
			double stayReward = Q[xBall][yBall][xSchlaeger/3];

			double leftWeight = softMax(xBall, yBall, leftReward, tau);
			double stayWeight = softMax(xBall, yBall, stayReward, tau);

			if(leftWeight > stayWeight){
				result = movements[0];
			}else if(leftWeight < stayWeight){
				result = movements[1];
			}else{
				int randomNumber = random.nextInt(1);
				result = movements[randomNumber];
			}
		} else{
			double rightReward = Q[xBall][yBall][(xSchlaeger+3)/3];
			double leftReward = Q[xBall][yBall][(xSchlaeger-3)/3];
			double stayReward = Q[xBall][yBall][xSchlaeger/3];

			double rightWeight = softMax(xBall, yBall, rightReward, tau);
			double leftWeight = softMax(xBall, yBall, leftReward, tau);
			double stayWeight = softMax(xBall, yBall, stayReward, tau);

			if(leftWeight > rightWeight || leftWeight > stayWeight) {
				result = movements[0];
			}else if (stayWeight > rightWeight || stayWeight > leftWeight) {
				result = movements[1];
			}else if(rightWeight > leftWeight || rightWeight > stayWeight) {
				result = movements[2];
			}else{
				int randomNumber = random.nextInt(2);
				result = movements[randomNumber];
			}
		}

		return result;
	}

	public double softMax(int xBall, int yBall, double actionReward, double tau){
		double numerator = Math.exp(actionReward/tau);
		double denumerator = 0f;

		for(int i = 0; i < Q[xBall][yBall].length; i++) {
			denumerator += Math.exp(Q[xBall][yBall][i] / tau);
		}

		return numerator/denumerator;
	}

	public void mouseReleased(MouseEvent e) {
		mousex = e.getX();
		mousey = e.getY();
		mousek = e.getButton();
	}

	public void mousePressed(MouseEvent e) {
		mousex = e.getX();
		mousey = e.getY();
		mousek = e.getButton();
	}

	public void mouseExited(MouseEvent e) {
		mousex = e.getX();
		mousey = e.getY();
		mousek = e.getButton();
	}

	public void mouseEntered(MouseEvent e) {
		mousex = e.getX();
		mousey = e.getY();
		mousek = e.getButton();
	}

	public void mouseClicked(MouseEvent e) {
		mousex = e.getX();
		mousey = e.getY();
		mousek = e.getButton();
	}

	public void mouseMoved(MouseEvent e) {
		// System.out.println(e.toString());
		mousex = e.getX();
		mousey = e.getY();
		mousek = e.getButton();
	}

	public void mouseDragged(MouseEvent e) {
		mousex = e.getX();
		mousey = e.getY();
		mousek = e.getButton();
	}

	public void keyTyped(KeyEvent e) {
		key = e.getKeyCode();
	}

	public void keyReleased(KeyEvent e) {
		key = e.getKeyCode();
	}

	public void keyPressed(KeyEvent e) {
		System.out.println(e.toString());
	}

	/**
	 * Construct main frame
	 *
	 * @param args
	 *            passed to MainFrame
	 */
	public static void main(String[] args) {
		new MainFrame(args);
	}
}
