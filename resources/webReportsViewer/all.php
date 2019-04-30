<!DOCTYPE HTML>
<html>

<head>
 <link rel="stylesheet" href="all.css">
</head>

<body>
<?php


if($_GET["auth"] !== "11fat11"){
  die("not authenticated");
}

$reportsDir = '../../upload/reports';
$reports = scandir($reportsDir, SCANDIR_SORT_DESCENDING);


$all='';
foreach($reports as $report){

  if ($report !== "." && $report !== ".."){
  
    $all .= '<br>'
      . '<a href="http://danrusu.ro/reports/report.php?auth=11fat11&report=' . $report . '">'
      . $report
      . '</a>';
  }
}

echo $all;
?>

</body>
</html>

