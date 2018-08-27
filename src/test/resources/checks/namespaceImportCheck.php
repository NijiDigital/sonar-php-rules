<?php

include("config.php");          // NOK {{Use namespace importing mechanism instead include/require functions.}}
include_once("config.php");     // NOK
require("config.php");          // NOK
require_once("config.php");     // NOK

