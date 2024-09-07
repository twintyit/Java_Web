<%@page contentType="text/html; charset=UTF-8"%>
<html>
<head>
    <title>Java 221</title>
</head>
<body>
<h1>Java web</h1>
<h2>JSP</h2>
<p>
    <code>&lt;%= 2 + 3 %&gt; = <%= 2 + 3 %> </code>
</p>
<p>
    &lt;% Java  %&gt;
    <br/>
    <code>&lt;int x = 10; %&gt; <% int x = 10; %> </code>
    <br/>
    <code>&lt;%= x %&gt; = <%= x %> </code>
</p>

<h3>Умовна верстка</h3>
<p>
    <pre>
    &lt;% if (Умова) {%&gt;
        HTML-якщо-true
    &lt;%} else {%&gt;
        HTML-якщо-false
    &lt;%} &gt;
    </pre>
    <br/>
    <% if( x % 2 == 0 ) { %>
        <b>x - парне число</b>
    <% } else { %>
        <i>х - непарне число</i>
    <% } %>
</p>

<h3>Цикли</h3>
<pre>
&lt;% for (int i = 0; i < 10 ; i++) {%&gt;
    HTML &lt;%= i%&gt;
&lt;% } %&gt;
</pre>
<% for (int i = 0; i < 10 ; i++) {%>
    <span><%= i%></span>
<% } %>

<%
    String[] arr = { "Product1", "Product2", "Product3", "Product4", "Product5" };
%>

<ul>
    <% for(String str :arr) {%>
        <li><%= str %></li>
    <%}%>
</ul>




</body>
</html>
