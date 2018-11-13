/* Canvas.java

{{IS_NOTE
	Purpose:
		Canvas prototype.
		
	Description:
		
	History:
		May 12, 2010 3:16:36 PM , Created by simon
}}IS_NOTE

Copyright (C) 2010 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
}}IS_RIGHT
*/
package com.gitlab.pedrioko.core.zk.component;

import com.gitlab.pedrioko.core.zk.component.model.Drawable;
import com.gitlab.pedrioko.core.zk.component.model.Path;
import com.gitlab.pedrioko.core.zk.component.model.Rectangle;
import com.gitlab.pedrioko.core.zk.component.model.Text;
import org.zkoss.json.JSONObject;
import org.zkoss.json.JSONValue;
import org.zkoss.zk.ui.sys.ContentRenderer;
import org.zkoss.zul.impl.XulElement;

import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.*;

/**
 * The prototype component corresponding to HTML 5 Canvas.
 * While HTML 5 Canvas is a command-based DOM object that allows user to draw
 * items on a surface, This Canvas maintains a list of drawable items and allow
 * user to operate the list by adding, removing, updating, replacing the
 * elements. The changes will be reflected on the client side upon these
 * operations.
 *
 * @author simon
 */
@SuppressWarnings("serial")
public class Canvas extends XulElement {
    private List<Drawable> _drawables;

    public Canvas() {
        _drawables = new LinkedList<Drawable>();
    }

    /* getter */

    /**
     * Return a list of all drawings in Canvas.
     */
    public List<Drawable> getAllDrawables() {
        return Collections.unmodifiableList(_drawables);
    }

    /**
     * Returns an iterator of Drawable list.
     */
    public Iterator<Drawable> iterator() {
        return _drawables.iterator();
    }

    /**
     * Returns a ListIterator of the Drawables
     * Returns a ListIterator of the Drawables
     */
    public ListIterator<Drawable> listIterator() {
        return _drawables.listIterator();
    }

    /**
     * Returns a ListIterator of the Drawables, starting at specific index.
     */
    public ListIterator<Drawable> listIterator(int index) {
        return _drawables.listIterator(index);
    }

    /**
     * Returns a view of the portion of this list between the specified
     * fromIndex, inclusive, and toIndex, exclusive.
     *
     * @param fromIndex
     * @param toIndex
     */
    public List<Drawable> subList(int fromIndex, int toIndex) {
        return _drawables.subList(fromIndex, toIndex);
    }

    /**
     * Returns an array containing all Drawable in the list.
     */
    public Drawable[] toArray() {
        return _drawables.toArray(new Drawable[0]);
    }

    /**
     * Returns the drawing at position index.
     *
     * @param index: drawings at 0 is the earliest drawing.
     */
    public Drawable get(int index) {
        return _drawables.get(index);
    }



    /* query */

    /**
     * Return true if the list is empty.
     */
    public boolean isEmpty() {
        return _drawables.isEmpty();
    }

    /**
     * Return true if contains the drawable.
     */
    public boolean contains(Drawable drawable) {
        return _drawables.contains(drawable);
    }

    /**
     * Return true if contains all the Drawables.
     *
     * @param drawables: A list of Drawables.
     */
    public boolean containsAll(Collection<Drawable> drawables) {
        return _drawables.containsAll(drawables);
    }

    /**
     * Returns the index of the first occurrence of the Drawable
     */
    public int indexOf(Drawable drawable) {
        return _drawables.indexOf(drawable);
    }

    /**
     * Returns the index of the last occurrence of the Drawable
     */
    public int lastIndexOf(Drawable drawable) {
        return _drawables.lastIndexOf(drawable);
    }

    /**
     * Returns the size of Drawable lists
     */
    public int size() {
        return _drawables.size();
    }



    /* operation */

    /**
     * Adds the Drawable object to the end of the list.
     *
     * @return true; See List API.
     */
    public boolean add(Drawable drawable) {
        smartUpdate("add", drawable.toJSONString(), true);
        return _drawables.add(drawable);
    }

    /**
     * Adds a Java 2D Shape.
     *
     * @param path: A Java 2D Shape
     * @return true; See List API.
     */
    public boolean add(java.awt.Shape shape) {
        if (shape instanceof java.awt.geom.Rectangle2D) {
            return add(new Rectangle((Rectangle2D) shape));
        } else {
            return add(new Path(shape));
        }
    }

    /**
     * Adds a Text. The reference point position depends on text align and text
     * baseline setting. See HTML 5 Canvas documentation.
     *
     * @param text: The String to be drawn.
     * @param x:    The X position.
     * @param y:    The Y position.
     * @return true; See List API.
     */
    public boolean add(String text, double x, double y) {
        return add(new Text(text, x, y));
    }

