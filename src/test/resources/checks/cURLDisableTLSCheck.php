<?php

curl_setopt($curl, CURLOPT_SSL_VERIFYHOST, 0); // NOK {{TLS trust chain verification should not be disabled.}}
curl_setopt($curl, CURLOPT_SSL_VERIFYHOST, false); // NOK
curl_setopt($curl, CURLOPT_SSL_VERIFYHOST, NULL); // NOK
curl_setopt($curl, CURLOPT_SSL_VERIFYHOST, array()); // NOK
curl_setopt($curl, CURLOPT_SSL_VERIFYHOST, ""); // NOK
curl_setopt($curl, CURLOPT_SSL_VERIFYHOST, "0"); // NOK
curl_setopt($curl, CURLOPT_SSL_VERIFYHOST, 0); // NOK
curl_setopt($curl, CURLOPT_SSL_VERIFYHOST, 0.0); // NOK

curl_setopt($curl, CURLOPT_SSL_VERIFYPEER, false); // NOK
curl_setopt($curl, CURLOPT_SSL_VERIFYPEER, false); // NOK

curl_setopt($curl, CURLOPT_SSL_VERIFYHOST, 1); // OK
curl_setopt($curl, CURLOPT_SSL_VERIFYHOST, true); // OK
curl_setopt($curl, CURLOPT_SSL_VERIFYHOST, "yes"); // OK
curl_setopt($curl, CURLOPT_SSL_VERIFYHOST, 12); // OK

curl_setopt($curl, CURLOPT_SSL_VERIFYHOST, $a); // NOK {{Potential TLS trust chain misconfiguration.}}
