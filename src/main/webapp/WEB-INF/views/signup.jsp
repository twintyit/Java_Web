
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<h1>Register user</h1>
  <div class="row">
    <form class="card-panel grey lighten-5"
          enctype="multipart/form-data"
          method="post"
          accept-charset="UTF-8">

      <div class="row">
        <div class="input-field col s6">
          <i class="material-icons prefix">badge</i>
          <input id="user-name" name="user-name" type="text" class="validate">
          <label for="user-name">First Name</label>
        </div>
        <div class="input-field col s6">
          <i class="material-icons prefix">phone</i>
          <input id="user-phone" name="user-phone" type="tel" class="validate">
          <label for="user-phone">Telephone</label>
        </div>
      </div>

      <div class="row">
        <div class="input-field col s6">
          <i class="material-icons prefix">alternate_email</i>
          <input id="user-email" name="user-email" type="text" class="validate">
          <label for="user-email">Email</label>
        </div>
        <div class="file-field input-field col s6">
          <div class="btn purple">
            <i class="material-icons">account_circle</i>
            <input type="file" name="user-avatar">
          </div>
          <div class="file-path-wrapper">
            <input class="file-path validate" type="text">
          </div>
        </div>
      </div>

      <div class="row">
        <div class="input-field col s6">
          <i class="material-icons prefix">lock</i>
          <input id="user-password" name="user-password" type="password" class="validate">
          <label for="user-password">Password</label>
        </div>
        <div class="input-field col s6">
          <i class="material-icons prefix">lock_open</i>
          <input id="user-repeat" name="user-repeat" type="password" class="validate">
          <label for="user-repeat">Repeat</label>
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
