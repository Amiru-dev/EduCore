<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Part E - Interpreter Pattern | SmartCity360</title>
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
        <li><a href="flyweight">💡 Flyweight</a></li>
        <li><a href="interpreter" class="active">📜 Interpreter</a></li>
    </ul>
</nav>

<div class="container">

    <div class="page-header">
        <h1>📜 City Policy DSL Interpreter <span class="pattern-badge">Interpreter Pattern</span></h1>
        <p>Parse and evaluate city operational rules (noise limits, parking fees, speed restrictions) defined in a Domain-Specific Language.</p>
    </div>

    <div class="grid-2">
        <!-- Context Form -->
        <div class="card">
            <div class="card-header">
                <div class="icon blue">🌍</div>
                <h2>Set City Context Variables</h2>
            </div>

            <form method="post" action="interpreter">
                <div class="form-row">
                    <div class="form-group">
                        <label for="zone">Zone Type</label>
                        <select id="zone" name="zone" class="form-control">
                            <option value="residential" ${zone == 'residential' ? 'selected' : ''}>🏘️ Residential</option>
                            <option value="commercial" ${zone == 'commercial' ? 'selected' : ''}>🏢 Commercial</option>
                            <option value="industrial" ${zone == 'industrial' ? 'selected' : ''}>🏭 Industrial</option>
                            <option value="school" ${zone == 'school' ? 'selected' : ''}>🏫 School</option>
                            <option value="hospital" ${zone == 'hospital' ? 'selected' : ''}>🏥 Hospital</option>
                        </select>
                    </div>

                    <div class="form-group">
                        <label for="time">Current Time</label>
                        <input type="text" id="time" name="time" class="form-control" 
                               value="${not empty time ? time : '23:00'}" placeholder="HH:MM format">
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label for="noiseLevel">Noise Level (dB)</label>
                        <input type="number" id="noiseLevel" name="noiseLevel" class="form-control" 
                               value="${not empty noiseLevel ? noiseLevel : '55'}" min="0" max="120">
                    </div>

                    <div class="form-group">
                        <label for="parkingZone">Parking Zone</label>
                        <select id="parkingZone" name="parkingZone" class="form-control">
                            <option value="A" ${parkingZone == 'A' ? 'selected' : ''}>Zone A (Premium)</option>
                            <option value="B" ${parkingZone == 'B' ? 'selected' : ''}>Zone B (Standard)</option>
                            <option value="C" ${parkingZone == 'C' ? 'selected' : ''}>Zone C (Economy)</option>
                        </select>
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label for="day">Day of Week</label>
                        <select id="day" name="day" class="form-control">
                            <option value="Monday" ${day == 'Monday' ? 'selected' : ''}>Monday</option>
                            <option value="Tuesday" ${day == 'Tuesday' ? 'selected' : ''}>Tuesday</option>
                            <option value="Wednesday" ${day == 'Wednesday' ? 'selected' : ''}>Wednesday</option>
                            <option value="Thursday" ${day == 'Thursday' ? 'selected' : ''}>Thursday</option>
                            <option value="Friday" ${day == 'Friday' ? 'selected' : ''}>Friday</option>
                            <option value="Saturday" ${day == 'Saturday' ? 'selected' : ''}>Saturday</option>
                            <option value="Sunday" ${day == 'Sunday' ? 'selected' : ''}>Sunday</option>
                        </select>
                    </div>

                    <div class="form-group">
                        <label for="vehicleSpeed">Vehicle Speed (km/h)</label>
                        <input type="number" id="vehicleSpeed" name="vehicleSpeed" class="form-control" 
                               value="${not empty vehicleSpeed ? vehicleSpeed : '70'}" min="0" max="200">
                    </div>
                </div>

                <div style="border-top: 1px solid var(--border); padding-top: 1rem; margin-top: 0.5rem;">
                    <div class="form-row">
                        <div class="form-group">
                            <label for="customRuleName">Custom Rule Name (Optional)</label>
                            <input type="text" id="customRuleName" name="customRuleName" class="form-control" 
                                   value="${customRuleName}" placeholder="e.g., CUSTOM-001">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="customRule">Custom DSL Rule (Optional)</label>
                        <textarea id="customRule" name="customRule" class="form-control" rows="2"
                                  placeholder="e.g., IF zone = hospital AND noise_level > 30 THEN enforce_silence">${customRule}</textarea>
                    </div>
                </div>

                <button type="submit" class="btn btn-primary">📜 Evaluate Policies</button>
            </form>
        </div>

        <!-- Results -->
        <div>
            <c:if test="${not empty evaluationResult}">
                <!-- Summary Stats -->
                <div class="stats-grid">
                    <div class="stat-card">
                        <div class="stat-value">${totalRules}</div>
                        <div class="stat-label">Total Rules</div>
                    </div>
                    <div class="stat-card">
                        <div class="stat-value" style="-webkit-text-fill-color: #10b981;">${triggeredRules}</div>
                        <div class="stat-label">Rules Triggered</div>
                    </div>
                    <div class="stat-card">
                        <div class="stat-value" style="-webkit-text-fill-color: #64748b;">${totalRules - triggeredRules}</div>
                        <div class="stat-label">Not Triggered</div>
                    </div>
                </div>

                <c:if test="${not empty customRuleStatus}">
                    <div class="card" style="margin-bottom: 1rem;">
                        <div class="result-box ${customRuleStatus.contains('Error') ? 'danger' : 'success'}">${customRuleStatus}</div>
                    </div>
                </c:if>

                <!-- Individual Rule Results -->
                <div class="card">
                    <div class="card-header">
                        <div class="icon green">📊</div>
                        <h2>Policy Evaluation Results</h2>
                    </div>

                    <c:forEach var="rule" items="${rules}">
                        <div style="padding: 0.75rem; margin-bottom: 0.75rem; background: var(--bg-dark); border: 1px solid ${rule.isSatisfied(context) ? 'rgba(16, 185, 129, 0.3)' : 'var(--border)'}; border-radius: var(--radius-sm);">
                            <div style="display: flex; align-items: center; gap: 0.5rem; margin-bottom: 0.35rem;">
                                <span class="badge ${rule.isSatisfied(context) ? 'badge-success' : 'badge-danger'}">
                                    ${rule.isSatisfied(context) ? '✓ TRIGGERED' : '✗ NOT MET'}
                                </span>
                                <strong style="font-size: 0.9rem;">${rule.ruleName}</strong>
                                <span style="color: var(--text-muted); font-size: 0.8rem;">— ${rule.description}</span>
                            </div>
                            <div style="font-family: 'JetBrains Mono', monospace; font-size: 0.8rem; color: var(--text-secondary);">
                                IF ${rule.condition.toExpressionString()} THEN ${rule.action}
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:if>

            <c:if test="${empty evaluationResult}">
                <div class="card">
                    <div class="card-header">
                        <div class="icon purple">📋</div>
                        <h2>Predefined City Policy Rules (DSL)</h2>
                    </div>
                    <div class="result-box">
${sampleRules}

---
Sample Context:
${sampleContext}

The Interpreter Pattern parses these DSL strings into
an Abstract Syntax Tree (AST) of Expression objects,
then evaluates them against the current city context.
                    </div>
                </div>
            </c:if>
        </div>
    </div>

</div>

<footer class="footer">
    <p>SmartCity360 &copy; 2024 UrbanTech Solutions &bull; Part E: Interpreter Pattern</p>
</footer>

</body>
</html>
