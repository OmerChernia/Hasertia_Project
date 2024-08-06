package il.cshaifasweng.OCSFMediatorExample.server;

import java.io.IOException;
import java.util.Scanner;


public class SimpleChatServer
{
	
	private static SimpleServer server;
    public static void main( String[] args ) throws IOException
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the password: ");
        String password = scanner.nextLine();
        server = new SimpleServer(3000,password);
        System.out.println("server is listening");
        server.listen();
    }
}
