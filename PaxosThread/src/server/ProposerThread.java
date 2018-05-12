package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ProposerThread extends Thread {
	public int NumbertoPropos =0;
	public int ValuetoPropos = 1;
	private int Result = 0;
	public AcceptorThread a;
	public AcceptorThread b;
	public AcceptorThread c;
	private long Time2= 50l;
	private long Time3= 1000l;
	private int ClientInput =0;
	private Socket client;
	private DataInputStream inputstream;
	private DataOutputStream outputstream;
	private int inputcount=0;
	public static boolean terminated = false;  //if a client is leave
	
	
	public ProposerThread (AcceptorThread a,AcceptorThread b,AcceptorThread c, Socket client) throws IOException {
		this.a=a; 
		this.b=b;
		this.c=c;
		this.client=client;
		this.inputstream = new DataInputStream(this.client.getInputStream());
		this.outputstream = new DataOutputStream(this.client.getOutputStream());
		
	}
	
	public void GetValue(int result) //Get final result from server.Learner
	{
		this.Result=result;
		try {
			this.outputstream.writeUTF(String.valueOf(this.Result));
			 System.out.println("server.Learner Sends message "+this.Result +" back to client. \n");
			this.outputstream.flush();//Write final result to client.client
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public void run() {
		try {
			this.outputstream.writeUTF("Game Start Now!");
			this.outputstream.flush();//notify client that game start now
		} catch (IOException e) {
		}

		//System.out.println("running");
		while(true) {
			if(terminated==true){
				try {
					this.outputstream.writeUTF("Sorry, one client leaves, game over");
					this.outputstream.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return;
			}
			//System.out.println("cannot stop");
			try {
				String line;
				line=this.inputstream.readUTF();
				if(line!=null) {
					System.out.println("inputstr: "+line);
					if(this.inputcount<5)//Control the number of rounds of game to five
					{
				    this.ClientInput=Integer.valueOf(line);
				    this.ValuetoPropos=this.ClientInput;//Get the new value from client.client
				    this.inputcount++;
			        this.Propos();//Propose new value
					System.out.println("I'm Waitting for client.client's new Income...");
					}
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				System.out.println("Sorry, one client leaves, game over");
				terminated = true;
				return;
			}
		}
	}
	
	public void Propos() {
		while(true)
			{
				this.NumbertoPropos++;//Add current N value by one to propose
				System.out.println("Number to propos: "+this.NumbertoPropos);
				System.out.println("this.a.CurrentAcceptedNumber: "+this.a.CurrentAcceptedNumber);//The current accepted N value
				String [] Resa=this.a.SetN(this.NumbertoPropos).split(" ");//Send new N value to Acceptor one
				String [] Resb=this.b.SetN(this.NumbertoPropos).split(" ");//Send new N value to Acceptor two
				String [] Resc=this.c.SetN(this.NumbertoPropos).split(" ");//Send new N value to Acceptor three
				if(Resa[0].equals("A")&&Resb[0].equals("A")&&Resc[0].equals("A"))//N is accepted 
				{
					System.out.println("I'm accepted.");
				try {
					Thread.sleep(Time2);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String [] VResa=this.a.SetV(this.ValuetoPropos, this.NumbertoPropos).split(" ");//Send value to Acceptor one
				String [] VResb=this.b.SetV(this.ValuetoPropos, this.NumbertoPropos).split(" ");//Send value to Acceptor two
				String [] VResc=this.c.SetV(this.ValuetoPropos, this.NumbertoPropos).split(" ");//Send value to Acceptor three
				
				try {
					Thread.sleep(Time3);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(VResa[0].equals("A")&&VResb[0].equals("A")&&VResc[0].equals("A"))//Value is accepted by all three Acceptors
				{
					//System.out.println("Break from inner while true");
					break;//Only break when value is accepted successfully
				}
				else //Value is not accepted, because the N value of value is smaller than the current accepted N value
				{
					System.out.println("Value denied N is too small, change N to "+this.a.CurrentAcceptedNumber);
					this.NumbertoPropos=this.a.CurrentAcceptedNumber;//Update N value to the current accepted N value and try again
					System.out.println("Value is not accepted.");
					try {
						Thread.sleep(Time3);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
				else//N is not accepted 
				{
					System.out.println("N denied N is too small, change N to "+this.a.CurrentAcceptedNumber);
					this.NumbertoPropos=this.a.CurrentAcceptedNumber;//Update N value to the current accepted N value and try again
					try {
						Thread.sleep(Time3);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
	}
}
