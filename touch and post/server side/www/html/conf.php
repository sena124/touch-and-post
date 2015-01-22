<?php
//Copyright (c) 2014 sena. All rights reserved.
ini_set( 'display_errors', 1 );
define( 'SMARTY_DIR', '/usr/local/lib/Smarty-3.1.16/libs/' );
require_once( SMARTY_DIR .'Smarty.class.php' );
$smarty = new Smarty();

$smarty->template_dir = '/var/www/smarty/templates/';
$smarty->compile_dir  = '/var/www/smarty/templates_c/';
$smarty->config_dir   = '/var/www/smarty/configs/';
$smarty->cache_dir    = '/var/www/smarty/cache/';


?>
