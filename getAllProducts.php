<?php

    include ('conf.php');

    $email="yassinhakiri@gmail.com";
    $pass = "yassinhk";
    //$id = "10";
    $size = 3;


   /* $email = $_POST['email'];
    $pass = $_POST['pass'];
    $size = $_POST['size'];
    $d = array();
    for($i = 0 ; $i<$size;$i++){
        $d[$i]= $_POST[$i];
    }*/

        
   $uid = $common->authenticate($db, $email, $pass, array());



        $res= $models->execute_kw($db, $uid, $pass,
                                  'product.product', 'search_read',array(),array());
      
    echo json_encode($res,JSON_PRETTY_PRINT);

?>