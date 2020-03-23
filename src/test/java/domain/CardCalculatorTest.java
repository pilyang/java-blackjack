package domain;

import domain.card.Card;
import domain.card.CardNumber;
import domain.card.CardSuitSymbol;
import domain.card.Cards;
import domain.player.Dealer;
import domain.player.Player;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class CardCalculatorTest {
    @DisplayName("카드의 총합 계산 테스트")
    @Test
    void getCardsSumTest() {
        Cards cardsWithAceForEleven = Cards.of(new ArrayList<>(Arrays.asList(
                Card.of(CardNumber.ACE, CardSuitSymbol.CLUB),
                Card.of(CardNumber.SEVEN, CardSuitSymbol.CLUB))));
        Cards cardsWithAceForOne = Cards.of(new ArrayList<>(Arrays.asList(
                Card.of(CardNumber.ACE, CardSuitSymbol.CLUB),
                Card.of(CardNumber.SEVEN, CardSuitSymbol.CLUB),
                Card.of(CardNumber.KING, CardSuitSymbol.SPACE),
                Card.of(CardNumber.THREE, CardSuitSymbol.DIAMOND))));
        Cards cards = Cards.of(new ArrayList<>(Arrays.asList(
                Card.of(CardNumber.QUEEN, CardSuitSymbol.CLUB),
                Card.of(CardNumber.SEVEN, CardSuitSymbol.CLUB))));

        Assertions.assertThat(CardCalculator.calculateAceStrategy(cardsWithAceForEleven)).isEqualTo(18);
        Assertions.assertThat(CardCalculator.calculateAceStrategy(cardsWithAceForOne)).isEqualTo(21);
        Assertions.assertThat(CardCalculator.calculateAceStrategy(cards)).isEqualTo(17);
    }

    @DisplayName("유저가 블랙잭일 때 유저의 승")
    @Test
    void userBlackJackTest() {
        Player player = new Player("lavine", new ArrayList<>(Arrays.asList(
                Card.of(CardNumber.ACE, CardSuitSymbol.DIAMOND),
                Card.of(CardNumber.KING, CardSuitSymbol.SPACE))),
                10_000);
        Dealer dealer = new Dealer(new ArrayList<>(Arrays.asList(
                Card.of(CardNumber.ACE, CardSuitSymbol.DIAMOND),
                Card.of(CardNumber.KING, CardSuitSymbol.SPACE))));

        Assertions.assertThat(CardCalculator.determineWinner(player.getCard(), dealer.getCard()))
                .isTrue();
    }

    @DisplayName("딜러만 블랙잭일 때 딜러의 승")
    @Test
    void dealerBlackJackTest() {
        Player player = new Player("lavine", new ArrayList<>(Arrays.asList(
                Card.of(CardNumber.ACE, CardSuitSymbol.DIAMOND),
                Card.of(CardNumber.THREE, CardSuitSymbol.SPACE))),
                10_000);
        Dealer dealer = new Dealer(new ArrayList<>(Arrays.asList(
                Card.of(CardNumber.ACE, CardSuitSymbol.DIAMOND),
                Card.of(CardNumber.KING, CardSuitSymbol.SPACE))));

        Assertions.assertThat(CardCalculator.determineWinner(player.getCard(), dealer.getCard()))
                .isFalse();
    }

    @DisplayName("유저와 딜러 둘 다 블랙잭이 아니고 유저의 수가 더 크면 유저의 승")
    @Test
    void userBiggerThenDealerTest() {
        Player player = new Player("lavine", new ArrayList<>(Arrays.asList(
                Card.of(CardNumber.KING, CardSuitSymbol.DIAMOND),
                Card.of(CardNumber.THREE, CardSuitSymbol.SPACE))),
                10_000);
        Dealer dealer = new Dealer(new ArrayList<>(Arrays.asList(
                Card.of(CardNumber.TWO, CardSuitSymbol.DIAMOND),
                Card.of(CardNumber.KING, CardSuitSymbol.SPACE))));

        Assertions.assertThat(CardCalculator.determineWinner(player.getCard(), dealer.getCard()))
                .isTrue();
    }

    @DisplayName("유저와 딜러 둘 다 블랙잭이 아니고 딜러의 수가 더 크면 유저의 패")
    @Test
    void dealerBiggerThenUserTest() {
        Player player = new Player("lavine", new ArrayList<>(Arrays.asList(
                Card.of(CardNumber.KING, CardSuitSymbol.DIAMOND),
                Card.of(CardNumber.TWO, CardSuitSymbol.SPACE))),
                10_000);
        Dealer dealer = new Dealer(new ArrayList<>(Arrays.asList(
                Card.of(CardNumber.THREE, CardSuitSymbol.DIAMOND),
                Card.of(CardNumber.KING, CardSuitSymbol.SPACE))));

        Assertions.assertThat(CardCalculator.determineWinner(player.getCard(), dealer.getCard()))
                .isFalse();
    }

    @DisplayName("딜러가 21을 넘는다면 무조건 유저의 승")
    @Test
    void dealerOverBlackJackTest() {
        Player player = new Player("pobi", new ArrayList<>(Arrays.asList(
                Card.of(CardNumber.KING, CardSuitSymbol.SPACE),
                Card.of(CardNumber.ACE, CardSuitSymbol.SPACE))),
                10_000);
        Dealer dealer = new Dealer(new ArrayList<>(Arrays.asList(
                Card.of(CardNumber.KING, CardSuitSymbol.SPACE),
                Card.of(CardNumber.JACK, CardSuitSymbol.SPACE),
                Card.of(CardNumber.THREE, CardSuitSymbol.DIAMOND))));

        Assertions.assertThat(CardCalculator.determineWinner(player.getCard(), dealer.getCard()))
                .isTrue();
    }

    @DisplayName("입력한 카드 묶음의 합이 21인지 판단하는 메서드 테스트")
    @Test
    void isCardsSumBlackJackTest() {
        Cards blackJackCards = Cards.of(new ArrayList<>(Arrays.asList(
                Card.of(CardNumber.ACE, CardSuitSymbol.DIAMOND),
                Card.of(CardNumber.JACK, CardSuitSymbol.DIAMOND)
        )));
        Cards notBlackJackCards = Cards.of(new ArrayList<>(Arrays.asList(
                Card.of(CardNumber.ACE, CardSuitSymbol.CLUB),
                Card.of(CardNumber.SEVEN, CardSuitSymbol.DIAMOND)
        )));

        Assertions.assertThat(CardCalculator.isMaxCardsSum(blackJackCards)).isTrue();
        Assertions.assertThat(CardCalculator.isMaxCardsSum(notBlackJackCards)).isFalse();
    }

    @DisplayName("입력한 카드의 합이 21 미만인지 판단하는 메서드 테스트")
    @Test
    void isPlayerCardSumUnderBlackJackTest() {
        Cards underBlackJack = Cards.of(new ArrayList<>(Arrays.asList(
                Card.of(CardNumber.JACK, CardSuitSymbol.DIAMOND),
                Card.of(CardNumber.FOUR, CardSuitSymbol.DIAMOND),
                Card.of(CardNumber.TWO, CardSuitSymbol.DIAMOND)
        )));
        Cards overBlackJack = Cards.of(new ArrayList<>(Arrays.asList(
                Card.of(CardNumber.NINE, CardSuitSymbol.CLUB),
                Card.of(CardNumber.EIGHT, CardSuitSymbol.CLUB),
                Card.of(CardNumber.SEVEN, CardSuitSymbol.CLUB)
        )));

        Assertions.assertThat(CardCalculator.isUnderMaxCardsSum(underBlackJack)).isTrue();
        Assertions.assertThat(CardCalculator.isUnderMaxCardsSum(overBlackJack)).isFalse();
    }

    @DisplayName("입력한 카드의 합이 16이하인지 판단하는 메서드 테스트")
    @Test
    void isDealerCardsSumUnderSixteenTest() {
        Cards underSixteen = Cards.of(new ArrayList<>(Arrays.asList(
                Card.of(CardNumber.FOUR, CardSuitSymbol.DIAMOND),
                Card.of(CardNumber.TWO, CardSuitSymbol.DIAMOND)
        )));
        Cards overSixteen = Cards.of(new ArrayList<>(Arrays.asList(
                Card.of(CardNumber.JACK, CardSuitSymbol.CLUB),
                Card.of(CardNumber.SEVEN, CardSuitSymbol.CLUB)
        )));

        Assertions.assertThat(CardCalculator.isUnderDealerStandard(underSixteen)).isTrue();
        Assertions.assertThat(CardCalculator.isUnderDealerStandard(overSixteen)).isFalse();
    }
}