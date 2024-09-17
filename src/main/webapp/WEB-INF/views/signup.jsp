
<%@ page contentType="text/html; charset=UTF-8" %>
<%
  String contextPath = request.getContextPath();
%>
<h1>Register user</h1>
  <div class="row">
    <form class="card-panel grey lighten-5"
          id="signup-form"
          action="<%=contextPath%>/signup"
          enctype="multipart/form-data"
          method="post"
          accept-charset="UTF-8">

      <div class="row">
        <div class="input-field col s6">
          <i class="material-icons prefix">badge</i>
          <input id="user-name" name="user-name" type="text" class="validate">
          <label for="user-name">First Name</label>
          <span class="error-message red-text" id="error-user-name"></span>  <!-- Error span -->
        </div>
        <div class="input-field col s6">
          <i class="material-icons prefix">cake</i>
          <input id="user-birthdate" name="user-birthdate" type="date" class="validate">
          <label for="user-birthdate">Дата народження</label>
          <span class="error-message red-text" id="error-user-birthdate"></span> <!-- Error span -->
        </div>
      </div>

      <div class="row">
        <div class="input-field col s6">
          <i class="material-icons prefix">alternate_email</i>
          <input id="user-email" name="user-email" type="text" class="validate">
          <label for="user-email">Email</label>
          <span class="error-message red-text" id="error-user-email"></span>  <!-- Error span -->
        </div>
        <div class="file-field input-field col s6">
          <div class="btn purple">
            <i class="material-icons">account_circle</i>
            <input type="file" name="user-avatar">
          </div>
          <div class="file-path-wrapper">
            <input class="file-path validate" type="text">
            <span class="error-message red-text" id="error-user-avatar"></span>
          </div>
        </div>
      </div>

      <div class="row">
        <div class="input-field col s6">
          <i class="material-icons prefix">lock</i>
          <input id="user-password" name="user-password" type="password" class="validate">
          <label for="user-password">Password</label>
          <span class="error-message red-text" id="error-user-password"></span>  <!-- Error span -->
        </div>
        <div class="input-field col s6">
          <i class="material-icons prefix">lock_open</i>
          <input id="user-repeat" name="user-repeat" type="password" class="validate">
          <label for="user-repeat">Repeat</label>
          <span class="error-message red-text" id="error-user-repeat"></span>  <!-- Error span -->
        </div>
      </div>

      <div class="row right">
        <button class="btn waves-effect purple darken-2" type="submit">Register
          <i class="material-icons right">send</i>
        </button>
      </div>
      <div style="height: 40px">
      </div>
    </form>
  </div>
