/* Canvas.js

	Purpose:
		Canvas prototype.
		
	Description:
		
	History:
		May 12, 2010 3:17:24 PM , Created by simon

Copyright (C) 2010 Potix Corporation. All Rights Reserved.

*/

/**
 * The ZK component corresponding to HTML 5 Canvas.
 * While HTML 5 Canvas is a command-based DOM object that allows user to draw
 * items on a surface, ZK Canvas maintains a list of drawable items and allow
 * user to operate the list by adding, removing, updating, replacing the
 * elements. The changes will be reflected on the client side upon these
 * operations.
 *
 * <p>Default {@link #getZclass}: z-canvas.
 */
zkforge.canvas.Canvas = zk.$extends(zul.Widget, {
    _cvs: null,
    _ctx: null,
    _drwbls: [],
    _states: [],

    // extended drawing states
    _drwTp: "fill",
    _drwTpBak: "fill",
    _txtMxW: -1,
    _txtMxWBak: -1,

    bind_: function () {
        this.$supers("bind_", arguments);

        this._cvs = document.createElement("canvas");

        var w = this.$n().clientWidth;
        var h = this.$n().clientHeight;
        if (w) this._cvs.width = zk.parseInt(w);
        if (h) this._cvs.height = zk.parseInt(h);

        jq(this.$n()).append(this._cvs);
        if (zk.ie) G_vmlCanvasManager.initElement(this._cvs);

        this._cvs.id = this.uuid + '-cnt';
        //if (!zk.ie) this._cvs.class = this.getZclass() + '-cnt'; // comfirm zclass spec

        this._ctx = this._cvs.getContext("2d");

    },

    setDrwngs: function (v) {
        this._drwbls = jq.evalJSON(v);
    },


    // add
    setAdd: function (drwJSON) {
        this.add(jq.evalJSON(drwJSON));
    },

    /**
     * Adds a Drawable to canvas.
     */
    add: function (drw) {
        this._paint(drw);
        this._drwbls.push(drw);
    },


    // add all
    setAddAll: function (drwsJSON) {
        this.addALL(jq.evalJSON(drwsJSON));
    },

    /**
     * Adds all the drawables in the array in the order.
     */
    addALL: function (drws) {
        var ln = drws.length;
        for (var i = 0; i < ln; i++) this._paint(drws[i]);
        this._drwbls = this._drwbls.concat(drws);
    },


    // remove
    setRemove: function (index) {
        this.remove(index);
    },

    /**
     * Removes the Drawable at specific index.
     */
    remove: function (index) {
        var drw = this._drwbls.splice(index, 1);
        this._repaint();
        return drw;
    },


    // remove all
    setRemoveAll: function (idsJSON) {
        this.removeAll(jq.evalJSON(idsJSON));
    },

    /**
     * Removes all the Drawables based on an array of indices
     */
    removeAll: function (indices) {
        for (var i = indices.length; i--;) this._drwbls.splice(indices[i], 1);
        this._repaint();
    },


    // retain all
    setRetainAll: function (idsJSON) {
        this.retainAll(jq.evalJSON(idsJSON));
    },

    /**
     * Retains all the Drawables based on an array of indices
     */
    retainAll: function (indices) {
        var drwbls2 = [];
        var ln = indices.length;
        for (var i = 0; i < ln; i++) drwbls2.push(this._drwbls[indices[i]]);
        this._drwbls = drwbls2;
        this._repaint();
    },


    // insert
    setInsert: function (idrwJSON) {
        var idrw = jq.evalJSON(idrwJSON);
        this.insert(idrw.i, idrw.drw);
    },

    /**
     * Inserts a Drawable at specific index.
     */
    insert: function (index, drw) {
        this._drwbls.splice(index, 0, drw);
        this._repaint();
    },


    // insert all
    setInsertAll: function (idrwsJSON) {
        var idrws = jq.evalJSON(idrwJSON);
        this.insertAll(idrws.i, idrws.drws);
    },

    /**
     * Inserts all Drawables in the list
     */
    insertAll: function (index, drws) {
        this._drwbls = this._drwbls.slice(0, index).concat(drws)
            .concat(this._drwbls.slice(index));
        this._repaint();
    },


    // replace
    setReplace: function (idrwJSON) {
        var idrw = jq.evalJSON(idrwJSON);
        this.replace(idrw.i, idrw.drw);
    },

    /**
     * Replace the Drawable at specific index.
     */
    replace: function (index, drw) {
        var removed = this._drwbls.splice(index, 1, drw);
        this._repaint();
        return removed[0];
    },


    // clear
    setClear: function () {
        this.clear();
    },

    /**
     * Remove all Drawables.
     */
    clear: function () {
        this._drwbls = [];
        this._clearCanvas();
    },


    // private //
    _clearCanvas: function () {
        this._ctx.clearRect(0, 0, this._cvs.width, this._cvs.height);
    },

    _repaint: function () {
        this._clearCanvas();
        var ln = this._drwbls.length;

        for (var i = 0; i < ln; i++) {
            this._paint(this._drwbls[i]);
        }
    },

    _paint: function (drw) {
        this._applyLocalState(drw.state);

        // the type value must match the return value of Drawable#getType()
        switch (drw.objtp) {
            case "rect":
                this._paintRect(drw.obj);
                break;
            case "path":
                this._paintPath(drw.obj);
                break;
            case "text":
                this._paintText(drw.obj);
                break;
            case "img":
            //break;
            default:
            // unsupported types
        }

        this._unapplyLocalState();
    },

    _paintRect: function (rect) {
        switch (this._drwTp) {
            case "none":
                break;
            case "stroke":
                this._ctx.strokeRect(rect.x, rect.y, rect.w, rect.h);
                break;
            case "both":
                // outline shadow
                this._ctx.save();
                this._ctx.globalAlpha = 0.01;
                this._ctx.strokeRect(rect.x, rect.y, rect.w, rect.h);
                this._ctx.restore();

                this._ctx.fillRect(rect.x, rect.y, rect.w, rect.h);

                // stroke outline without shadow
                this._ctx.save();
                this._clearShadow();
                this._ctx.strokeRect(rect.x, rect.y, rect.w, rect.h);
                this._ctx.restore();
                break;
            case "fill":
            default:
                this._ctx.fillRect(rect.x, rect.y, rect.w, rect.h);
        }
    },

    _paintPath: function (path) {
        // mimic path drawing based on data
        this._drawPath(path);

        switch (this._drwTp) {
            case "none":
                break;
            case "stroke":
                this._ctx.stroke();
                break;
            case "both":
                // outline shadow
                this._ctx.save();
                this._ctx.globalAlpha = 0.01;
                this._ctx.stroke();
                this._ctx.restore();

                this._ctx.fill();

                // stroke outline without shadow
                this._ctx.save();
                this._clearShadow();
                this._ctx.stroke();
                this._ctx.restore();
                break;
            case "fill":
            default:
                this._ctx.fill();
        }

        this._ctx.beginPath();
    },

    _drawPath: function (path) {
        var segments = path.sg;
        this._ctx.beginPath();

        for (var i = 0, len = segments.length; i < len; i++) {
            var data = segments[i].dt;
            switch (segments[i].tp) {
                case "mv":
                    this._ctx.moveTo(data[0], data[1]);
                    break;
                case "ln":
                    this._ctx.lineTo(data[0], data[1]);
                    break;
                case "qd":
                    this._ctx.quadraticCurveTo(data[0], data[1], data[2], data[3]);
                    break;
                case "bz":
                    this._ctx.bezierCurveTo(
                        data[0], data[1], data[2], data[3], data[4], data[5]);
                    break;
                // acrTo
                case "cl":
                    this._ctx.closePath();
            }
        }
    },

    _paintText: function (text) {
        switch (this._drwTp) {
            case "none":
                break;
            case "stroke":
                this._strkTxt(text);
                break;
            case "both":
                // outline shadow
                this._ctx.save();
                this._ctx.globalAlpha = 0.01;
                this._strkTxt(text);
                this._ctx.restore();

                this._filTxt(text);

                // stroke outline without shadow
                this._ctx.save();
                this._clearShadow();
                this._strkTxt(text);
                this._ctx.restore();
                break;
            case "fill":
            default:
                this._filTxt(text);
        }
    },

    _strkTxt: function (text) {
        if (this._txtMxW < 0) {
            this._ctx.strokeText(text.t, text.x, text.y);
        } else {
            this._ctx.strokeText(text.t, text.x, text.y, this._txtMxW);
        }
    },

    _filTxt: function (text) {
        if (this._txtMxW < 0) {
            this._ctx.fillText(text.t, text.x, text.y);
        } else {
            this._ctx.fillText(text.t, text.x, text.y, this._txtMxW);
        }
    },


    // state management helper //
    _applyLocalState: function (st) {
        // save current global state on DOM canvas context
        this._txtMxWBak = this._txtMxW;
        this._drwTpBak = this._drwTp;
        this._ctx.save();

        // apply local state to context
        this._setDOMContextState(st);
    },

    _setDOMContextState: function (st) {
        // this function applies the state of a drawable to DOM Canvas's global
        // state.

        if (st.dwtp) { // drawing type is NOT a part of DOM Canvas state
            this._drwTp = st.dwtp;
        }
        if (st.trns) { // transformation
            var trns = st.trns;
            this._ctx.setTransform(
                trns[0], trns[1], trns[2], trns[3], trns[4], trns[5]);
        }
        if (st.clp) { // clipping
            this._drawPath(st.clp.obj);
            this._ctx.clip();
        }

        if (st.strk) this._ctx.strokeStyle = st.strk;
        if (st.fil) this._ctx.fillStyle = st.fil;
        if (st.alfa) this._ctx.globalAlpha = st.alfa;
        if (st.lnw) this._ctx.lineWidth = st.lnw;
        if (st.lncp) this._ctx.lineCap = st.lncp;
        if (st.lnj) this._ctx.lineJoin = st.lnj;
        if (st.mtr) this._ctx.miterLimit = st.mtr;
        if (st.shx) this._ctx.shadowOffsetX = st.shx;
        if (st.shy) this._ctx.shadowOffsetY = st.shy;
        if (st.shb) this._ctx.shadowBlur = st.shb;
        if (st.shc) this._ctx.shadowColor = st.shc;
        if (st.cmp) this._ctx.globalCompositeOperation = st.cmp;
        if (st.fnt) this._ctx.font = st.fnt;
        if (st.txal) this._ctx.textAlign = st.txal;
        if (st.txbl) this._ctx.textBaseline = st.txbl;

        if (st.txmw) { // maxWidth is not a part of DOM Canvas state
            this._txtMxW = st.txmw;
        }
    },

    _unapplyLocalState: function () {
        // restore global state
        this._txtMxW = this._txtMxWBak;
        this._drwTp = this._drwTpBak;
        this._ctx.restore();
    },

    _clearShadow: function () {
        // remove shadow properties
        this._ctx.shadowOffsetX = null;
        this._ctx.shadowOffsetY = null;
        this._ctx.shadowBlur = null;
        this._ctx.shadowColor = null;
    },

    //super//
    getZclass: function () {
        var zcs = this._zclass;
        return zcs != null ? zcs : "z-canvas";
    }
});