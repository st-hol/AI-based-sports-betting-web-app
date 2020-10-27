'use strict';

var sendEmailForm = document.querySelector('#sendEmailForm');
var sendEmailLink = document.querySelector('#sendEmailLink');
var sendEmailError = document.querySelector('#sendEmailError');
var sendEmailSuccess = document.querySelector('#sendEmailSuccess');

function sendEmail() {

    var url = sendEmailLink.value;
    var xhr = new XMLHttpRequest();
    xhr.open("GET", url);


    xhr.onload = function () {

        console.log(xhr.responseText);
        var response = JSON.parse(xhr.responseText);
        if (xhr.status == 200 && response.success === true) {
            sendEmailError.style.display = "none";
            sendEmailSuccess.innerHTML = "<p>Email was sent.</p><p>TO:" + response.email + "</p>" +
                "<p> File name: " + response.fileName + "</p>";
            sendEmailSuccess.style.display = "block";

            document.querySelector(
                "#loader").style.display = "none";
            document.querySelector(
                "#loader").style.visibility = "hidden";
            document.querySelector(
                "body").style.visibility = "visible";

        } else {
            sendEmailSuccess.style.display = "none";
            sendEmailError.innerHTML = (response && response.message) || "Some Error Occurred";
        }
    }

    xhr.send();
}

sendEmailForm.addEventListener('submit', function (event) {


    document.querySelector(
        "#loader").style.display = "block";
    document.querySelector(
        "#loader").style.visibility = "visible";
    document.querySelector(
        "body").style.visibility = "hidden";
    sendEmail();
    event.preventDefault();

}, true);


// 'use strict';
//
// var sendEmailForm = document.querySelector('#sendEmailForm');
// var sendEmailLink = document.querySelector('#sendEmailLink');
// var sendEmailError = document.querySelector('#sendEmailError');
// var sendEmailSuccess = document.querySelector('#sendEmailSuccess');
//
// function sendEmail() {
//
//     var url = sendEmailLink.value;
//     var xhr = new XMLHttpRequest();
//     xhr.open("GET", url);
//
//     xhr.onreadystatechange = function() {
//         // 0   UNSENT  open() has not been called yet.
//         // 1   OPENED  send() has been called.
//         // 2   HEADERS_RECEIVED    send() has been called, and headers and status are available.
//         // 3   LOADING Downloading; responseText holds partial data.
//         // 4   DONE    The operation is complete.
//         if (xhr.readyState !== 4) {
//             document.querySelector(
//                 "body").style.visibility = "hidden";
//             document.querySelector(
//                 "#loader").style.visibility = "visible";
//         } else {
//             document.querySelector(
//                 "#loader").style.display = "none";
//             document.querySelector(
//                 "body").style.visibility = "visible";
//         }
//     };
//
//     xhr.onload = function () {
//
//         console.log(xhr.responseText);
//         var response = JSON.parse(xhr.responseText);
//         if (xhr.status == 200 && response.success === true) {
//             sendEmailError.style.display = "none";
//             sendEmailSuccess.innerHTML = "<p>Email was sent.</p><p>TO:" + response.email + "</p>" +
//                 "<p> File name: " + response.fileName + "</p>";
//             sendEmailSuccess.style.display = "block";
//         } else {
//             sendEmailSuccess.style.display = "none";
//             sendEmailError.innerHTML = (response && response.message) || "Some Error Occurred";
//         }
//     }
//
//     xhr.send();
// }
//
// sendEmailForm.addEventListener('submit', function (event) {
//
//     sendEmail();
//     event.preventDefault();
//
// }, true);


// 'use strict';
//
// var sendEmailForm = document.querySelector('#sendEmailForm');
// var sendEmailLink = document.querySelector('#sendEmailLink');
// var sendEmailError = document.querySelector('#sendEmailError');
// var sendEmailSuccess = document.querySelector('#sendEmailSuccess');
//
// function sendEmail() {
//
//     var url = sendEmailLink.value;
//     var xhr = new XMLHttpRequest();
//     xhr.open("GET", url);
//
//
//     xhr.onload = function () {
//
//         console.log(xhr.responseText);
//         var response = JSON.parse(xhr.responseText);
//         if (xhr.status == 200 && response.success === true) {
//             sendEmailError.style.display = "none";
//             sendEmailSuccess.innerHTML = "<p>Email was sent.</p><p>TO:" + response.email + "</p>" +
//                 "<p> File name: " + response.fileName + "</p>";
//             sendEmailSuccess.style.display = "block";
//
//             document.querySelector(
//                 "#loader").style.display = "none";
//             document.querySelector(
//                 "#loader").style.visibility = "hidden";
//
//         } else {
//             sendEmailSuccess.style.display = "none";
//             sendEmailError.innerHTML = (response && response.message) || "Some Error Occurred";
//         }
//     }
//
//     xhr.send();
// }
//
// sendEmailForm.addEventListener('submit', function (event) {
//
//
//     document.querySelector(
//         "#loader").style.display = "block";
//     document.querySelector(
//         "#loader").style.visibility = "visible";
//     sendEmail();
//     event.preventDefault();
//
// }, true);


// 'use strict';
//
// var sendEmailForm = document.querySelector('#sendEmailForm');
// var sendEmailLink = document.querySelector('#sendEmailLink');
// var sendEmailError = document.querySelector('#sendEmailError');
// var sendEmailSuccess = document.querySelector('#sendEmailSuccess');
//
// function sendEmail() {
//
//     var url = sendEmailLink.value;
//     var xhr = new XMLHttpRequest();
//     xhr.open("GET", url);
//
//     xhr.onreadystatechange = function() {
//         // 0   UNSENT  open() has not been called yet.
//         // 1   OPENED  send() has been called.
//         // 2   HEADERS_RECEIVED    send() has been called, and headers and status are available.
//         // 3   LOADING Downloading; responseText holds partial data.
//         // 4   DONE    The operation is complete.
//         if (xhr.readyState !== 4) {
//             document.querySelector(
//                 "body").style.visibility = "hidden";
//             document.querySelector(
//                 "#loader").style.visibility = "visible";
//         } else {
//             document.querySelector(
//                 "#loader").style.display = "none";
//             document.querySelector(
//                 "body").style.visibility = "visible";
//         }
//     };
//
//     xhr.onload = function () {
//
//         console.log(xhr.responseText);
//         var response = JSON.parse(xhr.responseText);
//         if (xhr.status == 200 && response.success === true) {
//             sendEmailError.style.display = "none";
//             sendEmailSuccess.innerHTML = "<p>Email was sent.</p><p>TO:" + response.email + "</p>" +
//                 "<p> File name: " + response.fileName + "</p>";
//             sendEmailSuccess.style.display = "block";
//         } else {
//             sendEmailSuccess.style.display = "none";
//             sendEmailError.innerHTML = (response && response.message) || "Some Error Occurred";
//         }
//     }
//
//     xhr.send();
// }
//
// sendEmailForm.addEventListener('submit', function (event) {
//
//     sendEmail();
//     event.preventDefault();
//
// }, true);
