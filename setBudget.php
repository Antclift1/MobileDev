<?php
	require "config.php";
	$username = $_POST["username"];
	$bud1 = $_POST["bud1"];
	$bud2 = $_POST["bud2"];
	$bud3 = $_POST["bud3"];
	$bud4 = $_POST["bud4"];
	$bud5 = $_POST["bud5"];
	$bud6 = $_POST["bud6"];
	$bud7 = $_POST["bud7"];
	$bud8 = $_POST["bud8"];
	$bud9 = $_POST["bud9"];
	$bud10 = $_POST["bud10"];
	
	$mysql_qry = sprintf("UPDATE `userBudget` SET 
	`bud1` = %s, 
	`bud2` = %s,
	`bud3` = %s,
	`bud4` = %s,
	`bud5` = %s,
	`bud6` = %s,
	`bud7` = %s,
	`bud8` = %s,
	`bud9` = %s,
	`bud10` = %s 
	WHERE `Username` = '%s';",
	$bud1,
	$bud2,
	$bud3,
	$bud4,
	$bud5,
	$bud6,
	$bud7,
	$bud8,
	$bud9,
	$bud10,
	$username;
	
   mysqli_query($conn ,$mysql_qry);
   echo $mysql_qry;
}?>