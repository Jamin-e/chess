package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;

import java.util.UUID;

public class UserService {
    private final DataAccess dataAccess;

    public UserService(DataAccess dataAccess){
        this.dataAccess = dataAccess;
    }

    public RegisterResult register(RegisterRequest registerrequest){}

    public LoginResult login(LoginRequest loginRequest) throws DataAccessException{
        if(loginRequest == null
                || loginRequest.password() == null || loginRequest.password().isBlank()
                || loginRequest.username() == null || loginRequest.username().isBlank()){
            throw new DataAccessException("Error: bad request");
        }
        UserData user =  dataAccess.getUser(loginRequest.username());
        if(user == null){
            throw new DataAccessException("Error: bad request");
        }

        if(!user.password().equals(loginRequest.password())){
            throw new DataAccessException("Error: unauthorized");
        }

        String token = UUID.randomUUID().toString();
        AuthData auth = new AuthData(token, user.username());
        dataAccess.createAuth(auth);

        return new LoginResult(user.username(), token);
    }

    public void logout(LogoutRequest logoutRequest) {}
}
