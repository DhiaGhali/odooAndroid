<?php
  
    include ('conf.php');
  
    //auth user info
    $email = "cm@cm.com" ; 
    $pass = "yassinhk" ; 

   /*$email = $_POST['email'];
    $pass  = $_POST['pass'];*/
    
    //auth
  
    $uid = $common->authenticate($db, $email, $pass, array());
    $result = array();
        
    $res= $models->execute_kw($db, $uid, $pass,
    'sale.order', 'search_read',array(),array('fields'=>array('id','name','order_line')));
    if($res==null){
        $res['commande']="noCommande";
    }
    echo json_encode($res, JSON_PRETTY_PRINT);
   
    
?>