    /**
     * Inserts the Drawable at specific index
     */
    @SuppressWarnings("unchecked")
    public void add(int index, Drawable drawable) {
        _drawables.add(index, drawable);
        JSONObject args = new JSONObject();
        args.put("i", index);
        args.put("drw", drawable);
        smartUpdate("insert", args, true);
    }

    /**
     * Inserts a Shape at specific position
     *
     * @param shape: a Java 2D Shape
     */
    public void add(int index, java.awt.Shape shape) {
        if (shape instanceof java.awt.geom.Rectangle2D) {
            add(index, new Rectangle((Rectangle2D) shape));
        } else {
            add(index, new Path(shape));
        }
    }

    /**
     * Inserts a Text at specific index. The reference point position depends
     * on text align and text baseline setting. See HTML 5 Canvas documentation.
     *
     * @param index: The position in the list to insert Text.
     * @param text:  The String to be drawn.
     * @param x:     The X position.
     * @param y:     The Y position.
     */
    public void add(int index, String text, double x, double y) {
        add(index, new Text(text, x, y));
    }

    /**
     * Adds all drawables in the list, in the given order.
     *
     * @param drawables: A List of Drawable
     * @return true; See List API.
     */
    public boolean addAll(List<Drawable> drawables) {
        smartUpdate("addAll", JSONValue.toJSONString(drawables), true);
        return _drawables.addAll(drawables);
    }

    /**
     * Inserts all drawables in the list at the specific index
     *
     * @param index:     the index to insert the Drawables
     * @param drawables: A List of Drawable
     * @return true if the operation changes the Canvas Drawable list
     */
    @SuppressWarnings("unchecked")
    public boolean addAll(int index, List<Drawable> drawables) {
        JSONObject args = new JSONObject();
        args.put("i", index);
        args.put("drws", drawables);
        smartUpdate("insertAll", args, true);
        return _drawables.addAll(index, drawables);
    }

    /**
     * Removes the Drawable at specific index.
     *
     * @return The removed Drawable
     */
    public Drawable remove(int index) {
        smartUpdate("remove", index, true);
        return _drawables.remove(index);
    }

    /**
     * Removes a Drawable.
     *
     * @return: true if the list is changed as a result.
     */
    public boolean remove(Drawable drawable) {
        smartUpdate("remove", _drawables.indexOf(drawable), true);
        return _drawables.remove(drawable);
    }

    /**
     * Removes all Drawables contained in the list
     *
     * @return: true if the list is changed as a result.
     */
    public boolean removeAll(Collection<Drawable> drawables) {
        List<Integer> indices = new ArrayList<Integer>();
        for (int i = 0; i < _drawables.size(); i++) {
            if (drawables.contains(_drawables.get(i))) indices.add(i);
        }
        smartUpdate("removeAll", JSONValue.toJSONString(indices));
        return _drawables.removeAll(drawables);
    }

    /**
     * Retains all Drawables contained in the list
     *
     * @return: true if the list is changed as a result.
     */
    public boolean retainAll(Collection<Drawable> drawables) {
        List<Integer> indices = new ArrayList<Integer>();
        for (int i = 0; i < _drawables.size(); i++) {
            if (drawables.contains(_drawables.get(i))) indices.add(i);
        }
        smartUpdate("retainAll", JSONValue.toJSONString(indices));
        return _drawables.retainAll(drawables);
    }


    /**
     * Clears the Drawable list. The Canvas is also cleared as a result.
     */
    public void clear() {
        _drawables.clear();
        smartUpdate("clear", null);
    }

    /**
     * Replace a Drawable at specific index.
     *
     * @return The replaced Drawable
     */
    @SuppressWarnings("unchecked")
    public Drawable set(int index, Drawable drawable) {
        JSONObject args = new JSONObject();
        args.put("i", index);
        args.put("drw", drawable);
        smartUpdate("replace", args, true);
        return _drawables.set(index, drawable);
    }

    /**
     * Replace a Shape at specific index.
     *
     * @return The replaced Drawable
     */
    public Drawable set(int index, java.awt.Shape shape) {
        if (shape instanceof java.awt.geom.Rectangle2D) {
            return set(index, new Rectangle((Rectangle2D) shape));
        } else {
            return set(index, new Path(shape));
        }
    }

    /**
     * Replaces a Text at specific index. The reference point position depends
     * on text align and text baseline setting. See HTML 5 Canvas documentation.
     *
     * @param index: The index of Drawable to be replaced.
     * @param text:  The String to be drawn.
     * @param x:     The X position.
     * @param y:     The Y position.
     * @return The replaced Drawable
     */
    public Drawable set(int index, String text, double x, double y) {
        return set(index, new Text(text, x, y));
    }


    /* super */
    @Override
    protected void renderProperties(ContentRenderer renderer)
            throws IOException {

        super.renderProperties(renderer);
        render(renderer, "drwngs", JSONValue.toJSONString(_drawables));
    }

    @Override
    public String getZclass() {
        return _zclass == null ? "z-canvas" : _zclass;
    }

}
