<%@page import="java.util.List"%>
<%@page import="com.shikhar.entity.Expense"%>
<%@page import="com.shikhar.entity.User"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%
User user = (User) session.getAttribute("User");

if (user == null) {
	response.sendRedirect("signin.jsp");
	return;
}

double totalExpense = (Double) request.getAttribute("totalExpense");
double todayExpense = (Double) request.getAttribute("todayExpense");
double monthExpense = (Double) request.getAttribute("monthExpense");
long totalRecords = (Long) request.getAttribute("totalRecords");

double monthlyBudget = (Double) request.getAttribute("monthlyBudget");
double remainingBudget = (Double) request.getAttribute("remainingBudget");

List<Expense> expenses = (List<Expense>) request.getAttribute("recentExpenses");
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

.card {
	border: none;
	border-radius: 15px;
	margin-top: -5px;
}

.table {
	margin-bottom: 0;
}

.table thead th {
	background: #0d6efd;
	color: white;
	position: sticky;
	top: 0;
	z-index: 100;
	vertical-align: middle;
}

/* Scrollable Container */
.expense-container {
	height: calc(100vh - 320px);
	overflow-y: auto;
	border: 1px solid #dee2e6;
	border-radius: 0 0 15px 15px;
	scrollbar-width: none;
	-ms-overflow-style: none;
}

.expense-container::-webkit-scrollbar {
	display: none;
}

.dashboard-card {
	border: none;
	border-radius: 18px;
	transition: .3s;
}

.dashboard-card:hover {
	transform: translateY(-5px);
	box-shadow: 0 10px 25px rgba(0, 0, 0, .12);
}

.dashboard-icon {
	width: 60px;
	height: 60px;
	border-radius: 50%;
	display: flex;
	justify-content: center;
	align-items: center;
	font-size: 24px;
	color: white;
}

.bg1 {
	background: #0d6efd;
}

.bg2 {
	background: #198754;
}

.bg3 {
	background: #ffc107;
	color: black;
}

.bg4 {
	background: #dc3545;
}

.dashboard-value {
	font-size: 28px;
	font-weight: bold;
}

.dashboard-title {
	color: #6c757d;
	font-size: 15px;
}
</style>

</head>

