$(window).scroll(function () {
    if ($(window).scrollTop() >= 100) {
        $('#myTopnav').addClass('topnav-downed');
        $('.sdbr-open-btn').addClass('sidebar-downed');
        $('.sidebar').addClass('sidebar-downed');
        //$('.sdbr-open-btn').css('margin-top', '-26px');
        //$('.sidebar').css('margin-top', '-26px');
        $('.my-nav-soc-li').css('display', 'none');
    } else {
        $('#myTopnav').removeClass('topnav-downed');
        $('.sdbr-open-btn').removeClass('sidebar-downed');
        $('.sidebar').removeClass('sidebar-downed');
        //$('.sdbr-open-btn').css('margin-top', '0px');
        //$('.sidebar').css('margin-top', '0px');
        $('.my-nav-soc-li').css('display', 'block');
    }
});
function myFunction() {
    var x = document.getElementById("myTopnav");
    x.classList.toggle("responsive");
}
