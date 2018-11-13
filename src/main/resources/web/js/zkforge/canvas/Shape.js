/* Shape.js

	Purpose:
		Canvas prototype.
		
	Description:
		
	History:
		May 18, 2010 3:24:34 PM , Created by simon

Copyright (C) 2010 Potix Corporation. All Rights Reserved.

*/

/**
 * Class for drawable shapes. As rectangle and path are the only two shapes
 * defined in DOM Canvas spec, currently Shape only has these two subclasses.
 */
zkforge.canvas.Shape = zk.$extends(zkforge.canvas.Drawable, {

    $init: function () {
        this.$super('$init');
    }

});

/**
 * Class for rectangle.
 */
zkforge.canvas.Rectangle = zk.$extends(zkforge.canvas.Shape, {

    /**
     * Constructs a Rectangle at (x,y) with width w, height h.
     */
    $init: function (x, y, w, h) {
        this.$super('$init');
        this.objtp = "rect";
        this.obj = new zk.Object();
        this.obj.x = x;
        this.obj.y = y;
        this.obj.w = w;
        this.obj.h = h;
    },

    /**
     * Sets the position. The reference point is the upper-left corner of the
     * rectangle.
     */
    setPos: function (x, y) {
        this.obj.x = x;
        this.obj.y = y;
        return this;
    },

    /**
     * Sets the size. The unit is pixel.
     */
    setSize: function (w, h) {
        this.obj.w = w;
        this.obj.h = h;
        return this;
    },

    /**
     * Scales the rectangle. Reference point is (0,0) on Canvas. if you need to
     * scale based on rectangle position, combine with the translate function.
     */
    scale: function (a, b) {
        this.obj.x *= a;
        this.obj.y *= b;
        this.obj.w *= a;
        this.obj.h *= b;
    },

    /**
     * Translate the rectangle.
     */
    translate: function (dx, dy) {
        this.obj.x += dx;
        this.obj.y += dy;
    }

});

/**
 * Class for path.
 * See HTML 5 Canvas document for details.
 */
zkforge.canvas.Path = zk.$extends(zkforge.canvas.Shape, {

    /**
     * Constructs an empty Path.
     */
    $init: function () {
        this.$super('$init');
        this.objtp = "path";
        this.obj = new zk.Object();
        this.obj.sg = [];
    },

    /**
     * Removes all subpaths.
     */
    beginPath: function () {
        this.obj.sg = [];
        return this;
    },

    /**
     * Mark the current subpath closed.
     */
    closePath: function () {
        var s = new Object();
        s.tp = "cl";
        s.dt = [];
        this.obj.sg.push(s);
        return this;
    },

    /**
     * Closes the current subpath and moves to a new point.
     */
    moveTo: function (x, y) {
        var s = Object();
        s.tp = "mv";
        s.dt = [x, y];
        this.obj.sg.push(s);
        return this;
    },

    /**
     * Adds a line.
     */
    lineTo: function (x, y) {
        var s = Object();
        s.tp = "ln";
        s.dt = [x, y];
        this.obj.sg.push(s);
        return this;
    },

    /**
     * Adds a quadratic curve. (cpx, cpy) is the control point.
     */
    quadTo: function (cpx, cpy, x, y) {
        var s = Object();
        s.tp = "qd";
        s.dt = [cpx, cpy, x, y];
        this.obj.sg.push(s);
        return this;
    },

    /**
     * Adds a Bezier curve. (cpx1, cpy1), (cpx2, cpy2) are control points.
     * See HTML 5 Canvas spec for details.
     */
    bezierTo: function (cpx1, cpy1, cpx2, cpy2, x, y) {
        var s = Object();
        s.tp = "bz";
        s.dt = [cpx1, cpy1, cpx2, cpy2, x, y];
        this.obj.sg.push(s);
        return this;
    },

    // arcTo

    /**
     * Transforms the path. Parameter should be a double array of length 6,
     * whose entries represent m11, m12, m21, m22, dx, dy respectively, and
     * all the points in the path (including control points of curves) will be
     * transformed by:
     *
     * [x] -> [m11, m21][x] + [dx]
     * [y]    [m12, m22][y]   [dy]
     *
     */
    transform: function (m) {
        for (var i = sg.length; i--;) {
            var dt = sg[i].dt;
            for (var j = (dt.length) / 2; j--;) {
                var x2 = m[0] * dt[2 * j] + m[2] * dt[2 * j + 1] + m[4];
                dt[2 * j + 1] = m[1] * dt[2 * j] + m[3] * dt[2 * j + 1] + m[5];
                dt[2 * j] = x2;
            }
        }
    },

    /**
     * Scales the path. Reference point is (0,0).
     */
    scale: function (a, b) {
        this.transform([a, 0, 0, b, 0, 0]);
    },

    /**
     * Translates the path.
     */
    translate: function (dx, dy) {
        this.transform([1, 0, 0, 1, dx, dy]);
    },

    /**
     * Returns a deep copy of this path.
     */
    clone: function () {
        var p2 = new zkforge.canvas.Path();
        p2._copyObj(this);
        p2._copyState(this);
    },

    // copy object data from path
    _copyObj: function (path) {
        this.obj.sg = [];
        var sg1 = path.obj.sg;
        for (var i = sg1.length; i--;) {
            this.obj.sg[i] = new Object();
            this.obj.sg[i].tp = sg1[i].tp;
            this.obj.sg[i].dt = [];
            for (var j = sg1[i].dt.length; j--;) {
                this.obj.sg[i].dt[j] = sg1[i].dt[j];
            }
        }
    }

});
