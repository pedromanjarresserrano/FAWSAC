var tns = function () {
    var t = window,
        Ii = t.requestAnimationFrame || t.webkitRequestAnimationFrame || t.mozRequestAnimationFrame || t.msRequestAnimationFrame || function (t) {
            return setTimeout(t, 16)
        }, e = window, Pi = e.cancelAnimationFrame || e.mozCancelAnimationFrame || function (t) {
            clearTimeout(t)
        };

    function Hi() {
        for (var t, e, n, i = arguments[0] || {}, a = 1, r = arguments.length; a < r; a++) if (null !== (t = arguments[a])) for (e in t) i !== (n = t[e]) && void 0 !== n && (i[e] = n);
        return i
    }

    function Ri(t) {
        return 0 <= ["true", "false"].indexOf(t) ? JSON.parse(t) : t
    }

    function Wi(t, e, n, i) {
        return i && t.setItem(e, n), n
    }

    function zi() {
        var t = document, e = t.body;
        return e || ((e = t.createElement("body")).fake = !0), e
    }

    var n = document.documentElement;

    function qi(t) {
        var e = "";
        return t.fake && (e = n.style.overflow, t.style.background = "", t.style.overflow = n.style.overflow = "hidden", n.appendChild(t)), e
    }

    function ji(t, e) {
        t.fake && (t.remove(), n.style.overflow = e, n.offsetHeight)
    }

    function Fi(t, e, n, i) {
        "insertRule" in t ? t.insertRule(e + "{" + n + "}", i) : t.addRule(e, n, i)
    }

    function Qi(t) {
        return ("insertRule" in t ? t.cssRules : t.rules).length
    }

    function Vi(t, e, n) {
        for (var i = 0, a = t.length; i < a; i++) e.call(n, t[i], i)
    }

    var i = "classList" in document.createElement("_"), Xi = i ? function (t, e) {
        return t.classList.contains(e)
    } : function (t, e) {
        return 0 <= t.className.indexOf(e)
    }, Yi = i ? function (t, e) {
        Xi(t, e) || t.classList.add(e)
    } : function (t, e) {
        Xi(t, e) || (t.className += " " + e)
    }, Ki = i ? function (t, e) {
        Xi(t, e) && t.classList.remove(e)
    } : function (t, e) {
        Xi(t, e) && (t.className = t.className.replace(e, ""))
    };

    function Ui(t, e) {
        return t.hasAttribute(e)
    }

    function Gi(t, e) {
        return t.getAttribute(e)
    }

    function r(t) {
        return void 0 !== t.item
    }

    function Ji(t, e) {
        if (t = r(t) || t instanceof Array ? t : [t], "[object Object]" === Object.prototype.toString.call(e)) for (var n = t.length; n--;) for (var i in e) t[n].setAttribute(i, e[i])
    }

    function _i(t, e) {
        t = r(t) || t instanceof Array ? t : [t];
        for (var n = (e = e instanceof Array ? e : [e]).length, i = t.length; i--;) for (var a = n; a--;) t[i].removeAttribute(e[a])
    }

    function Zi(t) {
        for (var e = [], n = 0, i = t.length; n < i; n++) e.push(t[n]);
        return e
    }

    function $i(t, e) {
        "none" !== t.style.display && (t.style.display = "none")
    }

    function ta(t, e) {
        "none" === t.style.display && (t.style.display = "")
    }

    function ea(t) {
        return "none" !== window.getComputedStyle(t).display
    }

    function na(e) {
        if ("string" == typeof e) {
            var n = [e], i = e.charAt(0).toUpperCase() + e.substr(1);
            ["Webkit", "Moz", "ms", "O"].forEach(function (t) {
                "ms" === t && "transform" !== e || n.push(t + i)
            }), e = n
        }
        for (var t = document.createElement("fakeelement"), a = (e.length, 0); a < e.length; a++) {
            var r = e[a];
            if (void 0 !== t.style[r]) return r
        }
        return !1
    }

    function ia(t, e) {
        var n = !1;
        return /^Webkit/.test(t) ? n = "webkit" + e + "End" : /^O/.test(t) ? n = "o" + e + "End" : t && (n = e.toLowerCase() + "end"), n
    }

    function aa(t, e) {
        for (var n in e) t.addEventListener(n, e[n], !1)
    }

    var a = !1;
    try {
        var o = Object.defineProperty({}, "passive", {
            get: function () {
                a = !0
            }
        });
        window.addEventListener("test", null, o)
    } catch (t) {
    }
    var u = !!a && {passive: !0};

    function ra(t, e) {
        for (var n in e) {
            var i = 0 <= ["touchstart", "touchmove"].indexOf(n) && u;
            t.removeEventListener(n, e[n], i)
        }
    }

    function oa() {
        return {
            topics: {}, on: function (t, e) {
                this.topics[t] = this.topics[t] || [], this.topics[t].push(e)
            }, off: function (t, e) {
                if (this.topics[t]) for (var n = 0; n < this.topics[t].length; n++) if (this.topics[t][n] === e) {
                    this.topics[t].splice(n, 1);
                    break
                }
            }, emit: function (e, n) {
                n.type = e, this.topics[e] && this.topics[e].forEach(function (t) {
                    t(n, e)
                })
            }
        }
    }

    Object.keys || (Object.keys = function (t) {
        var e = [];
        for (var n in t) Object.prototype.hasOwnProperty.call(t, n) && e.push(n);
        return e
    }), "remove" in Element.prototype || (Element.prototype.remove = function () {
        this.parentNode && this.parentNode.removeChild(this)
    });
    var ua = function (m) {
        m = Hi({
            container: ".slider",
            mode: "carousel",
            axis: "horizontal",
            items: 1,
            gutter: 0,
            edgePadding: 0,
            fixedWidth: !1,
            autoWidth: !1,
            viewportMax: !1,
            slideBy: 1,
            controls: !0,
            controlsPosition: "top",
            controlsText: ["prev", "next"],
            controlsContainer: !1,
            prevButton: !1,
            nextButton: !1,
            nav: !0,
            navPosition: "top",
            navContainer: !1,
            navAsThumbnails: !1,
            arrowKeys: !1,
            speed: 300,
            autoplay: !1,
            autoplayPosition: "top",
            autoplayTimeout: 5e3,
            autoplayDirection: "forward",
            autoplayText: ["start", "stop"],
            autoplayHoverPause: !1,
            autoplayButton: !1,
            autoplayButtonOutput: !0,
            autoplayResetOnVisibility: !0,
            animateIn: "tns-fadeIn",
            animateOut: "tns-fadeOut",
            animateNormal: "tns-normal",
            animateDelay: !1,
            loop: !0,
            rewind: !1,
            autoHeight: !1,
            responsive: !1,
            lazyload: !1,
            lazyloadSelector: ".tns-lazy-img",
            touch: !0,
            mouseDrag: !1,
            swipeAngle: 15,
            nested: !1,
            preventActionWhenRunning: !1,
            preventScrollOnTouch: "auto",
            freezable: !0,
            onInit: !1,
            useLocalStorage: !0
        }, m || {});
        var L = document, h = window, u = 13, s = 32, l = 33, c = 34, f = 35, d = 36, v = 37, p = 38, y = 39, g = 40,
            e = {}, n = m.useLocalStorage;
        if (n) {
            var t = navigator.userAgent, i = new Date;
            try {
                (e = h.localStorage) ? (e.setItem(i, i), n = e.getItem(i) == i, e.removeItem(i)) : n = !1, n || (e = {})
            } catch (t) {
                n = !1
            }
            n && (e.tnsApp && e.tnsApp !== t && ["tC", "tPL", "tMQ", "tTf", "t3D", "tTDu", "tTDe", "tADu", "tADe", "tTE", "tAE"].forEach(function (t) {
                e.removeItem(t)
            }), localStorage.tnsApp = t)
        }
        for (var a, r, o, x, b, w, C, A = e.tC ? Ri(e.tC) : Wi(e, "tC", function () {
            var t = document, e = zi(), n = qi(e), i = t.createElement("div"), a = !1;
            e.appendChild(i);
            try {
                for (var r, o = "(10px * 10)", u = ["calc" + o, "-moz-calc" + o, "-webkit-calc" + o], s = 0; s < 3; s++) if (r = u[s], i.style.width = r, 100 === i.offsetWidth) {
                    a = r.replace(o, "");
                    break
                }
            } catch (t) {
            }
            return e.fake ? ji(e, n) : i.remove(), a
        }(), n), M = e.tPL ? Ri(e.tPL) : Wi(e, "tPL", function () {
            var t, e = document, n = zi(), i = qi(n), a = e.createElement("div"), r = e.createElement("div"), o = "";
            a.className = "tns-t-subp2", r.className = "tns-t-ct";
            for (var u = 0; u < 70; u++) o += "<div></div>";
            return r.innerHTML = o, a.appendChild(r), n.appendChild(a), t = Math.abs(a.getBoundingClientRect().left - r.children[67].getBoundingClientRect().left) < 2, n.fake ? ji(n, i) : a.remove(), t
        }(), n), S = e.tMQ ? Ri(e.tMQ) : Wi(e, "tMQ", (r = document, o = zi(), x = qi(o), b = r.createElement("div"), w = r.createElement("style"), C = "@media all and (min-width:1px){.tns-mq-test{position:absolute}}", w.type = "text/css", b.className = "tns-mq-test", o.appendChild(w), o.appendChild(b), w.styleSheet ? w.styleSheet.cssText = C : w.appendChild(r.createTextNode(C)), a = window.getComputedStyle ? window.getComputedStyle(b).position : b.currentStyle.position, o.fake ? ji(o, x) : b.remove(), "absolute" === a), n), T = e.tTf ? Ri(e.tTf) : Wi(e, "tTf", na("transform"), n), E = e.t3D ? Ri(e.t3D) : Wi(e, "t3D", function (t) {
            if (!t) return !1;
            if (!window.getComputedStyle) return !1;
            var e, n = document, i = zi(), a = qi(i), r = n.createElement("p"),
                o = 9 < t.length ? "-" + t.slice(0, -9).toLowerCase() + "-" : "";
            return o += "transform", i.insertBefore(r, null), r.style[t] = "translate3d(1px,1px,1px)", e = window.getComputedStyle(r).getPropertyValue(o), i.fake ? ji(i, a) : r.remove(), void 0 !== e && 0 < e.length && "none" !== e
        }(T), n), B = e.tTDu ? Ri(e.tTDu) : Wi(e, "tTDu", na("transitionDuration"), n), k = e.tTDe ? Ri(e.tTDe) : Wi(e, "tTDe", na("transitionDelay"), n), N = e.tADu ? Ri(e.tADu) : Wi(e, "tADu", na("animationDuration"), n), D = e.tADe ? Ri(e.tADe) : Wi(e, "tADe", na("animationDelay"), n), O = e.tTE ? Ri(e.tTE) : Wi(e, "tTE", ia(B, "Transition"), n), I = e.tAE ? Ri(e.tAE) : Wi(e, "tAE", ia(N, "Animation"), n), P = h.console && "function" == typeof h.console.warn, H = ["container", "controlsContainer", "prevButton", "nextButton", "navContainer", "autoplayButton"], R = {}, W = H.length; W--;) {
            var z = H[W];
            if ("string" == typeof m[z]) {
                var q = m[z], j = L.querySelector(q);
                if (R[z] = q, !j || !j.nodeName) return void(P && console.warn("Can't find", m[z]));
                m[z] = j
            }
        }
        if (!(m.container.children.length < 1)) {
            var F = m.responsive, Q = m.nested, V = "carousel" === m.mode;
            if (F) {
                0 in F && (m = Hi(m, F[0]), delete F[0]);
                var X = {};
                for (var Y in F) {
                    var K = F[Y];
                    K = "number" == typeof K ? {items: K} : K, X[Y] = K
                }
                F = X, X = null
            }
            if (V && "outer" !== Q || function t(e) {
                for (var n in e) V || ("slideBy" === n && (e[n] = "page"), "edgePadding" === n && (e[n] = !1), "autoHeight" === n && (e[n] = !1)), "outer" === Q && "autoHeight" === n && (e[n] = !0), "responsive" === n && t(e[n])
            }(m), !V) {
                m.axis = "horizontal", m.slideBy = "page", m.edgePadding = !1;
                var U = m.animateIn, G = m.animateOut, J = m.animateDelay, _ = m.animateNormal
            }
            var Z, $ = "horizontal" === m.axis, tt = L.createElement("div"), et = L.createElement("div"),
                nt = m.container, it = nt.parentNode, at = nt.outerHTML, rt = nt.children, ot = rt.length, ut = xn(),
                st = !1;
            F && Hn();
            var lt, ct, ft, dt, vt, pt, mt, ht = m.autoWidth, yt = Cn("fixedWidth"), gt = Cn("edgePadding"),
                xt = Cn("gutter"), bt = bn(), wt = ht ? 1 : Math.floor(Cn("items")), Ct = Cn("slideBy"),
                At = m.viewportMax || m.fixedWidthViewportWidth, Mt = Cn("arrowKeys"), Tt = Cn("speed"), Et = m.rewind,
                Bt = !Et && m.loop, kt = Cn("autoHeight"), Nt = Cn("controls"), Lt = Cn("controlsText"), St = Cn("nav"),
                Dt = Cn("touch"), Ot = Cn("mouseDrag"), It = Cn("autoplay"), Pt = Cn("autoplayTimeout"),
                Ht = Cn("autoplayText"), Rt = Cn("autoplayHoverPause"), Wt = Cn("autoplayResetOnVisibility"),
                zt = (mt = document.createElement("style"), pt && mt.setAttribute("media", pt), document.querySelector("head").appendChild(mt), mt.sheet ? mt.sheet : mt.styleSheet),
                qt = m.lazyload, jt = m.lazyloadSelector, Ft = [], Qt = Bt ? (dt = function () {
                    {
                        if (ht || yt && !At) return ot - 1;
                        var t = yt ? "fixedWidth" : "items", e = [];
                        if ((yt || m[t] < ot) && e.push(m[t]), F) for (var n in F) {
                            var i = F[n][t];
                            i && (yt || i < ot) && e.push(i)
                        }
                        return e.length || e.push(0), Math.ceil(yt ? At / Math.min.apply(null, e) : Math.max.apply(null, e))
                    }
                }(), vt = V ? Math.ceil((5 * dt - ot) / 2) : 4 * dt - ot, vt = Math.max(dt, vt), wn("edgePadding") ? vt + 1 : vt) : 0,
                Vt = V ? ot + 2 * Qt : ot + Qt, Xt = !(!yt && !ht || Bt), Yt = yt ? oi() : null, Kt = !V || !Bt,
                Ut = $ ? "left" : "top", Gt = "", Jt = "", _t = yt ? function () {
                    return Math.ceil(-Yt / (yt + xt))
                } : ht ? function () {
                    for (var t = Vt, e = t - 1; t--;) lt[t] > -Yt && (e = t);
                    return e
                } : function () {
                    return Bt || V ? Math.max(0, Vt - Math.ceil(wt)) : Vt - 1
                }, Zt = yn(Cn("startIndex")), $t = Zt, te = (hn(), 0), ee = ht ? null : _t(),
                ne = m.preventActionWhenRunning, ie = m.swipeAngle, ae = !ie || "?", re = !1, oe = m.onInit,
                ue = new oa, se = " tns-slider tns-" + m.mode,
                le = nt.id || (ft = window.tnsId, window.tnsId = ft ? ft + 1 : 1, "tns" + window.tnsId),
                ce = Cn("disable"), fe = !1, de = m.freezable, ve = !(!de || ht) && Pn(), pe = !1, me = {
                    click: mi, keydown: function (t) {
                        switch ((t = Ai(t)).keyCode) {
                            case v:
                            case p:
                            case l:
                                He.disabled || mi(t, -1);
                                break;
                            case y:
                            case g:
                            case c:
                                Re.disabled || mi(t, 1);
                                break;
                            case d:
                                pi("first", t);
                                break;
                            case f:
                                pi("last", t)
                        }
                    }
                }, he = {
                    click: function (t) {
                        if (re) {
                            if (ne) return;
                            vi()
                        }
                        var e, n = (t = Ai(t)).target || t.srcElement;
                        for (; n !== je && !Ui(n, "data-nav");) n = n.parentNode;
                        Ui(n, "data-nav") && (pi(e = Xe = [].indexOf.call(qe, n), t), Ye === e && (Ze && bi(), Xe = -1))
                    }, keydown: function (t) {
                        var e = L.activeElement;
                        if (!Ui(e, "data-nav")) return;
                        var n = (t = Ai(t)).keyCode, i = [].indexOf.call(qe, e), a = Qe.length, r = Qe.indexOf(i);
                        m.navContainer && (a = ot, r = i);

                        function o(t) {
                            return m.navContainer ? t : Qe[t]
                        }

                        switch (n) {
                            case v:
                            case l:
                                0 < r && Ci(qe[o(r - 1)]);
                                break;
                            case p:
                            case d:
                                0 < r && Ci(qe[o(0)]);
                                break;
                            case y:
                            case c:
                                r < a - 1 && Ci(qe[o(r + 1)]);
                                break;
                            case g:
                            case f:
                                r < a - 1 && Ci(qe[o(a - 1)]);
                                break;
                            case u:
                            case s:
                                pi(Xe = i, t)
                        }
                    }
                }, ye = {
                    mouseover: function () {
                        Ze && (yi(), $e = !0)
                    }, mouseout: function () {
                        $e && (hi(), $e = !1)
                    }
                }, ge = {
                    visibilitychange: function () {
                        L.hidden ? Ze && (yi(), en = !0) : en && (hi(), en = !1)
                    }
                }, xe = {
                    keydown: function (t) {
                        switch ((t = Ai(t)).keyCode) {
                            case v:
                                mi(t, -1);
                                break;
                            case y:
                                mi(t, 1)
                        }
                    }
                }, be = {touchstart: ki, touchmove: Ni, touchend: Li, touchcancel: Li},
                we = {mousedown: ki, mousemove: Ni, mouseup: Li, mouseleave: Li}, Ce = wn("controls"), Ae = wn("nav"),
                Me = !!ht || m.navAsThumbnails, Te = wn("autoplay"), Ee = wn("touch"), Be = wn("mouseDrag"),
                ke = "tns-slide-active", Ne = "tns-complete", Le = {
                    load: function (t) {
                        Fn(Mi(t))
                    }, error: function (t) {
                        Qn(Mi(t))
                    }
                }, Se = "force" === m.preventScrollOnTouch;
            if (Ce) var De, Oe, Ie = m.controlsContainer, Pe = m.controlsContainer ? m.controlsContainer.outerHTML : "",
                He = m.prevButton, Re = m.nextButton, We = m.prevButton ? m.prevButton.outerHTML : "",
                ze = m.nextButton ? m.nextButton.outerHTML : "";
            if (Ae) var qe, je = m.navContainer, Fe = m.navContainer ? m.navContainer.outerHTML : "", Qe = [], Ve = Qe,
                Xe = -1, Ye = gn(), Ke = Ye, Ue = "tns-nav-active", Ge = "Carousel Page ", Je = " (Current Slide)";
            if (Te) var _e, Ze, $e, tn, en, nn = "forward" === m.autoplayDirection ? 1 : -1, an = m.autoplayButton,
                rn = m.autoplayButton ? m.autoplayButton.outerHTML : "",
                on = ["<span class='tns-visually-hidden'>", " animation</span>"];
            if (Ee || Be) var un, sn, ln = {}, cn = {}, fn = !1, dn = $ ? function (t, e) {
                return t.x - e.x
            } : function (t, e) {
                return t.y - e.y
            };
            ht || mn(ce || ve), T && (Ut = T, Gt = "translate", E ? (Gt += $ ? "3d(" : "3d(0px, ", Jt = $ ? ", 0px, 0px)" : ", 0px)") : (Gt += $ ? "X(" : "Y(", Jt = ")")), function () {
                F && Hn();
                !function () {
                    wn("gutter");
                    tt.className = "tns-outer", et.className = "tns-inner", tt.id = le + "-ow", et.id = le + "-iw", kt && (et.className += " tns-ah");
                    "" === nt.id && (nt.id = le);
                    se += M || ht ? " tns-subpixel" : " tns-no-subpixel", se += A ? " tns-calc" : " tns-no-calc", ht && (se += " tns-autowidth");
                    if (se += " tns-" + m.axis, nt.className += se, V) {
                        var t = L.createElement("div");
                        t.className = "tns-ovh", tt.appendChild(t), t.appendChild(et)
                    } else tt.appendChild(et);
                    it.insertBefore(tt, nt), et.appendChild(nt)
                }();
                for (var t = 0; t < ot; t++) {
                    var e = rt[t];
                    e.id || (e.id = le + "-item" + t), Yi(e, "tns-item"), !V && _ && Yi(e, _), Ji(e, {
                        "aria-hidden": "true",
                        tabindex: "-1"
                    })
                }
                if (Qt) {
                    for (var n = L.createDocumentFragment(), i = L.createDocumentFragment(), a = Qt; a--;) {
                        var r = a % ot, o = rt[r].cloneNode(!0);
                        if (_i(o, "id"), i.insertBefore(o, i.firstChild), V) {
                            var u = rt[ot - 1 - r].cloneNode(!0);
                            _i(u, "id"), n.appendChild(u)
                        }
                    }
                    nt.insertBefore(n, nt.firstChild), nt.appendChild(i), rt = nt.children
                }
                (function () {
                    for (var t = Zt, e = Zt + Math.min(ot, wt); t < e; t++) {
                        var n = rt[t];
                        Ji(n, {"aria-hidden": "false"}), _i(n, ["tabindex"]), Yi(n, ke), V || (n.style.left = 100 * (t - Zt) / wt + "%", Yi(n, U), Ki(n, _))
                    }
                    $ && (M || ht ? (Fi(zt, "#" + le + " > .tns-item", "font-size:" + h.getComputedStyle(rt[0]).fontSize + ";", Qi(zt)), Fi(zt, "#" + le, "font-size:0;", Qi(zt))) : V && Vi(rt, function (t, e) {
                        var n;
                        t.style.marginLeft = (n = e, A ? A + "(" + 100 * n + "% / " + Vt + ")" : 100 * n / Vt + "%")
                    }));
                    if (S) {
                        var i = An(m.edgePadding, m.gutter, m.fixedWidth, m.speed, m.autoHeight);
                        Fi(zt, "#" + le + "-iw", i, Qi(zt)), V && (i = $ && !ht ? "width:" + Mn(m.fixedWidth, m.gutter, m.items) + ";" : "", B && (i += kn(Tt)), Fi(zt, "#" + le, i, Qi(zt))), i = $ && !ht ? Tn(m.fixedWidth, m.gutter, m.items) : "", m.gutter && (i += En(m.gutter)), V || (B && (i += kn(Tt)), N && (i += Nn(Tt))), i && Fi(zt, "#" + le + " > .tns-item", i, Qi(zt))
                    } else {
                        et.style.cssText = An(gt, xt, yt, kt), V && $ && !ht && (nt.style.width = Mn(yt, xt, wt));
                        var i = $ && !ht ? Tn(yt, xt, wt) : "";
                        xt && (i += En(xt)), i && Fi(zt, "#" + le + " > .tns-item", i, Qi(zt))
                    }
                    if (F && S) for (var a in F) {
                        a = parseInt(a);
                        var r = F[a], i = "", o = "", u = "", s = "", l = ht ? null : Cn("items", a),
                            c = Cn("fixedWidth", a), f = Cn("speed", a), d = Cn("edgePadding", a),
                            v = Cn("autoHeight", a), p = Cn("gutter", a);
                        ("edgePadding" in r || "gutter" in r) && (o = "#" + le + "-iw{" + An(d, p, c, f, v) + "}"), V && $ && !ht && ("fixedWidth" in r || "items" in r || yt && "gutter" in r) && (u = "width:" + Mn(c, p, l) + ";"), B && "speed" in r && (u += kn(f)), u && (u = "#" + le + "{" + u + "}"), ("fixedWidth" in r || yt && "gutter" in r || !V && "items" in r) && (s += Tn(c, p, l)), "gutter" in r && (s += En(p)), !V && "speed" in r && (B && (s += kn(f)), N && (s += Nn(f))), s && (s = "#" + le + " > .tns-item{" + s + "}"), (i = o + u + s) && zt.insertRule("@media (min-width: " + a / 16 + "em) {" + i + "}", zt.cssRules.length)
                    }
                })(), Ln()
            }();
            var vn = Bt ? V ? function () {
                var t = te, e = ee;
                t += Ct, e -= Ct, gt ? (t += 1, e -= 1) : yt && bt % (yt + xt) && (e -= 1), Qt && (e < Zt ? Zt -= ot : Zt < t && (Zt += ot))
            } : function () {
                if (ee < Zt) for (; te + ot <= Zt;) Zt -= ot; else if (Zt < te) for (; Zt <= ee - ot;) Zt += ot
            } : function () {
                Zt = Math.max(te, Math.min(ee, Zt))
            }, pn = V ? function () {
                var e, n, i, a, t, r, o, u, s, l, c;
                ai(nt, ""), B || !Tt ? (li(), Tt && ea(nt) || vi()) : (e = nt, n = Ut, i = Gt, a = Jt, t = ui(), r = Tt, o = vi, u = Math.min(r, 10), s = 0 <= t.indexOf("%") ? "%" : "px", t = t.replace(s, ""), l = Number(e.style[n].replace(i, "").replace(a, "").replace(s, "")), c = (t - l) / r * u, setTimeout(function t() {
                    r -= u, l += c, e.style[n] = i + l + s + a, 0 < r ? setTimeout(t, u) : o()
                }, u)), $ || Si()
            } : function () {
                Ft = [];
                var t = {};
                t[O] = t[I] = vi, ra(rt[$t], t), aa(rt[Zt], t), ci($t, U, G, !0), ci(Zt, _, U), O && I && Tt && ea(nt) || vi()
            };
            return {
                version: "2.8.8", getInfo: Oi, events: ue, goTo: pi, play: function () {
                    It && !Ze && (xi(), tn = !1)
                }, pause: function () {
                    Ze && (bi(), tn = !0)
                }, isOn: st, updateSliderHeight: _n, refresh: Ln, destroy: function () {
                    if (zt.disabled = !0, zt.ownerNode && zt.ownerNode.remove(), ra(h, {resize: On}), Mt && ra(L, xe), Ie && ra(Ie, me), je && ra(je, he), ra(nt, ye), ra(nt, ge), an && ra(an, {click: wi}), It && clearInterval(_e), V && O) {
                        var t = {};
                        t[O] = vi, ra(nt, t)
                    }
                    Dt && ra(nt, be), Ot && ra(nt, we);
                    var r = [at, Pe, We, ze, Fe, rn];
                    for (var e in H.forEach(function (t, e) {
                        var n = "container" === t ? tt : m[t];
                        if ("object" == typeof n) {
                            var i = !!n.previousElementSibling && n.previousElementSibling, a = n.parentNode;
                            n.outerHTML = r[e], m[t] = i ? i.nextElementSibling : a.firstElementChild
                        }
                    }), H = U = G = J = _ = $ = tt = et = nt = it = at = rt = ot = Z = ut = ht = yt = gt = xt = bt = wt = Ct = At = Mt = Tt = Et = Bt = kt = zt = qt = lt = Ft = Qt = Vt = Xt = Yt = Kt = Ut = Gt = Jt = _t = Zt = $t = te = ee = ie = ae = re = oe = ue = se = le = ce = fe = de = ve = pe = me = he = ye = ge = xe = be = we = Ce = Ae = Me = Te = Ee = Be = ke = Ne = Le = ct = Nt = Lt = Ie = Pe = He = Re = De = Oe = St = je = Fe = qe = Qe = Ve = Xe = Ye = Ke = Ue = Ge = Je = It = Pt = nn = Ht = Rt = an = rn = Wt = on = _e = Ze = $e = tn = en = ln = cn = un = fn = sn = dn = Dt = Ot = null, this) "rebuild" !== e && (this[e] = null);
                    st = !1
                }, rebuild: function () {
                    return ua(Hi(m, R))
                }
            }
        }

        function mn(t) {
            t && (Nt = St = Dt = Ot = Mt = It = Rt = Wt = !1)
        }

        function hn() {
            for (var t = V ? Zt - Qt : Zt; t < 0;) t += ot;
            return t % ot + 1
        }

        function yn(t) {
            return t = t ? Math.max(0, Math.min(Bt ? ot - 1 : ot - wt, t)) : 0, V ? t + Qt : t
        }

        function gn(t) {
            for (null == t && (t = Zt), V && (t -= Qt); t < 0;) t += ot;
            return Math.floor(t % ot)
        }

        function xn() {
            return h.innerWidth || L.documentElement.clientWidth || L.body.clientWidth
        }

        function bn() {
            return function t(e) {
                var n, i = L.createElement("div");
                return e.appendChild(i), n = i.offsetWidth, i.remove(), n || t(e.parentNode)
            }(it) - (2 * gt - xt)
        }

        function wn(t) {
            if (m[t]) return !0;
            if (F) for (var e in F) if (F[e][t]) return !0;
            return !1
        }

        function Cn(t, e) {
            if (null == e && (e = ut), "items" === t && yt) return Math.floor(bt / (yt + xt)) || 1;
            var n = m[t];
            if (F) for (var i in F) e >= parseInt(i) && t in F[i] && (n = F[i][t]);
            return "slideBy" === t && "page" === n && (n = Cn("items")), V || "slideBy" !== t && "items" !== t || (n = Math.floor(n)), n
        }

        function An(t, e, n, i, a) {
            var r = "";
            if (void 0 !== t) {
                var o = t;
                e && (o -= e), r = $ ? "margin: 0 " + o + "px 0 " + t + "px;" : "margin: " + t + "px 0 " + o + "px 0;"
            } else if (e && !n) {
                var u = "-" + e + "px";
                r = "margin: 0 " + ($ ? u + " 0 0" : "0 " + u + " 0") + ";"
            }
            return a && B && i && (r += kn(i)), r
        }

        function Mn(t, e, n) {
            return t ? (t + e) * Vt + "px" : A ? A + "(" + 100 * Vt + "% / " + n + ")" : 100 * Vt / n + "%"
        }

        function Tn(t, e, n) {
            var i;
            if (t) i = t + e + "px"; else {
                V || (n = Math.floor(n));
                var a = V ? Vt : n;
                i = A ? A + "(100% / " + a + ")" : 100 / a + "%"
            }
            return i = "width:" + i, "inner" !== Q ? i + ";" : i + " !important;"
        }

        function En(t) {
            var e = "";
            !1 !== t && (e = ($ ? "padding-" : "margin-") + ($ ? "right" : "bottom") + ": " + t + "px;");
            return e
        }

        function Bn(t, e) {
            var n = t.substring(0, t.length - e).toLowerCase();
            return n && (n = "-" + n + "-"), n
        }

        function kn(t) {
            return Bn(B, 18) + "transition-duration:" + t / 1e3 + "s;"
        }

        function Nn(t) {
            return Bn(N, 17) + "animation-duration:" + t / 1e3 + "s;"
        }

        function Ln() {
            if (wn("autoHeight") || ht || !$) {
                var t = nt.querySelectorAll("span");

                function e() {
                    if (ht) {
                        var e = Bt ? Zt : ot - 1;
                        !function t() {
                            rt[e - 1].getBoundingClientRect().right.toFixed(2) === rt[e].getBoundingClientRect().left.toFixed(2) ? n() : setTimeout(function () {
                                t()
                            }, 16)
                        }()
                    } else n();

                    function n() {
                        $ && !ht || (Zn(), ht ? (Yt = oi(), de && (ve = Pn()), ee = _t(), mn(ce || ve)) : Si()), V && si(), Sn(), Dn()
                    }
                }

                Vi(t, function (t) {
                    var e = t.src;
                    e.indexOf("data:image") < 0 ? (aa(t, Le), t.src = "data:image/gif;base64,R0lGODlhAQABAIAAAAAAAP///ywAAAAAAQABAAACAUwAOw==", t.src = e, Yi(t, "loading"), Xn(t)) : qt || Fn(t)
                }), Ii(function () {
                    Un(Zi(t), function () {
                        ct = !0
                    })
                }), !ht && $ && (t = Yn(Zt, wt)), qt ? e() : Ii(function () {
                    Un(Zi(t), e)
                })
            } else V && si(), Sn(), Dn()
        }

        function Sn() {
            if (Te) {
                var t = It ? "stop" : "start";
                an ? Ji(an, {"data-action": t}) : m.autoplayButtonOutput && (tt.insertAdjacentHTML("top" !== m.autoplayPosition ? "beforeend" : "afterbegin", '<button data-action="' + t + '" type="button">' + on[0] + t + on[1] + Ht[0] + "</button>"), an = tt.querySelector("[data-action]")), an && aa(an, {click: wi}), It && (xi(), Rt && aa(nt, ye), Wt && aa(nt, ge))
            }
            if (Ae) {
                var n = V ? Qt : 0;
                if (je) Ji(je, {"aria-label": "Carousel Pagination"}), Vi(qe = je.children, function (t, e) {
                    Ji(t, {"data-nav": e, tabindex: "-1", "aria-controls": rt[n + e].id, "aria-label": Ge + (e + 1)})
                }); else {
                    for (var e = "", i = Me ? "" : 'style="display:none"', a = 0; a < ot; a++) e += '<button data-nav="' + a + '" tabindex="-1" aria-controls="' + rt[n + a].id + '" ' + i + ' type="button" aria-label="' + Ge + (a + 1) + '"></button>';
                    e = '<div class="tns-nav" aria-label="Carousel Pagination">' + e + "</div>", tt.insertAdjacentHTML("top" !== m.navPosition ? "beforeend" : "afterbegin", e), je = tt.querySelector(".tns-nav"), qe = je.children
                }
                if (Di(), B) {
                    var r = B.substring(0, B.length - 18).toLowerCase(), o = "transition: all " + Tt / 1e3 + "s";
                    r && (o = "-" + r + "-" + o), Fi(zt, "[aria-controls^=" + le + "-item]", o, Qi(zt))
                }
                Ji(qe[Ye], {tabindex: "0", "aria-label": Ge + (Ye + 1) + Je}), Yi(qe[Ye], Ue), aa(je, he)
            }
            Ce && (Ie || He && Re || (tt.insertAdjacentHTML("top" !== m.controlsPosition ? "beforeend" : "afterbegin", '<div class="tns-controls" aria-label="Carousel Navigation" tabindex="0"><button data-controls="prev" tabindex="-1" aria-controls="' + le + '" type="button">' + Lt[0] + '</button><button data-controls="next" tabindex="-1" aria-controls="' + le + '" type="button">' + Lt[1] + "</button></div>"), Ie = tt.querySelector(".tns-controls")), He && Re || (He = Ie.children[0], Re = Ie.children[1]), m.controlsContainer && Ji(Ie, {
                "aria-label": "Carousel Navigation",
                tabindex: "0"
            }), (m.controlsContainer || m.prevButton && m.nextButton) && Ji([He, Re], {
                "aria-controls": le,
                tabindex: "-1"
            }), (m.controlsContainer || m.prevButton && m.nextButton) && (Ji(He, {"data-controls": "prev"}), Ji(Re, {"data-controls": "next"})), De = ti(He), Oe = ti(Re), ii(), Ie ? aa(Ie, me) : (aa(He, me), aa(Re, me))), Rn()
        }

        function Dn() {
            if (V && O) {
                var t = {};
                t[O] = vi, aa(nt, t)
            }
            Dt && aa(nt, be), Ot && aa(nt, we), Mt && aa(L, xe), "inner" === Q ? ue.on("outerResized", function () {
                In(), ue.emit("innerLoaded", Oi())
            }) : (F || yt || ht || kt || !$) && aa(h, {resize: On}), "outer" === Q ? ue.on("innerLoaded", Kn) : kt && !ce && Kn(), jn(), ce ? qn() : ve && zn(), ue.on("indexChanged", Gn), "inner" === Q && ue.emit("innerLoaded", Oi()), "function" == typeof oe && oe(Oi()), st = !0
        }

        function On(t) {
            Ii(function () {
                In(Ai(t))
            })
        }

        function In(t) {
            if (st) {
                "outer" === Q && ue.emit("outerResized", Oi(t)), ut = xn();
                var e, n = Z, i = !1;
                F && (Hn(), (e = n !== Z) && ue.emit("newBreakpointStart", Oi(t)));
                var a, r, o, u, s = wt, l = ce, c = ve, f = Mt, d = Nt, v = St, p = Dt, m = Ot, h = It, y = Rt, g = Wt,
                    x = Zt;
                if (e) {
                    var b = yt, w = kt, C = Lt, A = Ht;
                    if (!S) var M = xt, T = gt
                }
                if (Mt = Cn("arrowKeys"), Nt = Cn("controls"), St = Cn("nav"), Dt = Cn("touch"), Ot = Cn("mouseDrag"), It = Cn("autoplay"), Rt = Cn("autoplayHoverPause"), Wt = Cn("autoplayResetOnVisibility"), e && (ce = Cn("disable"), yt = Cn("fixedWidth"), Tt = Cn("speed"), kt = Cn("autoHeight"), Lt = Cn("controlsText"), Ht = Cn("autoplayText"), Pt = Cn("autoplayTimeout"), S || (gt = Cn("edgePadding"), xt = Cn("gutter"))), mn(ce), bt = bn(), $ && !ht || ce || (Zn(), $ || (Si(), i = !0)), (yt || ht) && (Yt = oi(), ee = _t()), (e || yt) && (wt = Cn("items"), Ct = Cn("slideBy"), (r = wt !== s) && (yt || ht || (ee = _t()), vn())), e && ce !== l && (ce ? qn() : function () {
                    if (!fe) return;
                    if (zt.disabled = !1, nt.className += se, si(), Bt) for (var t = Qt; t--;) V && ta(rt[t]), ta(rt[Vt - t - 1]);
                    if (!V) for (var e = Zt, n = Zt + ot; e < n; e++) {
                        var i = rt[e], a = e < Zt + wt ? U : _;
                        i.style.left = 100 * (e - Zt) / wt + "%", Yi(i, a)
                    }
                    Wn(), fe = !1
                }()), de && (e || yt || ht) && (ve = Pn()) !== c && (ve ? (li(ui(yn(0))), zn()) : (!function () {
                    if (!pe) return;
                    gt && S && (et.style.margin = "");
                    if (Qt) for (var t = "tns-transparent", e = Qt; e--;) V && Ki(rt[e], t), Ki(rt[Vt - e - 1], t);
                    Wn(), pe = !1
                }(), i = !0)), mn(ce || ve), It || (Rt = Wt = !1), Mt !== f && (Mt ? aa(L, xe) : ra(L, xe)), Nt !== d && (Nt ? Ie ? ta(Ie) : (He && ta(He), Re && ta(Re)) : Ie ? $i(Ie) : (He && $i(He), Re && $i(Re))), St !== v && (St ? (ta(je), Di()) : $i(je)), Dt !== p && (Dt ? aa(nt, be) : ra(nt, be)), Ot !== m && (Ot ? aa(nt, we) : ra(nt, we)), It !== h && (It ? (an && ta(an), Ze || tn || xi()) : (an && $i(an), Ze && bi())), Rt !== y && (Rt ? aa(nt, ye) : ra(nt, ye)), Wt !== g && (Wt ? aa(L, ge) : ra(L, ge)), e && (yt !== b && (i = !0), kt !== w && (kt || (et.style.height = "")), Nt && Lt !== C && (He.innerHTML = Lt[0], Re.innerHTML = Lt[1]), an && Ht !== A)) {
                    var E = It ? 1 : 0, B = an.innerHTML, k = B.length - A[E].length;
                    B.substring(k) === A[E] && (an.innerHTML = B.substring(0, k) + Ht[E])
                }
                if ((a = Zt !== x) && (ue.emit("indexChanged", Oi()), i = !0), r && (a || Gn(), V || function () {
                    for (var t = Zt + Math.min(ot, wt), e = Vt; e--;) {
                        var n = rt[e];
                        Zt <= e && e < t ? (Yi(n, "tns-moving"), n.style.left = 100 * (e - Zt) / wt + "%", Yi(n, U), Ki(n, _)) : n.style.left && (n.style.left = "", Yi(n, _), Ki(n, U)), Ki(n, G)
                    }
                    setTimeout(function () {
                        Vi(rt, function (t) {
                            Ki(t, "tns-moving")
                        })
                    }, 300)
                }()), !ce && !ve) {
                    if (e && !S && (gt === T && xt === M || (et.style.cssText = An(gt, xt, yt, Tt, kt)), $)) {
                        V && (nt.style.width = Mn(yt, xt, wt));
                        var N = Tn(yt, xt, wt) + En(xt);
                        u = Qi(o = zt) - 1, "deleteRule" in o ? o.deleteRule(u) : o.removeRule(u), Fi(zt, "#" + le + " > .tns-item", N, Qi(zt))
                    }
                    kt && Kn(), i && (si(), $t = Zt)
                }
                e && ue.emit("newBreakpointEnd", Oi(t))
            }
        }

        function Pn() {
            return yt || ht ? yt ? (yt + xt) * ot <= bt + 2 * gt : (Bt ? lt[ot] : ri()) <= bt + 2 * gt : ot <= wt
        }

        function Hn() {
            for (var t in Z = 0, F) (t = parseInt(t)) <= ut && (Z = t)
        }

        function Rn() {
            !It && an && $i(an), !St && je && $i(je), Nt || (Ie ? $i(Ie) : (He && $i(He), Re && $i(Re)))
        }

        function Wn() {
            It && an && ta(an), St && je && ta(je), Nt && (Ie ? ta(Ie) : (He && ta(He), Re && ta(Re)))
        }

        function zn() {
            if (!pe) {
                if (gt && (et.style.margin = "0px"), Qt) for (var t = "tns-transparent", e = Qt; e--;) V && Yi(rt[e], t), Yi(rt[Vt - e - 1], t);
                Rn(), pe = !0
            }
        }

        function qn() {
            if (!fe) {
                if (zt.disabled = !0, nt.className = nt.className.replace(se.substring(1), ""), _i(nt, ["style"]), Bt) for (var t = Qt; t--;) V && $i(rt[t]), $i(rt[Vt - t - 1]);
                if ($ && V || _i(et, ["style"]), !V) for (var e = Zt, n = Zt + ot; e < n; e++) {
                    var i = rt[e];
                    _i(i, ["style"]), Ki(i, U), Ki(i, _)
                }
                Rn(), fe = !0
            }
        }

        function jn() {
            if (qt && !ce) {
                var t = Zt;
                if (ht) for (var e = Zt + 1, n = e, i = lt[Zt] + bt + gt - xt; lt[e] < i;) n = ++e; else n = Zt + wt;
                for (gt && (t -= 1, ht || (n += 1)), t = Math.floor(Math.max(t, 0)), n = Math.ceil(Math.min(n, Vt)); t < n; t++) Vi(rt[t].querySelectorAll(jt), function (t) {
                    if (!Xi(t, Ne)) {
                        var e = {};
                        e[O] = function (t) {
                            t.stopPropagation()
                        }, aa(t, e), aa(t, Le);
                        var n = Gi(t, "data-srcset");
                        n && (t.srcset = n), t.src = Gi(t, "data-src"), Yi(t, "loading"), Xn(t)
                    }
                })
            }
        }

        function Fn(t) {
            Yi(t, "loaded"), Vn(t)
        }

        function Qn(t) {
            Yi(t, "failed"), Vn(t)
        }

        function Vn(t) {
            Yi(t, "tns-complete"), Ki(t, "loading"), ra(t, Le)
        }

        function Xn(t) {
            t.complete && (0 !== t.naturalWidth ? Fn(t) : Qn(t))
        }

        function Yn(t, e) {
            for (var n = [], i = t, a = Math.min(t + e, Vt); i < a; i++) Vi(rt[i].querySelectorAll("span"), function (t) {
                n.push(t)
            });
            return n
        }

        function Kn() {
            var t = kt ? Yn(Zt, wt) : Yn(Qt, ot);
            Ii(function () {
                Un(t, _n)
            })
        }

        function Un(n, t) {
            return ct ? t() : (n.forEach(function (t, e) {
                Xi(t, Ne) && n.splice(e, 1)
            }), n.length ? void Ii(function () {
                Un(n, t)
            }) : t())
        }

        function Gn() {
            jn(), function () {
                for (var t = Zt + Math.min(ot, wt), e = Vt; e--;) {
                    var n = rt[e];
                    Zt <= e && e < t ? Ui(n, "tabindex") && (Ji(n, {"aria-hidden": "false"}), _i(n, ["tabindex"]), Yi(n, ke)) : (Ui(n, "tabindex") || Ji(n, {
                        "aria-hidden": "true",
                        tabindex: "-1"
                    }), Xi(n, ke) && Ki(n, ke))
                }
            }(), ii(), Di(), function () {
                if (St && (Ye = 0 <= Xe ? Xe : gn(), Xe = -1, Ye !== Ke)) {
                    var t = qe[Ke], e = qe[Ye];
                    Ji(t, {tabindex: "-1", "aria-label": Ge + (Ke + 1)}), Ji(e, {
                        tabindex: "0",
                        "aria-label": Ge + (Ye + 1) + Je
                    }), Ki(t, Ue), Yi(e, Ue), Ke = Ye
                }
            }()
        }

        function Jn(t, e) {
            for (var n = [], i = t, a = Math.min(t + e, Vt); i < a; i++) n.push(rt[i].offsetHeight);
            return Math.max.apply(null, n)
        }

        function _n() {
            var t = kt ? Jn(Zt, wt) : Jn(Qt, ot);
            et.style.height !== t && (et.style.height = t + "px")
        }

        function Zn() {
            lt = [0];
            for (var t, e = $ ? "left" : "top", n = rt[0].getBoundingClientRect()[e], i = 1; i < Vt; i++) t = rt[i].getBoundingClientRect()[e], lt.push(t - n)
        }

        function $n(t) {
            return t.nodeName.toLowerCase()
        }

        function ti(t) {
            return "button" === $n(t)
        }

        function ei(t) {
            return "true" === t.getAttribute("aria-disabled")
        }

        function ni(t, e, n) {
            t ? e.disabled = n : e.setAttribute("aria-disabled", n.toString())
        }

        function ii() {
            if (Nt && !Et && !Bt) {
                var t = De ? He.disabled : ei(He), e = Oe ? Re.disabled : ei(Re), n = Zt <= te, i = !Et && ee <= Zt;
                n && !t && ni(De, He, !0), !n && t && ni(De, He, !1), i && !e && ni(Oe, Re, !0), !i && e && ni(Oe, Re, !1)
            }
        }

        function ai(t, e) {
            B && (t.style[B] = e)
        }

        function ri() {
            return yt ? (yt + xt) * Vt : lt[Vt - 1] + rt[Vt - 1].getBoundingClientRect().width
        }

        function oi() {
            var t = bt - (ri() - xt);
            return gt && (t += gt - xt), 0 < t && (t = 0), t
        }

        function ui(t) {
            var e;
            (null == t && (t = Zt), $ && !ht) ? e = yt ? -(yt + xt) * t : 100 * -t / (T ? Vt : wt) : e = -lt[t];
            return Xt && (e = Math.max(e, Yt)), e += !$ || ht || yt ? "px" : "%"
        }

        function si(t) {
            ai(nt, "0s"), li(t)
        }

        function li(t) {
            null == t && (t = ui()), nt.style[Ut] = Gt + t + Jt
        }

        function ci(t, e, n, i) {
            var a = t + wt;
            Bt || (a = Math.min(a, Vt));
            for (var r = t; r < a; r++) {
                var o = rt[r];
                i || (o.style.left = 100 * (r - Zt) / wt + "%"), J && k && (o.style[k] = o.style[D] = J * (r - t) / 1e3 + "s"), Ki(o, e), Yi(o, n), i && Ft.push(o)
            }
        }

        function fi(t, e) {
            Kt && vn(), (Zt !== $t || e) && (ue.emit("indexChanged", Oi()), ue.emit("transitionStart", Oi()), kt && Kn(), Ze && t && 0 <= ["click", "keydown"].indexOf(t.type) && bi(), re = !0, pn())
        }

        function di(t) {
            return t.toLowerCase().replace(/-/g, "")
        }

        function vi(t) {
            if (V || re) {
                if (ue.emit("transitionEnd", Oi(t)), !V && 0 < Ft.length) for (var e = 0; e < Ft.length; e++) {
                    var n = Ft[e];
                    n.style.left = "", D && k && (n.style[D] = "", n.style[k] = ""), Ki(n, G), Yi(n, _)
                }
                if (!t || !V && t.target.parentNode === nt || t.target === nt && di(t.propertyName) === di(Ut)) {
                    if (!Kt) {
                        var i = Zt;
                        vn(), Zt !== i && (ue.emit("indexChanged", Oi()), si())
                    }
                    "inner" === Q && ue.emit("innerLoaded", Oi()), re = !1, $t = Zt
                }
            }
        }

        function pi(t, e) {
            if (!ve) if ("prev" === t) mi(e, -1); else if ("next" === t) mi(e, 1); else {
                if (re) {
                    if (ne) return;
                    vi()
                }
                var n = gn(), i = 0;
                if ("first" === t ? i = -n : "last" === t ? i = V ? ot - wt - n : ot - 1 - n : ("number" != typeof t && (t = parseInt(t)), isNaN(t) || (e || (t = Math.max(0, Math.min(ot - 1, t))), i = t - n)), !V && i && Math.abs(i) < wt) {
                    var a = 0 < i ? 1 : -1;
                    i += te <= Zt + i - ot ? ot * a : 2 * ot * a * -1
                }
                Zt += i, V && Bt && (Zt < te && (Zt += ot), ee < Zt && (Zt -= ot)), gn(Zt) !== gn($t) && fi(e)
            }
        }

        function mi(t, e) {
            if (re) {
                if (ne) return;
                vi()
            }
            var n;
            if (!e) {
                for (var i = (t = Ai(t)).target || t.srcElement; i !== Ie && [He, Re].indexOf(i) < 0;) i = i.parentNode;
                var a = [He, Re].indexOf(i);
                0 <= a && (n = !0, e = 0 === a ? -1 : 1)
            }
            if (Et) {
                if (Zt === te && -1 === e) return void pi("last", t);
                if (Zt === ee && 1 === e) return void pi("first", t)
            }
            e && (Zt += Ct * e, ht && (Zt = Math.floor(Zt)), fi(n || t && "keydown" === t.type ? t : null))
        }

        function hi() {
            _e = setInterval(function () {
                mi(null, nn)
            }, Pt), Ze = !0
        }

        function yi() {
            clearInterval(_e), Ze = !1
        }

        function gi(t, e) {
            Ji(an, {"data-action": t}), an.innerHTML = on[0] + t + on[1] + e
        }

        function xi() {
            hi(), an && gi("stop", Ht[1])
        }

        function bi() {
            yi(), an && gi("start", Ht[0])
        }

        function wi() {
            Ze ? (bi(), tn = !0) : (xi(), tn = !1)
        }

        function Ci(t) {
            t.focus()
        }

        function Ai(t) {
            return Ti(t = t || h.event) ? t.changedTouches[0] : t
        }

        function Mi(t) {
            return t.target || h.event.srcElement
        }

        function Ti(t) {
            return 0 <= t.type.indexOf("touch")
        }

        function Ei(t) {
            t.preventDefault ? t.preventDefault() : t.returnValue = !1
        }

        function Bi() {
            return a = cn.y - ln.y, r = cn.x - ln.x, t = Math.atan2(a, r) * (180 / Math.PI), e = ie, n = !1, i = Math.abs(90 - Math.abs(t)), 90 - e <= i ? n = "horizontal" : i <= e && (n = "vertical"), n === m.axis;
            var t, e, n, i, a, r
        }

        function ki(t) {
            if (re) {
                if (ne) return;
                vi()
            }
            It && Ze && yi(), fn = !0, sn && (Pi(sn), sn = null);
            var e = Ai(t);
            ue.emit(Ti(t) ? "touchStart" : "dragStart", Oi(t)), !Ti(t) && 0 <= ["span", "a"].indexOf($n(Mi(t))) && Ei(t), cn.x = ln.x = parseInt(e.clientX), cn.y = ln.y = parseInt(e.clientY), V && (un = parseFloat(nt.style[Ut].replace(Gt, "").replace(Jt, "")), ai(nt, "0s"))
        }

        function Ni(t) {
            if (fn) {
                var e = Ai(t);
                cn.x = parseInt(e.clientX), cn.y = parseInt(e.clientY), V ? sn || (sn = Ii(function () {
                    !function t(e) {
                        if (!ae) return void(fn = !1);
                        Pi(sn);
                        fn && (sn = Ii(function () {
                            t(e)
                        }));
                        "?" === ae && (ae = Bi());
                        if (ae) {
                            !Se && Ti(e) && (Se = !0);
                            try {
                                e.type && ue.emit(Ti(e) ? "touchMove" : "dragMove", Oi(e))
                            } catch (t) {
                            }
                            var n = un, i = dn(cn, ln);
                            if (!$ || yt || ht) n += i, n += "px"; else {
                                var a = T ? i * wt * 100 / (bt * Vt) : 100 * i / bt;
                                n += a, n += "%"
                            }
                            nt.style[Ut] = Gt + n + Jt
                        }
                    }(t)
                })) : ("?" === ae && (ae = Bi()), ae && (Se = !0)), Se && t.preventDefault()
            }
        }

        function Li(i) {
            if (fn) {
                sn && (Pi(sn), sn = null), V && ai(nt, ""), fn = !1;
                var t = Ai(i);
                cn.x = parseInt(t.clientX), cn.y = parseInt(t.clientY);
                var a = dn(cn, ln);
                if (Math.abs(a)) {
                    if (!Ti(i)) {
                        var n = Mi(i);
                        aa(n, {
                            click: function t(e) {
                                Ei(e), ra(n, {click: t})
                            }
                        })
                    }
                    V ? sn = Ii(function () {
                        if ($ && !ht) {
                            var t = -a * wt / bt;
                            t = 0 < a ? Math.floor(t) : Math.ceil(t), Zt += t
                        } else {
                            var e = -(un + a);
                            if (e <= 0) Zt = te; else if (e >= lt[lt.length - 1]) Zt = ee; else for (var n = 0; n < Vt && e >= lt[n];) e > lt[Zt = n] && a < 0 && (Zt += 1), n++
                        }
                        fi(i, a), ue.emit(Ti(i) ? "touchEnd" : "dragEnd", Oi(i))
                    }) : ae && mi(i, 0 < a ? -1 : 1)
                }
            }
            "auto" === m.preventScrollOnTouch && (Se = !1), ie && (ae = "?"), It && !Ze && hi()
        }

        function Si() {
            et.style.height = lt[Zt + wt] - lt[Zt] + "px"
        }

        function Di() {
            St && !Me && (!function () {
                Qe = [];
                for (var t = gn() % wt; t < ot;) V && !Bt && ot < t + wt && (t = ot - wt), Qe.push(t), t += wt;
                (Bt && Qe.length * wt < ot || !Bt && 0 < Qe[0]) && Qe.unshift(0)
            }(), Qe !== Ve && (Vi(qe, function (t, e) {
                Qe.indexOf(e) < 0 ? $i(t) : ta(t)
            }), Ve = Qe))
        }

        function Oi(t) {
            return {
                container: nt,
                slideItems: rt,
                navContainer: je,
                navItems: qe,
                controlsContainer: Ie,
                hasControls: Ce,
                prevButton: He,
                nextButton: Re,
                items: wt,
                slideBy: Ct,
                cloneCount: Qt,
                slideCount: ot,
                slideCountNew: Vt,
                index: Zt,
                indexCached: $t,
                displayIndex: hn(),
                navCurrentIndex: Ye,
                navCurrentIndexCached: Ke,
                visibleNavIndexes: Qe,
                visibleNavIndexesCached: Ve,
                sheet: zt,
                isOn: st,
                event: t || {}
            }
        }

        P && console.warn("No slides found in", m.container)
    };
    return ua
}();
//# sourceMappingURL=../sourcemaps/tiny-slider.js.map