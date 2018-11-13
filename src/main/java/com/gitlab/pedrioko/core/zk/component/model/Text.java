/* Text.java

{{IS_NOTE
	Purpose:
		Canvas prototype.
		
	Description:
		
	History:
		May 18, 2010 7:25:28 PM , Created by simon
}}IS_NOTE

Copyright (C) 2010 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
}}IS_RIGHT
*/
package com.gitlab.pedrioko.core.zk.component.model;

import org.zkoss.json.JSONAware;
import org.zkoss.json.JSONObject;

/**
 * @author simon
 */
public class Text extends Drawable {
    private String _text;
    private double _x;
    private double _y;

    /**
     * Constructs a new Text object. The position reference point depends on
     * text align and text baseline (in drawing state).
     *
     * @param text: string to paint
     * @param x:    X position
     * @param y:    Y position
     */
    public Text(String text, double x, double y) {
        _text = text;
        _x = x;
        _y = y;
    }

    /**
     * Constructs a new Text object by cloning another.
     */
    public Text(Text text) {
        this(text._text, text._x, text._y);
    }

    /**
     * Returns X position.
     */
    public double getX() {
        return _x;
    }

    /**
     * Returns Y position.
     */
    public double getY() {
        return _y;
    }

    /**
     * Returns the text string.
     */
    public String getText() {
        return _text;
    }

    /**
     * Sets the text to be drawn.
     */
    public Text setText(String text) {
        _text = text;
        return this;
    }

    /**
     * Sets the position.
     */
    public Text setPosition(double x, double y) {
        _x = x;
        _y = y;
        return this;
    }

    /* interface implementation */
    @SuppressWarnings("unchecked")
    @Override
    public JSONAware getShapeJSONObject() {
        JSONObject obj = new JSONObject();
        obj.put("t", _text);
        obj.put("x", _x);
        obj.put("y", _y);
        return obj;
    }

    @Override
    public String getType() {
        return "text";
    }

    @Override
    public Object clone() {
        return new Text(this);
    }

}