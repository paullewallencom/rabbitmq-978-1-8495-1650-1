<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
<script src="http://cdnjs.cloudflare.com/ajax/libs/jquery/1.9.1/jquery.js"></script>
<link rel="stylesheet" href="http://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/2.3.1/css/bootstrap.css" />
<script src="http://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/2.3.1/js/bootstrap.js"></script>
<meta charset="UTF-8">

<script type="text/javascript">


  $(document).ready(function() {
	  var ws;
	   if ('WebSocket' in window) {
		   ws = new WebSocket('ws://' + window.location.hostname + ':8080/rmq/websocket');
	    }
	    else if ('MozWebSocket' in window) {
	    	ws = new MozWebSocket('ws://' + window.location.hostname + ':8080/rmq/websocket');
	    }
	    else {
	    	 
	    	 $("#tablebody").prepend("<tr><td> your browser doesn't support web socket </td></tr>");
	    	
	        return;
	    }
	  

	  ws.onopen = function(){
	  };
	  ws.onmessage = function(message){
	      $("#tablebody").prepend("<tr><td>"+message.data+ " </td></tr>");
	      var rowCount = $('#tablelog tr').length -1;
	      $("#labellog").text("Log messages, rows view count: "+ rowCount + " Max rows 20");
	      if (rowCount>=20)
	       $('#tablelog tr:last').remove();
	  };
	  
	  function closeConnect(){
	      ws.close();
	  }
	    
  
  });
</script>

<title>My RabbtiMQ Monitor</title>
</head>
<body>
    <div class="navbar navbar-inverse navbar-fixed-top">
      <div class="navbar-inner">
        <div class="container">
          <button type="button" class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
            <span class="icon-bar"/span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="brand" href="#">Rabbitmq Cookbook</a>
          <div class="nav-collapse collapse">
            <ul class="nav">
              <li class="active"><a href="#">Home</a></li>
              <li><a href="#about">About</a></li>
              <li><a href="#contact">Contact</a></li>
            </ul>
          </div><!--/.nav-collapse -->
        </div>
      </div>
    </div>

    <div class="container">

      <div class="hero-unit">
        <h1>My RabbitMQ Monitor</h1>
        <p> Chapter 3 Recipe 8</p>
        <p> We are using HTML5,Bootstrap Twitter,Google chart, WebSocket, Tomcat, Spring Source and RabbitMQ</p>
      </div>

        <div class="span12">
          <h2>RabbitMQ Logs</h2>
          <table id="tablelog" class="table table-striped">
              <thead>
                <tr>
                 
                  <th id="labellog">Log Messages</th>
                  
                </tr>
              </thead>
              <tbody id = "tablebody">
                
                
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
