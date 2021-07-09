package cse3040fp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
	static class User extends Thread {
		Socket socket;
		DataOutputStream out;
		DataInputStream in;
		String userID;
		Scanner input;

		User(Socket socket, String userID, Scanner input) {
			this.socket = socket;
			this.userID = userID;
			this.input = input;
			try {
				in = new DataInputStream(socket.getInputStream());
				out = new DataOutputStream(socket.getOutputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("Connection establishment failed.");
				System.exit(0);
			}
		}

		public void run() {
			String order;
			if (out != null && in != null) {
				System.out.println("Hello " + userID + "!");// user ����
				while (true) {
					System.out.print(userID + ">> ");
					order = input.nextLine().trim();
					if (order.equals("add"))
						dealAdd();
					else if (order.equals("borrow"))
						dealBorrow();
					else if (order.equals("return"))
						dealReturn();
					else if (order.equals("info"))
						dealInfo();
					else if (order.equals("search"))
						dealSearch();
					else
						showMessageForInvalid();
				}
			}
		}

		private void dealAdd() {
			String title;
			String author;
			String answer;// ���� ����
			try {

				System.out.print("add-title> ");
				title = input.nextLine().trim();
				if (title.equals(""))// �ƹ��͵� �Է¾��Ѱ��
					return;
				System.out.print("add-author> ");
				author = input.nextLine().trim();
				if (author.equals(""))// �ƹ��͵� �Է¾��Ѱ��
					return;
				// ����� �Է��� ��� server�� ���� ����.
				out.writeUTF("add");
				out.writeUTF(title);
				out.writeUTF(author);
				answer = in.readUTF();// ������ ��� �޾� ����.
				if (answer.equals("exist"))// ������ exist�̸� �̹� �����ϴ� å
				{
					System.out.println("The book already exists in the list.");
				} else {
					System.out.println("A new book added to the list.");
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
			}

		}

		public void dealBorrow() {
			String title;
			String answer;// ���� ����
			try {
				System.out.print("borrow-title> ");
				title = input.nextLine().trim();
				if (title.equals(""))// �ƹ��͵� �Է¾��Ѱ��
					return;
				out.writeUTF("borrow");
				out.writeUTF(userID);
				out.writeUTF(title);// title ����
				answer = in.readUTF();
				if (answer.equals("not available"))// ���� å�� �ų� ���� �̹� �������.
				{
					System.out.println("The book is not available.");
				} else {
					answer = in.readUTF();
					System.out.println("You borrowed a book. - " + answer);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
			}
		}

		public void dealReturn() {
			String title;
			String answer;// ���� ����
			try {
				System.out.print("return-title> ");
				title = input.nextLine().trim();
				if (title.equals(""))// �ƹ��͵� �Է¾��Ѱ��
					return;
				out.writeUTF("return");
				out.writeUTF(userID);
				out.writeUTF(title);// title ����
				answer = in.readUTF();
				if (answer.equals("not available")) {
					System.out.println("You did not borrow the book.");
					return;
				} else {
					answer = in.readUTF();
					System.out.println("You returned a book. - " + answer);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
			}
		}

		public void dealInfo() {
			String title;
			String answer;// ���� ����
			try {
				out.writeUTF("info");
				out.writeUTF(userID);
				answer = in.readUTF();
				System.out.print(answer);
			} catch (IOException e) {
				// TODO Auto-generated catch block
			}
		}

		public void dealSearch() {
			String search;
			String answer;// ���� ����
			try {
				
				while (true) {
					System.out.print("search-string> ");
					search = input.nextLine();
					if (search.length() == 0 || search.length() > 2)// �ƹ��͵� �Է¹��� �ʰų�, ����� �Է¹��� ���
						break;
					System.out.println("Search string must be longer than 2 characters.");
				}
				if(search.equals(""))
					return;
				out.writeUTF("search");
				out.writeUTF(search);// search ����
				answer = in.readUTF();
				System.out.print(answer);
			} catch (IOException e) {
				// TODO Auto-generated catch block
			}
		}

		public void showMessageForInvalid() {
			System.out.println("[available commands]");
			System.out.println("add: add a new book to the list of books.");
			System.out.println("borrow: borrow a book from the library.");
			System.out.println("return: return a book to the library.");
			System.out.println("info: show list of books I am currently borrowing.");
			System.out.println("search: search for books.");
		}
	}

	public static boolean checkUserId(String userID) {
		boolean flag = true;
		if (userID.equals(""))
			flag = false;
		for (int i = 0; i < userID.length(); i++) {
			if (userID.charAt(i) >= 'a' && userID.charAt(i) <= 'z')// �´�����
			{

			} else if (userID.charAt(i) >= '0' && userID.charAt(i) <= '9')// �´� ����
			{

			} else// Ʋ�� ����
			{
				flag = false;
				break;
			}
		}
		return flag;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String serverIp;
		int portNumber;
		String userID;
		Socket socket;
		Scanner input = new Scanner(System.in);
		if (args.length != 2) {
			System.out.println("Please give the IP address and port number as arguments.");
			System.exit(0);
		}
		serverIp = args[0];
		portNumber = Integer.parseInt(args[1]);
		try {
			socket = new Socket(serverIp, portNumber);

			while (true) {
				System.out.print("Enter userID>> ");
				userID = input.nextLine();
				if (checkUserId(userID))
					break;
				System.out.println("UserID must be a single word with lowercase alphabets and numbers.");
			}
			Thread user = new Thread(new User(socket, userID, input));
			user.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Connection establishment failed.");
			System.exit(0);
		}
	}

}
