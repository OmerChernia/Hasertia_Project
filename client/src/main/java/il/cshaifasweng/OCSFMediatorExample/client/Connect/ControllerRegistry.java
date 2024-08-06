package il.cshaifasweng.OCSFMediatorExample.client.Connect;

import il.cshaifasweng.OCSFMediatorExample.client.boundariesCustomer.*;
import il.cshaifasweng.OCSFMediatorExample.client.boundariesEmploee.*;
import org.greenrobot.eventbus.EventBus;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ControllerRegistry {

    // מיפוי סוגי משתמשים ל-Controllers רלוונטיים
    private static final Map<String, List<Class<?>>> userControllersMap = new HashMap<>();
    // רשימת ה-Controllers הרשומים כעת
    private static final List<Object> registeredControllers = new ArrayList<>();

    static {
        // הגדרת ה-Controllers עבור כל סוג משתמש

        // פרופיל דיפולטי (Default)
        userControllersMap.put("default", List.of(
                HomeController.class,
                LoginController.class
        ));

        // פרופיל לקוח (Customer)
        userControllersMap.put("customer", List.of(
                OrdersController.class,
                MovieInfoController.class,
                MEPurchaseController.class
        ));

        // פרופיל עובד (Employee)
        userControllersMap.put("employee", List.of(
                EditMovieListController.class,
                EditMovieScreeningsController.class,
                ReportsController.class,
                CustomerServiceController.class,
                SettingsController.class
        ));
    }

    // רישום ה-Controllers בהתאם לסוג המשתמש
    public static void registerControllersForUser(String userType) {
        List<Class<?>> controllersClasses = userControllersMap.get(userType);
        if (controllersClasses != null) {
            for (Class<?> controllerClass : controllersClasses) {
                try {
                    // יצירת מופע של ה-Controller ורישומו ל-EventBus
                    Object controller = controllerClass.getDeclaredConstructor().newInstance();
                    EventBus.getDefault().register(controller);
                    registeredControllers.add(controller);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            System.err.println("No controllers found for user type: " + userType);
        }
    }

    // ביטול רישום כל ה-Controllers
    public static void unregisterAll() {
        for (Object controller : registeredControllers) {
            EventBus.getDefault().unregister(controller);
        }
        registeredControllers.clear();
    }
}
