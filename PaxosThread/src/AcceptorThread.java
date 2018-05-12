
public class AcceptorThread extends Thread {
	public int CurrentAcceptedNumber = 0;
	public int CurrentAcceptedValue =0;
	public boolean flag=false;
	public String name="";//Identify each Acceptor
	public Learner learner;
	private long Time1= 1000l;//Acceptor receive N or new value and sleep for a while to reply to proposer
	private long Time=500l;//Each 0.5s check about whether new value is accepted; if yes, then it to Learner
	
	public synchronized String  SetN (int NumberfromProposer) //Get new N value from proposer
	{
		if(NumberfromProposer>this.CurrentAcceptedNumber)//Acceptor accepts N value
		{
			this.CurrentAcceptedNumber=NumberfromProposer;//Update current accepted N value to new N value
			try {
				Thread.sleep(Time1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}return "A "+this.CurrentAcceptedNumber;//Return result to proposer
		}
		else //Acceptor rejects N value
			{
			try {
				Thread.sleep(Time1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "D "+this.CurrentAcceptedNumber;//Return result to proposer
			}
	}
	
	public synchronized String SetV(int ValuefromProposer,int NumberofValue) //Get new value from proposer
	{
		if(NumberofValue==this.CurrentAcceptedNumber) //Acceptor accepts new value
		{
			this.CurrentAcceptedValue=ValuefromProposer;//Update current accepted value to new value
			this.flag=true;//Mark the new value is accepted
			try {
				Thread.sleep(Time1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "A "+this.CurrentAcceptedNumber+" "+this.CurrentAcceptedValue;//Return result to proposer
		}
		else {
			try {
				Thread.sleep(Time1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "D "+this.CurrentAcceptedNumber+" "+this.CurrentAcceptedValue;//Return result to proposer
		}
	}

	public AcceptorThread (String name)
	{
		this.name=name;
	}

	@Override
	public void run() {
		while(true) {
			if(ProposerThread.terminated==true){
				break;
			}
			while(true) {
				try {
					Thread.sleep(Time);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(this.flag==true) //If new value is accepted, Send the new value to Learner
				{
					System.out.println("Send to Learner "+this.CurrentAcceptedValue);
					if(this.name.equals("a1")) {
						System.out.println(this.name);
						this.learner.setValuea(this.CurrentAcceptedValue);//First acceptor send value to Learner
					}
					if(this.name.equals("b1")) {
						System.out.println(this.name);
						this.learner.setValueb(this.CurrentAcceptedValue);//Second acceptor send value to Learner
					}
					if(this.name.equals("c1")) {
						System.out.println(this.name);
						this.learner.setValuec(this.CurrentAcceptedValue);//Third acceptor send value to Learner
					}
					this.flag=false;

					break;
				}
					// TODO Auto-generated catch block
			}
			System.out.println("Accepter: I'm Listening...");
		}
	}
}
