package il.cshaifasweng.OCSFMediatorExample.client.dialog;

import il.cshaifasweng.OCSFMediatorExample.client.boundariesCustomer.OrdersController;
import il.cshaifasweng.OCSFMediatorExample.entities.HomeViewingPackageInstance;
import il.cshaifasweng.OCSFMediatorExample.entities.MovieTicket;
import il.cshaifasweng.OCSFMediatorExample.entities.MultiEntryTicket;
import il.cshaifasweng.OCSFMediatorExample.entities.Purchase;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.Locale;

public class DialogTicketController {

    @FXML
    private HBox hboxPurchaseDay;

    @FXML
    private HBox hboxTime;

    @FXML
    private HBox hbxActiveDay;


    @FXML
    private HBox hbxTheater;

    @FXML
    private Label lblActiveDay;

    @FXML
    private Label lblActiveMonth;

    @FXML
    private Label lblPrice;

    @FXML
    private Label lblActiveYear;

    @FXML
    private Label lblCustomer;

    @FXML
    private Label lblHall;

    @FXML
    private Label lblHour;

    @FXML
    private Label lblId;

    @FXML
    private Label lblLink;

    @FXML
    private Label lblMin;

    @FXML
    private Label lblPurchaseDay;

    @FXML
    private Label lblPurchaseMonth;

    @FXML
    private Label lblPurchaseYear;

    @FXML
    private Label lblSeat;

    @FXML
    private Label lblTheater;

    @FXML
    private Label lblTickets;

    @FXML
    private Label lblTitle;


    @FXML
    private Label lblActive;
    @FXML
    private VBox vbxHVP;

    @FXML
    private VBox vbxME;

    private MovieTicket movieTicket;
    private MultiEntryTicket multiEntryTicket;
    private HomeViewingPackageInstance homeViewingPackage;
    private OrdersController ordersController;

    public void setOrdersController(OrdersController ordersController) {
        this.ordersController = ordersController;
    }
    public void setTicketInfo(Purchase purchase) {
        if (purchase != null) {
            setPurchase(purchase);

            if (purchase instanceof MovieTicket) {
                movieTicket = (MovieTicket) purchase;
                setMovieTicket();
            } else if (purchase instanceof MultiEntryTicket) {
                multiEntryTicket = (MultiEntryTicket) purchase;
                setMultiEntryTicket();
            } else if (purchase instanceof HomeViewingPackageInstance) {
                homeViewingPackage = (HomeViewingPackageInstance) purchase;
                setHomeViewingPackage();
            }
        }
    }

    public void setPurchase(Purchase purchase) {
        LocalDate purchaseDate = purchase.getPurchaseDate().toLocalDate();
        if (purchaseDate != null) {
            lblPurchaseDay.setText(String.format("%02d", purchaseDate.getDayOfMonth()));
            lblPurchaseMonth.setText(purchaseDate.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH));
            lblPurchaseYear.setText(String.valueOf(purchaseDate.getYear()));
            lblCustomer.setText(purchase.getOwner().getName());
        }


    }

    public void setMovieTicket() {
        lblHour.setVisible(true);
        lblMin.setVisible(true);
        lblTitle.setText(movieTicket.getMovieInstance().getMovie().getEnglishName()+" | "+movieTicket.getMovieInstance().getMovie().getHebrewName());
        LocalDateTime activeDate = movieTicket.getMovieInstance().getTime();

        lblActiveDay.setText(String.format("%02d",activeDate.getDayOfMonth()));
        lblActiveMonth.setText(activeDate.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH));
        lblActiveYear.setText(String.valueOf(activeDate.getYear()));
        lblHour.setText(String.valueOf(activeDate.getHour()));
        lblMin.setText(String.format("%02d", activeDate.getMinute()));
        lblPrice.setText(String.valueOf(movieTicket.getMovieInstance().getMovie().getTheaterPrice())+"₪");
        lblTheater.setText(movieTicket.getSeat().getHall().getTheater().getLocation());
      //  lblHall.setText(movieTicket.getSeat().getHall().getName().replace("Hall", ""));
        String seat = "row: " + movieTicket.getSeat().getRow() + ", seat: " + movieTicket.getSeat().getCol();
        lblSeat.setText(seat);

        hboxTime.setVisible(true);
        hbxTheater.setVisible(true);
        vbxHVP.setVisible(false);
        vbxME.setVisible(false);
        hboxPurchaseDay.setVisible(true);
        hbxActiveDay.setVisible(true);
        lblActive.setVisible(true);

    }

    public void setMultiEntryTicket() {
        lblTitle.setText("Multi Entry Card");
        lblTickets.setText("Remaining entries: " + multiEntryTicket.getOwner().getTicket_counter());
        lblPrice.setText("");
        hbxTheater.setVisible(false);
        vbxHVP.setVisible(false);
        vbxME.setVisible(true);
        hboxPurchaseDay.setVisible(true);
        hbxActiveDay.setVisible(false);
        hboxTime.setVisible(false);
        lblActive.setVisible(false);

     }

    public void setHomeViewingPackage() {
        lblTitle.setText(homeViewingPackage.getMovie().getEnglishName());
        LocalDateTime activeDate = homeViewingPackage.getViewingDate();
        lblPrice.setText(String.valueOf(homeViewingPackage.getMovie().getHomeViewingPrice())+"₪");

        lblActiveDay.setText(String.valueOf(activeDate.getDayOfMonth()));
        lblActiveMonth.setText(activeDate.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH));
        lblActiveYear.setText(String.valueOf(activeDate.getYear()));

        lblLink.setText("link");

        hbxTheater.setVisible(false);
        vbxHVP.setVisible(true);
        vbxME.setVisible(false);
         hboxPurchaseDay.setVisible(true);
        hbxActiveDay.setVisible(true);
        hboxTime.setVisible(false);
        lblActive.setVisible(true);

    }


    @FXML
    private void closeDialogAddUser() {
        if (ordersController != null) {
            ordersController.closeDialogAddUser();
        }
    }
}
