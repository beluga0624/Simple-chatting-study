package chatting;

import java.net.Socket;
import java.util.Scanner;

public class MultiClient {
	public static void main(String[] args) {
		
		System.out.println("이름을 입력해주세요");
		Scanner s = new Scanner(System.in);
		String s_name = s.next();
		
		try {
			//서버의 주소와 포트번호를 동일하게 맞춰야 정상 접속
			String ServerIP = "192.168.1.6";
			Socket socket = new Socket(ServerIP, 9999);
			System.out.println("서버와 연결 성공");
			
			Thread sender = new Sender(socket, s_name);
			Thread receiver = new Receiver(socket);
			System.out.println("채팅방에 입장하셨습니다.");
			
			//메시지를 주고받는 sender, receiver의 run()메서드가 실행
			sender.start();
			receiver.start();
			
		}catch(Exception e) {
			System.out.println("예외발생[MultiClient class]" + e);
		}
	}
}
