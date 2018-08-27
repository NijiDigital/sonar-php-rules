<?php

curl_setopt($curl, CURLOPT_SSLVERSION, 0); // NOK {{Potential TLS misconfiguration.}}
curl_setopt($curl, CURLOPT_SSLVERSION, NULL); // NOK
curl_setopt($curl, CURLOPT_SSLVERSION, $a); // NOK
curl_setopt($curl, CURLOPT_SSLVERSION, CURL_SSLVERSION_SSLv3); // NOK

curl_setopt($curl, CURLOPT_SSL_CIPHER_LIST, NULL); // NOK
curl_setopt($curl, CURLOPT_SSL_CIPHER_LIST, $a); // NOK
curl_setopt($curl, CURLOPT_SSL_CIPHER_LIST, "AES128-SHA:RC2-CBC-MD5:KRB5-RC4-MD5:KRB5-RC4-SHA:RC4-SHA:RC4-MD5"); // NOK
