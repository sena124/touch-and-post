<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" 
  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="ja" lang="ja">
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="Content-Language" content="ja" />
    <meta http-equiv="Content-Style-Type" content="text/css" />
    <meta http-equiv="Content-Script-Type" content="text/javascript" />

    <audio id="SonarSound">
		<source src="su160.mp3" type="audio/mp3">
	</audio>

	<audio id="NoizeSound">
		<source src="noize.mp3" type="audio/mp3">
	</audio>

    <script type="text/javascript">
	
	var in_y = 7000;
	var dt = new Date();

    function init() {
 		setTimeout("location.reload()",10000);
    	setInterval("sample()",50);
    }

	function sample() {
		var canvas = document.getElementById('canvas1');
		canvas.width = 1200;
    	canvas.height = 700;

		if (canvas.getContext) {
			var ctx_back = canvas.getContext('2d');
			var ctx = canvas.getContext('2d');
			ctx_back.fillStyle = 'rgb(0,0,0)';
        	ctx_back.fillRect(0, 0, 1200, 700);
			ctx.strokeStyle = 'rgb(0,255,0)';


			{$js_sound}

			if (in_y>5000) {
				{$js_string}
				{$js_string_move}
			} else {
				{$js_sound_noize}
				{$js_string_move}
				{$js_string_ran1}
				{$js_string_ran2}
				{$js_string_ran3}
				NoizeSound.volume = (5000-in_y)*0.0002;
			}
		}
		
		if (in_y>49) {
			in_y = in_y -50;
		}
	}

	function mes() {
		var canvas = document.getElementById('canvas1');
		canvas.width = 1200;
    	canvas.height = 700;
    	if (canvas.getContext) {
			var ctx_back = canvas.getContext('2d');
			var ctx = canvas.getContext('2d');
			ctx_back.fillStyle = 'rgb(0,0,0)';
        	ctx_back.fillRect(0, 0, 1200, 700);
		}
	}

	</script>
	<title>{$title}</title>
  	</head>
  	<body onLoad="init()">
  		<center>
			<canvas id="canvas1" >
			</canvas>
		</center>
  </body>
</html>