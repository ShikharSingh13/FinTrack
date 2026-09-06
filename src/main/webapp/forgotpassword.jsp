<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>FinTrack</title>
</head>

<%@include file="common/cssLinks.jsp"%>

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

	<div
		class="container vh-100 d-flex justify-content-center align-items-center"
		data-aos="zoom-in-up" data-aos-duration="1500">

		<div class="card shadow p-4" style="width: 400px;">

			<h2 class="text-center mb-3">Forgot Password?</h2>

			<p class="text-muted text-center mb-4">Enter your registered
				email address to reset your password.</p>

			<form action="ForgotPassword" method="POST">

				<div class="mb-4">

					<label class="form-label">Email</label> <input type="email"
						class="form-control" name="email"
						placeholder="Enter your registered email" required>

				</div>

				<div class="d-grid">

					<button type="submit" class="btn btn-primary">Send Reset
						Link</button>

				</div>

			</form>

			<div class="text-center mt-3">

				<a href="signin.jsp" class="text-decoration-none"> ← Back to
					Sign In </a>

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