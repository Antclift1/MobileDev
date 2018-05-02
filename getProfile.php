<?php
	require "config.php";
   $username = $_POST["username"];
   $mysql_qry = "select * from users where Username like '$username';";

   $result = mysqli_query($conn ,$mysql_qry);
   if(mysqli_num_rows($result) > 0) {
     $row = $result->fetch_assoc();
     $data = $row;
     echo json_encode($data);
   }
   else {
   echo "Failed to get Data";
}?>