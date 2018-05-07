<html>
<head>
  <titel> student  </title>
<link rel="stylesheet" href="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/css/bootstrap.min.css">  
	<script src="http://cdn.static.runoob.com/libs/jquery/2.1.1/jquery.min.js"></script>
	<script src="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>
<body>
    学生信息:<br>
  学号:${student.id}&nbsp;&nbsp;&nbsp;&nbsp;
  姓名:${student.name}&nbsp;&nbsp;&nbsp;&nbsp;
  年龄:${student.age}&nbsp;&nbsp;&nbsp;&nbsp;
  家庭住址:${student.address}<br>
  
  <table class="table table-bordered">
	<caption>边框表格布局</caption>
	<thead>
		<tr>
		    <th>序号</th>
			<th>学号</th>
			<th>姓名</th>
			<th>年龄</th>
			<th>地址</th>
		</tr>
	</thead>
	<tbody>
	<#list list as p>
	    <#if p_index % 2 == 0>
		<tr  class="danger">
		<#else>
		<tr class="success">
		</#if>
		     <td>${p_index}</td>
			<td>${p.id}</td>
			<td>${p.name}</td>
			<td>${p.age}</td>
			<td>${p.address}</td>
		</tr>
	</#list>
	
	</tbody>
</table>
      <br>
      当前日期: ${date?string('yyyy/MM/dd HH:mm:ss')}

val 的值${val!}
<br>
引用模板测试  <br>
<#include "hello.ftl">












</body>
</html>