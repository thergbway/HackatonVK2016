<?php

/*echo '<pre>';
print_r($broken_url);
echo '</pre>';*/

// Если есть параметр в GET начинаем работу по парсингу переданных из приложения данных
if(isset($_GET) && !empty($_GET)) {
  $url = $_GET['url'];
  $broken_url = explode('&', $url);
  array_shift($broken_url);
  $params = array();
  for($i = 0; $i < count($broken_url); $i++) {
    $param = $broken_url[$i];
    $broken_param = explode('=', $param);
    $broken_param[1] = ($broken_param[1] == '') ? null : $broken_param[1];
    $params[$broken_param[0]] = $broken_param[1];
  }

  echo json_encode($params);
}

?>
