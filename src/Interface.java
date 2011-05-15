// Requiem for Empire
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class Interface extends JPanel implements MouseListener, MouseMotionListener, WindowListener, ComponentListener, KeyListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8097618780874029519L;
	private static final int HEIGHT = 600;
	private static final int WIDTH = 800;
	private ScriptEnvironment m_environment;
	private InterfaceElement_Root m_rootElement;
	private int m_lastX, m_lastY;
	private java.util.List<RiffInterface_Event> m_queuedEvents = new LinkedList<RiffInterface_Event>();
	private RiffInterface_MouseListener.MouseButton m_lastButton;
	private boolean m_ignoreMemoryWarning;
	private BufferedImage m_buffer, m_backBuffer;
	private long m_secondBegin;
	private int m_lastIteration;
	private int m_iterations;
	private boolean m_emergencyStop;
	private JFrame m_frame;
	private PainterThread m_painter;

	public Interface(ScriptEnvironment env) {
		m_frame = new JFrame("Requiem for Empire");
		m_frame.setSize(WIDTH, HEIGHT);
		m_frame.setContentPane(this);
		m_frame.addWindowListener(this);
		m_frame.addComponentListener(this);
		m_frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		m_frame.setVisible(true);
		m_frame.addKeyListener(this);
		m_environment = env;
		m_rootElement = new InterfaceElement_Root(env, this);
		addMouseListener(this);
		addMouseMotionListener(this);
		m_buffer = (BufferedImage) createImage(m_frame.getContentPane().getWidth(), m_frame.getContentPane().getHeight());
		m_backBuffer = (BufferedImage) createImage(m_frame.getContentPane().getWidth(), m_frame.getContentPane().getHeight());
		boolean flag = false;
		assert flag = true;
		if (flag) {
			m_painter = new PainterThread(this, 200);
		} else {
			m_painter = new PainterThread(this, 1000 / 60);
		}
		m_painter.start();
	}

	protected void clear(Graphics g) {
		paint(g);
	}

	@Override
	public void componentHidden(ComponentEvent e) {
	}

	@Override
	public void componentMoved(ComponentEvent e) {
	}

	@Override
	public void componentResized(ComponentEvent e) {
		if (m_buffer.getWidth() != getWidth() || m_buffer.getHeight() != getHeight()) {
			m_buffer = (BufferedImage) createImage(getWidth(), getHeight());
			m_backBuffer = (BufferedImage) createImage(getWidth(), getHeight());
		}
	}

	@Override
	public void componentShown(ComponentEvent e) {
	}

	public synchronized void flushQueue() {
		assert Debugger.addNode("Flushing Event Queue (" + m_queuedEvents.size() + " event(s))");
		List<RiffInterface_Event> flushQueue = new LinkedList<RiffInterface_Event>();
		flushQueue.addAll(m_queuedEvents);
		m_queuedEvents.removeAll(flushQueue);
		for (int i = 0; i < flushQueue.size(); i++) {
			m_rootElement.dispatchEvent(flushQueue.get(i));
		}
	}

	public BufferedImage getBackBuffer() {
		return m_backBuffer;
	}

	public ScriptEnvironment getEnvironment() {
		return m_environment;
	}

	public JFrame getFrame() {
		return m_frame;
	}

	public BufferedImage getFrontBuffer() {
		return m_buffer;
	}

	public InterfaceElement_Root getRoot() {
		return m_rootElement;
	}

	@Override
	public synchronized void keyPressed(KeyEvent e) {
		KeyEvent_KeyDown event = new KeyEvent_KeyDown(e.getKeyCode());
		assert Debugger.addNode(event);
		m_queuedEvents.add(event);
	}

	@Override
	public synchronized void keyReleased(KeyEvent e) {
		KeyEvent_KeyUp event = new KeyEvent_KeyUp(e.getKeyCode());
		assert Debugger.addNode(event);
		m_queuedEvents.add(event);
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public synchronized void mouseClicked(MouseEvent e) {
		RiffInterface_ClickEvent event = new RiffInterface_ClickEvent(e.getX(), e.getY(), RiffJavaToolbox.getRiffButton(e.getButton()), e.getClickCount());
		assert Debugger.addNode(event);
		m_queuedEvents.add(event);
		m_lastX = e.getX();
		m_lastY = e.getY();
		m_lastButton = RiffJavaToolbox.getRiffButton(e.getButton());
	}

	@Override
	public synchronized void mouseDragged(MouseEvent e) {
		double distance = RiffToolbox.getDistance(m_lastX, m_lastY, e.getX(), e.getY());
		if (e.getX() - m_lastX < 0) {
			distance *= -1;
		}
		if (e.getX() == m_lastX && e.getY() - m_lastY < 0 && distance > 0) {
			distance *= -1;
		}
		RiffInterface_DragEvent event = new RiffInterface_DragEvent(e.getX(), e.getY(), m_lastButton, e.getX() - m_lastX, e.getY() - m_lastY, distance);
		assert Debugger.addNode(event);
		m_queuedEvents.add(event);
		m_lastX = e.getX();
		m_lastY = e.getY();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

	@Override
	public synchronized void mousePressed(MouseEvent e) {
		RiffInterface_MouseDownEvent event = new RiffInterface_MouseDownEvent(e.getX(), e.getY(), RiffJavaToolbox.getRiffButton(e.getButton()));
		assert Debugger.addNode(event);
		m_queuedEvents.add(event);
		m_lastX = e.getX();
		m_lastY = e.getY();
		m_lastButton = RiffJavaToolbox.getRiffButton(e.getButton());
	}

	@Override
	public synchronized void mouseReleased(MouseEvent e) {
		RiffInterface_MouseUpEvent event = new RiffInterface_MouseUpEvent(e.getX(), e.getY(), RiffJavaToolbox.getRiffButton(e.getButton()));
		assert Debugger.addNode(event);
		m_queuedEvents.add(event);
		m_lastX = -1;
		m_lastY = -1;
	}

	@Override
	public void paint(Graphics g) {
		g.drawImage(m_buffer, 0, 0, null);
		g.dispose();
	}

	@Override
	public void update(Graphics g) {
		paint(g);
	}

	public synchronized void updateBufferedImage() {
		if (m_emergencyStop) {
			return;
		}
		if (m_secondBegin == 0) {
			m_secondBegin = System.currentTimeMillis();
		}
		if (System.currentTimeMillis() > m_secondBegin + 1000) {
			m_secondBegin = System.currentTimeMillis();
			m_lastIteration = m_iterations;
			m_iterations = 0;
		}
		m_iterations++;
		if (m_backBuffer == null) {
			return;
		}
		Graphics g = m_backBuffer.getGraphics();
		try {
			if (Debugger.isResetting()) {
				return;
			}
			if (Debugger.atFullAllocation() && Debugger.getFreePercentage() > 50) {
				Debugger.setExceptionsMode(true);
			}
			if (!m_ignoreMemoryWarning && Debugger.atFullAllocation() && Debugger.getFreePercentage() < 20) {
				System.gc();
				if (Debugger.getFreePercentage() > 20) {
					return;
				}
				Debugger.report();
				m_emergencyStop = true;
				int option = JOptionPane.showConfirmDialog(null, "Memory usage exceeds 80% of full allocation. Reset debug tree?", "Memory Warning (" + Debugger.getFreePercentage() + "% free)", JOptionPane.YES_NO_OPTION);
				if (option == JOptionPane.YES_OPTION) {
					Debugger.reset();
					m_emergencyStop = false;
				} else {
					m_ignoreMemoryWarning = true;
					m_emergencyStop = false;
				}
			}
			Graphics2D g2d = (Graphics2D) g;
			g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			m_rootElement.paint(g2d);
			g2d.setColor(Color.WHITE);
			g2d.drawString("" + m_lastIteration + " fps", getWidth() / 2, 20);
			g2d.dispose();
		} catch (Exception ex) {
			throw new Exception_InternalError(m_rootElement.getEnvironment(), ex);
		}
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowClosing(WindowEvent e) {
		m_painter.stopLoop();
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}
}

class PainterThread extends Thread {
	private Interface m_interface;
	private static int m_threadNum = 0;
	private volatile boolean m_shouldDraw = true;
	private static int m_delay;

	public PainterThread(Interface riffInterface, int delay) {
		super("Painter " + m_threadNum++);
		m_interface = riffInterface;
		m_delay = delay;
	}

	@Override
	public void run() {
		try {
			while (m_shouldDraw) {
				//Debugger.hitStopWatch(Thread.currentThread().getName());
				m_interface.getFrontBuffer().getGraphics().drawImage(m_interface.getBackBuffer(), 0, 0, null);
				m_interface.flushQueue();
				m_interface.updateBufferedImage();
				m_interface.repaint();
				Thread.sleep(m_delay);
			}
		} catch (RuntimeException ex) {
			stopLoop();
			m_interface.getFrame().dispose();
			throw ex;
		} catch (InterruptedException ex) {
		} finally {
			//Debugger.hitStopWatch(Thread.currentThread().getName());
		}
	}

	public void stopLoop() {
		m_shouldDraw = false;
		m_interface.getEnvironment().stopExecution();
	}
}
