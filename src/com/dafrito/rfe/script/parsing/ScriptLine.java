package com.dafrito.rfe.script.parsing;

import com.dafrito.rfe.gui.debug.CommonString;
import com.dafrito.rfe.gui.debug.Debugger;
import com.dafrito.rfe.inspect.Inspectable;
import com.dafrito.rfe.inspect.Nodeable;
import com.dafrito.rfe.script.ScriptEnvironment;
import com.dafrito.rfe.script.parsing.tokens.RiffToken;
import com.dafrito.rfe.script.parsing.tokens.RiffTokenVisitor;

@Inspectable
public class ScriptLine extends ScriptElement implements Nodeable, RiffToken {
	private String string;

	public ScriptLine(ScriptEnvironment env, String filename, int num, String string) {
		super(env, filename, num, string, string.length());
		this.string = string;
	}

	public ScriptLine(String string, ScriptLine otherLine, int oLO) {
		super(otherLine, oLO, string.length());
		this.string = string;
	}

	@Inspectable
	public String getString() {
		return this.string;
	}

	@Override
	public void nodificate() {
		assert Debugger.openNode(CommonString.SCRIPTLINE.getText() + this.string);
		assert Debugger.closeNode();
	}

	public void setString(String string) {
		this.string = string;
	}

	@Override
	public String toString() {
		return this.getFilename() + "@" + this.getLineNumber() + ": \"" + this.string + '"';
	}

	@Override
	public void accept(RiffTokenVisitor visitor) {
		visitor.visitUnparsed(this.getString());
	}
}
