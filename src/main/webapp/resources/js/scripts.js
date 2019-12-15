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

$('#encryptedFile').change(function () {
    var fileReader = new FileReader();
    fileReader.onload = function (e) {
        var data = e.target.result;
        $('#decryptedText').val(data);
    };
    fileReader.readAsText($('#encryptedFile').prop('files')[0]);
});

$('.js-submit-decrypt').on('click', function (e) {
    e.preventDefault();

    const text = $('#encryptedText').val();
    const cipherId = $('#cipher').val();
    const data = {
        cipherId: cipherId,
        text: text
    };

    //Hill
    if (cipherId == 2) {
        //TODO
        alert("Not implemented");
        return;
    }

    $.ajax({
        type: "POST",
        url: "/decrypt",
        data: JSON.stringify(data),
        contentType: "application/json",
        success: function (response) {
            $('#decryptedText').val(response);

        },
        error: function (response) {
            alert(response.responseText);
        }
    });
});

$('.js-submit-encrypt').on('click', function (e) {
    e.preventDefault();

    const text = $('#decryptedText').val();
    const cipherId = $('#cipher').val();
    const data = {
        cipherId: cipherId,
        text: text
    };

    //Affine
    if (cipherId == 0) {
        data["keyA"] = $('#keyA').val();
        data["keyB"] = $('#keyB').val();
    }
    //Caesar
    else if (cipherId == 1) {
        data["keyA"] = $('#keyA').val();
    }
    //Hill
    else if (cipherId == 2) {
        //TODO
        alert("Not implemented");
        return;
    }
    //RSA
    else if (cipherId == 3) {
        //nothing to do
    } //Transposition
    else if (cipherId == 4) {
        data["keyA"] = $('#keyA').val();
    }
    //Vernam
    else if (cipherId == 5) {
        data["keyA"] = $('#keyA').val();
    }
    //Vigenere
    else if (cipherId == 6) {
        data["keyA"] = $('#keyA').val();
    }

    $.ajax({
        type: "POST",
        url: "/encrypt",
        data: JSON.stringify(data),
        contentType: "application/json",
        success: function (response) {
            $('#encryptedText').val(response);

        },
        error: function (response) {
            alert(response.responseText);
        }
    });
});
