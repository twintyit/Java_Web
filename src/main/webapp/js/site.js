document.addEventListener("submit", e => {
    const form = e.target;
    if (form.id === "signup-form") {
        e.preventDefault();

        // Очищаем предыдущие ошибки
        document.querySelectorAll('.error-message').forEach(span => {
            span.textContent = '';
        });

        const formData = new FormData(form);

        fetch(form.action, {
            method: 'POST',
            body: formData
        }).then(r => r.json())
            .then(data => {
                if (data.status === "Error") {
                    displayErrors(data.data);  // Выводим ошибки
                } else {
                    console.log("Successful registration:", data);
                }
            });
    }
});

function displayErrors(errors) {
    for (let field in errors) {
        const errorElement = document.getElementById(`error-${field}`);
        if (errorElement) {
            errorElement.textContent = errors[field];
        }
    }
}

const currentUrl = window.location.pathname;

document.querySelectorAll('nav div ul li a').forEach(link => {
    if(link.getAttribute('href') === currentUrl) {
        link.parentElement.classList.add('active');
    }
});

