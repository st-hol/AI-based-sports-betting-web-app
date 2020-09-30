$(document).ready(function() {
    $('#report-submit-ajax-form').submit(function (event) {
        $.ajax({
            type: 'POST',
            url : 'make-report',
            data : {
                companyName : $('#companyName').val(),
                taxpayerCode : $('#taxpayerCode').val()
            },
            success : function() {
                console.log("success!");
                // alert('success!');
                $('#ajax-succeed').text("success!");
            }
        });
        event.preventDefault();
    });
});