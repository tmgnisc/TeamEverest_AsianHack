Smart Agritech - Hackathon Project by Everest Innovators
Overview

Smart Agritech is a comprehensive IoT-based solution aimed at enhancing agriculture management. The project is designed for municipalities and farmers to monitor and optimize their agricultural practices. Using a web-based application developed in Java Spring Boot, Smart Agritech enables municipalities, agriculture experts, and farmers to monitor environmental conditions, automate irrigation, and improve crop health. The project is developed by Everest Innovators and is intended to be sold to municipalities as a commercial solution.
Features
Key Features:

    Automated Irrigation: The soil moisture sensor controls a water pump, ensuring that crops receive optimal water based on real-time soil moisture levels.
    Environmental Monitoring: Data from sensors (Soil moisture, MQ135, and DHT) allow farmers to monitor:
        Soil Moisture
        Air Quality
        Temperature and Humidity (using DHT sensor)
    Multi-User Panels:
        Super Admin Panel (Municipality/Government): Manage IoT devices, monitor farms, view municipality maps, and manage data on farmers and crop production.
        Admin Panel (Agriculture Experts): Install and maintain devices on farmers' lands and assist farmers with technical issues.
        Farmer Panel: View sensor data, monitor crop health, manage water pumps, and contact agriculture experts for support.
    Municipal Data Insights: Provides the municipality with data on farmers, crop production, environmental conditions, and device status.
    Profit Model: Devices and apps are sold to municipalities with regular maintenance charges.

System Architecture

    IoT Devices:
        ESP Microcontroller: Central controller for collecting sensor data and controlling water pumps.
        Soil Moisture Sensor: Monitors soil moisture levels and triggers automatic irrigation.
        MQ135 Sensor: Measures air quality.
        DHT Sensor: Monitors temperature and humidity.

    Web-Based Application:
        Developed in Java Spring Boot.
        Three User Panels (Super Admin, Admin, Farmer).
        Integrated Google Maps API to show device locations on municipal maps.

    Real-Time Communication:
        Sensor data is continuously sent to the web app for real-time monitoring.
        Alerts and notifications are provided if there is an issue (e.g., low soil moisture).

How It Works

    Super Admin Panel (Municipality/Government):
        Add and monitor IoT devices across the municipality.
        View real-time data from farms.
        Access reports on crops and farmer production.

    Admin Panel (Agriculture Experts):
        Install and monitor IoT devices on farmland.
        Provide support to farmers through the app.
        Offer insights into crop health based on sensor data.

    Farmer Panel:
        View real-time data from sensors on their land.
        Automatically control the water pump for irrigation.
        Get environmental data (air quality, humidity, temperature) to assess crop health.
        Contact agriculture experts for help directly through the app.

Setup Instructions
Prerequisites

    Java 17
    Spring Boot 3.3
    MySQL Database
    ESP Microcontroller with attached sensors (Soil Moisture, MQ135, DHT)
    Leaflet JS API for displaying device locations
    Maven for building the project
