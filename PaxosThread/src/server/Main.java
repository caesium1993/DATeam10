package server;

import server.AcceptorThread;
import server.Learner;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * This project implements the processes of basic Paxos using multi-threads. We have three Acceptors and one
 * Learner on Server, and allocate a Proposer for every connected Client. Server begins the game until three Clients
 * connect. Each Client have five rounds for the game. No more input will be processed after the fifth round.
 * If any Client leaves during the game, Server terminates the game and notifies the other Clients.
 */

public class Main {

	public static ProposerThread p11=null;
	public static ProposerThread p21=null;
	public static ProposerThread p31=null;
	public static AcceptorThread a1,b1,c1;
	private static int count=0;
	public static Learner learner=new Learner();
	public static void main(String[] args) {
		ServerSocket serverSocket=null;
		int port = 8000;
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//System.out.println("1111111111111111111111");
		}
		// TODO Auto-generated method stub
		//The first proposer
		a1=new AcceptorThread("a1");//Initial first Acceptor
		b1=new AcceptorThread("b1");//Initial second Acceptor
		c1=new AcceptorThread("c1");//Initial third Acceptor

		a1.learner=learner;//Initial server.Learner
		b1.learner=learner;
		c1.learner=learner;

		ScheduledExecutorService scheduledThreadPool=Executors.newScheduledThreadPool(7);//Initial Thread Pool
		scheduledThreadPool.schedule(a1, 0,TimeUnit.SECONDS);//Thread of first Acceptor
		scheduledThreadPool.schedule(b1, 0,TimeUnit.SECONDS);//Thread of second Acceptor
		scheduledThreadPool.schedule(c1, 0,TimeUnit.SECONDS);//Thread of third Acceptor
		scheduledThreadPool.schedule(learner,0,TimeUnit.SECONDS);//Thread of server.Learner
		while(count<3)//Wait for three Clients' connection
		{
			try {
				Socket clientSocket = serverSocket.accept();
				System.out.println("count: "+count);
				if(count==0) {
					p11=new ProposerThread(a1,b1,c1,clientSocket);//Initial proposer for first client
				}
				else if(count==1) {
					p21=new ProposerThread(a1,b1,c1,clientSocket);//Initial proposer for second client
				}
				else if(count==2) {
					p31=new ProposerThread(a1,b1,c1,clientSocket);//Initial proposer for third client
				}
				count++;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		}
		learner.setp1(p11,p21,p31);
		scheduledThreadPool.schedule(p11, 0,TimeUnit.SECONDS);//Thread of first proposer
		scheduledThreadPool.schedule(p21, 0,TimeUnit.SECONDS);//Thread of second proposer
		scheduledThreadPool.schedule(p31, 0,TimeUnit.SECONDS);//Thread of third proposer

	}

}
