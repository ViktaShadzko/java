package edu.ehu.exam.service;

import edu.ehu.exam.model.GameType;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class GameService {


    public Map<String, Double> play(Map<String, Double> players, GameType gameType) {
        HashMap<String, Double> result = new HashMap<>();
        for (Map.Entry<String, Double> entry : players.entrySet()) {
            String playerName = entry.getKey();
            double betAmount = entry.getValue();

            double outcome = Math.random();

            double winnings = switch (gameType) {
                case POKER -> betAmount * (outcome < 0.5 ? 2 : 0.1); // 50% chance to win double
                case BLACKJACK -> betAmount * (outcome < 0.4 ? 1.5 : 0.2); // 40% chance to win 1.5x
                case ROULETTE -> betAmount * (outcome < 0.3 ? 35 : 0.3); // 30% chance to win 35x
                case BACCARAT -> betAmount * (outcome < 0.45 ? 1 : 0.4); // 45% chance to win equal amount
            };

            result.put(playerName, winnings);
        }
        return result;
    }
}
