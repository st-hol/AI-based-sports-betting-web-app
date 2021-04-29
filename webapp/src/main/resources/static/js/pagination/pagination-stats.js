$(document).ready(() => {
    changePageAndSize();
});

changePageAndSize = () => {
    $('#pageSizeSelect').change(evt => {

        if ('URLSearchParams' in window && window.location.search.includes('search')) {
            var searchParams = new URLSearchParams(window.location.search);
            searchParams.set("search", searchParams.get('search'));
            searchParams.set("pageSize", evt.target.value);
            searchParams.set("page", 1);
            window.location.search = searchParams.toString();
        } else {
            window.location.replace(`/user/stats/?pageSize=${evt.target.value}&page=1`);
        }
    });
};
