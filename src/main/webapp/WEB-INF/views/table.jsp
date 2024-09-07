<%@ page contentType="text/html;charset=UTF-8"%>
<title>Таблиця товарів</title>
<style>
    table {
        border-collapse: collapse;
        width: 50%;
        margin: 20px auto;
    }
    table, th, td {
        border: 1px solid black;
    }
    th, td {
        padding: 10px;
        text-align: center;
    }
    th {
        background-color: #f2f2f2;
    }
</style>

<%
    String[] products = { "Product 1", "Product 2", "Product 3", "Product 4", "Product 5" };
    double[] prices = { 100.50, 150.75, 200.20, 250.00, 300.99 };

%>

<h2>List of products</h2>

<table>
    <tr>
        <th>№</th>
        <th>Name of product</th>
        <th>Price</th>
    </tr>

    <%
        for (int i = 0; i < products.length; i++) {
    %>
    <tr>
        <td><%= i + 1 %></td>
        <td><%= products[i] %></td>
        <td><%= String.format("%.2f", prices[i]) %></td>
    </tr>
    <%
        }
    %>
</table>