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
    
    $d = array();
    $d[0]= "1";
    $d[1]= "2";
    $d[2]= "3";
        
   $uid = $common->authenticate($db, $email, $pass, array());


    $ret = array();
    $ret['data']=array();
    $result = array();


    for($i =0; $i<$size;$i++){
        $res= $models->execute_kw($db, $uid, $pass,
                                  'product.product', 'search_read',array(array(array('id', '=', $d[$i]))),
                                  array('fields'=>array('id','code','name','barcode','type','description','image')));
        $result['id'] = $res[0]['id'];
        $result['desc'] = $res[0]['description'];
        $result['code'] = $res[0]['code'];
        $result['barcode'] = $res[0]['barcode'];
        $result['type'] = $res[0]['type'];
        $result['name'] = $res[0]['name'];
        $result['image'] = $res[0]['image'];
     //   array_push($ret[$i],$result);
        //print_r(json_encode($res));
        array_push($ret['data'],$result);
    }
    
    echo json_encode($ret/*,JSON_PRETTY_PRINT*/);

?>