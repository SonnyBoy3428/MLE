import java.awt.Color;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.ImageObserver;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	public static final int	imageWidth		= 360;
	public static final int	imageHeight		= 360;
	public InputOutput		inputOutput		= new InputOutput(this);
	public boolean			stop			= false;
	ImagePanel				canvas			= new ImagePanel();
	ImageObserver			imo				= null;
	Image					renderTarget	= null;
	public int mousex,mousey,mousek;
	public int key;

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

	int xBallMax = 10;
	int yBallMax = 10;
	int xPaddleMax = 10;
	int xVMax = 2;
	int yVMax = 2;

	int amountOfActions = 3;
	double[][] Q = new double[xBallMax*yBallMax*xPaddleMax*xVMax*yVMax][amountOfActions];

	int state = 0;
	int action = 0;
	int reward = 0;

	double learningRate = 0.5;
	double discount = 0.9;

	boolean display = false;
	int counter = 0;
	int maxCounter = 100000;

	int miliseconds = 0;

	public void run() {

		int xBall=5, yBall=6, xPaddle=5, xV=1, yV=1;

		while (!stop) {
			if(display){
				inputOutput.fillRect(0,0,imageWidth-30, imageHeight, Color.black);
				inputOutput.fillRect(xBall*30, yBall*30, 30, 30, Color.green);
				inputOutput.fillRect(xPaddle*30, 11*30+20, 90, 10, Color.orange);
			}

			int newState = getState(xBall, yBall, xPaddle, xV, yV);
			action = qLearning(newState, reward);

			if (action == 2){
				xPaddle++;
			}
			if (action == 1){
				xPaddle--;
			}
			if(action == 0){
				// Do nothing
			}

			if (xPaddle<0){
				xPaddle=0;
			}
			if (xPaddle>10){
				xPaddle=10;
			}
				
			xBall+=xV;
			yBall+=yV;			
			if (xBall>9 || xBall<1){
				xV=-xV;
			}
			if (yBall>10 || yBall<1){
				yV=-yV;
			}
			
			if (yBall==11){
				if (xPaddle==xBall || xPaddle==xBall-1 || xPaddle==xBall-2){
					reward = 1;
					if(display){
						System.out.println("positive reward");
					}

				}else{
					reward = -1;
					if(display){
						System.out.println("negative reward");
					}
				}
			}else{
				reward = 0;
			}
			
			try {
			    Thread.sleep(miliseconds);                 //1000 milliseconds is one second.
			} catch(InterruptedException ex) {
			    Thread.currentThread().interrupt();
			}

			if(counter >= maxCounter){
				display = true;
				miliseconds = 100;
				counter = 0;
			}else{
				counter++;
			}

			if(display){
				repaint();
				validate();
			}
		}

		setVisible(false);
		dispose();
	}

	public int getState(int xBall, int yBall, int xPaddle, int xV, int yV){
		xV += 1;
		yV += 1;

		return (xBall*(1) + yBall*(yBallMax) + xPaddle*(yBallMax*xPaddleMax) + xV*(yBallMax*xPaddleMax*xVMax) + yV*(yBallMax*xPaddleMax*xVMax*yVMax));
	}

	public int qLearning(int newState, int reward){
		int newAction = selectAction(Q[newState]);

		Q[state][action] += learningRate*(reward+discount*Q[newState][newAction] - Q[state][action]);

		state = newState;

		return newAction;
	}

	public int selectAction(double[] Q){
		int newAction = 0;
		Double maxVal = null;

		for(int i = 0; i < Q.length; i++){
			if(maxVal == null){
				maxVal = Q[i];
				newAction = i;
			}else if(Q[i] > maxVal){
				maxVal = Q[i];
				newAction = i;
			}
		}

		return newAction;
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
