<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<html>
<head>
<script
	src="http://cdnjs.cloudflare.com/ajax/libs/jquery/1.9.1/jquery.js"></script>

<link
	href="//netdna.bootstrapcdn.com/twitter-bootstrap/2.3.2/css/bootstrap-combined.min.css"
	rel="stylesheet">
<script
	src="//netdna.bootstrapcdn.com/twitter-bootstrap/2.3.2/js/bootstrap.min.js"></script>


<meta charset="UTF-8">

<script>


function buybook(bookid){

$("#labelinfo").text("wait...");
	$.ajax({
	      type: "GET",
	      url: "rest/buybook/"+bookid,
	      // Error!
	      error: function(xhr, status, error) {
	    		$("#labelinfo").text("Error request: " +error);

	      },
        success: function(xhr, status, error) {
        	$("#labelinfo").text(xhr);

	      }
	    });


}

   

   $(document).ready(function() {
       
        $.getJSON('/rest/books', function(data) {
      
		 var tbl_body = "";
		 $.each(data, function() {
		        var tbl_row = "";
		        $.each(this, function(k , v) {
		            tbl_row += "<td>"+v+"</td>";
		        })
		        tbl_body += "<tr>"+tbl_row+"<td><button type='button' class='btn btn-info' onclick='JavaScript:buybook("+this['id']+")'>Buy...</button></td></tr>";                 
		    })
		    $("#tablebooks tbody").html(tbl_body);
		  });

      });


</script>

<title>My rabbitmq test on the cloud</title>
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
				<a class="brand" href="#"></a>
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
			<p>Chapter 11</p>
			<p>We are using Cloudfoundry, JQuery,Twitter bootstrap, Spring
				Source and RabbitMQ</p>
			<p><span >"${connFactroy}" </span></p>
		</div>

		<div class="row-fluid">
			<div class="span12">
				<span class="label label-info" id="labelinfo"></span>
			</div>
			<table class="table" id="tablebooks">
				<thead>
					<tr>
						<th>Book Title</th>
						<th>Book ID</th>
						<th>Book Price</th>
						<th></th>
					</tr>
				</thead>
				<tbody>

				</tbody>
			</table>
		</div>
	</div>
</body>




</body>
</html>
