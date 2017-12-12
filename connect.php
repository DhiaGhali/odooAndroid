<?php
  
    include ('conf.php');
  
    //auth user info
    $email = $_POST['email'] ; 
    $pass = $_POST['password'] ; 

   /* $email = "cm@cm.com";
    $pass  ="yassinhk";*/
    
    //auth
  
    $uid = $common->authenticate($db, $email, $pass, array());
    
    $result = array();
    if($uid!=null){
        $result['auth'] = "done";
        
        $res = $models->execute_kw($db, $uid, $pass,'res.users', 'read',array($uid),
                                array('fields'=>array('name','login','sel_groups_47_48', 'sel_groups_19_20_21')));
        //check user permission 
        $result['email'] = $res[0]['login'];
        $result['name'] =$res[0]['name'];
        $result['pass'] = $pass;
        if($res[0]['sel_groups_19_20_21']==21){
            $result['vente_permission']="1";
        }else if($res[0]['sel_groups_19_20_21']==20||$res[0]['sel_groups_19_20_21']==false||$res[0]['sel_groups_19_20_21']==19){
            $result['vente_permission']="0";
        }
        if($res[0]['sel_groups_47_48']==47||$res[0]['sel_groups_47_48']==false){
            $result['achat_permission']="0";
        }else if($res[0]['sel_groups_47_48']==48){
            $result['achat_permission']="1";
        }
    }else{
        $result['auth'] = "failed";
    }
    
    echo json_encode($result , JSON_PRETTY_PRINT);
   
    
?>