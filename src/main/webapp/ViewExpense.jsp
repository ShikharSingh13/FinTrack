<%@page import="java.util.List"%>

<%@page import="com.shikhar.entity.Expense"%>
<%@page import="com.shikhar.entity.User"%>
<%@page import="com.shikhar.entity.Category"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%
User user = (User) session.getAttribute("User");

if (user == null) {
	response.sendRedirect("signin.jsp");
	return;
}

// Search servlet will send this list
List<Expense> expenses = (List<Expense>) request.getAttribute("expenses");

String selectedCategory = (String) request.getAttribute("selectedCategory");

String searchDate = (String) request.getAttribute("searchDate");

String pageTitle = (String) request.getAttribute("pageTitle");

if (pageTitle == null) {
	pageTitle = "All Expenses";
}

if (selectedCategory == null) {
	selectedCategory = "";
}

if (searchDate == null) {
	searchDate = "";
}
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>FinTrack</title>

<%@include file="common/cssLinks.jsp"%>

<style>
html, body {
	
	margin: 0;
	overflow: hidden;
}

body {
	background: #f4f6f9;
	box-sizing: border-box;
}

.search-card {
	position: sticky;
	top: 15px;
	z-index: 1000;
	border-radius: 15px;
}

.expense-container {
	border: 2px solid #dee2e6;
	border-radius: 12px;
	padding: 10px;
	margin-top: -5px;
	height: calc(100vh - 320px);
	overflow-y: auto;
	background: white;
}

.expense-card {
	border-radius: 15px;
	transition: .3s;
}

.expense-card:hover {
	transform: translateY(-3px);
	box-shadow: 0 8px 20px rgba(0, 0, 0, .15);
}

.amount {
	font-size: 24px;
	font-weight: bold;
	color: #0d6efd;
}

.title {
	font-size: 20px;
	font-weight: 600;
}

.description {
	color: #666;
}

.icon-circle {
	width: 60px;
	height: 60px;
	border-radius: 50%;
	background: #e8f0ff;
	display: flex;
	justify-content: center;
	align-items: center;
	font-size: 25px;
	color: #0d6efd;
}

.action-btn {
	width: 40px;
	height: 40px;
	border-radius: 50%;
}

.expense-container::-webkit-scrollbar {
	display: none;
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

	<!-- ViewExpense -->

	<div class="container mt-4">

		<!-- Search Card -->

		<div class="card shadow search-card mb-4" data-aos="zoom-in"
			data-aos-duration="2000">

			<div class="card-body">

				<h4 class="mb-1">
					<i class="fa-solid fa-wallet text-primary"></i>
					<%=pageTitle%>
				</h4>


				<form action="SearchExpense" method="post">

					<div class="row g-3">

						<div class="col-md-5">

							<%
							List<Category> categories = (List<Category>) request.getAttribute("categories");
							%>

							<select class="form-select" name="categoryId">

								<option value="">All Categories</option>

								<%
								for (Category c : categories) {
								%>

								<option value="<%=c.getId()%>"
									<%=String.valueOf(c.getId()).equals(selectedCategory) ? "selected" : ""%>>

									<%=c.getName()%>

								</option>

								<%
								}
								%>

							</select>

						</div>
						<div class="col-md-3">

							<input type="date" class="form-control" name="date"
								value="<%=searchDate%>">

						</div>

						<div class="col-md-2 d-grid">

							<button type="submit" class="btn btn-primary">

								<i class="fa-solid fa-magnifying-glass"></i> Search

							</button>

						</div>

						<div class="col-md-2 d-grid">

							<a href="ViewExpense" class="btn btn-outline-secondary"> <i
								class="fa-solid fa-rotate-left"></i> Reset

							</a>

						</div>

					</div>

				</form>

			</div>

		</div>

		<!-- PDF Export Card -->

		<div class="card shadow mb-4" data-aos="fade-up"
			data-aos-duration="1500">

			<div class="card-body">

				<div class="row align-items-center">

					<div class="col-md-5">

						<h5 class="mb-1">

							<i class="fa-solid fa-file-pdf text-danger me-2"></i> Monthly
							Expense Report

						</h5>

						<small class="text-muted"> Select a month and year to
							download your expense report. </small>

					</div>

					<div class="col-md-7">

						<form action="ExportPdf" method="get"
							class="row g-2 justify-content-end">

							<!-- Month -->

							<div class="col-md-4">

								<select name="month" class="form-select" required>

									<option value="">Select Month</option>

									<option value="1">January</option>
									<option value="2">February</option>
									<option value="3">March</option>
									<option value="4">April</option>
									<option value="5">May</option>
									<option value="6">June</option>
									<option value="7">July</option>
									<option value="8">August</option>
									<option value="9">September</option>
									<option value="10">October</option>
									<option value="11">November</option>
									<option value="12">December</option>

								</select>

							</div>

							<!-- Year -->

							<div class="col-md-3">

								<select name="year" class="form-select" required>

									<option value="">Year</option>

									<option value="2026">2026</option>
									<option value="2025">2025</option>
									<option value="2024">2024</option>

								</select>

							</div>

							<!-- Export Button -->

							<div class="col-md-5 d-grid">

								<button type="submit" class="btn btn-danger">

									<i class="fa-solid fa-file-pdf me-1"></i> Export PDF

								</button>

							</div>

						</form>

					</div>

				</div>

			</div>

		</div>


		<!-- Expense Cards -->

		<div class="expense-container py-4" data-aos="zoom-in-up"
			data-aos-duration="2000">

			<%
			if (expenses.isEmpty()) {
			%>

			<div class="alert alert-danger text-center">No Expense Found</div>

			<%
			} else {

			for (Expense e : expenses) {
			%>

			<div class="card shadow expense-card mb-3">

				<div class="card-body">

					<div class="row align-items-center">

						<div class="col-md-1 text-center">

							<div class="icon-circle">

								<i class="fa-solid fa-wallet"></i>

							</div>

						</div>

						<div class="col-md-7">

							<!-- Category -->
							<div class="mb-2">

								<span class="badge rounded-pill bg-primary fs-6"> <i
									class="fa-solid fa-layer-group me-1"></i> <%=e.getCategory().getName()%>

								</span>

							</div>

							<!-- Title -->
							<div class="title">

								<%=e.getTitle()%>

							</div>

							<!-- Description -->
							<div class="description mt-2">

								<%=e.getDescription()%>

							</div>

							<div class="mt-3 text-muted">

								<i class="fa-solid fa-calendar-days"></i>

								<%=e.getDate()%>

								&nbsp;&nbsp; <i class="fa-solid fa-clock"></i>

								<%=e.getTime()%>

							</div>

						</div>

						<div class="col-md-2 text-center">

							<div class="amount">

								₹
								<%=e.getPrice()%>

							</div>

						</div>

						<div class="col-md-2 text-end">

							<a href="EditExpense.jsp?id=<%=e.getId()%>"
								class="btn btn-warning action-btn"> <i
								class="fa-solid fa-pen"></i>

							</a> <a href="DeleteExpense?id=<%=e.getId()%>"
								class="btn btn-danger action-btn"
								onclick="return confirm('Delete this expense?')"> <i
								class="fa-solid fa-trash"></i>

							</a>

						</div>

					</div>

				</div>

			</div>

			<%
			}
			}
			%>

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