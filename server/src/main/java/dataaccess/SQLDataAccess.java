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

    public void clear() throws DataAccessException{
        var sql = "DROP DATABASE IF EXISTS chess";
        try(var conn = DatabaseManager.getConnection();
            var ps = conn.prepareStatement(sql)){
            ps.executeUpdate();
        }
        catch(SQLException e){
            throw new DataAccessException(e.getMessage());
        }
    }

    public void createUser(UserData user) throws DataAccessException{
        var sql = "INSERT INTO user (username, password_hash, email) VALUES (?, ?, ?)";
        try(var conn = DatabaseManager.getConnection();
                var ps = conn.prepareStatement(sql)){
            ps.setString(1, user.username());
            ps.setString(2, user.password());
            ps.setString(3, user.email());
            ps.executeUpdate();
    }
    catch(SQLException e){
        throw new DataAccessException(e.getMessage());
    }
    }

    public UserData getUser(String username) throws DataAccessException{
        try(var conn = DatabaseManager.getConnection()){
            return null;
        }
        catch(SQLException e){
            throw new DataAccessException(e.getMessage());
        }
    }

    public void createAuth(AuthData auth) throws DataAccessException {
        var sql = "INSERT INTO auth (token, username) VALUES (?, ?)";
        try(var conn = DatabaseManager.getConnection();
        var ps = conn.prepareStatement(sql)){
            ps.setString(1,auth.authToken());
            ps.setString(2,auth.username());
            ps.executeUpdate();

        }
        catch(SQLException e){
            throw new DataAccessException(e.getMessage());
        }
    }

    public AuthData getAuth(String authToken) throws DataAccessException{
        try(var conn = DatabaseManager.getConnection()){
            return null;
        }
        catch(SQLException e){
            throw new DataAccessException(e.getMessage());
        }
    }

    public void deleteAuth(String authToken) throws DataAccessException {
        var sql = "DELETE FROM auth WHERE token = (token) VALUES (?)";
        try(var conn = DatabaseManager.getConnection();
        var ps = conn.prepareStatement(sql)){
            ps.setString(1, authToken);
            ps.executeUpdate();
        }
        catch(SQLException e){
            throw new DataAccessException(e.getMessage());
        }
    }

    public int createGame(String gamename) throws DataAccessException{
        try(var conn = DatabaseManager.getConnection()){
            return -1;
        }
        catch(SQLException e){
            throw new DataAccessException(e.getMessage());
        }
    }

    public GameData getGame(int gameID) throws DataAccessException{
        try(var conn = DatabaseManager.getConnection()){
            return null;
        }
        catch(SQLException e){
            throw new DataAccessException(e.getMessage());
        }
    }

    public JoinResult updateGame(GameData game, String color, String username) throws DataAccessException{
        try(var conn = DatabaseManager.getConnection()){
            return null;
        }
        catch(SQLException e){
            throw new DataAccessException(e.getMessage());
        }
    }

    public Collection<GameData> listGames() throws DataAccessException{
        try(var conn = DatabaseManager.getConnection()){
            return null;
        }
        catch(SQLException e){
            throw new DataAccessException(e.getMessage());
        }
    }

}
