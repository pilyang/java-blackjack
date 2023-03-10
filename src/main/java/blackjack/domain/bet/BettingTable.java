package blackjack.domain.bet;

import java.util.HashMap;
import java.util.Map;

public class BettingTable {

    private static final int UNIT_AMOUNT = 1_000;
    private static final int MIN_AMOUNT = 1_000;
    private static final int MAX_AMOUNT = 100_000;
    static final String OUT_OF_RANGE_EXCEPTION_MESSAGE = MIN_AMOUNT + "이상 " + MAX_AMOUNT + "이하여야 합니다.";
    static final String UNIT_AMOUNT_EXCEPTION_TEST = UNIT_AMOUNT + "원 단위로 입력해야 합니다.";

    private final Map<String, Money> table;

    public BettingTable() {
        table = new HashMap<>();
    }

    public void bet(final String name, final Money money) {
        validateRange(money);
        validateUnitAmount(money);
        table.put(name, money);
    }

    private void validateUnitAmount(final Money money) {
        if (money.getAmount() % UNIT_AMOUNT != 0) {
            throw new IllegalArgumentException(UNIT_AMOUNT_EXCEPTION_TEST);
        }
    }

    private void validateRange(final Money money) {
        if (money.getAmount() < MIN_AMOUNT || money.getAmount() > MAX_AMOUNT) {
            throw new IllegalArgumentException(OUT_OF_RANGE_EXCEPTION_MESSAGE);
        }
    }

    public Money get(final String name) {
        return table.getOrDefault(name, new Money(0));
    }
}
