(function () {
    "use strict";

    var prefersReducedMotion = window.matchMedia("(prefers-reduced-motion: reduce)").matches;


    function escapeHtml(s) {
        return s.replace(/[&<>]/g, function (c) {
            return { "&": "&amp;", "<": "&lt;", ">": "&gt;" }[c];
        });
    }

    function render(el, text, key, count) {
        var shown = text.slice(0, count);
        var idx = key ? text.indexOf(key) : -1;
        if (idx !== -1 && shown.length > idx) {
            var before = shown.slice(0, idx);
            var keyPart = shown.slice(idx, Math.min(shown.length, idx + key.length));
            var after = shown.slice(idx + key.length);
            el.innerHTML =
                escapeHtml(before) +
                '<span class="tw-key">' + escapeHtml(keyPart) + "</span>" +
                escapeHtml(after);
        } else {
            el.textContent = shown;
        }
    }

    function runTypewriter(el, phrases) {
        var TYPE_MS = 70, DELETE_MS = 40, HOLD_MS = 1500, GAP_MS = 350;
        var i = 0, count = 0, deleting = false;

        function tick() {
            var phrase = phrases[i];
            render(el, phrase.t, phrase.k, count);

            if (!deleting) {
                if (count < phrase.t.length) {
                    count++;
                    setTimeout(tick, TYPE_MS);
                } else {
                    deleting = true;
                    setTimeout(tick, HOLD_MS);
                }
            } else {
                if (count > 0) {
                    count--;
                    setTimeout(tick, DELETE_MS);
                } else {
                    deleting = false;
                    i = (i + 1) % phrases.length;
                    setTimeout(tick, GAP_MS);
                }
            }
        }

        tick();
    }

    function initTypewriter() {
        var el = document.getElementById("typewriter");
        if (!el) {
            return;
        }
        var phrases;
        try {
            phrases = JSON.parse(el.getAttribute("data-phrases") || "[]");
        } catch (e) {
            phrases = [];
        }
        if (!phrases.length) {
            return;
        }
        if (prefersReducedMotion) {
            render(el, phrases[0].t, phrases[0].k, phrases[0].t.length);
            return;
        }
        runTypewriter(el, phrases);
    }

    function initReveal() {
        var items = document.querySelectorAll(".reveal");
        if (!items.length) {
            return;
        }
        if (prefersReducedMotion || !("IntersectionObserver" in window)) {
            items.forEach(function (el) { el.classList.add("is-visible"); });
            return;
        }
        var observer = new IntersectionObserver(function (entries) {
            entries.forEach(function (entry) {
                if (entry.isIntersecting) {
                    entry.target.classList.add("is-visible");
                    observer.unobserve(entry.target);
                }
            });
        }, { threshold: 0.12, rootMargin: "0px 0px -40px 0px" });

        items.forEach(function (el) { observer.observe(el); });
    }

    initTypewriter();
    initReveal();
})();
