package il.cshaifasweng.OCSFMediatorExample.client.util.generators;

import il.cshaifasweng.OCSFMediatorExample.client.util.constants.ConstantsPath;
import il.cshaifasweng.OCSFMediatorExample.entities.*;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

public class ButtonFactory {

    public class ButtonTypeOrderCellValueFactory implements Callback<TableColumn.CellDataFeatures<Complaint, Button>, ObservableValue<Button>> {

        private final TableColumn<Complaint, Button> colPurchase;

        public ButtonTypeOrderCellValueFactory(TableColumn<Complaint, Button> colPurchase) {
            this.colPurchase = colPurchase;
        }

        @Override
        public ObservableValue<Button> call(TableColumn.CellDataFeatures<Complaint, Button> param) {
            Complaint item = param.getValue();

            Button button = new Button();
            button.setPrefWidth(colPurchase.getWidth() / 2);

            button.setPrefWidth(100);
            button.setPrefWidth(100); // Adjust the width as needed
            ImageView imageView = new ImageView();

            if (item.getPurchase() instanceof MultiEntryTicket) {
                imageView.setImage(new Image(getClass().getResourceAsStream(ConstantsPath.MEDIA_PACKAGE + "icons/genre/action.png")));
                button.getStyleClass().addAll("button-green", "table-row-cell");
                button.setText("Multi-Entry Card");
            } else if (item.getPurchase() instanceof MovieTicket) {
                imageView.setImage(new Image(getClass().getResourceAsStream(ConstantsPath.MEDIA_PACKAGE + "icons/genre/comedy.png")));
                button.getStyleClass().addAll("button-blue", "table-row-cell");
                button.setText("Movie Ticket");
            } else {
                imageView.setImage(new Image(getClass().getResourceAsStream(ConstantsPath.MEDIA_PACKAGE + "icons/genre/drama.png")));
                button.getStyleClass().addAll("button-purple", "table-row-cell");
                button.setText("Home Viewing Package");
            }

            return new SimpleObjectProperty<>(button);
        }
    }

    public class ButtonExistsCellValueFactory implements Callback<TableColumn.CellDataFeatures<Complaint, Button>, ObservableValue<Button>> {

        private final TableColumn<Complaint, Button> colStatus;

        public ButtonExistsCellValueFactory(TableColumn<Complaint, Button> colStatus) {
            this.colStatus = colStatus;
        }

        @Override
        public ObservableValue<Button> call(TableColumn.CellDataFeatures<Complaint, Button> param) {
            Complaint item = param.getValue();

            Button button = new Button();
            button.setText(item.isClosed() ? "CLOSE" : "OPEN");
            button.setPrefWidth(colStatus.getWidth() / 2);

            if (item.isClosed()) {
                button.getStyleClass().addAll("button-yes", "table-row-cell");
            } else {
                button.getStyleClass().addAll("button-no", "table-row-cell");
            }
            return new SimpleObjectProperty<>(button);
        }
    }

    public class ButtonTypePurchaseCellValueFactory implements Callback<TableColumn.CellDataFeatures<Purchase, Button>, ObservableValue<Button>> {

        private final TableColumn<Purchase, Button> colTypePurchase;

        public ButtonTypePurchaseCellValueFactory(TableColumn<Purchase, Button> colTypePurchase) {
            this.colTypePurchase = colTypePurchase;
        }

        @Override
        public ObservableValue<Button> call(TableColumn.CellDataFeatures<Purchase, Button> param) {
            Purchase item = param.getValue();

            Button button = new Button();
            button.setPrefWidth(100);
            button.setPrefWidth(100); // Adjust the width as needed
            ImageView imageView = new ImageView();
            button.setPrefWidth(colTypePurchase.getWidth() / 2);

            if (item instanceof MultiEntryTicket) {
                imageView.setImage(new Image(getClass().getResourceAsStream(ConstantsPath.MEDIA_PACKAGE + "icons/genre/action.png")));
                button.getStyleClass().addAll("button-orange", "table-row-cell");
                button.setText("Multi-Entry Card");
            } else if (item instanceof MovieTicket) {
                imageView.setImage(new Image(getClass().getResourceAsStream(ConstantsPath.MEDIA_PACKAGE + "icons/genre/comedy.png")));
                button.getStyleClass().addAll("button-blue", "table-row-cell");
                button.setText("Movie Ticket");
            } else {
                imageView.setImage(new Image(getClass().getResourceAsStream(ConstantsPath.MEDIA_PACKAGE + "icons/genre/drama.png")));
                button.getStyleClass().addAll("button-purple", "table-row-cell");
                button.setText("Home Viewing Package");
            }
            return new SimpleObjectProperty<>(button);
        }
    }

