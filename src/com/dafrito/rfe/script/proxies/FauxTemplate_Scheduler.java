package com.dafrito.rfe.script.proxies;

import java.util.LinkedList;
import java.util.List;

import com.dafrito.rfe.actions.Scheduler;
import com.dafrito.rfe.inspect.Nodeable;
import com.dafrito.rfe.logging.Logs;
import com.dafrito.rfe.script.Conversions;
import com.dafrito.rfe.script.ScriptConvertible;
import com.dafrito.rfe.script.ScriptEnvironment;
import com.dafrito.rfe.script.exceptions.ScriptException;
import com.dafrito.rfe.script.parsing.Referenced;
import com.dafrito.rfe.script.parsing.ScriptKeywordType;
import com.dafrito.rfe.script.values.RiffScriptFunction;
import com.dafrito.rfe.script.values.ScriptTemplate;
import com.dafrito.rfe.script.values.ScriptTemplate_Abstract;
import com.dafrito.rfe.script.values.ScriptValue;
import com.dafrito.rfe.script.values.ScriptValueType;
import com.dafrito.rfe.script.values.ScriptValue_Faux;

public class FauxTemplate_Scheduler extends FauxTemplate implements Nodeable, ScriptConvertible<Scheduler> {
	public static final String SCHEDULERSTRING = "Scheduler";
	private Scheduler scheduler;

	public FauxTemplate_Scheduler(ScriptEnvironment env) {
		super(env, ScriptValueType.createType(env, SCHEDULERSTRING), ScriptValueType.getObjectType(env), new LinkedList<ScriptValueType>(), false);
	}

	public FauxTemplate_Scheduler(ScriptEnvironment env, ScriptValueType type) {
		super(env, type);
	}

	// Nodeable implementation
	@Override
	public Scheduler convert(ScriptEnvironment env) {
		return this.scheduler;
	}

	// Function bodies are contained via a series of if statements in execute
	// Template will be null if the object is exactly of this type and is constructing, and thus must be created then
	@Override
	public ScriptValue execute(Referenced ref, String name, List<ScriptValue> params, ScriptTemplate_Abstract rawTemplate) throws ScriptException {
		assert Logs.openNode("Faux Template Executions", "Executing scheduler faux template function (" + RiffScriptFunction.getDisplayableFunctionName(name) + ")");
		FauxTemplate_Scheduler template = (FauxTemplate_Scheduler) rawTemplate;
		ScriptValue returning;
		assert Logs.addSnapNode("Template provided", template);
		assert Logs.addSnapNode("Parameters provided", params);
		if (name == null || name.equals("")) {
			if (template == null) {
				template = (FauxTemplate_Scheduler) this.createObject(ref, template);
			}
			switch (params.size()) {
			case 1:
				template.getScheduler().setDefaultListener(Conversions.getSchedulerListener(params.get(0)));
			case 0:
				assert Logs.closeNode();
				return template;
			}
		} else if (name.equals("schedule")) {
			ScriptTemplate_Abstract listener = null;
			if (params.size() == 3) {
				listener = Conversions.getTemplate(params.get(2));
			}
			template.getScheduler().schedule(Conversions.getNumber(this.getEnvironment(), params.get(0)).longValue(), Conversions.getAsset(this.getEnvironment(), params.get(1)), listener);
			assert Logs.closeNode();
			return null;
		} else if (name.equals("setDefaultListener")) {
			template.getScheduler().setDefaultListener(Conversions.getSchedulerListener(params.get(0)));
			assert Logs.closeNode();
			return null;
		} else if (name.equals("start")) {
			template.getScheduler().start();
			assert Logs.closeNode();
			return null;
		}
		returning = this.getExtendedFauxClass().execute(ref, name, params, template);
		assert Logs.closeNode();
		return returning;
	}

	public Scheduler getScheduler() {
		return this.scheduler;
	}

	// addFauxFunction(name,ScriptValueType type,List<ScriptValue_Abstract>params,ScriptKeywordType permission,boolean isAbstract)
	// All functions must be defined here. All function bodies are defined in 'execute'.
	@Override
	public void initialize() throws ScriptException {
		assert Logs.openNode("Faux Template Initializations", "Initializing scheduler faux template");
		this.addConstructor(this.getType());
		List<ScriptValue> params = new LinkedList<ScriptValue>();
		params.add(new ScriptValue_Faux(this.getEnvironment(), ScriptValueType.createType(this.getEnvironment(), FauxTemplate_SchedulerListener.SCHEDULERLISTENERSTRING)));
		this.addConstructor(this.getType(), params);
		this.disableFullCreation();
		this.getExtendedClass().initialize();
		params = new LinkedList<ScriptValue>();
		params.add(new ScriptValue_Faux(this.getEnvironment(), ScriptValueType.LONG));
		params.add(new ScriptValue_Faux(this.getEnvironment(), ScriptValueType.createType(this.getEnvironment(), FauxTemplate_Asset.ASSETSTRING)));
		this.addFauxFunction("schedule", ScriptValueType.VOID, params, ScriptKeywordType.PUBLIC, false, false);
		params = new LinkedList<ScriptValue>();
		params.add(new ScriptValue_Faux(this.getEnvironment(), ScriptValueType.LONG));
		params.add(new ScriptValue_Faux(this.getEnvironment(), ScriptValueType.createType(this.getEnvironment(), FauxTemplate_Asset.ASSETSTRING)));
		params.add(new ScriptValue_Faux(this.getEnvironment(), ScriptValueType.createType(this.getEnvironment(), FauxTemplate_SchedulerListener.SCHEDULERLISTENERSTRING)));
		this.addFauxFunction("schedule", ScriptValueType.VOID, params, ScriptKeywordType.PUBLIC, false, false);
		params = new LinkedList<ScriptValue>();
		params.add(new ScriptValue_Faux(this.getEnvironment(), ScriptValueType.createType(this.getEnvironment(), FauxTemplate_SchedulerListener.SCHEDULERLISTENERSTRING)));
		this.addFauxFunction("setDefaultListener", ScriptValueType.VOID, params, ScriptKeywordType.PUBLIC, false, false);
		params = new LinkedList<ScriptValue>();
		this.addFauxFunction("start", ScriptValueType.VOID, params, ScriptKeywordType.PUBLIC, false, false);
		assert Logs.closeNode();
	}

	// Define default constructor here
	@Override
	public ScriptTemplate instantiateTemplate() {
		return new FauxTemplate_Scheduler(this.getEnvironment(), this.getType());
	}

	@Override
	public void nodificate() {
		assert Logs.openNode("Scheduler Faux Template");
		super.nodificate();
		assert Logs.addNode(this.scheduler);
		assert Logs.closeNode();
	}

	public void setScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
	}
}
