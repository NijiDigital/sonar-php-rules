<?php

`cat /etc/pwd`;            // NOK {{Potential remote code execution.}}
exec($a);                  // NOK
passthru($a);              // NOK
proc_open($a);             // NOK
popen($a);                 // NOK
shell_exec($a);            // NOK
system($a);                // NOK
pcntl_exec($a);            // NOK

class Obj {
    function exec() {
        return "exec";
    }

    function system() {
        return "system";
    }
};

$myObj = new Obj();

$myObj->exec();            // OK
$myObj->system();          // OK
