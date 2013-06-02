/**
 * 
 */
package com.dafrito.rfe.logging;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.bluespot.logic.actors.Actor;
import com.dafrito.rfe.inspect.Inspectable;
import com.dafrito.rfe.inspect.Inspection;
import com.dafrito.rfe.inspect.Nodeable;
import com.dafrito.rfe.inspect.NodeableInspector;
import com.dafrito.rfe.script.exceptions.Exception_InternalError;
import com.dafrito.rfe.script.exceptions.ScriptException;

public class Logs {
	private static final ThreadLocalTreeLog<Object, ProxyTreeLog<Object>> masterLog = new ThreadLocalTreeLog<Object, ProxyTreeLog<Object>>() {
		@Override
		protected ProxyTreeLog<? super Object> newTreeLog(Thread thread) {
			return new ProxyTreeLog<>();
		}
	};

	/**
	 * Immediately add the specified listener. See
	 * {@link ThreadLocalTreeLog#addListener(Actor)} for details.
	 * <p>
	 * I use <? super Object> here simply to remind myself that this is the type
	 * I want in case I change from using Object at some point in the future.
	 * Since it's Object right now, it's equivalent to <Object>.
	 * 
	 * @param listener
	 *            the listener that will receive new logs for every thread
	 * @see ThreadLocalTreeLog#addListener(Actor)
	 * @see ProxyTreeLog#removeListener(TreeLog)
	 */
	public static void addListener(Actor<? super ProxyTreeLog<? super Object>> listener) {
		masterLog.addListener(listener);
	}

	/**
	 * Immediately remove the specified log.
	 * <p>
	 * I use <? super Object> here simply to remind myself that this is the type
	 * I want in case I change from using Object at some point in the future.
	 * Since it's Object right now, it's equivalent to <Object>.
	 * 
	 * @param listener
	 *            the listener to remove
	 * @see ThreadLocalTreeLog#removeListener(Actor)
	 */
	public static void removeListener(Actor<? super ProxyTreeLog<? super Object>> listener) {
		masterLog.removeListener(listener);
	}

	public static boolean openNode(CommonString scope) {
		return openNode(scope.getText());
	}

	public static boolean openNode(String scope) {
		openNode(null, scope);
		return true;
	}

	public static boolean openNode(String scopeGroup, String scope) {
		masterLog.enter(scope, scopeGroup != null ? scopeGroup : scope);
		return true;
	}

	public static boolean addNode(Object message) {
		return addNode(null, message);
	}

	public static boolean addNode(Object scope, Object message) {
		if (message == null) {
			return addNode(scope, "null");
		}
		if (message.getClass().isAnnotationPresent(Inspectable.class)) {
			NodeableInspector<Object> inspector = new NodeableInspector<Object>(masterLog);
			Inspection.reflect(inspector, message);
			inspector.close();
			return true;
		}
		if (message instanceof Nodeable) {
			addNodeableNode(scope, (Nodeable) message);
			if (message instanceof Exception) {
				registerHotspot((Exception) message);
			}
			return true;
		}
		if (message instanceof Iterable<?>) {
			return addCollectionNode((Iterable<?>) message);
		}
		if (message instanceof Map<?, ?>) {
			return addMapNode((Map<?, ?>) message);
		}
		if (message instanceof CommonString) {
			message = ((CommonString) message).getText();
		}
		masterLog.log(new LogMessage<Object>(message));

		if (message instanceof Exception) {
			registerHotspot((Exception) message);
		}
		return true;
	}

	// TODO Remove this warning when we're ready to reimplement hotspots
	@SuppressWarnings("unused")
	private static void registerHotspot(Exception exception) {
		String exceptionName;
		if (exception instanceof ScriptException) {
			exceptionName = ((ScriptException) exception).getName();
		} else if (exception instanceof Exception_InternalError) {
			exceptionName = ((Exception_InternalError) exception).getMessage();
		} else {
			exceptionName = "Exception";
		}
		// TODO Reimplement hotspots
		// getDebugger().getUnfilteredOutput().getHotspotPanel().addHotspot(getDebugInspector().getLastNodeAdded(), exceptionName);
	}

