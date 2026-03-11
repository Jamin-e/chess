package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import model.UserData;
import service.JoinResult;

import java.sql.DriverManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Properties;

public class SQLDataAccess implements DataAccess{
    private final Gson gson = new Gson();

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
        var sql = "SELECT username, password_hash, email FROM user WHERE username = (username) VALUES (?)";
        try(var conn = DatabaseManager.getConnection();
        var ps = conn.prepareStatement(sql)){
            ps.setString(1,username);
            ResultSet userdata = ps.executeQuery();
            UserData userData = new UserData(userdata.getNString("username"), userdata.getNString("password_hash"), userdata.getNString("email"));
            return userData;
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
        var sql ="SELECT token, username FROM auth WHERE token = (token) VALUES (?)";
        try(var conn = DatabaseManager.getConnection();
        var ps = conn.prepareStatement(sql)){
            ps.setString(1, authToken);
            ResultSet authSet = ps.executeQuery();
            AuthData userData = new AuthData(authSet.getNString("token"), authSet.getNString("username"));
            return userData;
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
        String json = gson.toJson(new ChessGame());
        var sql = """
                INSERT INTO game (game_name, white_name, black_username, game_state) VALUES (?,?,?,?)""";
        try(var conn = DatabaseManager.getConnection();
        var ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            ps.setString(1,gamename);
            ps.setString(2,null);
            ps.setString(3,null);
            ps.setString(4,json);

            try (var key = ps.getGeneratedKeys()) {
                if (key.next()) {
                    return key.getInt(1);
                } else {
                    throw new DataAccessException("No id for created game");
                }
            }
        }
        catch(SQLException e){
            throw new DataAccessException(e.getMessage());
        }
    }

    public GameData getGame(int gameID) throws DataAccessException{
        var sql = """
        SELECT id, game_name, white_username, black_username, game_state FROM game WHERE id = ?""";
        try(var conn = DatabaseManager.getConnection();
        var ps = conn.prepareStatement(sql)){
            ps.setInt(1, gameID);

            try (var query = ps.executeQuery()){
                if(!query.next()){
                    throw new DataAccessException("game does not exist");
                }
                int gameId = query.getInt("id");
                String name = query.getString("game_name");
                String white = query.getString("white_username");
                String black = query.getString("black_username");
                String json = query.getString("game_state");

                ChessGame game = gson.fromJson(json, ChessGame.class);

                return new GameData(gameId, white, black, name, game);
            }
        }
        catch(SQLException e){
            throw new DataAccessException(e.getMessage());
        }
    }

    public JoinResult joinGame(GameData game, String color, String username) throws DataAccessException{
        var sql = """
""";
        if (Objects.equals(color, "WHITE")) {
            sql = """
                    UPDATE game SET white_username = ? WHERE id = ?""";
        }
        else{
            sql = """
                    UPDATE game SET black_username = ? WHERE game_name = ?""";
        }
        try(var conn = DatabaseManager.getConnection();
        var ps = conn.prepareStatement(sql)){
            ps.setString(1,username);
            ps.setString(2, game.gameName());
            ps.executeUpdate();
            return new JoinResult();
        }
        catch(SQLException e){
            throw new DataAccessException(e.getMessage());
        }
    }

    public Collection<GameData> listGames() throws DataAccessException{
        var sql = """
        SELECT id, game_name, white_username, black_username, game_state FROM game;""";
        var result = new ArrayList<GameData>();
        try(var conn = DatabaseManager.getConnection();
        var ps = conn.prepareStatement(sql);
        var query = ps.executeQuery()){
            while(query.next()){
                int gameId = query.getInt("id");
                String name = query.getString("game_name");
                String white = query.getString("white_username");
                String black = query.getString("black_username");
                String json = query.getString("game_state");

                ChessGame game = gson.fromJson(json, ChessGame.class);

                result.add(new GameData(gameId,white,black,name,game));
            }
            return result;

        }
        catch(SQLException e){
            throw new DataAccessException(e.getMessage());
        }
    }

}
