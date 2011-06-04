package com.dafrito.rfe.script.operations;

import com.dafrito.rfe.gui.debug.Debugger;
import com.dafrito.rfe.inspect.Nodeable;
import com.dafrito.rfe.script.exceptions.Exception_Nodeable;
import com.dafrito.rfe.script.parsing.Referenced;
import com.dafrito.rfe.script.parsing.ScriptElement;
import com.dafrito.rfe.script.values.ScriptValue;
import com.dafrito.rfe.script.values.ScriptValueType;

public class ScriptExecutable_AssignValue extends ScriptElement implements ScriptExecutable, ScriptValue, Nodeable {
	private ScriptValue variable;
	private ScriptValue value;

	public ScriptExecutable_AssignValue(Referenced ref, ScriptValue lhs, ScriptValue rhs) {
		super(ref);
		this.variable = lhs;
		this.value = rhs;
	}

	@Override
	public ScriptValue castToType(Referenced ref, ScriptValueType type) throws Exception_Nodeable {
		return this.variable.castToType(ref, type);
	}

	// ScriptExecutable implementation
	@Override
	public ScriptValue execute() throws Exception_Nodeable {
		assert Debugger.openNode("Value-Assignment Expressions", "Assigning Value");
		assert Debugger.addSnapNode("Left variable", this.variable);
		assert Debugger.openNode("Retrieving value");
		assert Debugger.addSnapNode("Current value", this.value);
		ScriptValue value = this.value.getValue();
		assert Debugger.closeNode();
		assert Debugger.addSnapNode("Right value", value);
		value = this.variable.setValue(this, value);
		assert Debugger.closeNode();
		return value;
	}

	public ScriptValue getLeft() {
		return this.variable;
	}

	// ScriptValue_Abstract implementation
	@Override
	public ScriptValueType getType() {
		return this.variable.getType();
	}

	@Override
	public ScriptValue getValue() throws Exception_Nodeable {
		return this.execute();
	}

	@Override
	public boolean isConvertibleTo(ScriptValueType type) {
		return this.variable.isConvertibleTo(type);
	}

	// Nodeable implementation
	@Override
	public void nodificate() {
		assert Debugger.openNode("Assignment Script Expression");
		assert Debugger.addSnapNode("Left variable", this.variable);
		assert Debugger.addSnapNode("Right value", this.value);
		assert Debugger.closeNode();
	}

	@Override
	public ScriptValue setValue(Referenced ref, ScriptValue value) throws Exception_Nodeable {
		assert Debugger.openNode("Value Assignments", "Setting assigment-expression's right-side value");
		assert Debugger.addSnapNode("Former value", this.value);
		this.value = value.castToType(this, this.getType());
		assert Debugger.addSnapNode("New value", this.value);
		ScriptValue returning = this.execute();
		assert Debugger.closeNode();
		return returning;
	}

	@Override
	public int valuesCompare(Referenced ref, ScriptValue rhs) throws Exception_Nodeable {
		return this.variable.valuesCompare(ref, rhs);
	}

	@Override
	public boolean valuesEqual(Referenced ref, ScriptValue rhs) throws Exception_Nodeable {
		return this.variable.valuesEqual(ref, rhs);
	}
}