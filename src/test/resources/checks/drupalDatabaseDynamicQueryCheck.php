<?php

$query->condition('w.severity', '5', $operator);        // NOK {{Potential SQL Injection.}}
