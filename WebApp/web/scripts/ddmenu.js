var ddmenuOptions =
        {
            menuId: "ddmenu",
            linkIdToMenuHtml: null,
            effect: "fade", //"slide", "fade", or "none"
            open: "onmouseover", // or "onclick"
            speed: 200,
            delay: 50,
            license: "mylicense"
        };

var ddmenu = new McDdMenu(ddmenuOptions);

/* Menucool Drop Down Menu v2014.2.4 Copyright www.menucool.com */
function McDdMenu(m) {
    if (typeof String.prototype.trim !== "function")
        String.prototype.trim = function() {
            return this.replace(/^\s+|\s+$/g, "")
        };
    var db = function(a, b) {
        return a.getElementsByTagName(b)
    }, k = window, u = navigator, S = function(b, d) {
        if (k.getComputedStyle)
            var c = k.getComputedStyle(b, null);
        else if (b.currentStyle)
            c = b.currentStyle;
        else
            c = b[a];
        return c[d]
    }, N = function(a) {
        if (a && a.stopPropagation)
            a.stopPropagation();
        else
            k.event.cancelBubble = true
    }, X = function(a) {
        var b = a ? a : k.event;
        if (b.preventDefault)
            b.preventDefault();
        else
            a.returnValue = false
    }, w, a, d, q, o, I, O, f = document, j = "className", c = "length", E = "addEventListener", g = "target", qb = ["$1$2$3", "$1$2$3", "$1$24", "$1$23", "$1$22"], y = "offsetWidth", v = "zIndex", z = "onclick", rb = "", i = [], b, U, B, t, n, h = function() {
        return n && n[y]
    };
    if (typeof McDDs == "undefined")
        McDDs = [];
    var jb = function(b) {
        var a = 1, d = McDDs[c];
        while (d--)
            if (McDDs[d].a == b.a)
                a = 0;
        a && McDDs.push(b)
    }, e = function(a, c, b) {
        if (a[E])
            a[E](c, b, false);
        else
            a.attachEvent && a.attachEvent("on" + c, b)
    }, C = "ontouchstart"in k || k.DocumentTouch && document instanceof DocumentTouch, hb = u.msMaxTouchPoints || u.maxTouchPoints, H = (u.msPointerEnabled || u.pointerEnabled) && hb;
    if (H)
        if (u.msPointerEnabled)
            var T = "MSPointerOver", V = "MSPointerOut";
        else {
            T = "pointerOver";
            V = "pointerOut"
        }
    var R = function(a) {
        if (a) {
            var b = a[g];
            if (!b) {
                b = a.srcElement;
                a[g] = b
            }
            a[g].by = 4
        }
    }, J = function(b) {
        var a = i[c];
        while (a--)
            i[a].a != null && b != i[a] && i[a].l()
    }, pb = [/(?:.*\.)?(\w)([\w\-])[^.]*(\w)\.[^.]+$/, /.*([\w\-])\.(\w)(\w)\.[^.]+$/, /^(?:.*\.)?(\w)(\w)\.[^.]+$/, /.*([\w\-])([\w\-])\.com\.[^.]+$/, /^(\w)[^.]*(\w)$/], kb = function() {
        var c = 50, b = u.userAgent, a;
        if ((a = b.indexOf("MSIE ")) != -1)
            c = parseInt(b.substring(a + 5, b.indexOf(".", a)));
        return c
    }, Q = function() {
        b = {a: m.license, b: m.menuId, c: m.effect == "none" ? 0 : m.effect == "slide" ? 1 : 2, d: m.delay, e: m.linkIdToMenuHtml, f: m.speed, g: m.open.toLowerCase()};
        if (!b.d)
            b.c = 0;
        b.c2 = b.c
    }, A = kb(), D = function(e) {
        var a = e.childNodes, d = [];
        if (a)
            for (var b = 0, f = a[c]; b < f; b++)
                a[b].nodeType == 1 && d.push(a[b]);
        return d
    }, eb = function(a) {
        return a.replace(/(?:.*\.)?(\w)([\w\-])?[^.]*(\w)\.[^.]*$/, "$1$3$2")
    }, nb = function(h, a) {
        var b = function(a) {
            for (var d = unescape(a.substr(0, a[c] - 1)), f = a.substr(a[c] - 1, 1), e = "", b = 0; b < d[c]; b++)
                e += String.fromCharCode(d.charCodeAt(b) - f);
            return unescape(e)
        }, e = f.domain, d = Math.random(), g = b(eb(e));
        d = 1;
        U = "%66%75%6E%63%74%69%6F%6E%20%71%51%28%73%2C%6B%29%7B%76%3";
        if (P(a + d)[c] % g[c] > 8)
            try {
                a = (new Function("$", "_", "e", P(U, d[c]))).apply(this, [g, a, d])
            } catch (i) {
            }
        else
            d < .14 && (!e || e == b("qthfqmtxy5")) && a != "6c0l6" && h[w].appendChild(f[b("hwjfyjYj}yStij5")](b("iirjsz%ywnfq%{jwxnts5")))
    }, l = k.clearTimeout, s = k.setTimeout, F = "createElement", r = function(a, b) {
        return b ? f[a](b) : f[a]
    }, P = function(e, b) {
        for (var d = [], a = 0; a < e[c]; a++)
            d[d[c]] = String.fromCharCode(e.charCodeAt(a) - (b && b > 7 ? b : 3));
        return d.join("")
    }, mb = function(a, d) {
        var b = a[c];
        while (b--)
            if (a[b] === d)
                return true;
        return false
    }, Y = function(a, c) {
        var b = false;
        if (a[j])
            b = mb(a[j].split(" "), c);
        return b
    }, x = function(a, b, c) {
        if (!Y(a, b))
            if (a[j] == "")
                a[j] = b;
            else if (c)
                a[j] = b + " " + a[j];
            else
                a[j] += " " + b
    }, L = function(d, f) {
        if (d[j]) {
            for (var e = "", b = d[j].split(" "), a = 0, g = b[c]; a < g; a++)
                if (b[a] !== f)
                    e += b[a] + " ";
            d[j] = e.trim()
        }
    }, fb = function(d) {
        var c = d.children[0][a];
        c.WebkitTransition = c.msTransition = c.MozTransition = c.OTransition = "background-color " + (b.f + 100) + "ms ease-out"
    }, p = function(b, c) {
        b.oP = c;
        if (A > 8)
            b[a].opacity = c;
        else
            b[a].filter = "alpha(opacity=" + c * 100 + ")"
    }, ab = function(b, a) {
        return 1 - Math.pow(1 - b, a)
    }, G = function(c, f, g) {
        for (var a = [], e = Math.ceil(g / 16), d = 1; d <= e; d++)
            if (b.c == 2)
                a.push(c + ab(d / e, 2) * (f - c));
            else
                a.push(Math.round(c + ab(d / e, 2.5) * (f - c)));
        a.Q = 0;
        return a
    }, K = function(a) {
        return a.pointerType == a.MSPOINTER_TYPE_MOUSE || a.pointerType == "mouse"
    }, Z = function(b) {
        var a = this;
        a.a = null;
        a.b = b;
        a.a = null;
        a.d = null;
        a.e = null;
        a.f();
        a.g();
        a.s = 0
    }, cb = function(a) {
        t = S(a, "z-index") || S(a, "zIndex");
        if (t == "auto" || t == "")
            t = 1e10;
        this.q(a);
        this.r(a)
    };
    Z.prototype = {j: function() {
            var a = this;
            l(a.d);
            a.d = s(function() {
                a.l()
            }, 27)
        }, k: function() {
            if (this.s == 1)
                return;
            var e = this, c = e.a;
            c[a][d] = "block";
            c[a][q] = "9999px";
            c.mw = c.sI.clientWidth;
            c.mh = c.sI.clientHeight;
            c[a][q] = h() ? "auto" : c.mw + "px";
            c[a][o] = b.c == 1 ? "0px" : c.mh + "px";
            c[a].top = h() ? "0" : c.pA[I] - c.clientTop - 1 + "px";
            x(e.b, "over");
            e.b[a][v] = 2;
            if (A < 10)
                e.b[a][v] += t;
            if (b.c) {
                l(e.e);
                e.s = 1;
                if (b.c == 1)
                    e.m();
                else
                    e.n()
            }
        }, f: function() {
            var b = this;
            if (A < 8)
                b.b[a][d] = "inline";
            var h = D(b.b);
            if (h[c])
                if (h[0][O] != "A") {
                    var f = r(F, "a");
                    f.setAttribute("href", "#");
                    e(f, "click", function(a) {
                        X(a)
                    });
                    b.b.insertBefore(f, b.b.firstChild);
                    var g;
                    while (g = f.nextSibling) {
                        if (g.nodeType == 1 && g[O] == "DIV")
                            break;
                        f.appendChild(g)
                    }
                    f.innerHTML = f.innerHTML.trim();
                    b.a = f
                } else {
                    b.a = h[0];
                    b.a.getAttribute("href") == "#" && e(b.a, "click", function(a) {
                        X(a)
                    })
                }
        }, m: function() {
            var d = this, e = G(0, this.a.mh, b.f);
            d.e = setInterval(function() {
                if (++e.Q < e[c])
                    d.a[a][o] = e[e.Q] + "px";
                else {
                    d.a[a][o] = d.a.mh + "px";
                    l(d.e)
                }
            }, 16)
        }, n: function() {
            var a = this, d = G(a.a.oP, 1, b.f * (1 - a.a.oP));
            a.e = setInterval(function() {
                if (++d.Q < d[c])
                    p(a.a, d[d.Q]);
                else {
                    p(a.a, 1);
                    l(a.e)
                }
            }, 16)
        }, g: function() {
            var f = this, m = D(f.b), k = m[0];
            k.ta = 1;
            if (m[c] == 2) {
                k[a][v] = 3;
                if (A < 10)
                    k[a][v] += t;
                x(k, "arrow", 1);
                var j = m[1];
                x(j, "drop", 1);
                j[a][d] = "block";
                j[a][q] = "9999px";
                j[a].overflow = "hidden";
                var u = j.clientHeight, i = r(F, "div");
                i[a].padding = i[a].margin = "0";
                i[a][d] = "block";
                i[a].position = "relative";
                i[a].styleFloat = i[a].cssFloat = "left";
                i = j.insertBefore(i, j.firstChild);
                var s;
                while (s = i.nextSibling)
                    i.appendChild(s);
                i[a].top = "auto";
                i[a].bottom = "0";
                var o = i.offsetTop, l = u - o - i.clientHeight;
                if (l < 0)
                    l = 0;
                i[a].position = "absolute";
                i[a].paddingTop = o + "px";
                i[a].paddingBottom = l + "px";
                j[a].paddingTop = j[a].paddingBottom = "0px";
                b.c == 2 && p(j, 0);
                j[a][d] = "none";
                f.a = j;
                f.a.pA = k;
                f.a.sI = i;
                if (b.g == z)
                    e(f.b, "click", function(a) {
                        R(a);
                        f.ia(a)
                    });
                else if (H) {
                    e(f.b, T, function(a) {
                        if (!h()) {
                            a[g].by = 3;
                            if (K(a))
                                f.i(a);
                            else
                                f.ia(a)
                        }
                    });
                    e(f.b, V, function(a) {
                        !h() && K(a) && f.j()
                    });
                    e(f.b, "click", function(a) {
                        if (h()) {
                            a[g].by = 3;
                            f.ia(a)
                        }
                        N(a)
                    })
                } else {
                    if (C)
                        f.b.ontouchstart = function(a) {
                            a[g].by = 1;
                            a[g].ta && f.ia(a)
                        };
                    else if (n)
                        f.b[z] = function(a) {
                            if (h()) {
                                a[g].by = 1;
                                a[g].ta && f.ia(a)
                            }
                        };
                    e(f.b, "mouseover", function(a) {
                        if (!h()) {
                            R(a);
                            f.i(a)
                        }
                    });
                    e(f.b, "mouseout", function() {
                        !h() && f.j()
                    })
                }
                b.c && fb(f.b)
            } else {
                f.a = null;
                e(f.b, "mouseover", function() {
                    x(this, "over")
                });
                e(f.b, "mouseout", function() {
                    L(this, "over")
                })
            }
        }, ia: function(d) {
            N(d);
            J(this);
            this.i(d);
            if (McDDs[c] > 1)
                for (var a = 0; a < McDDs[c]; a++)
                    McDDs[a].a != b.b && McDDs[a].c()
        }, o: function() {
            if (this.a[a][d] != "none") {
                var e = this, f = G(e.a[I], 0, b.f * .5 * e.a[I] / e.a.mh);
                e.e = setInterval(function() {
                    if (++f.Q < f[c])
                        e.a[a][o] = f[f.Q] + "px";
                    else if (e.a[a][o] == "2px") {
                        e.a[a][d] = "none";
                        e.a[a][o] = "0px";
                        e.b[a][v] = 0;
                        l(e.e)
                    } else
                        e.a[a][q] = e.a[a][o] = "2px"
                }, 16)
            }
        }, p: function() {
            if (this.a[a][d] != "none") {
                var e = this, f = G(e.a.oP, 0, b.f * .7 * e.a.oP);
                e.e = setInterval(function() {
                    if (++f.Q < f[c])
                        p(e.a, f[f.Q]);
                    else if (e.a[a][q] == "2px") {
                        p(e.a, 0);
                        e.a[a][d] = "none";
                        e.b[a][v] = 0;
                        l(e.e)
                    } else
                        e.a[a][q] = "2px"
                }, 16)
            }
        }, i: function(c) {
            var a = this;
            l(a.d);
            if (n && b.c2 == 2 && a.s < 1) {
                b.c = h() ? 1 : 2;
                p(a.a, b.c == 2 ? 0 : 1)
            }
            if (b.g == z || h() || H && !K(c) || C && c[g].by == 1)
                a.d = s(function() {
                    if (a.s < 1)
                        a.k();
                    else
                        a.l()
                }, a.s < 1 ? 0 : 50);
            else
                a.d = s(function() {
                    a.k()
                }, b.d)
        }, l: function() {
            var c = this;
            if (c.s == -1)
                return;
            L(c.b, "over");
            if (b.c == 0)
                c.a[a][d] = "none";
            else {
                l(c.e);
                c.s = -1;
                if (b.c == 1)
                    c.o();
                else
                    c.p()
            }
        }};
    cb.prototype = {q: function(a) {
            nb(a, b.a)
        }, r: function(d) {
            (b.g == z || H || C) && e(f, C ? "touchstart" : "click", function(a) {
                !(a[g] || a.srcElement).by && J(0)
            });
            n && e(window, "resize", J);
            q = "width";
            o = "height";
            (new Function("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "z", "y", "x", function(d) {
                for (var b = [], a = 0, e = d[c]; a < e; a++)
                    b[b[c]] = String.fromCharCode(d.charCodeAt(a) - 4);
                return b.join("")
            }("zev$NAjyrgxmsr,|0}-zev$eAjyrgxmsr,f-zev$gAf2glevGshiEx,4-2xsWxvmrk,-?vixyvr$g2wyfwxv,g2pirkxl15-\u0081?vixyvr$|/e,}_6a-/}_4a/e,}_5a-?\u00810QAg,+=j7s=+-0qAe2e\u0080\u0080Q0rAtevwiMrx,q2glevEx,4--\u0080\u0080=0R?mj,RAk,g,+kvthpu+---zev$sA,R2vitpegi,h_r16a0l_r16a--2wtpmx,++-0tAQexl_g,+yhukvt+-a,-?mj,q%AN,r/+g+0s-**R2mrhi|Sj,+epl+-AA15-mj,tB2;-zev$uAk,g,+jylh{l[l{Uvkl+-0,tB2=:Cg,+kktlu|A'{yphs'}lyzpvu+->g,+kktlu|'{yphs'}lyzpvu+---0vAm_oa0wAv_oa?mj,tB2=9**w2rshiReqi%A+FSH]+-w_oa2mrwivxFijsvi,u0w-?ipwi$w2mrwivxFijsvi,u0v-\u0081\u0081\u0081jsv,zev$xA4?x@~2pirkxl?x//-mj,~_xa2rshiReqiAA+PM+-|2tywl,ri{$},~_xa--?"))).apply(this, [b, 0, P, pb, eb, 0, r, qb, d, 0, w, D(d), Z, i]);
            i[0].b[w][a].overflow = "hidden";
            setTimeout(function() {
                i[0].b[w][a].overflow = "visible"
            }, 10)
        }};
    var gb = function(c) {
        var b;
        if (k.XMLHttpRequest)
            b = new XMLHttpRequest;
        else
            b = new ActiveXObject("Microsoft.XMLHTTP");
        b.onreadystatechange = function() {
            if (b.readyState == 4 && b.status == 200) {
                var e = b.responseText, g = /^[\s\S]*<body[^>]*>([\s\S]+)<\/body>[\s\S]*$/i;
                if (g.test(e))
                    e = e.replace(g, "$1");
                e = e.trim();
                var f = r(F, "div");
                f[a].padding = f[a].margin = "0";
                c[w].insertBefore(f, c);
                f.innerHTML = e;
                c[a][d] = "none";
                M()
            }
        };
        b.open("GET", c.href, true);
        b.send()
    }, W = function() {
        if (B) {
            l(B);
            B = null
        }
        w = "parentNode", a = "style", d = "display", O = "nodeName", I = "offsetHeight";
        if (b.e) {
            var c = r("getElementById", b.e);
            if (c)
                gb(c);
            else
                alert('Cannot find the anchor (id="' + b.e + '")')
        } else
            M()
    }, M = function() {
        var j = 0, e = r("getElementById", b.b);
        if (e && e[y]) {
            for (var h = D(e), i = 0, f = 0; f < h[c]; f++)
                if (Y(h[f], "menu-icon"))
                    n = h[f];
            e = db(e, "ul");
            if (e[c]) {
                j = 1;
                e = e[0];
                if (n) {
                    n[z] = function(b) {
                        e[a][d] = e[y] == 0 ? "block" : "";
                        e[y] == 0 ? L(this, "menu-icon-active") : x(this, "menu-icon-active");
                        N(b)
                    };
                    if (!e[y]) {
                        e[a][d] = "block";
                        i = 1
                    }
                }
            }
        }
        if (j) {
            var g = new cb(e);
            g.a = b.b;
            g.c = J;
            jb(g);
            if (i == 1)
                e[a][d] = ""
        } else
            B = s(M, 500)
    }, lb = function(d) {
        var b = false;
        function a() {
            if (b)
                return;
            b = true;
            s(d, 4)
        }
        if (f[E])
            f[E]("DOMContentLoaded", a, false);
        else if (f.attachEvent) {
            try {
                var g = k.frameElement != null
            } catch (h) {
            }
            if (f.documentElement.doScroll && !g) {
                function c() {
                    if (b)
                        return;
                    try {
                        f.documentElement.doScroll("left");
                        a()
                    } catch (d) {
                        s(c, 10)
                    }
                }
                c()
            }
            f.attachEvent("onreadystatechange", function() {
                f.readyState === "complete" && a()
            })
        }
        e(window, "load", a)
    };
    if (A < 9) {
        var ob = r(F, "nav"), bb = db(f, "head");
        if (!bb[c])
            return;
        bb[0].appendChild(ob)
    }
    Q();
    lb(W);
    var ib = function() {
        var d = arguments[0];
        if (d) {
            for (var f in d)
                m[f] = d[f];
            Q()
        }
        for (var e, a = 0; a < i[c]; a++) {
            e = i[a].a;
            if (e) {
                i[a].s = 0;
                p(e, b.c == 2 ? 0 : 1)
            }
        }
    };
    return{changeOptions: ib, init: W}
}