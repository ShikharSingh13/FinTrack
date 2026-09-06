<%@page import="com.shikhar.entity.User"%>
<%@page import="java.util.List"%>
<%@page import="com.shikhar.entity.Category"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%
User user = (User) session.getAttribute("User");
Category editCategory = (Category) request.getAttribute("editCategory");

if (user == null) {
	response.sendRedirect("signin.jsp");
	return;
}
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>FinTrack</title>

<%@include file="common/cssLinks.jsp"%>

<style>
body {
	background: #f5f7fb;
}

.category-card {
	border: none;
	border-radius: 18px;
}

.table {
	margin-bottom: 0;
}

.table thead th {
	background: #0d6efd;
	color: white;
}
</style>

</head>
<body>

	<%@include file="common/navbar.jsp"%>

	<%
	String msg = (String) session.getAttribute("msg");
	String msgType = (String) session.getAttribute("msgType");

	if (msg != null) {
	%>

	<div class="d-flex justify-content-center mt-3">
		<div id="successAlert"
			class="alert alert-<%=msgType%> alert-dismissible fade show shadow text-center"
			role="alert" style="width: fit-content; min-width: 300px;">

			<%=msg%>

			<button class="btn-close" data-bs-dismiss="alert"></button>

		</div>
	</div>

	<%
	session.removeAttribute("msg");
	session.removeAttribute("msgType");
	}
	%>



	<div class="container mt-4">

		<div class="card category-card shadow">

			<div class="card-header bg-white">
				<h3>
					<i class="fa-solid fa-layer-group text-primary"></i> Category
					Management
				</h3>
				<p class="text-muted mb-0">Create and manage your expense
					categories.</p>
			</div>

			<div class="card-body">

				<form
					action="<%=(editCategory == null) ? "AddCategory" : "UpdateCategory"%>"
					method="post">

					<%
					if (editCategory != null) {
					%>

					<input type="hidden" name="id" value="<%=editCategory.getId()%>">

					<%
					}
					%>

					<div class="row">

						<div class="col-md-10">

							<label class="form-label">Category Name</label> <input
								type="text" name="categoryName" class="form-control"
								value="<%=editCategory != null ? editCategory.getName() : ""%>"
								placeholder="Enter category name" required>

						</div>

						<div class="col-md-2 d-flex flex-column justify-content-end">

							<button
								class="btn btn-<%=editCategory == null ? "primary" : "warning"%> w-100">

								<i
									class="fa-solid <%=editCategory == null ? "fa-plus" : "fa-pen"%>"></i>

								<%=editCategory == null ? "Add" : "Update"%>

							</button>

							<%
							if (editCategory != null) {
							%>

							<a href="ManageCategory" class="btn btn-secondary w-100 mt-2">

								Cancel </a>

							<%
							}
							%>

						</div>

					</div>

				</form>

			</div>

		</div>

		<br>

		<div class="card shadow">

			<div class="card-header bg-white">

				<h4>
					<i class="fa-solid fa-list text-primary"></i> Your Categories
				</h4>

			</div>

			<div class="card-body">

				<table class="table table-bordered table-hover">

					<thead>

						<tr>

							<th width="10%">S.No</th>
							<th>Category Name</th>
							<th>Used In</th>
							<th width="20%">Action</th>

						</tr>

					</thead>

					<tbody>

						<%
						List<Category> categories = (List<Category>) request.getAttribute("categories");

						if (categories == null || categories.isEmpty()) {
						%>

						<tr>
							<td colspan="3" class="text-center text-muted">No Categories
								Found</td>
						</tr>

						<%
						} else {

						int i = 1;

						for (Category category : categories) {
						%>

						<tr>

							<td><%=i++%></td>

							<td><%=category.getName()%></td>

							<td>
								<%
								Long count = (Long) request.getAttribute("count_" + category.getId());

								if (count == null) {
									count = 0L;
								}
								%> <%=count%> Expense<%=count != 1 ? "s" : ""%>

							</td>

							<td><a href="ManageCategory?id=<%=category.getId()%>"
								class="btn btn-sm btn-warning"> <i class="fa-solid fa-pen"></i>
							</a> <%
 if (count == 0) {
 %> <a href="DeleteCategory?id=<%=category.getId()%>"
								class="btn btn-sm btn-danger"
								onclick="return confirm('Are you sure you want to delete this category?');">

									<i class="fa-solid fa-trash"></i>

							</a> <%
 } else {
 %>

								<button class="btn btn-sm btn-secondary" disabled
									title="This category is being used by expenses">

									<i class="fa-solid fa-lock"></i>

								</button> <%
 }
 %></td>

						</tr>

						<%
						}
						}
						%>
					</tbody>

				</table>

			</div>

		</div>

	</div>

	<script>
		AOS.init();

		setTimeout(function() {
			let alert = document.getElementById("successAlert");
			if (alert) {
				let bsAlert = new bootstrap.Alert(alert);
				bsAlert.close();
			}
		}, 5000);
	</script>

</body>
</html>