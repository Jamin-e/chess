package ws;

public interface GameRepository {
    GameRecord getGame(Integer gameID);
    void save(GameRecord gameRecord);
}
