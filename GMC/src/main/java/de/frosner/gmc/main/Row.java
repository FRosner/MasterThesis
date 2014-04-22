package de.frosner.gmc.main;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.sf.qualitycheck.Check;

import org.apache.commons.math3.util.Precision;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

public class Row {

	public static class RowBuilder {

		private final double _probability;
		private final Map<Variable, Integer> _observations;

		public RowBuilder(double probability) {
			_probability = probability;
			_observations = Maps.newHashMap();
		}

		public RowBuilder withColumn(Variable variable, Integer value) {
			_observations.put(variable, value);
			return this;
		}

		public Row build() {
			return new Row(_probability, _observations);
		}

	}

	public static RowBuilder builder(double probability) {
		return new RowBuilder(probability);
	}

	public static Row withOneVariable(double probability, Variable variable, Integer observation) {
		Map<Variable, Integer> observations = Maps.newHashMap();
		observations.put(variable, observation);
		return new Row(probability, observations);
	}

	private final double _probability;
	private final Map<Variable, Integer> _observations;

	public Row(double probability, Map<Variable, Integer> observations) {
		Check.stateIsTrue(probability >= 0 && probability <= 1, "probability must be in (0,1)");
		Check.notEmpty(observations, "variables");
		_probability = probability;
		_observations = ImmutableMap.copyOf(observations);
	}

	public double getProbability() {
		return _probability;
	}

	public Integer getObservation(Variable of) {
		Check.contains(_observations.keySet(), of);
		return _observations.get(of);
	}

	public Set<Entry<Variable, Integer>> getVariableObservations() {
		return _observations.entrySet();
	}

	@Override
	public String toString() {
		return "Row [_probability=" + _probability + ", _observations=" + _observations + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_observations == null) ? 0 : _observations.hashCode());
		long temp;
		temp = Double.doubleToLongBits(_probability);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		if (!(obj instanceof Row)) {
			return false;
		}
		Row other = (Row) obj;
		return Precision.equals(_probability, other._probability, 0.00000001)
				&& _observations.equals(other._observations);
	}
}
