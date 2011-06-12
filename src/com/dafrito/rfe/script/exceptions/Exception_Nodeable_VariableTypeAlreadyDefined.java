/**
 * 
 */
package com.dafrito.rfe.script.exceptions;

import com.dafrito.rfe.gui.debug.Debugger;
import com.dafrito.rfe.script.parsing.Referenced;

public class Exception_Nodeable_VariableTypeAlreadyDefined extends ScriptException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3105141324838401870L;
	private String type;

	public Exception_Nodeable_VariableTypeAlreadyDefined(Referenced ref, String type) {
		super(ref);
		this.type = type;
	}

	@Override
	public void getExtendedInformation() {
		assert Debugger.addNode("The variable type, " + this.type + ", has already been defined");
	}

	@Override
	public String getName() {
		return "Predefined Variable-Type (" + this.type + ")";
	}
}