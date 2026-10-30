<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="SmartCity360 - A Smart City Management Platform demonstrating Object-Oriented Design Patterns">
    <title>SmartCity360 - Smart City Management Platform</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="css/style.css">
</head>
<body>

<!-- Navigation -->
<nav class="navbar">
    <a href="index.jsp" class="navbar-brand">
        <div class="logo-icon">🏙️</div>
        <span>SmartCity360</span>
    </a>
    <ul class="nav-links">
        <li><a href="index.jsp" class="active">🏠 Home</a></li>
        <li><a href="decorator">🔒 Decorator</a></li>
        <li><a href="composite">🏗️ Composite</a></li>
        <li><a href="chain">📋 Chain</a></li>
        <li><a href="flyweight">💡 Flyweight</a></li>
        <li><a href="interpreter">📜 Interpreter</a></li>
    </ul>
</nav>

<div class="container">

    <!-- Hero Section -->
    <div class="hero">
        <h1>🏙️ SmartCity360</h1>
        <p>A Smart City Management Platform built with Object-Oriented Design Patterns</p>
        <p class="subtitle">Developed by UrbanTech Solutions &bull; Java EE &bull; 5 Design Patterns</p>
    </div>

    <!-- Pattern Cards Grid -->
    <div class="pattern-grid">

        <!-- Part A - Decorator -->
        <a href="decorator" class="pattern-card">
            <div class="part-label">Part A</div>
            <h3>🔒 Dynamic Security Wrappers</h3>
            <p>Secure energy meter data streams with dynamically addable layers — encryption, authentication, and compression — without altering core logic.</p>
            <div class="card-footer">
                <span class="pattern-type">Decorator Pattern (Structural)</span>
                <span class="arrow">→</span>
            </div>
        </a>

        <!-- Part B - Composite -->
        <a href="composite" class="pattern-card">
            <div class="part-label">Part B</div>
            <h3>🏗️ Hierarchical Smart Zones</h3>
            <p>Model city zones (City → District → Street → Building) hierarchically so commands apply uniformly at any level.</p>
            <div class="card-footer">
                <span class="pattern-type">Composite Pattern (Structural)</span>
                <span class="arrow">→</span>
            </div>
        </a>

        <!-- Part C - Chain of Responsibility -->
        <a href="chain" class="pattern-card">
            <div class="part-label">Part C</div>
            <h3>📋 Citizen Complaint Escalation</h3>
            <p>Escalate citizen complaints from local staff to supervisors to city managers based on severity, with full audit trail.</p>
            <div class="card-footer">
                <span class="pattern-type">Chain of Responsibility (Behavioural)</span>
                <span class="arrow">→</span>
            </div>
        </a>

        <!-- Part D - Flyweight -->
        <a href="flyweight" class="pattern-card">
            <div class="part-label">Part D</div>
            <h3>💡 Smart Street Lights</h3>
            <p>Efficiently manage thousands of streetlights by sharing common properties and minimizing memory usage through Flyweight objects.</p>
            <div class="card-footer">
                <span class="pattern-type">Flyweight Pattern (Structural)</span>
                <span class="arrow">→</span>
            </div>
        </a>

        <!-- Part E - Interpreter -->
        <a href="interpreter" class="pattern-card">
            <div class="part-label">Part E</div>
            <h3>📜 City Policy DSL Interpreter</h3>
            <p>Parse and evaluate city operational rules (noise limits, parking hours, speed restrictions) defined in a Domain-Specific Language.</p>
            <div class="card-footer">
                <span class="pattern-type">Interpreter Pattern (Behavioural)</span>
                <span class="arrow">→</span>
            </div>
        </a>

    </div>

</div>

<!-- Footer -->
<footer class="footer">
    <p>SmartCity360 &copy; 2024 UrbanTech Solutions &bull; Object-Oriented Design Patterns Assessment</p>
</footer>

</body>
</html>
