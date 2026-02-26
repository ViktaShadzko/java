package edu.ehu.exam.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class GameTable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String name;

    @Enumerated(EnumType.STRING)
    private  GameType gameType;
    private double minBet;
    private double maxBet;
    private int maxPlayers;

    @ElementCollection
    @CollectionTable(name = "game_table_players", joinColumns = @JoinColumn(name = "game_table_id"))
    @MapKeyColumn(name = "player_name")
    @Column(name = "player_money")
    private Map<String,Double> players = new HashMap<>();
}
