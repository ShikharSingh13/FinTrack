<%@page import="org.hibernate.Session"%>
<%@page import="com.shikhar.HbUtility"%>
<%@page import="com.shikhar.entity.Expense"%>
<%@page import="com.shikhar.entity.User"%>
<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%
User user = (User) session.getAttribute("User");

if (user == null) {
	response.sendRedirect("signin.jsp");
	return;
}

int id = Integer.parseInt(request.getParameter("id"));

Session hibSession = HbUtility.sessionFactory.openSession();

Expense expense = hibSession.get(Expense.class, id);

// Security Check
if (expense == null || !expense.getUser().getEmail().equals(user.getEmail())) {

	hibSession.close();

	session.setAttribute("msg", "Invalid Expense");
	session.setAttribute("msgType", "danger");

	response.sendRedirect("ViewExpense.jsp");
	return;
}

hibSession.close();
%>

<!DOCTYPE html>
<html>
<head>

<meta charset="UTF-8">

<title>FinTrack</title>

<%@include file="common/cssLinks.jsp"%>

<style>
body {
	background: #f4f6f9;
}

.card {
	border: none;
	border-radius: 15px;
}
</style>

</head>

<body>

	<%@include file="common/navbar.jsp"%>

	<div class="container py-4" data-aos="zoom-in-up" data-aos-duration="2000">

		<div class="row justify-content-center">

			<div class="col-lg-5">

				<div class="card shadow">

					<div class="card-body">

						<h3 class="text-center mb-4">

							<i class="fa-solid fa-pen-to-square text-primary"></i> Edit
							Expense

						</h3>

						<form action="UpdateExpense" method="post">

							<input type="hidden" name="id" value="<%=expense.getId()%>">

							<div class="mb-3">

								<label>Title</label> <input type="text" class="form-control"
									name="title" value="<%=expense.getTitle()%>" required>

							</div>

							<div class="mb-3">

								<label>Date</label> <input type="date" class="form-control"
									name="date" value="<%=expense.getDate()%>" required>

							</div>

							<div class="mb-3">

								<label>Time</label> <input type="time" class="form-control"
									name="time" value="<%=expense.getTime()%>" required>

							</div>

							<div class="mb-3">

								<label>Description</label>

								<textarea class="form-control" rows="3" name="description"
									required><%=expense.getDescription()%></textarea>

							</div>

							<div class="mb-3">

								<label>Price</label> <input type="number" step="0.01"
									class="form-control" name="price"
									value="<%=expense.getPrice()%>" required>

							</div>

							<div class="d-grid">

								<button class="btn btn-primary">

									<i class="fa-solid fa-floppy-disk"></i> Update Expense

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
	</script>

</body>

</html>