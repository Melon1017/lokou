 <%@ page pageEncoding="UTF-8"%>
 <div class="mdl-grid lokou_content mdl-shadow--2dp">
 	<div class=" mdl-color--white mdl-cell mdl-cell--5-col lokou_cell">
		<table class="mdl-data-table mdl-js-data-table lokou_table">
			<thead>
				<tr>
					<th class="mdl-data-table__cell--non-numeric">配置组Id</th>
					<th class="mdl-data-table__cell--non-numeric">配置健Key</th>
					<th>查看内容</th>
				</tr>
			</thead>
			<tbody>
			
			<c:forEach var="config"  items="${configs}">
				<tr>
					<td class="mdl-data-table__cell--non-numeric">${config.groupId}</td>
					<td class="mdl-data-table__cell--non-numeric">${config.configKey}</td>
					<td><a href="${rootPath}/config/detail?groupId=${config.groupId}&configKey=${config.configKey}">查看明细</a></td>
				</tr>
             </c:forEach>
			</tbody>
		</table>
		</div>
</div>