<?php

    include ('conf.php');
    
    $email="yassinhakiri@gmail.com";
    $pass = "rubyjacke";
    $barCode ="1215";


    $uid = $common->authenticate($db, $email, $pass, array());

    $result = array();
        
   
    echo json_encode($result,JSON_PRETTY_PRINT);

?>