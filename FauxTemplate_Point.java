import java.util.*;
public class FauxTemplate_Point extends FauxTemplate implements ScriptConvertible,Nodeable{
	public static final String POINTSTRING="Point";
	public Point m_point;
	public FauxTemplate_Point(ScriptEnvironment env){
		super(env,ScriptValueType.createType(env,POINTSTRING),ScriptValueType.getObjectType(env),new LinkedList<ScriptValueType>(),false);
	}
	public FauxTemplate_Point(ScriptEnvironment env,ScriptValueType type,ScriptValueType extended,List<ScriptValueType>implemented,boolean isAbstract){
		super(env,type,extended,implemented,isAbstract);
	}
	public FauxTemplate_Point(ScriptEnvironment env,ScriptValueType type){
		super(env,type);
		m_point=new Point_Euclidean(env,0,0,0);
	}
	public Point getPoint(){return m_point;}
	public void setPoint(Point point){m_point=point;}
	// Define default constructor here
	public ScriptTemplate instantiateTemplate(){return new FauxTemplate_Point(getEnvironment(),getType());}
	// addFauxFunction(name,ScriptValueType type,List<ScriptValue_Abstract>params,ScriptKeywordType permission,boolean isAbstract)
	// All functions must be defined here. All function bodies are defined in 'execute'.
	public void initialize()throws Exception_Nodeable{
		assert Debugger.openNode("Faux Template Initializations","Initializing point faux template");
		addConstructor(getType(),ScriptValueType.createEmptyParamList());
		List<ScriptValue_Abstract>fxnParams=new LinkedList<ScriptValue_Abstract>();
		fxnParams.add(new ScriptValue_Faux(getEnvironment(),ScriptValueType.DOUBLE));
		fxnParams.add(new ScriptValue_Faux(getEnvironment(),ScriptValueType.DOUBLE));
		fxnParams.add(new ScriptValue_Faux(getEnvironment(),ScriptValueType.DOUBLE));
		addConstructor(getType(),fxnParams);
		disableFullCreation();
		getExtendedClass().initialize();
		addFauxFunction("getX",ScriptValueType.DOUBLE,ScriptValueType.createEmptyParamList(),ScriptKeywordType.PUBLIC,false,false);
		addFauxFunction("getY",ScriptValueType.DOUBLE,ScriptValueType.createEmptyParamList(),ScriptKeywordType.PUBLIC,false,false);
		addFauxFunction("getZ",ScriptValueType.DOUBLE,ScriptValueType.createEmptyParamList(),ScriptKeywordType.PUBLIC,false,false);
		fxnParams=new LinkedList<ScriptValue_Abstract>();
		fxnParams.add(new ScriptValue_Faux(getEnvironment(),ScriptValueType.DOUBLE));
		addFauxFunction("setX",ScriptValueType.VOID,fxnParams,ScriptKeywordType.PUBLIC,false,false);
		addFauxFunction("setY",ScriptValueType.VOID,fxnParams,ScriptKeywordType.PUBLIC,false,false);
		addFauxFunction("setZ",ScriptValueType.VOID,fxnParams,ScriptKeywordType.PUBLIC,false,false);
		assert Debugger.closeNode();
	}
	// Function bodies are contained via a series of if statements in execute
	// Template will be null if the object is exactly of this type and is constructing, and thus must be created then
	public ScriptValue_Abstract execute(Referenced ref,String name,List<ScriptValue_Abstract>params,ScriptTemplate_Abstract rawTemplate)throws Exception_Nodeable{
		assert Debugger.openNode("Faux Template Executions","Executing Point Faux Template Function ("+ScriptFunction.getDisplayableFunctionName(name)+")");
		FauxTemplate_Point template=(FauxTemplate_Point)rawTemplate;
		ScriptValue_Abstract returning=null;
		assert Debugger.addSnapNode("Template provided",template);
		assert Debugger.addSnapNode("Parameters provided",params);
		if(name==null||name.equals("")){
			if(template==null){template=(FauxTemplate_Point)createObject(ref,template);}
			if(params.size()==3){
				template.getPoint().setX(Parser.getDouble(params.get(0)).doubleValue());
				template.getPoint().setY(Parser.getDouble(params.get(1)).doubleValue());
				template.getPoint().setZ(Parser.getDouble(params.get(2)).doubleValue());
			}
			params.clear();
			returning=getExtendedFauxClass().execute(ref,name,params,template);
		}if(name.equals("getX")){returning=Parser.getRiffDouble(getEnvironment(),((FauxTemplate_Point)template).getPoint().getX());
		}else if(name.equals("getY")){returning=Parser.getRiffDouble(getEnvironment(),((FauxTemplate_Point)template).getPoint().getY());
		}else if(name.equals("getZ")){returning=Parser.getRiffDouble(getEnvironment(),((FauxTemplate_Point)template).getPoint().getZ());
		}else if(name.equals("setX")){((FauxTemplate_Point)template).getPoint().setX(Parser.getDouble(params.get(0)).doubleValue());
		}else if(name.equals("setY")){((FauxTemplate_Point)template).getPoint().setY(Parser.getDouble(params.get(0)).doubleValue());
		}else if(name.equals("setZ")){((FauxTemplate_Point)template).getPoint().setZ(Parser.getDouble(params.get(0)).doubleValue());
		}else{returning=getExtendedFauxClass().execute(ref,name,params,template);}
		assert Debugger.closeNode();
		return returning;
	}
	// ScriptConvertible and Nodeable implementations
	public Object convert(){return m_point;}
	public boolean nodificate(){
		assert Debugger.openNode("Point Faux Template");
		assert super.nodificate();
		assert Debugger.addNode(m_point);
		assert Debugger.closeNode();
		return true;
	}
}
