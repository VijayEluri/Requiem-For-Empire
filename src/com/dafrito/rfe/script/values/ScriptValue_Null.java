package com.dafrito.rfe.script.values;

import com.dafrito.rfe.gui.debug.Debugger;
import com.dafrito.rfe.inspect.Nodeable;
import com.dafrito.rfe.script.exceptions.Exception_InternalError;
import com.dafrito.rfe.script.exceptions.ScriptException;
import com.dafrito.rfe.script.exceptions.Exception_Nodeable_IncomparableObjects;
import com.dafrito.rfe.script.operations.ScriptExecutable;
import com.dafrito.rfe.script.parsing.Referenced;
import com.dafrito.rfe.script.parsing.ScriptElement;

public class ScriptValue_Null extends ScriptElement implements ScriptExecutable, ScriptValue, Nodeable {
	public ScriptValue_Null(Referenced ref) {
		super(ref);
	}

	@Override
	public ScriptValue castToType(Referenced ref, ScriptValueType type) throws ScriptException {
		return this;
	}

	@Override
	public ScriptValue execute() throws ScriptException {
		return this;
	}

	// ScriptValue_Abstract implementation
	@Override
	public ScriptValueType getType() {
		return ScriptValueType.VOID;
	}

	@Override
	public ScriptValue getValue() throws ScriptException {
		return this;
	}

	@Override
	public boolean isConvertibleTo(ScriptValueType type) {
		return true;
	}

	@Override
	public void nodificate() {
		assert Debugger.addNode("Null Script-Value");
	}

	@Override
	public ScriptValue setValue(Referenced ref, ScriptValue value) throws ScriptException {
		throw new Exception_InternalError("Set Value");
	}

	@Override
	public int valuesCompare(Referenced ref, ScriptValue rhs) throws ScriptException {
		throw new Exception_Nodeable_IncomparableObjects(ref, this, rhs);
	}

	@Override
	public boolean valuesEqual(Referenced ref, ScriptValue rhs) throws ScriptException {
		return (rhs == null || rhs.getValue() == null || rhs.getValue() instanceof ScriptValue_Null);
	}
}
