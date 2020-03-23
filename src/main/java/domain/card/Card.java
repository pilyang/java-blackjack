package domain.card;

import java.util.ArrayList;
import java.util.List;

public class Card {
    private static final List<Card> cards;

    private CardNumber cardNumber;
    private CardSuitSymbol cardSuitSymbol;

    static {
        cards = new ArrayList<>();
        for (CardNumber cardNumber : CardNumber.values()) {
            addCard(cardNumber);
        }
    }

    private static void addCard(CardNumber cardNumber) {
        for (CardSuitSymbol cardSuitSymbol : CardSuitSymbol.values()) {
            cards.add(new Card(cardNumber, cardSuitSymbol));
        }
    }

    private Card(CardNumber cardNumber, CardSuitSymbol cardSuitSymbol) {
        this.cardNumber = cardNumber;
        this.cardSuitSymbol = cardSuitSymbol;
    }

    public static Card of(CardNumber cardNumber, CardSuitSymbol cardSuitSymbol) {
        return cards.stream()
                .filter(card -> card.isSameCard(cardNumber, cardSuitSymbol))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public static List<Card> getCards() {
        return new ArrayList<>(cards);
    }

    private boolean isSameCard(CardNumber cardNumber, CardSuitSymbol cardSuitSymbol) {
        return this.cardNumber.equals(cardNumber) && this.cardSuitSymbol.equals(cardSuitSymbol);
    }

    public boolean isAce() {
        return this.cardNumber.isAce();
    }

    public int getCardNumber() {
        return this.cardNumber.getCardNumber();
    }

    @Override
    public String toString() {
        return getCardNumber() + this.cardSuitSymbol.getSuitSymbol();
    }
}