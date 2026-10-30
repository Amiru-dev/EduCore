<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Part C - Chain of Responsibility | SmartCity360</title>
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
        <li><a href="chain" class="active">📋 Chain</a></li>
        <li><a href="flyweight">💡 Flyweight</a></li>
        <li><a href="interpreter">📜 Interpreter</a></li>
    </ul>
</nav>

<div class="container">

    <div class="page-header">
        <h1>📋 Citizen Complaint Escalation <span class="pattern-badge">Chain of Responsibility</span></h1>
        <p>Submit complaints that automatically escalate from local staff → supervisors → city managers based on severity level.</p>
    </div>

    <!-- Chain Visualization -->
    <div class="card" style="margin-bottom: 1.5rem;">
        <div class="card-header">
            <div class="icon purple">🔗</div>
            <h2>Escalation Chain</h2>
        </div>
        <div class="chain-visual">
            <div class="chain-node ${not empty complaint && complaint.handledBy.contains('Local Staff') ? 'active' : ''}">
                <span style="font-size: 1.5rem;">👨‍💼</span>
                <span class="node-title">Mr. Perera</span>
                <span class="node-role">Local Staff</span>
                <span class="badge badge-success">Severity ≤ LOW</span>
            </div>
            <span class="chain-arrow">→</span>
            <div class="chain-node ${not empty complaint && complaint.handledBy.contains('Supervisor') ? 'active' : ''}">
                <span style="font-size: 1.5rem;">👩‍💼</span>
                <span class="node-title">Mrs. Fernando</span>
                <span class="node-role">Supervisor</span>
                <span class="badge badge-warning">Severity ≤ MEDIUM</span>
            </div>
            <span class="chain-arrow">→</span>
            <div class="chain-node ${not empty complaint && complaint.handledBy.contains('City Manager') ? 'active' : ''}">
                <span style="font-size: 1.5rem;">🏛️</span>
                <span class="node-title">Dr. Wickramasinghe</span>
                <span class="node-role">City Manager</span>
                <span class="badge badge-danger">Severity ≤ CRITICAL</span>
            </div>
        </div>
    </div>

    <div class="grid-2">
        <!-- Complaint Form -->
        <div class="card">
            <div class="card-header">
                <div class="icon blue">📝</div>
                <h2>Submit Complaint</h2>
            </div>

            <form method="post" action="chain">
                <div class="form-group">
                    <label for="residentName">Resident Name</label>
                    <input type="text" id="residentName" name="residentName" class="form-control" 
                           placeholder="e.g., Kamal Silva" required>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label for="category">Category</label>
                        <select id="category" name="category" class="form-control">
                            <option value="Noise">🔊 Noise Complaint</option>
                            <option value="Infrastructure">🛣️ Infrastructure</option>
                            <option value="Safety">⚠️ Public Safety</option>
                            <option value="Environment">🌿 Environment</option>
                            <option value="Utilities">💧 Utilities</option>
                        </select>
                    </div>

                    <div class="form-group">
                        <label for="severity">Severity Level</label>
                        <select id="severity" name="severity" class="form-control">
                            <option value="LOW">🟢 LOW - Minor issue</option>
                            <option value="MEDIUM">🟡 MEDIUM - Needs attention</option>
                            <option value="HIGH">🟠 HIGH - Urgent</option>
                            <option value="CRITICAL">🔴 CRITICAL - Emergency</option>
                        </select>
                    </div>
                </div>

                <div class="form-group">
                    <label for="description">Complaint Description</label>
                    <textarea id="description" name="description" class="form-control" rows="3"
                              placeholder="Describe the issue in detail..." required></textarea>
                </div>

                <button type="submit" class="btn btn-primary">📤 Submit Complaint</button>
            </form>
        </div>

        <!-- Results -->
        <div>
            <c:if test="${not empty complaint}">
                <div class="card">
                    <div class="card-header">
                        <div class="icon green">✅</div>
                        <h2>Complaint Resolution</h2>
                    </div>

                    <div class="stats-grid" style="margin-bottom: 1rem;">
                        <div class="stat-card">
                            <div class="stat-value" style="font-size: 1rem;">${complaint.complaintId}</div>
                            <div class="stat-label">Complaint ID</div>
                        </div>
                        <div class="stat-card">
                            <div class="stat-value" style="font-size: 1rem;">${complaint.severity.label}</div>
                            <div class="stat-label">Severity</div>
                        </div>
                        <div class="stat-card">
                            <div class="stat-value" style="font-size: 1rem;">${complaint.handledBy}</div>
                            <div class="stat-label">Handled By</div>
                        </div>
                    </div>

                    <div class="form-group">
                        <label>Resolution</label>
                        <div class="result-box success">${result}</div>
                    </div>

                    <div class="form-group">
                        <label>Escalation Audit Log</label>
                        <div class="result-box">${escalationLog}</div>
                    </div>
                </div>
            </c:if>

            <c:if test="${empty complaint}">
                <div class="card">
                    <div class="card-header">
                        <div class="icon blue">ℹ️</div>
                        <h2>How Escalation Works</h2>
                    </div>
                    <div class="result-box">
Chain of Responsibility Pattern:

1. Complaint enters the chain at LOCAL STAFF
2. If severity > handler's threshold, it ESCALATES to the next handler
3. Each handler logs its decision (handle or pass)
4. The complaint is resolved by the first handler with sufficient authority

Example Escalation:
  🟢 LOW → Handled by Local Staff (Mr. Perera)
  🟡 MEDIUM → Escalated to Supervisor (Mrs. Fernando)
  🟠 HIGH → Escalated to City Manager (Dr. Wickramasinghe)
  🔴 CRITICAL → Escalated to City Manager + Emergency Response
                    </div>
                </div>
            </c:if>
        </div>
    </div>

</div>

<footer class="footer">
    <p>SmartCity360 &copy; 2024 UrbanTech Solutions &bull; Part C: Chain of Responsibility Pattern</p>
</footer>

</body>
</html>
