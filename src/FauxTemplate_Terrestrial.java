import java.util.*;
public class FauxTemplate_Terrestrial extends FauxTemplate implements ScriptConvertible,Nodeable{
	public static final String TERRESTRIALSTRING="Terrestrial";
	private Terrestrial m_terrestrial;
	public FauxTemplate_Terrestrial(ScriptEnvironment env){
		super(env,ScriptValueType.createType(env,TERRESTRIALSTRING),ScriptValueType.getObjectType(env),new LinkedList<ScriptValueType>(),false);
	}
	public FauxTemplate_Terrestrial(ScriptEnvironment env,ScriptValueType type){
		super(env,type);
		setTerrestrial(new Terrestrial(env,1));
	}
	public Terrestrial getTerrestrial(){return m_terrestrial;}
	public void setTerrestrial(Terrestrial terrestrial){m_terrestrial=terrestrial;}
	// Define default constructor here
	public ScriptTemplate instantiateTemplate(){return new FauxTemplate_Terrestrial(getEnvironment(),getType());}
	// addFauxFunction(name,ScriptValueType type,List<ScriptValue_Abstract>params,ScriptKeywordType permission,boolean isAbstract)
	// All functions must be defined here. All function bodies are defined in 'execute'.
	public void initialize()throws Exception_Nodeable{
		assert Debugger.openNode("Faux Template Initializations","Initializing terrestrial faux template");
		addConstructor(getType(),ScriptValueType.createEmptyParamList());
		List<ScriptValue_Abstract>fxnParams=new LinkedList<ScriptValue_Abstract>();
		fxnParams.add(new ScriptValue_Faux(getEnvironment(),ScriptValueType.DOUBLE));
		addConstructor(getType(),fxnParams);
		disableFullCreation();
		getExtendedClass().initialize();
		fxnParams=ScriptValueType.createEmptyParamList();
		fxnParams.add(new ScriptValue_Faux(getEnvironment(),ScriptValueType.createType(getEnvironment(),FauxTemplate_DiscreteRegion.DISCRETEREGIONSTRING)));
		addFauxFunction("add",ScriptValueType.VOID,fxnParams,ScriptKeywordType.PUBLIC,false,false);
		fxnParams=ScriptValueType.createEmptyParamList();
		fxnParams.add(new ScriptValue_Faux(getEnvironment(),ScriptValueType.createType(getEnvironment(),FauxTemplate_Scenario.SCENARIOSTRING)));
		fxnParams.add(new ScriptValue_Faux(getEnvironment(),ScriptValueType.createType(getEnvironment(),FauxTemplate_MovementEvaluator.MOVEMENTEVALUATORSTRING)));
		fxnParams.add(new ScriptValue_Faux(getEnvironment(),ScriptValueType.createType(getEnvironment(),FauxTemplate_Asset.ASSETSTRING)));
		fxnParams.add(new ScriptValue_Faux(getEnvironment(),ScriptValueType.createType(getEnvironment(),FauxTemplate_Point.POINTSTRING)));
		fxnParams.add(new ScriptValue_Faux(getEnvironment(),ScriptValueType.createType(getEnvironment(),FauxTemplate_Point.POINTSTRING)));
		addFauxFunction("getPath",ScriptValueType.createType(getEnvironment(),FauxTemplate_Path.PATHSTRING),fxnParams,ScriptKeywordType.PUBLIC,false,false);
		assert Debugger.closeNode();
	}
	// Function bodies are contained via a series of if statements in execute
	// Template will be null if the object is exactly of this type and is constructing, and thus must be created then
	public ScriptValue_Abstract execute(Referenced ref,String name,List<ScriptValue_Abstract>params,ScriptTemplate_Abstract rawTemplate)throws Exception_Nodeable{
		assert Debugger.openNode("Faux Template Executions","Executing terrestrial faux template function ("+ScriptFunction.getDisplayableFunctionName(name)+")");
		FauxTemplate_Terrestrial template=(FauxTemplate_Terrestrial)rawTemplate;
		ScriptValue_Abstract returning;
		assert Debugger.addSnapNode("Template provided",template);
		assert Debugger.addSnapNode("Parameters provided",params);
		if(name==null||name.equals("")){
			if(template==null){template=(FauxTemplate_Terrestrial)createObject(ref,template);}
			template.setTerrestrial(new Terrestrial(getEnvironment(),Parser.getDouble(params.get(0))));
			assert Debugger.closeNode();
			return template;
		}else if(name.equals("add")){
			DiscreteRegion region=Parser.getDiscreteRegion(params.get(0));
			assert Debugger.addSnapNode("Adding discrete region to terrestrial",region);
			template.getTerrestrial().add(region);
			assert Debugger.closeNode();
			return null;
		}else if(name.equals("getPath")){
			returning=Parser.getRiffPath(getEnvironment(),template.getTerrestrial().getPath(getEnvironment(),Parser.getScenario(params.get(0)),Parser.getTemplate(params.get(1)),Parser.getAsset(params.get(2)),Parser.getPoint(params.get(3)),Parser.getPoint(params.get(4))));
			assert Debugger.closeNode();
			return returning;
		}
		returning=getExtendedFauxClass().execute(ref,name,params,template);
		assert Debugger.closeNode();
		return returning;
	}
	// Nodeable and ScriptConvertible implementations
	public Object convert(){return getTerrestrial();}
	public boolean nodificate(){
		assert Debugger.openNode("Terrestrial Faux Template");
		assert super.nodificate();
		assert Debugger.closeNode();
		return true;
	}
}
