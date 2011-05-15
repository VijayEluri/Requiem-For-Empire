import java.util.List;

public class PolygonPipeline extends Thread {
	private DiscreteRegion region;
	private Terrestrial terrestrial;
	public static final String POLYGONPIPELINESTRING = "Polygon Pipeline";
	private static int threadNum = 0;

	public PolygonPipeline(Terrestrial panel, DiscreteRegion region) {
		super(POLYGONPIPELINESTRING + " " + threadNum++);
		this.terrestrial = panel;
		this.region = region;
	}

	@Override
	public void run() {
		Debugger.hitStopWatch(Thread.currentThread().getName());
		assert Debugger.openNode("Polygon Pipeline Executions", "Executing Polygon Pipeline");
		assert Debugger.addNode(this.region);
		if (this.region == null) {
			assert Debugger.closeNode("Region is null.");
			return;
		}
		List<DiscreteRegion> polygonList = RiffPolygonToolbox.convertPolyToConvex(this.region);
		if (polygonList == null) {
			assert Debugger.closeNode("Region is null.");
			return;
		}
		polygonList = RiffPolygonToolbox.joinPolygons(polygonList);
		polygonList = RiffPolygonToolbox.optimizePolygons(polygonList);
		this.terrestrial.addValidatedRegions(polygonList);
		assert Debugger.closeNode();
		Debugger.hitStopWatch(Thread.currentThread().getName());
	}
}
