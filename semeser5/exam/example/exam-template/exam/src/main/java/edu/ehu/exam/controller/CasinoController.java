package edu.ehu.exam.controller;

import edu.ehu.exam.dto.OccupyRequest;
import edu.ehu.exam.dto.RealiseRequest;
import edu.ehu.exam.model.GameTable;
import edu.ehu.exam.model.GameType;
import edu.ehu.exam.service.CasinoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tables")
@RequiredArgsConstructor
public class CasinoController {
    private final CasinoService casinoService;

    @GetMapping
    public List<GameTable> getAll() {
        return casinoService.getAllGameTables();
    }

    @GetMapping("/{id}")
    public GameTable getById(@PathVariable String id) {
        return casinoService.getGameTableById(id);
    }

    @GetMapping("/available")
    public List<GameTable> getAvailable() {
        return casinoService.getAvailableGameTables();
    }

    @GetMapping("/by-type/{gameType}")
    public List<GameTable> getByType(@PathVariable GameType gameType) {
        return casinoService.getGameTablesByType(gameType);
    }

    @PostMapping
    public GameTable create(@RequestBody GameTable gameTable) {
        return casinoService.createGameTable(gameTable);
    }

    @PostMapping("/{id}/occupy")
    public void occupy(@PathVariable String id, @RequestBody OccupyRequest request) {
        casinoService.occupyTable(request.getMoney(), request.getName(), id);
    }

    @PostMapping("/{id}/release")
    public void release(@PathVariable String id, @RequestBody RealiseRequest request) {
        casinoService.releaseTable(request.getName(), id);
    }

    @PostMapping("/{id}/play")
    public void play(@PathVariable String id) {
        casinoService.playGame(id);
    }

    @PutMapping("/{id}")
    public GameTable update(@PathVariable String id, @RequestBody GameTable gameTable) {
        return casinoService.updateGameTable(id, gameTable);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        casinoService.deleteGameTable(id);
    }
}
