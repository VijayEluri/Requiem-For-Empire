package com.dafrito.rfe.debug;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class Debug_Filter extends JPanel implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3002302090734581411L;
	private JButton addFilter, removeFilter;
	private DefaultListModel filters = new DefaultListModel();
	private JList filterList;
	private boolean isListening;
	private Debug_Listener listener;

	public Debug_Filter(Debug_Listener listener) {
		this.listener = listener;
		this.setLayout(new BorderLayout());
		JPanel filterButtons = new JPanel();
		this.add(filterButtons, BorderLayout.NORTH);
		filterButtons.setLayout(new GridLayout(2, 0));
		filterButtons.add(this.addFilter = new JButton("Add Filter"));
		filterButtons.add(this.removeFilter = new JButton("Remove Filter"));
		this.add(new JScrollPane(this.filterList = new JList(this.filters)));
		this.addFilter.addActionListener(this);
		this.removeFilter.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(this.addFilter)) {
			Object data;
			if (this.getListener().getTreePanel().getSelectedNode() != null) {
				data = JOptionPane.showInputDialog(null, "Insert the name of the filter you wish to add", "Filter Additions", JOptionPane.PLAIN_MESSAGE, null, null, this.getListener().getTreePanel().getFirstAvailableFilter());
			} else {
				data = this.getListener().getTreePanel().getFirstAvailableFilter();
			}
			if (data != null) {
				this.addFilter(data);
			}
		} else if (e.getSource().equals(this.removeFilter)) {
			this.filters.remove(this.filters.indexOf(this.filterList.getSelectedValue()));
		}
	}

	public boolean addFilter(Object filter) {
		if (filter == null || this.getListener().getDebugger().isFilterUsed(filter.toString(), this.listener.getThreadName()) != null) {
			return false;
		}
		this.filters.add(this.filters.size(), filter);
		return true;
	}

	public DefaultListModel getFilters() {
		return this.filters;
	}

	public Debug_Listener getListener() {
		return this.listener;
	}

	public boolean isFilterUsed(Object test) {
		for (int i = 0; i < this.filters.size(); i++) {
			if (this.filters.get(i).equals(test)) {
				return true;
			}
		}
		return false;
	}

	public boolean isListening() {
		if (this.getListener().isUnfiltered()) {
			return true;
		}
		if (this.getListener().isCapturing() || this.getListener().isSynchronizing()) {
			return this.isListening;
		}
		return false;
	}

	public void setListening(boolean listening) {
		this.isListening = listening;
	}

	public void sniffNode(Debug_TreeNode node) {
		for (int i = 0; i < this.filters.size(); i++) {
			if (this.isListening == true) {
				return;
			}
			this.isListening = node.getData().equals(this.filters.get(i)) || (node.getGroup() != null && node.getGroup().equals(this.filters.get(i)));
		}
	}
}