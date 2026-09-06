<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>

<html>

<head>

    <meta charset="UTF-8">

    <title>FinTrack</title>

    <!-- Common CSS -->
    <%@ include file="common/cssLinks.jsp" %>

    <!-- Index Page CSS -->
    <link rel="stylesheet" href="css/index.css">

</head>


<body>


    <!-- =================================================
         FINTRACK SPLASH SCREEN
         ================================================= -->

    <div id="fintrackSplash">

        <img
            src="images/fintrack-logo.png"
            alt="FinTrack Logo"
            class="fintrack-splash-logo">

        <div class="fintrack-splash-title">
            FinTrack
        </div>

        <div class="fintrack-splash-subtitle">
            Track Today, Save Tomorrow
        </div>

        <div class="fintrack-loading"></div>

    </div>



    <!-- =================================================
         MAIN WEBSITE
         ================================================= -->

    <div id="fintrackMain">


        <!-- =================================================
             NAVBAR
             ================================================= -->

        <%@ include file="common/navbar.jsp" %>



        <!-- =================================================
             HERO SECTION
             ================================================= -->

        <section class="fintrack-hero">


            <!-- Background decorative circles -->

            <div class="hero-circle hero-circle-one"></div>

            <div class="hero-circle hero-circle-two"></div>



            <div class="container">


                <div class="row align-items-center">


                    <!-- =====================================
                         LEFT SIDE
                         ===================================== -->

                    <div class="col-lg-5 hero-content">


                        <div class="hero-badge">

                            <i class="fa-solid fa-wallet"></i>

                            Smart Personal Finance

                        </div>


                        <h1>

                            Take Control of

                            <span>Your Money</span>

                        </h1>


                        <p>

                            Track your expenses, manage your budget,
                            and understand your spending habits —
                            all in one simple and powerful platform.

                        </p>


                        <div class="hero-buttons">


                            <a href="signup.jsp"
                               class="btn hero-primary-btn">

                                <i class="fa-solid fa-rocket"></i>

                                Get Started

                            </a>


                            <a href="signin.jsp"
                               class="btn hero-secondary-btn">

                                Sign In

                                <i class="fa-solid fa-arrow-right"></i>

                            </a>


                        </div>


                        <div class="hero-features">


                            <div>

                                <i class="fa-solid fa-check"></i>

                                Expense Tracking

                            </div>


                            <div>

                                <i class="fa-solid fa-check"></i>

                                Budget Management

                            </div>


                            <div>

                                <i class="fa-solid fa-check"></i>

                                Financial Analytics

                            </div>


                        </div>


                    </div>



                    <!-- =====================================
                         RIGHT SIDE - SCREENSHOT
                         ===================================== -->

                    <div class="col-lg-7">


                        <div class="hero-dashboard">


                            <!-- Glow -->

                            <div class="dashboard-glow"></div>


                            <!-- Screenshot container -->

                            <div class="screenshot-window">


                                <div class="window-header">


                                    <div class="window-dots">

                                        <span></span>
                                        <span></span>
                                        <span></span>

                                    </div>


                                    <div class="window-title">

                                        FinTrack Dashboard

                                    </div>


                                </div>


                                <div class="screenshot-slider">


                                    <img
                                        src="images/img3.png"
                                        class="hero-screenshot active"
                                        alt="FinTrack Dashboard">


                                    <img
                                        src="images/img1.png"
                                        class="hero-screenshot"
                                        alt="FinTrack Expense Tracking">


                                    <img
                                        src="images/img2.png"
                                        class="hero-screenshot"
                                        alt="FinTrack Analytics">


                                    <img
                                        src="images/img4.png"
                                        class="hero-screenshot"
                                        alt="FinTrack Budget">


                                    <img
                                        src="images/img5.png"
                                        class="hero-screenshot"
                                        alt="FinTrack Reports">


                                </div>


                            </div>


                        </div>


                    </div>


                </div>


            </div>


        </section>



        <!-- =================================================
             FEATURE CARDS
             ================================================= -->

        <section class="fintrack-features">


            <div class="container">


                <div class="text-center feature-heading">


                    <span>

                        POWERFUL FEATURES

                    </span>


                    <h2>

                        Everything you need to manage your money

                    </h2>


                    <p>

                        Simple tools designed to help you build
                        better financial habits.

                    </p>


                </div>



                <div class="row g-4">


                    <!-- Expense -->

                    <div class="col-md-4">


                        <div class="feature-card">


                            <div class="feature-icon">

                                <i class="fa-solid fa-wallet"></i>

                            </div>


                            <h4>

                                Expense Tracking

                            </h4>


                            <p>

                                Record and organize your daily
                                expenses effortlessly.

                            </p>


                        </div>


                    </div>



                    <!-- Budget -->

                    <div class="col-md-4">


                        <div class="feature-card">


                            <div class="feature-icon">

                                <i class="fa-solid fa-chart-pie"></i>

                            </div>


                            <h4>

                                Smart Analytics

                            </h4>


                            <p>

                                Understand where your money goes
                                with clear financial insights.

                            </p>


                        </div>


                    </div>



                    <!-- Budget -->

                    <div class="col-md-4">


                        <div class="feature-card">


                            <div class="feature-icon">

                                <i class="fa-solid fa-bullseye"></i>

                            </div>


                            <h4>

                                Budget Management

                            </h4>


                            <p>

                                Set monthly budgets and stay
                                on track with your spending.

                            </p>


                        </div>


                    </div>


                </div>


            </div>


        </section>


    </div>



    <!-- =================================================
         SCREENSHOT ANIMATION
         ================================================= -->

    <script>

        document.addEventListener("DOMContentLoaded", function () {

            const screenshots =
                document.querySelectorAll(".hero-screenshot");

            let current = 0;

            setInterval(function () {

                screenshots[current].classList.remove("active");

                current++;

                if (current >= screenshots.length) {

                    current = 0;

                }

                screenshots[current].classList.add("active");

            }, 4000);

        });

    </script>


</body>

</html>