<body>

	<%@include file="common/navbar.jsp"%>

	<div class="container mt-4">

		<!-- Welcome Card -->

		<div class="card shadow mb-4" data-aos="zoom-in"
			data-aos-duration="2000">

			<div class="card-body">

				<h3>

					Welcome <span class="text-primary"> <%=user.getName()%>

					</span>

				</h3>

			</div>

		</div>

		<!-- Cards -->
		<div class="row mb-4">

			<div class="col-md-3 mb-3" data-aos="fade-left"
				data-aos-duration="2000">

				<a href="ViewExpense" class="text-decoration-none text-dark">

					<div class="card dashboard-card shadow">

						<div
							class="card-body d-flex justify-content-between align-items-center">

							<div>

								<div class="dashboard-title">Total Expense</div>

								<div class="dashboard-value text-primary">
									₹
									<%=totalExpense%>
								</div>

							</div>

							<div class="dashboard-icon bg1">
								<i class="fa-solid fa-wallet"></i>
							</div>

						</div>

					</div>

				</a>

			</div>

			<div class="col-md-3 mb-3" data-aos="fade-left"
				data-aos-duration="2000">
				<a href="ViewExpense?filter=today"
					class="text-decoration-none text-dark">
					<div class="card dashboard-card shadow">

						<div
							class="card-body d-flex justify-content-between align-items-center">

							<div>

								<div class="dashboard-title">Today's Expense</div>

								<div class="dashboard-value text-success">
									₹
									<%=todayExpense%>
								</div>

							</div>

							<div class="dashboard-icon bg2">
								<i class="fa-solid fa-calendar-day"></i>
							</div>

						</div>

					</div>

				</a>

			</div>

			<!-- Monthly Budget -->
			<div class="col-md-3 mb-3" data-aos="fade-left"
				data-aos-duration="2000">

				<a href="ManageBudget" class="text-decoration-none text-dark">
					<div class="card dashboard-card shadow">

						<div
							class="card-body d-flex justify-content-between align-items-center">

							<div>

								<div class="dashboard-title">Monthly Budget</div>

								<div class="dashboard-value text-success">
									₹
									<%=monthlyBudget%>
								</div>

							</div>

							<div class="dashboard-icon bg2">
								<i class="fa-solid fa-money-bill-wave"></i>
							</div>

						</div>

					</div>
				</a>
			</div>

			<!-- Total Records -->
			<div class="col-md-3 mb-3" data-aos="fade-left"
				data-aos-duration="2000">
				<a href="ViewExpense" class="text-decoration-none text-dark">
					<div class="card dashboard-card shadow">

						<div
							class="card-body d-flex justify-content-between align-items-center">

							<div>

								<div class="dashboard-title">Total Records</div>

								<div class="dashboard-value text-danger">
									<%=totalRecords%>
								</div>

							</div>

							<div class="dashboard-icon bg4">
								<i class="fa-solid fa-file-lines"></i>
							</div>

						</div>

					</div>

				</a>

			</div>

			<!-- Budget Cards -->

			<div class="row mb-4">



				<!-- Spent This Month -->
				<div class="col-md-6 mb-3" data-aos="fade-right"
					data-aos-duration="2000">
					<a href="ViewExpense?filter=month"
						class="text-decoration-none text-dark">
						<div class="card dashboard-card shadow">

							<div
								class="card-body d-flex justify-content-between align-items-center">

								<div>

									<div class="dashboard-title">Spent This Month</div>

									<div class="dashboard-value text-warning">
										₹
										<%=monthExpense%>
									</div>

								</div>

								<div class="dashboard-icon bg3">
									<i class="fa-solid fa-chart-line"></i>
								</div>

							</div>

						</div>
					</a>
				</div>

				<!-- Remaining Budget -->
				<div class="col-md-6 mb-3" data-aos="fade-right"
					data-aos-duration="2000">
					<a href="ViewExpense" class="text-decoration-none text-dark">
						<div class="card dashboard-card shadow">

							<div
								class="card-body d-flex justify-content-between align-items-center">

								<div>

									<div class="dashboard-title">Remaining Budget</div>

									<div class="dashboard-value text-primary">
										₹
										<%=remainingBudget%>
									</div>

								</div>

								<div class="dashboard-icon bg1">
									<i class="fa-solid fa-piggy-bank"></i>
								</div>

							</div>

						</div>
					</a>
				</div>

			</div>


			<!-- Recent Expenses -->

			<div class="card shadow" data-aos="zoom-in-up"
				data-aos-duration="2000">

				<div class="card-header bg-white">

					<h4>
						<i class="fa-solid fa-clock-rotate-left text-primary"></i> Recent
						Expenses
					</h4>

					<p class="text-muted">Here are your recent expenses from the
						last 7 days.</p>

				</div>

				<div class="card-body p-2">

					<!-- Scrollable Table -->
					<div class="expense-container">

						<table class="table table-hover table-bordered mb-0">

							<thead>

								<tr>

									<th>Date</th>
									<th>Time</th>
									<th>Category</th>
									<th>Title</th>
									<th>Description</th>
									<th>Amount</th>

								</tr>

							</thead>

							<tbody>

								<%
								if (expenses.isEmpty()) {
								%>

								<tr>

									<td colspan="6" class="text-center text-danger py-4">

										<div class="alert alert-danger text-center">No Expense
											Found! Go to View-Expanse to see all Expanses</div>

									</td>

								</tr>

								<%
								} else {

								for (Expense e : expenses) {
								%>

								<tr>

									<td><%=e.getDate()%></td>

									<td><%=e.getTime()%></td>

									<td><span class="badge bg-primary"> <%=e.getCategory().getName()%>

									</span></td>

									<td><%=e.getTitle()%></td>

									<td><%=e.getDescription()%></td>

									<td class="fw-bold text-primary">₹ <%=e.getPrice()%>

									</td>

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
			</script>
</body>
</html>