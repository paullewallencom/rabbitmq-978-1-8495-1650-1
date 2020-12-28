<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<html>
<head>
<script src="http://cdnjs.cloudflare.com/ajax/libs/jquery/1.9.1/jquery.js"></script>

<link rel="stylesheet" href="http://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/2.3.2/css/bootstrap.css" />
<script src="http://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/2.3.2/js/bootstrap.js"></script>


<meta charset="UTF-8">

<script>
function myexecutesearch()
{
	 
	$("#results").text("wait...");
	$.ajax({
	      type: "GET",
	      url: "searchbook?bookkey="+$("#lblkey").val(),
	      // Error!
	      error: function(xhr, status, error) {
	    		$("#results").text("Error request: " +error);

	      },
        success: function(xhr, status, error) {
        	$("#results").text(xhr);

	      }
	    });
	  
}
</script>

<title>My Books search</title>
</head>
<body>
	<div class="navbar navbar-inverse navbar-fixed-top">
		<div class="navbar-inner">
			<div class="container">
				<button type="button" class="btn btn-navbar" data-toggle="collapse"
					data-target=".nav-collapse">
					<span class="icon-bar" /span> <span class="icon-bar"></span>
						<span class="icon-bar"></span>
				</button>
				<a class="brand" href="#">Rabbitmq Cookbook</a>
				<div class="nav-collapse collapse">
					<ul class="nav">
						<li class="active"><a href="#">Home</a></li>
						<li><a href="#about">About</a></li>
						<li><a href="#contact">Contact</a></li>
					</ul>
				</div>
				<!--/.nav-collapse -->
			</div>
		</div>
	</div>

	<div class="container">

		<div class="hero-unit">
			<h1>My Books search</h1>
			<p>Chapter 5</p>
			<p> RabbitMQ last status: ${rabbitmqstate} </p>
			<p>We are using JQuery,Twitter bootstrap, Spring Source and RabbitMQ</p>
			
			<p>
		</div>

		
			<div class="row">
					<div class="span2"><input id="lblkey"class="input-medium search-query" type="text" placeholder="book key (1..10)">
					</div>
				<div class="span2"><button class="btn-mini btn-primary" onclick="myexecutesearch()" type="button">Search book key..</button></div>
				
			</div>
	<div class="row">
		

<div class="span12"><h2 id="results">..</h2> </div>
			</div>
	</div>	

		
	</div>





</body>
</html>
