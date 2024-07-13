package il.cshaifasweng.OCSFMediatorExample.entities.Messages;

public class LoginMessage extends Message
{
    public String id;
    public RequestType requestType;
    public ResponseType responseType;

    public LoginMessage(){}
    public LoginMessage(String id, MessageType type, RequestType requestType)
    {
        //LOGIN , LOGOUT
        super(type);
        this.id = id;
        this.requestType = requestType;
    }

    public enum ResponseType
    {
        LOGIN_SUCCESFUL,
        LOGIN_FAILED,
        LOGOUT_SUCCESFUL,
        LOGOUT_FAILED,
        ALREADY_LOGGED
    }
    public enum RequestType {
        LOGIN,
        LOGOUT
    }
}
