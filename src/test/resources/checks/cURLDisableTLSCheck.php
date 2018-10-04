<?php

$verify_host_on = '2'; // OK
curl_setopt($curl, CURLOPT_SSL_VERIFYHOST, $verify_host_on);
curl_setopt($curl, CURLOPT_SSL_VERIFYHOST, '2'); // OK; default value is 2 to "check the existence of a common name and also verify that it matches the hostname provided" according to PHP's documentation

$verify_peer_on = TRUE; // OK; default value is TRUE
curl_setopt($curl, CURLOPT_SSL_VERIFYPEER, $verify_peer_on);
curl_setopt($curl, CURLOPT_SSL_VERIFYPEER, '1'); // OK

$verify_host_off = FALSE; // NOK {{Change this code to enable trust chain verification.}}
curl_setopt($curl, CURLOPT_SSL_VERIFYHOST, $verify_host_off);
curl_setopt($curl, CURLOPT_SSL_VERIFYHOST, TRUE); // NOK; TRUE is casted to 1 which is not a secure configuration
curl_setopt($curl, CURLOPT_SSL_VERIFYHOST, '0'); // NOK

$verify_peer_off = '0'; // NOK
curl_setopt($curl, CURLOPT_SSL_VERIFYPEER, FALSE); // NOK
curl_setopt($curl, CURLOPT_SSL_VERIFYPEER, $verify_peer_off);