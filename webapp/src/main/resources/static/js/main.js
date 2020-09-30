'use strict';
window.onload=function() {
    var singleUploadForm = document.querySelector('#singleUploadForm');
    var singleFileUploadInput = document.querySelector('#singleFileUploadInput');
    var singleFileUploadError = document.querySelector('#singleFileUploadError');
    var singleFileUploadSuccess = document.querySelector('#singleFileUploadSuccess');

    function uploadSingleFile(file) {
        var formData = new FormData();
        formData.append("file", file);

        var xhr = new XMLHttpRequest();
        xhr.open("POST", "/admin/upload-stats");

        xhr.onload = function () {
            console.log(xhr.responseText);
            var response = JSON.parse(xhr.responseText);
            if (xhr.status == 200) {
                singleFileUploadError.style.display = "none";
                singleFileUploadSuccess.innerHTML = "<p>File Uploaded Successfully.</p><p>DownloadUrl : <a href='" + response.fileName + "' target='_blank'>" + response.fileName + "</a></p>";
                singleFileUploadSuccess.style.display = "block";
            } else {
                singleFileUploadSuccess.style.display = "none";
                singleFileUploadError.innerHTML = (response && response.message) || "Some Error Occurred";
            }
        };

        xhr.send(formData);
    }

    singleUploadForm.addEventListener('submit', function (event) {
        var files = singleFileUploadInput.files;
        if (files.length === 0) {
            singleFileUploadError.innerHTML = "Please select a file";
            singleFileUploadError.style.display = "block";
        }
        uploadSingleFile(files[0]);
        event.preventDefault();
    }, true);

};
// 'use strict';
//
// var sendEmailForm = document.querySelector('#sendEmailForm');
// var sendEmailLink = document.querySelector('#sendEmailLink');
// var sendEmailError = document.querySelector('#sendEmailError');
// var sendEmailSuccess = document.querySelector('#sendEmailSuccess');
//
// // var multipleUploadForm = document.querySelector('#multipleUploadForm');
// // var multipleFileUploadInput = document.querySelector('#multipleFileUploadInput');
// // var multipleFileUploadError = document.querySelector('#multipleFileUploadError');
// // var multipleFileUploadSuccess = document.querySelector('#multipleFileUploadSuccess');
//
// function sendEmail(file) {
//     var formData = new FormData();
//     formData.append("file", file);
//
//     var xhr = new XMLHttpRequest();
//     xhr.open("POST", "/inspector/upload-template");
//
//     xhr.onload = function () {
//         console.log(xhr.responseText);
//         var response = JSON.parse(xhr.responseText);
//         if (xhr.status == 200) {
//             sendEmailError.style.display = "none";
//             sendEmailSuccess.innerHTML = "<p>File Uploaded Successfully.</p><p>DownloadUrl : <a href='" + response.fileDownloadUri + "' target='_blank'>" + response.fileDownloadUri + "</a></p>";
//             sendEmailSuccess.style.display = "block";
//         } else {
//             sendEmailSuccess.style.display = "none";
//             sendEmailError.innerHTML = (response && response.message) || "Some Error Occurred";
//         }
//     }
//
//     xhr.send(formData);
// }
// //
// // function uploadMultipleFiles(files) {
// //     var formData = new FormData();
// //     for (var index = 0; index < files.length; index++) {
// //         formData.append("files", files[index]);
// //     }
// //
// //     var xhr = new XMLHttpRequest();
// //     xhr.open("POST", "/storing/uploadMultipleFiles");
// //
// //     xhr.onload = function () {
// //         console.log(xhr.responseText);
// //         var response = JSON.parse(xhr.responseText);
// //         if (xhr.status == 200) {
// //             multipleFileUploadError.style.display = "none";
// //             var content = "<p>All Files Uploaded Successfully</p>";
// //             for (var i = 0; i < response.length; i++) {
// //                 content += "<p>DownloadUrl : <a href='" + response[i].fileDownloadUri + "' target='_blank'>" + response[i].fileDownloadUri + "</a></p>";
// //             }
// //             multipleFileUploadSuccess.innerHTML = content;
// //             multipleFileUploadSuccess.style.display = "block";
// //         } else {
// //             multipleFileUploadSuccess.style.display = "none";
// //             multipleFileUploadError.innerHTML = (response && response.message) || "Some Error Occurred";
// //         }
// //     }
// //
// //     xhr.send(formData);
// // }
//
// sendEmailForm.addEventListener('submit', function (event) {
//     var files = sendEmailLink.files;
//     if (files.length === 0) {
//         sendEmailError.innerHTML = "Please select a file";
//         sendEmailError.style.display = "block";
//     }
//     sendEmail(files[0]);
//     event.preventDefault();
// }, true);
//
//
// // multipleUploadForm.addEventListener('submit', function (event) {
// //     var files = multipleFileUploadInput.files;
// //     if (files.length === 0) {
// //         multipleFileUploadError.innerHTML = "Please select at least one file";
// //         multipleFileUploadError.style.display = "block";
// //     }
// //     uploadMultipleFiles(files);
// //     event.preventDefault();
// // }, true);