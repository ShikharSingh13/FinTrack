<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>FinTrack</title>
</head>

<%@include file="common/cssLinks.jsp"%>
<body class="bg-light">

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

			<h2 class="text-center mb-4">Sign Up</h2>

			<form action="Register" method="POST">

				<div class="mb-3">
					<label class="form-label">Name</label> <input type="text"
						class="form-control" name="name" placeholder="Enter your name"
						required>
				</div>

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

					<div id="passwordStrength" class="mt-2 small"></div>
				</div>

				<!-- Password end -->


				<div class="d-grid">
					<button class="btn btn-primary">Sign Up</button>
				</div>

				<div class="text-center mt-2">
					Already have an account <a href="signin.jsp"
						class="text-decoration-none">Sign-In</a>
				</div>

			</form>

		</div>
	</div>

	<!-- Auto hide alert after 3 seconds -->
	<script>
		AOS.init();

		setTimeout(function() {
			let alert = document.getElementById("errorAlert");

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

		const passwordInput = document.getElementById("password");
		const passwordStrength = document.getElementById("passwordStrength");

		passwordInput
				.addEventListener(
						"input",
						function() {

							const password = passwordInput.value;

							if (password.length === 0) {
								passwordStrength.innerHTML = "";
								return;
							}

							let score = 0;

							// Length
							if (password.length >= 8) {
								score++;
							}

							// Lowercase
							if (/[a-z]/.test(password)) {
								score++;
							}

							// Uppercase
							if (/[A-Z]/.test(password)) {
								score++;
							}

							// Number
							if (/[0-9]/.test(password)) {
								score++;
							}

							// Special character
							if (/[^A-Za-z0-9]/.test(password)) {
								score++;
							}

							if (score <= 2) {

								passwordStrength.innerHTML = '<span class="text-danger fw-bold">Weak Password</span>';

							} else if (score === 3 || score === 4) {

								passwordStrength.innerHTML = '<span class="text-warning fw-bold">Medium Password</span>';

							} else {

								passwordStrength.innerHTML = '<span class="text-success fw-bold">Strong Password</span>';
							}

						});
	</script>

</body>
</html>