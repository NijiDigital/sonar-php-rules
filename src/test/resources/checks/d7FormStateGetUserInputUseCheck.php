<?php

function validateForm($form, $form_state) {
    $email = $form_state['values']; // OK
    $email = $form_state['input'];  // NOK {{Do not use the raw $form_state['input'], use $form_state['values'] instead.}}
}
