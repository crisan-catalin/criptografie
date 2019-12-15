$('#cipher').change(function () {
   const cipherId = this.value;
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
