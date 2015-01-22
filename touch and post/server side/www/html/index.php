<?php
include 'conf.php';


$php_file_path = __FILE__;
$cut_before = 14;
$cut_after = 4;

$php_file_name = substr( $php_file_path , $cut_before , strlen($php_file_path)-$cut_before );
$file_name = substr( $php_file_name , 0 , strlen($php_file_name)-$cut_after );
$tpl_file_name = $file_name.".tpl";
$smarty->assign( 'title', $file_name );
$smarty->display( $tpl_file_name );
?>