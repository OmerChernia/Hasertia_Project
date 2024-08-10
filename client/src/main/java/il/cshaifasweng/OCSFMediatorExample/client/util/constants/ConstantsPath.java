package il.cshaifasweng.OCSFMediatorExample.client.util.constants;

import javafx.scene.effect.BoxBlur;

public class ConstantsPath {

    public static final String TITLE = "Hasretia";
    public static final Double MIN_WIDTH = 1040.00;
    public static final Double MIN_HEIGHT = 640.00;

    public static final String SOURCE_PACKAGE = "/il/cshaifasweng/OCSFMediatorExample/client/";
    public static final String BOUNDARIES_PACKAGE = SOURCE_PACKAGE + "boundaries/";
    public static final String REPORTS_PACKAGE =  BOUNDARIES_PACKAGE + "reports/";
    public static final String CONTENT_MANAGER_PACKAGE =  BOUNDARIES_PACKAGE + "contentManager/";
    public static final String CUSTOMER_SERVICE_PACKAGE =  BOUNDARIES_PACKAGE + "customerService/";
    public static final String REGISTER_USER_PACKAGE =  BOUNDARIES_PACKAGE + "registeredUser/";
    public static final String USER_PACKAGE =  BOUNDARIES_PACKAGE + "user/";


    /* USER_VIEWS */
    public static final String START_VIEW =  USER_PACKAGE + "StartView.fxml";
    public static final String ABOUT_VIEW =  USER_PACKAGE + "AboutView.fxml";
    public static final String DIALOG_TICKET_VIEW =  USER_PACKAGE + "dialogTicket.fxml";
    public static final String HOME_VIEW =  USER_PACKAGE + "HomeView.fxml";
    public static final String ME_PURCHASE_VIEW =  USER_PACKAGE + "MEPurchaseView.fxml";
    public static final String PACKAGE_PURCHASE_VIEW =  USER_PACKAGE + "PackagePurchaseView.fxml";
    public static final String MOVIE_INFO_VIEW =  USER_PACKAGE + "MovieInfo.fxml";
    public static final String MOVIE_SMALL_VIEW =  USER_PACKAGE + "MovieSmall.fxml";
    public static final String THEATER_PURCHASE_VIEW =  USER_PACKAGE + " TheaterPurchaseView.fxml";


     /* REGISTER_USER_VIEWS */
    public static final String COMPLAINT_VIEW =  REGISTER_USER_PACKAGE + "ComplaintView.fxml";
    public static final String DIALOG_COMPLAINT_VIEW  =  REGISTER_USER_PACKAGE + "dialogComplaint.fxml";
    public static final String ORDERS_VIEW =  REGISTER_USER_PACKAGE + "OrdersView.fxml";

    /* CONTENT_MANAGER_VIEWS */
    public static final String CONTENT_MOVIES_VIEW  =  CONTENT_MANAGER_PACKAGE + "EditMovieListView.fxml";
    public static final String CONTENT_SCREENINGS_VIEW  =  CONTENT_MANAGER_PACKAGE + "EditMovieScreeningsView.fxml";

    /* CUSTOMER_MANAGER_VIEWS */
    public static final String CUSTOMER_SERVICE_VIEW  =  CUSTOMER_SERVICE_PACKAGE + "CustomerService.fxml";
    public static final String DIALOG_CUSTOMER_SERVICE_VIEW  =  CUSTOMER_SERVICE_PACKAGE + "dialogCustomerService.fxml";


    /* REPORTS_VIEWS */
    public static final String COMPANY_MANAGER_VIEW  =  REPORTS_PACKAGE + "ReportsView.fxml";


    public static final String MEDIA_PACKAGE = SOURCE_PACKAGE+ "media/";
    public static final String MOVIE_PACKAGE = MEDIA_PACKAGE + "Movie/";
    public static final String ICON_PACKAGE = MEDIA_PACKAGE + "icons/";
    public static final String PROFILE_PICTURES_PACKAGE = MEDIA_PACKAGE + "profiles/";












    public static final String STAGE_ICON = MEDIA_PACKAGE + "icon.jpeg";
    public static final String NO_IMAGE_AVAILABLE = MEDIA_PACKAGE + "empty-image.jpg";
    public static final String INFORMATION_IMAGE = MEDIA_PACKAGE + "information.png";
    public static final String ERROR_IMAGE = MEDIA_PACKAGE + "error.png";
    public static final String SUCCESS_IMAGE = MEDIA_PACKAGE + "success.png";

    public static final String CSS_LIGHT_THEME = SOURCE_PACKAGE + "css/main.css";
    public static final String LIGHT_THEME = ConstantsPath.class.getResource(CSS_LIGHT_THEME).toExternalForm();

    public static final String MESSAGE_IMAGE_LARGE = "Please upload a picture smaller than 1 MB.";
    public static final String MESSAGE_IMAGE_NOT_FOUND = "Image not found. The record will be saved.";
    public static final String MESSAGE_INSUFFICIENT_DATA = "Insufficient data";
    public static final String MESSAGE_USER_ALREADY_EXISTS = "This user already exists";
    public static final String MESSAGE_PASSWORDS_NOT_MATCH = "Passwords do not match.";
    public static final String MESSAGE_ENTER_AT_LEAST_4_CHARACTERES = "Please enter at least 4 characters";
    public static final String MESSAGE_NO_RECORD_SELECTED = "Select an item from the table.";

    public static final String MESSAGE_ADDED = "Record added successfully";
    public static final String MESSAGE_UPDATED = "Record updated successfully";
    public static final String MESSAGE_DELETED = "Record deleted successfully";


    public static final String REALIZED = "Realized";
    public static final String NOT_REALIZED = "Not realized";


    public static final BoxBlur BOX_BLUR_EFFECT = new BoxBlur(3, 3, 3);
}
