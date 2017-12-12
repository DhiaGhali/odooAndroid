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
    'purchase.order', 'search_read',array(),array('fields'=>array('id','name','date_planned','order_line','invoice_status','order_line')));
    if($res==null){
        $res['commande']="noCommande";
    }
    echo json_encode($res, JSON_PRETTY_PRINT);
   
    
?>