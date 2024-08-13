package il.cshaifasweng.OCSFMediatorExample.client.controllers;

import il.cshaifasweng.OCSFMediatorExample.client.connect.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Employee;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.EmployeeMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.TheaterManager;

public class EmployeeController {

    public static void requestEmployeeById(int id) {
        EmployeeMessage requestMessage = new EmployeeMessage(EmployeeMessage.MessageType.REQUEST, EmployeeMessage.RequestType.GET_EMPLOYEE_BY_ID);
        SimpleClient.getClient().sendRequest(requestMessage);
    }

}
