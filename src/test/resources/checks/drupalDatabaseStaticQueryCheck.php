<?php

$query = $connection->query('SELECT nid, title FROM {node}');                                               // OK
$query = $connection->query("SELECT t.s FROM {table} t WHERE t.field IN (:users)",  [':users' => $users]);  // OK
$query = db_query('SELECT nid, title FROM {node}');                                                         // OK
$query = db_query("SELECT t.s FROM {table} t WHERE t.field IN (:users)",  [':users' => $users]);            // OK

$query = $connection->query("SELECT nid, title FROM {node} WHERE user = $name");        // NOK {{Potential SQL Injection.}}
$query = $connection->query("SELECT nid, title FROM {node} WHERE user = ".$name);       // NOK
$query = $connection->query($sql_str);                                                  // NOK
$query = db_query("SELECT nid, title FROM {node} WHERE user = $name");                  // NOK
$query = db_query("SELECT nid, title FROM {node} WHERE user = ".$name);                 // NOK
$query = db_query($sql_str);                                                            // NOK
