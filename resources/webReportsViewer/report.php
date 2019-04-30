<?php

if($_GET["auth"] !== "11fat11"){
  die("not authenticated");
}

$reportsDir = '../../upload/reports';


echo file_get_contents($reportsDir . "/" . $_GET["report"]);


?>