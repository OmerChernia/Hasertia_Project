package il.cshaifasweng.OCSFMediatorExample.client.util;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class ButtonFactory {

    private static Button createButton(String text, String imagePath, String styleClass) {
        Button button = new Button(text);
        button.setPrefWidth(100);
        if (!imagePath.isEmpty()) {
            ImageView imageView = new ImageView(new Image(Objects.requireNonNull(ButtonFactory.class.getResourceAsStream(imagePath))));
            imageView.setFitHeight(20);
            imageView.setFitWidth(20);
            button.setGraphic(imageView);
        }
        button.getStyleClass().addAll(styleClass, "table-row-cell");
        return button;
    }

    private static Button createButton(String text, String styleClass) {
        Button button = new Button(text);
        button.setPrefWidth(100);
        button.getStyleClass().addAll(styleClass, "table-row-cell");
        return button;
    }

    public static Button createUrgencyButton(long hoursLeft) {
        Button statusButton = new Button(getTextForHoursLeft(hoursLeft));
        statusButton.getStyleClass().addAll(getStyleForHoursLeft(hoursLeft) , "table-row-cell");
        return statusButton;
    }

     private static String getStyleForHoursLeft(long hoursLeft) {
        if (hoursLeft <= 6) {
            return "button-red";
        } else if (hoursLeft <= 12) {
            return "button-orange";
        } else {
            return "button-green";
        }
    }

    private static String getTextForHoursLeft(long hoursLeft) {
        if (hoursLeft <= 6) {
            return "High";
        } else if (hoursLeft <= 12) {
            return "Medium";
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

    public static class ButtonStatusTicketCellValueFactory implements Callback<TableColumn.CellDataFeatures<Purchase, Node>, ObservableValue<Node>> {

        @Override
        public ObservableValue<Node> call(TableColumn.CellDataFeatures<Purchase, Node> param) {
            Purchase purchase = param.getValue();
            String text;
            String styleClass;
            String imagePath = "";

            if (purchase instanceof MultiEntryTicket) {
                return new SimpleObjectProperty<>(null);
            } else if (purchase instanceof HomeViewingPackageInstance) {
                HomeViewingPackageInstance viewingPackage = (HomeViewingPackageInstance) purchase;
                if (purchase.getIsActive()) {
                    if (viewingPackage.isLinkActive()) {
                        text = "Available";
                        styleClass = "button-green";
                        return new SimpleObjectProperty<>(createButton(text, imagePath, styleClass));
                    } else {
                        text = "Link Open On " + viewingPackage.getViewingDate().format(DateTimeFormatter.ofPattern("dd/MM/yy"));
                        styleClass = "label-blue";
                        return new SimpleObjectProperty<>(createLabel(text, styleClass));
                    }
                } else {
                    text = "Not Available";
                    styleClass = "button-red";
                    return new SimpleObjectProperty<>(createButton(text, imagePath, styleClass));
                }
            } else {
                if (purchase.getIsActive()) {
                    text = "Available";
                    styleClass = "button-green";
                } else {
                    text = "Not Available";
                    styleClass = "button-red";
                }
                return new SimpleObjectProperty<>(createButton(text, styleClass));
            }
        }

        private Label createLabel(String text, String styleClass) {
            Label label = new Label(text);
            label.getStyleClass().add(styleClass);
            return label;
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
            String text, imagePath, styleClass="button-default";

            if (item.getPurchase() instanceof MultiEntryTicket) {
                text = "Multi-Entry Card";
                imagePath = ConstantsPath.GENERAL_PACKAGE + "two-tickets.png";
            } else if (item.getPurchase() instanceof MovieTicket) {
                text = "Theater Ticket";
                imagePath = ConstantsPath.GENERAL_PACKAGE + "movie-theater.png";
            } else if (item.getPurchase() instanceof HomeViewingPackageInstance) {
                text = "Home Viewing";
                imagePath = ConstantsPath.GENERAL_PACKAGE + "home.png";
            } else {
                imagePath = ConstantsPath.GENERAL_PACKAGE + "more-info.png";
                text = "UnClassified";
            }

            return new SimpleObjectProperty<>(createButton(text, imagePath, styleClass));
        }
    }




    public static class ButtonTypePurchaseCellValueFactory implements Callback<TableColumn.CellDataFeatures<Purchase, Button>, ObservableValue<Button>> {

        @Override
        public ObservableValue<Button> call(TableColumn.CellDataFeatures<Purchase, Button> param) {
            Purchase item = param.getValue();
            String text="", imagePath="", styleClass="button-default";

            if (item instanceof MultiEntryTicket) {
                text = "Multi-Entry Card";
                imagePath = ConstantsPath.GENERAL_PACKAGE + "two-tickets.png";
             } else if (item instanceof MovieTicket) {
                text = "Theater Ticket";
                imagePath = ConstantsPath.GENERAL_PACKAGE + "movie-theater.png";
             } else if (item instanceof HomeViewingPackageInstance) {
                text = "Home Viewing";
                imagePath = ConstantsPath.GENERAL_PACKAGE + "home.png";
             }

            return new SimpleObjectProperty<>(createButton(text, imagePath, styleClass));
        }
    }

    public static Button createButtonGenre(Movie movie) {

            String text = movie.getGenre();
            String imagePath = "";
            String styleClass;

            switch (movie.getGenre()) {
                case "action":
                    imagePath = ConstantsPath.GENRE_PACKAGE + "action.png";
                    styleClass = "button-blue";
                    break;
                case "comedy":
                    imagePath = ConstantsPath.GENRE_PACKAGE + "comedy.png";
                    styleClass = "button-blue";
                    break;
                case "drama":
                    imagePath = ConstantsPath.GENRE_PACKAGE + "drama.png";
                    styleClass = "button-purple";
                    break;
                case "horror":
                    imagePath = ConstantsPath.GENRE_PACKAGE + "horror.png";
                    styleClass = "button-pink";
                    break;
                case "romance":
                    imagePath = ConstantsPath.GENRE_PACKAGE + "romance.png";
                    styleClass = "button-pink";
                    break;
                case "sci-fi":
                    imagePath = ConstantsPath.GENRE_PACKAGE + "sci-fi.png";
                    styleClass = "button-tomato";
                    break;
                case "thriller":
                    imagePath = ConstantsPath.GENRE_PACKAGE + "thriller.png";
                    styleClass = "button-orange";
                    break;
                case "animation":
                    imagePath = ConstantsPath.GENRE_PACKAGE + "animation.png";
                    styleClass = "button-orange";
                    break;
                case "fantasy":
                    imagePath = ConstantsPath.GENRE_PACKAGE + "fantasy.png";
                    styleClass = "button-light-purple";
                    break;
                case "musical":
                    imagePath = ConstantsPath.GENRE_PACKAGE + "musical.png";
                    styleClass = "button-hot-pink";
                    break;
                default:
                    styleClass = "button-default";
                    break;
            }

             return createButton( text,  imagePath,  styleClass);

    }

    public static class ButtonGenreCellValueFactory implements Callback<TableColumn.CellDataFeatures<Movie, Button>, ObservableValue<Button>> {

        @Override
        public ObservableValue<Button> call(TableColumn.CellDataFeatures<Movie, Button> param) {
            Movie item = param.getValue();
            String text = item.getGenre();
            String imagePath = "";
            String styleClass;

            switch (item.getGenre()) {
                case "action":
                    imagePath = ConstantsPath.GENRE_PACKAGE + "action.png";
                    styleClass = "button-green";
                    break;
                case "comedy":
                    imagePath = ConstantsPath.GENRE_PACKAGE + "comedy.png";
                    styleClass = "button-blue";
                    break;
                case "drama":
                    imagePath = ConstantsPath.GENRE_PACKAGE + "drama.png";
                    styleClass = "button-purple";
                    break;
                case "horror":
                    imagePath = ConstantsPath.GENRE_PACKAGE + "horror.png";
                    styleClass = "button-red";
                    break;
                case "romance":
                    imagePath = ConstantsPath.GENRE_PACKAGE + "romance.png";
                    styleClass = "button-pink";
                    break;
                case "sci-fi":
                    imagePath = ConstantsPath.GENRE_PACKAGE + "sci-fi.png";
                    styleClass = "button-tomato";
                    break;
                case "thriller":
                    imagePath = ConstantsPath.GENRE_PACKAGE + "thriller.png";
                    styleClass = "button-green";
                    break;
                case "animation":
                    imagePath = ConstantsPath.GENRE_PACKAGE + "animation.png";
                    styleClass = "button-orange";
                    break;
                case "fantasy":
                    imagePath = ConstantsPath.GENRE_PACKAGE + "fantasy.png";
                    styleClass = "button-light-purple";
                    break;
                case "musical":
                    imagePath = ConstantsPath.GENRE_PACKAGE + "musical.png";
                    styleClass = "button-hot-pink";
                    break;
                default:
                    styleClass = "button-default";
                    break;
            }

            return new SimpleObjectProperty<>(createButton(text, imagePath, styleClass));
        }
    }

    public static class ButtonMovieTypeCellValueFactory implements Callback<TableColumn.CellDataFeatures<Movie, Button>, ObservableValue<Button>> {

        @Override
        public ObservableValue<Button> call(TableColumn.CellDataFeatures<Movie, Button> param) {
            Movie item = param.getValue();
            String text = item.getStreamingType().toString();
            String styleClass = "button-default";
            String imagePath = "";

            imagePath = ConstantsPath.GENERAL_PACKAGE + "action.png";

            switch (item.getStreamingType()) {
                case HOME_VIEWING:
                    text = "Home Viewing";
                    imagePath = ConstantsPath.GENERAL_PACKAGE + "home.png";
                    break;
                case THEATER_VIEWING:
                    text = "Theater Viewing";
                    imagePath = ConstantsPath.GENERAL_PACKAGE + "movie-theater.png";
                    break;
                case BOTH:
                    text = "Theater & Home Viewing";
                    imagePath = ConstantsPath.GENERAL_PACKAGE + "movie-projector.png";
                    break;

            }

            return new SimpleObjectProperty<>(createButton(text, "", styleClass));
        }
    }
}

