package blackjack.domain.user;

import blackjack.domain.card.Card;
import blackjack.domain.card.CardGroup;

import java.util.List;
import java.util.stream.Collectors;

public class Player extends User {

    public Player(String name, CardGroup cardGroup) {
        super(name, cardGroup);
    }

    @Override
    protected List<Card> getInitialStatus() {
        return getStatus().stream()
                .limit(2)
                .collect(Collectors.toUnmodifiableList());
    }
}
