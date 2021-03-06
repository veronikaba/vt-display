package display;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.htw.fiw.vs.fernseher.IDisplayRemote;
import org.htw.fiw.vs.fernseher.IServicePoint;

public class DisplayFrame extends JFrame implements Runnable,IDisplayRemote{

	JPanel panel;
	JButton on = new JButton("Fernseher an");
	JButton off = new JButton("Fernseher aus");
	boolean status = false;
	public DisplayFrame displayFrame;
	private ArrayList<IServicePoint> observers;


	public DisplayFrame() {
		super("Display");
		observers = new ArrayList<IServicePoint>();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		panel = new JPanel();
		panel.setBackground(Color.WHITE);


		panel.add(on);
		panel.add(off);


		this.setSize(500, 500);
		this.setVisible(true);
		this.add(panel);
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension screenSize = tk.getScreenSize();
		int screenHeight = screenSize.height;
		int screenWidth = screenSize.width;
		setLocation(screenWidth / 8, screenHeight / 6);
	}

	@Override
	public void run() {

		while(true){
			try {
				Thread.sleep(100);

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void register(IServicePoint newObserver) {
		observers.add(newObserver);

	}

	@Override
	public void unregister(IServicePoint deleteObserver) {
		int observerIndex = observers.indexOf(deleteObserver);
		System.out.println("Observer " + (observerIndex+1) + " deleted");

		observers.remove(observerIndex);

	}

	@Override
	public void notifyObserver() throws RemoteException {
		for(IServicePoint observer : observers){    

			observer.update(status);

		}

	}

	public void setStatus(boolean newStatus) throws RemoteException{
		this.status = newStatus;
		notifyObserver();
	}

}
