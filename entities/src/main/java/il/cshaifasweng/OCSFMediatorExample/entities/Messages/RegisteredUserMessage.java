package il.cshaifasweng.OCSFMediatorExample.entities.Messages;


import il.cshaifasweng.OCSFMediatorExample.entities.RegisteredUser;

public class RegisteredUserMessage extends Message
{
    public String user_id;
    public String name;
    public String email;

    public RequestType requestType;
    public ResponseType responseType;

    public RegisteredUser registeredUser = null;

    public RegisteredUserMessage(MessageType messageType,String user_id, String firstName, String lastName, String email, RequestType requestType)
    {
        super(messageType);
        this.user_id = user_id;
        this.name = firstName + " " + lastName;
        this.email = email;
        this.requestType = requestType;
    }

    public enum RequestType
    {
        ADD_NEW_USER
    }
    public enum ResponseType
    {
        USER_ADDED,
        USER_DID_NOT_ADDED
    }
}
