
public class Learner extends Thread{
	private int Result=0;
	private int Valuea=0;
	private int Valueb=0;
	private int Valuec=0;
	private boolean flaga=false;
	private boolean flagb=false;
	private boolean flagc=false;
	private ProposerThread p1,p2,p3;
	private long Time1=50l;//Each 0.5s check if new value is sent to me; if yes, send it to proposer
	
	
	public void setValuea(int Valuea) {
		flaga=true;//Mark that the first Acceptor sends value to Learner
		this.Valuea=Valuea;
	}
	public void setValueb(int Valueb) {
		flagb=true;//Mark that the second Acceptor sends value to Learner
		this.Valueb=Valueb;
	}
	public void setValuec(int Valuec) {
		flagc=true;//Mark that the third Acceptor sends value to Learner
		this.Valuec=Valuec;
	}
	
	public void setp1(ProposerThread p1,ProposerThread p2,ProposerThread p3) {
		this.p1=p1;this.p2=p2;this.p3=p3;
	}
	
	@Override
	public void run() {
		
		while (true) {
			if(ProposerThread.terminated==true){
				break;
			}
			while(true) {
				try {
					Thread.sleep(Time1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (flaga == true && flagb == true && flagc == true) // Three Acceptors all send value to Learner
				{
					flaga = false;
					flagb = false;
					flagc = false;
					if (this.Valuea == this.Valueb && this.Valueb == this.Valuec) // Three Acceptors all send the same value to Learner
					{
						this.Result = this.Result + this.Valuea;//Learner update final result based on new value
						p1.GetValue(this.Result);//Sends final result to first proposer
						p2.GetValue(this.Result);//Sends final result to second proposer
						p3.GetValue(this.Result);//Sends final result to third proposer
						System.out.println("Learner will send " + this.Result + " as result.");
						break;
					}

				}
			}
		}
		
	}
}
