import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class InterfaceElement_Panel extends InterfaceElement implements Interface_Container, RiffInterface_MouseListener, Nodeable, ScriptConvertible {
	private List<GraphicalElement> elements;
	private Point_Euclidean offset;
	private Terrestrial terrestrial;
	private ScriptTemplate_Abstract dali;
	private Graphics2D graphics;

	public InterfaceElement_Panel(ScriptEnvironment env, Stylesheet uniqueStyle, Stylesheet classStyle) {
		super(env, uniqueStyle, classStyle);
		this.offset = new Point_Euclidean(env, 0, 0, 0);
		this.elements = new LinkedList<GraphicalElement>();
		this.dali = env.getTemplate(FauxTemplate_RiffDali.RIFFDALISTRING);
	}

	@Override
	public void add(GraphicalElement element) {
		this.elements.add(element);
		element.setParent(this);
		if (this.getRoot() != null) {
			this.getRoot().repaint();
		}
	}

	public void addAll(List list) {
		for (int i = 0; i < list.size(); i++) {
			this.add((GraphicalElement) list.get(i));
		}
		if (this.getRoot() != null) {
			this.getRoot().repaint();
		}
	}

	@Override
	public void clear() {
		this.elements.clear();
	}

	// ScriptConvertible implementation
	@Override
	public Object convert() {
		FauxTemplate_Panel panel = new FauxTemplate_Panel(this.getEnvironment(), ScriptValueType.createType(this.getEnvironment(), FauxTemplate_Panel.PANELSTRING));
		panel.setElement(this);
		return panel;
	}

	public void drawRegion(DiscreteRegion region) {
		DiscreteRegion.paint(this.getGraphics(), DiscreteRegion.transform(region, this.getOffset(), this.getDrawingBounds(), true), this.getDrawingBounds(), false);
	}

	public void drawString(String string, Color color, Point location) {
		Color oldColor = this.getGraphics().getColor();
		this.getGraphics().setColor(color);
		Point point = DiscreteRegion.transformPoint(this.getEnvironment(), location, this.getOffset(), this.getDrawingBounds(), true);
		this.getGraphics().drawString(string, (int) point.getX(), (int) point.getY());
		this.getGraphics().setColor(oldColor);
	}

	public void drawTransformedRegion(DiscreteRegion region) {
		DiscreteRegion.paint(this.getGraphics(), DiscreteRegion.transform(region, this.getOffset(), this.getDrawingBounds(), false), this.getDrawingBounds(), false);
	}

	public void fillRegion(DiscreteRegion region) {
		DiscreteRegion.paint(this.getGraphics(), DiscreteRegion.transform(region, this.getOffset(), this.getDrawingBounds(), true), this.getDrawingBounds(), true);
	}

	public void fillTransformedRegion(DiscreteRegion region) {
		DiscreteRegion.paint(this.getGraphics(), DiscreteRegion.transform(region, this.getOffset(), this.getDrawingBounds(), false), this.getDrawingBounds(), true);
	}

	@Override
	public InterfaceElement getContainerElement() {
		return this;
	}

	@Override
	public List<GraphicalElement> getElements() {
		return Collections.unmodifiableList(this.elements);
	}

	public Graphics2D getGraphics() {
		return this.graphics;
	}

	public Point getOffset() {
		return this.offset;
	}

	public Terrestrial getTerrestrial() {
		return this.terrestrial;
	}

	@Override
	public boolean isFocusable() {
		return true;
	}

	// Nodeable implementation
	@Override
	public boolean nodificate() {
		assert Debugger.openNode("Panel Interface Element");
		assert super.nodificate();
		assert Debugger.addSnapNode("Terrestrial", this.terrestrial);
		assert Debugger.addSnapNode("Graphical Elements: (" + this.elements.size() + " element(s))", this.elements);
		assert Debugger.closeNode();
		return true;
	}

	@Override
	public void paint(Graphics2D g2d) {
		Set<DiscreteRegion> regions;
		this.graphics = g2d;
		if (this.terrestrial.getTree() == null) {
			regions = new HashSet<DiscreteRegion>();
		} else {
			regions = this.terrestrial.getTree().getRegionList();
		}
		assert Debugger.openNode("Painting Panel Elements (" + this.elements.size() + " element(s), " + regions.size() + " region(s))");
		super.paint(g2d);
		assert Debugger.addNode("X-offset: " + this.offset.getX());
		assert Debugger.addNode("Y-offset: " + this.offset.getY());
		assert Debugger.addNode("Zoom factor: " + this.offset.getZ());
		try {
			List<ScriptValue_Abstract> params = new LinkedList<ScriptValue_Abstract>();
			params.add((ScriptValue_Abstract) this.convert());
			List<ScriptValue_Abstract> regionList = new LinkedList<ScriptValue_Abstract>();
			List<ScriptValue_Abstract> assetList = new LinkedList<ScriptValue_Abstract>();
			for (DiscreteRegion region : regions) {
				regionList.add((ScriptValue_Abstract) region.convert());
				if (region.getProperty("Archetypes") != null) {
					for (Asset asset : ((ArchetypeMapNode) region.getProperty("Archetypes")).getAllAssets()) {
						assetList.add((ScriptValue_Abstract) asset.convert());
					}
				}
			}
			params.add(Parser.getRiffList(this.getEnvironment(), regionList));
			params.add(Parser.getRiffList(this.getEnvironment(), assetList));
			if (this.dali != null) {
				ScriptExecutable_CallFunction.callFunction(this.getEnvironment(), null, this.dali, "paintPanel", params);
			}
		} catch (Exception_Nodeable exception) {
			throw new Exception_InternalError(this.getEnvironment(), exception);
		}
		for (GraphicalElement elem : this.elements) {
			elem.paint(g2d);
		}
		g2d.setColor(this.getBackgroundColor());
		g2d.draw(this.getDrawingBounds());
		assert Debugger.closeNode();
	}

	@Override
	public void riffMouseEvent(RiffInterface_MouseEvent event) {
		if (event instanceof RiffInterface_DragEvent) {
			if (event.getButton() == RiffInterface_MouseListener.MouseButton.LEFT) {
				this.offset.addX(-((RiffInterface_DragEvent) event).getXOffset() / Math.pow(2, this.offset.getZ()));
				this.offset.addY(-((RiffInterface_DragEvent) event).getYOffset() / Math.pow(2, this.offset.getZ()));
			}
			if (event.getButton() == RiffInterface_MouseListener.MouseButton.RIGHT) {
				this.offset.addZ(((RiffInterface_DragEvent) event).getDistance() / 50);
			}
		}
	}

	@Override
	public void setPreferredWidth(int width) {
	}

	public void setRiffDali(ScriptTemplate_Abstract dali) {
		this.dali = dali;
	}

	public void setTerrestrial(Terrestrial terrestrial) {
		this.terrestrial = terrestrial;
	}

	public int size() {
		return this.elements.size();
	}
}
