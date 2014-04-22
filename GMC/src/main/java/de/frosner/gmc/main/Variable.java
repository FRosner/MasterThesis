package de.frosner.gmc.main;

import net.sf.qualitycheck.Check;

public class Variable {

	private final String _name;

	public Variable(String name) {
		_name = Check.notNull(name);
	}

	@Override
	public String toString() {
		return _name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_name == null) ? 0 : _name.hashCode());
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
		if (getClass() != obj.getClass()) {
			return false;
		}
		Variable other = (Variable) obj;
		return _name.equals(other._name);
	}

}
