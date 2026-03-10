package dataaccess;

import model.AuthData;
import model.GameData;
import model.UserData;
import service.JoinResult;

import java.sql.DriverManager;
import java.sql.*;
import java.util.Collection;
import java.util.Properties;

public class SQLDataAccess implements DataAccess{

    public SQLDataAccess() {

    }

    public void createUser(UserData user) throws DataAccessException{

    }

    public UserData getUser(String username) throws DataAccessException{

    }

    public void createAuth(AuthData auth) throws DataAccessException {

    }

    public AuthData getAuth(String authToken) throws DataAccessException{

    }

    public void deleteAuth(String authToken) throws DataAccessException {

    }

    public int createGame(String gamename) throws DataAccessException{

    }

    public GameData getGame(int gameID) throws DataAccessException{

    }

    public JoinResult updateGame(GameData game, String color, String username) throws DataAccessException{

    }

    public Collection<GameData> listGames(){

    }

}
