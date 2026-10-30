<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Part D - Flyweight Pattern | SmartCity360</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;800&family=JetBrains+Mono:wght@400;500&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="css/style.css">
</head>
<body>

<nav class="navbar">
    <a href="index.jsp" class="navbar-brand">
        <div class="logo-icon">🏙️</div>
        <span>SmartCity360</span>
    </a>
    <ul class="nav-links">
        <li><a href="index.jsp">🏠 Home</a></li>
        <li><a href="decorator">🔒 Decorator</a></li>
        <li><a href="composite">🏗️ Composite</a></li>
        <li><a href="chain">📋 Chain</a></li>
        <li><a href="flyweight" class="active">💡 Flyweight</a></li>
        <li><a href="interpreter">📜 Interpreter</a></li>
    </ul>
</nav>

<div class="container">

    <div class="page-header">
        <h1>💡 Smart Street Lights <span class="pattern-badge">Flyweight Pattern</span></h1>
        <p>Manage thousands of street lights efficiently by sharing common properties (model, wattage, firmware) across all instances of the same type.</p>
    </div>

    <!-- Configuration -->
    <div class="card">
        <div class="card-header">
            <div class="icon blue">⚙️</div>
            <h2>Generate Street Lights</h2>
        </div>
        <form method="post" action="flyweight" style="display: flex; align-items: end; gap: 1rem; flex-wrap: wrap;">
            <div class="form-group" style="margin-bottom: 0; flex: 1; min-width: 200px;">
                <label for="lightCount">Number of Street Lights (10 - 50,000)</label>
                <input type="number" id="lightCount" name="lightCount" class="form-control" 
                       value="${not empty lightCount ? lightCount : 1000}" min="10" max="50000">
            </div>
            <button type="submit" class="btn btn-primary" style="margin-bottom: 0;">💡 Generate & Analyze</button>
        </form>
    </div>

    <c:if test="${not empty lightCount}">
        <!-- Memory Stats -->
        <div class="stats-grid">
            <div class="stat-card">
                <div class="stat-value">${lightCount}</div>
                <div class="stat-label">Total Lights</div>
            </div>
            <div class="stat-card">
                <div class="stat-value">${uniqueTypes}</div>
                <div class="stat-label">Unique Types (Flyweights)</div>
            </div>
            <div class="stat-card">
                <div class="stat-value">${onCount}</div>
                <div class="stat-label">Lights ON</div>
            </div>
            <div class="stat-card">
                <div class="stat-value">${offCount}</div>
                <div class="stat-label">Lights OFF</div>
            </div>
            <div class="stat-card">
                <div class="stat-value">${totalEnergy}</div>
                <div class="stat-label">Total Energy (kWh)</div>
            </div>
        </div>

        <!-- Memory Comparison -->
        <div class="card">
            <div class="card-header">
                <div class="icon green">📊</div>
                <h2>Memory Usage Comparison</h2>
            </div>

            <div class="stats-grid">
                <div class="stat-card" style="border-color: rgba(239, 68, 68, 0.3);">
                    <div class="stat-value" style="font-size: 1.3rem; -webkit-text-fill-color: #ef4444;">${naiveMemory}</div>
                    <div class="stat-label">Without Flyweight</div>
                </div>
                <div class="stat-card" style="border-color: rgba(16, 185, 129, 0.3);">
                    <div class="stat-value" style="font-size: 1.3rem; -webkit-text-fill-color: #10b981;">${flyweightMemory}</div>
                    <div class="stat-label">With Flyweight ✓</div>
                </div>
                <div class="stat-card" style="border-color: rgba(14, 165, 233, 0.3);">
                    <div class="stat-value" style="font-size: 1.3rem;">${memorySaved}</div>
                    <div class="stat-label">Memory Saved</div>
                </div>
                <div class="stat-card" style="border-color: rgba(139, 92, 246, 0.3);">
                    <div class="stat-value" style="font-size: 1.3rem; -webkit-text-fill-color: #8b5cf6;">${savingsPercent}%</div>
                    <div class="stat-label">Savings Percentage</div>
                </div>
            </div>

            <p style="color: var(--text-secondary); font-size: 0.85rem; margin-top: 0.5rem;">
                ⏱️ Creation time for ${lightCount} lights: <strong>${creationTimeMs} ms</strong>
            </p>
        </div>

        <div class="grid-2">
            <!-- Shared Flyweight Types -->
            <div class="card">
                <div class="card-header">
                    <div class="icon purple">🔗</div>
                    <h2>Shared Flyweight Types (Intrinsic State)</h2>
                </div>
                <p style="color: var(--text-secondary); font-size: 0.85rem; margin-bottom: 1rem;">
                    Only <strong>${uniqueTypes}</strong> type objects are created and shared across all ${lightCount} lights.
                </p>
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>Model</th>
                            <th>Wattage</th>
                            <th>Color Temp</th>
                            <th>Schedule</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="entry" items="${flyweightTypes}">
                            <tr>
                                <td><strong>${entry.value.model}</strong></td>
                                <td>${entry.value.wattage}W</td>
                                <td>${entry.value.colorTemperature}K</td>
                                <td><span class="badge badge-info">${entry.value.defaultSchedule}</span></td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>

            <!-- Sample Lights -->
            <div class="card">
                <div class="card-header">
                    <div class="icon orange">💡</div>
                    <h2>Sample Lights (Extrinsic State)</h2>
                </div>
                <p style="color: var(--text-secondary); font-size: 0.85rem; margin-bottom: 1rem;">
                    Showing first 20 of ${lightCount} lights. Each stores only unique location data.
                </p>
                <div style="overflow-x: auto;">
                    <table class="data-table">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Location</th>
                                <th>Type</th>
                                <th>Brightness</th>
                                <th>Status</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="light" items="${sampleLights}">
                                <tr>
                                    <td><code>${light.lightId}</code></td>
                                    <td>${light.location}</td>
                                    <td>${light.type.model}</td>
                                    <td>${light.brightness}%</td>
                                    <td>
                                        <span class="badge ${light.on ? 'badge-success' : 'badge-danger'}">
                                            ${light.on ? 'ON' : 'OFF'}
                                        </span>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </c:if>

</div>

<footer class="footer">
    <p>SmartCity360 &copy; 2024 UrbanTech Solutions &bull; Part D: Flyweight Pattern</p>
</footer>

</body>
</html>
