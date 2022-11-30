package chatting;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Sender extends Thread{
	
	Socket socket;
	DataOutputStream out;
	String name;//메시지 보내는 사람
	
	public Sender(Socket socket, String name) {
		this.socket = socket;
		
		try {
			out = new DataOutputStream(this.socket.getOutputStream());
			this.name = name;
		}catch(Exception e) {
			System.out.println("예외:" + e);
		}
	}
	
	@Override
	public void run() {
		Scanner s = new Scanner(System.in);
		
		try {
			out.writeUTF(name);//메시지 전송
		}catch(IOException e) {
			System.out.println("예외:" + e);
		}
		
		while(out != null) {
			try{
				//메시지를 보내는 사용자명과 메시지를 전송
				out.writeUTF("[" + name +"]" + s.nextLine());
				
			}catch(IOException e) {
				System.out.println("예외:" + e);
			}
		}
	}
}
