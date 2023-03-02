package blackjack.domain;

import java.util.ArrayList;
import java.util.List;

public class CardGroup {

    private final List<Card> cards = new ArrayList<>();

    public CardGroup(final Card firstCard, final Card secondCard){
        cards.add(firstCard);
        cards.add(secondCard);
    }

    public int getTotalValue() {
        return cards.stream().map(Card::getNumber)
                .mapToInt(CardNumber::getValue)
                .sum();
    }
}
