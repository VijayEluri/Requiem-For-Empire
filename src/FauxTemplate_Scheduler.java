import java.util.LinkedList;
import java.util.List;

public class FauxTemplate_Scheduler extends FauxTemplate implements Nodeable, ScriptConvertible {
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
	public Object convert() {
		return this.scheduler;
	}

	// Function bodies are contained via a series of if statements in execute
	// Template will be null if the object is exactly of this type and is constructing, and thus must be created then
	@Override
	public ScriptValue_Abstract execute(Referenced ref, String name, List<ScriptValue_Abstract> params, ScriptTemplate_Abstract rawTemplate) throws Exception_Nodeable {
		assert Debugger.openNode("Faux Template Executions", "Executing scheduler faux template function (" + ScriptFunction.getDisplayableFunctionName(name) + ")");
		FauxTemplate_Scheduler template = (FauxTemplate_Scheduler) rawTemplate;
		ScriptValue_Abstract returning;
		assert Debugger.addSnapNode("Template provided", template);
		assert Debugger.addSnapNode("Parameters provided", params);
		ScriptValue_Abstract value;
		if (name == null || name.equals("")) {
			if (template == null) {
				template = (FauxTemplate_Scheduler) this.createObject(ref, template);
			}
			switch (params.size()) {
			case 1:
				template.getScheduler().setDefaultListener(Parser.getSchedulerListener(params.get(0)));
			case 0:
				assert Debugger.closeNode();
				return template;
			}
		} else if (name.equals("schedule")) {
			ScriptTemplate_Abstract listener = null;
			if (params.size() == 3) {
				listener = Parser.getTemplate(params.get(2));
			}
			template.getScheduler().schedule(Parser.getNumber(params.get(0)).longValue(), Parser.getAsset(params.get(1)), listener);
			assert Debugger.closeNode();
			return null;
		} else if (name.equals("setDefaultListener")) {
			template.getScheduler().setDefaultListener(Parser.getSchedulerListener(params.get(0)));
			assert Debugger.closeNode();
			return null;
		} else if (name.equals("start")) {
			template.getScheduler().start();
			assert Debugger.closeNode();
			return null;
		}
		returning = this.getExtendedFauxClass().execute(ref, name, params, template);
		assert Debugger.closeNode();
		return returning;
	}

	public Scheduler getScheduler() {
		return this.scheduler;
	}

	// addFauxFunction(name,ScriptValueType type,List<ScriptValue_Abstract>params,ScriptKeywordType permission,boolean isAbstract)
	// All functions must be defined here. All function bodies are defined in 'execute'.
	@Override
	public void initialize() throws Exception_Nodeable {
		assert Debugger.openNode("Faux Template Initializations", "Initializing scheduler faux template");
		this.addConstructor(this.getType(), ScriptValueType.createEmptyParamList());
		List<ScriptValue_Abstract> params = new LinkedList<ScriptValue_Abstract>();
		params.add(new ScriptValue_Faux(this.getEnvironment(), ScriptValueType.createType(this.getEnvironment(), FauxTemplate_SchedulerListener.SCHEDULERLISTENERSTRING)));
		this.addConstructor(this.getType(), params);
		this.disableFullCreation();
		this.getExtendedClass().initialize();
		params = new LinkedList<ScriptValue_Abstract>();
		params.add(new ScriptValue_Faux(this.getEnvironment(), ScriptValueType.LONG));
		params.add(new ScriptValue_Faux(this.getEnvironment(), ScriptValueType.createType(this.getEnvironment(), FauxTemplate_Asset.ASSETSTRING)));
		this.addFauxFunction("schedule", ScriptValueType.VOID, params, ScriptKeywordType.PUBLIC, false, false);
		params = new LinkedList<ScriptValue_Abstract>();
		params.add(new ScriptValue_Faux(this.getEnvironment(), ScriptValueType.LONG));
		params.add(new ScriptValue_Faux(this.getEnvironment(), ScriptValueType.createType(this.getEnvironment(), FauxTemplate_Asset.ASSETSTRING)));
		params.add(new ScriptValue_Faux(this.getEnvironment(), ScriptValueType.createType(this.getEnvironment(), FauxTemplate_SchedulerListener.SCHEDULERLISTENERSTRING)));
		this.addFauxFunction("schedule", ScriptValueType.VOID, params, ScriptKeywordType.PUBLIC, false, false);
		params = new LinkedList<ScriptValue_Abstract>();
		params.add(new ScriptValue_Faux(this.getEnvironment(), ScriptValueType.createType(this.getEnvironment(), FauxTemplate_SchedulerListener.SCHEDULERLISTENERSTRING)));
		this.addFauxFunction("setDefaultListener", ScriptValueType.VOID, params, ScriptKeywordType.PUBLIC, false, false);
		params = new LinkedList<ScriptValue_Abstract>();
		this.addFauxFunction("start", ScriptValueType.VOID, params, ScriptKeywordType.PUBLIC, false, false);
		assert Debugger.closeNode();
	}

	// Define default constructor here
	@Override
	public ScriptTemplate instantiateTemplate() {
		return new FauxTemplate_Scheduler(this.getEnvironment(), this.getType());
	}

	@Override
	public boolean nodificate() {
		assert Debugger.openNode("Scheduler Faux Template");
		assert super.nodificate();
		assert Debugger.addNode(this.scheduler);
		assert Debugger.closeNode();
		return true;
	}

	public void setScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
	}
}
