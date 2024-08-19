package il.cshaifasweng.OCSFMediatorExample.client.util.generators;

import il.cshaifasweng.OCSFMediatorExample.client.util.constants.ConstantsPath;
import il.cshaifasweng.OCSFMediatorExample.entities.*;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

public class ButtonFactory {

    private static Button createButton(String text, String imagePath, String styleClass) {
        Button button = new Button(text);
        button.setPrefWidth(100);
        if (!imagePath.isEmpty()) {
            ImageView imageView = new ImageView(new Image(ButtonFactory.class.getResourceAsStream(imagePath)));
            imageView.setFitHeight(20);
            imageView.setFitWidth(20);
            button.setGraphic(imageView);
        }
        button.getStyleClass().addAll(styleClass, "table-row-cell");
        return button;
    }

    public static Button createUrgencyButton(long hoursLeft) {
        Button statusButton = new Button(getTextForHoursLeft(hoursLeft));
        statusButton.setStyle(getStyleForHoursLeft(hoursLeft) + "; -fx-text-fill: white;");
        return statusButton;
    }

     private static String getStyleForHoursLeft(long hoursLeft) {
        if (hoursLeft <= 6) {
            return "-fx-background-color: red;";
        } else if (hoursLeft <= 12) {
            return "-fx-background-color: #ffc107;";
        } else {
            return "-fx-background-color: green;";
        }
    }

    private static String getTextForHoursLeft(long hoursLeft) {
        if (hoursLeft <= 6) {
            return "High";
        } else if (hoursLeft <= 12) {
            return "Med";
        } else {
            return "Low";
        }
    }

