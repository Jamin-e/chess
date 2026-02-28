package service;

public record JoinRequest(String authToken, String playerColor, String gameID) {
}
