package edu.ehu.exam.service;

import edu.ehu.exam.exception.TableNotFound;
import edu.ehu.exam.model.GameTable;
import edu.ehu.exam.model.GameType;
import edu.ehu.exam.repository.TableRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CasinoService {
    private final TableRepository tableRepository;
    private final GameService gameService;

    public List<GameTable> getAllGameTables() {
        return tableRepository.findAll();
    }

    public GameTable getGameTableById(String id) {
        return tableRepository.findById(id).orElseThrow(() -> new TableNotFound("Game table not found"));
    }

    public List<GameTable> getAvailableGameTables() {
        return getAllGameTables().stream().filter(tbl-> tbl.getPlayers().size()< tbl.getMaxPlayers()).toList();
    }

    public List<GameTable> getGameTablesByType(GameType gameType) {
        return tableRepository.findByGameType(gameType);
    }

    public GameTable createGameTable(GameTable gameTable) {
        return save(gameTable);
    }

    private @NonNull GameTable save(GameTable gameTable) {
        return tableRepository.save(gameTable);
    }

    public void occupyTable(double money, String name, String id) {
        GameTable gameTableById = getGameTableById(id);
        if (gameTableById.getPlayers().size() >= gameTableById.getMaxPlayers()) {
            throw new RuntimeException("Table is full");
        }
        gameTableById.getPlayers().put(name, money);
        save(gameTableById);
    }

    public void releaseTable(String name, String id) {
        GameTable gameTableById = getGameTableById(id);
        if (!gameTableById.getPlayers().containsKey(name)) {
            throw new RuntimeException("Player not found at the table");
        }
        gameTableById.getPlayers().remove(name);
    }

    public void playGame(String id) {
        GameTable gameTableById = getGameTableById(id);
        Map<String, Double> players = gameTableById.getPlayers();
        Map<String, Double> result = gameService.play(players, gameTableById.getGameType());
        gameTableById.setPlayers(result);
        save(gameTableById);
    }

    public GameTable updateGameTable(String id, GameTable gameTable) {
        gameTable.setId(id);
        return save(gameTable);
    }

    public void deleteGameTable(String id) {
        tableRepository.deleteById(id);
    }

}
