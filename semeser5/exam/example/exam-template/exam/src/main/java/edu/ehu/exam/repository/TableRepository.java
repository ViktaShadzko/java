package edu.ehu.exam.repository;

import edu.ehu.exam.model.GameTable;
import edu.ehu.exam.model.GameType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TableRepository extends JpaRepository<GameTable, String> {

    List<GameTable> findByGameType(GameType gameType);
}
