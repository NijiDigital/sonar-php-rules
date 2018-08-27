<?php

    $value = $_GET['param1'];                                               // NOK {{Do not use $_GET directly. Prefer using the filter_input() function.}}
    $query_string = "/var/www/html/images/".$_GET['image_name']."png";      // NOK {{Do not use $_GET directly. Prefer using the filter_input() function.}}
    $value = $_POST['param1'];                                              // NOK {{Do not use $_POST directly. Prefer using the filter_input() function.}}
    $query_string = "/var/www/html/images/".$_POST['image_name']."png";     // NOK {{Do not use $_POST directly. Prefer using the filter_input() function.}}
    $value = $_COOKIE['param1'];                                            // NOK {{Do not use $_COOKIE directly. Prefer using the filter_input() function.}}
    $query_string = "/var/www/html/images/".$_COOKIE['image_name']."png";   // NOK {{Do not use $_COOKIE directly. Prefer using the filter_input() function.}}
    $value = $_SERVER['param1'];                                            // NOK {{Do not use $_SERVER directly. Prefer using the filter_input() function.}}
    $query_string = "/var/www/html/images/".$_SERVER['image_name']."png";   // NOK {{Do not use $_SERVER directly. Prefer using the filter_input() function.}}
    $value = $_ENV['param1'];                                            // NOK {{Do not use $_ENV directly. Prefer using the filter_input() function.}}
    $query_string = "/var/www/html/images/".$_ENV['image_name']."png";   // NOK {{Do not use $_ENV directly. Prefer using the filter_input() function.}}
