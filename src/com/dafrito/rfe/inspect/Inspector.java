package com.dafrito.rfe.inspect;

import com.dafrito.rfe.DebugEnvironment;
import com.dafrito.rfe.Debugger;

/**
 * Allows inspection of arbitrary content.
 * <p>
 * This is intended to supersede the old debug-centric inspection we wrote with
 * one that's much more flexible. Currently, this interface is pretty generic,
 * but I might make it more specific if this is warranted.
 * 
 * @author Aaron Faanes
 * @see Debugger
 * @see DebugEnvironment
 */
public interface Inspector {

	/**
	 * Visit a field.
	 * 
	 * @param name
	 *            the logical name of the field
	 * @param value
	 *            the value of the specified field
	 */
	public void field(String name, Object value);

	/**
	 * Visit some value. This may be the logical content of some group, or an
	 * element in a collection.
	 * 
	 * @param value
	 *            the visited value
	 */
	public void value(Object value);

	/**
	 * Visit a inner group. The returned {@link Inspector} must be used to
	 * insert content for the inner group.
	 * <p>
	 * Don't retain these inspectors any longer than you need them. They're not
	 * intended to be used beyond the lifespan of their parents.
	 * 
	 * @param groupName
	 *            the logical name of the group
	 * @return a {@link Inspector} that must be used to visit the content of the
	 *         group
	 */
	public Inspector group(String groupName);

	/**
	 * Visit a comment. This lets inspectors ignore descriptive information,
	 * while still retaining values passed through {@link #value(Object)}.
	 * 
	 * @param note
	 *            the comment that is made
	 */
	public void comment(String note);

}
