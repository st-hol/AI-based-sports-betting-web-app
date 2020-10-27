$(document).ready(() => {
    changePageAndSize();
});

changePageAndSize = () => {
    $('#pageSizeSelect').change(evt => {
        window.location.replace(`/user/events/?pageSize=${evt.target.value}&page=1`);
    });
};
