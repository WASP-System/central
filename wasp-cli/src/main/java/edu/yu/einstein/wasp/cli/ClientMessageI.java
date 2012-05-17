package edu.yu.einstein.wasp.cli;

import java.rmi.RemoteException;

import org.springframework.integration.Message;

public interface ClientMessageI {
	
	public Message process(Message m) throws RemoteException;

}
