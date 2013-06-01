package com.dafrito.rfe;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.bluespot.swing.Components;
import com.dafrito.rfe.gui.debug.LogViewer;
import com.dafrito.rfe.gui.debug.ScriptEditor;

/**
 * Create a new editing environment.
 * 
 * @author Aaron Faanes
 * 
 */
public class EditorRunner implements Runnable {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new EditorRunner());
	}

	@Override
	public void run() {
		if (!SwingUtilities.isEventDispatchThread()) {
			throw new IllegalStateException("Runner must be run from EDT");
		}
		Components.LookAndFeel.GTK.activate();
		JFrame logFrame = new LogViewer();
		logFrame.setSize(800, 600);
		logFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		Components.center(logFrame);
		logFrame.setVisible(true);

		JFrame scriptFrame = new ScriptEditor();
		scriptFrame.setSize(800, 600);
		scriptFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		scriptFrame.setLocationRelativeTo(logFrame);
		scriptFrame.setVisible(true);
	}
}
