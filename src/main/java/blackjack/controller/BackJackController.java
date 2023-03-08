package blackjack.controller;

import blackjack.domain.BlackJackGame;
import blackjack.domain.GameResult;
import blackjack.domain.card.Card;
import blackjack.domain.card.generator.RandomDeckGenerator;
import blackjack.dto.CardAndScoreResult;
import blackjack.view.InputView;
import blackjack.view.OutputView;
import blackjack.view.ViewRenderer;

import java.util.List;
import java.util.Map;

public class BackJackController {

    private final InputView inputView = new InputView();
    private final OutputView outputView = new OutputView();

    public void run() {
        BlackJackGame blackJackGame = initBlackJackGame();
        printInitialStatus(blackJackGame);
        playPlayerTurn(blackJackGame);
        playDealerTurn(blackJackGame);
        printCardResult(blackJackGame);
        printGameResult(blackJackGame);
    }

    private BlackJackGame initBlackJackGame() {
        outputView.printPlayerNameRequestMessage();
        final List<String> names = inputView.readPlayerNames();
        outputView.printLineBreak();
        return new BlackJackGame(names, new RandomDeckGenerator());
    }

    private void printInitialStatus(BlackJackGame blackJackGame) {
        outputView.printInitialStatus(ViewRenderer.renderStatus(blackJackGame.getInitialHoldingCards()));
        outputView.printLineBreak();
    }

    private void playPlayerTurn(BlackJackGame blackJackGame) {
        final List<String> playerNames = blackJackGame.getPlayerNames();
        for (final String name : playerNames) {
            playFor(blackJackGame, name);
            outputView.printLineBreak();
        }
    }

    private void playFor(BlackJackGame blackJackGame, String name) {
        while (isContinuous(name, blackJackGame)) {
            blackJackGame.playPlayer(name);
            List<Card> userCards = blackJackGame.getHandholdingCards().get(name);
            outputView.printCards(name, ViewRenderer.renderCardsToString(userCards));
        }
    }

    private boolean isContinuous(String name, BlackJackGame blackJackGame) {
        if (blackJackGame.isPossibleToDraw(name)) {
            outputView.printDrawCardRequestMessage(name);
            return DrawInput.from(inputView.readDrawOrStay()).isDraw();
        }
        return false;
    }

    private void playDealerTurn(BlackJackGame blackJackGame) {
        int dealerDrawCount = blackJackGame.playDealerTurn();
        while (dealerDrawCount-- > 0) {
            outputView.printDealerDrawInfoMessage();
        }
        outputView.printLineBreak();
    }

    private void printCardResult(BlackJackGame blackJackGame) {
        final List<CardAndScoreResult> cardAndScoreResult = blackJackGame.getCardAndScoreResult();
        outputView.printCarAndScoreResult(cardAndScoreResult);
        outputView.printLineBreak();
    }

    private void printGameResult(BlackJackGame blackJackGame) {
        Map<String, GameResult> gameResult = blackJackGame.getGameResult();
        outputView.printWinningResult(ViewRenderer.renderWinningResult(gameResult));
    }
}
