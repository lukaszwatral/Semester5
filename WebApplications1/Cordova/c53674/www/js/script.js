let apiKey = "369eaf134483b1376eea77db7bb9f4bf";
let submitButton = document.getElementById("submit");
let cityInput = document.getElementById("city");

function fetchWeather() {
    let city = cityInput.value;
    if (city !== "") {
        let req = new XMLHttpRequest();
        req.open("GET", `https://api.openweathermap.org/data/2.5/weather?q=${city}&appid=${apiKey}&units=metric`, true);
        req.onload = function () {
            let response = JSON.parse(req.responseText);
            console.log(response);

            document.getElementById('weather').style.visibility = "visible";
            document.getElementById('container').getElementsByTagName('h1')[0].style.margin = "0px";
            let weather = document.getElementById("weather");
            weather.innerHTML = ''; // Clear previous content


            let cityName = response.name;
            let temp = Number(response.main.temp.toFixed(0)) === -0 ? 0 : Number(response.main.temp.toFixed(0));
            let tempMin = Number(response.main.temp_min.toFixed(0)) === -0 ? 0 : Number(response.main.temp_min.toFixed(0));
            let tempMax = Number(response.main.temp_max.toFixed(0)) === -0 ? 0 : Number(response.main.temp_max.toFixed(0));
            let description = response.weather[0].description;
            let icon = response.weather[0].icon;
            let feelsLike = Number(response.main.feels_like.toFixed(0)) === -0 ? 0 : Number(response.main.feels_like.toFixed(0));
            let humidity = response.main.humidity;
            let wind = response.wind.speed;
            let pressure = response.main.pressure;

            let currentDateTime = new Date();
            let day = currentDateTime.toLocaleString('en-US', {weekday: 'short'});
            let month = currentDateTime.toLocaleString('en-US', {month: 'long'});
            let dayNumber = currentDateTime.getDate();
            let time = currentDateTime.toLocaleTimeString('pl-PL', {timeZone: 'UTC'});
            time = time.substring(0, 8);

            let card = document.createElement("div");
            card.className = "card";

            let cityNameElement = document.createElement("h2");
            cityNameElement.id = "cityName";
            cityNameElement.textContent = cityName;

            let dateTimeElement = document.createElement("h3");
            dateTimeElement.id = "dateTime";
            dateTimeElement.textContent = `${day}, ${dayNumber} ${month} ${time}`;

            let weatherInfo = document.createElement("div");
            weatherInfo.className = "weatherInfo";

            let iconElement = document.createElement("img");
            iconElement.id = "icon";
            iconElement.src = `https://openweathermap.org/img/w/${icon}.png`;
            iconElement.alt = "icon";

            let tempElement = document.createElement("h2");
            tempElement.id = "temp";
            tempElement.textContent = `${temp}°C`;

            weatherInfo.appendChild(iconElement);
            weatherInfo.appendChild(tempElement);

            let detailsElement = document.createElement("h3");
            detailsElement.id = "details";
            detailsElement.textContent = description;

            let feelsLikeElement = document.createElement("h5");
            feelsLikeElement.id = "feelsLike";
            feelsLikeElement.textContent = `${tempMin}°C/${tempMax}°C Feels like ${feelsLike}°C`;

            let weatherStatsContainer = document.createElement("div");
            weatherStatsContainer.className = "weatherStatsContainer";

            let windElement = document.createElement("div");
            windElement.className = "weatherStats";
            windElement.innerHTML = `<img src="./img/wind.png" alt="wind"><h5 id="wind">${wind} m/s</h5>`;

            let pressureElement = document.createElement("div");
            pressureElement.className = "weatherStats";
            pressureElement.innerHTML = `<img src="./img/pressure.png" alt="pressure"><h5 id="pressure">${pressure} hPa</h5>`;

            let humidityElement = document.createElement("div");
            humidityElement.className = "weatherStats";
            humidityElement.innerHTML = `<img src="./img/humidity.png" alt="humidity"><h5 id="humidity">${humidity}%</h5>`;

            weatherStatsContainer.appendChild(windElement);
            weatherStatsContainer.appendChild(pressureElement);
            weatherStatsContainer.appendChild(humidityElement);

            card.appendChild(cityNameElement);
            card.appendChild(dateTimeElement);
            card.appendChild(weatherInfo);
            card.appendChild(detailsElement);
            card.appendChild(feelsLikeElement);
            card.appendChild(weatherStatsContainer);

            weather.appendChild(card);

        };
        req.send();

        fetch(`https://api.openweathermap.org/data/2.5/forecast?q=${city}&appid=${apiKey}&units=metric`)
            .then((response) => response.json())
            .then((data) => {
                console.log(data);
                let forecastContainer = document.getElementById("forecastSlider");
                forecastContainer.innerHTML = ''; // Clear previous content

                let groupedForecasts = {};

                data.list.forEach((forecast) => {
                    if (forecast.dt * 1000 < Date.now() + 3600000) {
                        return;
                    }
                    let date = new Date(forecast.dt * 1000);
                    let dayKey = date.toLocaleDateString('en-US', {weekday: 'short', day: 'numeric', month: 'long'}).replace(/(\w+), (\w+) (\d+)/, '$1, $3 $2');
                    if (!groupedForecasts[dayKey]) {
                        groupedForecasts[dayKey] = [];
                    }

                    let forecastCard = document.createElement("div");
                    forecastCard.className = "sliderCard card";

                    let time = date.toLocaleTimeString('pl-PL', {timeZone: 'UTC'});
                    time = time.substring(0, 5);
                    let dateElement = document.createElement("h3");
                    dateElement.textContent = `${dayKey} ${time}`;

                    let weatherInfo = document.createElement("div");
                    weatherInfo.className = "weatherInfo";

                    let iconElement = document.createElement("img");
                    iconElement.id = "icon";
                    iconElement.src = `https://openweathermap.org/img/w/${forecast.weather[0].icon}.png`;
                    iconElement.alt = "icon";

                    let tempElement = document.createElement("h2");
                    tempElement.id = "temp";
                    tempElement.textContent = `${Number(forecast.main.temp.toFixed(0)) === -0 ? 0 : forecast.main.temp.toFixed(0)}°C`;

                    weatherInfo.appendChild(iconElement);
                    weatherInfo.appendChild(tempElement);

                    let descriptionElement = document.createElement("h3");
                    descriptionElement.textContent = forecast.weather[0].description;

                    let feelsLikeElement = document.createElement("h5");
                    feelsLikeElement.textContent = `Feels like ${Number(forecast.main.feels_like.toFixed(0)) === -0 ? 0 : Number(forecast.main.feels_like.toFixed(0))}°C`;
                    let weatherStatsContainer = document.createElement("div");
                    weatherStatsContainer.className = "weatherStatsContainer";

                    let windElement = document.createElement("div");
                    windElement.className = "weatherStats";
                    windElement.innerHTML = `<img src="./img/wind.png" alt="wind"><h5 id="wind">${forecast.wind.speed} m/s</h5>`;

                    let pressureElement = document.createElement("div");
                    pressureElement.className = "weatherStats";
                    pressureElement.innerHTML = `<img src="./img/pressure.png" alt="pressure"><h5 id="pressure">${forecast.main.pressure} hPa</h5>`;

                    let humidityElement = document.createElement("div");
                    humidityElement.className = "weatherStats";
                    humidityElement.innerHTML = `<img src="./img/humidity.png" alt="humidity"><h5 id="humidity">${forecast.main.humidity}%</h5>`;

                    weatherStatsContainer.appendChild(windElement);
                    weatherStatsContainer.appendChild(pressureElement);
                    weatherStatsContainer.appendChild(humidityElement);

                    forecastCard.appendChild(dateElement);
                    forecastCard.appendChild(weatherInfo);
                    forecastCard.appendChild(descriptionElement);
                    forecastCard.appendChild(feelsLikeElement);
                    forecastCard.appendChild(weatherStatsContainer);

                    groupedForecasts[dayKey].push(forecastCard);
                });

                for (let dayKey in groupedForecasts) {
                    let dayContainer = document.createElement("div");
                    dayContainer.className = "dayContainer sliderContainer";

                    let dayTitle = document.createElement("h2");
                    dayContainer.appendChild(dayTitle);

                    let sliderWrapper = document.createElement("div");
                    sliderWrapper.className = "sliderWrapper";

                    let sliderTrack = document.createElement("div");
                    sliderTrack.className = "sliderTrack";

                    groupedForecasts[dayKey].forEach(card => {
                        sliderTrack.appendChild(card);
                    });

                    sliderWrapper.appendChild(sliderTrack);
                    dayContainer.appendChild(sliderWrapper);

                    let prevButton = document.createElement("button");
                    prevButton.className = "sliderButton prev";
                    prevButton.textContent = "<";
                    dayContainer.appendChild(prevButton);

                    let nextButton = document.createElement("button");
                    nextButton.className = "sliderButton next";
                    nextButton.textContent = ">";
                    dayContainer.appendChild(nextButton);

                    forecastContainer.appendChild(dayContainer);

                    let currentIndex = 0;

                    function updateSlider() {
                        const cardWidth = sliderTrack.querySelector('.sliderCard').offsetWidth;
                        sliderTrack.style.transform = `translateX(-${currentIndex * cardWidth}px)`;
                    }

                    nextButton.addEventListener('click', () => {
                        if (currentIndex < groupedForecasts[dayKey].length - 1) {
                            currentIndex++;
                        } else {
                            currentIndex = 0; // Loop back to the beginning
                        }
                        updateSlider();
                    });

                    prevButton.addEventListener('click', () => {
                        if (currentIndex > 0) {
                            currentIndex--;
                        } else {
                            currentIndex = groupedForecasts[dayKey].length - 1; // Loop back to the end
                        }
                        updateSlider();
                    });

                    updateSlider();
                }
            });
    }
}

submitButton.addEventListener("click", fetchWeather);

cityInput.addEventListener("keydown", function(event) {
    if (event.key === "Enter") {
        fetchWeather();
    }
});