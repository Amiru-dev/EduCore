<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Part A - Decorator Pattern | SmartCity360</title>
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
        <li><a href="decorator" class="active">🔒 Decorator</a></li>
        <li><a href="composite">🏗️ Composite</a></li>
        <li><a href="chain">📋 Chain</a></li>
        <li><a href="flyweight">💡 Flyweight</a></li>
        <li><a href="interpreter">📜 Interpreter</a></li>
    </ul>
</nav>

<div class="container">

    <div class="page-header">
        <h1>🔒 Dynamic Security Wrappers <span class="pattern-badge">Decorator Pattern</span></h1>
        <p>Add encryption, authentication, and compression layers to energy meter data streams dynamically — without modifying the core stream logic.</p>
    </div>

    <div class="grid-2">
        <!-- Input Form -->
        <div class="card">
            <div class="card-header">
                <div class="icon blue">⚙️</div>
                <h2>Configure Data Stream</h2>
            </div>

            <form method="post" action="decorator">
                <div class="form-group">
                    <label for="meterId">Energy Meter ID</label>
                    <input type="text" id="meterId" name="meterId" class="form-control" 
                           value="${not empty meterId ? meterId : 'EM-001'}" placeholder="e.g., EM-001">
                </div>

                <div class="form-group">
                    <label for="inputData">Raw Sensor Data</label>
                    <textarea id="inputData" name="inputData" class="form-control" 
                              rows="3">${not empty inputData ? inputData : 'kWh=1250.7;voltage=230;current=5.4;powerFactor=0.95'}</textarea>
                </div>

                <div class="form-group">
                    <label>Security Layers (select to apply decorators)</label>
                    <div class="checkbox-group">
                        <div class="checkbox-item">
                            <input type="checkbox" id="auth" name="authentication" ${useAuth ? 'checked' : ''}>
                            <label for="auth">🔑 Authentication</label>
                        </div>
                        <div class="checkbox-item">
                            <input type="checkbox" id="encrypt" name="encryption" ${useEncryption ? 'checked' : ''}>
                            <label for="encrypt">🔐 Encryption (AES-256)</label>
                        </div>
                        <div class="checkbox-item">
                            <input type="checkbox" id="compress" name="compression" ${useCompression ? 'checked' : ''}>
                            <label for="compress">📦 Compression (GZIP)</label>
                        </div>
                    </div>
                </div>

                <div class="form-group">
                    <label for="authToken">Auth Token</label>
                    <input type="text" id="authToken" name="authToken" class="form-control" 
                           value="${not empty authToken ? authToken : 'SMART-TOKEN-2024'}" placeholder="Enter authentication token">
                </div>

                <button type="submit" class="btn btn-primary">🚀 Process Stream</button>
            </form>
        </div>

        <!-- Results -->
        <div>
            <c:if test="${not empty appliedLayers}">
                <div class="card">
                    <div class="card-header">
                        <div class="icon purple">📐</div>
                        <h2>Applied Decorator Layers</h2>
                    </div>
                    <div class="layer-stack">
                        <c:forEach var="layer" items="${appliedLayers}" varStatus="status">
                            <div class="layer-item">
                                <span class="layer-num">${status.index + 1}</span>
                                <span>${layer}</span>
                            </div>
                        </c:forEach>
                    </div>
                </div>

                <div class="card">
                    <div class="card-header">
                        <div class="icon green">📊</div>
                        <h2>Stream Output</h2>
                    </div>

                    <div class="form-group">
                        <label>Stream Description (Wrapper Chain)</label>
                        <div class="result-box">${description}</div>
                    </div>

                    <div class="form-group">
                        <label>Read Result (Data processed through all layers)</label>
                        <div class="result-box success">${readResult}</div>
                    </div>
                </div>
            </c:if>

            <c:if test="${empty appliedLayers}">
                <div class="card">
                    <div class="card-header">
                        <div class="icon blue">ℹ️</div>
                        <h2>How It Works</h2>
                    </div>
                    <p style="color: var(--text-secondary); line-height: 1.8;">
                        The <strong>Decorator Pattern</strong> wraps the base <code>EnergyMeterStream</code> with
                        security layers at runtime. Each decorator implements the same <code>DataStream</code>
                        interface, so layers can be combined in any order:
                    </p>
                    <div class="result-box" style="margin-top: 1rem;">
Compress(Encrypt(Authenticate(EnergyMeterStream)))

Each layer adds its processing:
  → Authentication: Validates access token
  → Encryption: Encrypts/decrypts data (AES-256)
  → Compression: Compresses/decompresses data (GZIP)

Key Benefit: Core stream logic is NEVER modified.
New security layers can be added without touching existing code.
                    </div>
                </div>
            </c:if>
        </div>
    </div>

</div>

<footer class="footer">
    <p>SmartCity360 &copy; 2024 UrbanTech Solutions &bull; Part A: Decorator Pattern</p>
</footer>

</body>
</html>
