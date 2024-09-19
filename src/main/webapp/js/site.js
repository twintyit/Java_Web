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
                    displayErrors(data.data);
                } else {
                    console.log("Successful registration:", data);
                }
            });
    }
    else if( form.id === "modal-auth-form" ) {
        e.preventDefault();
        const queryString = new URLSearchParams(new FormData(form)).toString();
        fetch(`${form.action}?${queryString}`, {
            method: 'PATCH'
        }).then(data => data.json()).then( data =>{
            document.querySelectorAll('.error-message').forEach(error => error.textContent = '');

            if (data.status === 'Error') {
                for (const field in data.data) {
                    const errorElement = document.getElementById(`error-${field}`);
                    if (errorElement) {
                        errorElement.textContent = data.data[field];
                    }
                }
            } else {
                console.log("Authorization successful");
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

document.addEventListener('DOMContentLoaded', function() {
    // Materialize: init modal(s)
    M.Modal.init(
        document.querySelectorAll('.modal'), {
            opacity: 0.5,
            inDuration:	250,
            outDuration: 250,
            onOpenStart: null,
            onOpenEnd: null,
            onCloseStart: null,
            onCloseEnd:	null,
            preventScrolling: true,
            dismissible: true,
            startingTop: '4%',
            endingTop: '10%',
        });
});

