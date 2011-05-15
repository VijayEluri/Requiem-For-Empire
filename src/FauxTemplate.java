import java.util.*;
public abstract class FauxTemplate extends ScriptTemplate implements ScriptValue_Abstract{
	public FauxTemplate(ScriptEnvironment env,ScriptValueType type){
		super(env,type);
	}
	public FauxTemplate(ScriptEnvironment env,ScriptValueType type,ScriptValueType extended,List<ScriptValueType>implemented,boolean isAbstract){
		super(env,type,extended,implemented,isAbstract);
	}
	public static List<ScriptValue_Abstract> createEmptyParamList(){return new LinkedList<ScriptValue_Abstract>();}
	// Define default constructor here
	public ScriptTemplate instantiateTemplate(){return null;}
	// addFauxFunction(name,ScriptValueType type,List<ScriptValue_Abstract>params,ScriptKeywordType permission,boolean isAbstract,boolean isStatic)
	// All functions must be defined here. All function bodies are defined in 'execute'.
	public void initialize()throws Exception_Nodeable{
		
	}
	// Function bodies are contained via a series of if statements in execute
	// Template will be null if the object is exactly of this type and is constructing, and thus must be created then
	public ScriptValue_Abstract execute(Referenced ref,String name,List<ScriptValue_Abstract>params,ScriptTemplate_Abstract rawTemplate)throws Exception_Nodeable{
		assert Debugger.openNode("Faux Template Executions","Executing INSERTFAUXTEMPLATENAMEHERE Faux Template Function ("+ScriptFunction.getDisplayableFunctionName(name)+")");
		FauxTemplate_InterfaceElement template=(FauxTemplate_InterfaceElement)rawTemplate;
		ScriptValue_Abstract returning;
		assert Debugger.addSnapNode("Template provided",template);
		assert Debugger.addSnapNode("Parameters provided",params);
		throw new Exception_InternalError("Invalid default in FauxTemplate:execute");
	}
	public boolean nodificate(){
		super.nodificate();
		return true;
	}
	public FauxTemplate getExtendedFauxClass(){return (FauxTemplate)getExtendedClass();}
	public void addConstructor(ScriptValueType returnType,List<ScriptValue_Abstract>params)throws Exception_Nodeable{addFauxFunction("",returnType,params,ScriptKeywordType.PUBLIC,false,true);}
	public void addFauxFunction(String name,ScriptValueType returnType,List<ScriptValue_Abstract>params,ScriptKeywordType permission,boolean isAbstract,boolean isStatic)throws Exception_Nodeable{
		addFunction(null,name,new ScriptFunction_Faux(this,name,returnType,params,permission,isAbstract,isStatic));
	}
	public void initializeFunctions()throws Exception_Nodeable{}
	public void addTemplatePreconstructorExpression(ScriptExecutable exec)throws Exception_Nodeable{throw new Exception_InternalError(getEnvironment(),"Invalid call in FauxTemplate");}
	public int valuesCompare(Referenced ref, ScriptValue_Abstract rhs)throws Exception_Nodeable{throw new Exception_InternalError(getEnvironment(),"Invalid call in FauxTemplate");}
	public ScriptValue_Abstract setValue(Referenced ref, ScriptValue_Abstract value)throws Exception_Nodeable{throw new Exception_InternalError(getEnvironment(),"Invalid call in FauxTemplate");}
}