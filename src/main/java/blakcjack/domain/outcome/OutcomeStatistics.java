package blakcjack.domain.outcome;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class OutcomeStatistics {
	public static final int INITIAL_VALUE = 0;

	private final Map<String, Outcome> playersOutcome;
	private final Map<Outcome, Integer> dealerOutcome = new LinkedHashMap<>();

	public OutcomeStatistics(final Map<String, Outcome> playersOutcome) {
		this.playersOutcome = playersOutcome;
		initializeDealerOutcome(dealerOutcome);
		aggregateDealerOutcome();
	}

	private void aggregateDealerOutcome() {
		for (final String playerName : playersOutcome.keySet()) {
			final Outcome playerOutcome = playersOutcome.get(playerName);
			dealerOutcome.computeIfPresent(playerOutcome.getCounterpartOutcome(), (outcome, count) -> count + 1);
		}
	}

	private void initializeDealerOutcome(final Map<Outcome, Integer> dealerOutcome) {
		for (Outcome outcome : Outcome.values()) {
			dealerOutcome.put(outcome, INITIAL_VALUE);
		}
	}

	public Map<String, Outcome> getPlayersOutcome() {
		return playersOutcome;
	}

	public Map<Outcome, Integer> getDealerOutcome() {
		return dealerOutcome;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		final OutcomeStatistics that = (OutcomeStatistics) o;
		return Objects.equals(dealerOutcome, that.dealerOutcome) && Objects.equals(playersOutcome, that.playersOutcome);
	}

	@Override
	public int hashCode() {
		return Objects.hash(dealerOutcome, playersOutcome);
	}
}