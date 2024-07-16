package il.cshaifasweng.OCSFMediatorExample.entities.Controllers;

import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.PurchaseMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.MovieInstance;
import il.cshaifasweng.OCSFMediatorExample.entities.Purchase;

import java.io.IOException;
import java.util.Date;
import java.util.regex.Pattern;

public class MovieTicketPurchasePageController {
    private static SimpleClient client;

    public boolean isCreditCardValid(String creditCard, String year, String month, String cvc) {
        Pattern pattern = Pattern.compile("^\\d{16}$");
        return pattern.matcher(creditCard).matches() && cvc.length() == 3;
    }

    public boolean isIdValid(String id) {
        Pattern pattern = Pattern.compile("^\\d{9}$");
        return pattern.matcher(id).matches();
    }

    public String[] getMovieInstanceInfo(MovieInstance movie_inst) {
        return new String[] {movie_inst.getTitle(), movie_inst.getDirector(), movie_inst.getGenre()};
    }

    public void AddMovieTicketPurchaseToUser(String id) {
        Purchase purchase = new Purchase();
        PurchaseMessage purchaseMessage = new PurchaseMessage(Message.MessageType.REQUEST, PurchaseMessage.RequestType.ADD_PURCHASE, purchase, id);
        try {
            SimpleClient.getClient().sendToServer(purchaseMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean using_multi_entry_ticket(String id, int ticket_amount) {
        return ticket_amount > 1;
    }

    public boolean startTimer(Date start_timer) {
        long currentTime = System.currentTimeMillis();
        return start_timer.getTime() > currentTime;
    }

    public static void main(String[] args) throws IOException {
        client = SimpleClient.getClient();
        client.openConnection();

        //  NEED TO ENTER REAL VALUES FOR THE CREDIT CARD INFORMATION
        MovieTicketPurchasePageController controller = new MovieTicketPurchasePageController();
        boolean isCardValid = controller.isCreditCardValid("1234567812345678", "2023", "12", "123");
        boolean isIdValid = controller.isIdValid("123456789");
        String[] movieInfo = controller.getMovieInstanceInfo(new MovieInstance());
        controller.AddMovieTicketPurchaseToUser("123456789");
        boolean isUsingMultiEntryTicket = controller.using_multi_entry_ticket("123456789", 2);
        boolean isTimerStarted = controller.startTimer(new Date());

        Purchase purchase = new Purchase();
        String customerID = "123456789";
        PurchaseMessage purchaseMessage = new PurchaseMessage(Message.MessageType.REQUEST, PurchaseMessage.RequestType.ADD_PURCHASE, purchase, customerID);
        SimpleClient.getClient().sendToServer(purchaseMessage);
    }
}