<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<html>
<head>
<script src="http://cdnjs.cloudflare.com/ajax/libs/jquery/1.9.1/jquery.js"></script>
<link rel="stylesheet" href="http://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/2.3.1/css/bootstrap.css" />
<script src="http://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/2.3.1/js/bootstrap.js"></script>
<script type='text/javascript' src='https://www.google.com/jsapi'></script>
<meta charset="UTF-8">
<script type='text/javascript'>
      google.load('visualization', '1', {packages:['gauge']});
      google.setOnLoadCallback(setopt);
       var chart;
         var options;
         var data2;
      function setopt() {
         options = {
          width: 400, height: 120,
          redFrom: 90, redTo: 100,
          yellowFrom:75, yellowTo: 90,
          minorTicks: 5
        };


        
        
    


      }

  $(document).ready(function() {
	  var ws;
	   if ('WebSocket' in window) {
		   ws = new WebSocket('ws://' + window.location.host  + window.location.pathname+ "websocket");
	    }
	    else if ('MozWebSocket' in window) {
	    	ws = new MozWebSocket('ws://' + window.location.host + window.location.pathname+"websocket");
	    }
	    else {
	    	 
	    	 $("#tablebody").prepend("<tr><td> your browser doesn't support web socket </td></tr>");
	    	
	        return;
	    }
	  

	  ws.onopen = function(){
	  };
	  ws.onmessage = function(message){
	    // the JSON must be like {"UPDATETIME":"23/06/2013 22:55:32","SERVERID":"1","CPU":0,"MEM":40}!
       
        var obj = jQuery.parseJSON(message.data);
        xcpu=  obj.CPU ;
        xmem = obj.MEM;
        xupdate = obj.UPDATETIME;
        serverid = "server_id_" + obj.SERVERID;
        infoid = "info_id_" + obj.SERVERID; 
       
	    	var data = google.visualization.arrayToDataTable([
                                                          ['Label', 'Value'],
                                                          ['Memory', xmem],
                                                          ['CPU', xcpu]]);
        
       
	    if (document.getElementById(serverid)==null)
        {
          $("#tablebody").prepend("<tr><td> <div id='"+serverid+"' /><div id='"+infoid+"'> SERVER ID: " + obj.SERVERID+" Last Update: " + xupdate +"</div>  </td></tr>");
        } 

        
	    document.getElementById(infoid).innerHTML= "SERVER ID: " + obj.SERVERID+" Last Update: " + xupdate ;
	    chart = new google.visualization.Gauge(document.getElementById(serverid));
        chart.draw(data, options);
        
	  };
	  
	  function closeConnect(){
	      ws.close();
	  }
	    
  
  });
</script>

<title>My Servers Monitor</title>
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
			<h1>My Servers Monitor</h1>
			<p>Chapter 5</p>
			<p>   Initialization RabbitMQ message: ${rabbitmqstate} </p>
			<p>We are using JQuery,Google Chart,Twitter bootstrap, WebSocket,
				Tomcat, Spring Source and RabbitMQ</p>
			<p>
				<a href="/insight" class="btn btn-large btn-primary">Click if insight is enabled!</a>
			</p>
			<p>
		</div>

		<div class="span12">
			<h2>Servers</h2>
			<table id="tablelog" class="table table-striped">
				<thead>
					<tr>

						<th id="labellog">Status</th>

					</tr>
				</thead>
				<tbody id="tablebody">
					<tr>
						<td>
							<div id="chart_div"></div>
						</td>
					</tr>

				</tbody>
			</table>
		</div>

	</div>

	<hr>

	<footer>
		<p>Rabbitmq cookbook</p>
	</footer>



</body>
</html>
