package blackjack.domain.player;

import static org.assertj.core.api.Assertions.assertThat;

import blackjack.domain.card.Deck;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class DealerTest {

    @Test
    @DisplayName("딜러가 16이하일 때만 카드 추가하는지 확인")
    void checkHitAddCardTest() {
        Deck deck = new Deck();
        Dealer dealer = new Dealer(deck);
        while (dealer.isHittable()) {
            dealer.hit(deck.pick());
        }
        int dealerScore = dealer.getCards().calculateScore();
        assertThat(dealerScore > 16).isTrue();
    }

    @Test
    @DisplayName("딜러가 카드를 한장만 보여주는지 확인")
    void showDealerCards() {
        Deck deck = new Deck();
        Dealer dealer = new Dealer(deck);
        assertThat(dealer.getShowCards().getCardValues().size())
                .isEqualTo(1);
    }
}
