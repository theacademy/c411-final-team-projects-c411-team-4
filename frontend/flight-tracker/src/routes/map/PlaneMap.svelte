<script lang="js">
    import L from "leaflet";
    import MapToolbar from "./PlaneMapToolbar.svelte";
    import { onMount, onDestroy } from "svelte";
    import * as turf from "@turf/turf";

    let { planes, airports, routes } = $props();

    const PLANE_ICON_WIDTH = 50;
    const PLANE_ICON_HEIGHT = 50;
    const ANIMATION_INTERVAL = 17; // about 60FPS, might be too high for some computers.
    const INTERPOLATION_FACTOR = 0.01;

    // stateful
    let initialCenter = [39.8283, -98.5795]; // Default to center of USA
    let initialZoom = 5;
    let map;
    let planeMarkers = new Map();
    let followingPlaneId = $state(null);
    let airportMarkers = new Map();
    let routePolylines = new Map();
    let animationInterval;

    onMount(() => {
        initializeMap();
        animationInterval = setInterval(
            updateAllPlanePositions,
            ANIMATION_INTERVAL,
        );

        return () => {
            clearInterval(animationInterval);
            if (map) {
                map.remove();
                map = null;
            }
        };
    });

    $effect(() => {
        if (!map) return;

        const currentPlaneIds = new Set(planes.map((p) => p.id));
        for (const [id, marker] of planeMarkers.entries()) {
            if (!currentPlaneIds.has(id)) {
                marker.remove();
                planeMarkers.delete(id);
            }
        }

        for (const plane of planes) {
            if (!planeMarkers.has(plane.id)) {
                addPlaneMarker(plane);
            }
        }
    });

    $effect(() => {
        if (!map) return;

        const currentAirportIds = new Set(airports.map((a) => a.id));
        for (const [id, marker] of airportMarkers.entries()) {
            if (!currentAirportIds.has(id)) {
                marker.remove();
                airportMarkers.delete(id);
            }
        }

        for (const airport of airports) {
            if (!airportMarkers.has(airport.id)) {
                addAirportMarker(airport);
            }
        }
    });

    $effect(() => {
        if (!map) return;

        const currentRouteIds = new Set(routes.map((r) => r.id));
        for (const [id, polyline] of routePolylines.entries()) {
            if (!currentRouteIds.has(id)) {
                polyline.remove();
                routePolylines.delete(id);
            }
        }

        for (const route of routes) {
            if (!routePolylines.has(route.id)) {
                addRoutePolyline(route);
            }
        }
    });

    function initializeMap() {
        map = L.map("map", { preferCanvas: true }).setView(
            initialCenter,
            initialZoom,
        );

        L.tileLayer(
            "https://{s}.basemaps.cartocdn.com/rastertiles/voyager/{z}/{x}/{y}{r}.png",
            {
                attribution: `&copy;<a href="https://www.openstreetmap.org/copyright" target="_blank">OpenStreetMap</a>,
                    &copy;<a href="https://carto.com/attributions" target="_blank">CARTO</a>`,
                subdomains: "abcd",
                maxZoom: 14,
            },
        ).addTo(map);

        // Add toolbar
        let toolbar = L.control({ position: "topright" });
        let toolbarComponent;

        toolbar.onAdd = (map) => {
            let div = L.DomUtil.create("div");
            toolbarComponent = new MapToolbar({
                target: div,
                props: {
                    onReset: () => {
                        map.setView(initialCenter, initialZoom, {
                            animate: true,
                        });
                        followingPlaneId = null;
                    },
                },
            });
            return div;
        };

        toolbar.onRemove = () => {
            if (toolbarComponent) {
                toolbarComponent.$destroy();
            }
        };

        toolbar.addTo(map);

        for (const plane of planes) {
            addPlaneMarker(plane);
        }

        for (const airport of airports) {
            addAirportMarker(airport);
        }

        for (const route of routes) {
            addRoutePolyline(route);
        }
    }

    function addPlaneMarker(plane) {
        const position = [plane.latitude, plane.longitude];
        const marker = L.marker(position, {
            icon: createPlaneIcon(plane.rotation || 0),
            interactive: true,
            bubblingMouseEvents: false,
        }).addTo(map);

        marker.bindTooltip(
            `Click to follow ${plane.name || "plane " + plane.id}`,
            {
                direction: "top",
            },
        );

        marker.on("click", function (e) {
            L.DomEvent.stopPropagation(e);
            toggleFollowPlane(plane.id);
        });

        planeMarkers.set(plane.id, {
            marker,
            targetPos: [...position],
            currentRotation: plane.rotation || 0,
        });
    }

    function addAirportMarker(airport) {
        const position = [airport.latitude, airport.longitude];
        const marker = L.marker(position).addTo(map);

        marker.bindTooltip(airport.code, {
            permanent: true,
            direction: "bottom",
        });

        airportMarkers.set(airport.id, marker);
    }

    function addRoutePolyline(route) {
        const fromAirport = airports.find((a) => a.id === route.fromAirportId);
        const toAirport = airports.find((a) => a.id === route.toAirportId);

        if (fromAirport && toAirport) {
            const from = [fromAirport.longitude, fromAirport.latitude];
            const to = [toAirport.longitude, toAirport.latitude];

            const flightPath = getGreatCirclePath(from, to);
            const polyline = L.polyline(flightPath, {
                color: route.color || "blue",
                weight: route.weight || 2,
            }).addTo(map);

            routePolylines.set(route.id, polyline);
        }
    }

    function createPlaneIcon(rotation = 0) {
        const html = `<div class="plane-icon" style="transform: rotate(${rotation}deg);">
                        <img src="${planes.find((p) => p.iconUrl)?.iconUrl || "https://buckets.kmfg.dev/mthree/plane.png"}" 
                             width="${PLANE_ICON_WIDTH}" height="${PLANE_ICON_HEIGHT}"/>
                      </div>`;
        return L.divIcon({
            html,
            className: "plane-container",
            iconSize: [PLANE_ICON_HEIGHT, PLANE_ICON_WIDTH],
            iconAnchor: [PLANE_ICON_HEIGHT / 2, PLANE_ICON_WIDTH / 2],
        });
    }

    function getGreatCirclePath(from, to, steps = 30) {
        const line = turf.greatCircle(from, to, { npoints: steps });
        return line.geometry.coordinates.map((coord) => [coord[1], coord[0]]); // Swap [lon, lat] -> [lat, lon]
    }

    function calculateBearing(start, end) {
        const latOne = (Math.PI * start[0]) / 180;
        const longOne = (Math.PI * start[1]) / 180;
        const latTwo = (Math.PI * end[0]) / 180;
        const longTwo = (Math.PI * end[1]) / 180;

        const dLong = longTwo - longOne;

        const y = Math.sin(dLong) * Math.cos(latTwo);
        const x =
            Math.cos(latOne) * Math.sin(latTwo) -
            Math.sin(latOne) * Math.cos(latTwo) * Math.cos(dLong);

        const bearing = (Math.atan2(y, x) * 180) / Math.PI;

        return (bearing + 360) % 360;
    }

    function interpolateAngle(currentAngle, targetAngle, factor = 0.1) {
        let delta = targetAngle - currentAngle;

        if (delta > 180) delta -= 360;
        if (delta < -180) delta += 360;

        return (currentAngle + delta * factor) % 360;
    }

    function toggleFollowPlane(planeId) {
        if (followingPlaneId === planeId) {
            followingPlaneId = null;
        } else {
            followingPlaneId = planeId;

            const plane = planes.find((p) => p.id === planeId);
            if (plane) {
                map.setView([plane.latitude, plane.longitude], 7, {
                    animate: true,
                });
            }
        }
    }

    function updateAllPlanePositions() {
        for (const plane of planes) {
            updatePlanePosition(plane);
        }
    }

    function updatePlanePosition(plane) {
        const planeData = planeMarkers.get(plane.id);
        if (!planeData) return;

        if (Math.random() < 0.1) {
            calculateNewTarget(plane);
        }

        const prevPos = [plane.latitude, plane.longitude];

        plane.latitude =
            plane.latitude +
            (planeData.targetPos[0] - plane.latitude) * INTERPOLATION_FACTOR;
        plane.longitude =
            plane.longitude +
            (planeData.targetPos[1] - plane.longitude) * INTERPOLATION_FACTOR;

        const targetRotation = calculateBearing(prevPos, [
            plane.latitude,
            plane.longitude,
        ]);
        planeData.currentRotation = interpolateAngle(
            planeData.currentRotation,
            targetRotation,
            0.1,
        );

        planeData.marker.setIcon(createPlaneIcon(planeData.currentRotation));
        planeData.marker.setLatLng([plane.latitude, plane.longitude]);

        if (followingPlaneId === plane.id) {
            map.panTo([plane.latitude, plane.longitude], {
                animate: true,
                duration: 0.5,
            });
        }
    }

    function calculateNewTarget(plane) {
        const planeData = planeMarkers.get(plane.id);
        if (!planeData) return;

        if (plane.heading !== undefined) {
            const angle = (Math.PI * plane.heading) / 180;
            const stepSize = (plane.speed || 0.1) / 64;

            planeData.targetPos[0] += stepSize * Math.cos(angle);
            planeData.targetPos[1] += stepSize * Math.sin(angle);
        } else {
            // Random movement as fallback
            const angle = Math.random() * 2 * Math.PI;
            const minStep = 0.05 / 64;
            const maxStep = 0.15 / 64;
            const stepSize = Math.random() * (maxStep - minStep) + minStep;

            planeData.targetPos[0] += stepSize * Math.cos(angle);
            planeData.targetPos[1] += stepSize * Math.sin(angle);
        }
    }

    function resizeMap() {
        if (map) {
            map.invalidateSize();
        }
    }
</script>

<svelte:window onresize={resizeMap} />

<link
    rel="stylesheet"
    href="https://unpkg.com/leaflet@1.6.0/dist/leaflet.css"
    integrity="sha512-xwE/Az9zrjBIphAcBb3F6JVqxf46+CDLwfLMHloNu6KEQCAWi6HcDUbeOfBIptF7tcCzusKFjFw2yuvEpDL9wQ=="
    crossorigin=""
/>
<div id="map" style="height:100%;width:100%"></div>

<style>
    .plane-icon {
        font-size: 50px;
        color: #007bff;
        text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.5);
        transition: transform 0.1s ease-out;
    }

    .plane-container {
        cursor: pointer;
    }

    .plane-container {
        z-index: 1000 !important;
    }
</style>
