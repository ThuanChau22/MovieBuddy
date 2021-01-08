// Load previous selected option on reload
function loadSelectedOption(defaultId, selectId, optionValue) {
    if (optionValue != "") {
        $(defaultId).removeAttr("selected");
        $(selectId).val(optionValue);
    }
}

// Submit a form
function submitForm(formId) {
    $(formId).submit();
}