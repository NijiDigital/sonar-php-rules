<?php


$search = filter_input(INPUT_GET, 'search');    // NOK {{filter_input() should not be used with FILTER_DEFAULT filter option.}}
$search = filter_input(INPUT_GET, 'search', FILTER_DEFAULT);    // NOK {{filter_input() should not be used with FILTER_DEFAULT filter option.}}
$search = filter_input(INPUT_GET, 'search', FILTER_UNSAFE_RAW); // NOK {{filter_input() should not be used with FILTER_UNSAFE_RAW filter option.}}
