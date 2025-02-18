/******/ (() => { // webpackBootstrap
/******/ 	"use strict";
/*!*******************!*\
  !*** ./script.ts ***!
  \*******************/


var msg = "Hello!";
alert(msg);
var styles = {
  "Styl 1": "./styles/style1.css",
  "Styl 2": "./styles/style2.css",
  "Styl 3": "./styles/style3.css"
};
dynamicSetStyle("Styl 1");
loadStyles();
function loadStyles() {
  var _a;
  var nav = document.querySelector("nav");
  var _loop = function _loop(style) {
    var li = document.createElement("li");
    var a = document.createElement("a");
    a.onclick = function () {
      return switchStyle(style);
    };
    a.textContent = style;
    a.style.cursor = "pointer";
    li.appendChild(a);
    (_a = nav.querySelector("ul")) === null || _a === void 0 ? void 0 : _a.appendChild(li);
  };
  for (var style in styles) {
    _loop(style);
  }
}
function switchStyle(style) {
  var link = document.querySelector("link[rel=\"stylesheet\"]");
  var newLink = document.createElement("link");
  newLink.rel = "stylesheet";
  newLink.href = styles[style];
  document.head.replaceChild(newLink, link);
}
function dynamicSetStyle(style) {
  var link = document.createElement("link");
  link.rel = "stylesheet";
  link.href = styles[style];
  document.head.appendChild(link);
}
/******/ })()
;