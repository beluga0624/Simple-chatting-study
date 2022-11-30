package chatting;

import java.io.DataInputStream;
import java.net.Socket;

//스레드를 활용하는 방법 2가지
//1. Thread 직접 상속
//2. Runnable 인터페이스를 가지고 구현
public class Receiver extends Thread{
	
	Socket socket; //클라이언트의 TCP기능 수행을 위한 클래스
	DataInputStream in; //기본자료형을 바이트 단위로 입력하는 스트림
	
	public Receiver(Socket socket) {
		this.socket = socket;
		
		try {
			//서버를 통해 전달되는 메시지를 읽는다
			in = new DataInputStream(this.socket.getInputStream());
		}catch(Exception e) {
			System.out.println("예외:" + e);
		}
	}
	
	@Override
	public void run() {
		
		while(in != null) {
			try {
				//문자열을 읽어온다
				System.out.println(in.readUTF());
			}catch(Exception e) {
				System.out.println("예외:" + e);
			}
		}
	}
}
