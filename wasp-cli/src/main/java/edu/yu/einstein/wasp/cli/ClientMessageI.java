package edu.yu.einstein.wasp.cli;

import java.rmi.RemoteException;

import org.springframework.integration.Message;

public interface ClientMessageI {
	
	public String echo(Message m) throws RemoteException;
	
	public Message process(Message m) throws RemoteException;

}
