/* Rectangle.java

{{IS_NOTE
	Purpose:
		Canvas prototype.
		
	Description:
		
	History:
		May 13, 2010 12:35:23 PM , Created by simon
}}IS_NOTE

Copyright (C) 2010 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
}}IS_RIGHT
*/
package com.gitlab.pedrioko.core.zk.component.model;

import org.zkoss.json.JSONAware;
import org.zkoss.json.JSONObject;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * @author simon
 */
public class Rectangle extends Shape {

    /**
     * Constructs a Rectangle at (0,0) with 0 width and height
     */
    public Rectangle() {
        _internalShape = new Rectangle2D.Double();
    }

    /**
     * Creates a new Rectangle.
     *
     * @param x: x-position
     * @param y: y-position
     * @param w: width
     * @param h: height
     */
    public Rectangle(double x, double y, double w, double h) {
        _internalShape = new Rectangle2D.Double(x, y, w, h);
    }

    /**
     * Creates a new Rectangle based on the given Rectangle2D.Float
     */
    public Rectangle(Rectangle2D rect) {
        _internalShape = (java.awt.Shape) rect.clone();
    }

    /**
     * Creates a new Rectangle by cloning another
     */
    public Rectangle(Rectangle rect) {
        this((Rectangle2D.Double) rect._internalShape);
    }


    /**
     * Sets the position of Rectangle
     *
     * @param x: The X coordinate.
     * @param y: The Y coordinate.
     */
    public Rectangle setPosition(double x, double y) {
        ((Rectangle2D.Double) _internalShape).x = x;
        ((Rectangle2D.Double) _internalShape).y = y;
        return this;
    }

    /**
     * Sets the size of Rectangle
     */
    public Rectangle setSize(double width, double height) {
        ((Rectangle2D.Double) _internalShape).width = width;
        ((Rectangle2D.Double) _internalShape).height = height;
        return this;
    }


    /* interface implementation */
    @Override
    public String getType() {
        // This value must match the setting in Canvas.js #_paint
        return "rect";
    }

    @SuppressWarnings("unchecked")
    @Override
    public JSONAware getShapeJSONObject() {
        Rectangle2D.Double rect = (Rectangle2D.Double) _internalShape;
        JSONObject m = new JSONObject();
        m.put("x", rect.x);
        m.put("y", rect.y);
        m.put("w", rect.width);
        m.put("h", rect.height);
        return m;
    }

    @Override
    public Object clone() {
        return new Rectangle(this);
    }


    /* delegation: operation */
    public Rectangle setRect(double x, double y, double w, double h) {
        ((Rectangle2D.Double) _internalShape).setRect(x, y, w, h);
        return this;
    }

    public Rectangle setRect(Rectangle2D r) {
        ((Rectangle2D.Double) _internalShape).setRect(r);
        return this;
    }

    public Rectangle add(double newx, double newy) {
        ((Rectangle2D.Double) _internalShape).add(newx, newy);
        return this;
    }

    public Rectangle add(Point2D pt) {
        ((Rectangle2D.Double) _internalShape).add(pt);
        return this;
    }

    public Rectangle add(Rectangle2D r) {
        ((Rectangle2D.Double) _internalShape).add(r);
        return this;
    }

    /* delegation: query */
    public double getCenterX() {
        return ((Rectangle2D.Double) _internalShape).getCenterX();
    }

    public double getCenterY() {
        return ((Rectangle2D.Double) _internalShape).getCenterY();
    }

    public double getHeight() {
        return ((Rectangle2D.Double) _internalShape).getHeight();
    }

    public double getMaxX() {
        return ((Rectangle2D.Double) _internalShape).getMaxX();
    }

    public double getMaxY() {
        return ((Rectangle2D.Double) _internalShape).getMaxY();
    }

    public double getMinX() {
        return ((Rectangle2D.Double) _internalShape).getMinX();
    }

    public double getMinY() {
        return ((Rectangle2D.Double) _internalShape).getMinY();
    }

    public double getWidth() {
        return ((Rectangle2D.Double) _internalShape).getWidth();
    }

    public double getX() {
        return ((Rectangle2D.Double) _internalShape).getX();
    }

    public double getY() {
        return ((Rectangle2D.Double) _internalShape).getY();
    }

    public boolean intersectsLine(double x1, double y1, double x2, double y2) {
        return ((Rectangle2D.Double) _internalShape).intersectsLine(x1, y1, x2, y2);
    }

    public boolean intersectsLine(Line2D l) {
        return ((Rectangle2D.Double) _internalShape).intersectsLine(l);
    }

    public boolean isEmpty() {
        return ((Rectangle2D.Double) _internalShape).isEmpty();
    }

    public int outcode(double x, double y) {
        return ((Rectangle2D.Double) _internalShape).outcode(x, y);
    }

    public int outcode(Point2D p) {
        return ((Rectangle2D.Double) _internalShape).outcode(p);
    }


    public String toString() {
        return ((Rectangle2D.Double) _internalShape).toString();
    }
    /* end of delegation */

}