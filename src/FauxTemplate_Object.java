import java.util.*;
public class FauxTemplate_Object extends FauxTemplate implements ScriptConvertible,Nodeable{
	public static ScriptKeyword OBJECT;
	public static final String OBJECTSTRING="Object";
	public FauxTemplate_Object(ScriptEnvironment env){
		super(env,ScriptValueType.getObjectType(env),null,new LinkedList<ScriptValueType>(),true);
	}
	public FauxTemplate_Object(ScriptEnvironment env,ScriptValueType type){
		super(env,type);
	}
	// Define default constructor here
	public ScriptTemplate instantiateTemplate(){return new FauxTemplate_Object(getEnvironment(),getType());}
	// addFauxFunction(name,ScriptValueType type,List<ScriptValue_Abstract>params,ScriptKeywordType permission,boolean isAbstract)
	// All functions must be defined here. All function bodies are defined in 'execute'.
	public void initialize()throws Exception_Nodeable{
		assert Debugger.openNode("Faux Template Initializations","Initializing object faux template");
		addConstructor(getType(),ScriptValueType.createEmptyParamList());
		disableFullCreation();
		assert Debugger.closeNode();
	}
	// Function bodies are contained via a series of if statements in execute
	// Template will be null if the object is exactly of this type and is constructing, and thus must be created then
	public ScriptValue_Abstract execute(Referenced ref,String name,List<ScriptValue_Abstract>params,ScriptTemplate_Abstract rawTemplate)throws Exception_Nodeable{
		assert Debugger.openNode("Faux Template Executions","Executing Object Faux Template Function ("+ScriptFunction.getDisplayableFunctionName(name)+")");
		ScriptValue_Abstract returning;
		assert Debugger.addSnapNode("Template provided",rawTemplate);
		assert Debugger.addSnapNode("Parameters provided",params);
		if(name==null||name.equals("")){
			if(params.size()!=0){throw new Exception_Nodeable_FunctionNotFound(ref,name,params);}
			if(rawTemplate==null){rawTemplate=(ScriptTemplate_Abstract)createObject(ref,rawTemplate);}
			assert Debugger.closeNode();
			return rawTemplate;
		}
		throw new Exception_Nodeable_FunctionNotFound(ref,name,params);
	}
	// ScriptConvertible and Nodeable implementations
	public Object convert(){return this;}
	public boolean nodificate(){
		assert Debugger.openNode("Object Faux Template");
		assert super.nodificate();
		assert Debugger.closeNode();
		return true;
	}
}