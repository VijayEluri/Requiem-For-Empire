package com.dafrito.rfe.script.operations;

import com.dafrito.rfe.inspect.Nodeable;
import com.dafrito.rfe.logging.Logs;
import com.dafrito.rfe.script.ScriptEnvironment;
import com.dafrito.rfe.script.exceptions.ScriptException;
import com.dafrito.rfe.script.parsing.Referenced;
import com.dafrito.rfe.script.parsing.ScriptElement;
import com.dafrito.rfe.script.parsing.ScriptKeywordType;
import com.dafrito.rfe.script.values.ScriptValue;
import com.dafrito.rfe.script.values.ScriptValueType;
import com.dafrito.rfe.script.values.ScriptValue_Variable;

public class ScriptExecutable_RetrieveCurrentObject extends ScriptValue_Variable implements ScriptExecutable, ScriptValue, Nodeable, Referenced {
	private ScriptElement reference;

	public ScriptExecutable_RetrieveCurrentObject(Referenced ref, ScriptValueType type) throws ScriptException {
		super(ref.getEnvironment(), type, ScriptKeywordType.PRIVATE);
		this.reference = ref.getDebugReference();
	}

	@Override
	public ScriptValue castToType(Referenced ref, ScriptValueType type) throws ScriptException {
		return this.getVariable().castToType(ref, type);
	}

	// ScriptExecutable implementation
	@Override
	public ScriptValue execute() throws ScriptException {
		return this.getValue();
	}

	// Referenced implementation
	@Override
	public ScriptElement getDebugReference() {
		return this.reference;
	}

	@Override
	public ScriptEnvironment getEnvironment() {
		return this.getDebugReference().getEnvironment();
	}

	// Overloaded ScriptValue_Variable functions
	@Override
	public ScriptKeywordType getPermission() throws ScriptException {
		return ScriptKeywordType.PRIVATE;
	}

	@Override
	public ScriptValue getValue() throws ScriptException {
		return this.getVariable().getValue();
	}

	public ScriptValue_Variable getVariable() throws ScriptException {
		assert Logs.addNode("Executing Current Object Retrieval");
		return new ScriptValue_Variable(this.getEnvironment(), this.getType(), this.getEnvironment().getCurrentObject(), this.getPermission());
	}

	// Abstract-value implementation
	@Override
	public boolean isConvertibleTo(ScriptValueType type) {
		return ScriptValueType.isConvertibleTo(this.getEnvironment(), this.getType(), type);
	}

	@Override
	public void nodificate() {
		assert Logs.openNode("Current Object Placeholder");
		super.nodificate();
		assert Logs.closeNode();
	}

	@Override
	public ScriptValue setReference(Referenced ref, ScriptValue value) throws ScriptException {
		return this.getVariable().setReference(ref, value);
	}

	@Override
	public ScriptValue setValue(Referenced ref, ScriptValue value) throws ScriptException {
		return this.getVariable().setValue(ref, value);
	}

	@Override
	public int valuesCompare(Referenced ref, ScriptValue rhs) throws ScriptException {
		return this.getValue().valuesCompare(ref, rhs);
	}

	@Override
	public boolean valuesEqual(Referenced ref, ScriptValue rhs) throws ScriptException {
		return this.getValue().valuesEqual(ref, rhs);
	}
}
