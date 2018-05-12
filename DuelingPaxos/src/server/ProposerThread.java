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
	private long Time2= 5000l;//Proposer's N is accepted then sleep time2
	private long Time3= 1000l;//Proposer's N or Value is denied then sleep time3
	private int ClientInput =0;
	private Socket client;
	private DataOutputStream outputstream;
	public static boolean terminated = false;  //if a client is leave
	private boolean flag; //Propose or not
	private int count = 0;
	
	
	public ProposerThread (AcceptorThread a,AcceptorThread b,AcceptorThread c, Socket client, boolean flag) throws IOException {
		this.a=a; 
		this.b=b;
		this.c=c;
		this.client=client;
		this.outputstream = new DataOutputStream(this.client.getOutputStream());
		this.flag = flag;
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
			this.outputstream.writeUTF("Game Start Now!\n"+"Send 1 to Server.");
			this.outputstream.flush();//notify client that game start now
		} catch (IOException e) {
		}

		while(true) {
			if(flag){
				this.ClientInput=1;
				this.ValuetoPropos=this.ClientInput;//Get the new value from client.client
				this.Propos();//Propose new value
				System.out.println("I'm Waitting for client.client's new Income...");
			}

		}
	}
	
	public void Propos() {
		while(true){
			if(count>20){
				return;
			}

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
					count++;
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
					count++;
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
