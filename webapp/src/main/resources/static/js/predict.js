$(document).ready(function () {
    $('#displayAfterSportTypeChosen').css({'display': 'none'});
    const sportType = $('#sportType');
    if (sportType.val() !== "") {
        processSelectChange(null);
    }
    $(sportType.on('change', function (event) {
        processSelectChange(event);
    }));

});

function processSelectChange(event) {
    const sportType = $('#sportType').val();
    if (sportType !== "") {
        $.ajax({
            data: {sportType: sportType},
            type: 'GET',
            url: '/admin/get-teams-by-sport'
        })
            .done(function (result) {
                if (result.error) {
                    console.log(result.error);
                } else {

                    if (result.length !== 0) {
                        $('#errorAlert').hide();
                        $('#displayAfterSportTypeChosen').css({'display': 'block'});

                        var $dropdown1 = $("#dropdownTeams1");
                        var $dropdown2 = $("#dropdownTeams2");
                        $.each(result, function () {
                            $dropdown1.append($("<option />").val(this.name).text(this.name));
                            $dropdown2.append($("<option />").val(this.name).text(this.name));
                        });
                    } else {
                        $("#dropdownTeams1")
                            .find('option')
                            .remove()
                            .end();
                        $("#dropdownTeams2")
                            .find('option')
                            .remove()
                            .end();

                        $('#errorAlert').text("No records found for given Sport Type").show();
                        // $('#successAlert').hide();
                        // alert("No records found for given Sport Type");
                        $('#displayAfterSportTypeChosen').css({'display': 'none'});
                    }

                }

            });

        if (event) {
            event.preventDefault();
        }
    }
}


// // Assign handlers immediately after making the request,
// // and remember the jqxhr object for this request
// var jqxhr = $.get( "example.php", function() {
//     alert( "success" );
// })
//     .done(function() {
//         alert( "second success" );
//     })
//     .fail(function() {
//         alert( "error" );
//     })
//     .always(function() {
//         alert( "finished" );
//     });
//
// // Perform other work here ...
//
// // Set another completion function for the request above
// jqxhr.always(function() {
//     alert( "second finished" );
// });