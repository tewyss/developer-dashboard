(function () {
    "use strict";

    var buttons = document.querySelectorAll(".theme-toggle");
    if (!buttons.length) {
        return;
    }

    function currentTheme() {
        return document.documentElement.getAttribute("data-theme") || "dark";
    }

    function applyTheme(theme) {
        document.documentElement.setAttribute("data-theme", theme);
        try {
            localStorage.setItem("theme", theme);
        } catch (e) {
        }
        var toDark = theme === "dark";
        buttons.forEach(function (btn) {
            btn.setAttribute("aria-label", toDark ? "Switch to light theme" : "Switch to dark theme");
            btn.setAttribute("aria-pressed", String(toDark));
        });
    }

    applyTheme(currentTheme());

    buttons.forEach(function (btn) {
        btn.addEventListener("click", function () {
            applyTheme(currentTheme() === "dark" ? "light" : "dark");
        });
    });
})();
