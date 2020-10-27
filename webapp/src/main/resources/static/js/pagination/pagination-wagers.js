$(document).ready(() => {
    changePageAndSize();
});

changePageAndSize = () => {
    $('#pageSizeSelect').change(evt => {
        window.location.replace(`/user/wagers/?pageSize=${evt.target.value}&page=1`);
    });
};
