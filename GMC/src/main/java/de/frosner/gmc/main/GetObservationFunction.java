package de.frosner.gmc.main;

import java.util.function.Function;

import net.sf.qualitycheck.Check;

public class GetObservationFunction implements Function<Row, Integer> {

	private final Variable _of;

	public GetObservationFunction(Variable of) {
		_of = Check.notNull(of);
	}

	@Override
	public Integer apply(Row t) {
		return t.getObservation(_of);
	}

}
