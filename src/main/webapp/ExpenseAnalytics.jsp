<%@page import="java.util.Map"%>
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
double monthExpense = (Double) request.getAttribute("monthExpense");
long totalRecords = (Long) request.getAttribute("totalRecords");

double averageExpense = (Double) request.getAttribute("averageExpense");
double highestExpense = (Double) request.getAttribute("highestExpense");

double monthlyBudget = (Double) request.getAttribute("monthlyBudget");
double remainingBudget = (Double) request.getAttribute("remainingBudget");

// Calculate budget usage percentage
double budgetPercentage = 0;

if (monthlyBudget > 0) {
	budgetPercentage = (monthExpense / monthlyBudget) * 100;
}

// Prevent percentage from going below 0
if (budgetPercentage < 0) {
	budgetPercentage = 0;
}

Map<String, Double> categoryExpenses = (Map<String, Double>) request.getAttribute("categoryExpenses");

Map<String, Double> monthlyExpenses = (Map<String, Double>) request.getAttribute("monthlyExpenses");
%>

<!DOCTYPE html>
<html lang="en">

<head>

<meta charset="UTF-8">

<meta name="viewport" content="width=device-width, initial-scale=1.0">

<title>FinTrack</title>


<%@include file="common/cssLinks.jsp"%>

</head>

<body>

	<%@include file="common/navbar.jsp"%>


	<div class="container mt-4">

		<!-- Page Header -->

		<div class="card shadow mb-4" data-aos="fade-right"
			data-aos-duration="2000">

			<div class="card-body">

				<h2 class="fw-bold">
					<i class="fa-solid fa-chart-pie text-primary"></i> Expense
					Analytics
				</h2>

				<p class="text-muted mb-0">Analyze your spending patterns and
					understand where your money goes.</p>

			</div>

		</div>


		<!-- Analytics Summary Cards -->

		<div class="row mb-4" data-aos="fade-left" data-aos-duration="2000">

			<!-- Total Expense -->

			<div class="col-md-3 mb-3">

				<div class="card dashboard-card shadow h-100">

					<div
						class="card-body d-flex justify-content-between align-items-center">

						<div>

							<div class="dashboard-title">Total Expense</div>

							<div class="dashboard-value text-primary">
								₹
								<%=String.format("%.2f", totalExpense)%>
							</div>

						</div>

						<div class="dashboard-icon bg1">

							<i class="fa-solid fa-wallet"></i>

						</div>

					</div>

				</div>

			</div>


			<!-- This Month -->

			<div class="col-md-3 mb-3">

				<div class="card dashboard-card shadow h-100">

					<div
						class="card-body d-flex justify-content-between align-items-center">

						<div>

							<div class="dashboard-title">This Month</div>

							<div class="dashboard-value text-success">
								₹
								<%=String.format("%.2f", monthExpense)%>
							</div>

						</div>

						<div class="dashboard-icon bg2">

							<i class="fa-solid fa-calendar"></i>

						</div>

					</div>

				</div>

			</div>


			<!-- Average Expense -->

			<div class="col-md-3 mb-3">

				<div class="card dashboard-card shadow h-100">

					<div
						class="card-body d-flex justify-content-between align-items-center">

						<div>

							<div class="dashboard-title">Average Expense</div>

							<div class="dashboard-value text-warning">
								₹
								<%=String.format("%.2f", averageExpense)%>
							</div>

						</div>

						<div class="dashboard-icon bg3">

							<i class="fa-solid fa-calculator"></i>

						</div>

					</div>

				</div>

			</div>


			<!-- Highest Expense -->

			<div class="col-md-3 mb-3">

				<div class="card dashboard-card shadow h-100">

					<div
						class="card-body d-flex justify-content-between align-items-center">

						<div>

							<div class="dashboard-title">Highest Expense</div>

							<div class="dashboard-value text-danger">
								₹
								<%=String.format("%.2f", highestExpense)%>
							</div>

						</div>

						<div class="dashboard-icon bg4">

							<i class="fa-solid fa-arrow-up"></i>

						</div>

					</div>

				</div>

			</div>

		</div>


		<!-- Budget Analysis -->

		<div class="card shadow mb-4">

			<div class="card-header bg-white">

				<h4 class="mb-0">
					<i class="fa-solid fa-piggy-bank text-primary"></i> Monthly Budget
					Analysis
				</h4>

				<p class="text-muted mb-0">Track your spending against your
					monthly budget.</p>

			</div>


			<div class="card-body">

				<div class="row">

					<!-- Monthly Budget -->

					<div class="col-md-4 mb-3">

						<div class="border rounded p-3 h-100">

							<div class="text-muted">Monthly Budget</div>

							<h3 class="text-primary fw-bold mt-2">
								₹
								<%=String.format("%.2f", monthlyBudget)%>
							</h3>

						</div>

					</div>


					<!-- Spent -->

					<div class="col-md-4 mb-3">

						<div class="border rounded p-3 h-100">

							<div class="text-muted">Spent This Month</div>

							<h3 class="text-warning fw-bold mt-2">
								₹
								<%=String.format("%.2f", monthExpense)%>
							</h3>

						</div>

					</div>


					<!-- Remaining -->

					<div class="col-md-4 mb-3">

						<div class="border rounded p-3 h-100">

							<div class="text-muted">Remaining Budget</div>

							<h3 class="text-success fw-bold mt-2">
								₹
								<%=String.format("%.2f", remainingBudget)%>
							</h3>

						</div>

					</div>

				</div>


				<!-- Progress -->

				<div class="mt-3">

					<div class="d-flex justify-content-between mb-2">

						<span class="fw-bold"> Budget Usage </span> <span class="fw-bold">
							<%=String.format("%.1f", budgetPercentage)%>%
						</span>

					</div>


					<div class="progress" style="height: 25px;">

						<div class="progress-bar" role="progressbar"
							style="width: <%=Math.min(budgetPercentage, 100)%>%">

							<%=String.format("%.1f", budgetPercentage)%>%

						</div>

					</div>

				</div>


				<!-- Budget Status -->

				<div class="mt-3">

					<%
					if (monthlyBudget == 0) {
					%>

					<div class="alert alert-info mb-0">

						<i class="fa-solid fa-circle-info"></i> No monthly budget has been
						set yet. <a href="ManageBudget" class="alert-link"> Set your
							budget </a>

					</div>

					<%
					} else if (monthExpense > monthlyBudget) {
					%>

					<div class="alert alert-danger mb-0">

						<i class="fa-solid fa-triangle-exclamation"></i> You have exceeded
						your monthly budget by <strong> ₹ <%=String.format("%.2f", Math.abs(remainingBudget))%>
						</strong>.

					</div>

					<%
					} else if (budgetPercentage >= 80) {
					%>

					<div class="alert alert-warning mb-0">

						<i class="fa-solid fa-triangle-exclamation"></i> You have used <strong>
							<%=String.format("%.1f", budgetPercentage)%>%
						</strong> of your monthly budget. Spend carefully.

					</div>

					<%
					} else {
					%>

					<div class="alert alert-success mb-0">

						<i class="fa-solid fa-circle-check"></i> You are within your
						monthly budget. <strong> ₹ <%=String.format("%.2f", remainingBudget)%>
						</strong> remaining.

					</div>

					<%
					}
					%>

				</div>

			</div>

		</div>


		<!-- Charts -->

		<div class="row">

			<!-- Category Wise Chart -->

			<div class="col-md-6 mb-4" data-aos="zoom-in-up"
				data-aos-duration="2000">

				<div class="card shadow h-100">

					<div class="card-header bg-white">

						<h4 class="mb-0">

							<i class="fa-solid fa-chart-pie text-primary"></i> Category-wise
							Expense

						</h4>

						<p class="text-muted mb-0">See how your expenses are
							distributed across categories.</p>

					</div>

					<div class="card-body">

						<div style="height: 350px;">

							<canvas id="categoryChart"></canvas>

						</div>

					</div>

				</div>

			</div>


			<!-- Monthly Chart -->

			<div class="col-md-6 mb-4" data-aos="zoom-in-up"
				data-aos-duration="2000">

				<div class="card shadow h-100">

					<div class="card-header bg-white">

						<h4 class="mb-0">

							<i class="fa-solid fa-chart-column text-success"></i> Monthly
							Expense

						</h4>

						<p class="text-muted mb-0">Your spending trend for the last
							six months.</p>

					</div>

					<div class="card-body">

						<div style="height: 350px;">

							<canvas id="monthlyChart"></canvas>

						</div>

					</div>

				</div>

			</div>

		</div>

	</div>


	<!-- Chart.js -->

	<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>


	<script>

