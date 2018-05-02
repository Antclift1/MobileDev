<?php
	require "config.php";
   $username = $_POST["username"];
   $password = $_POST["password"];
   $mysql_qry = "select * from users where Username like '$username' and Password like '$password';";

   $result = mysqli_query($conn ,$mysql_qry);
   if(mysqli_num_rows($result) > 0) {
     $row = $result->fetch_assoc();
     $data = $row['Username'];
     echo $data;
   }
   else {
   echo "Login Failed";
}?>