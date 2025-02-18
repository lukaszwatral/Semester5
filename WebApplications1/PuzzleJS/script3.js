document.addEventListener("DOMContentLoaded", () => {
    askForLocation();
    askForNotifications();
});

let userLatitude = null;
let userLongitude = null;

let map = L.map('map');
L.tileLayer.provider('Esri.WorldImagery').addTo(map);

document.getElementById("getLocation").addEventListener("click", function(event) {
    if(userLatitude !== null && userLongitude !== null) {
        map.setView([userLatitude, userLongitude], 18);
        let marker = L.marker([userLatitude, userLongitude]).addTo(map);
        marker.bindPopup("<strong>You are here!</strong>");
    } else {

        if (!navigator.geolocation) {
            console.log("No geolocation.");
        }
        navigator.geolocation.getCurrentPosition(position => {
            userLatitude = position.coords.latitude;
            userLongitude = position.coords.longitude;

            map.setView([userLatitude, userLongitude], 18);
            let marker = L.marker([userLatitude, userLongitude]).addTo(map);
            marker.bindPopup("<strong>You are here!</strong>");
        }, positionError => {
            console.error(positionError);
        });
    }
});

let pieces = [];

document.getElementById("saveButton").addEventListener("click", function() {
    leafletImage(map, function (err, canvas) {
        let rasterMap = document.getElementById("rasterMap");
        let rasterContext = rasterMap.getContext("2d", {willReadFrequently: true});

        rasterMap.width = 600;
        rasterMap.height = 300;
        rasterContext.drawImage(canvas, 0, 0, 600, 300);

        shufflePuzzles(rasterContext);
    });
});

function shufflePuzzles(rasterContext) {
    let shuffledPuzzles = document.getElementById("shuffledPuzzles");
    shuffledPuzzles.innerHTML = "";
    let pieceWidth = 600 / 4;
    let pieceHeight = 300 / 4;

    for(let i = 0; i < 4; i++) {
        for(let j = 0; j < 4; j++) {
            let piece = rasterContext.getImageData(j * pieceWidth, i * pieceHeight, pieceWidth, pieceHeight);
            pieces.push({piece, x:j, y:i});
        }
    }

    pieces.sort(() => Math.random() - 0.5);

    for (let i = 0; i < 4; i++) {
        for (let j = 0; j < 4; j++) {
            let pieceData = pieces[i * 4 + j];
            let pieceCanvas = document.createElement("canvas");
            pieceCanvas.width = pieceWidth;
            pieceCanvas.height = pieceHeight;
            pieceCanvas.classList.add("draggable");
            pieceCanvas.draggable = true;
            pieceCanvas.dataset.x = pieceData.x;
            pieceCanvas.dataset.y = pieceData.y;
            let pieceContext = pieceCanvas.getContext("2d");
            pieceContext.putImageData(pieceData.piece, 0, 0);
            shuffledPuzzles.appendChild(pieceCanvas);

            pieceCanvas.addEventListener("dragstart", function(event) {
                event.dataTransfer.setData("text/plain", JSON.stringify({ x: pieceData.x, y: pieceData.y }));
            });
        }
    }
}

document.querySelectorAll(".slot").forEach(slot => {
    slot.addEventListener("dragover", function(event) {
        event.preventDefault();
    });

    slot.addEventListener("drop", function(event) {
        event.preventDefault();
        let data = JSON.parse(event.dataTransfer.getData("text/plain"));
        let targetX = parseInt(this.dataset.x);
        let targetY = parseInt(this.dataset.y);
        let pieceCanvas = document.querySelector(`canvas[data-x='${data.x}'][data-y='${data.y}']`);

        if (data.x === targetX && data.y === targetY) {
            pieceCanvas.draggable = false;
            this.appendChild(pieceCanvas);
            this.classList.add("correct");
            pieceCanvas.style.cursor = "default";
        } else {
            this.appendChild(pieceCanvas);
        }

        pieceCanvas.style.position = "relative";
        pieceCanvas.style.left = "0";
        pieceCanvas.style.top = "0";

        setTimeout(() => {
            let allCorrect = true;
            document.querySelectorAll(".slot").forEach(slot => {
                if (!slot.classList.contains("correct")) {
                    allCorrect = false;
                }
            });

            if (allCorrect) {
                alert("Puzzle completed!");
                    if (Notification.permission === "granted") {
                        new Notification("Puzzle completed!");
                    } else if (Notification.permission !== "denied") {
                        Notification.requestPermission().then(permission => {
                            if (permission === "granted") {
                                new Notification("Puzzle completed!");
                            }
                        });
                }
            }
        }, 0);
    });
});

function askForNotifications() {
    if ("Notification" in window) {
        Notification.requestPermission().then((permission) => {
            if (permission === "granted") {
            } else if (permission === "denied") {
                console.warn("Access denied for notifications.");
            }
        });
    } else {
        console.error("Browser does not support notifications.");
    }
}

function askForLocation() {
    if ("geolocation" in navigator) {
        navigator.geolocation.getCurrentPosition(
            (position) => {
                userLatitude = position.coords.latitude;
                userLongitude = position.coords.longitude;

            },
            (error) => {
                console.error("Couldn't get location.", error.message);
            }
        );
    } else {
        console.error("Browser does not support geolocation.");
    }
}
