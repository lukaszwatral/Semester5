let map = L.map('map').setView([53.430127, 14.564802], 18);
L.tileLayer.provider('Esri.WorldImagery').addTo(map);

document.getElementById("getLocation").addEventListener("click", function(event) {
    if (!navigator.geolocation) {
        console.log("No geolocation.");
    }

    navigator.geolocation.getCurrentPosition(position => {
        console.log(position);
        let lat = position.coords.latitude;
        let lon = position.coords.longitude;

        map.setView([lat, lon]);
        let marker = L.marker([lat, lon]).addTo(map);
        marker.bindPopup("You are here!");
    }, positionError => {
        console.error(positionError);
    });
});

let pieces = [];
document.getElementById("saveButton").addEventListener("click", function() {
    leafletImage(map, function (err, canvas) {
        let rasterMap = document.getElementById("rasterMap");
        let rasterContext = rasterMap.getContext("2d");
        let shuffledDiv = document.getElementById("shuffledPuzzles");
        let dragTarget = document.getElementById("drag-target");

        rasterMap.width = 600;
        rasterMap.height = 300;
        rasterContext.drawImage(canvas, 0, 0, 600, 300);

        shuffledDiv.innerHTML = "";
        dragTarget.innerHTML = "";

        let pieceWidth = 600 / 4;
        let pieceHeight = 300 / 4;

        for (let i = 0; i < 4; i++) {
            for (let j = 0; j < 4; j++) {
                let piece = rasterContext.getImageData(j * pieceWidth, i * pieceHeight, pieceWidth, pieceHeight);
                pieces.push({ piece, x: j, y: i });
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
                shuffledDiv.appendChild(pieceCanvas);

                pieceCanvas.addEventListener("dragstart", function(event) {
                    event.dataTransfer.setData("text/plain", JSON.stringify({ x: pieceData.x, y: pieceData.y }));
                });
            }
        }

        dragTarget.addEventListener("dragover", function(event) {
            event.preventDefault();
        });

        dragTarget.addEventListener("drop", function(event) {
            event.preventDefault();
            let data = JSON.parse(event.dataTransfer.getData("text/plain"));
            let targetX = Math.floor(event.offsetX / pieceWidth);
            let targetY = Math.floor(event.offsetY / pieceHeight);

            if (data.x === targetX && data.y === targetY) {
                let pieceCanvas = document.querySelector(`canvas[data-x='${data.x}'][data-y='${data.y}']`);
                pieceCanvas.style.position = "absolute";
                pieceCanvas.style.left = `${targetX * pieceWidth}px`;
                pieceCanvas.style.top = `${targetY * pieceHeight}px`;
                pieceCanvas.draggable = false; // Make the piece no longer draggable
                dragTarget.appendChild(pieceCanvas);
            } else {
                console.log("Incorrect position");
            }
        });



    });
});

