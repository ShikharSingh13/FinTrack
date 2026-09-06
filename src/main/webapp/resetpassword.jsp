<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Reset Password - FinTrack</title>
</head>

<%@include file="common/cssLinks.jsp"%>

<body>

	<%@include file="common/navbar.jsp"%>

	<%
	String token = request.getParameter("token");

	if (token == null || token.trim().isEmpty()) {
	%>

	<div
		class="container vh-100 d-flex justify-content-center align-items-center">
		<div class="alert alert-danger text-center">Invalid password
			reset link.</div>
	</div>

	<%
	} else {
	%>

	<div
		class="container vh-100 d-flex justify-content-center align-items-center"
		data-aos="zoom-in-up" data-aos-duration="1500">

		<div class="card shadow p-4" style="width: 400px;">

			<h2 class="text-center mb-3">Reset Password</h2>

			<p class="text-muted text-center mb-4">Enter your new password
				below.</p>

			<form action="ResetPassword" method="POST">

				<!-- Hidden Token -->
				<input type="hidden" name="token" value="<%=token%>">

				<div class="mb-3">

					<label class="form-label"> New Password </label>

					<div class="position-relative">

						<input type="password" class="form-control pe-5" name="password"
							id="password" placeholder="Enter new password" required>

						<span onclick="togglePassword('password', 'eyeIcon')"
							class="position-absolute top-50 end-0 translate-middle-y me-3"
							style="cursor: pointer; color: #6c757d;"> <i id="eyeIcon"
							class="fa-regular fa-eye-slash"></i>

						</span>

					</div>

				</div>

				<div class="mb-4">

					<label class="form-label"> Confirm Password </label>

					<div class="position-relative">

						<input type="password" class="form-control pe-5"
							name="confirmPassword" id="confirmPassword"
							placeholder="Confirm new password" required> <span
							onclick="togglePassword('confirmPassword', 'confirmEyeIcon')"
							class="position-absolute top-50 end-0 translate-middle-y me-3"
							style="cursor: pointer; color: #6c757d;"> <i
							id="confirmEyeIcon" class="fa-regular fa-eye-slash"></i>

						</span>

					</div>

				</div>

				<div class="d-grid">

					<button type="submit" class="btn btn-primary">Reset
						Password</button>

				</div>

			</form>

		</div>

	</div>

	<%
	}
	%>

	<script>
		AOS.init();

		function togglePassword(passwordId, iconId) {

			const password = document.getElementById(passwordId);
			const eyeIcon = document.getElementById(iconId);

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