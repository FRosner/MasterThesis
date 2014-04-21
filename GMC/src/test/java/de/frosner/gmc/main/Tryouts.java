package de.frosner.gmc.main;

import static java.util.stream.Collectors.averagingDouble;
import static java.util.stream.Collectors.groupingBy;
import static org.fest.assertions.Assertions.assertThat;

import java.awt.Button;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class Tryouts {

	private static class Person {

		private final String _name;
		private final String _department;
		private final double _salary;

		public Person(String name, String department, double salary) {
			_name = name;
			_department = department;
			_salary = salary;
		}

		public String getName() {
			return _name;
		}

		public String getDepartment() {
			return _department;
		}

		public double getSalary() {
			return _salary;
		}

		@Override
		public String toString() {
			return "Person [_name=" + _name + ", _department=" + _department + ", _salary=" + _salary + "]";
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((_department == null) ? 0 : _department.hashCode());
			result = prime * result + ((_name == null) ? 0 : _name.hashCode());
			long temp;
			temp = Double.doubleToLongBits(_salary);
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
			if (!(obj instanceof Person)) {
				return false;
			}
			Person other = (Person) obj;
			if (_department == null) {
				if (other._department != null) {
					return false;
				}
			} else if (!_department.equals(other._department)) {
				return false;
			}
			if (_name == null) {
				if (other._name != null) {
					return false;
				}
			} else if (!_name.equals(other._name)) {
				return false;
			}
			if (Double.doubleToLongBits(_salary) != Double.doubleToLongBits(other._salary)) {
				return false;
			}
			return true;
		}

	}

	@Test
	public void testAggregation() {
		List<Person> lists = Lists.newArrayList(new Person("Frank Rosner", "IT", 50000d), new Person("Rick Moritz",
				"IT", 60000d), new Person("Elisabeth Weise", "Bio", 40000d), new Person("Heidrun Rosner", "Management",
				80000d), new Person("Bernd Weigel", "Management", 70000d));

		assertThat(lists.stream().mapToDouble(Person::getSalary).sum()).isEqualTo(300000);

		Map<String, Double> expectedAverageSalaries = Maps.newHashMap();
		expectedAverageSalaries.put("IT", 55000D);
		expectedAverageSalaries.put("Bio", 40000D);
		expectedAverageSalaries.put("Management", 75000d);
		assertThat(lists.stream().collect(groupingBy(Person::getDepartment, averagingDouble(Person::getSalary))))
				.isEqualTo(expectedAverageSalaries);
	}

	@Test
	public void testParallelVsSequential() {
		List<Double> doubles = Lists.newArrayList();
		for (int i = 0; i < 10000000; i++) {
			doubles.add((double) i);
		}
		Stream<Double> doublesStream;
		double result;

		Date startLoop = new Date();
		result = 0d;
		for (Double double1 : doubles) {
			result += double1;
		}
		Date endLoop = new Date();
		System.err.println("Sequential Loop: " + (endLoop.getTime() - startLoop.getTime()) + " (" + result + ")");

		doublesStream = doubles.stream();
		Date startSequential = new Date();
		result = doublesStream.reduce((a, b) -> a + b).get();
		Date endSequential = new Date();
		System.err.println("Sequential Stream (reduce): " + (endSequential.getTime() - startSequential.getTime())
				+ " (" + result + ")");

		doublesStream = doubles.parallelStream();
		Date startParallel = new Date();
		result = doublesStream.reduce((a, b) -> a + b).get();
		Date endParallel = new Date();
		System.err.println("Parallel Stream (reduce): " + (endParallel.getTime() - startParallel.getTime()) + " ("
				+ result + ")");

		doublesStream = doubles.stream();
		Date startSequentialSum = new Date();
		result = doublesStream.mapToDouble(Double::valueOf).sum();
		Date endSequentialSum = new Date();
		System.err.println("Sequential Stream (sum): " + (endSequentialSum.getTime() - startSequentialSum.getTime())
				+ " (" + result + ")");

		for (int i = 0; i < 10; i++) {
			doublesStream = doubles.parallelStream();
			Date startParallelSum = new Date();
			result = doublesStream.mapToDouble(Double::valueOf).sum();
			Date endParallelSum = new Date();
			System.err.println("Parallel Stream (sum): " + (endParallelSum.getTime() - startParallelSum.getTime())
					+ " (" + result + ")");
		}

	}

	@Test
	public void testButtons() {
		Button button1 = new Button();
		Button button2 = new Button();
		List<Button> buttons = Lists.newArrayList(button1, button2);

		buttons.forEach(button -> assertThat(button.isEnabled()).isTrue());
		buttons.forEach(button -> button.setEnabled(false));
		buttons.forEach(button -> assertThat(button.isEnabled()).isFalse());
	}

}
