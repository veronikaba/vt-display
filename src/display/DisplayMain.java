package display;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import org.htw.fiw.vs.IBinder;
import org.htw.fiw.vs.fernseher.IDisplayRemote;
import org.htw.fiw.vs.fernseher.IServicePoint;

public class DisplayMain extends UnicastRemoteObject implements IDisplayRemote,ActionListener{

	private ArrayList<IServicePoint> observers;
	public boolean status;
	public String number;
	
	
	

	static DisplayFrame displayFrame=new DisplayFrame();

	public DisplayMain() throws RemoteException {
		super();
	
		observers = new ArrayList<IServicePoint>();


	}

	public static void main(String[] args) {

		if(args.length == 2){

			try {
				
				//hier stehen lassen!!!
				System.setProperty("java.rmi.server.hostname","141.45.208.212");
				String ip = args[0];
				int port = Integer.parseInt(args[1]);
				String protokoll = "rmi://";
				String url = protokoll + ip + ":" + port + "/binder";
				try {

					DisplayMain displayMain = new DisplayMain();
					displayFrame.on.addActionListener(displayMain);
					displayFrame.off.addActionListener(displayMain);
					Thread displayFrameThread = new Thread(displayFrame);
					displayFrameThread.start();
					System.out.println("Display gestartet");

				
					IBinder binder = (IBinder) Naming.lookup(url);
					binder.bind("Display", displayMain);

					System.out.println("Display-RMI-Binding war erfolgreich");

				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NotBoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} catch (NumberFormatException nfe) {
				System.out.println("Kein gueltiger Port");
			}

		} else {
			System.out.println("Bitte IP-Adresse und Port als Programmparameter uebergeben");
		}
	}


	@Override
	public void register(IServicePoint newObserver) {
		observers.add(newObserver);
		System.out.println("Observer wurde erfolgreich am Display angemeldet!");


	}

	//@Override
	public void unregister(IServicePoint deleteObserver) {
		int observerIndex = observers.indexOf(deleteObserver);
		System.out.println("Observer " + (observerIndex+1) + " deleted");

		observers.remove(observerIndex);

	}

	@Override
	public void notifyObserver() throws RemoteException {
		for(IServicePoint observer : observers){    

			observer.update(status);
			observer.update(number);


		}


	}
    @Override
	public void setStatus(boolean newStatus) throws RemoteException{


		this.status = newStatus;


		this.notifyObserver();
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==displayFrame.on) {
			try {
				this.setStatus(true);
				System.out.println(status);
				
			}catch (Exception exception) {
			}
		}
		if (e.getSource()==displayFrame.off) {
			try {
				this.setStatus(false);
				System.out.println(status);
			}catch (Exception exception) {
			}



		}
	}
}


