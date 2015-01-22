<?php
//Copyright (c) 2014 sena. All rights reserved.
include 'conf.php';
include 'db_conf.php';
$php_file_path = __FILE__;
$cut_before = 14;
$cut_after = 4;
$now = date("Y-m-d H:i:s", time());
$db_time;
$db_time_str;
$posted_str = file_get_contents("php://input");
$json;

class Figure
{
    public $r = 10;
    public $g = 10;
    public $b = 10;
    public $a = 10;
    public $varX = array();
    public $varY = array();
}
$fig = array();

try {
    $pdo = new PDO($dsn,$user,$password,
    array(PDO::ATTR_EMULATE_PREPARES => false));
    if ($posted_str != "") {
        $stmt = $pdo -> prepare("INSERT INTO touch_and_post (str,created_time,updated_time) VALUES ('".$posted_str."','".$now."','".$now."')");
        $stmt->execute();
    }
    
    $pdo = null;
    $stmt = null;

} catch (Exception $e) {
    echo $e->getMessage();
}

if ($posted_str == "") {
    try {
        $pdo = new PDO($dsn,$user,$password,
        array(PDO::ATTR_EMULATE_PREPARES => false));
        $sql = 'select str from touch_and_post where id = (SELECT MAX(id) from touch_and_post)';
        $stmt = $pdo->query($sql);
        $flag = $stmt->execute();
        if (!$flag) {   
            $info = $stmt->errorInfo();
        }
        while ($data = $stmt->fetch(PDO::FETCH_ASSOC)) {
            $json = $data['str'];
        }

        $sql2 = 'select updated_time from touch_and_post where id = (SELECT MAX(id) from touch_and_post)';
        $stmt2 = $pdo->query($sql2);
        $flag2 = $stmt2->execute();
        if (!$flag2) {   
            $info2 = $stmt2->errorInfo();
        }
        while ($data2 = $stmt2->fetch(PDO::FETCH_ASSOC)) {
            $db_time = $data2['updated_time'];
        }

    $pdo = null;
    $stmt = null;
    $stmt2 = null;

    } catch (Exception $e) {
    echo $e->getMessage();
    }
}

$decoded_json = json_decode($json);

$ii = 0;
foreach ($decoded_json->fig as $val) {
    $fig[$ii] = new Figure();
    $fig[$ii]->r = $decoded_json->fig[$ii]->r;
    $fig[$ii]->g = $decoded_json->fig[$ii]->g;
    $fig[$ii]->b = $decoded_json->fig[$ii]->b;
    $fig[$ii]->a = $decoded_json->fig[$ii]->a;
    
    $jj = 0;
    foreach ($decoded_json->fig[$ii]->varXY as $val) {
        $fig[$ii]->varX[$jj] = $decoded_json->fig[$ii]->varXY[$jj]->varX;
        $fig[$ii]->varY[$jj] = $decoded_json->fig[$ii]->varXY[$jj]->varY;
        $jj++;
    }
    $ii++;
}

try {
    $pdo = new PDO($dsn,$user,$password,
    array(PDO::ATTR_EMULATE_PREPARES => false));

    $cid = 10;

    $ii = 0;
    foreach ($fig as $val) {
        $jj = 0;
        foreach ($fig[$ii]->varX as $val) {
            $stmt = $pdo -> prepare("INSERT INTO canvas (cid,lid,x,y,created_time,updated_time) VALUES (".$cid.",".$ii.",".$fig[$ii]->varX[$jj].",".$fig[$ii]->varY[$jj].",'".$now."','".$now."')");
            $stmt->execute();
            $jj++;
        }
        $ii++;
    }

    $pdo = null;
    $stmt = null;

} catch (Exception $e) {
    echo $e->getMessage();
}

