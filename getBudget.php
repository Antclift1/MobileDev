<?php
	require "config.php";
	$username = $_POST["username"];	
   
	$mysql_qry = "select * from `userbudget` where `Username` like '$username'";

   $result = mysqli_query($conn ,$mysql_qry);
      if(mysqli_num_rows($result) > 0) {
     $row = $result->fetch_assoc();
     echo json_encode($row);
   }
   else {
   echo "Failed to retireve budget";

}?>