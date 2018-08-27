<?php

class MyForm extends FormBase {
    public function validateForm(array &$form, FormStateInterface $form_state) {

        $email = $form_state->getValue('email');        // OK
        $email = $form_state->getUserInput('email');    // NOK {{Do not use the raw $form_state->getUserInput(). Use $form_state->getValue() instead.}}
    }
 }
