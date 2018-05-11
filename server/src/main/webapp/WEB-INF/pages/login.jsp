<%@ page pageEncoding="UTF-8"%>
<%@ include file="/asset/asset.jsp"%>
<head>
<title>登录</title>
</head>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<body class="lokou_login_content">
	<table style="width: 100%; height: 100%;">
		<tr>
			<td style="text-align: center; vertical-align: middle;">
				<div class="mdl-grid"
					style="background-color: #ffffff; width: 350px; height: 290px; display: inline-block;">
					<div class=" mdl-color--white mdl-cell mdl-cell--12-col">

						<div class=" lokou_login_textarea mdl-textfield mdl-js-textfield">
							<!--2.声明组件的input元素-->
							<input type="text" id="name" class="mdl-textfield__input"
								name="name" value="" pattern="[\\dA-Za-z]{1,16}" />
							<!--3.声明组件的label元素-->
							<label class="mdl-textfield__label">用户名</label>
							<!--4.声明组件的error元素-->
							<span class="mdl-textfield__error">用户名必须为16字符以内的字母或数字组合</span>
						</div>
					</div>
					<div class=" mdl-color--white mdl-cell mdl-cell--12-col">
						<div class="lokou_login_textarea mdl-textfield mdl-js-textfield ">
							<!--2.声明组件的input元素-->
							<input type="password" id="password" class="mdl-textfield__input"
								name="password" value="" pattern="[\x00-\xff]{1,16}" />
							<!--3.声明组件的label元素-->
							<label class="mdl-textfield__label">密码</label>
							<!--4.声明组件的error元素-->
							<span class="mdl-textfield__error">密码必须是1-16字符的字母或数字或特殊字符号</span>
						</div>
					</div>
					<div class=" mdl-color--white mdl-cell mdl-cell--12-col">
						<label
							class="lokou_mdl-checkbox mdl-checkbox mdl-js-checkbox mdl-js-ripple-effect"
							for="remember_pwd"> <input type="checkbox"
							id="remember_pwd" class="mdl-checkbox__input" checked /> <span
							class="lokou_check_label">记住密码</span>
						</label>
					</div>
					<div class=" mdl-color--white mdl-cell mdl-cell--12-col">
						<span class="mdl-textfield__error error_msg">用户名或密码错误</span>
					</div>
					<div
						class="lokou_mdl-login_button mdl-color--white mdl-cell mdl-cell--12-col">
						<button
							class="mdl-button mdl-js-button mdl-button--raised mdl-js-ripple-effect mdl-button--colored lokou_cell"
							id="login_button">登录</button>
					</div>
				</div>
			</td>
		</tr>
	</table>
</body>
<script>
	$(document).ready(function() {
		$("#login_button").click(function() {
			var name = $("#name").val();
			var password = $("#password").val();
			password = hex_sha1(password);
			var submitStr = "name=" + name + "&password=" + password;
			var hasInvalid = $(".lokou_login_textarea").hasClass("is-invalid");
			if (!hasInvalid) {
				$.ajax({
					type : "get",
					url : "/lokou/loginAuth?" + submitStr,
					dataType : "json",
					success : function(data) {
						if (data.status == true) {
							window.location = "/lokou/console/portal";
						} else {
							$(".error_msg").text(data.msg);
							$(".error_msg").css("visibility", "visible");
						}
					},
					error : function() {
					}

				});
			}
		});
		$("input").focus(function() {
			$(".error_msg").html("");
			$(".error_msg").css("visibility", "hidden");

		})
	});
</script>
</html>
