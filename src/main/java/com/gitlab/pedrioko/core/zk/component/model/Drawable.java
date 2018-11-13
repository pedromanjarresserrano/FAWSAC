/* Drawable.java

{{IS_NOTE
	Purpose:
		Canvas prototype.
		
	Description:
		
	History:
		May 13, 2010 11:55:17 AM , Created by simon
}}IS_NOTE

Copyright (C) 2010 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
}}IS_RIGHT
*/
package com.gitlab.pedrioko.core.zk.component.model;

import org.zkoss.json.JSONAware;
import org.zkoss.json.JSONObject;

import java.awt.geom.Path2D;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author simon
 */
public abstract class Drawable implements JSONAware, Cloneable {
    private Attrs _state;
    private java.awt.Shape _clipping;

    /**
     * Subclass constructor should call super().
     */
    public Drawable() {
        _state = new Attrs();
    }

    /**
     * Returns the type of Drawable to specify how the resource should be drawn
     * on client side
     */
    //The value must match the setting in Canvas.js #_paint
    public abstract String getType();

    /**
     * Returns a JSON Object representing ONLY the shape.
     * The drawing state is covered in toJSONString()
     */
    public abstract JSONAware getShapeJSONObject();

    public String toJSONString() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("objtp", getType());
        map.put("obj", getShapeJSONObject());
        map.put("state", _state);
        return JSONObject.toJSONString(map);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }



    /* state management */

    /**
     * Uses the state from another Drawable.
     */
    public void copyStateFrom(Drawable drawable) {
        _state = new Attrs(drawable._state);
    }

    /**
     * Returns the drawing type.
     */
    public String getDrawingType() {
        return (String) get(AttrDef.DRAWING_TYPE);
    }

    /**
     * Sets the drawing type.
     *
     * @param type: Default value is FILL.
     */
    public Drawable setDrawingType(String value) {
        set(AttrDef.DRAWING_TYPE, value);
        return this;
    }

    /**
     * Removes the drawing type setting.
     */
    public void removeDrawingType() {
        remove(AttrDef.DRAWING_TYPE);
    }

    /**
     * Returns the transformation matrix entries
     *
     * @return A double array of length 6, with entries representing m11, m12
     * m21, m22, dx, dy respectively
     */
    public double[] getTransformation() {
        return (double[]) get(AttrDef.TRANSFORMATION);
    }

    /**
     * Sets transformation matrix by a double Array.
     *
     * @param value: must be a double Array of length 6, with entries
     *               representing m11, m12, m21, m22, dx, dy respectively.
     */
    public Drawable setTransformation(double[] value) {
        boolean isDefault = true;
        for (int i = 0; i < 6; i++) {
            if (value[i] != Transformation.DEFAULT[i]) isDefault = false;
        }
        if (isDefault) {
            remove(AttrDef.TRANSFORMATION);
        } else {
            set(AttrDef.TRANSFORMATION, value);
        }
        return this;
    }

    /**
     * Sets the transformation matrix. Note that the default value of m11 and
     * m22 are 1, while the rests are 0.
     */
    public Drawable setTransformation(double m11, double m12, double m21,
                                      double m22, double dx, double dy) {

        double[] newMatrix = {m11, m12, m21, m22, dx, dy};
        setTransformation(newMatrix);
        return this;
    }

    /**
     * Removes the transformation setting.
     */
    public void removeTransformation() {
        remove(AttrDef.TRANSFORMATION);
    }

    /**
     * Returns the clipping region.
     */
    public java.awt.Shape getClipping() {
        return _clipping;
    }

    /**
     * Sets the clipping region.
     *
     * @param clipping: Can be any java.awt.Shape
     */
    public Drawable setClipping(java.awt.Shape shape) {
        // Java 2D uses Bezier curve to simulate arc when passing into Path2D
        // so passing an Arc here will turn it into a path formed by Bezier curves
        _clipping = shape;
        set(AttrDef.CLIPPING, new Path(new Path2D.Double(shape)));
        return this;
    }


    // Clipping

    /**
     * Removes the clipping region setting.
     */
    public void removeClipping() {
        _clipping = null;
        remove(AttrDef.CLIPPING);
    }

    /**
     * Returns stroke style.
     *
     * @return
     */
    public Object getStrokeStyle() {
        return get(AttrDef.STROKE_STYLE);
    }

    /**
     * Sets strokeStyle as a CSS color.
     *
     * @param cssColor: must be a valid CSS color string.
     */
    public Drawable setStrokeStyle(String cssColor) {
        set(AttrDef.STROKE_STYLE, cssColor);
        return this;
    }


    // Stroke Style

    /**
     * Removes strokeStyle setting.
     *
     * @return
     */
    public void removeStrokeStyle() {
        remove(AttrDef.STROKE_STYLE);
    }

    /**
     * Returns fill style.
     *
     * @return
     */
    public Object getFillStyle() {
        return get(AttrDef.FILL_STYLE);
    }

    // TODO: support gradient and pattern
    //public void setStrokeStyle(GradientPaint gradient){
    //}

    /**
     * Sets fillStyle as a CSS color.
     *
     * @param cssColor: must be a valid CSS color string.
     */
    public Drawable setFillStyle(String cssColor) {
        set(AttrDef.FILL_STYLE, cssColor);
        return this;
    }


    // Fill Style

    /**
     * Removes fillStyle setting.
     *
     * @return
     */
    public void removeFillStyle() {
        remove(AttrDef.FILL_STYLE);
    }

    /**
     * Returns the alpha value.
     */
    public double getAlpha() {
        return (Double) get(AttrDef.ALPHA);
    }

    // TODO: support gradient and pattern
    //public void setFillStyle(GradientPaint gradient){
    //}

    /**
     * Sets the alpha value.
     *
     * @param alpha: must be in range [0,1].
     *               Any value less then 0 or greater than 1 is ignored.
     */
    public Drawable setAlpha(double value) {
        if (value < 0 || value > 1) return this;
        set(AttrDef.ALPHA, value);
        return this;
    }


    // Alpha

    /**
     * Removes alpha setting.
     */
    public void removeAlpha() {
        remove(AttrDef.ALPHA);
    }

    /**
     * Gets line width setting.
     */
    public double getLineWidth() {
        return (Double) get(AttrDef.LINE_WIDTH);
    }

    /**
     * Sets line width. Default value is 1.
     */
    public Drawable setLineWidth(double value) {
        set(AttrDef.LINE_WIDTH, value);
        return this;
    }


    // Line Width

    /**
     * Removes line width setting
     */
    public void removeLineWidth() {
        remove(AttrDef.LINE_WIDTH);
    }

    /**
     * Gets line cap setting.
     */
    public String getLineCap() {
        return (String) get(AttrDef.LINE_CAP);
    }

    /**
     * Sets line cap type. Default value is BUTT.
     */
    public Drawable setLineCap(String value) {
        set(AttrDef.LINE_CAP, value);
        return this;
    }

    /**
     * Removes line cap setting.
     */
    public void removeLineCap() {
        remove(AttrDef.LINE_CAP);
    }

    /**
     * Gets line join setting.
     */
    public String getLineJoin() {
        return (String) get(AttrDef.LINE_JOIN);
    }

    /**
     * Sets line join type. Default value is MITER
     */
    public Drawable setLineJoin(String value) {
        set(AttrDef.LINE_JOIN, value);
        return this;
    }

    /**
     * Removes line join setting.
     */
    public void removeLineJoin() {
        remove(AttrDef.LINE_JOIN);
    }

    /**
     * Get miter limit setting.
     */
    public double getMiterLimit() {
        return (Double) get(AttrDef.MITER_LIMIT);
    }

    /**
     * Sets miter limit. Default value is 10.
     */
    public Drawable setMiterLimit(double value) {
        set(AttrDef.MITER_LIMIT, value);
        return this;
    }

    /**
     * Removes miter limit setting.
     */
    public void removeMiterLimit() {
        remove(AttrDef.MITER_LIMIT);
    }

    /**
     * Returns shadow offset X value.
     */
    public double getShadowOffsetX() {
        return (Double) get(AttrDef.SHADOW_OFFSET_X);
    }


    // Miter Limit

    /**
     * Returns shadow offset Y value.
     */
    public double getShadowOffsetY() {
        return (Double) get(AttrDef.SHADOW_OFFSET_Y);
    }

    /**
     * Sets shadow offset.
     */
    public Drawable setShadowOffset(double x, double y) {
        set(AttrDef.SHADOW_OFFSET_X, x);
        set(AttrDef.SHADOW_OFFSET_Y, y);
        return this;
    }

    /**
     * Removes shadow offset setting.
     */
    public void removeShadowOffset() {
        remove(AttrDef.SHADOW_OFFSET_X);
        remove(AttrDef.SHADOW_OFFSET_Y);
    }


    // Shadow Offset

    /**
     * Returns shadow blur value.
     */
    public double getShadowBlur() {
        return (Double) get(AttrDef.SHADOW_BLUR);
    }

    /**
     * Sets shadow blur value.
     */
    public Drawable setShadowBlur(double value) {
        set(AttrDef.SHADOW_BLUR, value);
        return this;
    }

    /**
     * Removes shadow blur setting.
     */
    public void removeShadowBlur() {
        remove(AttrDef.SHADOW_BLUR);
    }

    /**
     * Returns shadow color as a css color string.
     */
    public String getShadowColor() {
        return (String) get(AttrDef.SHADOW_COLOR);
    }


    // Shadow Blur

    /**
     * Sets shadow color
     *
     * @param cssColor: must be a valid css color
     */
    public Drawable setShadowColor(String cssColor) {
        set(AttrDef.SHADOW_COLOR, cssColor);
        return this;
    }

    /**
     * Removes shadow color setting.
     */
    public void removeShadowColor() {
        remove(AttrDef.SHADOW_COLOR);
    }

    /**
     * Returns the composite value.
     */
    public String getComposite() {
        return (String) get(AttrDef.COMPOSITE);
    }


    // Shadow Color

    /**
     * Sets the composite value.
     *
     * @param composite: must be chosen from the given constant strings in
     *                   Drawable.Composite, or a string that specifies a vendor-specific
     *                   composite operation. (See HTML Canvas doc)
     */
    public Drawable setComposite(String value) {
        set(AttrDef.COMPOSITE, value);
        return this;
    }

    /**
     * Removes the composite setting.
     */
    public void removeComposite() {
        remove(AttrDef.COMPOSITE);
    }

    /**
     * Returns font setting.
     */
    public String getFont() {
        return (String) get(AttrDef.FONT);
    }

    /**
     * Sets the font.
     */
    public Drawable setFont(String value) {
        set(AttrDef.FONT, value);
        return this;
    }

    /**
     * Removes font setting.
     */
    public void removeFont() {
        remove(AttrDef.FONT);
    }

    /**
     * Returns text align setting.
     */
    public String getTextAlign() {
        return (String) get(AttrDef.TEXT_ALIGN);
    }

    /**
     * Sets text align.
     *
     * @param value:
     */
    public Drawable setTextAlign(String value) {
        set(AttrDef.TEXT_ALIGN, value);
        return this;
    }


    // Font

    /**
     * Removes text align
     */
    public void removeTextAlign() {
        remove(AttrDef.TEXT_ALIGN);
    }

    /**
     * Returns text baseline setting.
     */
    public String getTextBaseline() {
        return (String) get(AttrDef.TEXT_BASELINE);
    }

    /**
     * Sets text baseline.
     */
    public Drawable setTextBaseline(String value) {
        set(AttrDef.TEXT_BASELINE, value);
        return this;
    }

    /**
     * Removes text baseline setting
     */
    public void removeTextBaseline() {
        remove(AttrDef.TEXT_BASELINE);
    }

    /**
     * Returns text max width setting.
     */
    public double getTextMaxWidth() {
        return (Double) get(AttrDef.TEXT_MAX_WIDTH);
    }

    /**
     * Sets text max width. Default value is -1.
     *
     * @param value: Any value less than 0 means no limit on maximal width
     */
    public Drawable setTextMaxWidth(double value) {
        set(AttrDef.TEXT_MAX_WIDTH, value);
        return this;
    }

    /**
     * Removes text max width limit
     */
    public void removeTextMaxWidth() {
        remove(AttrDef.TEXT_MAX_WIDTH);
    }

    /* helper */
    // a special generic getter that returns default value if attribute is unset.
    private Object get(AttrDef def) {
        return get(def, true);
    }

    private Object get(AttrDef def, boolean returnDefaultValue) {
        Object result = _state.get(def.getKey());
        return (returnDefaultValue && result == null) ? def.getDefault() : result;
    }

    // a special setter that removes the attribute when set to default value
    @SuppressWarnings("unchecked")
    private void set(AttrDef def, Object value) {
        if (value.equals(def.getDefault())) {
            _state.remove(def.getKey());
        } else {
            _state.put(def.getKey(), value);
        }
    }

    private Object remove(AttrDef def) {
        return _state.remove(def.getKey());
    }


    // Text Max Width
    /*
     * NOT defined in DOM Canvas spec
     * In DOM Canvas a different function is called when you need to specify
     * the max width. To organize all attributes into Drawable, we keep a value
     * of max width here. When max width is set to negative number, it means
     * no limit on max width.
     */

    /*
     * private constants helpers (for component developer)
     *
     * These values are the keys of Drawing State attributes when converted
     * into a JSON object. It must exactly match the setting in Canvas.js
     * #_setDOMContextState
     */
    private enum AttrDef {
        DRAWING_TYPE("dwtp", DrawingType.DEFAULT),
        TRANSFORMATION("trns", null), // manually handle default check
        CLIPPING("clp", null),
        STROKE_STYLE("strk", "#000000"),
        FILL_STYLE("fil", "#000000"),
        ALPHA("alfa", 1.0),
        LINE_WIDTH("lnw", 1),
        LINE_CAP("lncp", LineCap.DEFAULT),
        LINE_JOIN("lnj", LineJoin.DEFAULT),
        MITER_LIMIT("mtr", 10),
        SHADOW_OFFSET_X("shx", 0),
        SHADOW_OFFSET_Y("shy", 0),
        SHADOW_BLUR("shb", 0),
        SHADOW_COLOR("shc", ""), // default transparent black
        COMPOSITE("cmp", Composite.DEFAULT),
        FONT("fnt", "10px sans-serif"),
        TEXT_ALIGN("txal", TextAlign.DEFAULT),
        TEXT_BASELINE("txbl", TextBaseline.DEFAULT),
        TEXT_MAX_WIDTH("txmw", -1);

        String _key;
        Object _defaultValue;

        private AttrDef(String key, Object defaultValue) {
            _key = key;
            _defaultValue = defaultValue;
        }

        public String getKey() {
            return _key;
        }

        public Object getDefault() {
            return _defaultValue;
        }
    }

    // Drawing Type
    /*
     * NOT defined in DOM Spec but introduced for better concept organization.
     * See Canvas.js #_paintRect, #_paintPath, #_paintText
     */
    public static class DrawingType {
        public static final String NONE = "none";
        public static final String FILL = "fill";
        public static final String STROKE = "stroke";
        public static final String BOTH = "both";

        public static final String DEFAULT = FILL;
    }

    // Transformation
    public static class Transformation {
        private static final double[] DEFAULT = {1, 0, 0, 1, 0, 0};
    }

    /* end of state management */

    // Line Cap
    /*
     * String values are defined in DOM Canvas spec
     */
    public static class LineCap {
        public static final String BUTT = "butt";
        public static final String ROUND = "round";
        public static final String SQUARE = "square";

        public static final String DEFAULT = BUTT;
    }

    // Line Join
    /*
     * String values are defined in DOM Canvas spec
     */
    public static class LineJoin {
        public static final String ROUND = "round";
        public static final String BEVEL = "bevel";
        public static final String MITER = "miter";

        public static final String DEFAULT = MITER;
    }

    // Composite
    /*
     * These values are exactly the String values defined in DOM Canvas spec,
     * and will be packed into JSON object as Map values. Some of them are
     * long Strings, but using it directly saves the work (and trouble) to
     * convert it back at client side.
     */
    public static class Composite {
        public static final String SRC_ATOP = "source-atop";
        public static final String SRC_IN = "source-in";
        public static final String SRC_OUT = "source-out";
        public static final String SRC_OVER = "source-over";
        public static final String DEST_ATOP = "destination-atop";
        public static final String DEST_IN = "destination-in";
        public static final String DEST_OUT = "destination-out";
        public static final String DEST_OVER = "destination-over";
        public static final String LIGHTER = "lighter";
        public static final String COPY = "copy";
        public static final String XOR = "xor";

        public static final String DEFAULT = SRC_OVER;
    }

    // Text Alignment
    /*
     * String values are defined in DOM Canvas spec
     */
    public static class TextAlign {
        public static final String START = "start";
        public static final String END = "end";
        public static final String LEFT = "left";
        public static final String RIGHT = "right";
        public static final String CENTER = "center";

        public static final String DEFAULT = START;
    }

    // Text Baseline
    /*
     * String values are defined in DOM Canvas spec
     */
    public static class TextBaseline {
        public static final String TOP = "top";
        public static final String HANGING = "hanging";
        public static final String MIDDLE = "middle";
        public static final String ALPHABETIC = "alphabetic";
        public static final String IDEOGRAPHIC = "ideographic";
        public static final String BOTTOM = "bottom";

        public static final String DEFAULT = ALPHABETIC;
    }

    // An utility class to store attribute data as a Map
    @SuppressWarnings("serial")
    private static class Attrs extends JSONObject {

        /* copy & merge */
        private Attrs() {
        }

        private Attrs(Attrs attr2) {
            copyFrom(attr2);
        }

        // copy values from attrs, always overwrites
        @SuppressWarnings("unchecked")
        private void copyFrom(Attrs attr2) {
            Iterator<Map.Entry<Object, Object>> itr = attr2.entrySet().iterator();
            while (itr.hasNext()) {
                Map.Entry<Object, Object> e = itr.next();
                put(e.getKey(), e.getValue());
            }
        }
    }
}