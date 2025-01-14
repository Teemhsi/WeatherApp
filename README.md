# WeatherApp - Real-Time Weather Data to InfluxDB

## Overview

`WeatherApp` is a Java application that fetches real-time weather data from the **OpenWeatherMap API** for several cities in Italy, processes the temperature data, and stores it in an **InfluxDB** database. This allows users to track the temperature of different cities over time. The data can be later visualized and analyzed using **Grafana** or any other time-series visualization tool.

## Features

- Fetches real-time weather data (temperature) from OpenWeatherMap API.
- Supports multiple cities (e.g., Rome, Milan, Florence, Naples, and Turin).
- Stores the fetched data into **InfluxDB** for time-series analysis.
- Automatically inserts temperature data into InfluxDB at regular intervals.
- Designed for simplicity and scalability; easy to extend to more cities or other weather data parameters.

## Prerequisites

- **Java 11+**: The application is built with Java and requires a JDK to run.
- **InfluxDB**: A time-series database to store the weather data.
- **OpenWeatherMap API Key**: Sign up for a free API key from OpenWeatherMap to fetch weather data.
- **Grafana (Optional)**: For visualizing the data stored in InfluxDB.
