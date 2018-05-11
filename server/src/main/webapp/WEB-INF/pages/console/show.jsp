<script type="text/javascript">
	$(document).ready(function() {
		$("#addnew").click(function() {
			window.location.href = rootPath + "/config/addnew";
		});
	});
</script>
<%@ page pageEncoding="UTF-8"%>
<div class="config_main" style="margin-top:15%;">
	<form action="${rootPath}/config/add" method="post">
		<div class="mdl-grid lokou_content mdl-shadow--2dp mdl-shadow--2dp">
			<div class=" mdl-color--white mdl-cell mdl-cell--5-col">
				<div class="mdl-textfield mdl-js-textfield lokou_input">
					<!--2.声明组件的input元素-->
					<input type="text" class="mdl-textfield__input" name="groupId"
						value="${config.groupId}" />
					<!--3.声明组件的label元素-->
					<label class="mdl-textfield__label">配置组ID:</label>
					<!--4.声明组件的error元素-->
					<span class="mdl-textfield__error">输入错误</span>
				</div>
			</div>
			<div class="mdl-cell mdl-cell--1-col"></div>
			<div class=" mdl-color--white mdl-cell mdl-cell--5-col">
				<div class="mdl-textfield mdl-js-textfield lokou_input">
					<!--2.声明组件的input元素-->
					<input type="text" class="mdl-textfield__input" name="configKey"
						value="${config.configKey}" />
					<!--3.声明组件的label元素-->
					<label class="mdl-textfield__label">配置的键名:</label>
					<!--4.声明组件的error元素-->
					<span class="mdl-textfield__error">输入错误</span>
				</div>
			</div>
			<div class="mdl-cell mdl-cell--1-col">
				<div
					class="mdl-textfield mdl-js-textfield mdl-textfield--expandable">
					<label class="mdl-button mdl-js-button mdl-button--icon mdl-js-ripple-effect"
						for="search"> <i class="material-icons">search</i>
					</label>
				</div>
			</div>
		</div>
	</form>
</div>
