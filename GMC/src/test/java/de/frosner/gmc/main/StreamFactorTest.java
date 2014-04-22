package de.frosner.gmc.main;

import static org.fest.assertions.Assertions.assertThat;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class StreamFactorTest {

	private StreamFactor _table;
	private Variable _x;
	private Variable _y;

	@Before
	public void createFactor() {
		_x = new Variable("X");
		_y = new Variable("Y");
		List<Double> probabilities = Lists.newArrayList(0.1, 0.9, 0.4, 0.6);
		List<Row> rawTable = Lists.newArrayList();
		Map<Variable, Integer> observations = Maps.newHashMap();
		for (int xValue = 0; xValue <= 1; xValue++) {
			for (int yValue = 0; yValue <= 1; yValue++) {
				observations.put(_x, xValue);
				observations.put(_y, yValue);
				rawTable.add(new Row(probabilities.get(yValue + 2 * xValue), observations));
				observations.clear();
			}
		}
		_table = new StreamFactor(rawTable);
	}

	@Test
	public void testGroupSum() {
		StreamFactor groupedByX = _table.groupSumBy(_x);
		assertThat(groupedByX.getRows()).containsExactly(Row.withOneVariable(1, _x, 0), Row.withOneVariable(1, _x, 1));
	}

	@Test
	public void testJoinWith() {
		StreamFactor toBeJoined = new StreamFactor(Lists.newArrayList(Row.withOneVariable(0.2, _x, 0),
				Row.withOneVariable(0.8, _x, 1)));

		List<Row> expected = Lists.newArrayList();
		expected.add(Row.builder(0.02).withColumn(_x, 0).withColumn(_y, 0).build());
		expected.add(Row.builder(0.18).withColumn(_x, 0).withColumn(_y, 1).build());
		expected.add(Row.builder(0.32).withColumn(_x, 1).withColumn(_y, 0).build());
		expected.add(Row.builder(0.48).withColumn(_x, 1).withColumn(_y, 1).build());
		assertThat(_table.joinWith(toBeJoined, _x)).isEqualTo(new StreamFactor(expected));
		assertThat(toBeJoined.joinWith(_table, _x)).isEqualTo(new StreamFactor(expected));
	}

}
