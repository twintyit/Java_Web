<%@ page contentType="text/html;charset=UTF-8"%>

<%
      String pageName = (String) request.getAttribute("page");
      String contextPath = request.getContextPath();
%>
<html>
<head>
    <title>PV-221</title>
    <!--Import Google Icon Font-->
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
    <!-- Compiled and minified CSS -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/css/materialize.min.css" />
    <link rel="stylesheet" href="<%=contextPath%>/css/site.css" />
</head>
<body>

  <header>
      <nav>
          <div class="nav-wrapper purple">
              <a href="<%=contextPath%>/" class="site-logo left ">
              <img src="<%=contextPath%>/img/Java_Logo.png" class="logo-img" alt="logo" />
                  PV-221</a>
              <ul id="nav-mobile" class="left">
                  <li><a href="<%=contextPath%>/index">Index</a></li>
                  <li><a href="<%=contextPath%>/table">Table</a></li>
                  <li><a href="<%=contextPath%>/servlets">Servlets</a></li>
              </ul>
              <a class="nav-addon right" href="<%=contextPath%>/signup"><i class="material-icons">person_add</i></a>
          </div>
      </nav>
  </header>

  <main class="container"><jsp:include page='<%=pageName +".jsp"%>' /></main>
    <div class="spacer"></div>
  <footer class="page-footer purple">
      <div class="container">
          <div class="row">
              <div class="col l6 s12">
                  <h5 class="white-text">Footer Content</h5>
                  <p class="grey-text text-lighten-4">You can use rows and columns here to organize your footer content.</p>
              </div>
              <div class="col l4 offset-l2 s12">
                  <h5 class="white-text">Links</h5>
                  <ul>
                      <li><a class="grey-text text-lighten-3" href="#!">Link 1</a></li>
                      <li><a class="grey-text text-lighten-3" href="#!">Link 2</a></li>
                      <li><a class="grey-text text-lighten-3" href="#!">Link 3</a></li>
                      <li><a class="grey-text text-lighten-3" href="#!">Link 4</a></li>
                  </ul>
              </div>
          </div>
      </div>
      <div class="footer-copyright">
          <div class="container">
              © 2024 ITSTEP PV-221
              <a class="grey-text text-lighten-4 right" href="#!">More Links</a>
          </div>
      </div>
  </footer>

  <script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/js/materialize.min.js"></script>
  <script src="<%=contextPath%>/js/site.js"></script>
</body>
</html>
