package service;


import model.GameData;

import java.util.Collection;
import java.util.Map;

public record ListResult(Collection<GameData> games) {
}
