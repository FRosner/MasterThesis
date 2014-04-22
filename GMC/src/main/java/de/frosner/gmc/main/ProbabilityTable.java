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

public class ProbabilityTable extends ProbabilitySource {

	public static ProbabilityTable withOneVariable(Variable variable, Map<Integer, Double> probabilities) {
		Check.notNull(variable, "variable");
		Check.notEmpty(probabilities);

		List<Row> rawTable = Lists.newArrayList();
		for (Entry<Integer, Double> row : probabilities.entrySet()) {
			Map<Variable, Integer> observations = Maps.newHashMap();
			observations.put(variable, row.getKey());
			rawTable.add(new Row(row.getValue(), observations));
		}

		return new ProbabilityTable(rawTable);

	}

	private final List<Row> _probabilities;

	public ProbabilityTable(List<Row> table) {
		Check.notEmpty(Check.noNullElements(table, "table"), "table");
		_probabilities = ImmutableList.copyOf(table);
	}

	public List<Row> getRows() {
		return _probabilities;
	}

	public ProbabilityTable groupSum(Variable by) {
		Map<Integer, Double> groupSummedProbabilities = _probabilities.parallelStream().collect(
				groupingBy(new GetObservationFunction(by), summingDouble(Row::getProbability)));
		return ProbabilityTable.withOneVariable(by, groupSummedProbabilities);
	}

}
