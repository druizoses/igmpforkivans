package utils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import NetworkProtocols.IGMP.util.Semaphore;

public class Lan {
	Hashtable<Integer, LanWriter> writers = new Hashtable<Integer, LanWriter>();
	
	
	public void receivedData(DatagramPacket data, int puerto){
		sendData(data, puerto);
		System.out.println("Llegó datagram de "+data.getAddress()+" port: "+data.getPort());
	}
	
	public void addInterface(InetAddress dirLocal, int puertoLocal, InetAddress dirRemoto, int puertoRemoto){
		try {
			DatagramSocket socket = new DatagramSocket(puertoRemoto, dirLocal);
			LanWriter lw = new LanWriter(socket, dirRemoto, puertoLocal);
			LanReader lr = new LanReader(socket, this, puertoRemoto);
			writers.put(puertoRemoto, lw);
			lw.start();
			lr.start();
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Propaga un datagram.
	 * @param data: datagram que llegó.
	 * @param puerto: puerto por el que llegó el datagram.
	 */
	private void sendData(DatagramPacket data, int puerto){
		Enumeration<Integer> e = writers.keys();
		while (e.hasMoreElements()) {
			Integer i = (Integer) e.nextElement();
			if (i.intValue() != puerto){
				LanWriter lw = writers.get(i);
				lw.send(data);
			}
		}
	}
}

class LanReader extends Thread{

	DatagramSocket socket;
	private Lan myLan;
	private boolean terminar;
	int puerto;
	
	public LanReader(DatagramSocket socket, Lan lan, int puerto){
		this.socket = socket;
		this.myLan = lan;
		this.puerto = puerto;
	}
	
	public void run() {
		while (!terminar){
			DatagramPacket data = new DatagramPacket(new byte[20000],20000);
			try {
				socket.receive(data);
			} catch (IOException e) {
				e.printStackTrace();
			}
			myLan.receivedData(data, puerto);
		}
	}
	
	public void terminar(){
		terminar = true;
	}
}

class LanWriter extends Thread{
	ArrayList<DatagramPacket> buff = new ArrayList<DatagramPacket>();
	Semaphore mutex, hayTarea;
	private boolean terminar=false;
	DatagramSocket socket = null;
	InetAddress dir;
	int port;
	
	public LanWriter(DatagramSocket socket, InetAddress dir, int port) {
		mutex = new Semaphore(1);
		hayTarea = new Semaphore(0);
		this.socket = socket;
		this.dir = dir;
		this.port = port;
	}
	
	public void send(DatagramPacket data){
		mutex.P();
		buff.add(data);
		hayTarea.V();
		mutex.V();
	}
	
	public void run() {
		while (! terminar){
			hayTarea.P();
			DatagramPacket data = buff.remove(0);
			try {
				socket.send(new DatagramPacket(data.getData(), data.getData().length, dir, port));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void terminar(){
		terminar = true;
		hayTarea.Terminar();
	}
}