    public static class ButtonUrgencyCell extends TableCell<Complaint, Button> {
        @Override
        protected void updateItem(Button item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                setGraphic(null);
            } else {
                Complaint complaint = getTableRow().getItem();
                long hoursLeft = 24 - java.time.Duration.between(complaint.getCreationDate(), java.time.LocalDateTime.now()).toHours();
                Button urgencyButton = createUrgencyButton(hoursLeft);
                setGraphic(urgencyButton);
            }
        }
    }

    public static class ButtonUrgencyCellFactory implements Callback<TableColumn<Complaint, Button>, TableCell<Complaint, Button>> {
        @Override
        public TableCell<Complaint, Button> call(TableColumn<Complaint, Button> param) {
            return new ButtonUrgencyCell();
        }
    }

    public static class ButtonTypeOrderCellValueFactory implements Callback<TableColumn.CellDataFeatures<Complaint, Button>, ObservableValue<Button>> {

        @Override
        public ObservableValue<Button> call(TableColumn.CellDataFeatures<Complaint, Button> param) {
            Complaint item = param.getValue();
            String text, imagePath, styleClass;

            if (item.getPurchase() instanceof MultiEntryTicket) {
                text = "Multi-Entry Card";
                imagePath = ConstantsPath.MEDIA_PACKAGE + "icons/icons8-two-tickets.png";
                styleClass = "button-green";
            } else if (item.getPurchase() instanceof MovieTicket) {
                text = "Theater Ticket";
                imagePath = ConstantsPath.MEDIA_PACKAGE + "icons/icons8-movie-theater.png";
                styleClass = "button-blue";
            } else if (item.getPurchase() instanceof HomeViewingPackageInstance) {
                text = "Home Viewing";
                imagePath = ConstantsPath.MEDIA_PACKAGE + "icons/online-movie.png";
                styleClass = "button-purple";
            } else {
                imagePath = ConstantsPath.MEDIA_PACKAGE + "icons/icons8-more-info.png";
                text = "UnClassified";
                styleClass = "button-red";
            }

            return new SimpleObjectProperty<>(createButton(text, imagePath, styleClass));
        }
    }

    public static class ButtonExistsCellValueFactory implements Callback<TableColumn.CellDataFeatures<Complaint, Button>, ObservableValue<Button>> {

        @Override
        public ObservableValue<Button> call(TableColumn.CellDataFeatures<Complaint, Button> param) {
            Complaint item = param.getValue();
            String text = item.isClosed() ? "CLOSE" : "OPEN";
            String styleClass = item.isClosed() ? "button-green" : "button-red";

            return new SimpleObjectProperty<>(createButton(text, "", styleClass));
        }
    }

    public static class ButtonRequestCellValueFactory implements Callback<TableColumn.CellDataFeatures<PriceRequest, Button>, ObservableValue<Button>> {

        @Override
        public ObservableValue<Button> call(TableColumn.CellDataFeatures<PriceRequest, Button> param) {
            return new SimpleObjectProperty<>(createButton("OPEN", "", "button-yes"));
        }
    }

    public static class ButtonTypePurchaseCellValueFactory implements Callback<TableColumn.CellDataFeatures<Purchase, Button>, ObservableValue<Button>> {

        @Override
        public ObservableValue<Button> call(TableColumn.CellDataFeatures<Purchase, Button> param) {
            Purchase item = param.getValue();
            String text, imagePath, styleClass;

            if (item instanceof MultiEntryTicket) {
                text = "Multi-Entry Card";
                imagePath = ConstantsPath.MEDIA_PACKAGE + "icons/icons8-two-tickets.png";
                styleClass = "button-orange";
            } else if (item instanceof MovieTicket) {
                text = "Theater Ticket";
                imagePath = ConstantsPath.MEDIA_PACKAGE + "icons/icons8-movie-theater.png";
                styleClass = "button-blue";
            } else if (item instanceof HomeViewingPackageInstance) {
                text = "Home Viewing";
                imagePath = ConstantsPath.MEDIA_PACKAGE + "icons/online-movie.png";
                styleClass = "button-purple";
            } else {
                imagePath = ConstantsPath.MEDIA_PACKAGE + "icons/icons8-more-info.png";
                text = "Unregistered";
                styleClass = "button-red";
            }


            return new SimpleObjectProperty<>(createButton(text, imagePath, styleClass));
        }
    }

    public static class ButtonStatusCellValueFactory implements Callback<TableColumn.CellDataFeatures<Purchase, Button>, ObservableValue<Button>> {

        @Override
        public ObservableValue<Button> call(TableColumn.CellDataFeatures<Purchase, Button> param) {
            Purchase item = param.getValue();
            String text = item.getPurchaseValidation().equals("REALIZED") ? "Available" : "Available";
            String styleClass = item.getPurchaseValidation().equals("REALIZED") ? "button-red" : "button-green";

            return new SimpleObjectProperty<>(createButton(text, "", styleClass));
        }
    }

    public static class ButtonGenreCellValueFactory implements Callback<TableColumn.CellDataFeatures<Movie, Button>, ObservableValue<Button>> {

        @Override
        public ObservableValue<Button> call(TableColumn.CellDataFeatures<Movie, Button> param) {
            Movie item = param.getValue();
            String text = item.getGenre();
            String imagePath = "";
            String styleClass;

            switch (item.getGenre().toLowerCase()) {
                case "action":
                    imagePath = ConstantsPath.MEDIA_PACKAGE + "icons/genre/action.png";
                    styleClass = "button-green";
                    break;
                case "comedy":
                    imagePath = ConstantsPath.MEDIA_PACKAGE + "icons/genre/comedy.png";
                    styleClass = "button-blue";
                    break;
                case "drama":
                    imagePath = ConstantsPath.MEDIA_PACKAGE + "icons/genre/drama.png";
                    styleClass = "button-purple";
                    break;
                case "horror":
                    imagePath = ConstantsPath.MEDIA_PACKAGE + "icons/genre/horror.png";
                    styleClass = "button-red";
                    break;
                case "romance":
                    imagePath = ConstantsPath.MEDIA_PACKAGE + "icons/genre/romance.png";
                    styleClass = "button-pink";
                    break;
                case "sci-fi":
                    imagePath = ConstantsPath.MEDIA_PACKAGE + "icons/genre/sci-fi.png";
                    styleClass = "button-tomato";
                    break;
                case "thriller":
                    imagePath = ConstantsPath.MEDIA_PACKAGE + "icons/genre/thriller.png";
                    styleClass = "button-green";
                    break;
                case "animation":
                    imagePath = ConstantsPath.MEDIA_PACKAGE + "icons/genre/animation.png";
                    styleClass = "button-orange";
                    break;
                case "fantasy":
                    imagePath = ConstantsPath.MEDIA_PACKAGE + "icons/genre/fantasy.png";
                    styleClass = "button-light-purple";
                    break;
                case "musical":
                    imagePath = ConstantsPath.MEDIA_PACKAGE + "icons/genre/musical.png";
                    styleClass = "button-hot-pink";
                    break;
                default:
                    styleClass = "button-default";
                    break;
            }

            return new SimpleObjectProperty<>(createButton(text, imagePath, styleClass));
        }
    }
    public static class ButtonStatusTicketCellValueFactory implements Callback<TableColumn.CellDataFeatures<Purchase, Button>, ObservableValue<Button>> {

        @Override
        public ObservableValue<Button> call(TableColumn.CellDataFeatures<Purchase, Button> param) {
            Purchase item = param.getValue();
            String text;
            String styleClass;
            String imagePath;

            if (item instanceof MultiEntryTicket) {
                text = "";
                imagePath = "";
                styleClass = "button-gray";
            } else {
                if (item.getIsActive()) {
                    text = "Available";
                    imagePath = ConstantsPath.MEDIA_PACKAGE + "icons/icons8-happy-50.png";
                    styleClass = "button-green";
                } else {
                    text = "Not Available";
                    imagePath = ConstantsPath.MEDIA_PACKAGE + "icons/icons8-sad-50.png";
                    styleClass = "button-red";
                }
            }

            return new SimpleObjectProperty<>(createButton(text, imagePath, styleClass));
        }
    }

    public static class ButtonMovieTypeCellValueFactory implements Callback<TableColumn.CellDataFeatures<Movie, Button>, ObservableValue<Button>> {

        @Override
        public ObservableValue<Button> call(TableColumn.CellDataFeatures<Movie, Button> param) {
            Movie item = param.getValue();
            String text = item.getStreamingType().toString();
            String styleClass;

            switch (item.getStreamingType()) {
                case HOME_VIEWING:
                    styleClass = "button-purple";
                    break;
                case THEATER_VIEWING:
                    styleClass = "button-blue";
                    break;
                case BOTH:
                    styleClass = "button-orange";
                    break;
                default:
                    styleClass = "button-gray";
                    break;
            }

            return new SimpleObjectProperty<>(createButton(text, "", styleClass));
        }
    }
}