    public class ButtonStatusCellValueFactory implements Callback<TableColumn.CellDataFeatures<Purchase, Button>, ObservableValue<Button>> {
        @Override
        public ObservableValue<Button> call(TableColumn.CellDataFeatures<Purchase, Button> param) {
            Purchase item = param.getValue();

            Button button = new Button();
            button.setText(item.getPurchaseValidation().equals("REALIZED") ? "Available" : "Available");
            button.setPrefWidth(100); // Adjust the width as needed

            if (item.getPurchaseValidation().equals("REALIZED")) {
                button.getStyleClass().addAll("button-red", "table-row-cell");
            } else {
                button.getStyleClass().addAll("button-green", "table-row-cell");
            }
            return new SimpleObjectProperty<>(button);
        }
    }


    public class ButtonGenreCellValueFactory implements Callback<TableColumn.CellDataFeatures<Movie, Button>, ObservableValue<Button>> {
        private TableColumn<Movie, String> colGenre;

        // קונסטרקטור שמקבל את TableColumn כפרמטר
        public ButtonGenreCellValueFactory() {
            this.colGenre = colGenre;
        }

        @Override
        public ObservableValue<Button> call(TableColumn.CellDataFeatures<Movie, Button> param) {
            // שימוש ב-colGenre או כל לוגיקה אחרת
            Movie item = param.getValue();

            Button button = new Button();
            button.setText(item.getGenre());


            button.setPrefWidth(100);
            button.setPrefWidth(100); // Adjust the width as needed
            ImageView imageView = new ImageView();

            // Add style classes based on the genre
            switch (item.getGenre().toLowerCase()) {
                case "action":
                    imageView.setImage(new Image(getClass().getResourceAsStream(ConstantsPath.MEDIA_PACKAGE + "icons/genre/action.png")));
                    button.getStyleClass().addAll("button-green", "table-row-cell");
                    break;
                case "comedy":
                     imageView.setImage(new Image(getClass().getResourceAsStream(ConstantsPath.MEDIA_PACKAGE + "icons/genre/comedy.png")));
                    button.getStyleClass().addAll("button-blue", "table-row-cell");
                    break;
                case "drama":
                    imageView.setImage(new Image(getClass().getResourceAsStream(ConstantsPath.MEDIA_PACKAGE + "icons/genre/drama.png")));
                    button.getStyleClass().addAll("button-purple", "table-row-cell");
                    break;
                case "horror":
                    imageView.setImage(new Image(getClass().getResourceAsStream(ConstantsPath.MEDIA_PACKAGE + "icons/genre/horror.png")));
                    button.getStyleClass().addAll("button-red", "table-row-cell");
                    break;
                case "romance":
                     imageView.setImage(new Image(getClass().getResourceAsStream(ConstantsPath.MEDIA_PACKAGE + "icons/genre/romance.png")));
                    button.getStyleClass().addAll("button-pink", "table-row-cell");
                    break;
                case "sci-fi":
                    imageView.setImage(new Image(getClass().getResourceAsStream(ConstantsPath.MEDIA_PACKAGE + "icons/genre/sci-fi.png")));
                    button.getStyleClass().addAll("button-tomato", "table-row-cell");
                    break;
                case "thriller":
                    imageView.setImage(new Image(getClass().getResourceAsStream(ConstantsPath.MEDIA_PACKAGE + "icons/genre/horror.png")));
                    button.getStyleClass().addAll("button-green", "table-row-cell");
                    break;
                case "animation":
                     imageView.setImage(new Image(getClass().getResourceAsStream(ConstantsPath.MEDIA_PACKAGE + "icons/genre/animation.png")));
                    button.getStyleClass().addAll("button-orange", "table-row-cell");
                    break;

                case "fantasy":
                    imageView.setImage(new Image(getClass().getResourceAsStream(ConstantsPath.MEDIA_PACKAGE + "icons/genre/fantasy.png")));
                    button.getStyleClass().addAll("button-light-purple", "table-row-cell");
                    break;
                case "musical":
                     imageView.setImage(new Image(getClass().getResourceAsStream(ConstantsPath.MEDIA_PACKAGE + "icons/genre/musical.png")));
                    button.getStyleClass().addAll("button-hot-pink", "table-row-cell");
                    break;

                default:
                    button.getStyleClass().addAll("button-default", "table-row-cell");
                    break;
            }

            // Attach image to the button
            if (imageView.getImage() != null) {
                imageView.setFitHeight(20);  // Set the image height
                imageView.setFitWidth(20);   // Set the image width
                button.setGraphic(imageView);
            }

            return new SimpleObjectProperty<>(button);
        }
        // מתודה חדשה שמקבלת כפתור ומחזירה את הכפתור מעודכן עם הגדרות הז'אנר
        public Button configureGenreButton(Button button, Movie movie) {
            button.setText(movie.getGenre());
            button.setPrefWidth(100);
            ImageView imageView = new ImageView();

            // Add style classes based on the genre
            switch (movie.getGenre().toLowerCase()) {
                case "action":
                    imageView.setImage(new Image(getClass().getResourceAsStream(ConstantsPath.MEDIA_PACKAGE + "icons/genre/action.png")));
                    button.getStyleClass().addAll("button-green", "table-row-cell");
                    break;
                case "comedy":
                    imageView.setImage(new Image(getClass().getResourceAsStream(ConstantsPath.MEDIA_PACKAGE + "icons/genre/comedy.png")));
                    button.getStyleClass().addAll("button-blue", "table-row-cell");
                    break;
                case "drama":
                    imageView.setImage(new Image(getClass().getResourceAsStream(ConstantsPath.MEDIA_PACKAGE + "icons/genre/drama.png")));
                    button.getStyleClass().addAll("button-purple", "table-row-cell");
                    break;
                case "horror":
                    imageView.setImage(new Image(getClass().getResourceAsStream(ConstantsPath.MEDIA_PACKAGE + "icons/genre/horror.png")));
                    button.getStyleClass().addAll("button-red", "table-row-cell");
                    break;
                case "romance":
                    imageView.setImage(new Image(getClass().getResourceAsStream(ConstantsPath.MEDIA_PACKAGE + "icons/genre/romance.png")));
                    button.getStyleClass().addAll("button-pink", "table-row-cell");
                    break;
                case "sci-fi":
                    imageView.setImage(new Image(getClass().getResourceAsStream(ConstantsPath.MEDIA_PACKAGE + "icons/genre/sci-fi.png")));
                    button.getStyleClass().addAll("button-tomato", "table-row-cell");
                    break;
                case "thriller":
                    imageView.setImage(new Image(getClass().getResourceAsStream(ConstantsPath.MEDIA_PACKAGE + "icons/genre/thriller.png")));
                    button.getStyleClass().addAll("button-green", "table-row-cell");
                    break;
                case "animation":
                    imageView.setImage(new Image(getClass().getResourceAsStream(ConstantsPath.MEDIA_PACKAGE + "icons/genre/animation.png")));
                    button.getStyleClass().addAll("button-orange", "table-row-cell");
                    break;
                case "fantasy":
                    imageView.setImage(new Image(getClass().getResourceAsStream(ConstantsPath.MEDIA_PACKAGE + "icons/genre/fantasy.png")));
                    button.getStyleClass().addAll("button-light-purple", "table-row-cell");
                    break;
                case "musical":
                    imageView.setImage(new Image(getClass().getResourceAsStream(ConstantsPath.MEDIA_PACKAGE + "icons/genre/musical.png")));
                    button.getStyleClass().addAll("button-hot-pink", "table-row-cell");
                    break;
                default:
                    button.getStyleClass().addAll("button-default", "table-row-cell");
                    break;
            }

            // Attach image to the button
            if (imageView.getImage() != null) {
                imageView.setFitHeight(20);  // Set the image height
                imageView.setFitWidth(20);   // Set the image width
                button.setGraphic(imageView);
            }

            return button;
        }

    }


