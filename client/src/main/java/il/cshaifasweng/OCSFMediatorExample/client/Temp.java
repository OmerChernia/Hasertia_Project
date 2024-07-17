package il.cshaifasweng.OCSFMediatorExample.client;
import il.cshaifasweng.OCSFMediatorExample.entities.*;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Scanner;

public class Temp {

    private static SimpleClient client;


    public static void main(String[] args) throws IOException {
        client = SimpleClient.getClient();
        client.openConnection();
        Complaint c =new Complaint("new info2222",LocalDateTime.now().minusHours(20),null,false,null);
        LoginMessage loginMessage = new LoginMessage("318111222", Message.MessageType.REQUEST,LoginMessage.RequestType.LOGIN);
        EmployeeLoginMessage employeeLoginMessage = new EmployeeLoginMessage("238947615", Message.MessageType.REQUEST, LoginMessage.RequestType.LOGOUT,"password120");
        ComplaintMessage complaintMessage = new ComplaintMessage(Message.MessageType.REQUEST,ComplaintMessage.RequestType.ADD_COMPLIANT,c);
        ComplaintMessage complaintMessage2 = new ComplaintMessage(Message.MessageType.REQUEST,ComplaintMessage.RequestType.GET_ALL_COMPLIANTS);


        MovieMessage movieMessage = new MovieMessage(Message.MessageType.REQUEST,MovieMessage.RequestType.GET_ALL_MOVIES);

        MovieInstanceMessage movieInstanceMessage = new MovieInstanceMessage(Message.MessageType.REQUEST, MovieInstanceMessage.RequestType.GET_ALL_MOVIE_INSTANCES_BY_THEATER_NAME,"Haifa");
        //SimpleClient.getClient().sendToServer(complaintMessage);
        SimpleClient.getClient().sendToServer(complaintMessage2);
        SimpleClient.getClient().sendToServer(movieMessage);
        SimpleClient.getClient().sendToServer(movieInstanceMessage);



    }
    public static void func1(LoginMessage message)
        {
            System.out.println(message.responseType);
        }
    public static void func2(EmployeeLoginMessage message)
    {
        System.out.println(message.responseType);
    }
    public static void func3(ComplaintMessage message)
    {
        System.out.println(message.responseType);
        for(Complaint comp :  message.compliants)
        {
            System.out.println(comp.toString());
        }
    }
    public static void func4(MovieMessage message)
    {
        System.out.println(message.responseType);
        for(Movie movie : message.movies)
        {
            System.out.println(movie.getId());
        }
    }
    public static void func5(MovieInstanceMessage message)
    {
        System.out.println(message.responseType);
        for(MovieInstance movie : message.movies)
        {
            System.out.println(movie.getHall().getTheater().getLocation());
        }
    }

}
