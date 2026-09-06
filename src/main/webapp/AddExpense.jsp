<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>


<%@page import="com.shikhar.entity.User"%>
<%@page import="com.shikhar.entity.Category"%>
<%@page import="com.shikhar.HbUtility"%>

<%@page import="org.hibernate.Session"%>

<%@page import="java.util.List"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>FinTrack</title>

<%@include file="common/cssLinks.jsp"%>

<style>
body {
	background-color: #f8f9fa;
}

.card {
	border: none;
	border-radius: 15px;
}

.form-control {
	border-radius: 10px;
}

.btn-primary {
	border-radius: 10px;
	font-weight: 600;
}

.btn-primary:hover {
	transform: translateY(-2px);
	transition: 0.3s;
}

.card-title {
	font-weight: 600;
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

	<%
	User user = (User) session.getAttribute("User");

	if (user == null) {
		response.sendRedirect("signin.jsp");
		return;
	}

	Session hbSession = HbUtility.sessionFactory.openSession();

	String hql = "FROM Category WHERE user.email=:email ORDER BY name";

	List<Category> categories = hbSession.createQuery(hql, Category.class).setParameter("email", user.getEmail())
			.getResultList();

	hbSession.close();
	%>

	<div class="container py-4" data-aos="zoom-in-up"
		data-aos-duration="2000">

		<div class="row justify-content-center">

			<div class="col-lg-5 col-md-7">

				<div class="card shadow-lg">

					<div class="card-body p-4">

						<h3 class="text-center mb-4">
							<i class="fa-solid fa-wallet text-primary me-2"></i> Add Expense
						</h3>

						<form action="AddExpense" method="post">

							<!-- Category -->
							<div class="mb-3">

								<label class="form-label"> <i
									class="fa-solid fa-layer-group me-2 text-primary"></i> Category
								</label> <select class="form-select" name="categoryId" required>

									<option value="">-- Select Category --</option>

									<%
									for (Category category : categories) {
									%>

									<option value="<%=category.getId()%>">
										<%=category.getName()%>
									</option>

									<%
									}
									%>

								</select>

							</div>

							<!-- Title -->
							<div class="mb-3">
								<label class="form-label"> <i
									class="fa-solid fa-pen me-2 text-primary"></i>Title
								</label> <input type="text" class="form-control" name="title"
									placeholder="Enter expense title" required>
							</div>

							<!-- Date -->
							<div class="mb-3">
								<label class="form-label"> <i
									class="fa-solid fa-calendar-days me-2 text-primary"></i>Date
								</label> <input type="date" class="form-control" name="date" required>
							</div>

							<!-- Time -->
							<div class="mb-3">
								<label class="form-label"> <i
									class="fa-solid fa-clock me-2 text-primary"></i>Time
								</label> <input type="time" class="form-control" name="time" required>
							</div>

							<!-- Description -->
							<div class="mb-3">
								<label class="form-label"> <i
									class="fa-solid fa-file-lines me-2 text-primary"></i>Description
								</label>

								<textarea class="form-control" rows="3" name="description"
									placeholder="Enter description" required></textarea>
							</div>

							<!-- Price -->
							<div class="mb-4">
								<label class="form-label"> <i
									class="fa-solid fa-indian-rupee-sign me-2 text-primary"></i>Price
								</label> <input type="number" class="form-control" name="price"
									placeholder="Enter amount" required>
							</div>

							<!-- Button -->
							<div class="d-grid">
								<button type="submit" class="btn btn-primary">
									<i class="fa-solid fa-plus me-2"></i> Add Expense
								</button>
							</div>

						</form>

					</div>

				</div>

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