    public class ButtonMovieTypeCellValueFactory implements Callback<TableColumn.CellDataFeatures<Movie, Button>, ObservableValue<Button>> {
        private TableColumn<Movie, String> colType;

        // קונסטרקטור שמקבל את TableColumn כפרמטר
        public ButtonMovieTypeCellValueFactory() {
            this.colType = colType;
        }

        @Override
        public ObservableValue<Button> call(TableColumn.CellDataFeatures<Movie, Button> param) {
            Movie item = param.getValue();

            Button button = new Button();
            button.setText(item.getStreamingType().toString());

            button.setPrefWidth(100);
            button.setPrefWidth(100); // Adjust the width as needed
            ImageView imageView = new ImageView();


            // Add style classes based on the genre
            switch (item.getStreamingType()) {
                case HOME_VIEWING:
                    button.getStyleClass().addAll("button-action", "table-row-cell");
                    break;
                case THEATER_VIEWING:
                    //   imageView.setImage(new Image(getClass().getResourceAsStream(ConstantsPath.MEDIA_PACKAGE + "icons/comedy-icon.png")));
                    button.getStyleClass().addAll("button-comedy", "table-row-cell");
                    break;
                case BOTH:
                    button.getStyleClass().addAll("button-drama", "table-row-cell");
                    break;
                default:
                    button.getStyleClass().addAll("button-default", "table-row-cell");
                    break;
            }

            // Attach image to the button
            if (imageView.getImage() != null) {
                imageView.setFitHeight(20);  // Set the image height
                imageView.setFitWidth(20);   // Set the image width
                button.setGraphic(imageView);
            }

            return new SimpleObjectProperty<>(button);
        }

    }

    }















