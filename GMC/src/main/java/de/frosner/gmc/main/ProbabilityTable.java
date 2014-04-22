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

	private final List<Row> _probabilities;

	public ProbabilityTable(List<Row> table) {
		Check.notEmpty(Check.noNullElements(table, "table"), "table");
		_probabilities = ImmutableList.copyOf(table);
	}

	public List<Row> getRows() {
		return _probabilities;
	}

	public ProbabilityTable groupBySum(Variable by) {
		Map<Integer, Double> result = _probabilities.parallelStream().collect(
				groupingBy(new GetObservationFunction(by), summingDouble(Row::getProbability)));

		List<Row> groupedByTable = Lists.newArrayList();
		for (Entry<Integer, Double> row : result.entrySet()) {
			Map<Variable, Integer> groupedByObservations = Maps.newHashMap();
			groupedByObservations.put(by, row.getKey());
			groupedByTable.add(new Row(row.getValue(), groupedByObservations));
		}

		return new ProbabilityTable(groupedByTable);
	}

}
