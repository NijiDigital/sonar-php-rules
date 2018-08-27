<?php

$obj1 = unserialize($a);                    // NOK {{Potential object injection.}}
$obj2 = unserialize($_POST['content']);     // NOK
$data = serialize($a);                      // OK
