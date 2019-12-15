$('#cipher').change(function () {
    const cipherId = this.value;
    $keyA = $('#keyA');
    $keyB = $('#keyB');
    $keyAContainer = $keyA.closest("div").closest("div");
    $keyBContainer = $keyB.closest("div").closest("div");

    //Affine
    if (cipherId == 0) {
        $keyA.prop("required", true);
        $keyB.prop("required", true);
        $keyAContainer.removeClass("invisible");
        $keyBContainer.removeClass("invisible");
    }
    //Caesar
    else if (cipherId == 1) {
        $keyA.prop("required", true);
        $keyB.prop("required", false);
        $keyAContainer.removeClass("invisible");
        $keyBContainer.addClass("invisible");
    }
    //Hill
    else if (cipherId == 2) {
        //TODO
        alert("Not implemented");
    }
    //RSA
    else if (cipherId == 3) {
        $keyA.prop("required", false);
        $keyB.prop("required", false);
        $keyAContainer.addClass("invisible");
        $keyBContainer.addClass("invisible");
    } //Transposition
    else if (cipherId == 4) {
        $keyA.prop("required", true);
        $keyB.prop("required", false);
        $keyAContainer.removeClass("invisible");
        $keyBContainer.addClass("invisible");
    }
    //Vernam
    else if (cipherId == 5) {
        $keyA.prop("required", true);
        $keyB.prop("required", false);
        $keyAContainer.removeClass("invisible");
        $keyBContainer.addClass("invisible");
    }
    //Vigenere
    else if (cipherId == 6) {
        $keyA.prop("required", true);
        $keyB.prop("required", false);
        $keyAContainer.removeClass("invisible");
        $keyBContainer.addClass("invisible");
    }
});

$('.js-submit-encrypt').on('submit', function (e) {
    e.preventDefault();

    const text = $('#decryptedText').val();
    const cipherId = $('#cipher').val();
    const data = {};

    $.ajax({
        type: "POST",
        url: "/encrypt",
        data: JSON.stringify({
            cipherId: cipherId,
            text: text,
            offset: 2
        }),
        contentType: "application/json",
        success: function (response) {
            $('#encryptedText').val(response);

        },
        error: function (response) {
            console.log(response);
        }
    });
});
