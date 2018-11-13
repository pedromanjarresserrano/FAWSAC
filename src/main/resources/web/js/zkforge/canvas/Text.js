/* Text.js

	Purpose:
		Canvas prototype.
		
	Description:
		
	History:
		May 19, 2010 6:12:45 PM , Created by simon

Copyright (C) 2010 Potix Corporation. All Rights Reserved.

*/

/**
 * The class for drawable text object.
 */
zkforge.canvas.Text = zk.$extends(zkforge.canvas.Drawable, {

    /**
     * Constructs a Text object at (x,y) with string txt.
     */
    $init: function (txt, x, y) {
        this.$super('$init');
        this.objtp = "text";
        this.obj = new zk.Object();
        this.obj.t = txt;
        this.obj.x = x;
        this.obj.y = y;
    },

    /**
     * Sets the text position.
     */
    setPos: function (x, y) {
        this.obj.x = x;
        this.obj.y = y;
        return this;
    },

    /**
     * Sets the text to be drawn.
     */
    setText: function (txt) {
        this.obj.t = txt;
        return this;
    }

});