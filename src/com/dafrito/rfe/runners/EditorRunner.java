package com.dafrito.rfe.runners;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.bluespot.swing.Components;
import com.dafrito.rfe.DebugEnvironment;

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
		Components.LookAndFeel.NATIVE.activate();
		JFrame frame = new DebugEnvironment(800, 600);
		Components.center(frame);
		frame.setVisible(true);
	}
}