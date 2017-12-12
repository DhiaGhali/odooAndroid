<?php
    header('Content-Type: application/json');
    include ('ripcord/ripcord.php');

    //odoo server config
    $url = 'http://localhost:8069' ; 
    $db = 'odoo';
    $common = ripcord::client("$url/xmlrpc/2/common");
    $models = ripcord::client("$url/xmlrpc/2/object");

?>