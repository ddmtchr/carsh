import React, { useEffect } from 'react';
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';

const MapComponent: React.FC = () => {
    useEffect(() => {
        // Создаем карту
        const map = L.map('map', {
            center: [59.9343, 30.3351], // Координаты Санкт-Петербурга
            zoom: 12,
        });

        // Добавляем слой с картой (например, OpenStreetMap)
        L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
            attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors',
        }).addTo(map);

        // Добавляем маркер на центр города
        const marker = L.marker([59.9343, 30.3351]).addTo(map);
        marker.bindPopup('Санкт-Петербург').openPopup();

        return () => {
            map.remove();
        };
    }, []);

    return (
        <div
            id="map"
            style={{ width: '100%', height: '100vh' }}
        />
    );
};

const MainScreen: React.FC = () => {
    return (
        <div>
            <h1>React Leaflet Map</h1>
            <MapComponent />
        </div>
    );
};

export default MainScreen;
