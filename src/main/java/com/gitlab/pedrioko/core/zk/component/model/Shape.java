/* Shape.java

{{IS_NOTE
	Purpose:
		Canvas prototype.
		
	Description:
		
	History:
		May 13, 2010 12:00:15 PM , Created by simon
}}IS_NOTE

Copyright (C) 2010 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
}}IS_RIGHT
*/
package com.gitlab.pedrioko.core.zk.component.model;

import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * A wrapper of java.awt.Shape that implements Drawable
 *
 * @author simon
 */
public abstract class Shape extends Drawable {
    protected java.awt.Shape _internalShape;

    public Shape() {
        super();
    }

    /* delegation */
    public boolean contains(double x, double y) {
        return _internalShape.contains(x, y);
    }

    public boolean contains(Point2D p) {
        return _internalShape.contains(p);
    }

    public boolean intersects(double x, double y, double w, double h) {
        return _internalShape.intersects(x, y, w, h);
    }

    public boolean intersects(Rectangle2D r) {
        return _internalShape.intersects(r);
    }

    public boolean contains(double x, double y, double w, double h) {
        return _internalShape.contains(x, y, w, h);
    }

    public boolean contains(Rectangle2D r) {
        return _internalShape.contains(r);
    }

    public PathIterator getPathIterator(AffineTransform at) {
        return _internalShape.getPathIterator(at);
    }

    public PathIterator getPathIterator(AffineTransform at, double flatness) {
        return _internalShape.getPathIterator(at, flatness);
    }

}