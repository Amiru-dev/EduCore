<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Part B - Composite Pattern | SmartCity360</title>
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
        <li><a href="composite" class="active">🏗️ Composite</a></li>
        <li><a href="chain">📋 Chain</a></li>
        <li><a href="flyweight">💡 Flyweight</a></li>
        <li><a href="interpreter">📜 Interpreter</a></li>
    </ul>
</nav>

<div class="container">

    <div class="page-header">
        <h1>🏗️ Hierarchical Smart Zones <span class="pattern-badge">Composite Pattern</span></h1>
        <p>Model city zones hierarchically (City → District → Street → Building) and apply commands uniformly at any level.</p>
    </div>

    <!-- Stats -->
    <div class="stats-grid">
        <div class="stat-card">
            <div class="stat-value">${not empty totalUsage ? totalUsage : '2781.5'}</div>
            <div class="stat-label">Total Usage (kWh)</div>
        </div>
        <div class="stat-card">
            <div class="stat-value">2</div>
            <div class="stat-label">Districts</div>
        </div>
        <div class="stat-card">
            <div class="stat-value">4</div>
            <div class="stat-label">Streets</div>
        </div>
        <div class="stat-card">
            <div class="stat-value">10</div>
            <div class="stat-label">Buildings</div>
        </div>
    </div>

    <div class="grid-2">
        <!-- Zone Control -->
        <div class="card">
            <div class="card-header">
                <div class="icon blue">⚡</div>
                <h2>Zone Power Control</h2>
            </div>

            <form method="post" action="composite">
                <div class="form-group">
                    <label for="targetZone">Target Zone</label>
                    <select id="targetZone" name="targetZone" class="form-control">
                        <option value="city">🏙️ Entire City (Colombo)</option>
                        <option value="Colombo-07">📍 District: Colombo-07 (Cinnamon Gardens)</option>
                        <option value="Colombo-03">📍 District: Colombo-03 (Kollupitiya)</option>
                        <option value="Maitland">🛣️ Street: Maitland Place</option>
                        <option value="Ward">🛣️ Street: Ward Place</option>
                        <option value="Galle">🛣️ Street: Galle Road</option>
                        <option value="Duplication">🛣️ Street: Duplication Road</option>
                    </select>
                </div>

                <div class="btn-group">
                    <button type="submit" name="action" value="shutdown" class="btn btn-danger">⛔ Shutdown Power</button>
                    <button type="submit" name="action" value="restore" class="btn btn-success">✅ Restore Power</button>
                </div>
            </form>

            <c:if test="${not empty operationResult}">
                <div style="margin-top: 1rem;">
                    <label style="font-size: 0.875rem; font-weight: 500; color: var(--text-secondary);">
                        Operation Result (${lastAction} on ${lastTarget})
                    </label>
                    <div class="result-box ${lastAction == 'shutdown' ? 'warning' : 'success'}">
${operationResult}
                    </div>
                </div>
            </c:if>
        </div>

        <!-- Zone Hierarchy Report -->
        <div class="card">
            <div class="card-header">
                <div class="icon green">🌳</div>
                <h2>Zone Hierarchy & Usage Report</h2>
            </div>

            <div class="result-box">
${not empty report ? report : '[City] Colombo | Power: ON | Usage: 2781.5 kWh | Sub-zones: 2
    [District] Colombo-07 (Cinnamon Gardens) | Power: ON | Usage: 1336.7 kWh | Sub-zones: 2
        [Street] Maitland Place | Power: ON | Usage: 951.5 kWh | Sub-zones: 3
            [Building] City Hospital | Power: ON | Usage: 450.5 kWh
            [Building] Central Library | Power: ON | Usage: 120.3 kWh
            [Building] Tech Park Tower | Power: ON | Usage: 380.7 kWh
        [Street] Ward Place | Power: ON | Usage: 385.2 kWh | Sub-zones: 2
            [Building] Government Office | Power: ON | Usage: 210.0 kWh
            [Building] Smart School Complex | Power: ON | Usage: 175.2 kWh
    [District] Colombo-03 (Kollupitiya) | Power: ON | Usage: 1444.8 kWh | Sub-zones: 2
        [Street] Galle Road | Power: ON | Usage: 1261.3 kWh | Sub-zones: 3
            [Building] Shopping Mall | Power: ON | Usage: 520.8 kWh
            [Building] Hotel Grand | Power: ON | Usage: 410.0 kWh
            [Building] Convention Center | Power: ON | Usage: 330.5 kWh
        [Street] Duplication Road | Power: ON | Usage: 184.0 kWh | Sub-zones: 2
            [Building] Residential Tower A | Power: ON | Usage: 95.4 kWh
            [Building] Residential Tower B | Power: ON | Usage: 88.6 kWh'}
            </div>
        </div>
    </div>

</div>

<footer class="footer">
    <p>SmartCity360 &copy; 2024 UrbanTech Solutions &bull; Part B: Composite Pattern</p>
</footer>

</body>
</html>