$ii = 0;
foreach ($decoded_json->fig as $val) {
    $fig[$ii] = new Figure();
    $fig[$ii]->r = $decoded_json->fig[$ii]->r;
    $fig[$ii]->g = $decoded_json->fig[$ii]->g;
    $fig[$ii]->b = $decoded_json->fig[$ii]->b;
    $fig[$ii]->a = $decoded_json->fig[$ii]->a;
    
    $jj = 0;
    foreach ($decoded_json->fig[$ii]->varXY as $val) {
        $fig[$ii]->varX[$jj] = $decoded_json->fig[$ii]->varXY[$jj]->varX;
        $fig[$ii]->varY[$jj] = $decoded_json->fig[$ii]->varXY[$jj]->varY;
        $jj++;
    }
    $ii++;
}

$js_string = "";
$js_string_move = "";
$js_string_ran1 = "";
$js_string_ran2 = "";
$js_string_ran3 = "";
$js_sound = "";
$js_sound_noize = "";

if ($posted_str == "" ) {
    $ii = 0;
    foreach ($fig as $val) {
        $js_string = $js_string."ctx.beginPath();";
        $js_string_move = $js_string_move."ctx.beginPath();";
        $js_string_ran1 = $js_string_ran1."ctx.beginPath();";
        $js_string_ran2 = $js_string_ran2."ctx.beginPath();";
        $js_string_ran3 = $js_string_ran3."ctx.beginPath();";
        $jj = 0;
        foreach ($fig[$ii]->varX as $val) {
            if (strtotime($now) < strtotime($db_time) + 11) {
                $js_string = $js_string."ctx.lineTo(".$fig[$ii]->varX[$jj].",".$fig[$ii]->varY[$jj].");";
                $js_string_move = $js_string_move."ctx.lineTo(".$fig[$ii]->varX[$jj].",".$fig[$ii]->varY[$jj]."+in_y);";
                $js_sound = "document.getElementById('SonarSound').play();";
            } else {
                $js_string = $js_string."ctx.lineTo(".$fig[$ii]->varX[$jj].",".$fig[$ii]->varY[$jj].");";
                $js_string_ran1 = $js_string_ran1."ctx.lineTo(".$fig[$ii]->varX[$jj]."+(Math.random()-0.5)*(4500-in_y)*0.03,".$fig[$ii]->varY[$jj]."+(Math.random()-0.5)*(4500-in_y)*0.03);";
                $js_string_ran2 = $js_string_ran2."ctx.lineTo(".$fig[$ii]->varX[$jj]."+(Math.random()-0.5)*(4500-in_y)*0.03,".$fig[$ii]->varY[$jj]."+(Math.random()-0.5)*(4500-in_y)*0.03);";
                $js_string_ran3 = $js_string_ran3."ctx.lineTo(".$fig[$ii]->varX[$jj]."+(Math.random()-0.5)*(4500-in_y)*0.03,".$fig[$ii]->varY[$jj]."+(Math.random()-0.5)*(4500-in_y)*0.03);";
                $js_sound_noize = "document.getElementById('NoizeSound').play();";
            }
            $jj++;
        }
        $js_string = $js_string."ctx.stroke();";
        $js_string_move = $js_string_move."ctx.stroke();";
        $js_string_ran1 = $js_string_ran1."ctx.stroke();";
        $js_string_ran2 = $js_string_ran2."ctx.stroke();";
        $js_string_ran3 = $js_string_ran3."ctx.stroke();";
        $ii++;
    }
}


$smarty->assign('hoge', 'hogehoge');
$smarty->assign('js_string', $js_string);
$smarty->assign('js_string_move', $js_string_move);
$smarty->assign('js_string_ran1', $js_string_ran1);
$smarty->assign('js_string_ran2', $js_string_ran2);
$smarty->assign('js_string_ran3', $js_string_ran3);
$smarty->assign('js_sound', $js_sound);
$smarty->assign('js_sound_noize', $js_sound_noize);
$smarty->assign('db_time', $db_time);
$smarty->assignByRef('fig', $fig);

$php_file_name = substr( $php_file_path , $cut_before , strlen($php_file_path)-$cut_before );
$file_name = substr( $php_file_name , 0 , strlen($php_file_name)-$cut_after );
$tpl_file_name = $file_name.".tpl";
$smarty->assign( 'title', $file_name );
$smarty->display( $tpl_file_name );
?>