console.log("Script works");

const currentUrl = window.location.pathname;

document.querySelectorAll('nav div ul li a').forEach(link => {
    if(link.getAttribute('href') === currentUrl) {
        link.parentElement.classList.add('active');
    }
});