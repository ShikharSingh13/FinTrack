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
		data-aos="zoom-in-up" data-aos-duration="2000">
		<div class="card shadow p-4" style="width: 400px;">

			<h2 class="text-center mb-4">Sign In</h2>

			<form action="Login" method="POST">

				<div class="mb-3">
					<label class="form-label">Email</label> <input type="email"
						class="form-control" name="email" placeholder="Enter your email"
						required>
				</div>
				<!-- Password -->
				<div class="mb-4">
					<label class="form-label">Password</label>

					<div class="position-relative">

						<input type="password" class="form-control pe-5" id="password"
							name="password" placeholder="Enter your password" required>

						<span onclick="togglePassword()"
							class="position-absolute top-50 end-0 translate-middle-y me-3"
							style="cursor: pointer; color: #6c757d;"> <i id="eyeIcon"
							class="fa-regular fa-eye-slash"></i>

						</span>

					</div>
				</div>

				<!-- Password end -->


				<div class="d-grid">
					<button class="btn btn-primary">Sign In</button>
				</div>

				<div class="text-center mt-2">
					<a href="forgotpassword.jsp" class="text-decoration-none">
						Forgot Password? </a>
				</div>

				<div class="text-center mt-2">
					Don't have an account <a href="signup.jsp"
						class="text-decoration-none">Sign-Up</a>
				</div>
			</form>

		</div>
	</div>

	<!-- Auto close alert -->
	<script>
		AOS.init();

		setTimeout(function() {
			let alert = document.getElementById("successAlert");
			if (alert) {
				let bsAlert = new bootstrap.Alert(alert);
				bsAlert.close();
			}
		}, 5000);

		function togglePassword() {

			const password = document.getElementById("password");
			const eyeIcon = document.getElementById("eyeIcon");

			if (password.type === "password") {
				password.type = "text";
				eyeIcon.classList.remove("fa-eye-slash");
				eyeIcon.classList.add("fa-eye");
			} else {
				password.type = "password";
				eyeIcon.classList.remove("fa-eye");
				eyeIcon.classList.add("fa-eye-slash");
			}
		}
	</script>


</body>
</html>