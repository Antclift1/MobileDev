<?php
	require "config.php";
	$email = $_POST["email"];
   $username = $_POST["username"];
   $password = $_POST["password"];
   $budget = $_POST["budget"];
   $first = $_POST["first"];
   $last = $_POST["last"];
   $gender = $_POST["gender"];
   $dob = $_POST["dob"];
   $mysql_qry = "select email from users where Username like '$email';";

   $result = mysqli_query($conn ,$mysql_qry);
   if(mysqli_num_rows($result) > 0) {
     
     echo "Email is already being used.";
   }
   
   $mysql_qry = "select Username from users where Username like '$username';";

   $result = mysqli_query($conn ,$mysql_qry);
   if(mysqli_num_rows($result) > 0) {
     
     echo "Account Exists";
   }
   else {
	$mysql_qry = "INSERT INTO `users` (`Email`,`Username`, `Password`, `Firstname`,`Lastname`,`Gender`,`Budget`) VALUES ('$email','$username', '$password', '$first', '$last', '$gender', '$budget');";
	mysqli_query($conn ,$mysql_qry);
	echo $username;
}?>