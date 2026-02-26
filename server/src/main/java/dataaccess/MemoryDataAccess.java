package dataaccess;

import model.AuthData;
import model.UserData;

import java.util.HashMap;
import java.util.Map;

public class MemoryDataAccess implements DataAccess {
    private final Map<String, UserData> users = new HashMap<>();
    private final Map<String, AuthData> auths = new HashMap<>();

    public void clear(){
        users.clear();
        auths.clear();
    }

    public void createUser(UserData user) throws DataAccessException{
        if(users.containsKey(user.username())){
            throw new DataAccessException("user already exists");
        }
        users.put(user.username(),user);
    }

    public UserData getUser(String username) throws DataAccessException{
        if(!users.containsKey(username)){
            throw new DataAccessException("user does not exist");
        }
        return users.get(username);
    }

    public void createAuth(AuthData auth) throws DataAccessException {
        if(auths.containsKey(auth.authToken())){
            throw new DataAccessException("auth already exists");
        }
        auths.put(auth.authToken(),auth);
    }

    public AuthData getAuth(String authToken) throws DataAccessException{
        if(!auths.containsKey(authToken)){
            throw new DataAccessException("auth does not exist");
        }
        return auths.get(authToken);
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        if(!auths.containsKey(authToken)){
            throw new DataAccessException("auth does not exist");
        }
        auths.remove(authToken);
    }
}
