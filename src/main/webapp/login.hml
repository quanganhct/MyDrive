<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	<meta name="description" content="Xenon Boostrap Admin Panel" />
	<meta name="author" content="" />
	
	<title>DreamTeam Drive Login</title>

	<link rel="stylesheet" href="http://fonts.googleapis.com/css?family=Arimo:400,700,400italic">
	<link rel="stylesheet" href="assets/css/fonts/linecons/css/linecons.css">
	<link rel="stylesheet" href="assets/css/fonts/fontawesome/css/font-awesome.min.css">
	<link rel="stylesheet" href="assets/css/bootstrap.css">
	<link rel="stylesheet" href="assets/css/xenon-core.css">
	<link rel="stylesheet" href="assets/css/xenon-forms.css">
	<link rel="stylesheet" href="assets/css/xenon-components.css">
	<link rel="stylesheet" href="assets/css/xenon-skins.css">
	<link rel="stylesheet" href="assets/css/custom.css">

	<script src="assets/js/jquery-1.11.1.min.js"></script>

	<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
	<!--[if lt IE 9]>
		<script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
		<script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
	<![endif]-->
	
	
</head>
<body class="page-body login-page login-light">

	
	<div class="login-container">
	
		<div class="row">
		
			<div class="col-sm-6 col-md-offset-3">
			
				
				<!-- Errors container -->
				<div class="errors-container">
				
									
				</div>
				
				<!-- Add class "fade-in-effect" for login form effect -->
				<form method="post" role="form" id="login" class="login-form fade-in-effect">
					
					<div class="login-header">
						<a href="index.html" class="logo">
							<!--<img src="assets/images/logo-white-bg@2x.png" alt="" width="80" />-->
							<h1>DT Drive <span>log in</span></h1>
						</a>
						
						<p>Dear user, log in to access to your Drive!</p>
					</div>
	
					
					<div class="form-group">
						<a href="#" class="btn btn-primary btn-block text-left google">
							<i class="fa-google-plus"></i>
							Log In with Google
						</a>
					</div>
					
					<div class="login-footer">
						
						<div class="info-links">
							<a href="#">ToS</a> -
							<a href="#">Privacy Policy</a>
						</div>
						
					</div>
					
				</form>
				
			
				
			</div>
			
		</div>
		
	</div>

	<!-- Bottom Scripts -->
	<script src="assets/js/bootstrap.min.js"></script>
	<script src="assets/js/TweenMax.min.js"></script>
	<script src="assets/js/resizeable.js"></script>
	<script src="assets/js/joinable.js"></script>
	<script src="assets/js/xenon-api.js"></script>
	<script src="assets/js/xenon-toggles.js"></script>
	<script src="assets/js/jquery-validate/jquery.validate.min.js"></script>
	<script src="assets/js/toastr/toastr.min.js"></script>


	<!-- JavaScripts initializations and stuff -->
	<script src="assets/js/xenon-custom.js"></script>
	<script type="text/javascript">
	$( document ).ready(function(){
		   	setTimeout(function(){ $(".fade-in-effect").addClass('in'); }, 1);
						
						
						// Set Form focus
						$("form#login .form-group:has(.form-control):first .form-control").focus();

		  	$(".google").on("click",function(e){
		  		e.preventDefault();
					//$('#myModal').modal('show');
					$.get("authurl",function(res){
						console.log(res);
						window.location=res.url;
					});
				return false;
			});
	});
	</script>

</body>
</html>