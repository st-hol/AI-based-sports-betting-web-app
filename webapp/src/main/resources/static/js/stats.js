$(document).ready(function () {
    $('#srch_a').click(function() {
        console.log('click');

        const search = $('#srch').val();
        console.log('search' + search);

        if ('URLSearchParams' in window) {
            var searchParams = new URLSearchParams(window.location.search);
            searchParams.set("search", search);
            window.location.search = searchParams.toString();
        } else {
            window.location.href = window.location.href.slice(0,  window.location.href.indexOf('?')+1) + "?search=" + search;
        }

    });
    $('#srch_a_me').click(function() {
        console.log('click');

        const search = "findme";
        console.log('search' + search);

        if ('URLSearchParams' in window) {
            var searchParams = new URLSearchParams(window.location.search);
            searchParams.set("search", search);
            window.location.search = searchParams.toString();
        }else {
            window.location.href = window.location.href.slice(0,  window.location.href.indexOf('?')+1) + "?search=" + search;
        }
    });
    $('#srch_a_all').click(function() {
        console.log('click');

        const search = "";
        console.log('search' + search);

        if ('URLSearchParams' in window) {
            var searchParams = new URLSearchParams(window.location.search);
            searchParams.set("search", search);
            window.location.search = searchParams.toString();
        }else {
            window.location.href = window.location.href.slice(0,  window.location.href.indexOf('?')+1) + "?search=" + search;
        }
    });

});


// const search = $('#srch').val();
// if (search !== "") {
//     $.ajax({
//         data: {search: search},
//         type: 'GET',
//         url: '/user/stats'
//     })
//         .done(function (result) {
//             if (result.error) {
//                 console.log(result.error);
//             } else {
//                 console.log(result);
//             }
//         });
//
//     if (event) {
//         event.preventDefault();
//     }
// }