AOS.init();

	// ================= Category Chart =================

	const categoryLabels = [
		<%if (categoryExpenses != null) {

	for (String category : categoryExpenses.keySet()) {%>

		"<%=category%>",

		<%}
}%>
	];


	const categoryData = [
		<%if (categoryExpenses != null) {

	for (Double amount : categoryExpenses.values()) {%>

		<%=amount%>,

		<%}
}%>
	];


	new Chart(document.getElementById('categoryChart'), {

		type: 'doughnut',

		data: {

			labels: categoryLabels,

			datasets: [{

				data: categoryData,

				borderWidth: 2

			}]

		},

		options: {

			responsive: true,

			maintainAspectRatio: false,

			plugins: {

				legend: {

					position: 'bottom'

				}

			}

		}

	});


	// ================= Monthly Chart =================

	const monthlyLabels = [

		<%if (monthlyExpenses != null) {

	for (String month : monthlyExpenses.keySet()) {%>

		"<%=month%>",

		<%}
}%>

	];


	const monthlyData = [

		<%if (monthlyExpenses != null) {

	for (Double amount : monthlyExpenses.values()) {%>

		<%=amount%>,

		<%}
}%>

	];


	new Chart(document.getElementById('monthlyChart'), {

		type: 'bar',

		data: {

			labels: monthlyLabels,

			datasets: [{

				label: 'Monthly Expense (₹)',

				data: monthlyData,

				borderWidth: 1

			}]

		},

		options: {

			responsive: true,

			maintainAspectRatio: false,

			scales: {

				y: {

					beginAtZero: true

				}

			},

			plugins: {

				legend: {

					display: false

				}

			}

		}

	});

</script>

</body>

</html>
