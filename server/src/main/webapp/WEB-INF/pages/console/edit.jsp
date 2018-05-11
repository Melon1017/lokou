<%@ page pageEncoding="UTF-8"%>
<div class="config_main">
	<form action="${rootPath}/config/add" method="post">
		<div class="mdl-grid lokou_content mdl-shadow--2dp">
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
			<div class=" mdl-color--white mdl-cell mdl-cell--2-col">
			</div>
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
			<div class="mdl-color--white mdl-cell mdl-cell--12-col">
				<div class="mdl-textfield mdl-js-textfield lokou_textarea">
					<!--使用rows属性声明行数-->
					<textarea class="mdl-textfield__input" rows="20" cols="100"
						name="configValue">${config.configValue}</textarea>
					<label class="mdl-textfield__label">配置内容</label> <span
						class="mdl-textfield__error">输入错误</span>
				</div>
			</div>
			<div class="mdl-cell mdl-cell--12-col lokou_cell">
				<button
					class="mdl-button mdl-js-button mdl-button--raised mdl-js-ripple-effect mdl-button--colored  lokou_cell">保存</button>
			</div>
		</div>
	</form>
</div>
