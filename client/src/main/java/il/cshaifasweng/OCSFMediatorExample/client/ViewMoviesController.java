package il.cshaifasweng.OCSFMediatorExample.client;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import il.cshaifasweng.OCSFMediatorExample.client.boundaries.user.MovieInfoBoundary;
import il.cshaifasweng.OCSFMediatorExample.client.boundaries.user.MovieSmallBoundary;
import il.cshaifasweng.OCSFMediatorExample.client.boundaries.user.DialogTicketController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ViewMoviesController implements Initializable {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnLogin;

    @FXML
    private Button btnSignout;

    @FXML
    private Button btnCustomers;

    @FXML
    private Button btnMenus;

    @FXML
    private Button btnOrders;

    @FXML
    private Button btnOverview;

    @FXML
    private Button btnPackages;

    @FXML
    private Button btnExit;

    @FXML
    private GridPane pnItems;

    @FXML
    private Pane pnlCustomer;

    @FXML
    private Pane pnlMenus;

    @FXML
    private Pane pnlOrders;

    @FXML
    private Pane pnlOverview;

    private GridPane originalGridPane;

    @FXML
    private ComboBox<String> cmxCinema;

    @FXML
    private ComboBox<String> cmxMovie;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnSignout.setVisible(false);
        btnCustomers.setVisible(false);
        btnMenus.setVisible(false);
        btnOrders.setVisible(false);
        btnPackages.setVisible(false);

        // Populate ComboBoxes
        cmxCinema.getItems().addAll("Cinema 1", "Cinema 2", "Cinema 3");
        cmxMovie.getItems().addAll("Movie 1", "Movie 2", "Movie 3");

        cmxCinema.setOnAction(this::handleCinemaSelection);
        cmxMovie.setOnAction(this::handleMovieSelection);

        originalGridPane = new GridPane();
        originalGridPane.setHgap(10);
        originalGridPane.setVgap(10);
        originalGridPane.setPadding(new Insets(10));

        int numRows = 2;
        int numCols = 7;

        for (int i = 0; i < numCols; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPercentWidth(100.0 / numCols);
            originalGridPane.getColumnConstraints().add(colConst);
        }
        for (int i = 0; i < numRows; i++) {
            RowConstraints rowConst = new RowConstraints();
            rowConst.setPercentHeight(100.0 / numRows);
            originalGridPane.getRowConstraints().add(rowConst);
        }

        int totalItems = 10;

        for (int i = 0; i < totalItems; i++) {
            int row = i / numCols;
            int col = i % numCols;

            try {
                Node normalItem = FXMLLoader.load(getClass().getResource("MovieSmallImage.fxml"));
                normalItem.setStyle("-fx-pref-width: 100%; -fx-pref-height: 100%;");
                FXMLLoader loader = new FXMLLoader(getClass().getResource("MovieSmall.fxml"));
                Node hoverItem = loader.load();
                hoverItem.setStyle("-fx-pref-width: 100%; -fx-pref-height: 100%;");
                MovieSmallBoundary controller = loader.getController();
//                if (controller != null) {
//                    controller.setParentController(this);
//                }

                final Node currentNormalItem = normalItem;
                final Node currentHoverItem = hoverItem;

                normalItem.setOnMouseEntered(event -> {
                    originalGridPane.getChildren().remove(currentNormalItem);
                    originalGridPane.add(currentHoverItem, col, row);
                });
                hoverItem.setOnMouseExited(event -> {
                    originalGridPane.getChildren().remove(currentHoverItem);
                    originalGridPane.add(currentNormalItem, col, row);
                });

                originalGridPane.add(normalItem, col, row);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        pnItems.getChildren().add(originalGridPane);
    }

    @FXML
    private void handleCinemaSelection(ActionEvent event) {
        // Handle cinema selection logic here
        String selectedCinema = cmxCinema.getSelectionModel().getSelectedItem();
        System.out.println("Selected Cinema: " + selectedCinema);
    }
    @FXML
    private void handleMovieSelection(ActionEvent event) {
        // Handle movie selection logic here
        String selectedMovie = cmxMovie.getSelectionModel().getSelectedItem();
        System.out.println("Selected Movie: " + selectedMovie);
    }

    public void showMovieDetails() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MovieInfo.fxml"));
            Node movieDetails = loader.load();
            MovieInfoBoundary controller = loader.getController();


            VBox vbox = new VBox();
            vbox.setAlignment(Pos.CENTER);
            vbox.getChildren().add(movieDetails);

            StackPane stackPane = new StackPane();
            stackPane.setAlignment(Pos.CENTER);
            stackPane.getChildren().add(vbox);

            stackPane.setPrefSize(pnItems.getWidth(), pnItems.getHeight());

            pnItems.getChildren().clear();
            pnItems.getChildren().add(stackPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showGridPane() {
        pnItems.getChildren().clear();
        pnItems.getChildren().add(originalGridPane);
    }

    @FXML
    public void showTicketsPane() {
        try {
            VBox ticketsPane = new VBox();
            ticketsPane.setSpacing(10);
            ticketsPane.setPadding(new Insets(10));
            ticketsPane.setAlignment(Pos.CENTER); // Align items in the center of VBox

            int numTickets = 5; // Number of tickets to display

            for (int i = 0; i < numTickets; i++) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("dialogTicket.fxml"));
                Node ticketItem = loader.load();

                DialogTicketController controller = loader.getController();
//                if (controller != null) {
//                    controller.setTicketInfo("/images/ticket.png", "Ticket Info " + (i + 1));
//                }

                ticketsPane.getChildren().add(ticketItem);
            }

            // Wrap VBox in a StackPane to center it within pnItems
            StackPane centeredPane = new StackPane();
            centeredPane.setAlignment(Pos.CENTER);
            centeredPane.getChildren().add(ticketsPane);

            pnItems.getChildren().clear();
            pnItems.getChildren().add(centeredPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void handleClicks(ActionEvent actionEvent) {
        if (actionEvent.getSource() == btnCustomers) {
            pnlCustomer.setStyle("-fx-background-color : #1620A1");
            pnlCustomer.toFront();
        }
        if (actionEvent.getSource() == btnMenus) {
            pnlMenus.setStyle("-fx-background-color : #53639F");
            pnlMenus.toFront();
        }
        if (actionEvent.getSource() == btnOverview) {
            showGridPane();
            pnlOverview.setStyle("-fx-background-color : #02030A");
            pnlOverview.toFront();
        }
        if (actionEvent.getSource() == btnOrders) {
             showTicketsPane(); // Call the method to show tickets
         }
        if (actionEvent.getSource() == btnLogin) {
            showLoginScreen();
        }
        if (actionEvent.getSource() == btnSignout) {
            handleSignOut();
        }
        if (actionEvent.getSource() == btnExit) {
            Platform.exit();
        }
    }

    private void showLoginScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("dialogComplaint.fxml"));
            Parent root = loader.load();
            Delete_LogInBoundary controller = loader.getController();
            if (controller != null) {
                controller.setParentController(this);
            }

            Stage loginStage = new Stage();
            loginStage.initModality(Modality.APPLICATION_MODAL);
            loginStage.initStyle(StageStyle.UNDECORATED);  // Set stage style to UNDECORATED
            loginStage.setTitle("Log In");
            loginStage.setScene(new Scene(root));
            loginStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleSuccessfulLogin() {
        btnLogin.setVisible(false);
        btnSignout.setVisible(true);
        btnCustomers.setVisible(true);
        btnMenus.setVisible(true);
        btnOrders.setVisible(true);
        btnPackages.setVisible(true);
        showGridPane();
    }

    private void handleSignOut() {
        btnLogin.setVisible(true);
        btnSignout.setVisible(false);
        btnCustomers.setVisible(false);
        btnMenus.setVisible(false);
        btnOrders.setVisible(false);
        btnPackages.setVisible(false);
    }
}
