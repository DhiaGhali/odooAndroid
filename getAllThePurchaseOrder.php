<?php
  
    include ('conf.php');
  
    //auth user info
    $email = "yassinhakiri@gmail.com" ; 
    $pass = "yassinhk" ; 
/*
   $email = $_POST['email'];
    $pass  = $_POST['pass'];
  */  
    //auth
  
    $uid = $common->authenticate($db, $email, $pass, array());
    $result = array();
        
    $res= $models->execute_kw($db, $uid, $pass,
    'purchase.order.line', 'search_read',array(),array());

    
    echo json_encode($res, JSON_PRETTY_PRINT);
   
    
?>