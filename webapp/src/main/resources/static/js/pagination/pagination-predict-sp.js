$(document).ready(() => {
    changePageAndSize();
});

changePageAndSize = () => {
    $('#pageSizeSelect').change(evt => {
        window.location.replace(`/admin/predict-sport-event/?pageSize=${evt.target.value}&page=1`);
    });
};
