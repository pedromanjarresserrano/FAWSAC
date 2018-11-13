/* Drawable.js

	Purpose:
		Canvas prototype.
		
	Description:
		
	History:
		May 18, 2010 5:16:04 PM , Created by simon

Copyright (C) 2010 Potix Corporation. All Rights Reserved.

*/

/**
 * The class for drawable objects in Canvas. Its subclasses include Shape,
 * Text, ImageSource (not available yet)
 *
 * ImageSource should have subclasses Image, CanvasImage, Video.
 *
 */
zkforge.canvas.Drawable = zk.$extends(zk.Object, {
    objtp: "",
    obj: null,
    state: null,

    $init: function () {
        this.state = new Object();
    },

    /**
     * Returns drawing type.
     */
    getDrawingType: function () {
        return this.state.dwtp;
    },

    /**
     * Sets drawing type.
     */
    setDrawingType: function (v) {
        this.state.dwtp = v;
        return this;
    },

    /**
     * Returns transformation.
     */
    getTransformation: function () {
        return this.state.trns;
    },

    /**
     * Sets transformation
     */
    setTransformation: function (v) {
        this.state.trns = v;
        return this;
    },

    /**
     * Returns transformation.
     */
    getClipping: function () {
        return this.state.clp;
    },

    /**
     * Sets transformation
     */
    setClipping: function (v) {
        this.state.clp = v;
        return this;
    },

    /**
     * Returns transformation.
     */
    getStrokeStyle: function () {
        return this.state.strk;
    },

    /**
     * Sets transformation
     */
    setStrokeStyle: function (v) {
        this.state.strk = v;
        return this;
    },

    /**
     * Returns fill style.
     */
    getFillStyle: function () {
        return this.state.fil;
    },

    /**
     * Sets fill style.
     */
    setFillStyle: function (v) {
        this.state.fil = v;
        return this;
    },

    /**
     * Returns alpha.
     */
    getAlpha: function () {
        return this.state.alfa;
    },

    /**
     * Sets alpha.
     */
    setAlpha: function (v) {
        this.state.alfa = v;
        return this;
    },

    /**
     * Returns line width.
     */
    getLineWidth: function () {
        return this.state.lnw;
    },

    /**
     * Sets line width.
     */
    setLineWidth: function (v) {
        this.state.lnw = v;
        return this;
    },

    /**
     * Returns line cap.
     */
    getLineCap: function () {
        return this.state.lncp;
    },

    /**
     * Sets line cap.
     */
    setLineCap: function (v) {
        this.state.lncp = v;
        return this;
    },

    /**
     * Returns line join.
     */
    getLineJoin: function () {
        return this.state.lnj;
    },

    /**
     * Sets line join.
     */
    setLineJoin: function (v) {
        this.state.lnj = v;
        return this;
    },

    /**
     * Returns miter limit.
     */
    getMiterLimit: function () {
        return this.state.mtr;
    },

    /**
     * Sets miter limit.
     */
    setMiterLimit: function (v) {
        this.state.mtr = v;
        return this;
    },

    /**
     * Returns shadow offset X.
     */
    getShadowOffsetX: function () {
        return this.state.shx;
    },

    /**
     * Sets shadow offset X;
     */
    setShadowOffsetX: function (v) {
        this.state.shx = v;
        return this;
    },

    /**
     * Returns shadow offset Y.
     */
    getShadowOffsetY: function () {
        return this.state.shy;
    },

    /**
     * Sets shadow offset Y.
     */
    setShadowOffsetY: function (v) {
        this.state.shy = v;
        return this;
    },

    /**
     * Returns shadow blur.
     */
    getShadowBlur: function () {
        return this.state.shb;
    },

    /**
     * Sets shadow blur.
     */
    setShadowBlur: function (v) {
        this.state.shb = v;
        return this;
    },

    /**
     * Returns shadow color.
     */
    getShadowColor: function () {
        return this.state.shc;
    },

    /**
     * Sets shadow color.
     */
    setShadowColor: function (v) {
        this.state.shc = v;
        return this;
    },

    /**
     * Returns composite.
     */
    getComposite: function () {
        return this.state.cmp;
    },

    /**
     * Sets composite.
     */
    setComposite: function (v) {
        this.state.cmp = v;
        return this;
    },

    /**
     * Returns font.
     */
    getFont: function () {
        return this.state.fnt;
    },

    /**
     * Sets font.
     */
    setFont: function (v) {
        this.state.fnt = v;
        return this;
    },

    /**
     * Returns text align.
     */
    getTextAlign: function () {
        return this.state.txal;
    },

    /**
     * Sets text align.
     */
    setTextAlign: function (v) {
        this.state.txal = v;
        return this;
    },

    /**
     * Returns text baseline.
     */
    getTextBaseline: function () {
        return this.state.txbl;
    },

    /**
     * Sets text baseline.
     */
    setTextBaseline: function (v) {
        this.state.txbl = v;
        return this;
    },

    /**
     * Returns text max width.
     */
    getTextMaxWidth: function () {
        return this.state.txmw;
    },

    /**
     * Sets text max width.
     */
    setTextMaxWidth: function (v) {
        this.state.txmw = v;
        return this;
    },


    // copy state data from drw
    _copyState: function (drw) {
        this.state.dwtp = drw.state.dwtp;
        this.state.strk = drw.state.strk;
        this.state.fil = drw.state.fil;
        this.state.alfa = drw.state.alfa;
        this.state.lnw = drw.state.lnw;
        this.state.lncp = drw.state.lncp;
        this.state.lnj = drw.state.lnj;
        this.state.mtr = drw.state.mtr;
        this.state.shx = drw.state.shx;
        this.state.shy = drw.state.shy;
        this.state.shb = drw.state.shb;
        this.state.shc = drw.state.shc;
        this.state.cmp = drw.state.cmp;
        this.state.fnt = drw.state.fnt;
        this.state.txal = drw.state.txal;
        this.state.txbl = drw.state.txbl;
        this.state.txmw = drw.state.txmw;

        if (drw.state.trns != null) {
            this.state.trns = [];
            for (i = 6; i--;) {
                this.state.trns[i] = drw.state.trns[i];
            }
        } else {
            this.state.trns = null;
        }

        if (drw.state.clp != null) {
            this.state.clp = new zkforge.canvas.Path();
            this.state.clp._copyObj(drw.state.clp);
        } else {
            this.state.clp = null;
        }
    }
});
