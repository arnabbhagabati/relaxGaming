package com.relax.main.services;

import com.relax.main.beans.Game;
import com.relax.main.beans.GameStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GameEngineServiceTest {

    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private GameEngineService gameService;

    private Game mockGame;

    @BeforeEach
    void setUp() {
        mockGame = new Game("2f5a9c85-fccc-4a0b-b056-ad6711c80209","player1",50);
        mockGame.setStatus(GameStatus.IN_PROGRESS);
        mockGame.setPayout(BigDecimal.valueOf(200.0));
    }

    @Test
    void testDoubleOrNothing() {
        when(gameRepository.getById("2f5a9c85-fccc-4a0b-b056-ad6711c80209")).thenReturn(mockGame);
        try {
            BigDecimal payout = gameService.doubleOrNothing("2f5a9c85-fccc-4a0b-b056-ad6711c80209","player1");
            assert(payout.compareTo(BigDecimal.valueOf(400)) == 0 || payout.compareTo(BigDecimal.valueOf(0)) == 0);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}