	public static boolean addNodeableNode(Object group, Nodeable nodeable) {
		nodeable.nodificate();
		return true;
	}

	public synchronized static boolean addCollectionNode(Iterable<?> iterable) {
		Iterator<?> iter = iterable.iterator();
		while (iter.hasNext()) {
			addNode(iter.next());
		}
		return true;
	}

	public synchronized static boolean addMapNode(Map<?, ?> map) {
		if (map.isEmpty()) {
			return true;
		}
		Iterator<?> iter = map.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<?, ?> entry = (Map.Entry<?, ?>) iter.next();
			addSnapNode(entry.getKey().toString(), entry.getValue());
		}
		return true;
	}

	public static boolean addSnapNode(CommonString scope, Object message) {
		return addSnapNode(scope.getText(), message);
	}

	public static boolean addSnapNode(String scope, Object message) {
		return addSnapNode(null, scope, message);
	}

	public static boolean addSnapNode(String scopeGroup, String scope, Object message) {
		openNode(scopeGroup, scope);
		addNode(message);
		closeNode();
		return true;
	}

	public static boolean closeNode() {
		masterLog.leave();
		return true;
	}

	public static boolean closeNode(Object message) {
		addNode(message);
		closeNode();
		return true;
	}

	public static boolean closeNode(String scope, Object message) {
		addSnapNode(scope, message);
		closeNode();
		return true;
	}

	public static void printException(Exception ex) {
		System.err.println(ex);
		if (ex instanceof ScriptException || ex instanceof Exception_InternalError) {
			assert addNode("Exceptions and Errors", ex);
		} else {
			assert addSnapNode("Exceptions and Errors", "Exception", ex);
		}
	}

	public static void report() {
		System.out.println("Performance Report");
		NumberFormat nf = NumberFormat.getInstance();
		System.out.println("Maximum Memory Available: " + nf.format(Runtime.getRuntime().maxMemory()) + " bytes (" + Logs.getAllocationPercentage() + "% allocated)");
		System.out.println("Used Memory Before GC: " + nf.format(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) + " bytes (" + Logs.getFreePercentage() + "% free)");
		System.gc();
		System.out.println("Used Memory After GC : " + nf.format(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) + " bytes (" + Logs.getFreePercentage() + "% free)");
		System.out.println("Free Memory: " + nf.format(Runtime.getRuntime().freeMemory()) + " bytes");
	}

	public static boolean atFullAllocation() {
		return getAllocationPercentage() == 100;
	}

	public static int getAllocationPercentage() {
		return (int) ((((double) Runtime.getRuntime().totalMemory()) / ((double) Runtime.getRuntime().maxMemory())) * 100);
	}

	public static int getFreePercentage() {
		return (int) ((((double) Runtime.getRuntime().freeMemory()) / ((double) Runtime.getRuntime().totalMemory())) * 100);
	}

	private static final Map<String, Long> stopWatches = new HashMap<String, Long>();

	/**
	 * Hit a stopwatch named {@code Thread.currentThread().getName()}.
	 * 
	 * @see Logs#hitStopWatch(String)
	 */
	public static void hitStopWatch() {
		Logs.hitStopWatch(Thread.currentThread().getName());
	}

	/**
	 * Start or stop a stop watch.
	 * 
	 * @param name
	 *            the name of the stop watch. If no stopwatch is running with
	 *            this name, a stopwatch is begun. Otherwise, the stopwatch is
	 *            stopped.
	 */
	public static void hitStopWatch(String name) {
		if (stopWatches.get(name) == null) {
			stopWatches.put(name, Long.valueOf(System.currentTimeMillis()));
			return;
		}
		System.out.println("StopWatcher: " + name + " executed in " + (((double) (System.currentTimeMillis() - stopWatches.get(name).longValue())) / 1000) + " seconds");
		stopWatches.remove(name);
	}

}