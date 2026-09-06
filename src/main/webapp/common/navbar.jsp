<%@page import="com.shikhar.entity.User"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
User u = (User) session.getAttribute("User");
%>
<nav
	class="navbar navbar-expand-lg navbar-dark bg-primary sticky-top shadow">
	<div class="container-fluid">
		<a class="navbar-brand" href="index.jsp"><i
			class="fa-solid fa-indian-rupee-sign"></i> FinTrack</a>
		<button class="navbar-toggler" type="button" data-bs-toggle="collapse"
			data-bs-target="#navbarSupportedContent"
			aria-controls="navbarSupportedContent" aria-expanded="false"
			aria-label="Toggle navigation">
			<span class="navbar-toggler-icon"></span>
		</button>
		<div class="collapse navbar-collapse" id="navbarSupportedContent">

			<ul class="navbar-nav me-auto mb-2 mb-lg-0">

				<%
				if (u != null) {
				%>

				<li class="nav-item"><a class="nav-link active"
					href="DashboardServlet"> <i class="fa-solid fa-gauge-high"></i>
						Dashboard
				</a></li>

				<!-- Expenses -->
				<li class="nav-item dropdown"><a
					class="nav-link dropdown-toggle active" href="#" role="button"
					data-bs-toggle="dropdown"> <i class="fa-solid fa-wallet"></i>
						Expenses

				</a>

					<ul class="dropdown-menu shadow">

						<li><a class="dropdown-item" href="AddExpense.jsp"> <i
								class="fa-solid fa-plus"></i> Add Expense
						</a></li>

						<li><a class="dropdown-item" href="ViewExpense"> <i
								class="fa-solid fa-receipt"></i> View Expenses
						</a></li>

						<li><a class="dropdown-item" href="ManageCategory"> <i
								class="fa-solid fa-layer-group"></i> Manage Categories
						</a></li>

						<li><a class="dropdown-item" href="ManageBudget"> <i
								class="fa-solid fa-money-bill-wave"></i> Manage Budget
						</a></li>

					</ul></li>

				<!-- Reports -->
				<li class="nav-item dropdown"><a
					class="nav-link dropdown-toggle active" href="#" role="button"
					data-bs-toggle="dropdown"> <i class="fa-solid fa-chart-column"></i>
						Reports

				</a>

					<ul class="dropdown-menu shadow">

						<li><a class="dropdown-item"
							href="DashboardServlet?view=analytics"> <i
								class="fa-solid fa-chart-pie"></i> Expense Analytics
						</a></li>

						

						<li><a class="dropdown-item" href="ViewExpense"> <i
								class="fa-solid fa-file-pdf"></i> Export PDF
						</a></li>

					</ul></li>

				<%
				} else {
				%>

				<li class="nav-item"><a class="nav-link active"
					href="index.jsp"> <i class="fa-solid fa-house"></i> Home
				</a></li>

				<%
				}
				%>

			</ul>


			<ul class="navbar-nav ms-auto mb-2 mb-lg-0">

				<%
				if (u != null) {
				%>

				<li class="nav-item dropdown"><a
					class="nav-link dropdown-toggle active" href="#" role="button"
					data-bs-toggle="dropdown"> <i class="fa-solid fa-circle-user"></i>
						<%=u.getName()%>

				</a>

					<ul class="dropdown-menu dropdown-menu-end shadow">

						<li><span class="dropdown-item-text"> <i
								class="fa-solid fa-user"></i> Welcome, <strong><%=u.getName()%></strong>

						</span></li>

						<li><hr class="dropdown-divider"></li>

						<li><a class="dropdown-item text-danger" href="Logout"> <i
								class="fa-solid fa-right-from-bracket"></i> Logout

						</a></li>

					</ul></li>

				<%
				} else {
				%>

				<li class="nav-item"><a class="nav-link active"
					href="signin.jsp"> <i
						class="fa-solid fa-arrow-right-to-bracket"></i> Sign In
				</a></li>

				<li class="nav-item"><a class="nav-link active"
					href="signup.jsp"> <i class="fa-solid fa-user-plus"></i> Sign
						Up
				</a></li>

				<%
				}
				%>

			</ul>

		</div>
	</div>
</nav>