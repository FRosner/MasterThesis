package de.frosner.gmc.main;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingDouble;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.qualitycheck.Check;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.frosner.gmc.main.Row.RowBuilder;

public class StreamFactor extends Factor {

	public static StreamFactor withOneVariable(Variable variable, Map<Integer, Double> probabilities) {
		Check.notNull(variable, "variable");
		Check.notEmpty(probabilities);

		List<Row> rawTable = Lists.newArrayList();
		for (Entry<Integer, Double> row : probabilities.entrySet()) {
			Map<Variable, Integer> observations = Maps.newHashMap();
			observations.put(variable, row.getKey());
			rawTable.add(new Row(row.getValue(), observations));
		}

		return new StreamFactor(rawTable);

	}

	private final List<Row> _probabilities;

	public StreamFactor(List<Row> table) {
		Check.notEmpty(Check.noNullElements(table, "table"), "table");
		_probabilities = ImmutableList.copyOf(table);
	}

	public List<Row> getRows() {
		return _probabilities;
	}

	public StreamFactor groupSumBy(Variable groupBy) {
		Map<Integer, Double> groupSummedProbabilities = _probabilities.parallelStream().collect(
				groupingBy(new GetObservationFunction(groupBy), summingDouble(Row::getProbability)));
		return StreamFactor.withOneVariable(groupBy, groupSummedProbabilities);
	}

	public StreamFactor joinWith(StreamFactor toBeJoined, Variable key) {
		List<Row> rawResult = Lists.newArrayList();
		for (Row thisRow : _probabilities) {
			for (Row toBeJoinedRow : toBeJoined._probabilities) {
				if (thisRow.getObservation(key).equals(toBeJoinedRow.getObservation(key))) {
					RowBuilder joinedRow = Row.builder(thisRow.getProbability() * toBeJoinedRow.getProbability());
					for (Entry<Variable, Integer> entry : thisRow.getVariableObservations()) {
						joinedRow.withColumn(entry.getKey(), entry.getValue());
					}
					for (Entry<Variable, Integer> entry : toBeJoinedRow.getVariableObservations()) {
						if (!entry.getKey().equals(key)) {
							joinedRow.withColumn(entry.getKey(), entry.getValue());
						}
					}
					rawResult.add(joinedRow.build());
				}
			}
		}
		return new StreamFactor(rawResult);
	}

	@Override
	public String toString() {
		return _probabilities.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_probabilities == null) ? 0 : _probabilities.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof StreamFactor)) {
			return false;
		}
		StreamFactor other = (StreamFactor) obj;
		return _probabilities.equals(other._probabilities);
	}
}
