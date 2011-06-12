/**
 * 
 */
package com.dafrito.rfe.script.exceptions;

import com.dafrito.rfe.gui.debug.Debugger;
import com.dafrito.rfe.script.ScriptEnvironment;
import com.dafrito.rfe.script.parsing.Referenced;
import com.dafrito.rfe.script.values.ScriptFunction;

public class Exception_Nodeable_IllegalNullReturnValue extends ScriptException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6453503758041260366L;
	private ScriptFunction function;

	public Exception_Nodeable_IllegalNullReturnValue(Referenced ref, ScriptFunction fxn) {
		super(ref);
		this.function = fxn;
	}

	public Exception_Nodeable_IllegalNullReturnValue(ScriptEnvironment env, ScriptFunction fxn) {
		super(env);
		this.function = fxn;
	}

	@Override
	public void getExtendedInformation() {
		assert Debugger.addSnapNode("This function is attempting to return implicitly, even though it is of type, " + this.function.getReturnType(), this.function);
	}

	@Override
	public String getName() {
		return "Illegal Null Return Value";
	}
}