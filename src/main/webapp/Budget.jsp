<%@page import="java.time.LocalDate"%>
<%@page import="java.util.List"%>
<%@page import="com.shikhar.entity.Budget"%>
<%@page import="com.shikhar.entity.User"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%
User user = (User) session.getAttribute("User");
Budget editBudget = (Budget) request.getAttribute("editBudget");

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

.budget-card {
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


		<!-- Budget Form -->

		<div class="card budget-card shadow">


			<div class="card-header bg-white">

				<h3>

					<i class="fa-solid fa-money-bill-wave text-primary"></i> Budget
					Management

				</h3>


				<p class="text-muted mb-0">Create and manage your monthly
					budgets.</p>


			</div>



			<div class="card-body">


				<form
					action="<%=(editBudget == null) ? "AddBudget" : "UpdateBudget"%>"
					method="post">



					<%
					if (editBudget != null) {
					%>

					<input type="hidden" name="id" value="<%=editBudget.getId()%>">


					<%
					}
					%>



					<div class="row">



						<!-- Month -->

						<div class="col-md-3">

							<label class="form-label"> Month </label> <select name="month"
								class="form-select" required>


								<option value="">Select Month</option>


								<%
								for (int i = 1; i <= 12; i++) {
								%>


								<option value="<%=i%>"
									<%=editBudget != null && editBudget.getMonth() == i ? "selected" : ""%>>

									<%=java.time.Month.of(i).getDisplayName(java.time.format.TextStyle.FULL, java.util.Locale.ENGLISH)%>


								</option>


								<%
								}
								%>


							</select>


						</div>





						<!-- Year -->


						<div class="col-md-2">


							<label class="form-label"> Year </label> <input type="number"
								name="year" class="form-control"
								value="<%=editBudget != null ? editBudget.getYear() : LocalDate.now().getYear()%>"
								required>


						</div>





						<!-- Amount -->


						<div class="col-md-5">


							<label class="form-label"> Budget Amount </label> <input
								type="number" step="0.01" min="1" name="amount"
								class="form-control"
								value="<%=editBudget != null ? editBudget.getAmount() : ""%>"
								placeholder="Enter monthly budget" required>


						</div>






						<!-- Button -->


						<div class="col-md-2 d-flex flex-column justify-content-end">



							<button
								class="btn btn-<%=editBudget == null ? "primary" : "warning"%> w-100">


								<i class="fa-solid <%=editBudget == null ? "fa-plus" : "fa-pen"%>"></i>


								<%=editBudget == null ? "Add" : "Update"%>


							</button>




							<%
							if (editBudget != null) {
							%>


							<a href="ManageBudget" class="btn btn-secondary w-100 mt-2">

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





		<!-- Budget List -->


		<div class="card shadow">


			<div class="card-header bg-white">


				<h4>

					<i class="fa-solid fa-list text-primary"></i> Your Monthly Budgets

				</h4>


			</div>




			<div class="card-body">



				<table class="table table-bordered table-hover">


					<thead>


						<tr>

							<th width="10%">S.No</th>

							<th>Month</th>

							<th>Year</th>

							<th>Amount</th>

							<th width="20%">Action</th>


						</tr>


					</thead>




					<tbody>



						<%
						List<Budget> budgets = (List<Budget>) request.getAttribute("budgetList");

						if (budgets == null || budgets.isEmpty()) {
						%>


						<tr>

							<td colspan="5" class="text-center text-muted">No Budget
								Found</td>

						</tr>



						<%
						}

						else {

						int i = 1;

						for (Budget budget : budgets) {
						%>



						<tr>


							<td><%=i++%></td>


							<td><%=budget.getMonthName()%></td>



							<td><%=budget.getYear()%></td>



							<td>₹ <%=budget.getAmount()%>

							</td>



							<td><a href="ManageBudget?id=<%=budget.getId()%>"
								class="btn btn-sm btn-warning"> <i class="fa-solid fa-pen"></i>

							</a> <a href="DeleteBudget?id=<%=budget.getId()%>"
								class="btn btn-sm btn-danger"
								onclick="return confirm('Are you sure you want to delete this budget?');">


									<i class="fa-solid fa-trash"></i>


							</a></td>


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