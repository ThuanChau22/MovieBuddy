// Load previous selected option on reload
function loadSelectedOption(defaultId, selectId, optionValue) {
    if (optionValue != "") {
        document.getElementById(defaultId).removeAttribute("selected");
        document.getElementById(selectId).value = optionValue;
    }
}

// Submit a form
function submitForm(formId) {
    document.getElementById(formId).submit();
}