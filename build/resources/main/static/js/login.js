document.getElementById('loginForm').addEventListener('submit', async function (event) {
    event.preventDefault();
    const username = document.getElementById('username').value.trim().toLowerCase();
    const password = document.getElementById('password').value.trim();
    const errorMessageElement = document.getElementById('errorMessage');

    // Clear previous error message
    errorMessageElement.textContent = '';

    // Validation checks
    if (!username) {
        errorMessageElement.textContent = 'Username is required.';
        return;
    }

    if (!password) {
        errorMessageElement.textContent = 'Password is required.';
        return;
    }

    try {
        const response = await fetch('/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ username, password })
        });

        if (response.ok) {
            window.location.href = '/home';
        } else {
            errorMessageElement.textContent = await response.text();
        }
    } catch (error) {
        errorMessageElement.textContent = 'An error occurred. Please try again.';
    }
});
