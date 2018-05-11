<%@ page pageEncoding="UTF-8"%>
<%@ include file="/asset/asset.jsp"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<body>
	<div
		class="mdl-layout mdl-js-layout mdl-layout--fixed-drawer mdl-layout--fixed-header ">
		<div class="mdl-layout__header mdl-color--teal-500">
			<div class="mdl-layout__header-row ">
				<span class="mdl-layout-title">控制台</span>
				<div class="mdl-layout-spacer"></div>
				<nav class="mdl-navigation">
					<button
						class="mdl-button mdl-js-button mdl-js-ripple-effect mdl-button--icon"
						id="hdrbtn">
						<i class="material-icons">more_vert</i>
					</button>
					<ul
						class="mdl-menu mdl-js-menu mdl-js-ripple-effect mdl-menu--bottom-right"
						for="hdrbtn">
						<li class="mdl-menu__item">About</li>
						<li class="mdl-menu__item">Contact</li>
						<li class="mdl-menu__item">Legal information</li>
					</ul>
				</nav>
			</div>
		</div>
		<div class="mdl-layout__drawer mdl-layout mdl-layout--fixed-drawer demo-drawer mdl-layout__drawer mdl-color--teal-800 mdl-color-text--grey-50">
			  <header class="lokou-drawer-header">
          <i class="material-icons" style="font-size:50px;">&#xE853;</i>
          <div class="lokou-avatar-dropdown">
            <span>${userName}</span>
            <div class="mdl-layout-spacer"></div>
            <button id="accbtn" class="mdl-button mdl-js-button mdl-js-ripple-effect mdl-button--icon">
              <i class="material-icons" role="presentation">arrow_drop_down</i>
              <span class="visuallyhidden">Accounts</span>
            </button>
            <ul class="mdl-menu mdl-menu--bottom-right mdl-js-menu mdl-js-ripple-effect" for="accbtn">
              <li class="mdl-menu__item">用户资料</li>
              <li class="mdl-menu__item">退出账号</li>
            </ul>
          </div>
        </header>
			<%@ include file="left.jsp"%>
		</div>
		<div class="mdl-layout__content mdl-color--grey-100">
			<jsp:include   page="${contentMain==null?'show.jsp' : contentMain}" flush="true"/>
		</div>
	</div>
</body>
</html>
