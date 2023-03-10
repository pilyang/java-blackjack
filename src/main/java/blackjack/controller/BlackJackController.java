package blackjack.controller;

import blackjack.domain.BlackJackGame;
import blackjack.domain.card.generator.RandomDeckGenerator;
import blackjack.dto.CardAndScore;
import blackjack.dto.HoldingCards;
import blackjack.dto.NameCardAndScore;
import blackjack.dto.ProfitResult;
import blackjack.util.RepeatValidator;
import blackjack.view.InputView;
import blackjack.view.OutputView;

import java.util.ArrayList;
import java.util.List;

import static blackjack.domain.user.Dealer.DEALER_NAME_CODE;

public class BlackJackController {

    private final InputView inputView = new InputView();
    private final OutputView outputView = new OutputView();

    public void run() {
        BlackJackGame blackJackGame = initBlackJackGame();
        bettingPlayers(blackJackGame);
        printInitialStatus(blackJackGame);
        playPlayerTurn(blackJackGame);
        playDealerTurn(blackJackGame);
        blackJackGame.judgeResults();
        printCardResult(blackJackGame);
        printProfit(blackJackGame);
    }

    private BlackJackGame initBlackJackGame() {
        outputView.printPlayerNameRequestMessage();
        return RepeatValidator.readUntilValidate(() -> {
            final List<String> names = inputView.readPlayerNames();
            outputView.printLineBreak();
            return new BlackJackGame(names, new RandomDeckGenerator());
        });
    }

    private void bettingPlayers(BlackJackGame blackJackGame) {
        final List<String> playerNames = blackJackGame.getPlayerNames();

        for (String name : playerNames) {
            RepeatValidator.runUntilValidate(() -> bettingPlayer(blackJackGame, name));
        }
    }

    private void bettingPlayer(final BlackJackGame blackJackGame, final String name) {
        outputView.printPlayerBettingAmountRequestMessage(name);
        final int amount = inputView.readBettingMoneyAmount();
        blackJackGame.bet(name, amount);
    }

    private void printInitialStatus(BlackJackGame blackJackGame) {
        final List<HoldingCards> initialHoldingCards = new ArrayList<>();
        initialHoldingCards.add(new HoldingCards(
                DEALER_NAME_CODE,
                blackJackGame.getInitialHoldingCards(DEALER_NAME_CODE))
        );
        for (String name : blackJackGame.getPlayerNames()) {
            initialHoldingCards.add(new HoldingCards(name, blackJackGame.getInitialHoldingCards(name)));
        }

        outputView.printInitialHoldingCards(initialHoldingCards);
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
            final HoldingCards holdingCards = new HoldingCards(name, blackJackGame.getHandholdingCards(name));
            outputView.printCards(holdingCards);
        }
    }

    private boolean isContinuous(String name, BlackJackGame blackJackGame) {
        if (blackJackGame.isPossibleToDraw(name)) {
            outputView.printDrawCardRequestMessage(name);
            return RepeatValidator.readUntilValidate(() -> DrawInput.from(inputView.readDrawOrStay()).isDraw());
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
        final List<NameCardAndScore> cardAndScoreResults = getCardResults(blackJackGame);

        outputView.printCardAndScoreResult(cardAndScoreResults);
        outputView.printLineBreak();
    }

    private static List<NameCardAndScore> getCardResults(final BlackJackGame blackJackGame) {
        final List<NameCardAndScore> cardAndScoreResults = new ArrayList<>();
        final CardAndScore dealerCardAndScore = blackJackGame.getCardAndScore(DEALER_NAME_CODE);
        cardAndScoreResults.add(new NameCardAndScore(DEALER_NAME_CODE, dealerCardAndScore.getCards(), dealerCardAndScore.getScore()));
        for (String name : blackJackGame.getPlayerNames()) {
            final CardAndScore cardAndScore = blackJackGame.getCardAndScore(name);
            cardAndScoreResults.add(new NameCardAndScore(name, cardAndScore.getCards(), cardAndScore.getScore()));
        }
        return cardAndScoreResults;
    }

    private void printProfit(final BlackJackGame blackJackGame) {
        final List<ProfitResult> profitResults = new ArrayList<>();
        profitResults.add(new ProfitResult(DEALER_NAME_CODE, blackJackGame.getDealerProfitAmount()));

        for (String name : blackJackGame.getPlayerNames()) {
            profitResults.add(new ProfitResult(name, blackJackGame.getPlayerProfitAmount(name)));
        }
        outputView.printProfitResult(profitResults);
    }
}
