package chatting;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

//서버 기능을 수행
public class MultiServer {
	
	//key:value 형태로 되어있고 key는 중복X value는 중복O
	//key, value는 객체형태로 선언
	HashMap clientMap;
	ServerSocket serverSocket = null;
	Socket socket = null;
	
	public MultiServer() {
		clientMap = new HashMap();
		//여러개의 스레드가 하나의 Collection에 접근할 때 발생할 수 있는 예외를 동기화를 통해 해결
		Collections.synchronizedMap(clientMap);
	}
	
	//초기화 작업을 하는 메서드 선언
	public void init() {
		try {
			//9999 포트번호로 서버 시작
			serverSocket = new ServerSocket(9999);
			
			System.out.println("서버가 시작되었습니다.");
			
			//서버와 클라이언트 통신 처리 흐름
			//1. ServerSocket을 이용하여 클라이언트 접속을 위한 준비를 한다.
			//2. 서버에 클라이언트의 접속 요청이 있으면 accept()메서드를 사용하여 요청을 받아 들인다.
			//3. 각 클라이언트에서 보낸 메시지를 다른 클라이언트 들에게 전송
			//4. 서버가 클라이언트에게 메시지 전송
			//5. close문을 수헹하면 통신 종료
			while(true) {
				socket = serverSocket.accept();
				//접속한 IP주소와 접속한 포트번호를 출력
				System.out.println(socket.getInetAddress() + ":" + socket.getPort());
				
				Thread msr = new MultiServerRec(socket);
				msr.start();
			}
		}catch(Exception e) {
			System.out.println("예외:" + e);
		}		
	}
	
	//클라이언트에게 전체 메시지를 보내는 메서드
	public void sendAllMsg(String msg) {
		//keySet() : 접속한 사용자명만 가져옴
		Iterator it = clientMap.keySet().iterator();
		
		while(it.hasNext()) {
			try {
				//사용자의 메시지를 가져와 전송
				DataOutputStream it_out = (DataOutputStream)clientMap.get(it.next());
				it_out.writeUTF(msg);
			} catch(Exception e) {
				System.out.println("예외:" + e);
			}
		}
	}
	
	public static void main(String[] args) {
		MultiServer ms = new MultiServer();
		ms.init();
	}
	
	class MultiServerRec extends Thread{
		Socket socket;
		
		//기본자료형 & 문자열을 바이트 단위로 입출력
		//클라이언트로부터 메시지를 받기 위한 스트림
		DataInputStream in;
		//클라이언트에게 메시지를 보내기 위한 스트림
		DataOutputStream out;
		
		public MultiServerRec(Socket socket) {
			this.socket = socket;
			
			try {
				in = new DataInputStream(this.socket.getInputStream());
				out = new DataOutputStream(this.socket.getOutputStream());
			}catch(Exception e){
				System.out.println("예외:" + e);
			}
		}
		
		@Override
		public void run() {
			String name = "";
			
			try {
				name = in.readUTF();
				
				sendAllMsg(name + "님이 입장하셨습니다.");
				clientMap.put(name, out);
				
				System.out.println("현재 접속자 수는" + clientMap.size() + "명 입니다.");
				
				while(in != null) {
					sendAllMsg(in.readUTF());
				}
			}catch(Exception e) {
				System.out.println(e + "--->");
			}finally {
				clientMap.remove(name);
				sendAllMsg(name + "님이 퇴장하셨습니다.");
				System.out.println("현재 접속자 수는 " + clientMap.size() + "명 입니다.");
			}
		}
		
	}
	
	
}
