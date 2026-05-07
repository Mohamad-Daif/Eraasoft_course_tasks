<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<%@ page import="exception.ExceptionModel" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Error ${statusCode}</title>
  <style>
    * {
      margin: 0;
      padding: 0;
      box-sizing: border-box;
    }

    body {
      font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      min-height: 100vh;
      display: flex;
      justify-content: center;
      align-items: center;
      color: #333;
    }

    .error-container {
      background: white;
      padding: 3rem 4rem;
      border-radius: 20px;
      box-shadow: 0 20px 60px rgba(0,0,0,0.3);
      text-align: center;
      max-width: 500px;
      width: 90%;
      animation: slideUp 0.6s ease-out;
    }

    @keyframes slideUp {
      from {
        opacity: 0;
        transform: translateY(40px);
      }
      to {
        opacity: 1;
        transform: translateY(0);
      }
    }

    .error-icon {
      font-size: 5rem;
      margin-bottom: 1rem;
    }

    .error-code {
      font-size: 6rem;
      font-weight: 900;
      color: #667eea;
      line-height: 1;
      margin-bottom: 0.5rem;
    }

    .error-title {
      font-size: 1.5rem;
      color: #555;
      margin-bottom: 1rem;
      font-weight: 600;
    }

    .error-message {
      color: #777;
      font-size: 1rem;
      line-height: 1.6;
      margin-bottom: 2rem;
      padding: 1rem;
      background: #f8f9fa;
      border-radius: 10px;
      border-left: 4px solid #667eea;
    }

    .home-btn {
      display: inline-block;
      padding: 12px 30px;
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      color: white;
      text-decoration: none;
      border-radius: 25px;
      font-weight: 600;
      transition: transform 0.2s, box-shadow 0.2s;
    }

    .home-btn:hover {
      transform: translateY(-2px);
      box-shadow: 0 10px 20px rgba(102, 126, 234, 0.4);
    }

    .error-details {
      margin-top: 2rem;
      font-size: 0.85rem;
      color: #aaa;
    }
  </style>
</head>
<body>
<%
  // Get attributes from request (set by servlet or error handler)
  Integer statusCode = (Integer) request.getAttribute("statusCode");
  String message = (String) request.getAttribute("message");

  // Fallbacks if attributes are missing
  if (statusCode == null) {
    statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
    if (statusCode == null) statusCode = 500;
  }
  if (message == null) {
    message = (String) request.getAttribute("javax.servlet.error.message");
    if (message == null) message = "An unexpected error occurred.";
  }

  // Determine icon and title based on status code
  String icon = "⚠️";
  String title = "Oops! Something went wrong";

  if (statusCode == 404) {
    icon = "🔍";
    title = "Page Not Found";
  } else if (statusCode == 403) {
    icon = "🚫";
    title = "Access Denied";
  } else if (statusCode == 500) {
    icon = "💥";
    title = "Internal Server Error";
  } else if (statusCode >= 400 && statusCode < 500) {
    icon = "❌";
    title = "Client Error";
  }
%>

<div class="error-container">
  <div class="error-icon"><%= icon %></div>
  <div class="error-code"><%= statusCode %></div>
  <div class="error-title"><%= title %></div>
  <div class="error-message">
    <%= message != null ? message : "No additional details available." %>
  </div>
  <a href="${pageContext.request.contextPath}/" class="home-btn">
    ← Back to Home
  </a>
  <div class="error-details">
    Request ID: <%= java.util.UUID.randomUUID().toString().substring(0, 8) %>
  </div>
</div>
</body>
</html>
